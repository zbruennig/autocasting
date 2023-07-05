package com.autocasting;

import net.runelite.api.Actor;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.Player;
import net.runelite.api.Varbits;

import org.apache.commons.lang3.ArrayUtils;

import javax.inject.Inject;

public class AutocastingUtil {
    @Inject
    private Client client;

    public int getAutocastVarbit() {
        return client.getVarbitValue(AutocastingConstants.VARBIT_AUTOCAST_SPELL);
    }

    public AutocastingSpell getAutocastSpell() {
        int varbitValue = getAutocastVarbit();
        return AutocastingSpell.getAutocastingSpell(varbitValue);
    }

    public int getWeaponTypeId() {
        return client.getVarbitValue(Varbits.EQUIPPED_WEAPON_TYPE);
    }

    // Based off StatusBarsPlugin.java
    public boolean computeIsInCombat()
    {
        final Player localPlayer = client.getLocalPlayer();
        if (localPlayer == null)
        {
            return false;
        }
        final Actor interacting = localPlayer.getInteracting();
        boolean fightingNPC = interacting instanceof NPC && ArrayUtils.contains(((NPC) interacting).getComposition().getActions(), "Attack");
        boolean fightingPlayer = interacting instanceof Player && client.getVarbitValue(Varbits.PVP_SPEC_ORB) == 1;
        return fightingNPC || fightingPlayer;
    }
}
