package com.example.diceroller;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

class DefOptionSelectedListener implements AdapterView.OnItemSelectedListener {

    private final MainActivity activity;

    DefOptionSelectedListener( MainActivity activity ) {
        this.activity = activity;
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id ) {
        Spinner optionSpinner = activity.findViewById( R.id.defOption );
        Constants.DefOption option = Constants.DefOption.valueOf(
                optionSpinner.getSelectedItem().toString().toUpperCase() );
        FormBuilder.updateForm( option, activity );
    }

    @Override
    public void onNothingSelected( AdapterView<?> arg0 ) {
    }
}