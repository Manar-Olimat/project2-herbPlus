package com.example.project_2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_2.Models.plantTypeGalleryModel;

import java.util.List;

public class plantTypeGalleryAdapter extends RecyclerView.Adapter<plantTypeGalleryAdapter.plantTypeGalleryViewHolder> {
        Context context;
        List<plantTypeGalleryModel> modelList;

public plantTypeGalleryAdapter(Context context, List<plantTypeGalleryModel> modelList) {
        this.context = context;
        this.modelList = modelList;
        }

@NonNull
@Override
public plantTypeGalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.plant_gallery_type, parent, false);

        return new plantTypeGalleryAdapter.plantTypeGalleryViewHolder(view );     }

@Override
public void onBindViewHolder(@NonNull plantTypeGalleryViewHolder holder, int position) {
        holder.name1.setText(modelList.get(position).getName1());
        holder.name2.setText(modelList.get(position).getName2());
        holder.imgURL1.setBackgroundResource(modelList.get(position).getImgURL1());
        holder.imgURL2.setBackgroundResource(modelList.get(position).getImgURL2());

        }

@Override
public int getItemCount() {
        return modelList.size();
        }


public class plantTypeGalleryViewHolder extends RecyclerView.ViewHolder{
    TextView name1,name2;
    ImageView imgURL1,imgURL2;
    public plantTypeGalleryViewHolder(@NonNull View itemView) {
        super(itemView);
        name1=itemView.findViewById(R.id.text1);
        name2=itemView.findViewById(R.id.text2);
        imgURL1=itemView.findViewById(R.id.image1);
        imgURL2=itemView.findViewById(R.id.image2);
    }
}
}
