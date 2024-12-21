package spireCafe.interactables.patrons.missingno;

import com.evacipated.cardcrawl.mod.stslib.cards.targeting.SelfOrEnemyTargeting;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import spireCafe.abstracts.AbstractSCCard;

import static com.badlogic.gdx.math.MathUtils.random;
import static spireCafe.Anniv7Mod.makeID;
import static spireCafe.interactables.patrons.missingno.MissingnoPatches.MISSINGNO_RELIC_LANDING_SFX;
import static spireCafe.util.CardArtRoller.computeCard;
import static spireCafe.util.Wiz.atb;

public class MissingnoCard extends AbstractSCCard {

    public static final String ID = makeID(MissingnoCard.class.getSimpleName());

    public MissingnoCard() {
        super(ID, 0, CardType.SKILL, CardRarity.SPECIAL, SelfOrEnemyTargeting.SELF_OR_ENEMY);
        exhaust = true;
        MarkovChain markovChain = new MarkovChain();
        String markovText = markovChain.generateText(5, 15).replaceAll("[~@]", "").replaceAll("#.", "");
        rawDescription = rawDescription + markovText;
        if (CardLibrary.cards != null && !CardLibrary.cards.isEmpty()) {
            computeCard(this, true);
            needsArtRefresh = false;
        }
        initializeDescription();
    }

    @Override
    public void upp() {

    }

    @Override
    public boolean canUpgrade() {
        return false;
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
        AbstractCreature target = SelfOrEnemyTargeting.getTarget(this);
        atb(new SFXAction(MISSINGNO_RELIC_LANDING_SFX)); //TODO: random sounds
        atb(new DrawCardAction(1));

        if(target instanceof AbstractMonster) {
            MissingnoPatches.GlitchedFields.isGlitched.set(target, true);
            MissingnoPatches.GlitchedFields.glitchOffset.set(target, random.nextInt(200));
        }
    }
}
