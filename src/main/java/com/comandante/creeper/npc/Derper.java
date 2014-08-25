package com.comandante.creeper.npc;


import com.comandante.creeper.managers.GameManager;
import org.fusesource.jansi.Ansi;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static com.comandante.creeper.model.Color.CYAN;
import static com.comandante.creeper.model.Color.RED;

public class Derper extends Npc {

    private final static long phraseIntervalMs = 300000;
    private final static String NAME = "derper";
    private final Random random;
    private final static String colorName = new StringBuilder()
            .append(RED)
            .append("d")
            .append(CYAN)
            .append("erpe")
            .append(RED)
            .append("r")
            .append(new Ansi().reset().toString()).toString();

    public Derper(GameManager gameManager, Integer roomId) {
        super(gameManager, roomId, NAME, colorName, 0);
        this.random = new Random();
    }

    @Override
    public void run() {
        super.run();
        if (System.currentTimeMillis() - getLastPhraseTimestamp() > phraseIntervalMs) {
            int size = PHRASES.size();
            npcSay(getRoomId(), PHRASES.get(random.nextInt(size)));
            setLastPhraseTimestamp(System.currentTimeMillis());
        }
    }

    public static List<String> PHRASES = Arrays.asList(
            "Zug, zug.",
            "Don't provoke me.",
            "Tough guy, eh?",
            "I will end you.",
            "This is not a drill, I will eliminate your existence.",
            "This is smash talk, I will derp you.",
            "Have ever been derped by a derper? I will hurt you.",
            "I was taught to provoke.",
            "Oh hey I will fight you.",
            "WORDS OF THREATENING NATURE!"
    );
}
