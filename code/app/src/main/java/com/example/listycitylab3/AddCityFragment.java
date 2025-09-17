package com.example.listycitylab3;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.io.Serializable;


public class AddCityFragment extends DialogFragment {
    interface AddCityDialogListener {
        void addCity(City city);

        void editCity(City city);
    }
    private AddCityDialogListener listener;

    public AddCityFragment() {
        // empty constructor
    }

    static AddCityFragment newInstance(City city) {
        Bundle args = new Bundle();
        args.putSerializable("city", (Serializable) city);
        AddCityFragment fragment = new AddCityFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof AddCityDialogListener) {
            listener = (AddCityDialogListener) context;
        } else {
            throw new RuntimeException(context + " must implement AddCityDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        // Access the Bundle using getArguments() and retrieve the City object.
        Bundle args = getArguments();
        City cityToEdit = null;
        boolean isEditing = false;

        if (args != null) {
            // Retrieve the City object from Bundle using the key.
            cityToEdit = (City) args.getSerializable("city");
            isEditing = (cityToEdit != null);
        }

        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_add_city, null);
        EditText editCityName = view.findViewById(R.id.edit_text_city_text);
        EditText editProvinceName = view.findViewById(R.id.edit_text_province_text);

        if (isEditing) {
            editCityName.setText(cityToEdit.getName());
            editProvinceName.setText(cityToEdit.getProvince());
        }

        // Set appropriate title based on whether we're adding or editing
        String title = isEditing ? "Edit City" : "Add a city";
        String positiveButtonText = isEditing ? "Update" : "Add";

        // Store final reference for lambda
        final City finalCityToEdit = cityToEdit;
        final boolean finalIsEditing = isEditing;

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        return builder.setView(view).setTitle("Add a city").setNegativeButton("Cancel", null).setPositiveButton("Add", (dialog, which) -> {
            String cityName = editCityName.getText().toString();
            String provinceName = editProvinceName.getText().toString();

            if (finalIsEditing) {
                // Update existing city using the setters we added
                finalCityToEdit.setName(cityName);
                finalCityToEdit.setProvince(provinceName);
                listener.editCity(finalCityToEdit);
            } else {
                // Create new city
                listener.addCity(new City(cityName, provinceName));
            }
        }).create();
    }
}
