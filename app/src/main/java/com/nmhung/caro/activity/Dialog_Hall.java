package com.nmhung.caro.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nmhung.caro.R;
import com.nmhung.caro.model.UserModel;

import java.util.List;

public class Dialog_Hall extends DialogFragment {


    private List<UserModel> list;
    public Dialog_Hall(List<UserModel> list) {
        this.list = list;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_fragment_hall, container);
        ImageView imageView = v.findViewById(R.id.btnDismiss);
        imageView.setOnClickListener(view -> dismiss());

        RecyclerView rcHall = v.findViewById(R.id.rcHall);
        HallAdapter adapter = new HallAdapter(getContext(),list);
        rcHall.setAdapter(adapter);
        rcHall.setLayoutManager(new LinearLayoutManager(getContext()));
        return v;
    }




}
