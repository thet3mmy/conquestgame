package page.rightshift.conq;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;

public class ConqCamera extends OrthographicCamera {
    @Override
    public void update() {
        if(Gdx.input.isButtonPressed(0)) {
            Vector2 diff = new Vector2(-Gdx.input.getDeltaX(), Gdx.input.getDeltaY());
            diff.scl(zoom * 2);
            translate(diff);
        }
        super.update();
    }

    ConqCamera(int w, int h) {
        super(w, h);
    }
}
