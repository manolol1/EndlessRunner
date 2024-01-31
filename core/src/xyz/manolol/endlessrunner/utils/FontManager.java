package xyz.manolol.endlessrunner.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;

public class FontManager implements Disposable {

    private final FreeTypeFontGenerator fontGenerator;
    private final GlyphLayout layout;
    private final FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    private final ObjectMap<Integer, BitmapFont> fonts;
    public FontManager(String filePath) {
        this.fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal(filePath));
        this.parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        this.layout = new GlyphLayout();
        this.fonts = new ObjectMap<>();
    }

    public BitmapFont getFont(int size) {
        // generate new font if the specific size hasn't been generated yet
        if (!fonts.containsKey(size)) {
            parameter.size = size;
            parameter.minFilter = Texture.TextureFilter.Linear;
            parameter.magFilter = Texture.TextureFilter.Linear;
            fonts.put(size, fontGenerator.generateFont(parameter));
        }
        return fonts.get(size);
    }

    @Override
    public void dispose() {
        for (BitmapFont font : fonts.values()) {
            if (font != null) {
                font.dispose();
            }
        }
    }

}
