package com.example.diceroller;

import android.app.Activity;
import android.widget.LinearLayout;

public class DiceGroup extends LinearLayout {

    DiceGroup( Constants.Player player, Activity activity ) {
        super( activity );

        int groupId;

        if ( player == Constants.Player.ATTACKER ) {
            groupId = R.id.atkDiceGroup;
        } else {
            groupId = R.id.defDiceGroup;
        }

        this.setLayoutParams( new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT ) );
        this.setOrientation( LinearLayout.VERTICAL );
        this.setId( groupId );

        FormBuilder.addDiceCount( this, player, activity );
        FormBuilder.addBonusGroup( this, player, activity );
        FormBuilder.addChallengeToggle( this, player, activity );

        //FormBuilder.insertBetween( aboveView, belowView, this, activity );
    }
}
