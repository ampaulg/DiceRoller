package com.example.diceroller;

import android.text.TextUtils;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class UiHelper {

    public static void writeNumber( int viewId, int outputNumber, AppCompatActivity activity ) {
        TextView outputView = activity.findViewById( viewId );
        outputView.setText( String.format( Locale.CANADA, "%d", outputNumber ) );
    }

    public static void writeString( int viewId, int outputStringId, AppCompatActivity activity ) {
        TextView outputView = activity.findViewById( viewId );
        outputView.setText( activity.getText( outputStringId ) );
    }

    public static void removeError( int boxId, AppCompatActivity activity ) {
        EditText box = activity.findViewById( boxId );
        box.setError( null );
    }

    public static void setDefOptionLabel( Constants.DefOption option, AppCompatActivity activity ) {
        int defOptionLabelViewId = R.id.defOptionChoiceLabel;
        switch ( option ) {
            case NONE:
                writeString( defOptionLabelViewId, R.string.no_def_option_label, activity );
                break;
            case DODGE:
                writeString( defOptionLabelViewId, R.string.dodge_result_label, activity );
                break;
            case DEFLECT:
                writeString( defOptionLabelViewId, R.string.deflect_result_label, activity );
                break;
            case INTERCEPT:
                writeString( defOptionLabelViewId, R.string.intercept_result_label, activity );
                break;
        }
    }

    private static boolean checkInvalidBox( int boxId, int errorId, AppCompatActivity activity ) {
        EditText box = activity.findViewById( boxId );
        String boxInput = box.getText().toString();
        if ( TextUtils.isEmpty( boxInput ) ) {

            box.setError( activity.getString( errorId ) );
            return true;
        }
        return false;
    }

    public static boolean checkValidInputs( AppCompatActivity activity ) {
        boolean errorFound = false;

        for ( int viewId : Constants.mandatoryFields ) {
            if ( checkInvalidBox( viewId, R.string.required_field, activity ) ) {
                errorFound = true;
            }
        }

        for ( int[] checkbox : Constants.checkBoxValidatorData ) {
            CheckBox box = activity.findViewById( checkbox[ Constants.CHECKBOX ] );
            if ( box.isChecked() ) {
                if ( checkInvalidBox( checkbox[ Constants.FIELD ], checkbox[ Constants.ERROR ], activity ) ) {
                    errorFound = true;
                }
            }
        }

        Spinner defOptionSpinner = activity.findViewById( R.id.defOption );
        Constants.DefOption option = Constants.DefOption.valueOf(
                defOptionSpinner.getSelectedItem().toString().toUpperCase() );
        if ( option == Constants.DefOption.INTERCEPT ) {
            for ( int viewId : Constants.interceptMandatoryFields ) {
                if ( checkInvalidBox( viewId, R.string.intercept_required_field, activity ) ) {
                    errorFound = true;
                }
            }
        }

        return !errorFound;
    }

    public static int getIntInputValue( int id, AppCompatActivity activity ) {
        EditText box = activity.findViewById( id );
        String input = box.getText().toString();
        int value;
        if ( TextUtils.isEmpty( input ) ) {
            value = 0;
        } else {
            value = Integer.parseInt( input );
        }
        return value;
    }

}
