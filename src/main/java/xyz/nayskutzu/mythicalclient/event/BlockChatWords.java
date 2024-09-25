package xyz.nayskutzu.mythicalclient.event;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import java.util.Random;

public class BlockChatWords {

    private static final List<String> BLOCKED_WORDS = loadBlockedWords();
    public static BlockChatWords instance = new BlockChatWords();

    @SubscribeEvent
    public void onChatMessage(ClientChatReceivedEvent event) {
        String messageText = event.message.getUnformattedText();

        for (String word : BLOCKED_WORDS) {
            if (messageText.contains(word)) {
                event.setCanceled(true);

                List<String> responses = Arrays.asList(
                        "Hello there!",
                        "How's it going?",
                        "Nice to see you!",
                        "Greetings!",
                        "Hey!",
                        "I want to tell you guys a secret.. I'm a bad at this game!",
                        "I'm a bot, beep boop!",
                        "Yeahh whats the weather like?",
                        "I'm a bot, I'm not a human!",
                        "I like to talk like this!",
                        "I suck at this game!");

                Random random = new Random();
                String randomResponse = responses.get(random.nextInt(responses.size()));

                // Send the random response to the chat
                Minecraft.getMinecraft().thePlayer.sendChatMessage(randomResponse);
                break; // Ensure the message is not processed multiple times
            }
        }
    }

    private static List<String> loadBlockedWords() {
        List<String> blockedWords = new ArrayList<>();
        try (InputStream inputStream = BlockChatWords.class.getResourceAsStream("/block_words.txt");
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            blockedWords = reader.lines().collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return blockedWords;
    }
}