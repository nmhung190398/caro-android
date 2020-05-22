package com.nmhung.caro.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nmhung.caro.R;
import com.nmhung.caro.model.UserModel;

import java.util.List;

public class ListUserAdapter extends BaseAdapter {
    private Context context;
    private List<UserModel> userModels;


    public ListUserAdapter(Context context, List<UserModel> userModels) {
        this.userModels = userModels;
        this.context = context;
    }

    @Override
    public int getCount() {
        return userModels.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return userModels.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Log.i("HDZ", "getView: " + position);
        ViewHolder holder = null;
        View viewProduct;
        if (convertView == null) {
            viewProduct = convertView.inflate(parent.getContext(), R.layout.list_item, null);
            holder = new ViewHolder(viewProduct, userModels.get(position));
            viewProduct.setTag(holder);
        } else {
            viewProduct = convertView;
        }
        return viewProduct;
    }


    static class ViewHolder {
        TextView username;
        TextView password;

        public ViewHolder(View convertView, UserModel userModel) {
            username = convertView.findViewById(R.id.username_1);
            password = convertView.findViewById(R.id.password_1);
            username.setText(userModel.getUsername());
            password.setText(userModel.getPassword());
        }
    }
}
