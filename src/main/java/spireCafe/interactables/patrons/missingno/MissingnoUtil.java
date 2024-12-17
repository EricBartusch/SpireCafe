package spireCafe.interactables.patrons.missingno;

import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static spireCafe.Anniv7Mod.makeShaderPath;

public class MissingnoUtil {
    public static FrameBuffer createBuffer() {
        return createBuffer(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public static FrameBuffer createBuffer(int sizeX, int sizeY) {
        return new FrameBuffer(Pixmap.Format.RGBA8888, sizeX, sizeY, false, false);
    }

    public static void beginBuffer(FrameBuffer fbo) {
        fbo.begin();
        Gdx.gl.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glColorMask(true, true, true, true);
    }

    public static TextureRegion getBufferTexture(FrameBuffer fbo) {
        TextureRegion texture = new TextureRegion(fbo.getColorBufferTexture());
        texture.flip(false, true);
        return texture;
    }

    public static boolean isGlitched() {
        return true;
    }

    public static ShaderProgram initGlitchShader(ShaderProgram glitchShader) {
        if (glitchShader == null) {
            try {
                glitchShader = new ShaderProgram(
                        Gdx.files.internal(makeShaderPath("missingno/glitch/vertex.vs")),
                        Gdx.files.internal(makeShaderPath("missingno/glitch/fragment.fs"))
                );
                if (!glitchShader.isCompiled()) {
                    System.err.println(glitchShader.getLog());
                }
                if (!glitchShader.getLog().isEmpty()) {
                    System.out.println(glitchShader.getLog());
                }
            } catch (GdxRuntimeException e) {
                System.out.println("ERROR: missingno shader:");
                e.printStackTrace();
            }
        }
        return glitchShader;
    }


}
