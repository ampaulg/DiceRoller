package com.example.diceroller;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Locale;

public final class UiHelper {

    public static void writeNumber( int viewId, int outputNumber, Activity activity ) {
        TextView outputView = activity.findViewById( viewId );
        outputView.setText( String.format( Locale.CANADA, "%d", outputNumber ) );
    }

    public static void writeString( int viewId, int outputStringId, Activity activity ) {
        TextView outputView = activity.findViewById( viewId );
        outputView.setText( activity.getText( outputStringId ) );
    }

    public static void removeError( int boxId, Activity activity ) {
        EditText box = activity.findViewById( boxId );
        box.setError( null );
    }

    public static void setDefOptionLabel( Constants.DefOption option, Activity activity ) {
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

    private static boolean checkInvalidBox( int boxId, int errorId, Activity activity ) {
        EditText box = activity.findViewById( boxId );
        String boxInput = box.getText().toString();
        if ( TextUtils.isEmpty( boxInput ) ) {

            box.setError( activity.getString( errorId ) );
            return true;
        }
        return false;
    }

    public static boolean checkValidInputs( Activity activity ) {
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

    public static int getIntInputValue( int id, Activity activity ) {
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

    // Bugged, does not work when the screen has been scrolled to not be at the start
    public static void clearFocus( Activity activity ) {
        activity.findViewById( R.id.constraintLayout ).requestFocus();
        hideKeyboard( activity );
    }

    // Code from here
    // https://stackoverflow.com/a/17789187
    public static void hideKeyboard( Activity activity ) {
        InputMethodManager imm =
                ( InputMethodManager ) activity.getSystemService( Activity.INPUT_METHOD_SERVICE );
        View view = activity.getCurrentFocus();
        if ( view == null ) {
            view = new View( activity );
        }
        imm.hideSoftInputFromWindow( view.getWindowToken(), 0 );
        view.clearFocus();
    }

    public static  void openKeyboard( Activity activity ) {
        // Code from here
        // https://stackoverflow.com/a/8078341
        InputMethodManager imm =
                ( InputMethodManager ) activity.getSystemService( Context.INPUT_METHOD_SERVICE );
        imm.toggleSoftInput( InputMethodManager.SHOW_FORCED,0 );
    }

}
