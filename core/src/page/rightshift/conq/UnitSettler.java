package page.rightshift.conq;

import com.badlogic.gdx.math.Vector2;

public class UnitSettler extends Unit {
    UnitSettler(Vector2 p, int o) {
        super(p, o, Game.set.getTile(4), 2.5f);
        canSettle = true;
    }
}
