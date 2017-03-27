package com.example.naresh.demoproject_1.dialog;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;

import com.example.naresh.demoproject_1.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by naresh on 8/3/17.
 */

public class FilterDialogFragment extends DialogFragment {

    private String TAG = FilterDialogFragment.class.getSimpleName();
    private View rootView;
    private Button btnFromDatePicker, btnToDatePicker, btnYes, btnNo;
    private Spinner spinnerOrder, spinnerSort;
    private String myDateFormat = "dd/MM/yyyy";
    private String toDateValue, fromDateValue;
    Calendar FromCalender = Calendar.getInstance();
    Calendar ToCalender = Calendar.getInstance();

    public static final String ARG_ORDER = "order";
    public static final String ARG_SORT = "sort";
    public static final String ARG_TODATE = "todate";
    public static final String ARG_FROMDATE = "fromdate";
    public static final String ARG_DRAWER_NAME = "drawerName";
    public static final String ARG_SORT_ARRAY = "sortUserArray";

    private String selectedOrderData, selectedSortData;
    private String selectedToDate = "";
    private String selectedFromDate = "";
    private String[] orderUserArray, sortUserArray;


    public OnInfoChangedListener listener;

    public void setListener(OnInfoChangedListener listener) {
        this.listener = listener;
    }

    DatePickerDialog.OnDateSetListener fromDateListener, toDateListener;


    public static FilterDialogFragment newInstance(String[] sortUserArray,
                                                   String order,
                                                   String sort,
                                                   String todate,
                                                   String fromdate) {
        FilterDialogFragment filterDialogFragment = new FilterDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArray(ARG_SORT_ARRAY, sortUserArray);
        bundle.putString(ARG_ORDER, order);
        bundle.putString(ARG_SORT, sort);
        bundle.putString(ARG_TODATE, todate);
        bundle.putString(ARG_FROMDATE, fromdate);
        filterDialogFragment.setArguments(bundle);
        return filterDialogFragment;
    }

    // Interfaces Declarations
    public interface OnInfoChangedListener {
        void onInfoChanged(String order, String sort, String FromDate, String ToDate);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.dialogfragment, container, false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        spinnerOrder = (Spinner) rootView.findViewById(R.id.spinner_order);
        spinnerSort = (Spinner) rootView.findViewById(R.id.spinner_sort);
        btnFromDatePicker = (Button) rootView.findViewById(R.id.btn_from_date_picker);
        btnToDatePicker = (Button) rootView.findViewById(R.id.btn_to_date_picker);
        btnYes = (Button) rootView.findViewById(R.id.btn_yes);
        btnNo = (Button) rootView.findViewById(R.id.btn_no);

        Bundle bundle = getArguments();

        sortUserArray = bundle.getStringArray(ARG_SORT_ARRAY);
        selectedOrderData = bundle.getString(ARG_ORDER);
        selectedSortData = bundle.getString(ARG_SORT);
        selectedFromDate = bundle.getString(ARG_FROMDATE);
        selectedToDate = bundle.getString(ARG_TODATE);

   /*   Log.e(TAG, "Filter Bundle : Order Value" + selectedOrderData);
        Log.e(TAG, "Filter Bundle : SortValue" + selectedSortData);
        Log.e(TAG, "Filter Bundle : FromDate Value" + selectedFromDate);
        Log.e(TAG, "Filter Bundle : ToDate Value" + selectedToDate);

  */
        int orderPosition = getItemPosition(R.array.spinnerUserOrder, selectedOrderData);
        int sortPosition = getItemPosition(R.array.spinnerUserSort, selectedSortData);


       ArrayAdapter<CharSequence> adapterSpinnerOrder = ArrayAdapter.createFromResource(
                getActivity(), R.array.spinnerUserOrder, android.R.layout.simple_spinner_item);
        adapterSpinnerOrder.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOrder.setAdapter(adapterSpinnerOrder);



        ArrayAdapter<CharSequence> adapterSpinnerSort = new ArrayAdapter<CharSequence>(getActivity(),
                android.R.layout.simple_spinner_item, sortUserArray);
        adapterSpinnerSort.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSort.setAdapter(adapterSpinnerSort);


        spinnerSort.setSelection(((ArrayAdapter<String>) spinnerSort.getAdapter())
                .getPosition(selectedSortData));

        spinnerOrder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSortData = sortUserArray[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (orderPosition != -1) {
            spinnerOrder.setSelection(orderPosition);
        }
        if (sortPosition != -1) {
            spinnerSort.setSelection(sortPosition);
        }

        btnFromDatePicker.setText(selectedFromDate);
        btnToDatePicker.setText(selectedToDate);

        fromDateListener = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                FromCalender.set(year, month, dayOfMonth);
                updateFromDatePicker();
            }
        };

        toDateListener = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker ToDatePicker, int year, int month, int dayOfMonth) {
                ToCalender.set(year, month, dayOfMonth);
                updateTODatePicker();
            }
        };

        btnFromDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int year = FromCalender.get(Calendar.YEAR);
                int month = FromCalender.get(Calendar.MONTH);
                int day = FromCalender.get(Calendar.DAY_OF_MONTH);
                new DatePickerDialog(getContext(), fromDateListener, year, month, day).show();

            }
        });

        btnToDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int year = ToCalender.get(Calendar.YEAR);
                int month = ToCalender.get(Calendar.MONTH);
                int day = ToCalender.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(getContext(), toDateListener, year, month, day);
                dialog.getDatePicker().setMinDate(FromCalender.getTimeInMillis());
                dialog.show();

            }
        });
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String order = (String) spinnerOrder.getSelectedItem();
                String sort = (String) spinnerSort.getSelectedItem();
                String fromDate = null;
                String toDate = null;

                if (fromDateValue != null) {
                    fromDate = convertDateFormat(FromCalender.getTimeInMillis(), "yyyy-MM-dd");
                }
                if (toDateValue != null) {
                    toDate = convertDateFormat(ToCalender.getTimeInMillis(), "yyyy-MM-dd");
                }
                listener.onInfoChanged(order, sort, fromDate, toDate);
                dismiss();
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return rootView;
    }


    String convertDateFormat(long timeInMilliSecond, String dateFormat) {
        Date date = new Date(timeInMilliSecond);
        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        return format.format(date);
    }

    private void updateFromDatePicker() {

        SimpleDateFormat sdf = new SimpleDateFormat(myDateFormat, Locale.US);
        fromDateValue = sdf.format(FromCalender.getTime());
        btnFromDatePicker.setText(fromDateValue);
    }

    private void updateTODatePicker() {

        SimpleDateFormat sdf = new SimpleDateFormat(myDateFormat, Locale.US);
        toDateValue = sdf.format(ToCalender.getTime());
        btnToDatePicker.setText(toDateValue);
    }

    private int getItemPosition(int strArray, String value) {

        String[] listArray;
        listArray = getResources().getStringArray(strArray);

        int position = -1;
        for (int i = 0; i < listArray.length; i++) {
            if (listArray[i].equals(value)) {
                position = i;
                break;
            }
        }
        return position;
    }

}
