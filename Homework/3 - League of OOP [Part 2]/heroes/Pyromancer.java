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
        strategy = new PyromancerStrategy();
    }

    private final class Fireblast extends Ability
    {
        int damage = 350;
        int damage_level_up_increase = 50;

        protected void levelUp()
        {
            damage += damage_level_up_increase;
        }

        protected float getModifier(char terrain)
        {
            if(terrain == 'V')
                return 1.25f;
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
            total_damage_to_be_done = Math.round(base_damage_to_be_done * getModifier(Map.getInstance().getTerrain(Pyromancer.this.position)));
            total_damage_to_be_done = Math.round(total_damage_to_be_done * (getModifier(hero) + extra_race_modifier));
        }

        protected void cast(Hero hero)
        {
            hero.takeDamage(total_damage_to_be_done);
        }
    }

    private final class Ignite extends Ability
    {
        int base_damage = 150;
        int base_damage_level_up_increase = 20;
        int damage_over_time = 50;
        int damage_over_time_level_up_increase = 30;
        int damage_over_time_rounds = 2;

        protected void levelUp()
        {
            base_damage += base_damage_level_up_increase;
            damage_over_time += damage_over_time_level_up_increase;
        }

        protected float getModifier(char terrain)
        {
            if(terrain == 'V')
                return 1.25f;
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
            base_damage_to_be_done = base_damage;
            total_damage_to_be_done = Math.round(base_damage_to_be_done * getModifier(Map.getInstance().getTerrain(Pyromancer.this.position)));
            total_damage_to_be_done = Math.round(total_damage_to_be_done * (getModifier(hero) + extra_race_modifier));
        }

        protected void computeDebuffsToBeApplied(Hero hero)
        {
            int total_dot = Math.round(damage_over_time * getModifier(Map.getInstance().getTerrain(Pyromancer.this.position)));
            total_dot = Math.round(total_dot * (getModifier(hero) + extra_race_modifier));
            debuffs_to_be_applied = new ArrayList<>();
            debuffs_to_be_applied.add(new DamageOverTime(damage_over_time_rounds, total_dot));
        }
    }

    private final class PyromancerStrategy extends Strategy
    {
        public void apply()
        {
            if(Pyromancer.this.hp > Pyromancer.this.max_hp / 4 && Pyromancer.this.hp < Pyromancer.this.max_hp / 3)
            {
                Pyromancer.this.hp -= Pyromancer.this.hp / 4;
                Pyromancer.this.extra_race_modifier += 0.7f;
            }
            else if(Pyromancer.this.hp < Pyromancer.this.max_hp / 4)
            {
                Pyromancer.this.extra_race_modifier -= 0.3f;
                Pyromancer.this.hp += Pyromancer.this.hp / 3;
            }
        }
    }

    public void receiveAngel(Angel angel)
    {
        angel.influenceHero(this);
    }
}
