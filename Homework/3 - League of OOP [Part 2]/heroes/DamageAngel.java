package heroes;

import java.util.ArrayList;
import java.util.Arrays;

final class DamageAngel extends Angel
{
    void influenceHero(Hero hero)
    {
        hero.receiveAngel(this);
    }

    void influenceHero(Pyromancer pyromancer)
    {
        if(!pyromancer.dead)
        {
            pyromancer.extra_race_modifier += 0.2f;
            notifyObservers(new ArrayList<>(Arrays.asList(this, true, pyromancer)));
        }
    }

    void influenceHero(Knight knight)
    {
        if(!knight.dead)
        {
            knight.extra_race_modifier += 0.15f;
            notifyObservers(new ArrayList<>(Arrays.asList(this, true, knight)));
        }
    }

    void influenceHero(Wizard wizard)
    {
        if(!wizard.dead)
        {
            wizard.extra_race_modifier += 0.4f;
            notifyObservers(new ArrayList<>(Arrays.asList(this, true, wizard)));
        }
    }

    void influenceHero(Rogue rogue)
    {
        if(!rogue.dead)
        {
            rogue.extra_race_modifier += 0.3f;
            notifyObservers(new ArrayList<>(Arrays.asList(this, true, rogue)));
        }
    }
}
