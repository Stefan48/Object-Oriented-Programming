package heroes;

import java.util.ArrayList;
import java.util.Arrays;

final class XPAngel extends Angel
{
    void influenceHero(Hero hero)
    {
        hero.receiveAngel(this);
    }

    void influenceHero(Pyromancer pyromancer)
    {
        if(!pyromancer.dead)
        {
            notifyObservers(new ArrayList<>(Arrays.asList(this, true, pyromancer)));
            pyromancer.receiveExperience(50);
        }
    }

    void influenceHero(Knight knight)
    {
        if(!knight.dead)
        {
            notifyObservers(new ArrayList<>(Arrays.asList(this, true, knight)));
            knight.receiveExperience(45);
        }
    }

    void influenceHero(Wizard wizard)
    {
        if(!wizard.dead)
        {
            notifyObservers(new ArrayList<>(Arrays.asList(this, true, wizard)));
            wizard.receiveExperience(60);
        }
    }

    void influenceHero(Rogue rogue)
    {
        if(!rogue.dead)
        {
            notifyObservers(new ArrayList<>(Arrays.asList(this, true, rogue)));
            rogue.receiveExperience(40);
        }
    }
}
