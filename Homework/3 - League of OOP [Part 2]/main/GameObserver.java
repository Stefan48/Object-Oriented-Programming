package main;

import fileio.FileSystem;
import heroes.Angel;
import heroes.Hero;

import java.util.List;

public final class GameObserver implements Observer
{
    private FileSystem fs;

    public GameObserver(FileSystem fs)
    {
        this.fs = fs;
    }

    public void update(Object obj)
    {
        try
        {
            if(obj instanceof Hero)
            {
                fs.writeWord(obj.getClass().getSimpleName() + " " + ((Hero)obj).getId() + " reached level " + ((Hero)obj).getLevel() + "\n");
            }
            if(obj instanceof Angel)
            {
                fs.writeWord("Angel " + obj.getClass().getSimpleName() + " was spawned at " + ((Angel)obj).getPosition() + "\n");
                return;
            }
            if(obj instanceof List)
            {
                if(((List)obj).get(0) instanceof Angel)
                {
                    boolean helped = (boolean)((List)obj).get(1);
                    fs.writeWord(((List)obj).get(0).getClass().getSimpleName());
                    if(helped)
                        fs.writeWord(" helped ");
                    else
                        fs.writeWord(" hit ");
                    fs.writeWord(((List)obj).get(2).getClass().getSimpleName() + " " + ((Hero)((List)obj).get(2)).getId() + "\n");
                    return;
                }
                if(((List)obj).get(0) instanceof Hero)
                {
                    if(((List)obj).get(1) instanceof Angel)
                    {
                        fs.writeWord("Player " + ((List)obj).get(0).getClass().getSimpleName() + " " + ((Hero)((List)obj).get(0)).getId());
                        if(((Hero)((List)obj).get(0)).isDead())
                            fs.writeWord(" was killed by an angel\n");
                        else
                            fs.writeWord(" was brought to life by an angel\n");
                        return;
                    }
                    if(((List)obj).get(1) instanceof Hero)
                    {
                        fs.writeWord("Player " + ((List)obj).get(0).getClass().getSimpleName() + " " + ((Hero)((List)obj).get(0)).getId() + " was killed by " + ((List)obj).get(1).getClass().getSimpleName() + " " + ((Hero)((List)obj).get(1)).getId() + "\n");
                        return;
                    }
                }
            }
        }
        catch(Exception e1)
        {
            e1.printStackTrace();
        }
    }
}
