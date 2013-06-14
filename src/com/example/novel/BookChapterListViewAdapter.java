package com.example.novel;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BookChapterListViewAdapter extends BaseAdapter {  
	
    private List<BookChapterEntity> items;  
    private LayoutInflater inflater;  
      
    public BookChapterListViewAdapter(Context context, List<BookChapterEntity> items) {  
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
            view = inflater.inflate(R.layout.chapter_list_item, null);  
        }  
        TextView tvChapter = (TextView) view.findViewById(R.id.liChapter);
        tvChapter.setText(items.get(position).getChapterTitle());
//        if(items.get(position).getIsRead() == 0)
//        	view.setBackgroundColor(Color.argb(0x80, 0xFF, 0xCC, 0));
        return view;
    }
    
    public void addItem(BookChapterEntity item) {  
        items.add(item);  
    }  
    
}