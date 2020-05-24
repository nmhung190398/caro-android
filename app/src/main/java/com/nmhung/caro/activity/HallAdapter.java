package com.nmhung.caro.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nmhung.caro.R;
import com.nmhung.caro.model.UserModel;

import java.util.List;

public class HallAdapter extends RecyclerView.Adapter<HallAdapter.ViewHolder> {
    private Context mContext;
    private List<UserModel> mHeros;

    public HallAdapter(Context mContext, List<UserModel> mHeros) {
        this.mContext = mContext;
        this.mHeros = mHeros;
    }

    public void notifyDataSetChanged(List<UserModel> mHeros){
        this.mHeros = mHeros;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View heroView = inflater.inflate(R.layout.item_user, parent, false);
        ViewHolder viewHolder = new ViewHolder(heroView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserModel hero = mHeros.get(position);
        holder.mTextName.setText(hero.getUsername());
        holder.mHall.setText(hero.getTotalWin() + "");
    }

    @Override
    public int getItemCount() {
        return mHeros == null ? 0 : mHeros.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageHero;
        private TextView mTextName, mHall;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageHero = itemView.findViewById(R.id.imgUser);
            mTextName = itemView.findViewById(R.id.txtName);
            mHall = itemView.findViewById(R.id.txtHall);
        }
    }
}
