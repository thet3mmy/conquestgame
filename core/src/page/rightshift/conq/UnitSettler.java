package page.rightshift.conq;

import com.badlogic.gdx.math.Vector2;

public class UnitSettler extends Unit {
    UnitSettler(Vector2 p, int o) {
        super(p, o, Game.set.getTile(4), 5f);
        canSettle = true;
        displayName = "Settler";
        owner = o;
        prodCost = 25; // make settlers very expensive
        damage = 0;
        hp = 10;
    }

    @Override
    public void turnEnded() {
        maxMove = 5f;
    }
}
