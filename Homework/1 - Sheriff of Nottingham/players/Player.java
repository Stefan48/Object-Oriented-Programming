package com.tema1.players;

import com.tema1.cards.Card;

import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;

public abstract class Player
{
    private int id;
    protected int coins;
    protected ArrayList<Card> cards;
    protected ArrayList<Card> collection;

    public Player()
    {
        coins = 80; // starting coins
        cards = new ArrayList<>();
        collection = new ArrayList<>();
    }

    public final int getID()
    {
        return id;
    }
    public final void setID(int id)
    {
        this.id = id;
    }
    public final int getCoins()
    {
        return coins;
    }
    public abstract String getType();

    public final void drawCards(List<Integer> deck)
    {
        for(int i = 0; i < Card.Constants.MAX_HAND_SIZE; ++i)
        {
            if(deck.isEmpty() || cards.size() == Card.Constants.MAX_HAND_SIZE)
                return;
            cards.add(new Card(deck.get(0)));
            deck.remove(0);
        }
    }

    public final void discardHand()
    {
        cards.clear();
    }

    public final void collectIllegalBonus()
    {
        int size = collection.size();
        for(int i = 0; i < size; ++i)
        {
            if(!collection.get(i).isLegal())
            {
                for(int j = 0; j < collection.get(i).getBonus().size(); j += 2)
                {
                    for(int k = 0; k < collection.get(i).getBonus().get(j); ++k)
                    {
                        collection.add(new Card(collection.get(i).getBonus().get(j + 1)));
                    }
                }
            }
        }
    }

    public static void collectProfitAndLegalBonus(List<Player> players)
    {
        int[] cnt;
        int[] king_cnt = new int[Card.Constants.MAX_LEGAL_ID];
        int[] queen_cnt = new int[Card.Constants.MAX_LEGAL_ID];
        int[] king_id = new int[Card.Constants.MAX_LEGAL_ID];
        int[] queen_id = new int[Card.Constants.MAX_LEGAL_ID];
        Card card;

        for(int i = 0; i < players.size(); ++i)
        {
            cnt = new int[Card.Constants.MAX_LEGAL_ID];
            for(int j = 0; j < players.get(i).collection.size(); ++j)
            {
                card = players.get(i).collection.get(j);
                players.get(i).coins += card.getProfit(); // collect profit
                if(card.isLegal())
                {
                    cnt[card.getID()]++;
                    if(cnt[card.getID()] > king_cnt[card.getID()])
                    {
                        if(i != king_id[card.getID()])
                        {
                            queen_id[card.getID()] = king_id[card.getID()];
                            queen_cnt[card.getID()] = king_cnt[card.getID()];
                            king_id[card.getID()] = i;
                        }
                        king_cnt[card.getID()] = cnt[card.getID()];
                    }
                    else if(cnt[card.getID()] > queen_cnt[card.getID()])
                    {
                        queen_cnt[card.getID()] = cnt[card.getID()];
                        queen_id[card.getID()] = i;
                    }
                }
            }
        }
        // collect king and queen bonus
        for(int i = 0; i < Card.Constants.MAX_LEGAL_ID; ++i)
        {
            if(king_cnt[i] != 0)
                players.get(king_id[i]).coins += new Card(i).getBonus().get(0);
            if(queen_cnt[i] != 0)
                players.get(queen_id[i]).coins += new Card(i).getBonus().get(1);
        }
    }

    public abstract List<Card> createBag();

    // multiple dispatch pattern
    public void inspect(Player other_player) {}

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

    public void acceptBribe(Player other_player) {}
    public void bribe(BribePlayer bribe_player)
    {
        bribe_player.acceptBribe(this);
    }

    // Comparator for sorting players by coins
    public static Comparator<Player> sortByCoins = new Comparator<Player>()
    {
        public int compare(Player p1, Player p2)
        {
            if(p1.coins == p2.coins)
                return p1.id - p2.id;
            // descending order
            return p2.coins - p1.coins;
        }
    };
}
