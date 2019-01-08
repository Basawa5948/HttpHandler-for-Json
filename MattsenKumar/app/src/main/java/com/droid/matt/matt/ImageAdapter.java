package com.droid.matt.matt;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private ArrayList<HashMap<String,String>> imagesList = new ArrayList<>();
    private Context context;

    public ImageAdapter(ArrayList<HashMap<String, String>> imagesList, Context context) {
        this.imagesList = imagesList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_items_second,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        HashMap<String,String> hashMap = new HashMap<>();
        hashMap = imagesList.get(position);
        String url = hashMap.get("LargerImageUrl");
        String user = hashMap.get("USER");

        Picasso.with(context).load(url).fit().centerCrop().into(holder.thumb);
        holder.title.setText(user);
    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView thumb;
        public TextView title;
        public LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            thumb = (ImageView) itemView.findViewById(R.id.thumb);
            title = (TextView) itemView.findViewById(R.id.user);
        }
    }
}
