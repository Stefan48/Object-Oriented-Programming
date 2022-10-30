package heroes;

import main.Observable;
import map.Position;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Hero extends Observable
{
    int hp;
    int max_hp;
    int max_hp_level_up_increase;
    int xp;
    int level;
    boolean dead;
    boolean impaired;
    Position position = new Position();
    Ability[] abilities;
    List<Debuff> debuffs = new ArrayList<>();
    float extra_race_modifier = 0.0f;
    Strategy strategy;
    int id;

    protected static int base_xp_level_up = 250;
    protected static int xp_per_level = 50;

    protected abstract class Ability
    {
        protected int base_damage_to_be_done;
        protected int total_damage_to_be_done;
        protected List<Debuff> debuffs_to_be_applied = new ArrayList<>();

        protected abstract void levelUp();

        protected float getModifier(char terrain)
        {
            return 1.0f;
        }

        protected float getModifier(Hero hero)
        {
            return 1.0f;
        }

        protected void computeDamageToBeDone(Hero hero) {}

        protected void computeDebuffsToBeApplied(Hero hero) {}

        protected void cast(Hero hero)
        {
            hero.takeDamage(total_damage_to_be_done);
            hero.receiveDebuffs(debuffs_to_be_applied);
        }
    }

    protected abstract class Debuff
    {
        protected int rounds;

        Debuff()
        {
            rounds = 1;
        }

        Debuff(int rounds)
        {
            this.rounds = rounds;
        }

        final int getRounds()
        {
            return rounds;
        }

        abstract void applyEffect(Hero hero);
    }

    protected class Impair extends Debuff
    {
        Impair() {}
        Impair(int rounds)
        {
            this.rounds = rounds;
        }

        void applyEffect(Hero hero)
        {
            hero.impaired = true;
            rounds--;
        }
    }

    protected class DamageOverTime extends Debuff
    {
        int damage;

        DamageOverTime(int damage)
        {
            this.damage = damage;
        }

        DamageOverTime(int rounds, int damage)
        {
            this.rounds = rounds;
            this.damage = damage;
        }

        void applyEffect(Hero hero)
        {
            hero.takeDamage(damage);
            rounds--;
        }
    }

    protected abstract class Strategy
    {
        public abstract void apply();
    }

    public void applyStrategy()
    {
        strategy.apply();
    }

    public final int getHp()
    {
        return hp;
    }
    public final int getXp()
    {
        return xp;
    }
    public final int getLevel()
    {
        return level;
    }
    public final boolean isDead()
    {
        return dead;
    }
    public final boolean isImpaired()
    {
        return impaired;
    }
    public final Position getPosition()
    {
        return position;
    }
    public final void setPosition(int x, int y)
    {
        position.x = x;
        position.y = y;
    }
    public final void setPosition(Position pos)
    {
        position.x = pos.x;
        position.y = pos.y;
    }
    public final int getId()
    {
        return id;
    }
    public final void setId(int id)
    {
        this.id = id;
    }

    void levelUp()
    {
        level++;
        notifyObservers(this);
        max_hp += max_hp_level_up_increase;
        if(!dead)
            hp = max_hp;
        for (Ability ability : abilities)
            ability.levelUp();
    }

    public final void move(char direction)
    {
        if(impaired || dead)
            return;
        if(direction == 'U')
            position.x--;
        else if(direction == 'D')
            position.x++;
        else if(direction == 'L')
            position.y--;
        else if(direction == 'R')
            position.y++;
    }

    public static void fight(Hero hero1, Hero hero2)
    {
        int hero1_level = hero1.level;
        int hero2_level = hero2.level;
        hero1.computeAllAbilities(hero2);
        hero2.computeAllAbilities(hero1);
        hero1.castAllAbilities(hero2);
        hero2.castAllAbilities(hero1);
        if(hero1.dead)
        {
            if(hero2.dead)
            {
                if(hero1.id < hero2.id)
                {
                    hero2.notifyObservers(new ArrayList<Object>(Arrays.asList(hero2, hero1)));
                    hero1.notifyObservers(new ArrayList<Object>(Arrays.asList(hero1, hero2)));
                }
                else
                {
                    hero1.notifyObservers(new ArrayList<Object>(Arrays.asList(hero1, hero2)));
                    hero2.notifyObservers(new ArrayList<Object>(Arrays.asList(hero2, hero1)));
                }
            }
            else
            {
                hero1.notifyObservers(new ArrayList<Object>(Arrays.asList(hero1, hero2)));
            }
        }
        else if(hero2.dead)
        {
            hero2.notifyObservers(new ArrayList<Object>(Arrays.asList(hero2, hero1)));
        }
        if(hero1.dead && !hero2.dead)
            hero2.receiveExperience(Math.max(0, 200 - (hero2_level - hero1_level) * 40));
        if(hero2.dead && !hero1.dead)
            hero1.receiveExperience(Math.max(0, 200 - (hero1_level - hero2_level) * 40));
    }

    void receiveExperience(int experience)
    {
        int previous_level = level;
        xp += experience;
        if(xp >= base_xp_level_up)
        {
            int lvl = (xp - base_xp_level_up) / xp_per_level + 1;
            for(int i = previous_level + 1; i <= lvl; ++i)
                levelUp();
        }
    }

    void computeAllAbilities(Hero hero)
    {
        for(int i = 0; i < abilities.length; ++i)
        {
            abilities[i].computeDamageToBeDone(hero);
            abilities[i].computeDebuffsToBeApplied(hero);
        }
    }

    void castAllAbilities(Hero hero)
    {
        for(int i = 0; i < abilities.length; ++i)
            abilities[i].cast(hero);
    }

    void takeDamage(int damage)
    {
        hp -= damage;
        if(hp <= 0)
        {
            hp = 0;
            dead = true;
        }
    }

    void receiveDebuffs(List<Debuff> debuffs)
    {
        this.debuffs = new ArrayList<>(debuffs);
    }

    public void receiveDebuffEffects()
    {
        for(int i = 0; i < debuffs.size(); ++i)
        {
            debuffs.get(i).applyEffect(this);
            if(debuffs.get(i).rounds == 0)
            {
                debuffs.remove(i);
                i--;
            }
        }
    }

    public void clearDebuffs()
    {
        impaired = false;
    }

    public abstract void receiveAngel(Angel angel);
}
