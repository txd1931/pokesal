package dev.rhtj.pokesal;

import dev.rhtj.pokesal.interfaces.MainMenu;

public class Main {
    public static void main(String[] args) {
        
        Game game = new Game(System.out, System.in, new MainMenu());
        game.start();
    }
}