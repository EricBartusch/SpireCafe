package spireCafe.interactables.patrons.missingno;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ShaderHelper;
import spireCafe.abstracts.AbstractSCRelic;

import static spireCafe.Anniv7Mod.makeID;
import static spireCafe.interactables.patrons.missingno.MissingnoPatron.assetID;
import static spireCafe.interactables.patrons.missingno.MissingnoUtil.initGlitchShader;

public class MissingnoRelic extends AbstractSCRelic {

    public static final String ID = makeID(MissingnoRelic.class.getSimpleName());

    private ShaderProgram glitchShader;
    public MissingnoRelic() {
        super(ID, assetID, RelicTier.SPECIAL, LandingSound.HEAVY);
    }

}
