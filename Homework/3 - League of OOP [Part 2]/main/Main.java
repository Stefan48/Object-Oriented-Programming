package main;

import heroes.Angel;
import heroes.Hero;
import heroes.HeroFactory;
import map.Map;
import fileio.FileSystem;

import java.util.ArrayList;
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
            GameObserver game_observer = new GameObserver(fs);
            // initialize map
            int map_length = fs.nextInt();
            int map_width = fs.nextInt();
            char[][] map = new char[map_length][map_width];
            for(int i = 0; i < map_length; ++i)
                map[i] = fs.nextWord().toCharArray();
            Map.getInstance().setMap(map);
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
                heroes.get(i).setId(i);
                heroes.get(i).addObserver(game_observer);
            }
            // read moves
            int nr_rounds = fs.nextInt();
            char[][] moves = new char[nr_rounds][nr_heroes];
            for(int i = 0; i < nr_rounds; ++i)
                    moves[i] = fs.nextWord().toCharArray();
            // rounds
            for(int r = 0; r < nr_rounds; ++r)
            {
                fs.writeWord("~~ Round " + (r + 1) + " ~~\n");
                for(int i = 0; i < nr_heroes; ++i)
                    if(!heroes.get(i).isDead())
                    {
                        heroes.get(i).clearDebuffs();
                        heroes.get(i).receiveDebuffEffects();
                        heroes.get(i).move(moves[r][i]);
                        if(!heroes.get(i).isDead())
                        {
                            if(!heroes.get(i).isImpaired())
                                heroes.get(i).applyStrategy();
                        }
                    }
                // fight
                for(int i = 0; i < nr_heroes; ++i)
                {
                    if(!heroes.get(i).isDead())
                    {
                        for(int j = i + 1; j < nr_heroes; ++j)
                            if(!heroes.get(j).isDead() && heroes.get(i).getPosition().equals(heroes.get(j).getPosition()))
                                Hero.fight(heroes.get(i), heroes.get(j));
                    }
                }
                // angels
                int nr_angels = fs.nextInt();
                String angel_type;
                Angel angel;
                for(int i = 0; i < nr_angels; ++i)
                {
                    String s = fs.nextWord();
                    angel_type = "";
                    int k = 0;
                    while(s.charAt(k) != ',')
                    {
                        angel_type += s.charAt(k);
                        k++;
                    }
                    k++;
                    x = 0;
                    while(Character.isDigit(s.charAt(k)))
                    {
                        x = x * 10 + (s.charAt(k) - '0');
                        k++;
                    }
                    k++;
                    y = 0;
                    while(k < s.length() && Character.isDigit(s.charAt(k)))
                    {
                        y = y * 10 + (s.charAt(k) - '0');
                        k++;
                    }
                    angel = HeroFactory.getInstance().getAngel(angel_type);
                    angel.setPosition(x, y);
                    angel.addObserver(game_observer);
                    angel.notifyObservers(angel);
                    for(int j = 0; j < nr_heroes; ++j)
                        if(heroes.get(j).getPosition().equals(angel.getPosition()))
                            heroes.get(j).receiveAngel(angel);
                }
                fs.writeCharacter('\n');
            }
            // output
            fs.writeWord("~~ Results ~~\n");
            for(int i = 0; i < heroes.size(); ++i)
            {
                fs.writeWord(heroes.get(i).getClass().getSimpleName().charAt(0) + " ");
                if(heroes.get(i).isDead())
                    fs.writeWord("dead\n");
                else
                    fs.writeWord(heroes.get(i).getLevel() + " " + heroes.get(i).getXp() + " " + heroes.get(i).getHp() + " " + heroes.get(i).getPosition() + "\n");
            }
            fs.close();
        }
        catch (Exception e1)
        {
            e1.printStackTrace();
        }
    }
}
