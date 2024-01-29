package xyz.manolol.endlessrunner.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import xyz.manolol.endlessrunner.Main;
import xyz.manolol.endlessrunner.Utils.FontManager;
import xyz.manolol.endlessrunner.Utils.PrefsManager;

import java.util.Iterator;

@SuppressWarnings("FieldCanBeLocal")
public class GameScreen extends ScreenAdapter {
    private final Main main;
    private final OrthographicCamera camera;
    private final FitViewport viewport;
    private final ShapeRenderer shapeRenderer;

    PrefsManager prefs;

    private final FontManager fontManager;
    private final Stage stage;
    private final VisTable table;
    private final VisLabel scoreLabel;

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

    private final Rectangle player;

    private final Array<Rectangle> obstacles;

    private float jumpForceLeft = 0;

    private float obstacleSpeed =  OBSTACLE_SPEED_START;
    private float obstacleDistance = OBSTACLE_DISTANCE_START;

    private float timeUntilDifficultyIncrease = DIFFICULTY_INCREASE_INTERVAL;

    private int score = 0; // increases when obstacle leaves the screen

    public GameScreen(Main main) {
        this.main = main;
        camera = new OrthographicCamera();
        viewport = new FitViewport(1920, 1080, camera);
        shapeRenderer = new ShapeRenderer();

        prefs = new PrefsManager();

        player = new Rectangle(50, FLOOR_HEIGHT, PLAYER_SIZE, PLAYER_SIZE);

        obstacles = new Array<>();

        spawnObstacle(viewport.getWorldWidth() / 2);
        spawnObstacle(viewport.getWorldWidth() / 1.1f);

        // User Interface //
        fontManager = new FontManager("fonts/Ubuntu-Regular.ttf");
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);
        table = new VisTable();
        table.setFillParent(true);
        table.top().right();

        VisLabel.LabelStyle labelStyle = new Label.LabelStyle();

        labelStyle.font = fontManager.getFont(80);
        scoreLabel = new VisLabel("", labelStyle);
        table.add(scoreLabel).pad(50);

        stage.addActor(table);
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

        // Spawn new Obstacles
        if (obstacles.isEmpty()) {
            spawnObstacle(viewport.getWorldWidth());
        } else {
            if (obstacles.peek().x < viewport.getWorldWidth() - (obstacleDistance + MathUtils.random(-250.0f, +200.0f))) {
                spawnObstacle(viewport.getWorldWidth());
            }
        }

        // Update obstacle positions and remove old obstacles
        Iterator<Rectangle> iter = obstacles.iterator();
        while (iter.hasNext()) {
            Rectangle obstacle = iter.next();
            obstacle.x -= obstacleSpeed * delta;

            if (obstacle.overlaps(player)) {
                main.setScreen(new GameOverScreen(main, score));
                return;
            }

            // If the obstacle is no longer visible, remove it
            if (obstacle.x + obstacle.width < 0) {
                iter.remove();
                score++;
            }
        }

        // Update Score label
        scoreLabel.setText(score);


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

        // UI
        stage.act(delta);
        stage.draw();

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
