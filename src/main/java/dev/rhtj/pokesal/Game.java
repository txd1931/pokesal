package dev.rhtj.pokesal;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

import dev.rhtj.pokesal.interfaces.MainMenu;
import dev.rhtj.pokesal.interfaces.Menu;

public class Game {

    private static final String ANSI_CLEAR_AND_RESET = "\033[H\033[2J";
    
    private static Game instance;

    private ArrayList<String> playerInputLog = null; 
    private PrintStream out = null;
    private InputStream in = null;
    private Menu currentMenu = null;

    private World world;


    private Game() { }

    public static synchronized Game getInstance() {
        if (instance == null) {
            instance = new Game();
        }
        return instance;
    }

    public void setup(PrintStream out, InputStream in) {
        loadData();
        this.out = out;
        this.in = in;
        world = new World(10);
        this.currentMenu = new MainMenu();
    }

    public void start() {
        playerInputLog = new ArrayList<>();
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
        //out.print(ANSI_CLEAR_AND_RESET);
        out.flush();
    }

    public World getWorld() {
        return world;
    }

    private void loadData() {
        ItemRegistry.loadItems();
        PokesalRegistry.loadPokesals();
    }

    public static void sleep(long miliseconds) {
        try {
            Thread.sleep(miliseconds);
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
            System.err.println(e.getStackTrace());
            System.exit(1);
        }
    }
}
