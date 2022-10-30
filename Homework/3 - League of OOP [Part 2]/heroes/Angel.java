package heroes;

import main.Observable;
import map.Position;

public abstract class Angel extends Observable
{
    protected Position position = new Position();

    abstract void influenceHero(Hero hero);
    abstract void influenceHero(Pyromancer pyromancer);
    abstract void influenceHero(Knight knight);
    abstract void influenceHero(Wizard wizard);
    abstract void influenceHero(Rogue rogue);

    public final Position getPosition()
    {
        return position;
    }

    public final void setPosition(int x, int y)
    {
        position.x = x;
        position.y = y;
    }
    public final void setPosition(Position pos)
    {
        position.x = pos.x;
        position.y = pos.y;
    }
}
