package main;

import map.Position;

public class PairIntPosition
{
    private int key;
    private Position value;

    public PairIntPosition(int key, Position value)
    {
        this.key = key;
        this.value = new Position(value);
    }

    public int getKey()
    {
        return key;
    }

    public Position getValue()
    {
        return value;
    }
}
