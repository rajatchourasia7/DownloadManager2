package com.example.rajat_pc.downloadmanager2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by RAJAT-PC on 28-03-2017.
 */
public class CustomAdapter extends BaseAdapter{
    private ArrayList<DownloadTask> _data = new ArrayList<>();
    Context context;
    public CustomAdapter(ArrayList<DownloadTask> downloads, Context c){
        _data = downloads;
        context = c;
    }
    public int getCount(){
        if(_data == null)
        {
            return 0;
        }
        return _data.size();
    }

    public long getItemId(int position){
        return position;
    }

    public Object getItem(int position){
        return _data.get(position);
    }

    public View getView(int Position, View ConvertView, ViewGroup Parent){
        DownloadTask dt = (DownloadTask) getItem(Position);
        View v = ConvertView;
        if(v == null){
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(R.layout.list_item,null);
        }
        TextView text1 = (TextView) v.findViewById(R.id.txt1);
        TextView text2 = (TextView) v.findViewById(R.id.txt2);
        //new DownloadTask(dt).execute();
        text1.setText(dt.getFileName());
        text2.setText(dt.getTextView().getText());
        return v;
    }
}
