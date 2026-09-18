package dev.rhtj.pokesal.interfaces;

import java.io.PrintStream;
import java.util.Scanner;

import dev.rhtj.pokesal.AnsiCode;

public class ExitMenu implements Menu {

    private boolean guaranteedSavedGame = false;
    private Menu nextMenu = null;
    private Menu caller = null;

    public ExitMenu(Menu caller) {
        this.caller = caller;
    }

    private void printOptions(PrintStream out) {
        out.println("1 - voltar");
        if (!guaranteedSavedGame) {
            out.println("2 - salvar");
        }

        out.println((guaranteedSavedGame ? "2" : "3") + " - sair");
    }

    @Override
    public void display(PrintStream out) {    
        out.println(
            AnsiCode.apply(
                "Tem certesa que deseja sair de Pokesal?", 
                AnsiCode.BOLD, 
                AnsiCode.WHITE
            )
        );
        if (guaranteedSavedGame) {
            out.println(
                AnsiCode.apply(
                    "O jogo esta salvo e voce nao perdera nenhum progresso", 
                    AnsiCode.ITALIC, 
                    AnsiCode.GREEN
                ) + "\n"
            );    
        } else {
            out.println(
                AnsiCode.apply(
                    "Voce pode perder progresso ao sair sem salvar.", 
                    AnsiCode.ITALIC, 
                    AnsiCode.RED
                ) + "\n"
            );    
        }
        
        printOptions(out);
    }

    @Override
    public String getInput(Scanner scanner) {
        int input = 0;
        try {
            input = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {}
        if (input == 1) {
            nextMenu = caller;
        } else if (guaranteedSavedGame && input == 2 ||
            !guaranteedSavedGame && input == 3) {
            nextMenu = null;
        } else if (!guaranteedSavedGame && input == 2) {
            guaranteedSavedGame = true;
            nextMenu = this;
        } else {
            nextMenu = this;
        }
        return String.valueOf(input);
    }

    @Override
    public Menu next() {
        return nextMenu;
    }
}
