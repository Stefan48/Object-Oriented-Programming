package main;

import heroes.Hero;
import heroes.HeroFactory;
import map.Map;
import map.Position;
import fileio.FileSystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main
{
    public static void main(String[] args)
    {
        final String input_path = args[0];
        final String output_path = args[1];
        try
        {
            FileSystem fs = new FileSystem(input_path, output_path);

            // initialize map
            int map_length = fs.nextInt();
            int map_width = fs.nextInt();
            char[][] map = new char[map_length][map_width];
            for(int i = 0; i < map_length; ++i)
                map[i] = fs.nextWord().toCharArray();
            Map.setMap(map);
            // initialize heroes
            int nr_heroes = fs.nextInt();
            List<Hero> heroes = new ArrayList<>();
            char race;
            int x, y;
            for(int i = 0; i < nr_heroes; ++i)
            {
                race = fs.nextWord().charAt(0);
                x = fs.nextInt();
                y = fs.nextInt();
                heroes.add(HeroFactory.getInstance().getHero(race));
                heroes.get(i).setPosition(x, y);
            }
            // play
            int nr_rounds = fs.nextInt();
            List<PairIntPosition> positions = new ArrayList<>(); // positions of alive heroes
            for(int i = 0; i < nr_heroes; ++i)
                positions.add(new PairIntPosition(i, heroes.get(i).getPosition()));
            char[] moves;
            // rounds
            for(int r = 0; r < nr_rounds; ++r)
            {
                moves = fs.nextWord().toCharArray();
                positions = new ArrayList<>();
                for(int i = 0; i < nr_heroes; ++i)
                    if(!heroes.get(i).isDead())
                    {
                        heroes.get(i).clearDebuffs();
                        heroes.get(i).receiveDebuffEffects();
                        heroes.get(i).move(moves[i]);
                        if(!heroes.get(i).isDead())
                            positions.add(new PairIntPosition(i, heroes.get(i).getPosition()));
                    }
                Collections.sort(positions, Position.sortAscending);
                for(int i = 1; i < positions.size(); ++i)
                    if(positions.get(i).getValue().equals(positions.get(i - 1).getValue()))
                            Hero.fight(heroes.get(positions.get(i).getKey()), heroes.get(positions.get(i - 1).getKey()));
            }
            // output
            for(int i = 0; i < heroes.size(); ++i)
            {
                fs.writeWord(heroes.get(i).getClass().getSimpleName().charAt(0) + " ");
                if(heroes.get(i).isDead())
                    fs.writeWord("dead\n");
                else fs.writeWord(heroes.get(i).getLevel() + " " + heroes.get(i).getXp() + " " + heroes.get(i).getHp() + " " + heroes.get(i).getPosition() + "\n");
            }
            fs.close();
        }
        catch (Exception e1)
        {
            e1.printStackTrace();
        }
    }
}
