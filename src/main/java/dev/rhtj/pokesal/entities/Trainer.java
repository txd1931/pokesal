package dev.rhtj.pokesal.entities;

public class Trainer {
    
    private String name;
    private String appearence;
    private int cash;
    private Backpack backpack;
    private int xPos;
    private int yPos;

    private boolean isPlayer = false;

    public Trainer(boolean isPlayer) {
        this.isPlayer = isPlayer;

        backpack = new Backpack(this);
    }

    public String getName() {
        return name;
    }

    public String getAppearence() {
        return appearence;
    }

    public int getCash() {
        return cash;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAppearence(String appearence) {
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
