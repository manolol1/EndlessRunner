package xyz.manolol.endlessrunner;

import com.badlogic.gdx.Game;
import com.kotcrab.vis.ui.VisUI;
import xyz.manolol.endlessrunner.screens.*;

public class Main extends Game {
    @Override
    public void create() {
        VisUI.load();
        this.setScreen(new MainMenuScreen(this));
    }

    @Override
    public void dispose() {
        VisUI.dispose();
    }
}
