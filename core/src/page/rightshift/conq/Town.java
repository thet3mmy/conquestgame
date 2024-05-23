package page.rightshift.conq;

import java.util.LinkedList;

import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Vector2;

public class Town extends MyCell {
    public boolean isCoastal;
    public int population;
    public int food;
    public int size = 1;
    public int turnsAlive = 0;
    public int nextGrowth = -999;
    public LinkedList<ProductionItem> queue;

    Town(Vector2 p, int o, TiledMapTile t) {
        super(p, o);
        pos = p.cpy(); // no idea if this is needed

        population = 1;
        food = 0;

        setTile(t);

        // determine if this is a coastal city
        isCoastal = false;
        LinkedList<Cell> cells = getAdjacentCells();
        for(Cell c: cells) {
            Boolean isWater = (Boolean)c.getTile().getProperties().get("water");
            if(isWater != null) {
                if(isWater) {
                    isCoastal = true;
                }
            }
        }

        displayName = "Town";
        queue = new LinkedList<>();

    }

    public LinkedList<Cell> getAdjacentCells() {
        int x = (int)pos.x;
        int y = (int)pos.y;
        LinkedList<Cell> aCells = new LinkedList<>();

        for(int a = x-size; a <= x+size; a++) {
            for(int b = y-size; b <= y+size; b++) {
                Cell c = Game.baseLayer.getCell(a, b);
                if(c != null) {
                    aCells.add(c);
                }
            }
        }
        return aCells;
    }

    public int calculateAvailableFood() {
        LinkedList<Cell> aCells = getAdjacentCells();
        int food = 0;

        for(Cell c: aCells) {
            TiledMapTile tile = c.getTile();
            food += ((Integer)tile.getProperties().get("food")).intValue();
        }
        
        return food;
    }

    public void growPopulation() {
        int turnsToGrow = 16 - ((int)(food / 2.5) / population);

        if(turnsAlive >= nextGrowth) {
            population++;
            nextGrowth = turnsAlive + turnsToGrow;
            if(population % 10 == 0) {
                size++;
            }
        }
    }

    @Override
    public void turnEnded() {
        turnsAlive++;
        food = calculateAvailableFood();
        growPopulation();
        for(ProductionItem it: new LinkedList<>(queue)) {
            it.progress++;
            if(it.progress >= it.complete) {
                try {
                    int x = (int)pos.x;
                    int y = (int)pos.y;

                    Unit u = it.produce();
                    Game.updateCells.add(u);
                    Game.unitLayer.setCell(x, y, u);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void produce(Class<? extends Unit> c) {
        ProductionItem it = new ProductionItem(turnsAlive, 5, c, this);
        queue.add(it);
        int index = queue.indexOf(it);
        it.complete += index * 2;
    }

    @Override
    public void renderStuff() {
        UIManager.font.draw(Game.batch, String.valueOf(population), pos.x * 64, pos.y * 64 + 12);
    }
}
