package xyz.manolol.endlessrunner.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import xyz.manolol.endlessrunner.Main;
import xyz.manolol.endlessrunner.Utils.FontManager;
import xyz.manolol.endlessrunner.Utils.PrefsManager;

@SuppressWarnings("FieldCanBeLocal")
public class GameOverScreen extends ScreenAdapter {
    private final FontManager fontManager;
    private final PrefsManager prefs;

    private final OrthographicCamera camera;
    private final FitViewport viewport;

    private final Stage stage;
    private final VisTable table;

    public GameOverScreen(Main main, int score) {
        fontManager = new FontManager("fonts/Ubuntu-Regular.ttf");
        prefs = new PrefsManager();

        camera = new OrthographicCamera();
        viewport = new FitViewport(1920, 1080, camera);

        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        table = new VisTable();
        table.setFillParent(true);

        VisTextButton.VisTextButtonStyle textButtonStyle = VisUI.getSkin().get(VisTextButton.VisTextButtonStyle.class);

        Label.LabelStyle labelStyle1 = new Label.LabelStyle();
        labelStyle1.font = fontManager.getFont(120);
        labelStyle1.fontColor = Color.RED;
        VisLabel label1 = new VisLabel("GAME OVER", labelStyle1);
        table.add(label1).pad(0).row();

        Label.LabelStyle labelStyle2 = new Label.LabelStyle();
        labelStyle2.font = fontManager.getFont(80);
        labelStyle2.fontColor = Color.WHITE;
        VisLabel label2 = new VisLabel("Score: " + score, labelStyle2);
        table.add(label2).pad(50).row();

        Label.LabelStyle labelStyle3 = new Label.LabelStyle();
        labelStyle3.font = fontManager.getFont(80);
        labelStyle3.fontColor = Color.WHITE;
        VisLabel label3 = new VisLabel("Highscore: " + prefs.getHighscore(), labelStyle3);
        if (score > prefs.getHighscore()) {
            prefs.setHighscore(score);
            labelStyle3.fontColor = Color.GREEN;
            label3 = new VisLabel("NEW Highscore: " + prefs.getHighscore(), labelStyle3);
        }
        table.add(label3).pad(50).row();

        textButtonStyle.font = fontManager.getFont(80);
        VisTextButton restartButton = new VisTextButton("Try Again", textButtonStyle);
        restartButton.pad(20);
        restartButton.setFocusBorderEnabled(false);
        restartButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.setScreen(new GameScreen(main));
            }
        });
        table.add(restartButton).width(500).pad(50).row();

        textButtonStyle.font = fontManager.getFont(80);
        VisTextButton mainMenuButton = new VisTextButton("Main Menu", textButtonStyle);
        mainMenuButton.pad(20);
        mainMenuButton.setFocusBorderEnabled(false);
        mainMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.setScreen(new MainMenuScreen(main));
            }
        });
        table.add(mainMenuButton).width(500);

        table.padBottom(100); // move the whole table up a little bit

        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        stage.act(delta);
        stage.draw();
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
        fontManager.dispose();
        stage.dispose();
    }
}
