package com.example.diceroller;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    DiceGroup atkDiceGroup;
    DiceGroup defDiceGroup;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        FormBuilder.populateSpinner( findViewById( R.id.constraintLayout ), R.id.defOption, R.array.def_options, this );
        Spinner defOptionSpinner = findViewById( R.id.defOption );
        defOptionSpinner.setOnItemSelectedListener( new DefOptionSelectedListener( this ) );
        /*
        FormBuilder.addDiceCount( Constants.Player.ATTACKER, this );
        FormBuilder.addBonusGroup( Constants.Player.ATTACKER, this );
        FormBuilder.addChallengeToggle( Constants.Player.ATTACKER, this );
        */
        /*
        FormBuilder.addDiceCount( Constants.Player.DEFENDER, this );
        FormBuilder.addBonusGroup( Constants.Player.DEFENDER, this );
        FormBuilder.addChallengeToggle( Constants.Player.DEFENDER, this );
        */

        atkDiceGroup = new DiceGroup( Constants.Player.ATTACKER, this );
        defDiceGroup = new DiceGroup( Constants.Player.DEFENDER, this );

        FormBuilder.insertBetween(
                findViewById( R.id.atkSectionLabel),
                findViewById( R.id.atkDamageTierLabel ),
                atkDiceGroup, this );
        FormBuilder.insertBetween(
                findViewById( R.id.defOptionSpinnerLabel ),
                findViewById( R.id.defDamageTierLabel ),
                defDiceGroup, this );
    }


    public void calculate( View view ) {
        if ( !UiHelper.checkValidInputs( this ) ) {
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
        int challengeResultViewId;

        CheckBox rollSetterCheckbox;
        int rollSetterId;

        if ( player == Constants.Player.ATTACKER ) {
            countBox = findViewById( R.id.atkDiceCount );
            challengeBox = findViewById( R.id.atkChallengeToggle );
            bonus = UiHelper.getIntInputValue( R.id.atkBonusValue, this );
            baseResultViewId = R.id.atkBaseRollResult;
            challengeResultViewId = R.id.atkChallengeRollResult;
            rollSetterCheckbox = findViewById( R.id.atkRollSetterCheckbox );
            rollSetterId = R.id.atkRollSetter;
        } else {
            countBox = findViewById( R.id.defDiceCount );
            challengeBox = findViewById( R.id.defChallengeToggle );
            bonus = UiHelper.getIntInputValue( R.id.defBonusValue, this );
            baseResultViewId = R.id.defBaseRollResult;
            challengeResultViewId = R.id.defChallengeRollResult;
            rollSetterCheckbox = findViewById( R.id.defRollSetterCheckbox );
            rollSetterId = R.id.defRollSetter;
        }

        int count = Integer.parseInt( countBox.getSelectedItem().toString() );

        int rollResult;
        if ( rollSetterCheckbox.isChecked() ) {
            rollResult = UiHelper.getIntInputValue( rollSetterId, this );
        } else {
            rollResult = rollDice( count, 6 );
        }
        UiHelper.writeNumber( baseResultViewId, rollResult, this );

        if ( challengeBox.isChecked() ) {
            rollResult += challenge( player );
        } else {
            UiHelper.writeString( challengeResultViewId, R.string.no_roll, this );
        }

        rollResult += bonus;

        return rollResult;
    }

    private int challenge( Constants.Player player ) {
        int challengeCost;
        int challengeResultViewId;

        CheckBox challengeSetterCheckbox;
        int challengeSetterId;

        if ( player == Constants.Player.ATTACKER ) {
            challengeCost = UiHelper.getIntInputValue( R.id.atkChallengeCost, this );
            challengeResultViewId = R.id.atkChallengeRollResult;
            challengeSetterCheckbox = findViewById( R.id.atkChallengeSetterCheckbox );
            challengeSetterId = R.id.atkChallengeSetter;
        } else {
            challengeCost = UiHelper.getIntInputValue( R.id.defChallengeCost, this );
            challengeResultViewId = R.id.defChallengeRollResult;
            challengeSetterCheckbox = findViewById( R.id.defChallengeSetterCheckbox );
            challengeSetterId = R.id.defChallengeSetter;

        }

        int challengeRoll;
        if ( challengeSetterCheckbox.isChecked() ) {
            challengeRoll = UiHelper.getIntInputValue( challengeSetterId, this );
        } else {
            challengeRoll = rollDice( 1, 20 );
        }

        int result = 0;

        if ( challengeCost > challengeRoll ) {
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

        if ( target == Constants.Player.DEFENDER ) {
            passiveDef = UiHelper.getIntInputValue( R.id.defPassiveDef, this );
            armourLow = UiHelper.getIntInputValue( R.id.defArmourLow, this );
            armourHigh = UiHelper.getIntInputValue( R.id.defArmourHigh, this );
        } else {
            passiveDef = UiHelper.getIntInputValue( R.id.atkPassiveDef, this );
            armourLow = UiHelper.getIntInputValue( R.id.atkArmourLow, this );
            armourHigh = UiHelper.getIntInputValue( R.id.atkArmourHigh, this );
        }

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
        Constants.Player playerType =  Constants.Player.valueOf( box.getTag().toString() );

        if ( box.isChecked() ) {
            // Needs fixing: gotta do this or else a keyboard doesn't appear for the new cost
            // if there's already an EditText selected while toggling
            UiHelper.hideKeyboard( this );
            FormBuilder.addChallengeCost( playerType, this );
        } else {
            FormBuilder.removeChallengeCost( playerType, this );
        }
    }

    public void toggleSetter( View view ) {
        CheckBox box = ( CheckBox ) view;
        if ( !box.isChecked() ) {
            Constants.SetterType setter =  Constants.SetterType.valueOf( box.getTag().toString() );
            switch ( setter ) {
                case ATK_ROLL:
                    UiHelper.removeError( R.id.atkRollSetter, this );
                    break;
                case ATK_CHALLENGE:
                    UiHelper.removeError( R.id.atkChallengeSetter, this );
                    break;
                case DEF_ROLL:
                    UiHelper.removeError( R.id.defRollSetter, this );
                    break;
                case DEF_CHALLENGE:
                    UiHelper.removeError( R.id.defChallengeSetter, this );
                    break;
            }
        }
    }

    private int getDamage( Constants.DamageTier tier, Constants.Player initiator ) {
        int damage;

        switch ( tier ) {
            case NONE:
                damage = 0;
                break;
            case ONE:
                damage = Constants.PASSIVE_DAMAGE;
                break;
            case LOW:
                if ( initiator == Constants.Player.ATTACKER ) {
                    damage = UiHelper.getIntInputValue( R.id.atkLowDamage, this );
                } else {
                    damage = UiHelper.getIntInputValue( R.id.defLowDamage, this );
                }
                break;
            case MED:
                if ( initiator == Constants.Player.ATTACKER ) {
                    damage = UiHelper.getIntInputValue( R.id.atkMedDamage, this );
                } else {
                    damage = UiHelper.getIntInputValue( R.id.defMedDamage, this );
                }
                break;
            case HIGH:
                if ( initiator == Constants.Player.ATTACKER ) {
                    damage = UiHelper.getIntInputValue( R.id.atkHighDamage, this );
                } else {
                    damage = UiHelper.getIntInputValue( R.id.defHighDamage, this );
                }
                break;
            default:
                throw new IllegalArgumentException( "Invalid damage tier" );
        }

        return damage;
    }
}
