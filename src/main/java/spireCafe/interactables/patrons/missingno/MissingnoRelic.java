package spireCafe.interactables.patrons.missingno;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import spireCafe.abstracts.AbstractSCRelic;

import static spireCafe.Anniv7Mod.makeID;
import static spireCafe.interactables.patrons.missingno.MissingnoPatron.assetID;

public class MissingnoRelic extends AbstractSCRelic {

    public static final String ID = makeID(MissingnoRelic.class.getSimpleName());

    public MissingnoRelic() {
        super(ID, assetID, RelicTier.SPECIAL, LandingSound.HEAVY);
    }

}
