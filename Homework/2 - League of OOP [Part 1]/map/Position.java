package map;

import main.PairIntPosition;

import java.util.Comparator;

public class Position
{
    public int x;
    public int y;

    public Position()
    {
        x = y = 0;
    }
    public Position(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
    public Position(Position other)
    {
        this.x = other.x;
        this.y = other.y;
    }

    public boolean equals(Position other)
    {
        return (this.x == other.x && this.y == other.y);
    }
    public boolean equals(Object o)
    {
        if (o == this)
            return true;
        if (!(o instanceof Position))
            return false;
        return equals((Position) o);
    }

    public String toString()
    {
        return x + " " + y;
    }

    // Comparator for sorting a list of PairIntPosition by position
    public static Comparator<PairIntPosition> sortAscending = new Comparator<PairIntPosition>()
    {
        public int compare(PairIntPosition p1, PairIntPosition p2)
        {
            if(p1.getValue().x == p2.getValue().x)
                return p1.getValue().y - p2.getValue().y;
            return p1.getValue().x - p2.getValue().x;
        }
    };
}
