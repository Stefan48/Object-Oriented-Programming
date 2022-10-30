package heroes;

import map.Map;

import java.util.ArrayList;

final class Pyromancer extends Hero
{
    Pyromancer()
    {
        hp = max_hp = 500;
        max_hp_level_up_increase = 50;
        abilities = new Ability[2];
        abilities[0] = new Fireblast();
        abilities[1] = new Ignite();
    }

    private class Fireblast extends Ability
    {
        int damage = 350;
        int damage_level_up_increase = 50;

        void levelUp()
        {
            damage += damage_level_up_increase;
        }

        float getModifier(char terrain)
        {
            if(terrain == 'V')
                return 1.25f;
            return 1.0f;
        }

        float getModifier(Hero hero)
        {
            String race = hero.getClass().getSimpleName();
            switch(race)
            {
                case "Pyromancer": return 0.9f;
                case "Knight": return 1.2f;
                case "Wizard": return 1.05f;
                case "Rogue": return 0.8f;
                default: return 1.0f;
            }
        }

        void computeDamageToBeDone(Hero hero)
        {
            base_damage_to_be_done = damage;
            total_damage_to_be_done = Math.round(base_damage_to_be_done * getModifier(Map.getTerrain(Pyromancer.this.position)));
            total_damage_to_be_done = Math.round(total_damage_to_be_done * getModifier(hero));
        }

        void cast(Hero hero)
        {
            hero.takeDamage(total_damage_to_be_done);
        }
    }

    private class Ignite extends Ability
    {
        int base_damage = 150;
        int base_damage_level_up_increase = 20;
        int damage_over_time = 50;
        int damage_over_time_level_up_increase = 30;
        int damage_over_time_rounds = 2;

        void levelUp()
        {
            base_damage += base_damage_level_up_increase;
            damage_over_time += damage_over_time_level_up_increase;
        }

        float getModifier(char terrain)
        {
            if(terrain == 'V')
                return 1.25f;
            return 1.0f;
        }

        float getModifier(Hero hero)
        {
            String race = hero.getClass().getSimpleName();
            switch(race)
            {
                case "Pyromancer": return 0.9f;
                case "Knight": return 1.2f;
                case "Wizard": return 1.05f;
                case "Rogue": return 0.8f;
                default: return 1.0f;
            }
        }

        void computeDamageToBeDone(Hero hero)
        {
            base_damage_to_be_done = base_damage;
            total_damage_to_be_done = Math.round(base_damage_to_be_done * getModifier(Map.getTerrain(Pyromancer.this.position)));
            total_damage_to_be_done = Math.round(total_damage_to_be_done * getModifier(hero));
        }

        void computeDebuffsToBeApplied(Hero hero)
        {
            int total_dot = Math.round(damage_over_time * getModifier(Map.getTerrain(Pyromancer.this.position)));
            total_dot = Math.round(total_dot * getModifier(hero));
            debuffs_to_be_applied = new ArrayList<>();
            debuffs_to_be_applied.add(new DamageOverTime(damage_over_time_rounds, total_dot));
        }
    }
}
