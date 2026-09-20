package dev.rhtj.pokesal;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import dev.rhtj.pokesal.entities.Battle;
import dev.rhtj.pokesal.entities.Trainer;

public class World {

    public enum TerrainType {
        HOT_ASPHALT("Asfalto Quente"),
        RAIN_PUDDLE("Poça de Chuva"),
        BOULEVARD("Canteiro Central"),
        WATER("Agua"),
        STORE("Loja");

        private final String name;

        TerrainType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public enum Direction {
        NULL(0, 0),
        TOP_LEFT(-1, -1),
        TOP(0, -1),
        TOP_RIGHT(1, -1),
        LEFT(-1, 0),
        RIGHT(1, 0),
        BOTTOM_LEFT(-1, 1),
        BOTTOM(0, 1),
        BOTTOM_RIGHT(1, 1);

        private int xMove, yMove;

        Direction(int xMove, int yMove) {
            this.xMove = xMove;
            this.yMove = yMove;
        }

        public int getxMove() {
            return xMove;
        }

        public int getyMove() {
            return yMove;
        }
    }

    private static final int WATER_PERCENTAGE = 10;
    private static final int STORE_PERCENTAGE = 20;
    private static final int TRAINER_PERCENTAGE = 15;

    private List<Battle> battles;    
    private List<Trainer> trainers;
    private TerrainType[][] map;

    public World(int size) {
        battles = new ArrayList<>();
        trainers = new ArrayList<>();
        long seed = new Random().nextLong();
        spawnTrainers(size, seed);
        map = generateMap(size, seed);
    }

    private void spawnTrainers(int size, long seed) {
        int trainerCount = (int) ((size * size) * (TRAINER_PERCENTAGE * 0.01f));
        Set<String> positions = new HashSet<>();
        Random random = new Random(seed);
        for (int i = 0; i < trainerCount; i++) {
            int x, y;
            String key;
            do {
                x = random.nextInt(size);
                y = random.nextInt(size);
                key = x + ", " + y;
            } while (positions.contains(key));
            Trainer newTreiner = new Trainer(i == 0);
            newTreiner.move(x, y);
            trainers.add(newTreiner);
        }
    }

    private TerrainType[][] generateMap(int size, long seed) {
        Random random = new Random(seed);
        TerrainType[][] terrain = new TerrainType[size][size];
        int storesSet = 0;
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                Integer roll = random.nextInt(3);
                terrain[y][x] = switch(roll) {
                    case 0 -> TerrainType.HOT_ASPHALT;
                    case 1 -> TerrainType.RAIN_PUDDLE;
                    case 2 -> TerrainType.BOULEVARD;
                    default -> TerrainType.WATER;
                };
                
                roll = random.nextInt(100);
                if (roll < WATER_PERCENTAGE)
                    terrain[y][x] = TerrainType.WATER;
                roll = random.nextInt(100);
                if (roll < STORE_PERCENTAGE) {
                    storesSet++;
                    terrain[y][x] = TerrainType.STORE;
                }
            }
        }
        if (storesSet == 0)
            set(0, 0, TerrainType.STORE);
        return terrain;
    }

    public Trainer getPlayer() {
        return trainers.get(0);
    }

    public Trainer getTrainer(int i) {
        return trainers.get(i);
    }

    public Trainer getRandomNPC() {
        Random random = new Random();
        return trainers.get(random.nextInt(1, trainers.size()));
    }

    public Trainer[] getTrainersAt(int x, int y) {
        List<Trainer> foundTrainers = new ArrayList<>();
        Trainer current = null;
        for (int i = 0; i < map.length; i++) {
            current = trainers.get(i);
            if (current.getXPos() == x && current.getYPos() == y) {
                foundTrainers.add(current);
            }
        }
        return (Trainer[]) foundTrainers.toArray();
    }

    public boolean isValidPosition(int x, int y) {
        if (x < 0 || x >= map.length) 
            return false;
        if (y < 0 || y >= map[y].length) 
            return false;
        return true;
    }

    public TerrainType get(int x, int y) {
        if (!isValidPosition(x, y))
            return TerrainType.WATER;
        return map[y][x];
    }

    public void set(int x, int y, TerrainType terrainType) {
        if (!isValidPosition(x, y))
            throw new IndexOutOfBoundsException();
        map[y][x] = terrainType;
    }

    public boolean attemptToMove(Trainer trainer, Direction direction) {
        int targetXPos = trainer.getXPos() + direction.xMove;
        int targetYPos = trainer.getYPos() + direction.yMove;
        
        if (get(targetXPos, targetYPos) == TerrainType.WATER)
            return false; 

        if (getTrainersAt(targetXPos, targetYPos).length >= 2)
            return false;
        
        trainer.move(targetXPos, targetYPos);
        return true;
    }

}
