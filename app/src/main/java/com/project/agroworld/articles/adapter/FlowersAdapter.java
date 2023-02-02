package com.project.agroworld.articles.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.agroworld.articles.listener.FlowerClickListener;
import com.project.agroworld.articles.listener.FruitsClickListener;
import com.project.agroworld.articles.model.FlowersResponse;
import com.project.agroworld.articles.viewholder.ArticleListViewHolder;
import com.project.agroworld.databinding.ArticleItemLayoutBinding;

import java.util.ArrayList;

public class FlowersAdapter extends RecyclerView.Adapter<ArticleListViewHolder> {
    private ArrayList<FlowersResponse> flowersResponseArrayList;
    private FlowerClickListener listener;

    public FlowersAdapter(ArrayList<FlowersResponse> flowersResponseArrayList, FlowerClickListener listener) {
        this.flowersResponseArrayList = flowersResponseArrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ArticleListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ArticleListViewHolder(ArticleItemLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleListViewHolder holder, int position) {
        FlowersResponse response = flowersResponseArrayList.get(position);
        holder.bindFlowersData(response, listener);
    }

    @Override
    public int getItemCount() {
        return flowersResponseArrayList.size();
    }
}