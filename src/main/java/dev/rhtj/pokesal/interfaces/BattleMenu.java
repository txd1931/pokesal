package dev.rhtj.pokesal.interfaces;

import java.io.PrintStream;
import java.util.Scanner;

import dev.rhtj.pokesal.AnsiCode;
import dev.rhtj.pokesal.Game;
import dev.rhtj.pokesal.entities.Battle;
import dev.rhtj.pokesal.entities.Trainer;

public class BattleMenu implements Menu {

    private Battle battle = null;
    private Menu nextMenu = null;
    private Battle.State battleState;
    private Options options = null;
    private int input = 0;

    public BattleMenu() {
        
        battle = createBattle();
        battleState = battle.getState();
        options = new Options();
    }

    private Battle createBattle() {
        Trainer player = Game.getInstance().getWorld().getTrainer(0);
        Trainer bot = Game.getInstance().getWorld().getRandomNPC();
        return new Battle(player, bot);
    }

    private void setupOptions() {
        options.clear();
        switch (battleState) {
            case NULL -> {
                System.err.println("Estado de batalha nulo");
                System.exit(1);
            }
            case TRAINER_A_TURN -> {
                options.add("Atacar");
                options.add("Mochila");
                options.add("Desistir");
            }
            case TRAINER_B_TURN -> {

            }
            case TRAINER_A_WON, TRAINER_B_WON -> {
                options.add("Voltar");
            }
        }
        options.setActionToAll(this::selectedOption);
    }

    private void selectedOption(String text) {
        switch (text) {
            case "Atacar" -> {
                nextMenu = this;
                battle.atack();
            }
            case "Mochila" -> {
                nextMenu = new BackpackMenu(battle.getCurrentTrainer().getBackpack(), true, false, this);
            }
            case "Desistir" -> {
                battle.getCurrentTrainer().getPokedeck().remove(battle.getCurrentPokesal());
                nextMenu = new MainMenu();
            }
            case "Voltar" -> {
                nextMenu = new MainMenu();
            }
            default -> {
                System.err.println("Texto de botão não reconhecido " + text);
                System.exit(1);
            }
        }
    }

    @Override
    public void display(PrintStream out) {
        setupOptions();
        out.println(
            AnsiCode.apply(
                "BATALHA",
                AnsiCode.BOLD, AnsiCode.RED 
            ) + "\n\n"
        );
        printBattleInfo(out);
        options.displayAll(out);
    }

    private void printBattleInfo(PrintStream out) {
        for (int i = 0; i < 2; i++) {
            out.println(
                battle.getTrainer(i).getNameWithAppearence() + " - " + 
                AnsiCode.apply(
                    battle.getTrainer(i).getPokedeck().getNext().getPokesalRecord().name(),
                    AnsiCode.BOLD, AnsiCode.WHITE
                ) + "\n"
            );
        }
    }

    @Override
    public String getInput(Scanner scanner) {
        
        switch (battleState) {
            case TRAINER_A_TURN, TRAINER_A_WON, TRAINER_B_WON -> {
                try {
                    input = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) {
                    nextMenu = this;
                    return null;
                }
                if (input < 0 || input > options.getTotal()) {
                    nextMenu = this;
                    return null;
                }
                options.choose(input);
                return String.valueOf(input);
            } 
            case TRAINER_B_TURN -> {
                return null;

            }
            case NULL -> {
                System.err.println("Estado de batalha não iniciado");
                System.exit(1);
            }
        }
        return null;
    }

    @Override
    public Menu next() {
        return nextMenu;
    }
}
