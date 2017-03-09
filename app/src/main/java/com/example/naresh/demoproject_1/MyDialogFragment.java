package com.example.naresh.demoproject_1;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by naresh on 8/3/17.
 */

public class MyDialogFragment extends DialogFragment {
    private View rootView;
    private Spinner spinnerOrder, spinnerSort;
    private Button btnFromDatePicker, btnToDatePicker, btnYes, btnNo;

    private Calendar FromCalender = Calendar.getInstance();
    private Calendar ToCalender = Calendar.getInstance();

    private DatePickerDialog.OnDateSetListener fromDateListener, toDateListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.mydialogfragment, container, false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        spinnerOrder = (Spinner) rootView.findViewById(R.id.spinner_order);
        spinnerSort = (Spinner) rootView.findViewById(R.id.spinner_sort);
        btnFromDatePicker = (Button) rootView.findViewById(R.id.btn_from_date_picker);
        btnToDatePicker = (Button) rootView.findViewById(R.id.btn_to_date_picker);
        btnYes = (Button) rootView.findViewById(R.id.btn_yes);
        btnNo = (Button) rootView.findViewById(R.id.btn_no);

        fromDateListener = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                 FromCalender.set(Calendar.YEAR, year);
                 FromCalender.set(Calendar.MONTH, month);
                 FromCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateFromDatePicker();
            }
        };

        toDateListener = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                 ToCalender.set(Calendar.YEAR, year);
                 ToCalender.set(Calendar.MONTH, month);
                 ToCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateTODatePicker();
            }
        };

        btnFromDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), fromDateListener,  FromCalender
                        .get(Calendar.YEAR),  FromCalender.get(Calendar.MONTH),
                         FromCalender.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btnToDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), toDateListener,  ToCalender
                        .get(Calendar.YEAR),  ToCalender.get(Calendar.MONTH),
                         ToCalender.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        return rootView;
    }

    private void updateFromDatePicker() {
        String myFormat = "dd/MM/yyyy"; // In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        btnFromDatePicker.setText(sdf.format( FromCalender.getTime()));
    }

    private void updateTODatePicker() {
        String myFormat = "dd/MM/yyyy"; // In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        btnToDatePicker.setText(sdf.format( ToCalender.getTime()));
    }
}
