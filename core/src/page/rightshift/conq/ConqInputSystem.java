package page.rightshift.conq;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;

public class ConqInputSystem implements InputProcessor {
    public boolean keyDown(int keycode) {return false;}
    public boolean keyUp(int keycode) {
        switch(keycode) {
            case Input.Keys.SPACE:
                Game.turnMovesLeft = 4;
                for(MyCell c: Game.updateCells) {
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
                Game.turnMovesLeft--;
                break;
            case Input.Keys.APOSTROPHE:
                if(Game.turnMovesLeft < 1) {break;}
                UnitSettler settler = new UnitSettler(Game.tilePos, Game.currentPlayer);
                Game.unitLayer.setCell((int)Game.tilePos.x, (int)Game.tilePos.y, settler);
                Game.updateCells.add(settler);
                Game.turnMovesLeft--;
                break;
            case Input.Keys.S:
                if(Game.turnMovesLeft < 4) {break;}
                if(Game.selectedUnit == null) {break;}
                if(!Game.selectedUnit.canSettle) {break;}
                Game.turnMovesLeft = 0;

                Town town = new Town(Game.selectedUnit.pos, Game.currentPlayer, Game.set.getTile(3));
                Game.objLayer.setCell((int)Game.selectedUnit.pos.x, (int)Game.selectedUnit.pos.y, town);
                Game.updateCells.add(town);
                Game.updateCells.remove(Game.selectedUnit);
                Game.unitLayer.setCell((int)Game.selectedUnit.pos.x, (int)Game.selectedUnit.pos.y, null);

                Game.selectedUnit = null;
                break;
        }
        return false;
    }
    public boolean keyTyped(char character) {return false;}
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        switch(button) {
            case 2:
                int x = (int)Game.tilePos.x;
                int y = (int)Game.tilePos.y;
                try {
                    MyCell c = (MyCell)Game.unitLayer.getCell(x, y);

                    if(c != null) {
                        Game.selectedUnit = (Unit)c;
                        System.out.println(c);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                if(Game.turnMovesLeft < 1) {break;}
                if(Game.selectedUnit == null) {break;}
                Game.turnMovesLeft--;

                Vector2 tPos = Game.tilePos.cpy();
                float dst = tPos.dst(Game.selectedUnit.pos);
                if(dst < Game.selectedUnit.maxMove) {
                    Game.unitLayer.setCell((int)Game.selectedUnit.pos.x, (int)Game.selectedUnit.pos.y, null);
                    Game.unitLayer.setCell((int)tPos.x, (int)tPos.y, Game.selectedUnit);
                    Game.selectedUnit.pos = tPos;
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
}
