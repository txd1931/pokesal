package dev.rhtj.pokesal.entities;

import java.util.List;
import java.util.Random;

public class Trainer {
    
    public enum Appearance {
        GREEN("Verde", "\u001B[32m"),
        RED("Vermelho", "\u001B[31m"),
        BLUE("Azul", "\u001B[34m"),
        ORANGE("Laranja", "\u001B[33m"),
        PURPLE("Roxo", "\u001B[35m"),
        YELLOW("Amarelo", "\u001B[38;5;214m"),
        CYAN("Ciano", "\u001B[36m"),
        LIME("lima", "\u001B[38;5;118m"); 

        public static final String RESET = "\u001B[0m";
        
        private final String name;
        private final String ansiCode;

        Appearance(String name, String ansiCode) {
            this.name = name;
            this.ansiCode = ansiCode;
        }

        public String getValue() {
            return this.ansiCode;
        }

        public String getName() {
            return this.name;
        }

        public String getNameWithAppearance() {
            return getValue() + getName() + RESET;
        }

        public static Appearance fromString(String input) {
            if (input == null) {
                return null;
            }
            String cleanedInput = input.replaceAll("\u001B\\[[;\\d]*m", "").trim();
            for (Appearance appearance : Appearance.values()) {
                if (appearance.name().equalsIgnoreCase(cleanedInput) || 
                    appearance.getName().equalsIgnoreCase(cleanedInput)) {
                    return appearance;
                }
            }
            return null;
        }
    }

    public static final String[] DEFAULT_NAMES = {
        "Rafael", "Hector", "João", "Thaylan", "Thiago", 
        "Felipe", "Rodrigo", "Gustavo", "Vitor", "Gabriel",     
        "Sophia", "Valentina", "Alice", "Beatriz", "Manuela", 
        "Laura", "Yasmin", "Giovanna", "Isabella", "Lívia", 
    };

    public static final int MAX_NAME_LENGTH = 15;
    
    private String name;
    private Appearance appearence;
    private int cash;
    private Backpack backpack;
    private int xPos = -1;
    private int yPos = -1;

    private boolean isPlayer = false;

    public Trainer(boolean isPlayer) {
        this.isPlayer = isPlayer;
        this.name = DEFAULT_NAMES[new Random().nextInt(DEFAULT_NAMES.length)];
        appearence = getRandomAppearance();
        backpack = new Backpack(this);
        cash = new Random().nextInt(5, 30) * 10;
    }

    public Appearance getRandomAppearance() {
        Appearance[] values = Appearance.values();
        int index = new Random().nextInt(values.length);
        return values[index];
    }

    public Backpack getBackpack() {
        return backpack;
    }

    public String getName() {
        return name;
    }
    
    public String getNameWithAppearence() {
        return appearence.getValue() + name + Appearance.RESET;
    }

    public Appearance getAppearence() {
        return appearence;
    }

    public int getCash() {
        return cash;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAppearence(Appearance appearence) {
        this.appearence = appearence;
    }

    public void setCash(int cash) {
        this.cash = cash;
    }
    
    public void addCash(int cash) {
        setCash(this.cash + cash);
    }

    public int getXPos() {
        return xPos;
    }
    public int getYPos() {
        return yPos;
    }

    public void move(int x, int y) {
        xPos = x;
        yPos = y;
    }
}
