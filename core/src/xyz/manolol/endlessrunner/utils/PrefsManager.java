package xyz.manolol.endlessrunner.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class PrefsManager {
    Preferences prefs;

    public PrefsManager() {
        prefs = Gdx.app.getPreferences("EndlessRunnerData");
    }

    public void setHighscore(int highscore) {
        prefs.putInteger("highscore", highscore);
        prefs.flush();
    }

    public int getHighscore() {
        return prefs.getInteger("highscore", 0);
    }
}
