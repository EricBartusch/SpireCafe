package spireCafe.interactables.patrons.missingno;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import spireCafe.abstracts.AbstractCutscene;
import spireCafe.abstracts.AbstractNPC;
import spireCafe.ui.Dialog;
import spireCafe.util.cutsceneStrings.CutsceneStrings;
import spireCafe.util.cutsceneStrings.LocalizedCutsceneStrings;

import static spireCafe.Anniv7Mod.makeID;
import static spireCafe.Anniv7Mod.modID;
import static spireCafe.interactables.patrons.missingno.MissingnoUtil.getRandomEventDescription;


public class MissingnoCutscene extends AbstractCutscene {
    public static final String ID = makeID(MissingnoCutscene.class.getSimpleName());
    private static final CutsceneStrings cutsceneStrings = LocalizedCutsceneStrings.getCutsceneStrings(ID);
    private boolean forRemove = false;
    private boolean forUpgrade = false;
    public static BitmapFont font;
    private static final FileHandle FONT_FILE = Gdx.files.internal(modID + "Resources/fonts/missingno/Gridlockd.ttf");

    public MissingnoCutscene(AbstractNPC character) {
        super(character, cutsceneStrings);
        if (font == null) {
            setupFont();
        }
    }

    //Instead of only having one line of dialogue after the transaction this Patron says a different line each time.
    @Override
    public String getBlockingDialogue() {
        int blockingDialogueIndex = character.blockingDialogueIndex;
        String blockingDialogue = cutsceneStrings.BLOCKING_TEXTS[blockingDialogueIndex];
        if(blockingDialogueIndex<cutsceneStrings.BLOCKING_TEXTS.length-1){
            character.blockingDialogueIndex++;
        }
        return blockingDialogue;
    }

    @Override
    protected void onClick() {
        // In the eventstrings, DESCRIPTIONS is an array of all the cutscene text.
        // dialogueIndex is the index of cutscene text the cutscene is currently displaying.
        // The default behavior when the player clicks is to simply display the text in dialogue index + 1.
        // This means that if you properly structure your cutscene strings into sections of
        // subsequent dialogue, you can take advantage of the default behavior to handle most
        // of the transitioning, and simply focus on writing code for handling the points where you
        // want to add player dialog options or exit the cutscene.

        // If you want player dialog options to appear during a specific dialog, you need to "add" the dialog options
        // during the previous dialogue index so that they will show up in the next dialog.
        if (dialogueIndex == 0) {
            // In this example, adding the player dialog option during index 0 will cause them to appear during index 1.
            nextDialogue(); // nextDialogue will transition us from dialogue index 0 to index 1.
            // Adds a player dialog option for upgrading a card. You can specify what happens when the button is clicked with setOptionResult.
            this.dialog.addDialogOption(OPTIONS[0] ).setOptionResult((i)->{
                nextDialogue();
                // If the player selects an option that provides some benefit (like upgrading a card, removing a card, etc.)
                // then this alreadyPerformedTransaction flag should be set to true. This will prevent the player from
                // interacting further with the NPC so they can't repeatedly perform the same transaction.
            });
            // Adds a player dialog option for removing a card.
            this.dialog.addDialogOption(OPTIONS[1]).setOptionResult((i)->{
                // goToDialogue(4) will jump to dialogue index 4 instead of advancing to index + 1.
                // This allows you to "jump" to different sections of cutscene text depending on player input.
                goToDialogue(2);

            });
        } else if (dialogueIndex == 2) {
            // Exit the cutscene at any of these dialogue indices.
            endCutscene();
        } else {
            // Default behavior is to simply display the text in the next dialogue index.
            nextDialogue();
        }
    }

    public void update() {
        super.update();
        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty() && forRemove) {
            for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
                AbstractDungeon.effectList.add(new PurgeCardEffect(c));
                AbstractDungeon.player.masterDeck.removeCard(c);
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            backToCutscene();
        }

        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty() && forUpgrade) {
            for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
                c.upgrade();
                AbstractDungeon.player.bottledCardUpgradeCheck(c);
                AbstractDungeon.effectsQueue.add(new ShowCardBrieflyEffect(c.makeStatEquivalentCopy()));
                AbstractDungeon.effectsQueue.add(new UpgradeShineEffect((float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            backToCutscene();
        }
    }

    @Override
    protected void goToDialogue(int newIndex) {
        Dialog.optionList.clear();
        this.dialogueIndex = newIndex;
        if (this.dialogueIndex == 1) {
            updateDialogueText();
        } else {
            endCutscene();
        }
    }

    @Override
    protected void updateDialogueText() {
        String text = appendSpeakerToDialogue(getRandomEventDescription());
        if (this.show) {
            this.show = false;
            this.dialog.show(text);
        } else {
            this.dialog.updateBodyText(text);
        }
    }

    private static void setupFont() {
        FreeTypeFontGenerator g = new FreeTypeFontGenerator(FONT_FILE);
        FreeTypeFontGenerator.FreeTypeFontParameter p = new FreeTypeFontGenerator.FreeTypeFontParameter();
        p.characters = "";
        p.incremental = true;
        p.size = Math.round(50.0f * Settings.scale);
        p.gamma = 1.2F;
        p.minFilter = Texture.TextureFilter.Linear;
        p.magFilter = Texture.TextureFilter.Linear;
        g.scaleForPixelHeight(p.size);
        font = g.generateFont(p);
        font.setUseIntegerPositions(false);
        (font.getData()).markupEnabled = true;
        if (LocalizedStrings.break_chars != null)
            (font.getData()).breakChars = LocalizedStrings.break_chars.toCharArray();
        (font.getData()).fontFile = FONT_FILE;
        System.out.println(font);
    }
}