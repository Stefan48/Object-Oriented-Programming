package heroes;

import map.Map;

import java.util.ArrayList;

final class Rogue extends Hero
{
    Rogue()
    {
        hp = max_hp = 600;
        max_hp_level_up_increase = 40;
        abilities = new Ability[2];
        abilities[0] = new Backstab();
        abilities[1] = new Paralysis();
        strategy = new RogueStrategy();
    }

    private final class Backstab extends Ability
    {
        int damage = 200;
        int damage_level_up_increase = 20;
        int hit_count;
        float critical_hit_amplifier = 1.5f;

        protected void levelUp()
        {
            damage += damage_level_up_increase;
        }

        protected float getModifier(char terrain)
        {
            if(terrain == 'W')
                return 1.15f;
            return 1.0f;
        }

        protected float getModifier(Hero hero)
        {
            String race = hero.getClass().getSimpleName();
            switch(race)
            {
                case "Pyromancer": return 1.25f;
                case "Knight": return 0.9f;
                case "Wizard": return 1.25f;
                case "Rogue": return 1.2f;
                default: return 1.0f;
            }
        }

        protected void computeDamageToBeDone(Hero hero)
        {
            base_damage_to_be_done = damage;
            if(Map.getInstance().getTerrain(Rogue.this.position) == 'W')
            {
                if(hit_count == 0)
                    base_damage_to_be_done = Math.round(base_damage_to_be_done * critical_hit_amplifier);
            }
            total_damage_to_be_done = Math.round(base_damage_to_be_done * getModifier(Map.getInstance().getTerrain(Rogue.this.position)));
            total_damage_to_be_done = Math.round(total_damage_to_be_done * (getModifier(hero) + extra_race_modifier));
        }

        protected void cast(Hero hero)
        {
            hero.takeDamage(total_damage_to_be_done);
            if(Map.getInstance().getTerrain(Rogue.this.position) == 'W')
                hit_count = (hit_count + 1) % 3;
            else
                hit_count = 0;
        }
    }

    private final class Paralysis extends Ability
    {
        int damage_over_time = 40;
        int damage_over_time_level_up_increase = 10;
        int damage_over_time_base_rounds = 3;
        int damage_over_time_amplified_rounds = 6;

        protected void levelUp()
        {
            damage_over_time += damage_over_time_level_up_increase;
        }

        protected float getModifier(char terrain)
        {
            if(terrain == 'W')
                return 1.15f;
            return 1.0f;
        }

        protected float getModifier(Hero hero)
        {
            String race = hero.getClass().getSimpleName();
            switch(race)
            {
                case "Pyromancer": return 1.2f;
                case "Knight": return 0.8f;
                case "Wizard": return 1.25f;
                case "Rogue": return 0.9f;
                default: return 1.0f;
            }
        }

        protected void computeDamageToBeDone(Hero hero)
        {
            base_damage_to_be_done = damage_over_time;
            total_damage_to_be_done = Math.round(base_damage_to_be_done * getModifier(Map.getInstance().getTerrain(Rogue.this.position)));
            total_damage_to_be_done = Math.round(total_damage_to_be_done * (getModifier(hero) + extra_race_modifier) - 0.00001f);
        }

        protected void computeDebuffsToBeApplied(Hero hero)
        {
            int rounds;
            if(Map.getInstance().getTerrain(Rogue.this.position) == 'W')
                rounds = damage_over_time_amplified_rounds;
            else
                rounds = damage_over_time_base_rounds;
            debuffs_to_be_applied = new ArrayList<>();
            debuffs_to_be_applied.add(new DamageOverTime(rounds, total_damage_to_be_done));
            debuffs_to_be_applied.add(new Impair(rounds));
        }
    }

    private final class RogueStrategy extends Strategy
    {
        public void apply()
        {
            if(Rogue.this.hp > Rogue.this.max_hp / 7 && Rogue.this.hp < Rogue.this.max_hp / 5)
            {
                Rogue.this.hp -= Rogue.this.hp / 7;
                Rogue.this.extra_race_modifier += 0.4f;
            }
            else if(Rogue.this.hp < Rogue.this.max_hp / 7)
            {
                Rogue.this.extra_race_modifier -= 0.1f;
                Rogue.this.hp += Rogue.this.hp / 2;
            }
        }
    }

    public void receiveAngel(Angel angel)
    {
        angel.influenceHero(this);
    }
}
