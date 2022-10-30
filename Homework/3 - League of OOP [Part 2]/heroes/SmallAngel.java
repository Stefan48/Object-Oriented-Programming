package heroes;

import java.util.ArrayList;
import java.util.Arrays;

final class SmallAngel extends Angel
{
    void influenceHero(Hero hero)
    {
        hero.receiveAngel(this);
    }

    void influenceHero(Pyromancer pyromancer)
    {
        if(!pyromancer.dead)
        {
            pyromancer.extra_race_modifier += 0.15f;
            pyromancer.hp = Math.min(pyromancer.hp + 15, pyromancer.max_hp);
            notifyObservers(new ArrayList<>(Arrays.asList(this, true, pyromancer)));
        }
    }

    void influenceHero(Knight knight)
    {
        if(!knight.dead)
        {
            knight.extra_race_modifier += 0.1f;
            knight.hp = Math.min(knight.hp + 10, knight.max_hp);
            notifyObservers(new ArrayList<>(Arrays.asList(this, true, knight)));
        }
    }

    void influenceHero(Wizard wizard)
    {
        if(!wizard.dead)
        {
            wizard.extra_race_modifier += 0.1f;
            wizard.hp = Math.min(wizard.hp + 25, wizard.max_hp);
            notifyObservers(new ArrayList<>(Arrays.asList(this, true, wizard)));
        }
    }

    void influenceHero(Rogue rogue)
    {
        if(!rogue.dead)
        {
            rogue.extra_race_modifier += 0.05f;
            rogue.hp = Math.min(rogue.hp + 20, rogue.max_hp);
            notifyObservers(new ArrayList<>(Arrays.asList(this, true, rogue)));
        }
    }
}
