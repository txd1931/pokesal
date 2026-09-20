package dev.rhtj.pokesal.entities;

import java.util.Random;

import dev.rhtj.pokesal.PokesalRegistry;

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
        "Laura", "Iasmin", "Giovana", "Isabela", "Lívia", 
    };

    public static final int MAX_NAME_LENGTH = 15;
    
    private String name;
    private Appearance appearence;
    private int cash;
    private int score;
    private Backpack backpack;
    private Pokedeck pokedeck;
    private int xPos = -1;
    private int yPos = -1;
    private Battle battle;
    private Bot bot;

    private boolean isPlayer = false;

    public Trainer(boolean isPlayer) {
        this.isPlayer = isPlayer;
        this.name = DEFAULT_NAMES[new Random().nextInt(DEFAULT_NAMES.length)];
        appearence = getRandomAppearance();
        backpack = new Backpack(this);
        pokedeck = generateRandomPokedeck();
        cash = new Random().nextInt(5, 30) * 10;
        if (this.isPlayer) {
            cash = 800;
        } else {
            bot = new Bot(this);
        }
    }

    private Pokedeck generateRandomPokedeck() {
        Random random = new Random();
        Pokedeck newPokedeck = new Pokedeck();
        int index = 0;
        Pokesal newPokesal = null; 
        int pokesalRegitrySize = PokesalRegistry.getRegistrySize();

        for (int i = 0; i <= random.nextInt(10); i++) {
            index = random.nextInt(pokesalRegitrySize);
            newPokesal = new Pokesal(PokesalRegistry.getById(index));
            newPokedeck.add(newPokesal);
        }
        return newPokedeck;
    }

    private Appearance getRandomAppearance() {
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

    public Pokedeck getPokedeck() {
        return pokedeck;
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

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
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

    public void setBattle(Battle battle) {
        this.battle = battle;
    }

    public Battle getBattle() {
        return battle;
    }

    public Bot getBot() {
        return bot;
    }
    
    public void setBot(Bot bot) {
        this.bot = bot;
    }
}
