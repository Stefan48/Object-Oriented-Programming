package heroes;

public final class HeroFactory
{
    private static HeroFactory instance = null;
    private HeroFactory() {}
    public static HeroFactory getInstance()
    {
        if(instance == null)
            instance = new HeroFactory();
        return instance;
    }
    public Hero getHero(char race)
    {
        switch (race)
        {
            case 'P': return new Pyromancer();
            case 'K': return new Knight();
            case 'W': return new Wizard();
            case 'R': return new Rogue();
            default: return null;
        }
    }
    public Angel getAngel(String type)
    {
        switch(type)
        {
            case "DamageAngel": return new DamageAngel();
            case "DarkAngel": return new DarkAngel();
            case "Dracula": return new Dracula();
            case "GoodBoy": return new GoodBoy();
            case "LevelUpAngel": return new LevelUpAngel();
            case "LifeGiver": return new LifeGiver();
            case "SmallAngel": return new SmallAngel();
            case "Spawner": return new Spawner();
            case "TheDoomer": return new TheDoomer();
            case "XPAngel": return new XPAngel();
            default: return null;
        }
    }
}
