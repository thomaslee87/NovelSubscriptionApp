package com.example.novel;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MoreSourceListViewAdapter extends BaseAdapter {  
	
    private List<ChooseSourceActivity.SourceSiteEntity> items;  
    
    private LayoutInflater inflater;  
      
    public MoreSourceListViewAdapter(Context context, List<ChooseSourceActivity.SourceSiteEntity> items) {  
        this.items = items;  
        
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
    }  
      
    @Override  
    public int getCount() {  
        return items.size();  
    }  
  
    @Override  
    public Object getItem(int position) {  
        return items.get(position);  
    }  
  
    @Override  
    public long getItemId(int position) {  
        return position;  
    }  
  
    @Override  
    public View getView(int position, View view, ViewGroup parent) {  
        if (view == null) {  
            view = inflater.inflate(R.layout.more_source_list_item, null);  
        }  
        ChooseSourceActivity.SourceSiteEntity moreSrcEntity = items.get(position);
        TextView tvName = (TextView) view.findViewById(R.id.list_item_site);
        tvName.setText(moreSrcEntity.site);
        TextView tvTitle = (TextView)view.findViewById(R.id.latest_title);
        tvTitle.setText(moreSrcEntity.update);
        
        return view;
    }
    
    public void addItem(ChooseSourceActivity.SourceSiteEntity item) {  
        items.add(item);  
    }  
    
}