package com.autocasting;

import net.runelite.api.ChatMessageType;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AutocastingMessages {
    @Inject
    private ChatMessageManager chatMessageManager;

    @Inject
    private AutocastingConfig config;

    @Inject
    private AutocastingState state;

    public void sendStatDrainMessage()
    {
        if (config.messageOnStatDrain()) {
            String autocastName = state.getCurrentAutocastSpell().getName();
            String message = String.format(AutocastingConstants.STAT_DRAIN_FORMAT, autocastName);
            sendChatMessage(message);
        }
    }

    public void sendNoCastsMessage()
    {
        if (config.messageOnStatDrain()) {
            String autocastName = state.getCurrentAutocastSpell().getName();
            String message = String.format(AutocastingConstants.NO_CASTS_FORMAT, autocastName);
            sendChatMessage(message);
        }
    }

    public void sendLowCastsMessage()
    {
        if (config.messageOnStatDrain()) {
            String autocastName = state.getCurrentAutocastSpell().getName();
            String threshold = Integer.toString(config.lowCastMessageThreshold());
            String message = String.format(AutocastingConstants.LOW_CASTS_FORMAT, threshold, autocastName);
            sendChatMessage(message);
        }
    }

    // Borrowed from DailyTasksPlugin.java
    private void sendChatMessage(String chatMessage)
    {
        final String message = new ChatMessageBuilder()
                .append(ChatColorType.HIGHLIGHT)
                .append(chatMessage)
                .build();

        chatMessageManager.queue(
                QueuedMessage.builder()
                        .type(ChatMessageType.CONSOLE)
                        .runeLiteFormattedMessage(message)
                        .build());
    }
}
