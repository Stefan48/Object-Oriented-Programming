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
}
