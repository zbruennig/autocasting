package com.autocasting;

import com.autocasting.datatypes.PlayerInventory;
import com.autocasting.datatypes.Spell;
import com.autocasting.dependencies.attackstyles.WeaponType;

import lombok.Getter;
import lombok.Setter;

import net.runelite.client.game.SpriteManager;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.image.BufferedImage;

@Singleton
public class AutocastingState {
    @Inject
    private AutocastingUtil util;

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

    public void updateAutocastSpell()
    {
        // Get new autocast spell.
        Spell newAutocastSpell = util.getAutocastSpell();
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
    At the end of both functions the second stage is called which computes total runes based on all factors. TODO HOW TO STORE???
    Finally based on current autocast spell and stage 2 results we can math out the number of casts available
    */

    public void updateRunes()
    {
        // TODO Iterate through all the places
        calculateNetRuneTypes();
    }

    public void updateInfiniteRuneSources()
    {
        // TODO Iterate through your equipped
        calculateNetRuneTypes();
    }

    private void calculateNetRuneTypes()
    {
        // TODO
        updateCastsRemaining();
    }

    private void updateCastsRemaining()
    {

    }


    public void updateIsEquippedWeaponMagic()
    {
        int weaponTypeID = util.getWeaponTypeId();
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
        boolean inCombat = util.isInCombat();
        int currentTick = util.getGameTick();
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
