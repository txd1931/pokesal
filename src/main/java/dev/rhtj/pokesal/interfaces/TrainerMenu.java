package dev.rhtj.pokesal.interfaces;

import java.io.PrintStream;
import java.util.Scanner;

import dev.rhtj.pokesal.AnsiCode;
import dev.rhtj.pokesal.entities.Trainer;
import dev.rhtj.pokesal.entities.Trainer.Appearance;

public class TrainerMenu implements Menu {
    

    private enum SubMenu {
        INITIAL_SELECTION,
        NAME_SELECTION,
        APPEARANCE_SELECTION
    }

    private SubMenu subMenu = null;
    private Trainer trainer = null;
    private Menu caller = null;
    private Menu nextMenu = null;
    private Options options;

    public TrainerMenu(Trainer trainer, Menu caller) {
        subMenu = SubMenu.INITIAL_SELECTION;
        this.caller = caller;
        this.trainer = trainer;
        options = new Options();
        initalMenu();
    }

    public void initalMenu() {
        
    }

    private void selectedChangeName(String text) {
        subMenu = SubMenu.NAME_SELECTION;
        nextMenu = this;
    }

    private void selectedChangeAppearence(String text) {
        subMenu = SubMenu.APPEARANCE_SELECTION;
        nextMenu = this;
    }
    
    @Override
    public void display(PrintStream out) {
        switch (subMenu) {
            case INITIAL_SELECTION -> {
                initialSelectionDisplay(out);
            }
            case NAME_SELECTION -> {
                nameSelectionDisplay(out);
            }
            case APPEARANCE_SELECTION -> {
                appearanceSelectionDisplay(out);
            }
            default -> {
                System.exit(1);
            }
        }
    }

    private void initialSelectionDisplay(PrintStream out) {
        options.clear();
        options.add("Nome", this::selectedChangeName);
        options.add("Aparencia", this::selectedChangeAppearence);
        options.add("Voltar", (text) -> { nextMenu = caller; });
        
        out.println(
            AnsiCode.apply(
                "SEU TREINADOR",
                AnsiCode.CYAN,
                AnsiCode.BOLD
            )
        );
        
        out.println("\n" + Trainer.Appearance.RESET + trainer.getNameWithAppearence());
        
        out.println(
            AnsiCode.apply(
                "$" + String.valueOf(trainer.getCash()) + "\n",
                AnsiCode.GREEN,
                AnsiCode.BOLD
            )
        );
        out.println(
            AnsiCode.apply(
                "Selecione o que você deseja alterar:",
                AnsiCode.WHITE
            )
        );
        options.displayAll(out);
    }

    private void nameSelectionDisplay(PrintStream out) {
        out.println(
            AnsiCode.apply(
                "Insira um nome de no máximo " + Trainer.MAX_NAME_LENGTH + " caracteres",
                AnsiCode.YELLOW
            )
        );
    }

    private void appearanceSelectionDisplay(PrintStream out) {
        int colorOptionsCount = Trainer.Appearance.values().length;
        String[] colorOptions = new String[colorOptionsCount];
        for (int i = 0; i < colorOptionsCount; ++i) {
            colorOptions[i] = 
                Trainer.Appearance.values()[i].getValue() + 
                Trainer.Appearance.values()[i].getName() +
                Trainer.Appearance.RESET;
        }
        options.clear();
        options.add(colorOptions);
        out.println(
            AnsiCode.apply(
                "Selecione a aparencia do seu treinador", 
                AnsiCode.WHITE
            )
        );
        Trainer.Appearance currentAppearance = trainer.getAppearence();
        out.println("\nAtual: " + currentAppearance.getValue() + currentAppearance.getName() + Trainer.Appearance.RESET + "\n");
        options.displayAll(out);
    }


    @Override
    public String getInput(Scanner scanner) {
        switch (subMenu) {
            case INITIAL_SELECTION -> {
                return initialSelectionInput(scanner);
            }
            case NAME_SELECTION -> {
                return nameSelectionInput(scanner);
            }
            case APPEARANCE_SELECTION -> {
                return appearanceSelectionInput(scanner);
            }
            default -> {
                System.exit(1);
                return "";
            }
        }
    }

    private String initialSelectionInput(Scanner scanner) {
        int input = 0;
        try {
            input = Integer.parseInt(scanner.nextLine());
        } catch (Exception e) {}
        
        if(!options.choose(input)) 
            nextMenu = this;

        return String.valueOf(input);
    }

    private String nameSelectionInput(Scanner scanner) {
        String input = "";
        input = scanner.nextLine();
        if (input == null) {
            nextMenu = this;
            return "";
        }
        if (input.length() > Trainer.MAX_NAME_LENGTH) {
            return "";
        }
        trainer.setName(input);
        subMenu = SubMenu.INITIAL_SELECTION;
        return input;
    }

    private String appearanceSelectionInput(Scanner scanner) {
        int input = 0;
        input = Integer.valueOf(scanner.nextLine());
        options.choose(input);
        for (Appearance appearance : Trainer.Appearance.values()) {
            System.out.println(appearance.getNameWithAppearance() + " --- " + 
                options.result()
            );
            if (appearance.getNameWithAppearance().equals(options.result())) {
                trainer.setAppearence(appearance);
                subMenu = SubMenu.INITIAL_SELECTION;
                return String.valueOf(input);
            }
        }
        return String.valueOf(input);
    }

    @Override
    public Menu next() {
        return nextMenu;
    }
}
