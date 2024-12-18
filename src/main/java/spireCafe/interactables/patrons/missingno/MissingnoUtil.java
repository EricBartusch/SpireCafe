package spireCafe.interactables.patrons.missingno;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.nio.charset.StandardCharsets;

import static spireCafe.Anniv7Mod.makeShaderPath;
import static spireCafe.Anniv7Mod.modID;

public class MissingnoUtil {
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

    public static String getRandomEventDescription() {
        MarkovChain mc = new MarkovChain(); //TODO: create once
        FileHandle fileHandle = Gdx.files.internal(modID + "Resources/localization/eng/MissingnoPatron/markov-text.txt");
        String text = fileHandle.readString(String.valueOf(StandardCharsets.UTF_8));
        mc.buildChain(text);

        return mc.generateText() + "?";
    }

}
