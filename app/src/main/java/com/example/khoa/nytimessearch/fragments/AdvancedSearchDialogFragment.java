package com.example.khoa.nytimessearch.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.khoa.nytimessearch.R;
import com.example.khoa.nytimessearch.activities.SearchActivity;

import java.util.Calendar;

public class AdvancedSearchDialogFragment extends DialogFragment {

    private static EditText etBeginDate;
    private String begindate_str;
    private Spinner sort_spinner;
    private String sort_str;
    private CheckBox cbArts;
    private CheckBox cbFashionStyle;
    private CheckBox cbSports;
    private Button btSave;

    public AdvancedSearchDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use 'newInstance' instead as shown below
    }

    public static AdvancedSearchDialogFragment newInstance(String title) {
        AdvancedSearchDialogFragment frag = new AdvancedSearchDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.advanced_search_fragment, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get fields from view
        etBeginDate = (EditText) view.findViewById(R.id.etBeginDate);
        sort_spinner = (Spinner) view.findViewById(R.id.sort_spinner);
        sort_str = sort_spinner.getSelectedItem().toString();
        cbArts = (CheckBox) view.findViewById(R.id.cbArts);
        cbFashionStyle = (CheckBox) view.findViewById(R.id.cbFasionStyle);
        cbSports = (CheckBox) view.findViewById(R.id.cbSports);
        btSave = (Button) view.findViewById(R.id.btSave);

        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Advanced Search");
        getDialog().setTitle(title);

        // Show soft keyboard automatically and request focus to field
        etBeginDate.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        // Pick a date for etBeginDate
        etBeginDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment fragment = new DatePickerDialogFragment();
                fragment.show(getFragmentManager(), "datePicker");
            }
        });

        // Save Advanced Search Info
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                begindate_str = etBeginDate.getText().toString();
                //Intent data = new Intent();
                Intent i = new Intent(getActivity(), SearchActivity.class);
                Bundle extras = new Bundle();
                extras.putString("beginDate", begindate_str);
                extras.putString("sort", sort_str);
                if (cbArts.isChecked()) { extras.putString("arts", "Arts"); } else { extras.putString("arts", ""); }
                if (cbFashionStyle.isChecked()) { extras.putString("fashionStyle", "Fashion & Style"); } else { extras.putString("fashionStyle", ""); }
                if (cbSports.isChecked()) { extras.putString("sports", "Sports"); } else { extras.putString("sports", ""); }
                i.putExtras(extras);
                startActivity(i);
                //Log.d("SearchActivity", begindate_str + " " + sort_str + " " + cbArts.isChecked() + " " + cbFashionStyle.isChecked() + " " + cbSports.isChecked());
                ft.commit();
            }
        });
    }

    public static class DatePickerDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        //@Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            String month_str = "";
            String day_str = "";
            month = month + 1;
            if (month < 10) { month_str = "0" + month; } else { month_str = "" + month; }
            if (day < 10) { day_str = "0" + day; } else { day_str = "" + day; }
            etBeginDate.setText(new StringBuilder().append(year).append(month_str).append(day_str));
        }
    }

}
