package heroes;

import java.util.ArrayList;
import java.util.List;

abstract class Ability
{
    protected int base_damage_to_be_done;
    protected int total_damage_to_be_done;
    protected List<Debuff> debuffs_to_be_applied = new ArrayList<>();

    abstract void levelUp();

    float getModifier(char terrain)
    {
        return 1.0f;
    }

    float getModifier(Hero hero)
    {
        return 1.0f;
    }

    void computeDamageToBeDone(Hero hero) {}

    void computeDebuffsToBeApplied(Hero hero) {}

    void cast(Hero hero)
    {
        hero.takeDamage(total_damage_to_be_done);
        hero.receiveDebuffs(debuffs_to_be_applied);
    }
}
