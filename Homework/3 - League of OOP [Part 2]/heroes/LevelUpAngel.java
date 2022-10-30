package heroes;

import java.util.ArrayList;
import java.util.Arrays;

final class LevelUpAngel extends Angel
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
            pyromancer.extra_race_modifier += 0.2f;
            int xp = Hero.base_xp_level_up + pyromancer.level * Hero.xp_per_level - pyromancer.xp;
            pyromancer.receiveExperience(xp);
        }
    }

    void influenceHero(Knight knight)
    {
        if(!knight.dead)
        {
            notifyObservers(new ArrayList<>(Arrays.asList(this, true, knight)));
            knight.extra_race_modifier += 0.1f;
            int xp = Hero.base_xp_level_up + knight.level * Hero.xp_per_level - knight.xp;
            knight.receiveExperience(xp);
        }
    }

    void influenceHero(Wizard wizard)
    {
        if(!wizard.dead)
        {
            notifyObservers(new ArrayList<>(Arrays.asList(this, true, wizard)));
            wizard.extra_race_modifier += 0.25f;
            int xp = Hero.base_xp_level_up + wizard.level * Hero.xp_per_level - wizard.xp;
            wizard.receiveExperience(xp);
        }
    }

    void influenceHero(Rogue rogue)
    {
        if(!rogue.dead)
        {
            notifyObservers(new ArrayList<>(Arrays.asList(this, true, rogue)));
            rogue.extra_race_modifier += 0.15f;
            int xp = Hero.base_xp_level_up + rogue.level * Hero.xp_per_level - rogue.xp;
            rogue.receiveExperience(xp);
        }
    }
}
