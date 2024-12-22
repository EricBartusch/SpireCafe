package spireCafe.interactables.patrons.missingno;

import com.evacipated.cardcrawl.mod.stslib.cards.targeting.SelfOrEnemyTargeting;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import spireCafe.abstracts.AbstractSCCard;

import static com.badlogic.gdx.math.MathUtils.random;
import static spireCafe.Anniv7Mod.makeID;
import static spireCafe.interactables.patrons.missingno.MarkovChain.MarkovType.CARD;
import static spireCafe.interactables.patrons.missingno.MissingnoPatches.MISSINGNO_RELIC_LANDING_SFX;
import static spireCafe.util.CardArtRoller.computeCard;
import static spireCafe.util.Wiz.atb;

public class MissingnoCard extends AbstractSCCard {

    public static final String ID = makeID(MissingnoCard.class.getSimpleName());

    public MissingnoCard() {
        super(ID, 0, CardType.SKILL, CardRarity.SPECIAL, SelfOrEnemyTargeting.SELF_OR_ENEMY);
        exhaust = true;
        String markovText = MarkovChain.getInstance(CARD).generateText(5, 15).replaceAll("[~@]", "").replaceAll("#.", "");
        rawDescription = rawDescription + markovText;
        if (CardLibrary.cards != null && !CardLibrary.cards.isEmpty()) {
            computeCard(this, true);
            needsArtRefresh = false;
        }
        baseMagicNumber = magicNumber = random(5);
        baseDamage = damage = random(12);
        baseBlock = block = random(8);
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
            MissingnoPatches.GlitchedMonsterFields.isGlitched.set(target, true);
            MissingnoPatches.GlitchedMonsterFields.glitchOffset.set(target, random.nextInt(200));
        } else {
            MissingnoPatches.GlitchedPlayerFields.glitchOffset.set(target, MissingnoPatches.GlitchedPlayerFields.glitchOffset.get(abstractPlayer) + random.nextInt(100));

        }
    }
}
