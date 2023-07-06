package com.autocasting;

import net.runelite.client.config.Alpha;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.config.Range;

import java.awt.*;

@ConfigGroup("autocasting")
public interface AutocastingConfig extends Config
{
	// OVERLAY

	@ConfigSection(
			name = "Overlay",
			description = "Overlay Settings",
			position = 0
	)
	String overlaySettings = "overlay";

	@ConfigItem(
			keyName = "showOverlay",
			name = "Show Overlay",
			description = "Show/hide overlay",
			position = 1,
			section = overlaySettings
	)
	default boolean showOverlay()
	{
		return true;
	}

	@ConfigItem(
			keyName = "showOverlayOutsideCombat",
			name = "Show Outside of Combat",
			description = "Display the autocast overlay panel outside of combat.",
			position = 2,
			section = overlaySettings
	)
	default boolean showOverlayOutsideCombat() { return true; }

	@ConfigItem(
			keyName = "showSpellName",
			name = "Show Spell Name",
			description = "Show/hide spell name",
			position = 3,
			section = overlaySettings
	)
	default boolean showSpellName()
	{
		return true;
	}

	@ConfigItem(
			keyName = "showSpellIcon",
			name = "Show Spell Icon",
			description = "Show/hide spell icon",
			position = 4,
			section = overlaySettings
	)
	default boolean showSpellIcon() { return true; }

	@ConfigItem(
			keyName = "overlayAlertStyle",
			name = "Overlay Alert Style",
			description = "Configures how the overlay behaves when the autocast is unusable.",
			position = 5,
			section = overlaySettings
	)
	default AutocastingConstants.OverlayNotificationType overlayAlertStyle() { return AutocastingConstants.OverlayNotificationType.FLASH; }


	@Alpha
	@ConfigItem(
			keyName = "overlayAlertColor",
			name = "Overlay Alert Color",
			description = "Color of Autocast overlay when flashing/solid",
			position = 6,
			section = overlaySettings
	)
	default Color overlayAlertColor() { return AutocastingConstants.RED_FLASH_COLOR; }

	@Range(min = AutocastingConstants.MINIMUM_COUNTER_FLASH_PERIOD)
	@ConfigItem(
			keyName = "flashPeriod",
			name = "Overlay Flash Period",
			description = "Period (in frames) at which the Autocast overlay flashes. Lower numbers flash faster.",
			position = 7,
			section = overlaySettings
	)
	default int getFlashPeriod()
	{
		return AutocastingConstants.DEFAULT_COUNTER_FLASH_PERIOD;
	}

	@ConfigItem(
			keyName = "showCastsRemaining",
			name = "Display Casts Remaining",
			description = "Puts the amount of casts of your autocast spell you have left, based on your current runes.",
			position = 8,
			section = overlaySettings
	)
	default boolean showCastsRemaining() { return true; }

	@Range(min = AutocastingConstants.MINIMUM_CAST_RUNES_THRESHOLD, max = AutocastingConstants.MAXIMUM_CAST_RUNES_THRESHOLD)
	@ConfigItem(
			keyName = "castRemainingThreshold",
			name = "Low Cast Threshold",
			description = "Max amount of remaining casts which will be displayed. (1-999,999)",
			position = 9,
			section = overlaySettings
	)
	default int castRemainingThreshold() { return AutocastingConstants.DEFAULT_CAST_RUNES_THRESHOLD; }

	// CHAT MESSAGES

	@ConfigSection(
			name = "In-Game Messages",
			description = "Message Settings",
			position = 10
	)
	String messageSettings = "messages";

	@ConfigItem(
			keyName = "messageOutOfCombat",
			name = "Messages Out Of Combat",
			description = "Controls if chat warnings for autocast spells will appear outside of combat.",
			position = 11,
			section = messageSettings
	)
	default boolean messageOutOfCombat() { return true; }

	@ConfigItem(
			keyName = "messageOnStatDrain",
			name = "Stat Drain Message",
			description = "Sends a game message when your magic level falls below the level required for your autocast spell.",
			position = 12,
			section = messageSettings
	)
	default boolean messageOnStatDrain() { return true; }

	@ConfigItem(
			keyName = "messageOnLowCasts",
			name = "Low Casts Remaining Message",
			description = "Messages you when your amount of casts falls to or below the Low Cast Threshold.",
			position = 13,
			section = messageSettings
	)
	default boolean messageOnLowCasts() { return true; }

	@Range(min = AutocastingConstants.MINIMUM_CAST_RUNES_THRESHOLD, max = AutocastingConstants.MAXIMUM_CAST_RUNES_THRESHOLD)
	@ConfigItem(
			keyName = "lowCastMessageThreshold",
			name = "Low Cast Threshold",
			description = "Amount of casts to message you on, based on your current runes.",
			position = 14,
			section = messageSettings
	)
	default int lowCastMessageThreshold() { return AutocastingConstants.DEFAULT_LOW_RUNES_MESSAGE_THRESHOLD; }

	@ConfigItem(
			keyName = "messageOnNoCasts",
			name = "No Casts Remainings Message",
			description = "Sends a game message when you run out of runes to be able to cast your autocast spell.",
			position = 15,
			section = messageSettings
	)
	default boolean messageOnNoCasts() { return true; }

	// DESKTOP NOTIFICATIONS

	@ConfigSection(
			name = "Desktop Notifications",
			description = "Notifications Settings",
			position = 16
	)
	String notificationSettings = "notifications";


	@ConfigItem(
			keyName = "notifyOutOfCombat",
			name = "Notify Out Of Combat",
			description = "Controls if desktop notifications for autocast spells will appear outside of combat.",
			position = 17,
			section = notificationSettings
	)
	default boolean notifyOutOfCombat() { return true; }

	@ConfigItem(
			keyName = "notifyOnStatDrain",
			name = "Stat Drain Notification",
			description = "Notifies you when your magic level falls below the level required for your autocast spell.",
			position = 18,
			section = notificationSettings
	)
	default boolean notifyOnStatDrain() { return true; }

	@ConfigItem(
			keyName = "notifyOnLowCasts",
			name = "Low Casts Remaining Notification",
			description = "Notifies you when your amount of casts falls to or below the Low Cast Threshold.",
			position = 19,
			section = notificationSettings
	)
	default boolean notifyOnLowCasts() { return true; }

	@Range(min = AutocastingConstants.MINIMUM_CAST_RUNES_THRESHOLD, max = AutocastingConstants.MAXIMUM_CAST_RUNES_THRESHOLD)
	@ConfigItem(
			keyName = "lowCastNotificationThreshold",
			name = "Low Cast Threshold",
			description = "Amount of casts to notify you on, based on your current runes.",
			position = 20,
			section = notificationSettings
	)
	default int lowCastNotificationThreshold() { return AutocastingConstants.DEFAULT_LOW_RUNES_NOTIFICATION_THRESHOLD; }


	@ConfigItem(
			keyName = "notifyOnLowCasts",
			name = "No Casts Remaining Notification",
			description = "Notifies you when you run out of runes to cast your autocast spell.",
			position = 21,
			section = notificationSettings
	)
	default boolean notifyOnNoCasts() { return true; }
}
