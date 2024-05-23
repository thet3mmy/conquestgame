package page.rightshift.conq;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Vector2;

public class MyCell extends Cell {
    public Vector2 pos;
    public int owner;
    public String displayName;
    public void turnEnded() {return;}
    public void renderStuff() {return;}

    MyCell(Vector2 p, int o) {
        super();
        pos = p;
        owner = o;
    }

    public String getDisplayName() {
        return displayName;
    }
}
