package page.rightshift.conq;

import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;

public class ConqInputSystem implements InputProcessor {
    public boolean keyDown(int keycode) {return false;}
    public boolean keyUp(int keycode) {
        switch(keycode) {
            case Input.Keys.SPACE:
                Game.turnMovesLeft = 8;
                for(MyCell c: new LinkedList<MyCell>(Game.updateCells)) {
                    c.turnEnded();
                }

                // switch players
                if(Game.currentPlayer == 1) {
                    Game.currentPlayer = 2;
                } else {
                    Game.currentPlayer = 1;
                }

                System.out.println(Game.currentPlayer);
                break;
            case Input.Keys.BACKSLASH:
                if(Game.turnMovesLeft < 1) {break;}
                Town t = new Town(Game.tilePos, Game.currentPlayer, Game.set.getTile(3));
                Game.objLayer.setCell((int)Game.tilePos.x, (int)Game.tilePos.y, t);
                Game.updateCells.add(t);
                break;
            case Input.Keys.APOSTROPHE:
                if(Game.turnMovesLeft < 1) {break;}
                UnitSettler settler = new UnitSettler(Game.tilePos, Game.currentPlayer);
                Game.unitLayer.setCell((int)Game.tilePos.x, (int)Game.tilePos.y, settler);
                Game.updateCells.add(settler);
                break;
            case Input.Keys.S:
                if(Game.turnMovesLeft < 1) {break;}
                if(Game.selectedUnit == null) {break;}
                if(!(Game.selectedUnit instanceof Unit)) {break;}
                Unit u = (Unit)Game.selectedUnit;
                if(!u.canSettle) {break;}
                if(u.owner != Game.currentPlayer) {break;}
                Game.turnMovesLeft--;

                Town town = new Town(Game.selectedUnit.pos, u.owner, Game.set.getTile(3));
                Game.objLayer.setCell((int)Game.selectedUnit.pos.x, (int)Game.selectedUnit.pos.y, town);
                Game.updateCells.add(town);
                Game.updateCells.remove(Game.selectedUnit);
                Game.unitLayer.setCell((int)Game.selectedUnit.pos.x, (int)Game.selectedUnit.pos.y, null);

                Game.selectedUnit = null;
                break;
            case Input.Keys.NUM_1:
                if(Game.selectedUnit == null) {break;}
                if(!(Game.selectedUnit instanceof Town)) {break;}
                Town town2 = (Town)Game.selectedUnit;
                town2.produce(UnitSettler.class);
                break;
            case Input.Keys.NUM_2:
                if(Game.selectedUnit == null) {break;}
                if(!(Game.selectedUnit instanceof Town)) {break;}
                Town town3 = (Town)Game.selectedUnit;
                town3.produce(UnitWarrior.class);
                break;
        }
        return false;
    }
    public boolean keyTyped(char character) {return false;}
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        switch(button) {
            case 2:
                MyCell c = null;
                MyCell _c = getClickedCell(Game.unitLayer);
                if(_c != null) {
                    if(_c.owner == Game.currentPlayer) {
                        c = _c;
                    }
                }

                if(_c == null) {
                    MyCell _c2 = getClickedCell(Game.objLayer);
                    if(_c2 != null) {
                        if(_c2.owner == Game.currentPlayer) {
                            c = _c2;
                        }
                    }
                }
                Game.selectedUnit = c;
                break;
            case 1:
                if(Game.turnMovesLeft < 1) {break;}
                if(Game.selectedUnit == null) {break;}
                if(!(Game.selectedUnit instanceof Unit)) {break;}
                Unit u = (Unit)Game.selectedUnit;
                Unit target = (Unit)getClickedCell(Game.unitLayer);

                Vector2 tPos = Game.tilePos.cpy();
                float dst = tPos.dst(Game.selectedUnit.pos);

                if(target == null) {
                    if(dst < u.maxMove) {
                        Game.unitLayer.setCell((int)Game.selectedUnit.pos.x, (int)Game.selectedUnit.pos.y, null);
                        Game.unitLayer.setCell((int)tPos.x, (int)tPos.y, Game.selectedUnit);
                        Game.selectedUnit.pos = tPos;
                        u.maxMove -= dst;
                        Game.turnMovesLeft--;
                    }
                } else {
                    if(target.owner != u.owner) {
                        int dmg = (int)(u.damage + (u.maxMove * 2));    // more damage when you are closer
                        u.hp -= (target.damage - u.maxMove);
                        target.hp -= dmg;
                        u.maxMove = 0;
                        Game.turnMovesLeft--;
                    }
                }
        }
        return false;
    }
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {return false;}
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {return false;}
    public boolean touchDragged(int screenX, int screenY, int pointer) {return false;}
    public boolean mouseMoved(int screenX, int screenY) {return false;}
    public boolean scrolled(float amountX, float amountY) {
        Game.camera.zoom += amountY * 2 * Gdx.graphics.getDeltaTime();
        return false;
    }

    public MyCell getClickedCell(TiledMapTileLayer layer) {
        int x = (int)Game.tilePos.x;
        int y = (int)Game.tilePos.y;
        MyCell c = null;

        try {
            c = (MyCell)layer.getCell(x, y);
            if(c != null) {
                Game.selectedUnit = (Unit)c;
                System.out.println(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return c;
    }
}
