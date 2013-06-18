package com.novel.subscription;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SubscriptionListViewAdapter extends BaseAdapter {  
	
    private List<SubscriptionEntity> items;  
    
    private LayoutInflater inflater;  
      
    public SubscriptionListViewAdapter(Context context, List<SubscriptionEntity> items) {  
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
            view = inflater.inflate(R.layout.subscription_list_item, null);  
        }  
        SubscriptionEntity subscriptionEntity = items.get(position);
        TextView tvName = (TextView) view.findViewById(R.id.list_item_text);
        tvName.setText(subscriptionEntity.getBook().getName());  
        TextView tvAuthor = (TextView) view.findViewById(R.id.list_item_author);
        tvAuthor.setText(subscriptionEntity.getBook().getAuthor());
        TextView tvTitle = (TextView)view.findViewById(R.id.latest_title);
        tvTitle.setText(subscriptionEntity.getLatestTitle());
        TextView tvReadTitle = (TextView)view.findViewById(R.id.read_title);
        tvReadTitle.setText(subscriptionEntity.getReadTitle());
        
        ProgressBar pb = (ProgressBar) view.findViewById(R.id.pbRefreshSub);
        
        if(subscriptionEntity.getIsUpdating())
        	pb.setVisibility(View.VISIBLE);
        else
        	pb.setVisibility(View.GONE);
        
//        if(!subscriptionEntity.getReadUrl().equals(subscriptionEntity.getLatestUrl()))
//        	tvTitle.setBackgroundColor(Color.argb(0x80, 0xFF, 0xCC, 0));
        
        return view;
    }
    
    public void addItem(SubscriptionEntity item) {  
        items.add(item);  
    }  
    
}