package com.example.diceroller;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

public final class FormBuilder {

    public static void addChallengeCost( Constants.Player player, Activity activity ) {

        int groupId;
        int labelId;
        int costId;
        View toggle;
        if ( player == Constants.Player.ATTACKER ) {
            groupId = R.id.atkChallengeCostGroup;
            labelId = R.id.atkChallengeCostLabel;
            costId = R.id.atkChallengeCost;
            toggle = activity.findViewById( R.id.atkChallengeToggle );
        } else {
            groupId = R.id.defChallengeCostGroup;
            labelId = R.id.defChallengeCostLabel;
            costId = R.id.defChallengeCost;
            toggle = activity.findViewById( R.id.defChallengeToggle );
        }

        LayoutInflater inflater = activity.getLayoutInflater();
        final View challengeCost = inflater.inflate( R.layout.challenge_roller,
                ( ViewGroup) activity.findViewById( R.id.constraintLayout ), false );

        challengeCost.setId( groupId );
        challengeCost.findViewWithTag( activity.getText( R.string.challenge_cost_label_tag ) )
                .setId( labelId );
        challengeCost.findViewWithTag( activity.getText( R.string.challenge_cost_field_tag ) )
                .setId( costId );

        setToRight( toggle, challengeCost, activity );

        UiHelper.openKeyboard( activity );
        activity.findViewById( costId ).requestFocus();
    }

    public static void removeChallengeCost( Constants.Player player, Activity activity ) {
        ViewGroup v = activity.findViewById( R.id.constraintLayout );
        if ( player == Constants.Player.ATTACKER ) {
            if ( activity.getCurrentFocus() == activity.findViewById( R.id.atkChallengeCost ) ) {
                UiHelper.clearFocus( activity );
            }
            v.removeView( activity.findViewById( R.id.atkChallengeCostGroup ) );
        } else { // Defender
            if ( activity.getCurrentFocus() == activity.findViewById( R.id.defChallengeCost ) ) {
                UiHelper.clearFocus( activity );
            }
            v.removeView( activity.findViewById( R.id.defChallengeCostGroup ) );
        }
    }

    private static void setToRight( View leftView, View rightView, Activity activity ) {

        ConstraintLayout layout = activity.findViewById( R.id.constraintLayout );
        layout.addView( rightView );

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone( layout );
        constraintSet.connect( rightView.getId(), ConstraintSet.TOP,
                leftView.getId(), ConstraintSet.TOP,
                Constants.MARGIN );
        constraintSet.connect( rightView.getId(), ConstraintSet.BOTTOM,
                leftView.getId(), ConstraintSet.BOTTOM,
                Constants.MARGIN );
        constraintSet.connect( rightView.getId(), ConstraintSet.LEFT,
                leftView.getId(), ConstraintSet.RIGHT,
                Constants.MARGIN );
        constraintSet.applyTo( layout );
    }
}
