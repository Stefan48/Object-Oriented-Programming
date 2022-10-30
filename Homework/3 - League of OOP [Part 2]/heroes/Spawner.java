package heroes;

import java.util.ArrayList;
import java.util.Arrays;

final class Spawner extends Angel
{
    void influenceHero(Hero hero)
    {
        hero.receiveAngel(this);
    }

    void influenceHero(Pyromancer pyromancer)
    {
        if(pyromancer.dead)
        {
            pyromancer.hp = 150;
            pyromancer.dead = false;
            notifyObservers(new ArrayList<>(Arrays.asList(this, true, pyromancer)));
            notifyObservers(new ArrayList<Object>(Arrays.asList(pyromancer, this)));
        }
    }

    void influenceHero(Knight knight)
    {
        if(knight.dead)
        {
            knight.hp = 200;
            knight.dead = false;
            notifyObservers(new ArrayList<>(Arrays.asList(this, true, knight)));
            notifyObservers(new ArrayList<Object>(Arrays.asList(knight, this)));
        }
    }

    void influenceHero(Wizard wizard)
    {
        if(wizard.dead)
        {
            wizard.hp = 120;
            wizard.dead = false;
            notifyObservers(new ArrayList<>(Arrays.asList(this, true, wizard)));
            notifyObservers(new ArrayList<Object>(Arrays.asList(wizard, this)));
        }
    }

    void influenceHero(Rogue rogue)
    {
        if(rogue.dead)
        {
            rogue.hp = 180;
            rogue.dead = false;
            notifyObservers(new ArrayList<>(Arrays.asList(this, true, rogue)));
            notifyObservers(new ArrayList<Object>(Arrays.asList(rogue, this)));
        }
    }
}
