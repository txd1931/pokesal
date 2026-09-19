package dev.rhtj.pokesal.interfaces;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import dev.rhtj.pokesal.AnsiCode;
import dev.rhtj.pokesal.entities.Pokedeck;
import dev.rhtj.pokesal.entities.Pokesal;
import dev.rhtj.pokesal.entities.Trainer;

public class PokedeckMenu implements Menu {
    
    private Menu caller = null;
    private Menu nextMenu = null;
    private Pokedeck pokedeck = null;
    private Trainer player = null;
    private Options options = new Options();
    private int selectedPokesal = 0;

    public PokedeckMenu(Trainer player, Menu caller) {
        this.player = player;
        this.pokedeck = player.getPokedeck();
        this.caller = caller;
    }

    private void defineOptions() {
        pokedeck = player.getPokedeck();
        options.clear();
        List<String> pokesalOptions = new ArrayList<>();
        String optionText = "";
        for (Pokesal pokesal : pokedeck.getAll()) {
            optionText = 
                AnsiCode.apply(pokesal.getPokesalRecord().name(), 
                AnsiCode.WHITE, AnsiCode.BOLD) +
                " - " +
                AnsiCode.apply("TIPO:" + pokesal.getPokesalRecord().type().getName(), 
                AnsiCode.DARK_GRAY, AnsiCode.BOLD) +
                "\n" +
                AnsiCode.apply("HP:" + pokesal.getHealthPoints() + "/" + pokesal.getPokesalRecord().healthPoints(), 
                AnsiCode.GREEN, AnsiCode.BOLD) +
                "  " +
                AnsiCode.apply("ATK:" + pokesal.getAtack() + "/" + pokesal.getPokesalRecord().attack(), 
                AnsiCode.RED, AnsiCode.BOLD) + 
                "  " +
                AnsiCode.apply("DEF:" + pokesal.getDefence() + "/" + pokesal.getPokesalRecord().defense(), 
                AnsiCode.BLUE, AnsiCode.BOLD) +
                "  " +
                AnsiCode.apply("SPD:" + pokesal.getSpeed() + "/" + pokesal.getPokesalRecord().speed(), 
                AnsiCode.ORANGE, AnsiCode.BOLD);
            pokesalOptions.add(optionText);
        }
        options.add(pokesalOptions.toArray(new String[0]));
        options.setActionToAll(this::selectedPokesal);
        if (pokedeck.getSize() > 0) {
            options.add("Mover para cima", this::selectedOption);
            options.add("Mover para baixo", this::selectedOption);
            options.add("Descartar", this::selectedOption);
        }
        options.add("Voltar", this::selectedOption);
    }

    private void selectedPokesal(String text) {
        nextMenu = this;
    }

    private void selectedOption(String text) {
        switch (text) {
            case "Mover para cima" -> {
                boolean hasMoved = 
                pokedeck.moveUp(selectedPokesal-1);
                if (hasMoved)
                    selectedPokesal--;
                nextMenu = this;
            }
            case "Mover para baixo" -> {
                boolean hasMoved = 
                pokedeck.moveDown(selectedPokesal-1);
                if (hasMoved)
                    selectedPokesal++;
                nextMenu = this;
            }
            case "Descartar" -> {
                pokedeck.remove(selectedPokesal-1);
                selectedPokesal = 0;
                nextMenu = this;
            }
            case "Voltar" -> {
                nextMenu = caller;
            }
            default -> {
                System.exit(1);
            }
        }
    }

    @Override
    public void display(PrintStream out) {
        defineOptions();
        if (pokedeck.getSize() > 0) {
            out.println("Pokedeck:");
            out.println("Você tem " + pokedeck.getSize() + " pokesal" + (pokedeck.getSize() == 1 ? "" : "s") + " na sua pokedeck");
        } else {
            out.println("Você não tem nenhum pokesal na sua pokedeck :(\n");
        }
        for (int i = 1; i <= pokedeck.getSize(); i++) {
            out.println();
            if (i <= pokedeck.getSize()) {
                if (i == selectedPokesal) 
                    out.print(AnsiCode.REVERSE);
                options.display(i, out);
                    out.print(AnsiCode.RESET);
            } else {
                options.display(i, out);
            }
        }
        if (selectedPokesal == 0) 
            out.println("\n");
        else 
            out.println("\n" + AnsiCode.apply(
                pokedeck.get(selectedPokesal-1).getPokesalRecord().name() + ": " +
                pokedeck.get(selectedPokesal-1).getPokesalRecord().description(), AnsiCode.DIM, AnsiCode.ITALIC
            ));
        
        out.println(AnsiCode.BOLD + "" + AnsiCode.CYAN);
        for (int i = pokedeck.getSize() + 1; i < pokedeck.getSize() + 5; i++) {
            
            options.display(i, out);
            if (options.get(i).equals("Voltar")) 
                break; 
        }
        out.print(AnsiCode.RESET);
    }

    @Override
    public String getInput(Scanner scanner) {
        int input = 0;
        
        try {
            input = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            input = 0;
        }

        if (input > 0 && input <= pokedeck.getSize()){
            if (input == selectedPokesal)
                selectedPokesal = 0;
            else 
                selectedPokesal = input;
        }
        options.choose(input);
        return String.valueOf(input);
    }
    
    @Override
    public Menu next() {
        return nextMenu;
    }
}
