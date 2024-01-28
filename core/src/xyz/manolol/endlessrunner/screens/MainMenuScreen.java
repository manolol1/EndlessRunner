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

public class MainMenuScreen extends ScreenAdapter {
    private final Main main;

    private final FontManager fontManager;

    private final OrthographicCamera camera;
    private final FitViewport viewport;

    private final Stage stage;
    private final VisTable table;

    public MainMenuScreen(Main main) {
        this.main = main;

        fontManager = new FontManager("fonts/Ubuntu-Regular.ttf");

        camera = new OrthographicCamera();
        viewport = new FitViewport(1920, 1080, camera);

        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        table = new VisTable();
        table.setFillParent(true);

        // Get the default styles
        VisLabel.LabelStyle labelStyle = new Label.LabelStyle();
        VisTextButton.VisTextButtonStyle textButtonStyle = VisUI.getSkin().get(VisTextButton.VisTextButtonStyle.class);

        labelStyle.font = fontManager.getFont(120);
        labelStyle.fontColor = Color.LIME;
        VisLabel label1 = new VisLabel("Endless Runner", labelStyle);
        table.add(label1).pad(80).row();

        textButtonStyle.font = fontManager.getFont(80);
        VisTextButton startButton = new VisTextButton("Play", textButtonStyle);
        startButton.pad(20);
        startButton.setFocusBorderEnabled(false);
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.setScreen(new GameScreen(main));
            }
        });
        table.add(startButton).width(500).pad(60).row();

        textButtonStyle.font = fontManager.getFont(80);
        VisTextButton exitButton = new VisTextButton("Exit", textButtonStyle);
        exitButton.pad(20);
        exitButton.setFocusBorderEnabled(false);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        table.add(exitButton).width(500);

        table.padBottom(200); // move the whole table up a little bit

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
