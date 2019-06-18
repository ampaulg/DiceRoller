package com.example.diceroller;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

final class UiHelper {

    static void writeNumber( int viewId, int outputNumber, Activity activity ) {
        TextView outputView = activity.findViewById( viewId );
        outputView.setText( String.format( Locale.CANADA, "%d", outputNumber ) );
    }

    static void writeString( int viewId, int outputStringId, Activity activity ) {
        TextView outputView = activity.findViewById( viewId );
        outputView.setText( activity.getText( outputStringId ) );
    }

    static void setDefOptionLabel( Constants.DefOption option, Activity activity ) {
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

    private static boolean checkInvalidBox( EditText box, int errorId, Activity activity ) {
        String boxInput = box.getText().toString();
        if ( TextUtils.isEmpty( boxInput ) ) {
            box.setError( activity.getString( errorId ) );
            return true;
        }

        return false;
    }

    static boolean checkInvalidInputs( Activity activity ) {
        LinearLayout topLayout = activity.findViewById( R.id.formHolder );
        return validateFields( topLayout, activity );
    }

    private static boolean validateFields( LinearLayout layout, Activity activity ) {
        boolean hasError = false;
        for ( int i = 0; i < layout.getChildCount(); i++ ) {
            View child = layout.getChildAt( i );
            if ( child.getVisibility() != View.GONE ) {
                if ( child instanceof EditText ) {
                    if ( checkInvalidBox( ( EditText ) child, R.string.required_field, activity ) ) {
                        hasError = true;
                    }
                } else if ( child instanceof LinearLayout ) {
                    if ( validateFields( ( LinearLayout ) child, activity ) ) {
                        hasError = true;
                    }
                }
            }
        }
        return hasError;
    }

    static void showMessage( int messageId, Activity activity ) {
        Toast toast = Toast.makeText( activity, messageId, Toast.LENGTH_SHORT );
        toast.show();
    }

    static int getIntInputValue( int parentId, int fieldId, Activity activity ) {
        View parent = activity.findViewById( parentId );
        EditText box = parent.findViewById( fieldId );
        String input = box.getText().toString();
        int value;
        if ( TextUtils.isEmpty( input ) ) {
            value = 0;
        } else {
            value = Integer.parseInt( input );
        }
        return value;
    }

    static <T extends View> T findNestedViewById( int parentId, int viewId, Activity activity ) {
        return activity.findViewById( parentId ).findViewById( viewId );
    }

    // Code from here
    // https://stackoverflow.com/a/17789187
    static void hideKeyboard( Activity activity ) {
        InputMethodManager imm =
                ( InputMethodManager ) activity.getSystemService( Activity.INPUT_METHOD_SERVICE );
        View view = activity.getCurrentFocus();
        if ( view == null ) {
            view = new View( activity );
        }
        imm.hideSoftInputFromWindow( view.getWindowToken(), 0 );
        view.clearFocus();
    }

    static void openKeyboard( Activity activity ) {
        // Code from here
        // https://stackoverflow.com/a/8078341
        InputMethodManager imm =
                ( InputMethodManager ) activity.getSystemService( Context.INPUT_METHOD_SERVICE );
        imm.toggleSoftInput( InputMethodManager.SHOW_FORCED,0 );
    }
}
