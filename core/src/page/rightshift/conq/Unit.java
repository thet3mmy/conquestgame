package page.rightshift.conq;

import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.math.Vector2;

public class Unit extends MyCell {
    public int hp = 100;
    public float maxMove = 1.5f;
    public boolean canSettle = false;
    public int owner;

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
}
