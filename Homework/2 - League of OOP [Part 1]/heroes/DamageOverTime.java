package heroes;

class DamageOverTime extends Debuff
{
    int damage;

    DamageOverTime(int damage)
    {
        this.damage = damage;
    }

    DamageOverTime(int rounds, int damage)
    {
        this.rounds = rounds;
        this.damage = damage;
    }

    void applyEffect(Hero hero)
    {
        hero.takeDamage(damage);
        rounds--;
    }
}
