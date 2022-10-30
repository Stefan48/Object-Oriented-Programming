package heroes;

abstract class Debuff
{
    protected int rounds;

    Debuff()
    {
        rounds = 1;
    }

    Debuff(int rounds)
    {
        this.rounds = rounds;
    }

    final int getRounds()
    {
        return rounds;
    }

    abstract void applyEffect(Hero hero);
}
