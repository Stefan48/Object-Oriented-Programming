package com.tema1.players;

import com.tema1.cards.Card;

import java.util.ArrayList;
import java.util.List;

public final class BasePlayer extends Player
{
    public String getType()
    {
        return "BASIC";
    }

    public List<Card> createBag()
    {
        List<Card> hand = new ArrayList<>(cards);
        hand.sort(Card.sortByID);
        int i = 0, max_profit = 0, max_id = 0;
        // check illegal cards
        while(i < hand.size() && !hand.get(i).isLegal())
        {
            if(hand.get(i).getProfit() > max_profit)
            {
                max_profit = hand.get(i).getProfit();
                max_id = hand.get(i).getID();
            }
            i++;
        }
        if(i < hand.size())
        {
            // has legal cards in hand
            int frequency = 1, max_frequency = 1;
            max_profit = hand.get(i).getProfit();
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
