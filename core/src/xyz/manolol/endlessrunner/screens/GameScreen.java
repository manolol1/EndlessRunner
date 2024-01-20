package xyz.manolol.endlessrunner.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import xyz.manolol.endlessrunner.Main;

public class GameScreen extends ScreenAdapter {
    Main main;
    OrthographicCamera camera;
    FitViewport viewport;
    ShapeRenderer shapeRenderer;

    private final float GRAVITY = 300.0f;
    private final float JUMP_FORCE = 400.0f;
    private final float JUMP_FORCE_DECREASE = 600.0f;

    private final float FLOOR_HEIGHT = 50;
    private final float PLAYER_SIZE = 50;

    private Rectangle player;

    private float jumpForceLeft = 0;

    @Override
    public void show() {
        main = new Main();
        camera = new OrthographicCamera();
        viewport = new FitViewport(1920, 1080, camera);
        shapeRenderer = new ShapeRenderer();

        player = new Rectangle(50, FLOOR_HEIGHT, PLAYER_SIZE, PLAYER_SIZE);
    }

    @Override
    public void render(float delta) {

        //**** UPDATE ****//

        if (player.y > FLOOR_HEIGHT) player.y -= GRAVITY * delta;

        if ((Gdx.input.isKeyPressed(Input.Keys.SPACE) || Gdx.input.isTouched()) && player.y <= FLOOR_HEIGHT) {
            jumpForceLeft = JUMP_FORCE + GRAVITY;
        }

        if (jumpForceLeft >= 0) {
            player.y += jumpForceLeft * delta;
            jumpForceLeft -= JUMP_FORCE_DECREASE * delta;
        }


        //**** RENDERING ****//

        ScreenUtils.clear(0, 0, 0, 0);
        camera.update();

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // floor
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(0, 0, viewport.getWorldWidth(), FLOOR_HEIGHT);

        // player
        shapeRenderer.setColor(Color.FOREST);
        shapeRenderer.rect(player.x, player.y, player.width, player.height);

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
