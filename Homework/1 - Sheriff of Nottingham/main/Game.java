package com.tema1.main;

import com.tema1.players.*;

import java.util.ArrayList;
import java.util.List;

public final class Game
{
    private static int round;

    public static int getRound()
    {
        return round;
    }

    private static boolean neighbors(int i, int j, int n)
    {
         if(Math.abs(i - j) == 1) return true;
         if((i == 0 && j == n-1) || (i == n-1 && j == 0)) return true;
         return false;
    }

    public static void Play(final int rounds, final List<String> player_types, final List<Integer> deck)
    {
        // max 5 rounds, max 7 players
        int nr_players = player_types.size();
        List<Player> players = new ArrayList<>();
        for(int i = 0; i < nr_players; ++i)
        {
            if (player_types.get(i).equals("basic"))
                players.add(new BasePlayer());
            else if (player_types.get(i).equals("greedy"))
                players.add(new GreedyPlayer());
            else if (player_types.get(i).equals("bribed"))
                players.add(new BribePlayer());
            players.get(i).setID(i);
        }

        for(round = 1; round <= rounds; ++round)
        {
            // sheriffs
            for(int j = 0; j < nr_players; ++j)
            {
                for(int i = 0; i < nr_players; ++i)
                {
                    if(i != j)
                        players.get(i).drawCards(deck);
                }
                // bribe sheriff checks neighbors first
                if(players.get(j) instanceof BribePlayer)
                {
                    for(int i = 0; i < nr_players; ++i)
                    {
                        if(neighbors(i, j, nr_players))
                        {
                            players.get(j).inspect(players.get(i));
                            players.get(i).discardHand();
                        }
                    }
                    for(int i = 0; i < nr_players; ++i)
                    {
                        if(i != j && !neighbors(i, j, nr_players))
                        {
                            players.get(j).acceptBribe(players.get(i));
                            players.get(i).discardHand();
                        }
                    }
                }
                else
                {
                    for(int i = 0; i < nr_players; ++i)
                    {
                        if(i != j)
                        {
                            players.get(i).drawCards(deck);
                            players.get(j).inspect(players.get(i));
                            players.get(i).discardHand();
                        }
                    }
                }
            }
        }

        for(int i = 0; i < nr_players; ++i)
            players.get(i).collectIllegalBonus();
        Player.collectProfitAndLegalBonus(players);
        players.sort(Player.sortByCoins);
        for(int i = 0; i < nr_players; ++i)
            System.out.println(players.get(i).getID() + " " + players.get(i).getType() + " " + players.get(i).getCoins());
    }
}
