package com.example.diceroller;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.dynamic_form );


        FormBuilder.populateSpinner( findViewById( R.id.formHolder ), R.id.defOption, R.array.def_options, this );
        Spinner defOptionSpinner = findViewById( R.id.defOption );
        defOptionSpinner.setOnItemSelectedListener( new DefOptionSelectedListener( this ) );

        FormBuilder.populateSpinner( findViewById( R.id.atkDiceGroup), R.id.diceCount, R.array.dice_count_options, this );
        FormBuilder.populateSpinner( findViewById( R.id.defDiceGroup), R.id.diceCount, R.array.dice_count_options, this );
    }

    public void calculate( View view ) {

        if ( UiHelper.checkInvalidInputs( this ) ) {
            UiHelper.showMessage( R.string.error_toast_msg, this );
            return;
        }

        int atkRollResult1 = getRollResult( Constants.Player.ATTACKER );
        UiHelper.writeNumber( R.id.atkRollTotal1, atkRollResult1, this );

        Spinner defOptionSpinner = findViewById( R.id.defOption );
        Constants.DefOption defOption = Constants.DefOption.valueOf(
                defOptionSpinner.getSelectedItem().toString().toUpperCase() );
        UiHelper.setDefOptionLabel( defOption, this );

        int evasionModifier = 0;
        if ( ( defOption == Constants.DefOption.DODGE ) ||
             ( defOption == Constants.DefOption.INTERCEPT ) ) {

            int defRollTotal = getRollResult( Constants.Player.DEFENDER );

            if ( defOption == Constants.DefOption.DODGE ) {
                evasionModifier = getDodgeResult( defRollTotal );
            } else { // Intercept
                evasionModifier = getAttackResult( defRollTotal, Constants.Player.ATTACKER );
            }

            UiHelper.writeNumber( R.id.defRollTotal, defRollTotal, this );
            UiHelper.writeNumber( R.id.defOptionResult, evasionModifier, this );
        }
        int atkRollResult2 = Math.max( atkRollResult1 - evasionModifier, 0 );
        UiHelper.writeNumber( R.id.atkRollTotal2, atkRollResult2, this );

        int atkDamage1 = getAttackResult( atkRollResult2, Constants.Player.DEFENDER );
        UiHelper.writeNumber( R.id.atkDamageResult1, atkDamage1, this );

        int atkDamage2;
        int deflectModifier = 0;
        if ( defOption == Constants.DefOption.DEFLECT ) {
            int defRoll = getRollResult( Constants.Player.DEFENDER );
            deflectModifier = ( int ) Math.ceil( defRoll / Constants.DEFLECT_SCALE );
            UiHelper.writeNumber( R.id.defRollTotal, defRoll, this );
            UiHelper.writeNumber( R.id.defOptionResult, deflectModifier, this );
        }
        atkDamage2 = Math.max( atkDamage1 - deflectModifier, 0 );
        UiHelper.writeNumber( R.id.atkDamageResult2, atkDamage2, this );

        FormBuilder.updateResults( defOption, this );
    }

    private int getDodgeResult( int defRoll ) {
        Constants.DamageTier tier = getDamageTier( defRoll, Constants.Player.DEFENDER );
        int dodgeResult;
        switch ( tier ) {
            case NONE:
                dodgeResult = Constants.DODGE_LOW;
                break;
            case ONE:
                dodgeResult = Constants.DODGE_LOW;
                break;
            case LOW:
                dodgeResult = Constants.DODGE_LOW;
                break;
            case MED:
                dodgeResult = Constants.DODGE_MED;
                break;
            case HIGH:
                dodgeResult = Constants.DODGE_HIGH;
                break;
            default:
                throw new IllegalArgumentException( "Invalid damage tier" );
        }
        return dodgeResult;
    }

    private int getRollResult( Constants.Player player ) {
        Spinner countBox;
        CheckBox challengeBox;
        int bonus;

        int baseResultViewId;
        int bonusTotalViewId;
        int challengeResultViewId;

        CheckBox enableDebugCheckbox = findViewById( R.id.debugModeToggle );
        CheckBox rollSetterCheckbox;
        int diceGroupId;

        if ( player == Constants.Player.ATTACKER ) {
            countBox = UiHelper.findNestedViewById( R.id.atkDiceGroup, R.id.diceCount, this );
            challengeBox = UiHelper.findNestedViewById( R.id.atkChallengeGroup, R.id.challengeToggle, this );
            bonus = UiHelper.getIntInputValue( R.id.atkBonusGroup, R.id.bonusValue, this );
            baseResultViewId = R.id.atkBaseRollResult;
            bonusTotalViewId = R.id.atkBonusTotal;
            challengeResultViewId = R.id.atkChallengeResult;
            rollSetterCheckbox = findViewById( R.id.atkRollSetterCheckbox );
            diceGroupId = R.id.atkDiceGroup;
        } else {
            countBox = UiHelper.findNestedViewById( R.id.defDiceGroup, R.id.diceCount, this );
            challengeBox = UiHelper.findNestedViewById( R.id.defChallengeGroup, R.id.challengeToggle, this );
            bonus = UiHelper.getIntInputValue( R.id.defBonusGroup, R.id.bonusValue, this );
            baseResultViewId = R.id.defBaseRollResult;
            bonusTotalViewId = R.id.defBonusTotal;
            challengeResultViewId = R.id.defChallengeResult;
            rollSetterCheckbox = findViewById( R.id.defRollSetterCheckbox );
            diceGroupId = R.id.defDiceGroup;
        }

        int count = Integer.parseInt( countBox.getSelectedItem().toString() );

        int rollResult;
        if ( enableDebugCheckbox.isChecked() && rollSetterCheckbox.isChecked() ) {
            rollResult = UiHelper.getIntInputValue( diceGroupId, R.id.diceSetterValue, this );
        } else {
            rollResult = rollDice( count, 6 );
        }
        UiHelper.writeNumber( baseResultViewId, rollResult, this );

        rollResult += bonus;
        UiHelper.writeNumber( bonusTotalViewId, rollResult, this );

        if ( challengeBox.isChecked() ) {
            rollResult += challenge( player );
        } else {
            UiHelper.writeString( challengeResultViewId, R.string.no_roll, this );
        }

        return rollResult;
    }

    private int challenge( Constants.Player player ) {
            int challengeCost;
            int challengeResultViewId;
            int challengeGroupId;
            int setterBoxId;

        CheckBox enableDebugCheckbox = findViewById( R.id.debugModeToggle );
        CheckBox challengeSetterCheckbox;
        if ( player == Constants.Player.ATTACKER ) {
            challengeGroupId = R.id.atkChallengeGroup;
            challengeResultViewId = R.id.atkChallengeResult;
            setterBoxId = R.id.atkChallengeSetterCheckbox;
        } else {
            challengeGroupId = R.id.defChallengeGroup;
            challengeResultViewId = R.id.defChallengeResult;
            setterBoxId = R.id.defChallengeSetterCheckbox;
        }

        challengeCost = UiHelper.getIntInputValue( challengeGroupId, R.id.challengeCost, this );
        challengeSetterCheckbox = findViewById( setterBoxId );

        int challengeRoll;
        boolean challengeSet = ( enableDebugCheckbox.isChecked() &&
                                 challengeSetterCheckbox.isChecked() );

        if ( challengeSet ) {
            challengeRoll = UiHelper.getIntInputValue( challengeGroupId, R.id.challengeSetterValue, this );
        } else {
            challengeRoll = rollDice( 1, 20 );
        }

        int result = 0;

        if ( ( challengeCost > challengeRoll ) || challengeSet ){
            result = challengeRoll;
            UiHelper.writeNumber( challengeResultViewId, challengeRoll, this );
        } else {
            String failResult= challengeRoll + " " + getText( R.string.failed_roll );
            TextView outputView = findViewById( challengeResultViewId );
            outputView.setText( failResult );
        }

        return result;
    }

    private int rollDice( int diceCount, int diceType ) {
        Random rand = new Random();
        int diceResult = 0;
        for ( int i = 0; i < diceCount; i++ ) {
            diceResult += ( rand.nextInt( diceType ) + 1 );
        }

        if ( ( diceType == 6 ) && ( diceResult == diceCount ) ) {
            diceResult = 0;
        }

        return diceResult;
    }

    private int getAttackResult( int roll, Constants.Player target ) {
        // get tier
        Constants.DamageTier tier = getDamageTier( roll, target );
        // get damage for that tier
        return getDamage( tier, getOtherPlayer( target ) );
    }

    private Constants.Player getOtherPlayer( Constants.Player player ) {
        if ( player == Constants.Player.ATTACKER ) {
            return Constants.Player.DEFENDER;
        } else return Constants.Player.ATTACKER;
    }

    private Constants.DamageTier getDamageTier( int roll, Constants.Player target ) {
        int passiveDef;
        int armourLow;
        int armourHigh;
        int defGroupId;

        if ( target == Constants.Player.DEFENDER ) {
            defGroupId = R.id.defDefenseGroup;
        } else {
            defGroupId = R.id.atkDefenseGroup;
        }
        passiveDef = UiHelper.getIntInputValue( defGroupId, R.id.passiveDefValue, this );
        armourLow = UiHelper.getIntInputValue( defGroupId, R.id.armourLowValue, this );
        armourHigh = UiHelper.getIntInputValue( defGroupId, R.id.armourHighValue, this );

        Constants.DamageTier tier;
        if ( roll < passiveDef ) {
            tier = Constants.DamageTier.NONE;
        } else if ( roll == passiveDef ) {
            tier = Constants.DamageTier.ONE;
        } else if ( roll < armourLow ) {
            tier = Constants.DamageTier.LOW;
        } else if ( roll <= armourHigh ) {
            tier = Constants.DamageTier.MED;
        } else {
            tier = Constants.DamageTier.HIGH;
        }

        return tier;
    }

    public void toggleChallenge( View view ) {
        CheckBox box = ( CheckBox ) view;
        View parent = ( View ) view.getParent();
        Constants.DiceMode mode;
        CheckBox debugToggle;
        Constants.Player player;

        if ( parent.getId() == R.id.atkChallengeGroup ) {
            player = Constants.Player.ATTACKER;
            debugToggle = findViewById(R.id.atkChallengeSetterCheckbox );
        } else {
            player = Constants.Player.DEFENDER;
            debugToggle = findViewById( R.id.defChallengeSetterCheckbox );
        }

        if ( debugToggle.isChecked() ) {
            mode = Constants.DiceMode.SET;
        } else {
            mode = Constants.DiceMode.ROLL;
        }

        if ( box.isChecked() ) {
            // Needs fixing: gotta do this or else a keyboard doesn't appear for the new cost
            // if there's already an EditText selected while toggling
            UiHelper.hideKeyboard( this );
            FormBuilder.showChallengeField( player, mode, this );
        } else {
            FormBuilder.hideChallengeField( player, this );
        }
    }

    public void toggleSetter( View view ) {
        CheckBox box = ( CheckBox ) view;
        Constants.SetterType setter =  Constants.SetterType.valueOf( box.getTag().toString() );
        switch ( setter ) {
            case DEBUG_TOGGLE:
                FormBuilder.toggleDebugMode( this );
                break;
            case ATK_ROLL:
                FormBuilder.toggleDiceDebug( Constants.Player.ATTACKER, this );
                break;
            case ATK_CHALLENGE:
                FormBuilder.toggleChallengeDebug( Constants.Player.ATTACKER, this );
                break;
            case DEF_ROLL:
                FormBuilder.toggleDiceDebug( Constants.Player.DEFENDER, this );
                break;
            case DEF_CHALLENGE:
                FormBuilder.toggleChallengeDebug( Constants.Player.DEFENDER, this );
                break;
        }
    }

    private int getDamage( Constants.DamageTier tier, Constants.Player initiator ) {
        int damage;
        int damageGroupId;

        if ( initiator == Constants.Player.ATTACKER ) {
            damageGroupId = R.id.atkDamageGroup;
        } else {
            damageGroupId = R.id.defDamageGroup;
        }

        switch ( tier ) {
            case NONE:
                damage = 0;
                break;
            case ONE:
                damage = Constants.PASSIVE_DAMAGE;
                break;
            case LOW:
                damage = UiHelper.getIntInputValue( damageGroupId, R.id.lowDamage, this );
                break;
            case MED:
                damage = UiHelper.getIntInputValue( damageGroupId, R.id.medDamage, this );
                break;
            case HIGH:
                damage = UiHelper.getIntInputValue( damageGroupId, R.id.highDamage, this );
                break;
            default:
                throw new IllegalArgumentException( "Invalid damage tier" );
        }

        return damage;
    }
}
