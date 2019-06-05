package com.example.diceroller;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

final class FormBuilder {

    static void addChallengeCost( Constants.Player player, Activity activity ) {

        int groupId;
        int labelId;
        int costId;
        ViewGroup challengeGroup;
        if ( player == Constants.Player.ATTACKER ) {
            groupId = R.id.atkChallengeCostGroup;
            labelId = R.id.atkChallengeCostLabel;
            costId = R.id.atkChallengeCost;
            challengeGroup = activity.findViewById( R.id.atkChallengeGroup );
        } else {
            groupId = R.id.defChallengeCostGroup;
            labelId = R.id.defChallengeCostLabel;
            costId = R.id.defChallengeCost;
            challengeGroup = activity.findViewById( R.id.defChallengeGroup);
        }

        LayoutInflater inflater = activity.getLayoutInflater();
        final View challengeCost = inflater.inflate( R.layout.challenge_roller,
                ( ViewGroup) activity.findViewById( R.id.constraintLayout ), false );

        challengeCost.setId( groupId );
        challengeCost.findViewWithTag( activity.getText( R.string.challenge_cost_label_tag ) )
                .setId( labelId );
        challengeCost.findViewWithTag( activity.getText( R.string.challenge_cost_field_tag ) )
                .setId( costId );

        challengeGroup.addView( challengeCost );

        UiHelper.openKeyboard( activity );
        activity.findViewById( costId ).requestFocus();
    }

    static void removeChallengeCost( Constants.Player player, Activity activity ) {
        ViewGroup challengeGroup;
        int costGroup;
        int costField;

        if ( player == Constants.Player.ATTACKER ) {
            challengeGroup = activity.findViewById( R.id.atkChallengeGroup );
            costGroup = R.id.atkChallengeCostGroup;
            costField = R.id.atkChallengeCost;
        } else {
            challengeGroup = activity.findViewById( R.id.defChallengeGroup );
            costGroup = R.id.defChallengeCostGroup;
            costField = R.id.defChallengeCost;
        }

        if ( activity.getCurrentFocus() == activity.findViewById( costField ) ) {
            UiHelper.clearFocus( activity );
        }
        challengeGroup.removeView( activity.findViewById( costGroup ) );
    }

    static void addDiceCount( Constants.Player player, Activity activity ) {
        int groupId;
        int questionId;
        int spinnerId;
        int diceTypeId;
        View aboveView;
        View belowView;

        if ( player == Constants.Player.ATTACKER ) {
            groupId = R.id.atkDiceCountGroup;
            questionId = R.id.atkSectionLabel;
            spinnerId= R.id.atkDiceCount;
            diceTypeId = R.id.atkDiceType;
            aboveView = activity.findViewById( R.id.atkSectionLabel );
            belowView = activity.findViewById( R.id.atkDamageTierLabel );
        } else {
            groupId = R.id.defDiceCountGroup;
            questionId = R.id.defSectionLabel;
            spinnerId= R.id.defDiceCount;
            diceTypeId = R.id.defDiceType;
            aboveView = activity.findViewById( R.id.defOptionSpinnerLabel );
            belowView = activity.findViewById( R.id.defDamageTierLabel );
        }

        LayoutInflater inflater = activity.getLayoutInflater();
        final View diceCount = inflater.inflate( R.layout.dice_count,
                ( ViewGroup ) activity.findViewById( R.id.constraintLayout ), false );

        populateSpinner( diceCount, R.id.atkDiceCount, R.array.dice_count_options, activity );

        diceCount.setId( groupId );
        diceCount.findViewWithTag( activity.getText( R.string.dice_label_tag ) )
                .setId( questionId );
        diceCount.findViewWithTag( activity.getText( R.string.dice_count_tag) )
                .setId( spinnerId );
        diceCount.findViewWithTag( activity.getText( R.string.dice_type_tag) )
                .setId( diceTypeId );

        insertBetween( aboveView, belowView, diceCount, activity );
    }

    static void addDiceCount( LinearLayout parent, Constants.Player player, Activity activity ) {
        int groupId;
        int questionId;
        int spinnerId;
        int diceTypeId;

        if ( player == Constants.Player.ATTACKER ) {
            groupId = R.id.atkDiceCountGroup;
            questionId = R.id.atkSectionLabel;
            spinnerId= R.id.atkDiceCount;
            diceTypeId = R.id.atkDiceType;
        } else {
            groupId = R.id.defDiceCountGroup;
            questionId = R.id.defSectionLabel;
            spinnerId= R.id.defDiceCount;
            diceTypeId = R.id.defDiceType;
        }

        LayoutInflater inflater = activity.getLayoutInflater();
        final View diceCount = inflater.inflate( R.layout.dice_count,
                ( ViewGroup ) activity.findViewById( R.id.constraintLayout ), false );

        populateSpinner( diceCount, R.id.atkDiceCount, R.array.dice_count_options, activity );

        diceCount.setId( groupId );
        diceCount.findViewWithTag( activity.getText( R.string.dice_label_tag ) )
                .setId( questionId );
        diceCount.findViewWithTag( activity.getText( R.string.dice_count_tag) )
                .setId( spinnerId );
        diceCount.findViewWithTag( activity.getText( R.string.dice_type_tag) )
                .setId( diceTypeId );

        parent.addView( diceCount, 0 );
    }

    static void addBonusGroup( Constants.Player player, Activity activity ) {
        int groupId;
        int labelId;
        int valueId;
        View aboveView;
        View belowView;

        if ( player == Constants.Player.ATTACKER ) {
            groupId = R.id.atkBonusGroup;
            labelId = R.id.atkBonusLabel;
            valueId = R.id.atkBonusValue;
            aboveView = activity.findViewById( R.id.atkDiceCountGroup );
            belowView = activity.findViewById( R.id.atkDamageTierLabel );
        } else {
            groupId = R.id.defBonusGroup;
            labelId = R.id.defBonusLabel;
            valueId = R.id.defBonusValue;
            aboveView = activity.findViewById( R.id.defDiceCountGroup );
            belowView = activity.findViewById( R.id.defDamageTierLabel );
        }

        LayoutInflater inflater = activity.getLayoutInflater();
        final View bonusGroup = inflater.inflate( R.layout.bonus_group,
                ( ViewGroup ) activity.findViewById( R.id.constraintLayout ), false );

        bonusGroup.setId( groupId );
        bonusGroup.findViewWithTag( activity.getText( R.string.bonus_label_tag ) )
                .setId( labelId );
        bonusGroup.findViewWithTag( activity.getText( R.string.bonus_value_tag ) )
                .setId( valueId );

        insertBetween( aboveView, belowView, bonusGroup, activity );
    }

    static void addBonusGroup( LinearLayout parent, Constants.Player player, Activity activity ) {
        int groupId;
        int labelId;
        int valueId;

        if ( player == Constants.Player.ATTACKER ) {
            groupId = R.id.atkBonusGroup;
            labelId = R.id.atkBonusLabel;
            valueId = R.id.atkBonusValue;
        } else {
            groupId = R.id.defBonusGroup;
            labelId = R.id.defBonusLabel;
            valueId = R.id.defBonusValue;
        }

        LayoutInflater inflater = activity.getLayoutInflater();
        final View bonusGroup = inflater.inflate( R.layout.bonus_group,
                ( ViewGroup ) activity.findViewById( R.id.constraintLayout ), false );

        bonusGroup.setId( groupId );
        bonusGroup.findViewWithTag( activity.getText( R.string.bonus_label_tag ) )
                .setId( labelId );
        bonusGroup.findViewWithTag( activity.getText( R.string.bonus_value_tag ) )
                .setId( valueId );

        parent.addView( bonusGroup );
    }

    static void addChallengeToggle( Constants.Player player, Activity activity ) {
        int groupId;
        int toggleId;
        View aboveView;
        View belowView;
        int newTag;

        if ( player == Constants.Player.ATTACKER ) {
            groupId = R.id.atkChallengeGroup;
            toggleId = R.id.atkChallengeToggle;
            aboveView = activity.findViewById( R.id.atkBonusGroup );
            belowView = activity.findViewById( R.id.atkDamageTierLabel );
            newTag = R.string.attacker_tag;
        } else {
            groupId = R.id.defChallengeGroup;
            toggleId = R.id.defChallengeToggle;
            aboveView = activity.findViewById( R.id.defBonusGroup );
            belowView = activity.findViewById( R.id.defDamageTierLabel );
            newTag = R.string.defender_tag;
        }

        LayoutInflater inflater = activity.getLayoutInflater();
        final View challengeGroup = inflater.inflate( R.layout.challenge_toggle,
                ( ViewGroup ) activity.findViewById( R.id.constraintLayout ), false );

        challengeGroup.setId( groupId );

        View checkbox = challengeGroup.findViewWithTag( activity.getText( R.string.challenge_toggle_tag ) );
        checkbox.setId( toggleId );
        checkbox.setTag( activity.getText( newTag ) );

        insertBetween( aboveView, belowView, challengeGroup, activity );
    }

    static void addChallengeToggle( LinearLayout parent, Constants.Player player, Activity activity ) {
        int groupId;
        int toggleId;
        int newTag;

        if ( player == Constants.Player.ATTACKER ) {
            groupId = R.id.atkChallengeGroup;
            toggleId = R.id.atkChallengeToggle;
            newTag = R.string.attacker_tag;
        } else {
            groupId = R.id.defChallengeGroup;
            toggleId = R.id.defChallengeToggle;
            newTag = R.string.defender_tag;
        }

        LayoutInflater inflater = activity.getLayoutInflater();
        final View challengeGroup = inflater.inflate( R.layout.challenge_toggle,
                ( ViewGroup ) activity.findViewById( R.id.constraintLayout ), false );

        challengeGroup.setId( groupId );

        View checkbox = challengeGroup.findViewWithTag( activity.getText( R.string.challenge_toggle_tag ) );
        checkbox.setId( toggleId );
        checkbox.setTag( activity.getText( newTag ) );

        parent.addView( challengeGroup, 2 );
    }

    static void insertBetween( View top, View bottom, View newView, Activity activity ) {
        ConstraintLayout layout = activity.findViewById( R.id.constraintLayout );
        layout.addView( newView );

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone( layout );

        constraintSet.connect( newView.getId(), ConstraintSet.TOP,
                top.getId(), ConstraintSet.BOTTOM,
                Constants.MARGIN );

        constraintSet.connect( bottom.getId(), ConstraintSet.TOP,
                               newView.getId(), ConstraintSet.BOTTOM,
                               Constants.MARGIN );
        constraintSet.applyTo( layout );
    }

    private static void insertToRight( View leftView, View newView, Activity activity ) {

        ConstraintLayout layout = activity.findViewById( R.id.constraintLayout );
        layout.addView( newView );

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone( layout );
        constraintSet.connect( newView.getId(), ConstraintSet.TOP,
                leftView.getId(), ConstraintSet.TOP,
                Constants.MARGIN );
        constraintSet.connect( newView.getId(), ConstraintSet.BOTTOM,
                leftView.getId(), ConstraintSet.BOTTOM,
                Constants.MARGIN );
        constraintSet.connect( newView.getId(), ConstraintSet.LEFT,
                leftView.getId(), ConstraintSet.RIGHT,
                Constants.MARGIN );
        constraintSet.applyTo( layout );
    }

    static void populateSpinner( View parentView, int spinnerId, int optionListId, Activity activity ) {
        Spinner spinner = parentView.findViewById( spinnerId );
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource( activity,
                optionListId, android.R.layout.simple_spinner_item );
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        spinner.setAdapter( adapter );
    }
}
