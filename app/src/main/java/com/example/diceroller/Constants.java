package com.example.diceroller;

public final class Constants {

    private Constants() {}

    public enum Player {
        ATTACKER,
        DEFENDER
    }

    public enum DefOption {
        NONE,
        DODGE,
        DEFLECT,
        INTERCEPT
    }

    public static final int[] mandatoryFields = {
            R.id.atkLowDamage,
            R.id.atkMedDamage,
            R.id.atkHighDamage,
            R.id.defPassiveDef,
            R.id.defArmourLow,
            R.id.defArmourHigh
    };

    public static final int[] interceptMandatoryFields = {
            R.id.defLowDamage,
            R.id.defMedDamage,
            R.id.defHighDamage,
            R.id.atkPassiveDef,
            R.id.atkArmourLow,
            R.id.atkArmourHigh
    };

}
