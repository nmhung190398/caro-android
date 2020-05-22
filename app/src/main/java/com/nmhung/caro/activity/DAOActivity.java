package com.nmhung.caro.activity;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.nmhung.caro.R;
import com.nmhung.caro.adapter.ListUserAdapter;
import com.nmhung.caro.dao.UserDAO;
import com.nmhung.caro.model.UserModel;

import java.util.ArrayList;
import java.util.List;

public class DAOActivity extends AppCompatActivity {

    ListView listViewUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dao);
        listViewUser = findViewById(R.id.list_user);
        List<UserModel> users = new ArrayList<>();
        for (int i = 0; i < 10; ++i) {
            UserModel userModel = new UserModel();
            userModel.setUsername("Hung Dz" + i);
            userModel.setPassword("password");
            users.add(userModel);
        }

        UserDAO userDAO = new UserDAO(this);
        userDAO.open();
        findViewById(R.id.loadUser).setOnClickListener(v -> {
            userDAO.fetch();
        });
        findViewById(R.id.saveUser).setOnClickListener(v -> {
            UserModel userModel = new UserModel();
            TextView password = findViewById(R.id.password);
            TextView username = findViewById(R.id.username);
            userModel.setPassword(password.getText().toString());
            userModel.setUsername(username.getText().toString());
            userDAO.insert(userModel);
        });

        ListUserAdapter listUserAdapter = new ListUserAdapter(this, users);


        listViewUser.setAdapter(listUserAdapter);
        listUserAdapter.notifyDataSetChanged();


    }

}
