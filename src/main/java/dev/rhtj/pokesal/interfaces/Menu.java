package dev.rhtj.pokesal.interfaces;

import java.io.PrintStream;
import java.util.Scanner;

public interface Menu {
    public void display(PrintStream out);
    public String getInput(Scanner scanner);
    public Menu next();
}
