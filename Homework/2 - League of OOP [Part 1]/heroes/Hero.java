package heroes;

import map.Position;

import java.util.ArrayList;
import java.util.List;

public abstract class Hero
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

    protected static int base_xp_level_up = 250;
    protected static int xp_per_level = 50;

    public int getHp()
    {
        return hp;
    }
    public int getXp()
    {
        return xp;
    }
    public int getLevel()
    {
        return level;
    }
    public boolean isDead()
    {
        return dead;
    }
    public Position getPosition()
    {
        return position;
    }

    void levelUp()
    {
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

    public static void fight(Hero hero1, Hero hero2)
    {
        int hero1_level = hero1.level;
        int hero2_level = hero2.level;
        hero1.computeAllAbilities(hero2);
        hero2.computeAllAbilities(hero1);
        hero1.castAllAbilities(hero2);
        hero2.castAllAbilities(hero1);
        if(hero2.dead)
            hero1.receiveExperience(Math.max(0, 200 - (hero1_level - hero2_level) * 40));
        if(hero1.dead)
            hero2.receiveExperience(Math.max(0, 200 - (hero2_level - hero1_level) * 40));
    }

    void receiveExperience(int experience)
    {
        int previous_level = level;
        xp += experience;
        if(xp >= base_xp_level_up)
            level = (xp - base_xp_level_up) / xp_per_level + 1;
        for(int i = previous_level + 1; i <= level; ++i)
            levelUp();
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
}
