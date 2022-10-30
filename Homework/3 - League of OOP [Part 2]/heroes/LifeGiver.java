package heroes;

import java.util.ArrayList;
import java.util.Arrays;

final class LifeGiver extends Angel
{
    void influenceHero(Hero hero)
    {
        hero.receiveAngel(this);
    }

    void influenceHero(Pyromancer pyromancer)
    {
        if(!pyromancer.dead)
        {
            pyromancer.hp = Math.min(pyromancer.hp + 80, pyromancer.max_hp);
            notifyObservers(new ArrayList<>(Arrays.asList(this, true, pyromancer)));
        }
    }

    void influenceHero(Knight knight)
    {
        if(!knight.dead)
        {
            knight.hp = Math.min(knight.hp + 100, knight.max_hp);
            notifyObservers(new ArrayList<>(Arrays.asList(this, true, knight)));
        }
    }

    void influenceHero(Wizard wizard)
    {
        if(!wizard.dead)
        {
            wizard.hp = Math.min(wizard.hp + 120, wizard.max_hp);
            notifyObservers(new ArrayList<>(Arrays.asList(this, true, wizard)));
        }
    }

    void influenceHero(Rogue rogue)
    {
        if(!rogue.dead)
        {
            rogue.hp = Math.min(rogue.hp + 90, rogue.max_hp);
            notifyObservers(new ArrayList<>(Arrays.asList(this, true, rogue)));
        }
    }
}
