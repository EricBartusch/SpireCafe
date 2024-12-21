package spireCafe.interactables.patrons.missingno;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Interpolation;
import com.esotericsoftware.spine.SkeletonMeshRenderer;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import spireCafe.util.TexLoader;

import java.util.ArrayList;

import static spireCafe.Anniv7Mod.*;
import static spireCafe.interactables.patrons.missingno.MissingnoUtil.initGlitchShader;
import static spireCafe.interactables.patrons.missingno.MissingnoUtil.isGlitched;


public class MissingnoPatches {
    private static ShaderProgram glitchShader = null;
    private static final Texture background = TexLoader.getTexture(makeCharacterPath("Missingno/coast.png"));

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
            if(MissingnoUtil.isGlitched()) {
                sb.flush();
                if (buffer == null) {
                    buffer = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
                }
                buffer.begin();
                Gdx.gl.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
                Gdx.gl.glColorMask(true, true, true, true);
            }
        }

        private static class LocatorTwo extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(SpriteBatch.class, "begin");
                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<Matcher>(), finalMatcher);
            }
        }

        @SpireInsertPatch(locator = LocatorTwo.class)
        public static void endBufferAndDraw(AbstractPlayer __instance, SpriteBatch sb) {
            if(MissingnoUtil.isGlitched()) {
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

    @SpirePatch(clz = AbstractMonster.class, method = SpirePatch.CLASS)
    public static class GlitchedFields
    {
        public static SpireField<Boolean> isGlitched = new SpireField<>(() -> false);
        public static SpireField<Integer> glitchOffset = new SpireField<>(() -> 200);

    }

    @SpirePatch(clz = AbstractMonster.class, method = "render")
    public static class ApplyMonsterShaders {
        private static FrameBuffer buffer;
        private static TextureRegion monsterTexture;

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(SkeletonMeshRenderer.class, "draw");
                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<Matcher>(), finalMatcher);
            }
        }

        @SpireInsertPatch(locator = Locator.class)
        public static void startBuffer(AbstractMonster __instance, SpriteBatch sb) {
            if(MissingnoUtil.isMonsterGlitched(__instance)) {
                sb.flush();
                if (buffer == null) {
                    buffer = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
                }
                buffer.begin();
                Gdx.gl.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
                Gdx.gl.glColorMask(true, true, true, true);
            }
        }

        private static class LocatorTwo extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(SpriteBatch.class, "begin");
                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<Matcher>(), finalMatcher);
            }
        }

        @SpireInsertPatch(locator = LocatorTwo.class)
        public static void endBufferAndDraw(AbstractMonster __instance, SpriteBatch sb) {
            if(MissingnoUtil.isMonsterGlitched(__instance)) {
                sb.flush();
                buffer.end();
                if (monsterTexture == null) {
                    monsterTexture = new TextureRegion(buffer.getColorBufferTexture());
                    monsterTexture.flip(false, true);
                } else {
                    monsterTexture.setTexture(buffer.getColorBufferTexture());
                }
                glitchShader = initGlitchShader(glitchShader);
                sb.begin();
                sb.setShader(glitchShader);
                glitchShader.setUniformf("u_time", (time % 10) + GlitchedFields.glitchOffset.get(__instance));
                glitchShader.setUniformf("u_shake_power", shake_power.get());
                glitchShader.setUniformf("u_shake_rate", shake_rate.get());
                glitchShader.setUniformf("u_shake_speed", shake_speed.get());
                glitchShader.setUniformf("u_shake_block_size", shake_block_size.get());
                glitchShader.setUniformf("u_shake_color_rate", shake_color_rate.get());


                sb.draw(monsterTexture, -Settings.VERT_LETTERBOX_AMT, -Settings.HORIZ_LETTERBOX_AMT);
                sb.setShader(null);
                sb.end();
            }
        }
    }
    @SpirePatch(clz = AbstractRelic.class, method = "renderInTopPanel")
    public static class MissingnoRelicRender {
        @SpireInsertPatch(locator = Locator.class)
        public static void MissingnoRelicPrefixRenderPatch(AbstractRelic __instance, SpriteBatch sb) {
            if(__instance.relicId.equals(MissingnoRelic.ID)) {
                glitchShader = initGlitchShader(glitchShader);
                sb.setShader(glitchShader);
            }
        }

        @SpireInsertPatch(locator = LocatorTwo.class)
        public static void MissingnoRelicInsertPatch(AbstractRelic __instance, SpriteBatch sb) {
            if(__instance.relicId.equals(MissingnoRelic.ID)) {
                sb.setShader(null);
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(SpriteBatch.class, "setColor");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }

        private static class LocatorTwo extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractRelic.class, "renderCounter");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    public static final String MISSINGNO_RELIC_LANDING_SFX = makeID("MissingnoRelic");

    @SpirePatch(clz = AbstractRelic.class, method = "playLandingSFX")
    public static class PlayMissingnoSoundPatch {

        @SpirePrefixPatch
        public static SpireReturn<Void> PlaySound(AbstractRelic __instance) {
            if(__instance.relicId.equals(MissingnoRelic.ID)) {
                CardCrawlGame.sound.play(MISSINGNO_RELIC_LANDING_SFX);
                return SpireReturn.Return();
            } else {
                return SpireReturn.Continue();
            }
        }

    }

    @SpirePatch(clz = AbstractDungeon.class, method = "render")
    public static class renderMyBg {

        @SpireInsertPatch(rloc = 11)
        public static void MissingnoRenderBg(AbstractDungeon __instance, SpriteBatch sb) {
            if (isGlitched()) {
                float alpha = getAlpha();
                Color prevColor = sb.getColor();
                sb.setColor(1f, 1f, 1f, alpha);
                sb.setBlendFunction(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
                sb.draw(background, 0, 0, Settings.WIDTH, Settings.HEIGHT);
                sb.setColor(prevColor);
            }
        }

        private static float getAlpha() {
            float period = 5.0f;
            float pauseDuration = 60.0f;
            float maxAlpha = 0.15f;
            float totalPeriod = period + pauseDuration;
            float normalizedTime = time % totalPeriod;

            float alpha;
            if (normalizedTime < period / 2) {
                // Fade in
                float cycleTime = normalizedTime / (period / 2);
                alpha = Interpolation.linear.apply(0f, maxAlpha, cycleTime);
            } else if (normalizedTime < period) {
                // Fade out
                float cycleTime = (normalizedTime - period / 2) / (period / 2);
                alpha = Interpolation.linear.apply(maxAlpha, 0f, cycleTime);
            } else {
                // Pause
                alpha = 0.0f;
            }
            return alpha;
        }
    }
}
