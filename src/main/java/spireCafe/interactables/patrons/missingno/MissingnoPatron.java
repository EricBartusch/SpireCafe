package spireCafe.interactables.patrons.missingno;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractPatron;
import spireCafe.util.TexLoader;

import static spireCafe.Anniv7Mod.*;
import static spireCafe.Anniv7Mod.shake_color_rate;
import static spireCafe.interactables.patrons.missingno.MissingnoUtil.initGlitchShader;

public class MissingnoPatron extends AbstractPatron {
    public static final String ID = MissingnoPatron.class.getSimpleName();
    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString(Anniv7Mod.makeID(ID));
    private static ShaderProgram glitchShader = null;

    public MissingnoPatron(float animationX, float animationY) {
        super(animationX, animationY, 160.0f, 200.0f);
        this.name = characterStrings.NAMES[0];
        this.img = TexLoader.getTexture(Anniv7Mod.makeCharacterPath("Missingno/image.png"));
        this.cutscenePortrait = new TextureRegion(TexLoader.getTexture(Anniv7Mod.makeCharacterPath("Missingno/Portrait.png")));

    }

    public void renderCutscenePortrait(SpriteBatch sb) {
        simpleRenderCutscenePortrait(sb, 1560.0F,0.0F, 0.0F, 0.0F, 0.0F);
    }

    public void onInteract() {
        AbstractDungeon.topLevelEffectsQueue.add(new MissingnoCutscene(this));
    }

    @Override
    public void renderAnimation(SpriteBatch sb) {
        super.renderAnimation(sb);
        glitchShader = initGlitchShader(glitchShader);
        sb.setShader(glitchShader);
        glitchShader.setUniformf("u_time", (time % 10) + 200);
        glitchShader.setUniformf("u_shake_power", shake_power.get());
        glitchShader.setUniformf("u_shake_rate", shake_rate.get());
        glitchShader.setUniformf("u_shake_speed", shake_speed.get());
        glitchShader.setUniformf("u_shake_block_size", shake_block_size.get());
        glitchShader.setUniformf("u_shake_color_rate", shake_color_rate.get());
        sb.draw(this.img, this.animationX - (float)this.img.getWidth() * Settings.scale / 2.0F, this.animationY, (float)this.img.getWidth() * Settings.scale, (float)this.img.getHeight() * Settings.scale, 0, 0, this.img.getWidth(), this.img.getHeight(), this.flipHorizontal, this.flipVertical);
        sb.setShader(null);
    }
}
