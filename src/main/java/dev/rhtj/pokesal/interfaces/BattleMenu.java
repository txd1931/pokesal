package dev.rhtj.pokesal.interfaces;

import java.io.PrintStream;
import java.util.Scanner;

import dev.rhtj.pokesal.AnsiCode;
import dev.rhtj.pokesal.Game;
import dev.rhtj.pokesal.entities.Battle;
import dev.rhtj.pokesal.entities.Pokesal;
import dev.rhtj.pokesal.entities.Trainer;
import dev.rhtj.pokesal.entities.Pokesal.Effect;

public class BattleMenu implements Menu {

    private Battle battle = null;
    private Menu nextMenu = null;
    private Battle.State battleState;
    private Options options = null;
    private int input = 0;
    private int priorCash;
    private Menu caller = null;

    public BattleMenu(Menu caller) {
        this.caller = caller;
        battle = createBattle();
        battleState = battle.getState();
        options = new Options();
        priorCash = battle.getTrainer(0).getCash();
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
                nextMenu = caller;
            }
            case "Voltar" -> {
                nextMenu = caller;
            }
            default -> {
                System.err.println("Texto de botão não reconhecido " + text);
                System.exit(1);
            }
        }
    }

    @Override
    public void display(PrintStream out) {
        battleState = battle.getState();
        setupOptions();
        out.println(
            AnsiCode.apply(
                "BATALHA",
                AnsiCode.BOLD, AnsiCode.RED 
            ) + "\n\n"
        );
        if (battleState.hasEnded()) {
            printBattleResults(out);
        } 
        else if (battleState.isActive()) {
            printBattleInfo(out);
        }
        else {
            System.err.print("Display em Batalha com estado nulo");
        }
        options.displayAll(out);
    }

    private void printBattleResults(PrintStream out) {
        int cashDiffMod = battle.getTrainer(0).getCash() - priorCash;
        if (cashDiffMod < 0) 
            cashDiffMod = 0 - cashDiffMod;
        out.println(
            battleState == Battle.State.TRAINER_A_WON ? 
            AnsiCode.apply(
                "VOCÊ GANHOU\n",
                AnsiCode.ITALIC, AnsiCode.BLUE
            ) +
            AnsiCode.apply(
                "$" + battle.getWinner().getCash(), 
                AnsiCode.BOLD, AnsiCode.WHITE
            ) + 
            AnsiCode.apply(
                "(+$" + cashDiffMod + ")", 
                AnsiCode.BOLD, AnsiCode.WHITE
            ):
            AnsiCode.apply(
                "VOCÊ PERDEU",
                AnsiCode.ITALIC, AnsiCode.RED
            ) + "\n" +
            AnsiCode.apply(
                "$" + battle.getWinner().getCash(), 
                AnsiCode.BOLD, AnsiCode.WHITE
            ) + 
            AnsiCode.apply(
                "(-$" + cashDiffMod + ")", 
                AnsiCode.BOLD, AnsiCode.WHITE
            )
        );
    }

    private void printBattleInfo(PrintStream out) {
        Trainer playingTrainer = battle.getCurrentTrainer();
        out.println(
            switch (battleState) {
                case NULL -> "NULL";
                case TRAINER_A_TURN, TRAINER_B_TURN -> "Vez de "  +  playingTrainer.getNameWithAppearence();
                case TRAINER_A_WON, TRAINER_B_WON -> "Vencedor: "  +  playingTrainer.getNameWithAppearence();
                default -> "DEFAULT";
            } + "\n"
        );
        for (int i = 0; i < 2; i++) {
            Pokesal pokesal = battle.getTrainer(i).getPokedeck().getNext();
            out.println(
                battle.getTrainer(i).getNameWithAppearence() + " - " + 
                AnsiCode.apply(
                    pokesal.getPokesalRecord().name(),
                    AnsiCode.BOLD, AnsiCode.WHITE
                ) + " - " +
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
                AnsiCode.ORANGE, AnsiCode.BOLD)
            );
            for (Pokesal.Effect effect : Pokesal.Effect.values()) {
                out.print(
                    AnsiCode.apply(
                        " " + effect.getName() + ": " + 
                        (Double.valueOf(pokesal.getEffect(effect) * 100d).intValue()),
                        AnsiCode.ORANGE
                    )
                );
            }
            out.println("\n");
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
                battle.getCurrentTrainer().getBot().run();
                nextMenu = this;
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
