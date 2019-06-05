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

    enum SetterType {
        ATK_ROLL,
        ATK_CHALLENGE,
        DEF_ROLL,
        DEF_CHALLENGE
    }

    static final double DEFLECT_SCALE = 10.0;

    static final int[] mandatoryFields = {
            R.id.atkLowDamage,
            R.id.atkMedDamage,
            R.id.atkHighDamage,
            R.id.defPassiveDef,
            R.id.defArmourLow,
            R.id.defArmourHigh
    };

    static final int[] interceptMandatoryFields = {
            R.id.defLowDamage,
            R.id.defMedDamage,
            R.id.defHighDamage,
            R.id.atkPassiveDef,
            R.id.atkArmourLow,
            R.id.atkArmourHigh
    };

    // This isn't a pretty solution but will be replaced by something that uses objects when making
    // the dynamic form
    static final int[][] checkBoxValidatorData = {
            // checkbox id, input field id, error message id
            { R.id.atkChallengeToggle, R.id.atkChallengeCost, R.string.challenge_required_field },
            { R.id.defChallengeToggle, R.id.defChallengeCost, R.string.challenge_required_field },
            { R.id.atkRollSetterCheckbox, R.id.atkRollSetter, R.string.setter_required_field },
            { R.id.atkChallengeSetterCheckbox, R.id.atkChallengeSetter, R.string.setter_required_field },
            { R.id.defRollSetterCheckbox, R.id.defRollSetter, R.string.setter_required_field },
            { R.id.defChallengeSetterCheckbox, R.id.defChallengeSetter, R.string.setter_required_field },
    };
    static final int CHECKBOX = 0;
    static final int FIELD = 1;
    static final int ERROR = 2;

    static final int PASSIVE_DAMAGE = 1;

    static final int DODGE_LOW = 5;
    static final int DODGE_MED = 10;
    static final int DODGE_HIGH = 15;

    //static final int MARGIN = 16;
    static final int MARGIN = 0;

}
