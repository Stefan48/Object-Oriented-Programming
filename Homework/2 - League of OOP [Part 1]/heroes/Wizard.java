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
    }

    private class Drain extends Ability
    {
        float drain_hp_percentage = 0.2f;
        float drain_hp_percentage_level_up_increase = 0.05f;

        void levelUp()
        {
            drain_hp_percentage += drain_hp_percentage_level_up_increase;
        }

        float getModifier(char terrain)
        {
            if(terrain == 'D')
                return 1.1f;
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
            base_damage_to_be_done = Math.round(drain_hp_percentage * Math.min(Math.round(0.3f * hero.max_hp), hero.hp));

            if(hero instanceof Knight)
            {
                total_damage_to_be_done = Math.round(base_damage_to_be_done * getModifier(Map.getTerrain(Wizard.this.position)));
                total_damage_to_be_done = Math.round(total_damage_to_be_done * getModifier(hero));
            }
            else
            {
                // for some reason if the opponent is not a Knight the modifiers are applied in this particular order
                total_damage_to_be_done = Math.round(base_damage_to_be_done * getModifier(hero));
                total_damage_to_be_done = Math.round(total_damage_to_be_done * getModifier(Map.getTerrain(Wizard.this.getPosition())));
            }
        }

        void cast(Hero hero)
        {
            hero.takeDamage(total_damage_to_be_done);
        }
    }

    private class Deflect extends Ability
    {
        float deflect_percentage = 0.35f;
        float deflect_percentage_level_up_increase = 0.02f;
        float max_deflect_percentage = 0.7f;

        void levelUp()
        {
            if(deflect_percentage < max_deflect_percentage)
                deflect_percentage = Math.min(deflect_percentage + deflect_percentage_level_up_increase, max_deflect_percentage);
        }

        float getModifier(char terrain)
        {
            if(terrain == 'D')
                return 1.1f;
            return 1.0f;
        }

        float getModifier(Hero hero)
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

        void computeDamageToBeDone(Hero hero)
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
                base_damage_to_be_done += Math.round(hero.abilities[i].base_damage_to_be_done * hero.abilities[i].getModifier(Map.getTerrain(hero.position)));
            }
            base_damage_to_be_done = Math.round(deflect_percentage * base_damage_to_be_done);
            total_damage_to_be_done = Math.round(base_damage_to_be_done * getModifier(hero));
            total_damage_to_be_done = Math.round(total_damage_to_be_done * getModifier(Map.getTerrain(Wizard.this.getPosition())));
        }

        void cast(Hero hero)
        {
            hero.takeDamage(total_damage_to_be_done);
        }
    }
}
