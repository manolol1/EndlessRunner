package xyz.manolol.endlessrunner;

import com.badlogic.gdx.Game;
import xyz.manolol.endlessrunner.screens.MainMenuScreen;

public class Main extends Game {
    @Override
    public void create() {
        this.setScreen(new MainMenuScreen(this));
    }
}
