package com.autocasting;

import com.autocasting.datatypes.PlayerInventory;
import com.autocasting.datatypes.RuneItem;
import com.autocasting.datatypes.RuneType;
import com.autocasting.datatypes.Spell;
import com.autocasting.dependencies.attackstyles.WeaponType;

import lombok.Getter;
import lombok.Setter;

import net.runelite.client.game.SpriteManager;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.image.BufferedImage;
import java.util.Map;

@Singleton
public class AutocastingState {
    @Inject
    private AutocastingClientData clientData;

    @Inject
    private AutocastingRuneUtil runeUtil;

    @Inject
    private SpriteManager spriteManager;

    @Getter
    @Setter
    private boolean magicLevelTooLowForSpell;

    @Getter
    @Setter
    private boolean isEquippedWeaponMagic;

    @Getter
    @Setter
    private WeaponType currentWeaponType;

    @Getter
    @Setter
    private Spell currentAutocastSpell;

    @Getter
    @Setter
    private PlayerInventory playerInventory;

    @Getter
    @Setter
    private boolean consideredInCombat;

    @Getter
    @Setter
    private int lastCombatTick;

    @Getter
    @Setter
    private Map<RuneType, Integer> availableRunes;

    @Getter
    @Setter
    private int castsRemaining;

    public void updateAutocastSpell()
    {
        // Get new autocast spell.
        Spell newAutocastSpell = clientData.getAutocastSpell();
        if (newAutocastSpell == null) { return; }
        // Some input (weapon change, attack style change, or autocast change) happened, so magic level must be fine now
        setMagicLevelTooLowForSpell(false);
        // If the new spell is not null, and there is currently no autocast spell selected, update it
        if (currentAutocastSpell == null || newAutocastSpell.getVarbitValue() != currentAutocastSpell.getVarbitValue())
        {
            currentAutocastSpell = newAutocastSpell;
        }
        updateCastsRemaining();
    }

    /*
    Calculating casts remaining is a 3 stage process, first we check inventory, equipment, and rune pouch for relevant items
    These get set on PlayerInventory. Subscription hooks will call stage 1 methods - updateRunes, updateInfiniteRuneSources
    At the end of both functions the second stage is called which computes total runes based on all factors.
    Finally based on current autocast spell and stage 2 results we can math out the number of casts available
    */

    public void updateRunes()
    {
        setPlayerInventory(runeUtil.updateInventory(playerInventory));
        calculateNetRuneTypes();
    }

    public void updateInfiniteRuneSources()
    {
        setPlayerInventory(runeUtil.updateEquipment(playerInventory));
        calculateNetRuneTypes();
    }

    private void calculateNetRuneTypes()
    {
        setAvailableRunes(runeUtil.availableRunes(playerInventory));
        updateCastsRemaining();
    }

    public void updateCastsRemaining()
    {
        if (currentAutocastSpell != null && currentAutocastSpell.getVarbitValue() > 0) {
            castsRemaining = runeUtil.calculateCastsRemaining(currentAutocastSpell, availableRunes);
            setCastsRemaining(castsRemaining);
        }
    }

    public void updateIsEquippedWeaponMagic()
    {
        int weaponTypeID = clientData.getWeaponTypeId();
        WeaponType newWeaponType = WeaponType.getWeaponType(weaponTypeID);
        if (newWeaponType == currentWeaponType) { return; }
        currentWeaponType = newWeaponType;

        isEquippedWeaponMagic = newWeaponType == WeaponType.TYPE_18 ||
                newWeaponType == WeaponType.TYPE_21;

        // The below types do have a casting option, but do not autocast spells, so leave them out.
        // TYPE_6: These are salamanders. They do not autocast, but give magic xp, so technically have a "casting" option.
        // TYPE_23: Trident, Sanguinesti, etc. Do not have autocast options, so do not show overlay when these are equipped.
    }

    public void updateCombatStatus()
    {
        boolean inCombat = clientData.isInCombat();
        int currentTick = clientData.getGameTick();
        if (inCombat) {
            lastCombatTick = currentTick;
            consideredInCombat = true;
        } else {
            consideredInCombat = currentTick - lastCombatTick <= AutocastingConstants.OUT_OF_COMBAT_TICK_DELAY;
        }
    }

    public BufferedImage getImage(int spriteID)
    {
        return spriteManager.getSprite(spriteID, 0);
    }
}
