package com.tema1.players;

import com.tema1.cards.Card;

import java.util.ArrayList;
import java.util.List;

public class BribePlayer extends Player
{
    public static final int SMALL_BRIBE = 5;
    public static final int BIG_BRIBE = 10;

    public String getType()
    {
        return "BRIBED";
    }

    public List<Card> createBag()
    {
        List<Card> hand = new ArrayList<>(cards);
        hand.sort(Card.sortByProfit);
        if(coins <= SMALL_BRIBE || hand.get(0).isLegal())
        {
            // base strategy
            int i = 0, max_id = hand.get(0).getID(); // in case he has only illegal cards
            hand.sort(Card.sortByID);
            // check illegal cards
            while(i < hand.size() && !hand.get(i).isLegal())
                i++;
            if(i < hand.size())
            {
                // has legal cards
                int frequency = 1, max_frequency = 1;
                int max_profit = hand.get(i).getProfit();
                max_id = hand.get(i).getID();
                i++;
                while(i < hand.size())
                {
                    if(hand.get(i).getID() == hand.get(i - 1).getID())
                    {
                        frequency++;
                        if(frequency > max_frequency)
                        {
                            max_frequency = frequency;
                            max_profit = hand.get(i).getProfit();
                            max_id = hand.get(i).getID();
                        }
                        else if(frequency == max_frequency)
                        {
                            if(hand.get(i).getProfit() > max_profit)
                            {
                                max_profit = hand.get(i).getProfit();
                                max_id = hand.get(i).getID();
                            }
                        }
                    }
                    else
                    {
                        frequency = 1;
                        if(frequency == max_frequency)
                        {
                            if (hand.get(i).getProfit() > max_profit)
                            {
                                max_profit = hand.get(i).getProfit();
                                max_id = hand.get(i).getID();
                            }
                        }
                    }
                    i++;
                }
                for(i = 0; i < hand.size(); ++i)
                    if(hand.get(i).getID() != max_id)
                    {
                        hand.remove(i);
                        i--;
                    }
                while(hand.size() > Card.Constants.MAX_BAG_SIZE)
                {
                    hand.remove(0);
                }
            }
            else
            {
                // has only illegal cards in hand
                hand.clear();
                if(coins >= Card.Constants.ILLEGAL_PENALTY)
                    hand.add(new Card(max_id));
            }
            return hand;
        }
        // bribe strategy
        int i = 0, necessary_coins = 0, illegal_cnt = 0;
        List<Card> bag= new ArrayList<>();
        while(i < hand.size() && bag.size() < Card.Constants.MAX_BAG_SIZE)
        {
            // Bribe player should add apples in his bag for free, but he doesn't.
            if(hand.get(i).isLegal())
            {
                if(necessary_coins == SMALL_BRIBE)
                {
                    if(coins > Card.Constants.ILLEGAL_PENALTY + Card.Constants.LEGAL_PENALTY)
                    {
                        bag.add(hand.get(i));
                        necessary_coins = Card.Constants.ILLEGAL_PENALTY + Card.Constants.LEGAL_PENALTY;
                    }
                }
                else if(necessary_coins + hand.get(i).getPenalty() < coins)
                {
                    bag.add(hand.get(i));
                    necessary_coins += hand.get(i).getPenalty();
                }
            }
            else if(!hand.get(i).isLegal())
            {
                if(illegal_cnt >= 2)
                {
                    if(necessary_coins + hand.get(i).getPenalty() < coins)
                    {
                        bag.add(hand.get(i));
                        necessary_coins += hand.get(i).getPenalty();
                        illegal_cnt++;
                    }
                }
                else if(illegal_cnt > 0)
                {
                    if(coins > 2 * Card.Constants.ILLEGAL_PENALTY)
                    {
                        bag.add(hand.get(i));
                        necessary_coins = 2 * Card.Constants.ILLEGAL_PENALTY;
                        illegal_cnt++;
                    }
                }
                else
                {
                    // first card to be added
                    bag.add(hand.get(i));
                    necessary_coins = SMALL_BRIBE;
                    illegal_cnt++;
                }
            }
            i++;
        }
        return bag;
    }

    public void acceptBribe(Player other_player)
    {
        other_player.bribe(this);
    }

    public void acceptBribe(BasePlayer other_player)
    {
        List<Card> bag = other_player.createBag();
        other_player.collection.addAll(bag);
    }

    public void acceptBribe(GreedyPlayer other_player)
    {
        List<Card> bag = other_player.createBag();
        other_player.collection.addAll(bag);
    }

    public void acceptBribe(BribePlayer other_player)
    {
        List<Card> bag = other_player.createBag();
        if(bag.isEmpty()) return;
        int illegal_cnt = 0;
        for(int i = 0; i < bag.size(); ++i)
        {
            if (!bag.get(i).isLegal())
                illegal_cnt++;
        }
        if(illegal_cnt != 0 && other_player.coins > SMALL_BRIBE)
        {
            // bribe strategy applied
            if(illegal_cnt < 3 /*illegal_count_bribe_step*/)
            {
                this.coins += SMALL_BRIBE;
                other_player.coins -= SMALL_BRIBE;
            }
            else
            {
                this.coins += BIG_BRIBE;
                other_player.coins -= BIG_BRIBE;
            }
        }
        other_player.collection.addAll(bag);
    }

    public void inspect(Player other_player)
    {
        other_player.getInspectedBy(this);
    }

    public void inspect(BasePlayer other_player)
    {
        List<Card> bag = other_player.createBag();
        if(bag.isEmpty()) return;
        if(this.coins < Card.Constants.MAX_BAG_SIZE * Card.Constants.LEGAL_PENALTY)
        {
            other_player.collection.addAll(bag);
            return;
        }
        boolean lied = false;
        for(int i = 0; i < bag.size(); ++i)
        {
            if(bag.get(i).isLegal())
                other_player.collection.add(bag.get(i));
            else
            {
                lied = true;
                this.coins += Card.Constants.ILLEGAL_PENALTY;
                other_player.coins -= Card.Constants.ILLEGAL_PENALTY;
            }
        }
        if(!lied)
        {
            this.coins -= bag.size() * Card.Constants.LEGAL_PENALTY;
            other_player.coins += bag.size() * Card.Constants.LEGAL_PENALTY;
        }
    }

    public void inspect(GreedyPlayer other_player)
    {
        List<Card> bag = other_player.createBag();
        if(bag.isEmpty()) return;
        if(this.coins < Card.Constants.MAX_BAG_SIZE * Card.Constants.LEGAL_PENALTY)
        {
            other_player.collection.addAll(bag);
            return;
        }
        boolean lied = false;
        for(int i = 0; i < bag.size(); ++i)
        {
            if(bag.get(i).isLegal())
                other_player.collection.add(bag.get(i));
            else
            {
                lied = true;
                this.coins += Card.Constants.ILLEGAL_PENALTY;
                other_player.coins -= Card.Constants.ILLEGAL_PENALTY;
            }
        }
        if(!lied)
        {
            this.coins -= bag.size() * Card.Constants.LEGAL_PENALTY;
            other_player.coins += bag.size() * Card.Constants.LEGAL_PENALTY;
        }
    }

    public void inspect(BribePlayer other_player)
    {
        List<Card> bag = other_player.createBag();
        if(bag.isEmpty()) return;
        if(this.coins < Card.Constants.MAX_BAG_SIZE * Card.Constants.LEGAL_PENALTY)
        {
            other_player.collection.addAll(bag);
            return;
        }
        boolean lied = false;
        for(int i = 0; i < bag.size(); ++i)
            if(!bag.get(i).isLegal())
            {
                lied = true;
                break;
            }
        if(!lied)
        {
            // bribe player applied base strategy
            this.coins -= bag.size() * Card.Constants.LEGAL_PENALTY;
            other_player.coins += bag.size() * Card.Constants.LEGAL_PENALTY;
            other_player.collection.addAll(bag);
        }
        else
        {
            for(int i = 0; i < bag.size(); ++i)
            {
                if(bag.get(i).getID() == Card.Constants.APPLE_ID)
                    other_player.collection.add(bag.get(i));
                else
                {
                    this.coins += bag.get(i).getPenalty();
                    other_player.coins -= bag.get(i).getPenalty();
                }
            }
        }
    }

    public void getInspectedBy(BasePlayer base_player)
    {
        base_player.inspect(this);
    }
    public void getInspectedBy(GreedyPlayer greedy_player)
    {
        greedy_player.inspect(this);
    }
    public void getInspectedBy(BribePlayer bribe_player)
    {
        bribe_player.inspect(this);
    }

    public void bribe(BribePlayer bribe_player)
    {
        bribe_player.acceptBribe(this);
    }
}
