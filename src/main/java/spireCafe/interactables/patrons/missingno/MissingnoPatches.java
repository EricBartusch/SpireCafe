package spireCafe.interactables.patrons.missingno;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.esotericsoftware.spine.SkeletonMeshRenderer;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import javassist.CannotCompileException;
import javassist.CtBehavior;

import java.util.ArrayList;

import static spireCafe.Anniv7Mod.*;
import static spireCafe.interactables.patrons.missingno.MissingnoUtil.initGlitchShader;


public class MissingnoPatches {
    private static ShaderProgram glitchShader = null;

    @SpirePatch(clz = AbstractPlayer.class, method = "renderPlayerImage")
    public static class ApplyPlayerShaders {
        private static FrameBuffer buffer;
        private static TextureRegion playerTexture;

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(SkeletonMeshRenderer.class, "draw");
                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<Matcher>(), finalMatcher);
            }
        }

        @SpireInsertPatch(locator = Locator.class)
        public static void startBuffer(AbstractPlayer __instance, SpriteBatch sb) {
            sb.flush();
            if (buffer == null) {
                buffer = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
            }
            buffer.begin();
            Gdx.gl.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
            Gdx.gl.glColorMask(true, true, true, true);
        }

        private static class LocatorTwo extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(SpriteBatch.class, "begin");
                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<Matcher>(), finalMatcher);
            }
        }

        @SpireInsertPatch(locator = LocatorTwo.class)
        public static void endBufferAndDraw(AbstractPlayer __instance, SpriteBatch sb) {
            sb.flush();
            buffer.end();
            if (playerTexture == null) {
                playerTexture = new TextureRegion(buffer.getColorBufferTexture());
                playerTexture.flip(false, true);
            } else {
                playerTexture.setTexture(buffer.getColorBufferTexture());
            }
            glitchShader = initGlitchShader(glitchShader);
            sb.begin();
            sb.setShader(glitchShader);
            glitchShader.setUniformf("u_time", (time % 10) + 200);
            glitchShader.setUniformf("u_shake_power", shake_power.get());
            glitchShader.setUniformf("u_shake_rate", shake_rate.get());
            glitchShader.setUniformf("u_shake_speed", shake_speed.get());
            glitchShader.setUniformf("u_shake_block_size", shake_block_size.get());
            glitchShader.setUniformf("u_shake_color_rate", shake_color_rate.get());


            sb.draw(playerTexture, -Settings.VERT_LETTERBOX_AMT, -Settings.HORIZ_LETTERBOX_AMT);
            sb.setShader(null);
            sb.end();
        }

    }
}
