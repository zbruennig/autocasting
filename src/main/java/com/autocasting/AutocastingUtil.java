package com.autocasting;

import net.runelite.api.Client;
import net.runelite.api.Varbits;

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
}
