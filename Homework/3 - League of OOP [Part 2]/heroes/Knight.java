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
        strategy = new KnightStrategy();
    }

    private final class Execute extends Ability
    {
        int damage = 200;
        int damage_level_up_increase = 30;
        float execute_hp_percentage = 0.2f;
        float execute_hp_percentage_level_up_increase = 0.01f;
        float max_execute_hp_percentage = 0.4f;

        protected void levelUp()
        {
            damage += damage_level_up_increase;
            if(execute_hp_percentage < max_execute_hp_percentage)
                execute_hp_percentage += execute_hp_percentage_level_up_increase;
        }

        protected float getModifier(char terrain)
        {
            if(terrain == 'L')
                return 1.15f;
            return 1.0f;
        }

        protected float getModifier(Hero hero)
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

        protected void computeDamageToBeDone(Hero hero)
        {
            base_damage_to_be_done = damage;
            total_damage_to_be_done = Math.round(base_damage_to_be_done * getModifier(Map.getInstance().getTerrain(Knight.this.position)));
            float race_modifier = getModifier(hero);
            if(race_modifier != 1.0f)
                race_modifier += extra_race_modifier;
            total_damage_to_be_done = Math.round(total_damage_to_be_done * race_modifier);
        }

        protected void cast(Hero hero)
        {
            hero.takeDamage(total_damage_to_be_done);
        }
    }

    private final class Slam extends Ability
    {
        int damage = 100;
        int damage_level_up_increase = 40;

        protected void levelUp()
        {
            damage += damage_level_up_increase;
        }

        protected float getModifier(char terrain)
        {
            if(terrain == 'L')
                return 1.15f;
            return 1.0f;
        }

        protected float getModifier(Hero hero)
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

        protected void computeDamageToBeDone(Hero hero)
        {
            base_damage_to_be_done = damage;
            total_damage_to_be_done = Math.round(base_damage_to_be_done * getModifier(Map.getInstance().getTerrain(Knight.this.position)));
            total_damage_to_be_done = Math.round(total_damage_to_be_done * (getModifier(hero) + extra_race_modifier));
        }

        protected void computeDebuffsToBeApplied(Hero hero)
        {
            debuffs_to_be_applied = new ArrayList<>();
            debuffs_to_be_applied.add(new Impair());
        }
    }

    private final class KnightStrategy extends Strategy
    {
        public void apply()
        {
            if(Knight.this.hp > Knight.this.max_hp / 3 && Knight.this.hp < Knight.this.max_hp / 2)
            {
                Knight.this.hp -= Knight.this.hp / 5;
                Knight.this.extra_race_modifier += 0.5f;
            }
            else if(Knight.this.hp < Knight.this.max_hp / 3)
            {
                Knight.this.extra_race_modifier -= 0.2f;
                Knight.this.hp += Knight.this.hp / 4;
            }
        }
    }

    public void receiveAngel(Angel angel)
    {
        angel.influenceHero(this);
    }
}
