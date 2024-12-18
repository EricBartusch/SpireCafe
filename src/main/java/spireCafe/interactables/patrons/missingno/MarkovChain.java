package spireCafe.interactables.patrons.missingno;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.nio.charset.StandardCharsets;
import java.util.*;

import static spireCafe.Anniv7Mod.modID;

public class MarkovChain {
    private Map<String, List<String>> markovChain = new HashMap<>();
    private Random random = new Random();

    public MarkovChain() {
        FileHandle fileHandle = Gdx.files.internal(modID + "Resources/localization/eng/MissingnoPatron/markov-text.txt");
        String text = fileHandle.readString(String.valueOf(StandardCharsets.UTF_8));
        buildChain(text);
    }

    public void buildChain(String text) {
        String[] words = text.split("\\s+");
        for (int i = 0; i < words.length - 1; i++) {
            String word = words[i].toLowerCase();
            String nextWord = words[i + 1].toLowerCase();
            markovChain.computeIfAbsent(word, k -> new ArrayList<>()).add(nextWord);
        }
    }

    private String getRandomSeed() {
        List<String> keys = new ArrayList<>(markovChain.keySet());
        if (keys.isEmpty()) {
            return ""; // If no keys are present, return an empty string
        }
        return keys.get(random.nextInt(keys.size()));
    }

    public String generateText() {
        String seed = getRandomSeed();
        int length = random.nextInt((40 - 25) + 1) + 25;
        StringBuilder result = new StringBuilder(seed);
        String currentWord = seed.toLowerCase();
        for (int i = 0; i < length; i++) {
            List<String> nextWords = markovChain.get(currentWord);
            if (nextWords == null || nextWords.isEmpty()) {
                break;
            }
            String nextWord = nextWords.get(random.nextInt(nextWords.size()));
            result.append(" ").append(nextWord);
            currentWord = nextWord;
        }
        return result.toString();
    }

}
