package spireCafe.interactables.patrons.missingno;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static spireCafe.Anniv7Mod.makeShaderPath;

public class MissingnoUtil {
    public static boolean isGlitched() {
        return AbstractDungeon.player.hasRelic(MissingnoRelic.ID);
    }

    public static boolean isMonsterGlitched(AbstractMonster m) {
        return MissingnoPatches.GlitchedFields.isGlitched.get(m);
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
