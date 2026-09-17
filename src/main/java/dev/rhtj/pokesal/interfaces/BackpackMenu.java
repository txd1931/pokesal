package dev.rhtj.pokesal.interfaces;

import java.io.PrintStream;
import java.util.Scanner;

import dev.rhtj.pokesal.AnsiCode;
import dev.rhtj.pokesal.entities.Backpack;

public class BackpackMenu implements Menu {

    private Menu caller = null;
    private Backpack backpack = null;
    private boolean canSell = false;

    public BackpackMenu(Backpack backpack, boolean canSell, Menu caller) {
        this.canSell = canSell;
        this.backpack = backpack;
        this.caller = caller;
    }

    @Override
    public void display(PrintStream out) {
        out.println(
            AnsiCode.apply(
                "MOCHILA", 
                AnsiCode.BOLD,
                AnsiCode.CYAN
            )
        );

        

    }

    @Override
    public String getInput(Scanner scanner) {
        scanner.nextLine();
        return null;
    }
    
    @Override
    public Menu next() {
        return caller;
    }
}
