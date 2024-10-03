package com.pasc.lib.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangwen881 on 17/2/23.
 */

public abstract class EasyBaseAdapter<T> extends BaseAdapter {

    protected List<T> mDataSet;

    protected final Context mContext;

    protected final LayoutInflater mInflater;

    public EasyBaseAdapter(Context context) {
        this(context, null);
    }

    public EasyBaseAdapter(Context context, List<T> dataset) {
        mDataSet = dataset;
        if (mDataSet == null) {
            mDataSet = new ArrayList<T>();
        }
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    /**
     * add adapter dataset item
     */
    public void addItems(T t) {
        mDataSet.add(t);
        notifyDataSetChanged();
    }

    /**
     * add all items
     */
    public void addAll(List<T> items) {
        if (items == null || items.isEmpty()) {
            return;
        }
        mDataSet.addAll(items);
        notifyDataSetChanged();
    }

    public void addAtPosition(int position, List<T> items) {
        mDataSet.addAll(position, items);
        notifyDataSetChanged();
    }

    public void remove(int position) {
        mDataSet.remove(position);
        notifyDataSetChanged();
    }

    public void removeAll(List<T> items) {
        mDataSet.removeAll(items);
        notifyDataSetChanged();
    }

    public void replaceItemAtPosition(int position, T t) {
        if (mDataSet == null || mDataSet.size() < position) {
            return;
        }
        mDataSet.remove(position);
        mDataSet.add(position, t);
        notifyDataSetChanged();
    }

    /**
     * @param datasets
     */
    public void updateDataSet(List<T> datasets) {
        mDataSet = datasets;
        notifyDataSetChanged();
    }

    public void setItem(int position, T t) {
        if (mDataSet == null || mDataSet.size() < position) {
            return;
        }
        mDataSet.set(position, t);
        notifyDataSetChanged();
    }

    public void clearDataSet() {
        mDataSet.clear();
        notifyDataSetChanged();
    }

    public List<T> getDataSet() {
        return mDataSet;
    }

    @Override
    public int getCount() {
        return mDataSet == null ? 0 : mDataSet.size();
    }

    @Override
    public T getItem(int position) {
        return mDataSet.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public abstract View getView(int position, View convertView, ViewGroup parent);
}
