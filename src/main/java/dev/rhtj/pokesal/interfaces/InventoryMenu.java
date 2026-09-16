package dev.rhtj.pokesal.interfaces;

import java.io.PrintStream;
import java.util.Scanner;

import dev.rhtj.pokesal.AnsiCode;

public class InventoryMenu implements Menu {

    private Menu caller = null;

    public InventoryMenu(Menu caller) {
        this.caller = caller;
    }

    @Override
    public void display(PrintStream out) {
        out.println(
            AnsiCode.apply(
                "INVENTARIO", 
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
