package heroes;

class Impair extends Debuff
{
    Impair() {}
    Impair(int rounds)
    {
        this.rounds = rounds;
    }

    void applyEffect(Hero hero)
    {
        hero.impaired = true;
        rounds--;
    }
}
