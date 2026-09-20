package dev.rhtj.pokesal.interfaces;

import java.io.PrintStream;
import java.util.Scanner;

import dev.rhtj.pokesal.AnsiCode;
import dev.rhtj.pokesal.Game;
import dev.rhtj.pokesal.entities.Trainer;

public class MainMenu implements Menu{
    
    private boolean greetPlayer = true;
    private int input = 0;
    private Trainer player = null;
    private Options options = null;
    private Menu nextMenu = this;
    private Menu gameMenu = null;

    public MainMenu() {
        player = Game.getInstance().getWorld().getPlayer();
        options = new Options();
        setupOptions();
    }

    private void setupOptions() {
        options.clear();
        options.add("Continuar Jogo");
        options.add("Sobre");
        options.add("Customizar seu Treinador");
        options.add("Sair");
        options.add("Mochila");
        options.add("Pokedeck");
        options.add("Loja de Pokesals");
        options.add("Loja de Itens");
        options.setActionToAll(this::selectOption);
    }

    private void selectOption(String text) {
        switch (text) {
            case "Continuar Jogo" -> {
                if (gameMenu == null) {
                    nextMenu = gameMenu = new GameMenu(this);
                    
                } else {
                    nextMenu = gameMenu;
                }
            } 
            case "Sobre" -> {
                nextMenu = new AboutMenu(this);
            }
            case "Customizar seu Treinador" -> {
                nextMenu = new TrainerMenu(player, this);
            }
            case "Sair" -> {
                nextMenu = new ExitMenu(this);
            }
            case "Mochila" -> {
                nextMenu = new BackpackMenu(player.getBackpack(), false, true, this);
            } 
            case "Pokedeck" -> {
                nextMenu = new PokedeckMenu(player, this);
            }
            case "Loja de Pokesals" -> {
                nextMenu = new PokesalStoreMenu(this);
            }
            case "Loja de Itens" -> {
                nextMenu = new ItemStoreMenu(this);
            }
            default -> {
                nextMenu = this;
            }
        }
    }

    private void showGreetMessage(PrintStream out) {
        out.println(
            AnsiCode.apply("Olá, bem vindo à Pokesal", 
                AnsiCode.BOLD, AnsiCode.BRIGHT_WHITE
            ) + "\n"
        );
        out.println(
            AnsiCode.apply("pressione enter para começar", 
                AnsiCode.BOLD, AnsiCode.BRIGHT_WHITE
            ) + "\n"
        );
    }

    @Override
    public void display(PrintStream out) {
        nextMenu = this;
        if (greetPlayer) {
            showGreetMessage(out);
            return;
        }
        out.println(
            AnsiCode.apply(
                "PokeSal - Menu", 
                AnsiCode.BOLD,
                AnsiCode.CYAN
            )
        );
        setupOptions();
        options.displayAll(out);
    }

    @Override
    public String getInput(Scanner scanner) {
        if (!greetPlayer) {
            try {
                input = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                input = 0;
            }
        } else {
            scanner.nextLine();
        }
        greetPlayer = false;

        options.choose(input);

        return String.valueOf(input);
    }

    @Override
    public Menu next() {
        return nextMenu;
    }
}
