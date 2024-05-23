package page.rightshift.conq;

import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

public class UIManager {
    public static BitmapFont font;
    public static SpriteBatch uiBatch;
    public static ShapeRenderer uiShapeRenderer;
    public static ShapeRenderer gridRenderer;
    public static LinkedList<String> infoPanelStrings;
    public static float pad = 8f;
    public static float height;
    public static Color p1Color;
    public static Color p2Color;

    UIManager() {
        uiBatch = new SpriteBatch();
        font = new BitmapFont();
        font.getData().setScale(1.5f);
        uiShapeRenderer = new ShapeRenderer();
        gridRenderer = new ShapeRenderer();
        infoPanelStrings = new LinkedList<>();
        height = Gdx.graphics.getHeight();

        p1Color = new Color(0.9f, 0.2f, 0.9f, 0.3f);
        p2Color = new Color(0.2f, 0.2f, 0.9f, 0.3f);
    }

    public void addLineToPanel(String line, int pos) {
        font.draw(uiBatch, line, 0+pad, height-((pad*3f) * (pos + 2)));
    }

    public void update() {
        float topY = height - pad;
        uiBatch.begin();
        font.draw(uiBatch, "Actions remaining: " + String.valueOf(Game.turnMovesLeft), 0, topY);
        font.draw(uiBatch, "Active player: " + String.valueOf(Game.currentPlayer), 350, topY);
        uiBatch.end();

        if(Game.selectedUnit != null) {
            // draw the side black panel to put the info on
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

            uiShapeRenderer.setColor(0.1f, 0.1f, 0.1f, 0.4f);
            uiShapeRenderer.begin(ShapeType.Filled);
            uiShapeRenderer.box(0, 0, 0, Gdx.graphics.getWidth() / 3, Gdx.graphics.getHeight(), 0);
            uiShapeRenderer.end();

            Gdx.gl.glDisable(GL20.GL_BLEND);

            // start adding the text to it
            uiBatch.begin();
            font.draw(uiBatch, "Selected: " + Game.selectedUnit.displayName, 0+pad, topY-16);

            addLineToPanel("Owner: " + String.valueOf(Game.selectedUnit.owner), 1);
            if(Game.selectedUnit instanceof Unit) {
                Unit u = (Unit)Game.selectedUnit;
                addLineToPanel("Health: " + String.valueOf(u.hp), 2);
                addLineToPanel("Moves: " + String.valueOf(u.maxMove), 3);
                addLineToPanel("Can settle: " + String.valueOf(u.canSettle), 4);
            }
            if(Game.selectedUnit instanceof Town) {
                Town t = (Town)Game.selectedUnit;

                // this is a devious hack to use reflection to get the displaynames
                // of items in the production queue by instantiating a new one at runtime.
                LinkedList<String> targetNames = new LinkedList<>();
                for(ProductionItem it: t.queue) {
                    targetNames.add(it.getDisplayNameOfTarget());
                }

                addLineToPanel("Population: " + String.valueOf(t.population), 2);
                addLineToPanel("Lifetime: " + String.valueOf(t.turnsAlive), 3);
                addLineToPanel("Next growth in: " + String.valueOf(t.nextGrowth - t.turnsAlive), 4);
                addLineToPanel("Control size: " + String.valueOf(t.size), 5);
                addLineToPanel("Coastal: " + String.valueOf(t.isCoastal), 6);
                addLineToPanel("Available food: " + String.valueOf(t.food), 7);
                addLineToPanel("Queue: " + String.valueOf(targetNames), 8);
            }

            uiBatch.end();
        }
    }

    public static void drawMapGrid() {
        ShapeRenderer r = gridRenderer;
        int huge = 64 * 128;

        r.setProjectionMatrix(Game.camera.combined);
        r.begin(ShapeType.Line);
        r.setColor(Color.BLACK);
        for(int i = -huge; i < huge; i += 64) {
            r.line(new Vector2(-huge, i), new Vector2(huge, i));
        }
        for(int j = -huge; j < huge; j += 64) {
            r.line(new Vector2(j, -huge), new Vector2(j, huge));
        }
        r.end();
    }

    public static void drawTerritoryControl() {
        ShapeRenderer r = gridRenderer;
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        r.begin(ShapeType.Filled);

        for(MyCell c: new LinkedList<>(Game.updateCells)) {
            if(c instanceof Town) {
                Town t = (Town)c;

                if(t.owner == 1) {
                    r.setColor(p1Color);
                } else {
                    r.setColor(p2Color);
                }
                
                int size = t.size;
                Vector2 start = new Vector2(t.pos.x - size, t.pos.y - size).scl(64);
                Vector2 end = new Vector2(t.pos.x + (size+1), t.pos.y + (size+1)).scl(64);
                r.box(start.x, start.y, 0f, end.x - start.x, end.y - start.y, 0f);
            }
        }

        r.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }
}
