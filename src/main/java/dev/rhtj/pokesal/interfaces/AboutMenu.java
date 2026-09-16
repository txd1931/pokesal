package dev.rhtj.pokesal.interfaces;

import java.io.PrintStream;
import java.util.Scanner;

import dev.rhtj.pokesal.AnsiCode;

public class AboutMenu implements Menu {
    
    private Menu caller = null;
    
    private static final String message = "Texto de descrição temporario";

    public AboutMenu(Menu caller) {
        this.caller = caller;
    }

    @Override
    public void display(PrintStream out) {
        out.println(
            AnsiCode.apply(message, 
                AnsiCode.DIM
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
