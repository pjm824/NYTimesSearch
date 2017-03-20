package com.pjm284.nytimessearch.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.pjm284.nytimessearch.R;
import com.pjm284.nytimessearch.models.Search;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class SearchFilterFragment extends BottomSheetDialogFragment implements DatePickerDialog.OnDateSetListener {

    public interface SearchFilterDialogListener {
        void onFinishEditDialog(Search search);
    }

    private Spinner sSort;
    private EditText etDate;
    private CheckBox cbNewsArts;
    private CheckBox cbNewsFashion;
    private CheckBox cbNewsSports;
    private Button btnSave;
    private Button btnCancel;

    private Search search;

    public SearchFilterFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static SearchFilterFragment newInstance(String title, Search search) {
        SearchFilterFragment frag = new SearchFilterFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        frag.setSearch(search);
        return frag;
    }

    public void setSearch(Search search) {
        this.search = search;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_filter, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        sSort = (Spinner) view.findViewById(R.id.sSort);
        etDate = (EditText) view.findViewById(R.id.etDate);
        cbNewsArts = (CheckBox) view.findViewById(R.id.cbNewsArts);
        cbNewsFashion = (CheckBox) view.findViewById(R.id.cbNewsFashion);
        cbNewsSports = (CheckBox) view.findViewById(R.id.cbNewsSports);
        btnSave = (Button) view.findViewById(R.id.btnSave);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);

        if (search.getStartDate() != null) {
            String myFormat = "MM/dd/yy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            etDate.setText(sdf.format(search.getStartDate().getTime()));
        }

        if (search.getSortOrder() != null) {
            if (search.getSortOrder().equals("oldest")) {
                sSort.setSelection(0);
            } else {
                sSort.setSelection(1);
            }
        }

        if (search.isNewsArts()) {
            cbNewsArts.setSelected(true);
        }

        if (search.isNewsFashion()) {
            cbNewsFashion.setSelected(true);
        }

        if (search.isNewsSports()) {
            cbNewsSports.setSelected(true);
        }

        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(view);
            }
        });

        btnSave.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                search.setSortOrder(sSort.getSelectedItem().toString());
                search.setNewsArts(cbNewsArts.isChecked());
                search.setNewsFashion(cbNewsFashion.isChecked());
                search.setNewsSports(cbNewsSports.isChecked());

                SearchFilterDialogListener listener = (SearchFilterDialogListener) getActivity();
                listener.onFinishEditDialog(search);
                // Close the dialog and return back to the parent activity
                dismiss();
            }
        });

        btnCancel.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        if (search.getStartDate() == null) {
            search.setStartDate(new GregorianCalendar());
        }

        Calendar cal = search.getStartDate();

        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, monthOfYear);
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        updateLabel();
    }

    public void updateLabel() {

        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        etDate.setText(sdf.format(search.getStartDate().getTime()));
    }

    public void showDatePickerDialog(View v) {
        FragmentManager fm = getFragmentManager();
        DatePickerFragment editNameDialogFragment = new DatePickerFragment();
        // SETS the target fragment for use later when sending results
        editNameDialogFragment.setTargetFragment(SearchFilterFragment.this, 300);
        editNameDialogFragment.show(fm, "datepicker");
    }
}