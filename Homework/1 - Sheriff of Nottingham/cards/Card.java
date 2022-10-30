package com.tema1.cards;

import java.util.*;

public final class Card
{
    private final int id;
    private final int profit;
    private final boolean legal;
    private final int penalty;
    private final List<Integer> bonus; /* King and Queen bonuses for legal goods,
                                        coefficients + legal cards (id) bonuses for illegal goods*/

    public static class Constants
    {
        public static final int APPLE_ID = 0;
        public static final int MAX_LEGAL_ID = 10;
        public static final int LEGAL_PENALTY = 2;
        public static final int ILLEGAL_PENALTY = 4;
        public static final int MAX_HAND_SIZE = 10;
        public static final int MAX_BAG_SIZE = 8;
    }

    public Card(int ID)
    {
        id = ID;
        if(id < Constants.MAX_LEGAL_ID)
        {
            legal = true;
            penalty = Constants.LEGAL_PENALTY;
            switch(id)
            {
                case 0: // apple
                    profit = 2;
                    bonus = Arrays.asList(20, 10);
                    break;
                case 1: // cheese
                    profit = 3;
                    bonus = Arrays.asList(19, 9);
                    break;
                case 2: // bread
                    profit = 4;
                    bonus =  Arrays.asList(18, 9);
                    break;
                case 3: // chicken
                    profit = 4;
                    bonus =  Arrays.asList(17, 8);
                    break;
                case 4: // tomato
                    profit = 3;
                    bonus =  Arrays.asList(16, 7);
                    break;
                case 5: // corn
                    profit = 2;
                    bonus =  Arrays.asList(15, 6);
                    break;
                case 6: // potato
                    profit = 3;
                    bonus =  Arrays.asList(14, 5);
                    break;
                case 7: // wine
                    profit = 5;
                    bonus =  Arrays.asList(13, 4);
                    break;
                case 8: // salt
                    profit = 2;
                    bonus =  Arrays.asList(12, 3);
                    break;
                case 9: // sugar
                    profit = 3;
                    bonus =  Arrays.asList(11, 2);
                    break;
                default:
                    profit = 0;
                    bonus = new ArrayList<>();
                    break;
            }
        }
        else
        {
            legal = false;
            penalty = Constants.ILLEGAL_PENALTY;
            switch(id)
            {
                case 20: // silk
                    profit = 9;
                    bonus =  Arrays.asList(3, 1);
                    break;
                case 21: // pepper
                    profit = 8;
                    bonus =  Arrays.asList(2, 3);
                    break;
                case 22: // barrel
                    profit = 7;
                    bonus =  Arrays.asList(2, 2);
                    break;
                case 23: // beer
                    profit = 6;
                    bonus =  Arrays.asList(4, 7);
                    break;
                case 24: // seafood
                    profit = 12;
                    bonus =  Arrays.asList(2, 4, 3, 6, 1, 3);
                    break;
                default:
                    profit = 0;
                    bonus = new ArrayList<>();
                    break;
            }
        }
    }

    public int getID()
    {
        return id;
    }
    public boolean isLegal()
    {
        return legal;
    }
    public int getProfit()
    {
        return profit;
    }
    public int getPenalty()
    {
        return penalty;
    }
    public List<Integer> getBonus()
    {
        List<Integer> copy = new ArrayList<>(bonus);
        return copy;
    }

    // Comparator for sorting a list of cards by ID
    public static Comparator<Card> sortByID = new Comparator<Card>()
    {
        public int compare(Card c1, Card c2)
        {
            // descending order
            return c2.id - c1.id;
        }
    };
    // Comparator for sorting a list of cards by profit, then by id
    public static Comparator<Card> sortByProfit = new Comparator<Card>()
    {
        public int compare(Card c1, Card c2)
        {
            // descending order
            if(c1.profit == c2.profit)
                return c2.id - c1.id;
            return c2.profit - c1.profit;
        }
    };
}
