package page.rightshift.conq;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Util {
    public static Vector2 getRealMousePos() {
        Vector3 pos3 = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        Vector3 real3 = Game.camera.unproject(pos3);
        return new Vector2(real3.x, real3.y);
    }

    public static Vector2 realToTilePos(Vector2 real) {
        return new Vector2((float)Math.floor(real.x / 64), (float)Math.floor(real.y / 64));
    }

    public static Vector2 getTileMousePos() {
        return realToTilePos(getRealMousePos());
    }

    public static Cell getCurCell(TiledMapTileLayer layer) {
        Vector2 tilePos = getTileMousePos();
        int x = (int)tilePos.x;
        int y = (int)tilePos.y;
        return layer.getCell(x, y);
    }
}
