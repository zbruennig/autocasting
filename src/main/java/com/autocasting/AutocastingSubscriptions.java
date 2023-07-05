package com.autocasting;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Skill;
import net.runelite.api.Varbits;
import net.runelite.api.events.StatChanged;
import net.runelite.api.events.VarbitChanged;
import net.runelite.client.eventbus.Subscribe;

import javax.inject.Inject;
import javax.inject.Singleton;

@Slf4j
@Singleton
public class AutocastingSubscriptions {
    @Inject
    private Client client;

    @Inject
    private AutocastingConfig config;

    @Inject
    private AutocastingMessages messages;

    @Inject
    private AutocastingNotifications notifications;

    @Inject
    private AutocastingState state;

    @Inject
    private AutocastingUtil util;

    @Subscribe
    public void onVarbitChanged(VarbitChanged event)
    {
        boolean isRelevantAutocastVarbit =
                event.getVarbitId() == AutocastingConstants.VARBIT_AUTOCAST_SPELL
                || event.getVarbitId() == Varbits.EQUIPPED_WEAPON_TYPE;
        if (isRelevantAutocastVarbit)
        {
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
                if (state.isMagicLevelTooLowForSpell()) {
                    // We don't need to send new messages or update state if it didn't actually change
                    state.setMagicLevelTooLowForSpell(true);
                    messages.sendStatDrainMessage();
                    notifications.notifyStatDrain();
                }
            }
            else {
                state.setMagicLevelTooLowForSpell(false);
            }
        }
    }
}
