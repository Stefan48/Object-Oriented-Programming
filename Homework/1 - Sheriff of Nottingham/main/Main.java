package com.tema1.main;

public class Main
{
    public static void main(String[] args)
    {
        GameInputLoader gameInputLoader = new GameInputLoader(args[0], args[1]);
        GameInput gameInput = gameInputLoader.load();
        if (!gameInput.isValidInput()) return;
        Game.Play(gameInput.getRounds(), gameInput.getPlayerNames(), gameInput.getAssetIds());
    }
}
