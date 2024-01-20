package xyz.manolol.endlessrunner.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import xyz.manolol.endlessrunner.Main;

public class GameScreen extends ScreenAdapter {
    Main main;
    OrthographicCamera camera;
    FitViewport viewport;
    ShapeRenderer shapeRenderer;

    private final int FLOOR_HEIGHT = 50;

    @Override
    public void show() {
        main = new Main();
        camera = new OrthographicCamera();
        viewport = new FitViewport(1920, 1080, camera);
        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 0);
        camera.update();

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // floor
        shapeRenderer.rect(0, 0, viewport.getWorldWidth(), FLOOR_HEIGHT);

        shapeRenderer.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void hide() {
        this.dispose();
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }
}
