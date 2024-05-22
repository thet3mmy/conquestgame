package page.rightshift.conq;

import java.util.LinkedList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

public class Game extends ApplicationAdapter {
	static SpriteBatch batch;
	OrthogonalTiledMapRenderer renderer;

	static TiledMapTileLayer baseLayer;
	static TiledMapTileLayer objLayer;
	static TiledMapTileLayer unitLayer;
	static TiledMapTileSet set;

	static ConqCamera camera;
	ConqInputSystem inputSystem;
	ShapeRenderer srend;
	static TiledMap map;

	static int turnMovesLeft = 4;
	static LinkedList<MyCell> updateCells;
	static Vector2 tilePos;
	static BitmapFont font;
	static Unit selectedUnit;

	static Color player1Color;
	static Color player2Color;
	static int currentPlayer = 1;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		map = new TmxMapLoader().load("map.tmx");
		camera = new ConqCamera(1024, 768);
		camera.setToOrtho(false);
		camera.update();
		renderer = new OrthogonalTiledMapRenderer(map);
		renderer.setView(camera);
		batch.setProjectionMatrix(camera.combined);
		srend = new ShapeRenderer();

		font = new BitmapFont();
		font.getData().scale(1.5f);
		
		baseLayer = (TiledMapTileLayer)map.getLayers().get(0);
		objLayer = (TiledMapTileLayer)map.getLayers().get(1);
		unitLayer = (TiledMapTileLayer)map.getLayers().get(2);
		set = map.getTileSets().getTileSet(0);
		updateCells = new LinkedList<>();

		inputSystem = new ConqInputSystem();
		Gdx.input.setInputProcessor(inputSystem);

		player1Color = new Color(0.9f, 0.2f, 0.9f, 0.3f);
		player2Color = new Color(0.2f, 0.9f, 0.4f, 0.3f);
	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 0, 0, 1);
		camera.update();
		renderer.setView(camera);
		renderer.render();

		// ############################
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		// ############################

		tilePos = realToTilePos(getRealCamPos());
		Cell curCell = baseLayer.getCell((int)tilePos.x, (int)tilePos.y);

		for(MyCell c: updateCells) {
			c.renderStuff();
		}

		// ###########################
		batch.end();
		// ###########################

		srend.setProjectionMatrix(camera.combined);
		srend.begin(ShapeType.Line);
		srend.setColor(Color.BLACK);
		int b = 64*64;
		
		// Draw horizontal tile borders
		for(int i = -b; i < b; i += 64) {
			srend.line(new Vector2(-b, i), new Vector2(b, i));
		}
		// Draw vertical tile borders
		for(int j = -b; j < b; j += 64) {
			srend.line(new Vector2(j, -b), new Vector2(j, b));
		}

		srend.end();

		// stackoverflow hack for ShapeRenderer alpha
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		srend.begin(ShapeType.Filled);
		srend.setColor(player1Color);
		for(MyCell c: updateCells) {
			if(c instanceof Town) {
				Town t = (Town)c;
				int size = t.size;
				Vector2 start = new Vector2(t.pos.x - size, t.pos.y - size).scl(64);
				Vector2 end = new Vector2(t.pos.x + (size+1), t.pos.y + (size+1)).scl(64);
				srend.box(start.x, start.y, 0f, end.x - start.x, end.y - start.y, 0f);
			}
		}

		srend.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);

		if(curCell != null) {
			//System.out.println(curCell.getTile().getProperties().get("passable"));
			//System.out.println(tilePos);
		}
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		map.dispose();
		renderer.dispose();
	}

	public Vector2 getRealCamPos() {
		Vector3 pos3 = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
		Vector3 real3 = camera.unproject(pos3);
		Vector2 real = new Vector2(real3.x, real3.y);
		return real;
	}

	public Vector2 realToTilePos(Vector2 real) {
		return new Vector2((float)Math.floor(real.x / 64), (float)Math.floor(real.y / 64));
	}
}
