package heroes;

import java.util.ArrayList;
import java.util.Arrays;

final class TheDoomer extends Angel
{
    void influenceHero(Hero hero)
    {
        if(!hero.dead)
        {
            hero.takeDamage(hero.hp);
            notifyObservers(new ArrayList<>(Arrays.asList(this, false, hero)));
            notifyObservers(new ArrayList<Object>(Arrays.asList(hero, this)));
        }
    }
    void influenceHero(Pyromancer pyromancer)
    {
        influenceHero((Hero)pyromancer);
    }
    void influenceHero(Knight knight)
    {
        influenceHero((Hero)knight);
    }
    void influenceHero(Wizard wizard)
    {
        influenceHero((Hero)wizard);
    }
    void influenceHero(Rogue rogue)
    {
        influenceHero((Hero)rogue);
    }
}
