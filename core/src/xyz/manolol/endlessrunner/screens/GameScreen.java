package xyz.manolol.endlessrunner.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import xyz.manolol.endlessrunner.Main;

import java.util.Iterator;

public class GameScreen extends ScreenAdapter {
    Main main;
    OrthographicCamera camera;
    FitViewport viewport;
    ShapeRenderer shapeRenderer;

    private final float GRAVITY = 400.0f;
    private final float JUMP_FORCE = 350.0f;
    private final float JUMP_FORCE_DECREASE = 700.0f;

    private final float OBSTACLE_SPEED_START = 200.0f;
    private final float OBSTACLE_SPEED_INCREASE = 30.0f;
    private final float OBSTACLE_DISTANCE_START = 1000.0f;

    private final float DIFFICULTY_INCREASE_INTERVAL = 2.5f;

    private final float FLOOR_HEIGHT = 50;
    private final float PLAYER_SIZE = 50;
    private final float OBSTACLE_SIZE = 40;

    private Rectangle player;

    private Array<Rectangle> obstacles;

    private float jumpForceLeft = 0;

    private float obstacleSpeed =  OBSTACLE_SPEED_START;
    private float obstacleDistance = OBSTACLE_DISTANCE_START;

    private float timeUntilDifficultyIncrease = DIFFICULTY_INCREASE_INTERVAL;

    @Override
    public void show() {
        main = new Main();
        camera = new OrthographicCamera();
        viewport = new FitViewport(1920, 1080, camera);
        shapeRenderer = new ShapeRenderer();

        player = new Rectangle(50, FLOOR_HEIGHT, PLAYER_SIZE, PLAYER_SIZE);

        obstacles = new Array<>();

        spawnObstacle(viewport.getWorldWidth() / 2);
        spawnObstacle(viewport.getWorldWidth() / 1.1f);
    }

    @Override
    public void render(float delta) {

        //**** UPDATE ****//

        // Player movement (Jump)
        if (player.y > FLOOR_HEIGHT) player.y -= GRAVITY * delta;

        if ((Gdx.input.isKeyPressed(Input.Keys.SPACE) || Gdx.input.isTouched()) && player.y <= FLOOR_HEIGHT) {
            jumpForceLeft = JUMP_FORCE + GRAVITY;
        }
        if (jumpForceLeft >= 0) {
            player.y += jumpForceLeft * delta;
            jumpForceLeft -= JUMP_FORCE_DECREASE * delta;
        }

        // Difficulty Increase
        timeUntilDifficultyIncrease -= delta;

        if (timeUntilDifficultyIncrease <= 0) {
            obstacleSpeed += OBSTACLE_SPEED_INCREASE;
            obstacleDistance += OBSTACLE_SPEED_INCREASE - 2;
            timeUntilDifficultyIncrease = DIFFICULTY_INCREASE_INTERVAL;
        }

        // Remove obstacles that are no longer on screen
        if (obstacles.peek().x < viewport.getWorldWidth() - (obstacleDistance + MathUtils.random(-250.0f, +200.0f))) {
            spawnObstacle(viewport.getWorldWidth());
        }

        // Update obstacle positions
        for (Rectangle obstacle : obstacles) {
            obstacle.x -= obstacleSpeed * delta;

            if (obstacle.overlaps(player)) {
                System.out.println("Game Over");
            }
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

        // obstacles
        shapeRenderer.setColor(Color.FIREBRICK);
        for (Rectangle obstacle : obstacles) {
            shapeRenderer.rect(obstacle.x, obstacle.y, obstacle.width, obstacle.height);
        }

        shapeRenderer.end();
    }

    private void spawnObstacle(float xPosition) {
        obstacles.add(new Rectangle(xPosition, FLOOR_HEIGHT, OBSTACLE_SIZE, OBSTACLE_SIZE));
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
