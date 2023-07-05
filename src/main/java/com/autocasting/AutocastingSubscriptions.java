package com.autocasting;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Skill;
import net.runelite.api.events.StatChanged;
import net.runelite.api.events.VarbitChanged;
import net.runelite.client.eventbus.Subscribe;

import javax.inject.Inject;

@Slf4j
public class AutocastingSubscriptions {
    @Inject
    private Client client;

    @Inject
    private AutocastingConfig config;

    @Inject
    private AutocastingMessages messages;

    @Inject
    private AutocastingState state;

    @Inject
    private AutocastingUtil util;

    @Subscribe
    public void onVarbitChanged(VarbitChanged event)
    {
        if (event.getVarbitId() == AutocastingConstants.VARBIT_AUTOCAST_SPELL)
        {
            log.info("Autocast varbit changed");
            log.info(event.toString());
            state.updateAutocastSpell();
            state.updateIsEquippedWeaponMagic();
        }
    }

    @Subscribe
    public void onStatChanged(StatChanged event)
    {
        if (event.getSkill().getName().equals(Skill.MAGIC.getName())) {
            log.info(event.toString());
            int boostedLevel = event.getBoostedLevel();

            // Now need to check if new boostedLevel is still high enough for the autocast spell
            int varbitValue = util.getAutocastVarbit();
            AutocastingSpell autocastSpell = AutocastingSpell.getAutocastingSpell(varbitValue);
            if (boostedLevel < autocastSpell.getLevelRequirement()) {
                state.setMagicLevelTooLowForSpell(true);
                if (config.messageOnStatDrain()) {
                    messages.sendChatMessage(AutocastingConstants.AUTOCAST_UNEQUIP_NOTIFICATION_MESSAGE);
                }
            }
            else {
                state.setMagicLevelTooLowForSpell(false);
            }
        }
    }
}
