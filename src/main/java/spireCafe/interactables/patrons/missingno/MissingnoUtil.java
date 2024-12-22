package spireCafe.interactables.patrons.missingno;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static spireCafe.Anniv7Mod.makeShaderPath;

public class MissingnoUtil {
    public static boolean isGlitched() {
        return CardCrawlGame.isInARun() && AbstractDungeon.player.hasRelic(MissingnoRelic.ID);
    }

    public static boolean isMonsterGlitched(AbstractMonster m) {
        return MissingnoPatches.GlitchedMonsterFields.isGlitched.get(m);
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

    public static void shuffleRelics() {
        if(CardCrawlGame.isInARun()) {
            ArrayList<AbstractRelic> relics = AbstractDungeon.player.relics;
            if (relics.size() <= 1) {
                return;
            }
            List<AbstractRelic> relicSublist = new ArrayList<>(relics.subList(1, relics.size()));
            Collections.shuffle(relicSublist);

            List<Float> currentXList = new ArrayList<>();
            List<Float> targetXList = new ArrayList<>();
            List<Hitbox> hitboxList = new ArrayList<>();

            for (int i = 1; i < relics.size(); i++) {
                currentXList.add(relics.get(i).currentX);
                targetXList.add(relics.get(i).targetX);
                hitboxList.add(relics.get(i).hb);
            }

            for (int i = 1; i < relics.size(); i++) {
                AbstractRelic newRelic = relicSublist.get(i - 1);

                relics.set(i, newRelic);

                newRelic.currentX = currentXList.get(i - 1);
                newRelic.targetX = targetXList.get(i - 1);
                newRelic.hb = hitboxList.get(i - 1);
            }
        }
    }
}
