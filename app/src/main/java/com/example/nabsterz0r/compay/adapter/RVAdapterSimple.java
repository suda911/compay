package com.example.nabsterz0r.compay.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nabsterz0r.compay.BR;
import com.example.nabsterz0r.compay.model.City;

import java.util.List;

public class RVAdapterSimple<P> extends RecyclerView.Adapter<RVAdapterSimple.ViewHolder> {
    private P presenter;
    private int holderLayout;
    private List<City> list;

    public RVAdapterSimple(List<City> cities, int holderLayout, P presenter) {
        this.presenter = presenter;
        this.holderLayout = holderLayout;
        this.list = cities;
    }

    @Override
    public RVAdapterSimple.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(holderLayout, parent, false);
        return new RVAdapterSimple.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RVAdapterSimple.ViewHolder holder, int position) {
        holder.viewDataBinding.setVariable(BR.item, list.get(holder.getAdapterPosition()));
        holder.viewDataBinding.setVariable(BR.presenter, presenter);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ViewDataBinding viewDataBinding;

        private ViewHolder(View itemView) {
            super(itemView);
            viewDataBinding = DataBindingUtil.bind(itemView);
        }
    }
}