package com.candymobi.notewidget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.candymobi.notewidget.bean.WidgetBean;
import com.candymobi.notewidget.utils.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SplashActivity extends AppCompatActivity {

    private MyAdapter mMyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setAdapter(mMyAdapter = new MyAdapter());
        loadData();

        if (!NewAppWidget.hasWidget(this)) {
            NewAppWidget.requestPlace(this);
        }
    }

    private void loadData() {
        AppWidgetManager appWidgetManager = (AppWidgetManager) getSystemService(Context.APPWIDGET_SERVICE);
        NoteManager noteManager = NoteManager.getInstance();
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, NewAppWidget.class));
        List<WidgetBean> list = new ArrayList<>();
        WidgetBean bean;
        for (int appWidgetId : appWidgetIds) {
            String noteContent = noteManager.getNoteContent(appWidgetId);
            String bgPath = noteManager.getBgPath(appWidgetId);
            bean = new WidgetBean();
            bean.setContent(noteContent);
            bean.setWidgetBg(bgPath);
            bean.setWidgetId(appWidgetId);
            list.add(bean);
        }
        mMyAdapter.submitList(list);
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivBg;
        TextView tvContent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivBg = itemView.findViewById(R.id.iv_bg);
            tvContent = itemView.findViewById(R.id.appwidget_text);
        }
    }

    static class MyAdapter extends ListAdapter<WidgetBean, ViewHolder> {
        protected MyAdapter() {
            super(new DiffUtil.ItemCallback<WidgetBean>() {
                @Override
                public boolean areItemsTheSame(@NonNull WidgetBean oldItem, @NonNull WidgetBean newItem) {
                    return oldItem == newItem;
                }

                @Override
                public boolean areContentsTheSame(@NonNull WidgetBean oldItem, @NonNull WidgetBean newItem) {
                    return Objects.equals(oldItem, newItem);
                }
            });
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.new_app_widget, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            WidgetBean item = getItem(position);
            holder.tvContent.setText(item.getContent());
            Glide.with(holder.ivBg).load(item.getWidgetBg())
                    .transform(new RoundedCorners(Util.dp2px(holder.itemView.getContext(), 20)))
                    .into(holder.ivBg);
        }
    }
}