package page.rightshift.conq;

import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.math.Vector2;

public class Unit extends MyCell {
    public int hp = 100;
    public float maxMove = 1.5f;
    public boolean canSettle = false;
    public int owner;
    public int prodCost;
    public int damage;

    Unit(Vector2 p, int o, TiledMapTile t) {
        super(p, o);
        pos = p;
        setTile(t);
    }

    Unit(Vector2 p, int o, TiledMapTile t, float mm) {
        super(p, o);
        pos = p;
        setTile(t);
        maxMove = mm;
    }

    @Override
    public void renderStuff() {
        UIManager.font.draw(Game.batch, String.valueOf(owner), pos.x * 64, pos.y * 64 + 64);
        UIManager.font.draw(Game.batch, String.valueOf(hp), pos.x * 64, pos.y * 64 + 20);
    }

    public void think() {
        processIsDead();
    }

    public void processIsDead() {
        if (hp < 0) {
            int x = (int)pos.x;
            int y = (int)pos.y;
            Game.unitLayer.setCell(x, y, null);
            Game.updateCells.remove(this);
        }
    }
}
