package com.autocasting;

import com.autocasting.dependencies.attackstyles.WeaponType;
import com.google.inject.Provides;
import javax.inject.Inject;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import net.runelite.api.*;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.StatChanged;
import net.runelite.api.events.VarbitChanged;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

import java.awt.image.BufferedImage;

/*
TODO: Add remaining spells to AutocastingSpells enum.
TODO: Only show overlay when player is equipping a staff or other autocastable weapon.
*/

@Slf4j
@PluginDescriptor(
		name = "Autocasting",
		description = "Notifies client when magic level falls below required level for spell.",
		tags = {"notifier", "notifications", "mage", "magic", "reduced", "reduction", "level", "drain", "autocast", "autocasting", "cast", "utilities"}
)
public class AutocastingPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private AutocastingConfig config;

	@Getter
	private boolean magicLevelTooLowForSpell;

	@Getter
	private boolean isEquippedWeaponMagic;

	private WeaponType currentWeaponType;

	@Getter
	private AutocastingSpell currentAutocastSpell;

	private static final String AUTOCAST_UNEQUIP_NOTIFICATION_MESSAGE = "Your magic level has dropped below what is required to autocast your spell.";

	private static final int VARBIT_AUTOCAST_SPELL = (276);


	@Inject
	private AutocastingOverlay autocastOverlay;

	@Inject
	private SpriteManager spriteManager;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private ClientThread clientThread;

	@Inject
	private ChatMessageManager chatMessageManager;

	@Override
	protected void startUp() throws Exception
	{
		log.info("Autocasting started!"); // TODO remove at some point
		clientThread.invoke(this::startPlugin);
		overlayManager.add(autocastOverlay);
	}

	@Override
	protected void shutDown() throws Exception
	{
		// TODO remove log?
		log.info("Autocasting stopped!");
  		overlayManager.remove(autocastOverlay);
	}

	private void startPlugin()
	{
		if (client.getGameState() == GameState.LOGGED_IN) {
			updateAutocastSpell();
		}
		else
		{
			currentAutocastSpell = AutocastingSpell.NO_SPELL;
		}
	}

	@Subscribe
	public void onVarbitChanged(VarbitChanged event)
	{
		updateAutocastSpell();
		updateIsEquippedWeaponMagic();
	}

	@Subscribe
	public void onStatChanged(StatChanged event)
	{

		Skill skill = event.getSkill();
		int boostedLevel = event.getBoostedLevel();

		if (skill.getName().equals(Skill.MAGIC.getName()))
		{
			// Now need to check if new boostedLevel is still high enough for the autocast spell
			int varbitValue = client.getVarbitValue(VARBIT_AUTOCAST_SPELL);
			AutocastingSpell autocastSpell = AutocastingSpell.getAutocastingSpell(varbitValue);
			if (boostedLevel < autocastSpell.getLevelRequirement())
			{
				magicLevelTooLowForSpell = true;
				if (config.sendGameMessage()) { sendChatMessage(AUTOCAST_UNEQUIP_NOTIFICATION_MESSAGE); }
			}
			else
			{
				magicLevelTooLowForSpell = false;
			}
		}
	}

	private void updateAutocastSpell() {
		// Get new autocast spell.
		AutocastingSpell newAutocastSpell = getAutocastSpellFromClient();
		if (newAutocastSpell == null) { return; }

		// If the new spell is not null, and there is currently no autocast spell selected, update it
		// TODO: The following if else tree can be mostly replaced with "currentAutocastSpell = newAutocastSpell;" for all cases, but
		// 		but I'm not sure how expensive that can add up to be with varbits changing all the time. Maybe it's better
		// 		than checking these conditions every time, I dont know. Someone with more knowledge, please let me know.
		if (currentAutocastSpell == null)
		{
			currentAutocastSpell = newAutocastSpell;
		}
		else if (newAutocastSpell.getVarbitValue() == currentAutocastSpell.getVarbitValue())
		{
			// No change to autocast spell
			return;
		}
		else
		{
			// Otherwise, update the spell.
			currentAutocastSpell = newAutocastSpell;
		}
	}

	private void updateIsEquippedWeaponMagic()
	{
		int weaponTypeID = client.getVar(Varbits.EQUIPPED_WEAPON_TYPE);
		WeaponType newWeaponType = WeaponType.getWeaponType(weaponTypeID);
		if (newWeaponType == currentWeaponType) { return; }
		currentWeaponType = newWeaponType;

		isEquippedWeaponMagic = newWeaponType == WeaponType.TYPE_18 ||
				newWeaponType == WeaponType.TYPE_21;

		// The below types do have a casting option, but do not autocast spells, so leave them out.
		// TYPE_6: These are salamanders. They do not autocast, but give magic xp, so technically have a "casting" option.
		// TYPE_23: Trident, Sanguinesti, etc. Do not have autocast options, so do not show overlay when these are equipped.
	}

	private AutocastingSpell getAutocastSpellFromClient()
	{
		// Get new autocast spell.
		int varbitValue = client.getVarbitValue(VARBIT_AUTOCAST_SPELL);
//		log.info("Autocast varbit value: " + varbitValue);
		return AutocastingSpell.getAutocastingSpell(varbitValue);
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

	public BufferedImage getImage(int spriteID)
	{
		return spriteManager.getSprite(spriteID, 0);
	}


	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{  // TODO remove this
		if (gameStateChanged.getGameState() == GameState.LOGGED_IN)
		{
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Autocasting says " + config.greeting(), null);
		}
	}

	@Provides
	AutocastingConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(AutocastingConfig.class);
	}
}