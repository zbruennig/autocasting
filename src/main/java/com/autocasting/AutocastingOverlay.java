package com.autocasting;

import com.google.inject.Inject;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.components.*;
import net.runelite.client.ui.overlay.components.ComponentOrientation;

import java.awt.*;
import java.awt.image.BufferedImage;

class AutocastingOverlay extends OverlayPanel
{
    private int counter = 0;
    private final int SPELL_NAME_AND_ICON_GAP = 4;

    private final AutocastingPlugin plugin;
    private final AutocastingConfig config;

    @Inject
    AutocastingOverlay(AutocastingPlugin plugin, AutocastingConfig config)
    {
        super(plugin);
        this.plugin = plugin;
        this.config = config;
        super.setDynamicFont(true);
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        if (!plugin.isEquippedWeaponMagic()) { return null; }
        if (!config.showOverlay()) { return null; }
        else if (!config.showSpellName() && !config.showSpellIcon()) { return null; }
        else if (config.showSpellName() && config.showSpellIcon())
        {
            TitleComponent spellNameComponent = TitleComponent.builder()
                    .text(getCurrentSpellName())
                    .build();

            ImageComponent spellImageComponent = new ImageComponent(getCurrentSpellImage());

            SplitComponent spellNameIcon = SplitComponent.builder()
                    .first(spellNameComponent)
                    .second(spellImageComponent)
                    .orientation(ComponentOrientation.HORIZONTAL)
                    .gap(new Point(SPELL_NAME_AND_ICON_GAP, 0))
                    .build();

            panelComponent.getChildren().add(
                    spellNameIcon);
        }
        else if(config.showSpellName() && !config.showSpellIcon())
        {
            TitleComponent spellNameComponent = TitleComponent.builder()
                    .text(getCurrentSpellName())
                    .build();
            panelComponent.getChildren().add(
                    spellNameComponent);
        }
        else if (!config.showSpellName() && config.showSpellIcon())
        {
            ImageComponent spellImageComponent = new ImageComponent(getCurrentSpellImage());
            panelComponent.getChildren().add(
                    spellImageComponent);
        }
        else { return null; }

        panelComponent.setPreferredSize(new Dimension(
                graphics.getFontMetrics().stringWidth(getCurrentSpellName()) + 10,
                0));

        configureBackground();

        return super.render(graphics);
    }

    private void configureBackground()
    {
        if (config.overlayNotificationType() == AutocastingConstants.OverlayNotificationType.FLASH)
        {
            flashBackground();
        }
        else
        {
            solidBackground();
        }
    }

    private void flashBackground()
    {
        if (plugin.isMagicLevelTooLowForSpell() && (++counter % config.getFlashPeriod() > config.getFlashPeriod() / 2))
        {
            panelComponent.setBackgroundColor(config.getOverlayColor());
        }
        else
        {
            panelComponent.setBackgroundColor(ComponentConstants.STANDARD_BACKGROUND_COLOR);
        }
    }

    private void solidBackground()
    {
        if (plugin.isMagicLevelTooLowForSpell() && config.overlayNotificationType() == AutocastingConstants.OverlayNotificationType.SOLID)
        {
            panelComponent.setBackgroundColor(config.getOverlayColor());
        }
        else
        {
            panelComponent.setBackgroundColor(ComponentConstants.STANDARD_BACKGROUND_COLOR);
        }
    }

    private BufferedImage getCurrentSpellImage()
    {
        return plugin.getImage(plugin.getCurrentAutocastSpell().getSpriteID());
    }

    private String getCurrentSpellName()
    {
        return plugin.getCurrentAutocastSpell().getName();
    }

}