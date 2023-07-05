package com.autocasting;

import com.autocasting.dependencies.attackstyles.WeaponType;
import com.google.inject.Provides;
import javax.inject.Inject;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import net.runelite.api.*;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.config.ConfigManager;
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
	private AutocastingState state;

	@Inject
	private AutocastingOverlay autocastOverlay;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private ClientThread clientThread;


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
  		overlayManager.remove(autocastOverlay);
	}

	private void startPlugin()
	{
		if (client.getGameState() == GameState.LOGGED_IN) {
			state.updateAutocastSpell();
		}
		else {
			state.setCurrentAutocastSpell(AutocastingSpell.NO_SPELL);
		}
	}

	@Provides
	AutocastingConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(AutocastingConfig.class);
	}
}