package main;

import java.util.ArrayList;
import java.util.List;

public class Observable
{
    private List<Observer> observers = new ArrayList<>();

    public void addObserver(Observer observer)
    {
        observers.add(observer);
    }

    public void removeObserver(Observer observer)
    {
        observers.remove(observer);
    }

    public void notifyObservers(Object obj)
    {
        for(Observer observer : observers)
            observer.update(obj);
    }
}
