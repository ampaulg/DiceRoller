package com.example.diceroller;

final class Constants {

    private Constants() {}

    enum Player {
        ATTACKER,
        DEFENDER
    }

    enum DefOption {
        NONE,
        DODGE,
        DEFLECT,
        INTERCEPT
    }

    enum DamageTier {
        NONE,
        ONE,
        LOW,
        MED,
        HIGH
    }

    enum DiceMode {
        ROLL,
        SET
    }

    enum SetterType {
        DEBUG_TOGGLE,
        ATK_ROLL,
        ATK_CHALLENGE,
        DEF_ROLL,
        DEF_CHALLENGE
    }

    static final double DEFLECT_SCALE = 10.0;

    static final int[] DEBUG_SETTERS = {
            R.id.atkRollSetterCheckbox,
            R.id.atkChallengeSetterCheckbox,
            R.id.defRollSetterCheckbox,
            R.id.defChallengeSetterCheckbox
    };

    static final int PASSIVE_DAMAGE = 1;

    static final int DODGE_LOW = 5;
    static final int DODGE_MED = 10;
    static final int DODGE_HIGH = 15;
}
