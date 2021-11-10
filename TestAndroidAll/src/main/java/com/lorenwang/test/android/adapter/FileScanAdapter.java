package com.lorenwang.test.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lorenwang.test.android.R;

import java.io.File;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 功能作用：文件扫描适配器
 * 初始注释时间： 2020/9/27 11:03 上午
 * 创建人：王亮（Loren）
 * 思路：
 * 方法：
 * 注意：
 * 修改人：
 * 修改时间：
 * 备注：
 *
 * @author 王亮（Loren）
 */
public class FileScanAdapter extends RecyclerView.Adapter {
    private final Context context;
    private List<File> list;
    private CheckListener listener;

    public FileScanAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_scan, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {
        ViewHolder vh = (ViewHolder) holder;
        vh.tvFileName.setText(list.get(i).getName());
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public void setData(List<File> data) {
        if (data != null) {
            this.list = data;
            notifyDataSetChanged();
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvFileName;

        ViewHolder(View view) {
            super(view);
            tvFileName = view.findViewById(R.id.tv_file_name);
        }
    }

    public interface CheckListener {
        void getState(File file);
    }

    public void setOnCheckListener(CheckListener listener) {
        this.listener = listener;
    }

}
