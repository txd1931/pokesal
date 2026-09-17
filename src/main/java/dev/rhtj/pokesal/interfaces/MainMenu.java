package dev.rhtj.pokesal.interfaces;

import java.io.PrintStream;
import java.util.Scanner;

import dev.rhtj.pokesal.AnsiCode;

public class MainMenu implements Menu{
    
    private boolean greetPlayer = true;
    private int input = 0;

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

    private void showOptions(PrintStream out) {
        out.println(
            AnsiCode.apply(
                "PokeSal - Menu", 
                AnsiCode.BOLD,
                AnsiCode.CYAN
            )
        );
        out.println("1 - Continuar Jogo");
        out.println("2 - Sobre");
        out.println("3 - Inventario");
        out.println("4 - Sair");
    }
    
    @Override
    public void display(PrintStream out) {
        if (greetPlayer) {
            showGreetMessage(out);
            return;
        }
        
        showOptions(out);

    }

    @Override
    public String getInput(Scanner scanner) {
        if (!greetPlayer) {
            try {
                input = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {}
        } else {
            scanner.nextLine();
        }
        greetPlayer = false;

        return String.valueOf(input);
    }

    @Override
    public Menu next() {
        return switch (input) {
            case 1 -> new GameMenu();
            case 2 -> new AboutMenu(this);
            case 3 -> new BackpackMenu(this);
            case 4 -> new ExitMenu(this);
            default -> this;
        };
    }
}
