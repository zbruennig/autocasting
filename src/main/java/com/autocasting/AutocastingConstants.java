package com.autocasting;

import lombok.AllArgsConstructor;

import java.awt.*;

public final class AutocastingConstants {
    // Config defaults
    public static final int DEFAULT_COUNTER_FLASH_PERIOD = 40;
    public static final int MINIMUM_COUNTER_FLASH_PERIOD = 1;
    public static final int DEFAULT_CAST_RUNES_THRESHOLD = 100;
    public static final int MINIMUM_CAST_RUNES_THRESHOLD = 1;
    public static final int MAXIMUM_CAST_RUNES_THRESHOLD = 99999;
    public static final int DEFAULT_LOW_RUNES_MESSAGE_THRESHOLD = 250;
    public static final int DEFAULT_LOW_RUNES_NOTIFICATION_THRESHOLD = 100;
    public static final Color RED_FLASH_COLOR = new Color(255, 0, 0, 186);

    // Common constants
    public static final int VARBIT_AUTOCAST_SPELL = 276;

    // Message constants
    public static final String AUTOCAST_UNEQUIP_NOTIFICATION_MESSAGE = "Your magic level has dropped below what is required to autocast your spell.";

    @AllArgsConstructor
    public static enum OverlayNotificationType
    {
        FLASH("Flash"),
        SOLID("Solid"),
        NONE("None");

        private final String value;

        @Override
        public String toString()
        {
            return value;
        }
    }


    private AutocastingConstants() {}
}
