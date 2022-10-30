package heroes;

import java.util.ArrayList;
import java.util.Arrays;

final class DarkAngel extends Angel
{
    void influenceHero(Hero hero)
    {
        hero.receiveAngel(this);
    }

    void influenceHero(Pyromancer pyromancer)
    {
        if(!pyromancer.dead)
        {
            pyromancer.takeDamage(30);
            notifyObservers(new ArrayList<>(Arrays.asList(this, false, pyromancer)));
            if(pyromancer.dead)
                notifyObservers(new ArrayList<>(Arrays.asList(pyromancer, this)));
        }
    }

    void influenceHero(Knight knight)
    {
        if(!knight.dead)
        {
            knight.takeDamage(40);
            notifyObservers(new ArrayList<>(Arrays.asList(this, false, knight)));
            if(knight.dead)
                notifyObservers(new ArrayList<>(Arrays.asList(knight, this)));
        }
    }

    void influenceHero(Wizard wizard)
    {
        if(!wizard.dead)
        {
            wizard.takeDamage(20);
            notifyObservers(new ArrayList<>(Arrays.asList(this, false, wizard)));
            if(wizard.dead)
                notifyObservers(new ArrayList<>(Arrays.asList(wizard, this)));
        }
    }

    void influenceHero(Rogue rogue)
    {
        if(!rogue.dead)
        {
            rogue.takeDamage(10);
            notifyObservers(new ArrayList<>(Arrays.asList(this, false, rogue)));
            if(rogue.dead)
                notifyObservers(new ArrayList<>(Arrays.asList(rogue, this)));
        }
    }
}
