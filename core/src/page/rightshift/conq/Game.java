package page.rightshift.conq;

import java.util.LinkedList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;

public class Game extends ApplicationAdapter {
	static SpriteBatch batch;
	static OrthogonalTiledMapRenderer renderer;

	static TiledMapTileLayer baseLayer;
	static TiledMapTileLayer objLayer;
	static TiledMapTileLayer unitLayer;
	static TiledMapTileSet set;

	static ConqCamera camera;
	static ConqInputSystem inputSystem;
	static UIManager uiManager;
	static ShapeRenderer srend;
	static TiledMap map;

	static int turnMovesLeft = 8;
	static LinkedList<MyCell> updateCells;
	static Vector2 tilePos;
	static MyCell selectedUnit;

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
		
		baseLayer = (TiledMapTileLayer)map.getLayers().get(0);
		objLayer = (TiledMapTileLayer)map.getLayers().get(1);
		unitLayer = (TiledMapTileLayer)map.getLayers().get(2);
		set = map.getTileSets().getTileSet(0);
		updateCells = new LinkedList<>();
		uiManager = new UIManager();

		inputSystem = new ConqInputSystem();
		Gdx.input.setInputProcessor(inputSystem);

		player1Color = new Color(0.9f, 0.2f, 0.9f, 0.3f);
		player2Color = new Color(0.2f, 0.2f, 0.9f, 0.3f);
	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 0, 0, 1);
		camera.update();
		
		// render the game world
		renderer.setView(camera);
		renderer.render();

		// render others translated by camera
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		tilePos = Util.getTileMousePos();

		// update all of the updateable cells
		for(MyCell c: new LinkedList<>(updateCells)) {
			c.renderStuff();
			// if the cell is a unit, then let it think
			if(c instanceof Unit) {
				Unit u = (Unit)c;
				u.think();
			}
		}

		batch.end();

		UIManager.drawMapGrid();
		UIManager.drawTerritoryControl();

		uiManager.update();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		map.dispose();
		renderer.dispose();
	}
}
