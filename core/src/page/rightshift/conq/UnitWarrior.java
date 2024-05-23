package page.rightshift.conq;

import com.badlogic.gdx.math.Vector2;

public class UnitWarrior extends Unit {
    UnitWarrior(Vector2 p, int o) {
        super(p, o, Game.set.getTile(5), 4f);
        canSettle = false;
        displayName = "Warrior";
        owner = o;
        prodCost = 10;
        damage = 25;
    }

    @Override
    public void turnEnded() {
        maxMove = 4f;
    }

    @Override
    public void think() {
        int x = (int)pos.x;
        int y = (int)pos.y;
        MyCell c = (MyCell)Game.objLayer.getCell(x, y);
        if(c != null) {
            if(c instanceof Town) {
                if(c.owner != owner) {
                    c.owner = owner;
                }
            }
        }
        super.think();
    }
}
