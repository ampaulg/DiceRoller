package com.example.diceroller;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

final class FormBuilder {

    private final static int[] NONE_FORM = {
            R.id.atkSectionLabel,
            R.id.atkDiceGroup,
            R.id.atkBonusGroup,
            R.id.atkChallengeGroup,
            R.id.atkDamageGroup,
            R.id.defSectionLabel,
            R.id.defOptionGroup,
            R.id.defDefenseGroup,
            R.id.rollButton,
            R.id.debugGroup,
            R.id.resultHolder
    };

    private final static int[] DODGE_OR_DEFLECT_FORM = {
            R.id.atkSectionLabel,
            R.id.atkDiceGroup,
            R.id.atkBonusGroup,
            R.id.atkChallengeGroup,
            R.id.atkDamageGroup,
            R.id.defDiceGroup,
            R.id.defBonusGroup,
            R.id.defChallengeGroup,
            R.id.defSectionLabel,
            R.id.defOptionGroup,
            R.id.defDefenseGroup,
            R.id.rollButton,
            R.id.debugGroup,
            R.id.resultHolder
    };

    private final static int[] INTERCEPT_FORM = {
            R.id.atkSectionLabel,
            R.id.atkDiceGroup,
            R.id.atkBonusGroup,
            R.id.atkChallengeGroup,
            R.id.atkDamageGroup,
            R.id.atkDefenseGroup,
            R.id.defSectionLabel,
            R.id.defDiceGroup,
            R.id.defBonusGroup,
            R.id.defChallengeGroup,
            R.id.defDamageGroup,
            R.id.defOptionGroup,
            R.id.defDefenseGroup,
            R.id.rollButton,
            R.id.debugGroup,
            R.id.resultHolder
    };

    private final static int[] NONE_RESULTS = {
            R.id.atkBaseRollResultGroup,
            R.id.atkRollBonusResult,
            R.id.atkDamageResult1Group
    };

    private final static int[] DODGE_OR_INTERCEPT_RESULTS = {
            R.id.atkBaseRollResultGroup,
            R.id.atkRollBonusResult,
            R.id.defBaseRollResultGroup,
            R.id.defRollBonusResult,
            R.id.defChoiceGroup,
            R.id.atkRollTotal2Group,
            R.id.atkDamageResult1Group
    };

    private final static int[] DEFLECT_RESULTS = {
            R.id.atkBaseRollResultGroup,
            R.id.atkRollBonusResult,
            R.id.defBaseRollResultGroup,
            R.id.defRollBonusResult,
            R.id.defChoiceGroup,
            R.id.atkDamageResult1Group,
            R.id.atkDamageResult2Group
    };

    private final static int[] ATK_CHALLENGE_RESULTS = {
            R.id.atkChallengeResultGroup,
            R.id.atkChallengeTotalGroup
    };

    private final static int[] DEF_CHALLENGE_RESULTS = {
            R.id.defChallengeResultGroup,
            R.id.defChallengeTotalGroup
    };

    static void updateForm( Constants.DefOption option, Activity activity ) {

        int[] formMembers;
        switch ( option ) {
            case NONE:
                formMembers = NONE_FORM;
                break;
            case DODGE:
                formMembers = DODGE_OR_DEFLECT_FORM;
                break;
            case DEFLECT:
                formMembers = DODGE_OR_DEFLECT_FORM;
                break;
            case INTERCEPT:
                formMembers = INTERCEPT_FORM;
                break;
            default:
                throw new IllegalArgumentException( "Invalid defensive option" );
        }

        LinearLayout formHolder = activity.findViewById( R.id.formHolder );

        for ( int i = 0; i < formHolder.getChildCount(); i++ ) {
            View v = formHolder.getChildAt( i );
            if ( idInArray( v.getId(), formMembers ) ) {
                v.setVisibility( View.VISIBLE );
            } else {
                v.setVisibility( View.GONE );
            }
        }
    }

    static void updateResults( Constants.DefOption option, Activity activity ) {

        CheckBox atkChallengeBox = UiHelper.findNestedViewById( R.id.atkChallengeGroup,
                                                                R.id.challengeToggle,
                                                                activity );
        boolean atkChallenged = atkChallengeBox.isChecked();
        CheckBox defChallengeBox = UiHelper.findNestedViewById( R.id.defChallengeGroup,
                                                                R.id.challengeToggle,
                                                                activity );
        boolean defChallenged = defChallengeBox.isChecked();

        int[] resultMembers;
        switch ( option ) {
            case NONE:
                resultMembers = NONE_RESULTS;
                break;
            case DODGE:
                resultMembers = DODGE_OR_INTERCEPT_RESULTS;
                break;
            case DEFLECT:
                resultMembers = DEFLECT_RESULTS;
                break;
            case INTERCEPT:
                resultMembers = DODGE_OR_INTERCEPT_RESULTS;
                break;
            default:
                throw new IllegalArgumentException( "Invalid defensive option" );
        }

        LinearLayout resultsHolder = activity.findViewById( R.id.resultHolder );

        for ( int i = 0; i < resultsHolder.getChildCount(); i++ ) {
            View v = resultsHolder.getChildAt( i );
            if ( idInArray( v.getId(), resultMembers ) ) {
                v.setVisibility( View.VISIBLE );
            } else {
                if ((atkChallenged && (idInArray(v.getId(), ATK_CHALLENGE_RESULTS))) ||
                        (defChallenged && (idInArray(v.getId(), DEF_CHALLENGE_RESULTS)))) {
                    v.setVisibility(View.VISIBLE);
                } else {
                    v.setVisibility(View.GONE);
                }
            }
        }
    }

    // Doing this manually so the API level doesn't need to be brought up
    private static boolean idInArray ( int id, int[] array ) {
        for ( int member : array ) {
            if ( id == member ) {
                return true;
            }
        }
        return false;
    }

    static void toggleDebugMode( Activity activity ) {
        for ( int id : Constants.DEBUG_SETTERS ) {
            View setter = activity.findViewById( id );
            if ( setter.getVisibility() == View.GONE ) {
                    setter.setVisibility( View.VISIBLE );
            } else {
                setter.setVisibility( View.GONE );
            }
        }
    }

    static void toggleDiceDebug( Constants.Player player, Activity activity ) {

        ViewGroup diceGroup;
        int groupId;

        if ( player == Constants.Player.ATTACKER ) {
            groupId = R.id.atkDiceGroup;
        } else {
            groupId = R.id.defDiceGroup;
        }

        diceGroup = activity.findViewById( groupId );

        if ( diceGroup.getVisibility() == View.GONE ) {
            return;
        } else {
            for ( int i = 0; i < diceGroup.getChildCount(); i++ ) {
                View child = diceGroup.getChildAt( i );
                if ( !child.getTag().equals( diceGroup ) ) {
                    if ( child.getVisibility() == View.GONE ) {
                        child.setVisibility( View.VISIBLE );
                    } else {
                        child.setVisibility( View.GONE );
                    }
                }
            }
        }

        UiHelper.hideKeyboard( activity );
    }

    static void showChallengeField( Constants.Player player, Constants.DiceMode mode, Activity activity ) {

        ViewGroup challengeGroup;
        String targetTag;

        if ( mode == Constants.DiceMode.SET ) {
            targetTag = ( String ) activity.getText( R.string.challenge_set_tag );
        } else {
            targetTag = ( String ) activity.getText( R.string.challenge_cost_tag );
        }

        if ( player == Constants.Player.ATTACKER ) {
            challengeGroup = activity.findViewById( R.id.atkChallengeGroup );
        } else {
            challengeGroup = activity.findViewById( R.id.defChallengeGroup);
        }

        for ( int i = 0; i < challengeGroup.getChildCount(); i++ ) {
            View child = challengeGroup.getChildAt( i );
            if ( child.getTag().equals( targetTag ) ) {
                child.setVisibility( View.VISIBLE );
                if ( child instanceof TextView ) {
                    child.requestFocus();
                }
            }
        }

        UiHelper.openKeyboard( activity );
    }

    static void hideChallengeField( Constants.Player player, Activity activity ) {

        ViewGroup challengeGroup;
        String targetTag = ( String )activity.getText( R.string.challenge_toggle_tag );

        if ( player == Constants.Player.ATTACKER ) {
            challengeGroup = activity.findViewById( R.id.atkChallengeGroup );
        } else {
            challengeGroup = activity.findViewById( R.id.defChallengeGroup);
        }


        for ( int i = 0; i < challengeGroup.getChildCount(); i++ ) {
            View child = challengeGroup.getChildAt( i );
            if ( !child.getTag().equals( targetTag ) ) {
                child.setVisibility( View.GONE );
            }
        }

        UiHelper.openKeyboard( activity );
    }

    static void toggleChallengeDebug( Constants.Player player, Activity activity ) {

        CheckBox challengeToggle;
        ViewGroup challengeGroup;
        int groupId;

        if ( player == Constants.Player.ATTACKER ) {
            groupId = R.id.atkChallengeGroup;
        } else {
            groupId = R.id.defChallengeGroup;
        }

        challengeGroup = activity.findViewById( groupId );
        challengeToggle = UiHelper.findNestedViewById( groupId, R.id.challengeToggle, activity );

        String checkboxTag = ( String )activity.getText( R.string.challenge_toggle_tag );

        if ( !challengeToggle.isChecked() ) {
            return;
        }

        for ( int i = 0; i < challengeGroup.getChildCount(); i++ ) {
            View child = challengeGroup.getChildAt( i );
            if ( !child.getTag().equals( checkboxTag ) ) {
                if ( child.getVisibility() == View.GONE ) {
                    child.setVisibility( View.VISIBLE );
                } else {
                    child.setVisibility( View.GONE );
                }
            }
        }

        UiHelper.hideKeyboard( activity );
    }

    static void populateSpinner( View parentView, int spinnerId, int optionListId, Activity activity ) {
        Spinner spinner = parentView.findViewById( spinnerId );
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource( activity,
                optionListId, android.R.layout.simple_spinner_item );
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        spinner.setAdapter( adapter );
    }
}
