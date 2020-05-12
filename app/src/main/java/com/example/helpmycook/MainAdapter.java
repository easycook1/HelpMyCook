package com.example.helpmycook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> implements View.OnClickListener {

    ArrayList<MainModel> mainModels;
    Context context;

    public MainAdapter(Context context, ArrayList<MainModel> mainModels){
        this.context = context;
        this.mainModels = mainModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainAdapter.ViewHolder holder, int position) {
        holder.imageButton.setBackground(mainModels.get(position).getLanglogo());
        holder.textView.setText(mainModels.get(position).getLangname());
        holder.imageButton.setTag(mainModels.get(position).getLangname());
    }

    @Override
    public int getItemCount() {
        return mainModels.size();
    }

    @Override
    public void onClick(View v) {
        ImageButton category;
        category = (ImageButton) v;

        String nameCategory  = (String) category.getTag();

        switch (nameCategory){
            case "Vegetales":
                nameCategory = "Vegetales";
            break;
            case "Proteinas":
                nameCategory = "Proteínas";
            break;
            case "Lacteos":
                nameCategory = "Lácteos";
            break;
            case "Cereales":
                nameCategory = "Cereales";
            break;
            default:
                throw new IllegalStateException("Categoria Invalida: " + nameCategory);
        }

        Toast.makeText(context,nameCategory,Toast.LENGTH_SHORT).show();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageButton imageButton;
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageButton = itemView.findViewById(R.id.image_button);
            textView = itemView.findViewById(R.id.text_view);

            imageButton.setOnClickListener(MainAdapter.this);
        }
    }
}
