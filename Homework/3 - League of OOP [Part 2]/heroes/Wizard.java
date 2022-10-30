package heroes;

import map.Map;

final class Wizard extends Hero
{
    Wizard()
    {
        hp = max_hp = 400;
        max_hp_level_up_increase = 30;
        abilities = new Ability[2];
        abilities[0] = new Drain();
        abilities[1] = new Deflect();
        strategy = new WizardStrategy();
    }

    private final class Drain extends Ability
    {
        float drain_hp_percentage = 0.2f;
        float drain_hp_percentage_level_up_increase = 0.05f;

        protected void levelUp()
        {
            drain_hp_percentage += drain_hp_percentage_level_up_increase;
        }

        protected float getModifier(char terrain)
        {
            if(terrain == 'D')
                return 1.1f;
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
            base_damage_to_be_done = Math.round(drain_hp_percentage * Math.min(Math.round(0.3f * hero.max_hp), hero.hp));
            float total_percentage = drain_hp_percentage * getModifier(Map.getInstance().getTerrain(Wizard.this.getPosition()));
            total_percentage = total_percentage * (getModifier(hero) + extra_race_modifier);
            total_damage_to_be_done = Math.round((total_percentage * Math.min(Math.round(0.3f * hero.max_hp), hero.hp)));
        }

        protected void cast(Hero hero)
        {
            hero.takeDamage(total_damage_to_be_done);
        }
    }

    private final class Deflect extends Ability
    {
        float deflect_percentage = 0.35f;
        float deflect_percentage_level_up_increase = 0.02f;
        float max_deflect_percentage = 0.7f;

        protected void levelUp()
        {
            if(deflect_percentage < max_deflect_percentage)
                deflect_percentage = Math.min(deflect_percentage + deflect_percentage_level_up_increase, max_deflect_percentage);
        }

        protected float getModifier(char terrain)
        {
            if(terrain == 'D')
                return 1.1f;
            return 1.0f;
        }

        protected float getModifier(Hero hero)
        {
            String race = hero.getClass().getSimpleName();
            switch(race)
            {
                case "Pyromancer": return 1.3f;
                case "Knight": return 1.4f;
                case "Rogue": return 1.2f;
                default: return 1.0f;
            }
        }

        protected void computeDamageToBeDone(Hero hero)
        {
            if(hero instanceof Wizard)
            {
                total_damage_to_be_done = base_damage_to_be_done = 0;
                return;
            }
            base_damage_to_be_done = 0;
            for(int i = 0; i < hero.abilities.length; ++i)
            {
                hero.abilities[i].computeDamageToBeDone(Wizard.this);
                base_damage_to_be_done += Math.round(hero.abilities[i].base_damage_to_be_done * hero.abilities[i].getModifier(Map.getInstance().getTerrain(hero.position)));
            }
            float total_percentage = deflect_percentage * getModifier(Map.getInstance().getTerrain(Wizard.this.getPosition()));
            total_percentage = total_percentage * (getModifier(hero) + extra_race_modifier);
            total_damage_to_be_done = Math.round(total_percentage * base_damage_to_be_done);
            base_damage_to_be_done = Math.round(deflect_percentage * base_damage_to_be_done);
        }

        protected void cast(Hero hero)
        {
            hero.takeDamage(total_damage_to_be_done);
        }
    }

    private final class WizardStrategy extends Strategy
    {
        public void apply()
        {
            if(Wizard.this.hp > Wizard.this.max_hp / 4 && Wizard.this.hp < Wizard.this.max_hp / 2)
            {
                Wizard.this.hp -= Wizard.this.hp / 10;
                Wizard.this.extra_race_modifier += 0.6f;
            }
            else if(Wizard.this.hp < Wizard.this.max_hp / 4)
            {
                Wizard.this.extra_race_modifier -= 0.2f;
                Wizard.this.hp += Wizard.this.hp / 5;
            }
        }
    }

    public void receiveAngel(Angel angel)
    {
        angel.influenceHero(this);
    }
}
