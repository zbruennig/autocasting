package com.autocasting.datatypes;


import lombok.Getter;
import lombok.Setter;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class PlayerInventory {
    @Getter
    private InfiniteRuneItem mainHand;

    @Getter
    private InfiniteRuneItem offHand;

    @Getter
    @Setter
    private Pouch pouch;

    @Getter
    private List<RuneItemQuantity> runes;

    public PlayerInventory() {
        mainHand = null;
        offHand = null;
        pouch = null;
        runes = new ArrayList<>();
    }

    public void addInfiniteRuneItem(InfiniteRuneItem item) {
        switch (item.getSlot()) {
            case WEAPON:
                mainHand = item;
                break;
            case SHIELD:
                offHand = item;
                break;
        }
    }

    public void clearItems() {

    }

    public void addRuneStack(RuneItemQuantity runeStack) {
        runes.add(runeStack);
    }

    public void clearRunes() {
        runes = new ArrayList<>();
    }
}
