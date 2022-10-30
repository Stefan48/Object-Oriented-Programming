package heroes;

import java.util.ArrayList;
import java.util.Arrays;

final class Dracula extends Angel
{
    void influenceHero(Hero hero)
    {
        hero.receiveAngel(this);
    }

    void influenceHero(Pyromancer pyromancer)
    {
        if(!pyromancer.dead)
        {
            pyromancer.extra_race_modifier -= 0.3f;
            pyromancer.takeDamage(40);
            notifyObservers(new ArrayList<>(Arrays.asList(this, false, pyromancer)));
            if(pyromancer.dead)
                notifyObservers(new ArrayList<>(Arrays.asList(pyromancer, this)));
        }
    }

    void influenceHero(Knight knight)
    {
        if(!knight.dead)
        {
            knight.extra_race_modifier -= 0.2f;
            knight.takeDamage(60);
            notifyObservers(new ArrayList<>(Arrays.asList(this, false, knight)));
            if(knight.dead)
                notifyObservers(new ArrayList<>(Arrays.asList(knight, this)));
        }
    }

    void influenceHero(Wizard wizard)
    {
        if(!wizard.dead)
        {
            wizard.extra_race_modifier -= 0.4f;
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
            rogue.extra_race_modifier -= 0.1f;
            rogue.takeDamage(35);
            notifyObservers(new ArrayList<>(Arrays.asList(this, false, rogue)));
            if(rogue.dead)
                notifyObservers(new ArrayList<>(Arrays.asList(rogue, this)));
        }
    }
}
