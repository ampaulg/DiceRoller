package com.example.diceroller;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        populateSpinner( R.id.atkDiceCount, R.array.dice_count_options );
        populateSpinner( R.id.defDiceCount, R.array.dice_count_options );
        populateSpinner( R.id.defOption, R.array.def_options );
        Spinner defOptionSpinner = findViewById( R.id.defOption );
        defOptionSpinner.setOnItemSelectedListener( new DefOptionSelectedListener() );
    }

    public void calculate( View view ) {
        if ( !checkValidInputs() ) {
            return;
        }

        int atkRollResult1 = getRollResult( Constants.Player.ATTACKER );
        writeNumber( R.id.atkRollTotal1, atkRollResult1 );

        Spinner defOptionSpinner = findViewById( R.id.defOption );
        Constants.DefOption defOption = Constants.DefOption.valueOf(
                defOptionSpinner.getSelectedItem().toString().toUpperCase() );
        setDefOptionLabel( defOption );

        int evasionModifier = 0;
        if ( ( defOption == Constants.DefOption.DODGE ) ||
             ( defOption == Constants.DefOption.INTERCEPT ) ) {

            int defRollTotal = getRollResult( Constants.Player.DEFENDER );

            if ( defOption == Constants.DefOption.DODGE ) {
                evasionModifier = getDodgeResult( defRollTotal );
            } else { // Intercept
                evasionModifier = getAttackResult( defRollTotal, Constants.Player.ATTACKER );
            }

            writeNumber( R.id.defRollTotal, defRollTotal );
            writeNumber( R.id.defOptionResult, evasionModifier);
        }
        int atkRollResult2 = Math.max( atkRollResult1 - evasionModifier, 0 );
        writeNumber( R.id.atkRollTotal2, atkRollResult2 );

        int atkDamage1 = getAttackResult( atkRollResult2, Constants.Player.DEFENDER );
        writeNumber( R.id.atkDamageResult1, atkDamage1 );

        int atkDamage2;
        int deflectModifier = 0;
        if ( defOption == Constants.DefOption.DEFLECT ) {
            int defRoll = getRollResult( Constants.Player.DEFENDER );
            deflectModifier = ( int ) Math.ceil( defRoll / Constants.DEFLECT_SCALE );
            writeNumber( R.id.defRollTotal, defRoll );
            writeNumber( R.id.defOptionResult, deflectModifier);
        }
        atkDamage2 = Math.max( atkDamage1 - deflectModifier, 0 );
        writeNumber( R.id.atkDamageResult2, atkDamage2);
    }

    private void writeNumber( int viewId, int outputNumber ) {
        TextView outputView = findViewById( viewId );
        outputView.setText( String.format( Locale.CANADA, "%d", outputNumber ) );
    }

    private void writeString( int viewId, int outputStringId ) {
        TextView outputView = findViewById( viewId );
        outputView.setText( getText( outputStringId ) );
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

    private void setDefOptionLabel( Constants.DefOption option ) {
        int defOptionLabelViewId = R.id.defOptionChoiceLabel;
        switch ( option ) {
            case NONE:
                writeString( defOptionLabelViewId, R.string.no_def_option_label );
                break;
            case DODGE:
                writeString( defOptionLabelViewId, R.string.dodge_result_label );
                break;
            case DEFLECT:
                writeString( defOptionLabelViewId, R.string.deflect_result_label );
                break;
            case INTERCEPT:
                writeString( defOptionLabelViewId, R.string.intercept_result_label );
                break;
        }
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
            bonus = getIntInputValue( R.id.atkBonusAmount );
            baseResultViewId = R.id.atkBaseRollResult;
            challengeResultViewId = R.id.atkChallengeRollResult;
            rollSetterCheckbox = findViewById( R.id.atkRollSetterCheckbox );
            rollSetterId = R.id.atkRollSetter;
        } else {
            countBox = findViewById( R.id.defDiceCount );
            challengeBox = findViewById( R.id.defChallengeToggle );
            bonus = getIntInputValue( R.id.defBonusAmount );
            baseResultViewId = R.id.defBaseRollResult;
            challengeResultViewId = R.id.defChallengeRollResult;
            rollSetterCheckbox = findViewById( R.id.defRollSetterCheckbox );
            rollSetterId = R.id.defRollSetter;
        }

        int count = Integer.parseInt( countBox.getSelectedItem().toString() );

        int rollResult;
        if ( rollSetterCheckbox.isChecked() ) {
            rollResult = getIntInputValue( rollSetterId );
        } else {
            rollResult = rollDice( count, 6 );
        }
        writeNumber( baseResultViewId, rollResult);

        if ( challengeBox.isChecked() ) {
            rollResult += challenge( player );
        } else {
            writeString( challengeResultViewId, R.string.no_roll );
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
            challengeCost = getIntInputValue( R.id.atkChallengeCost );
            challengeResultViewId = R.id.atkChallengeRollResult;
            challengeSetterCheckbox = findViewById( R.id.atkChallengeSetterCheckbox );
            challengeSetterId = R.id.atkChallengeSetter;
        } else {
            challengeCost = getIntInputValue( R.id.defChallengeCost );
            challengeResultViewId = R.id.defChallengeRollResult;
            challengeSetterCheckbox = findViewById( R.id.defChallengeSetterCheckbox );
            challengeSetterId = R.id.defChallengeSetter;

        }

        int challengeRoll;
        if ( challengeSetterCheckbox.isChecked() ) {
            challengeRoll = getIntInputValue( challengeSetterId );
        } else {
            challengeRoll = rollDice( 1, 20 );
        }

        int result = 0;

        if ( challengeCost > challengeRoll ) {
            result = challengeRoll;
            writeNumber( challengeResultViewId, challengeRoll);
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
            passiveDef = getIntInputValue( R.id.defPassiveDef );
            armourLow = getIntInputValue( R.id.defArmourLow );
            armourHigh = getIntInputValue( R.id.defArmourHigh );
        } else {
            passiveDef = getIntInputValue( R.id.atkPassiveDef );
            armourLow = getIntInputValue( R.id.atkArmourLow );
            armourHigh = getIntInputValue( R.id.atkArmourHigh );
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

    private boolean checkValidInputs() {
        boolean errorFound = false;

        for ( int viewId : Constants.mandatoryFields ) {
            if ( checkInvalidBox( viewId, R.string.required_field ) ) {
                errorFound = true;
            }
        }

        for ( int[] checkbox : Constants.checkBoxValidatorData ) {
            CheckBox box = findViewById( checkbox[ Constants.CHECKBOX ] );
            if ( box.isChecked() ) {
                if ( checkInvalidBox( checkbox[ Constants.FIELD ], checkbox[ Constants.ERROR ] ) ) {
                    errorFound = true;
                }
            }
        }

        Spinner defOptionSpinner = findViewById( R.id.defOption );
        Constants.DefOption option = Constants.DefOption.valueOf(
                defOptionSpinner.getSelectedItem().toString().toUpperCase() );
        if ( option == Constants.DefOption.INTERCEPT ) {
            for ( int viewId : Constants.interceptMandatoryFields ) {
                if ( checkInvalidBox( viewId, R.string.intercept_required_field) ) {
                    errorFound = true;
                }
            }
        }

        return !errorFound;
    }

    private boolean checkInvalidBox(int boxId, int errorId ) {
        EditText box = findViewById( boxId );
        String boxInput = box.getText().toString();
        if ( TextUtils.isEmpty( boxInput ) ) {

            box.setError( getString( errorId ) );
            return true;
        }
        return false;
    }

    public void toggleChallenge( View view ) {
        CheckBox box = ( CheckBox ) view;
        if ( !box.isChecked() ) {
            Constants.Player playerType =  Constants.Player.valueOf( box.getTag().toString() );
            if ( playerType == Constants.Player.ATTACKER ) {
                removeError( R.id.atkChallengeCost );
            } else if ( playerType == Constants.Player.DEFENDER ) {
                removeError( R.id.defChallengeCost );
            }
        }
    }

    public void toggleSetter( View view ) {
        CheckBox box = ( CheckBox ) view;
        if ( !box.isChecked() ) {
            Constants.SetterType setter =  Constants.SetterType.valueOf( box.getTag().toString() );
            switch ( setter ) {
                case ATK_ROLL:
                    removeError( R.id.atkRollSetter );
                    break;
                case ATK_CHALLENGE:
                    removeError( R.id.atkChallengeSetter );
                    break;
                case DEF_ROLL:
                    removeError( R.id.defRollSetter );
                    break;
                case DEF_CHALLENGE:
                    removeError( R.id.defChallengeSetter );
                    break;
            }
        }
    }

    private int getIntInputValue( int id ) {
        EditText box = findViewById(id);
        String input = box.getText().toString();
        int value;
        if ( TextUtils.isEmpty( input ) ) {
            value = 0;
        } else {
            value = Integer.parseInt(input);
        }
        return value;
    }

    private void populateSpinner( int spinnerId, int optionListId ) {
        Spinner spinner = findViewById( spinnerId );
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource( this,
                optionListId, android.R.layout.simple_spinner_item );
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        spinner.setAdapter( adapter );
    }

    private void removeError( int boxId ) {
        EditText box = findViewById( boxId );
        box.setError( null );
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
                    damage = getIntInputValue( R.id.atkLowDamage );
                } else {
                    damage = getIntInputValue( R.id.defLowDamage );
                }
                break;
            case MED:
                if ( initiator == Constants.Player.ATTACKER ) {
                    damage = getIntInputValue( R.id.atkMedDamage );
                } else {
                    damage = getIntInputValue( R.id.defMedDamage );
                }
                break;
            case HIGH:
                if ( initiator == Constants.Player.ATTACKER ) {
                    damage = getIntInputValue( R.id.atkHighDamage );
                } else {
                    damage = getIntInputValue( R.id.defHighDamage );
                }
                break;
            default:
                throw new IllegalArgumentException( "Invalid damage tier" );
        }

        return damage;
    }

    private class DefOptionSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected( AdapterView<?> parent, View view, int pos, long id ) {
            Spinner optionSpinner = findViewById( R.id.defOption );
            Constants.DefOption option = Constants.DefOption.valueOf(
                    optionSpinner.getSelectedItem().toString().toUpperCase() );
            if ( option != Constants.DefOption.INTERCEPT ) {
                for ( int viewId : Constants.interceptMandatoryFields ) {
                    removeError( viewId );
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }
}
