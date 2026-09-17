package dev.rhtj.pokesal;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

import dev.rhtj.pokesal.interfaces.Menu;

public class Game {

    private static final String ANSI_CLEAR_AND_RESET = "\033[H\033[2J";

    private ArrayList<String> playerInputLog = null; 
    private PrintStream out = null;
    private InputStream in = null;
    private Menu currentMenu = null;

    private World world;


    public Game(PrintStream out, InputStream in, Menu initialMenu) {
        this.out = out;
        this.in = in;
        this.currentMenu = initialMenu;
    }

    public void start() {
        playerInputLog = new ArrayList<>();
        world = new World(10);
        gameLoop();
    }

    private void gameLoop() {
        Menu nextMenu = null;
        String playerInput = "";
        Scanner scanner = new Scanner(in);
        while (currentMenu != null) {
            clearOutput();
            currentMenu.display(out);
            playerInput = currentMenu.getInput(scanner);
            playerInputLog.add(playerInput);
            nextMenu = currentMenu.next();
            currentMenu = nextMenu;
        }
        clearOutput();
        scanner.close();
    }

    private void clearOutput() { 
        out.print(ANSI_CLEAR_AND_RESET);
        out.flush();
    }

    public World getWorld() {
        return world;
    }
}
