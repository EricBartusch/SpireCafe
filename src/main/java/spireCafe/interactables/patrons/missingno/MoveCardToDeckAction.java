package spireCafe.interactables.patrons.missingno;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class MoveCardToDeckAction extends AbstractGameAction {

    AbstractCard card;
    MoveCardToDeckAction(AbstractCard card) {
        this.card = card;
    }
    @Override
    public void update() {
        AbstractDungeon.player.hand.moveToDeck(card, true);
        isDone = true;
    }
}