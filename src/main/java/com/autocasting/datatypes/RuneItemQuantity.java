package com.autocasting.datatypes;

import lombok.Data;

/**
 * A "tuple" of RuneItem and a quantity,
 * in other words a stack of physical runes
 */
@Data
public class RuneItemQuantity {
    private RuneItem rune;
    private int quantity;
}
