package com.autocasting;

import net.runelite.client.Notifier;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AutocastingNotifications {
    @Inject
    private Notifier notifier;

    @Inject
    private AutocastingConfig config;

    @Inject
    private AutocastingState state;

    public void notifyStatDrain() {
        if (config.notifyOnStatDrain()) {
            String autocastName = state.getCurrentAutocastSpell().getName();
            String message = String.format(AutocastingConstants.STAT_DRAIN_FORMAT, autocastName);
            sendNotification(message);
        }
    }

    public void handleCastsUpdated(int previous, int current) {
        if (current == 0) {
            if (previous != 0) {
                notifyNoCasts();
            }
        }
        else {
            int threshold = config.lowCastNotificationThreshold();
            if (previous > threshold && current <= threshold) {
                notifyLowCasts(current);
            }
        }
    }

    private void notifyNoCasts() {
        if (config.notifyOnNoCasts()) {
            String autocastName = state.getCurrentAutocastSpell().getName();
            String message = String.format(AutocastingConstants.NO_CASTS_FORMAT, autocastName);
            sendNotification(message);
        }
    }

    private void notifyLowCasts(int amount) {
        if (config.notifyOnLowCasts()) {
            String autocastName = state.getCurrentAutocastSpell().getName();
            String message = String.format(AutocastingConstants.LOW_CASTS_FORMAT, amount, autocastName);
            sendNotification(message);
        }
    }

    private void sendNotification(String message) {
        if (state.isConsideredInCombat() || config.notifyOutOfCombat()) {
            notifier.notify(message);
        }
    }
}
