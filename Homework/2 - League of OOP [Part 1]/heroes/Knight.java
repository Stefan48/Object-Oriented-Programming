package heroes;

import map.Map;

import java.util.ArrayList;

final class Knight extends Hero
{
    Knight()
    {
        hp = max_hp = 900;
        max_hp_level_up_increase = 80;
        abilities = new Ability[2];
        abilities[0] = new Execute();
        abilities[1] = new Slam();
    }

    private class Execute extends Ability
    {
        int damage = 200;
        int damage_level_up_increase = 30;
        float execute_hp_percentage = 0.2f;
        float execute_hp_percentage_level_up_increase = 0.01f;
        float max_execute_hp_percentage = 0.4f;

        void levelUp()
        {
            damage += damage_level_up_increase;
            if(execute_hp_percentage < max_execute_hp_percentage)
                execute_hp_percentage += execute_hp_percentage_level_up_increase;
        }

        float getModifier(char terrain)
        {
            if(terrain == 'L')
                return 1.15f;
            return 1.0f;
        }

        float getModifier(Hero hero)
        {
            String race = hero.getClass().getSimpleName();
            switch(race)
            {
                case "Pyromancer": return 1.1f;
                case "Wizard": return 0.8f;
                case "Rogue": return 1.15f;
                default: return 1.0f;
            }
        }

        void computeDamageToBeDone(Hero hero)
        {
            float hp_percentage = (float)hero.hp / hero.max_hp;
            if(hp_percentage < execute_hp_percentage)
                base_damage_to_be_done = hero.hp;
            else base_damage_to_be_done = damage;
            total_damage_to_be_done = Math.round(base_damage_to_be_done * getModifier(Map.getTerrain(Knight.this.position)));
            total_damage_to_be_done = Math.round(total_damage_to_be_done * getModifier(hero));
        }

        void cast(Hero hero)
        {
            hero.takeDamage(total_damage_to_be_done);
        }
    }

    private class Slam extends Ability
    {
        int damage = 100;
        int damage_level_up_increase = 40;

        void levelUp()
        {
            damage += damage_level_up_increase;
        }

        float getModifier(char terrain)
        {
            if(terrain == 'L')
                return 1.15f;
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
            total_damage_to_be_done = Math.round(base_damage_to_be_done * getModifier(Map.getTerrain(Knight.this.position)));
            total_damage_to_be_done = Math.round(total_damage_to_be_done * getModifier(hero));
        }

        void computeDebuffsToBeApplied(Hero hero)
        {
            debuffs_to_be_applied = new ArrayList<>();
            debuffs_to_be_applied.add(new Impair());
        }
    }
}
