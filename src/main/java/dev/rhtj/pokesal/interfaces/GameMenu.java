package dev.rhtj.pokesal.interfaces;

import java.io.PrintStream;
import java.util.Scanner;

import dev.rhtj.pokesal.Game;
import dev.rhtj.pokesal.World;
import dev.rhtj.pokesal.entities.Trainer;

public class GameMenu implements Menu {
    
    private World world;
    private Trainer player;

    private Menu caller;
    private Menu nextMenu;

    public GameMenu(Menu caller) {
        this.caller = caller;
        world = Game.getInstance().getWorld();
        player = world.getPlayer();
    }
    
    
    @Override
    public void display(PrintStream out) {
        
    }

    private void displayGrid(PrintStream out) {
        
    }

    @Override
    public String getInput(Scanner scanner) {
        return null;
    }

    @Override
    public Menu next() {
        return nextMenu;
    }
}
