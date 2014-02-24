package com.roommates.roommates;

import java.util.HashMap;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.roommates.roommates.R;

public class ArrayAdapterNavigationDrawer extends ArrayAdapter<String> {
    HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();
    private final Context context;
    private final String[] values;
    int selectedItem = 0;
    
    public ArrayAdapterNavigationDrawer(Context context, int textViewResourceId, String[] objects) {
        super(context, textViewResourceId, objects);
        for (int i = 0; i < objects.length; ++i) {
          mIdMap.put(objects[i], i);
        }
        this.context = context;
        this.values = objects;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      View rowView = inflater.inflate(R.layout.list_item_navigation_drawer, parent, false);
      
      TextView textView = (TextView) rowView.findViewById(R.id.firstLine);
      TextView textView2 = (TextView) rowView.findViewById(R.id.secondLine);
      ImageView imageView = (ImageView) rowView.findViewById(R.id.listIcon);
      
      textView.setText(values[position]);
//      textView2.setText("Descripcion de "+values[position]);
      textView2.setText(""); // No necesitamos descripcion por ahora
      
      // Apariencia:
      switch(position){
	      case 0:
	    	  if(position == selectedItem) {
	        	  ((View) rowView.findViewById(R.id.listItem1)).setBackgroundResource(R.drawable.lista_item_selected);
	    	      textView.setTextColor(Color.parseColor("#FFFFFF"));
	    	      textView2.setTextColor(Color.parseColor("#FFFEEE"));
	    	      imageView.setImageResource(R.drawable.ic_home_light);
	          }
	          else {
	        	  imageView.setImageResource(R.drawable.ic_home_dark);
	          }
	    	  break;
	      case 1:
	    	  if(position == selectedItem) {
	        	  ((View) rowView.findViewById(R.id.listItem1)).setBackgroundResource(R.drawable.lista_item_selected);
	    	      textView.setTextColor(Color.parseColor("#FFFFFF"));
	    	      textView2.setTextColor(Color.parseColor("#FFFEEE"));
	    	      imageView.setImageResource(R.drawable.ic_bills_light);
	          }
	          else {
	        	  imageView.setImageResource(R.drawable.ic_bills_dark);
	          }
	    	  break;
	      case 2:
	    	  if(position == selectedItem) {
	        	  ((View) rowView.findViewById(R.id.listItem1)).setBackgroundResource(R.drawable.lista_item_selected);
	    	      textView.setTextColor(Color.parseColor("#FFFFFF"));
	    	      textView2.setTextColor(Color.parseColor("#FFFEEE"));
	    	      imageView.setImageResource(R.drawable.ic_shopping_light);
	          }
	          else {
	        	  imageView.setImageResource(R.drawable.ic_shopping_dark);
	          }
	    	  break;
	      case 3:
	    	  if(position == selectedItem) {
	        	  ((View) rowView.findViewById(R.id.listItem1)).setBackgroundResource(R.drawable.lista_item_selected);
	    	      textView.setTextColor(Color.parseColor("#FFFFFF"));
	    	      textView2.setTextColor(Color.parseColor("#FFFEEE"));
	    	      imageView.setImageResource(R.drawable.ic_tasks_light);
	          }
	          else {
	        	  imageView.setImageResource(R.drawable.ic_tasks_dark);
	          }
	    	  break;
	      case 4:
	    	  if(position == selectedItem) {
	        	  ((View) rowView.findViewById(R.id.listItem1)).setBackgroundResource(R.drawable.lista_item_selected);
	    	      textView.setTextColor(Color.parseColor("#FFFFFF"));
	    	      textView2.setTextColor(Color.parseColor("#FFFEEE"));
	    	      imageView.setImageResource(R.drawable.ic_roommates_light);
	          }
	          else {
	        	  imageView.setImageResource(R.drawable.ic_roommates_dark);
	          }
	    	  break;
	      case 5:
	      	  if(position == selectedItem) {
	          	  ((View) rowView.findViewById(R.id.listItem1)).setBackgroundResource(R.drawable.lista_item_selected);
	      	      textView.setTextColor(Color.parseColor("#FFFFFF"));
	      	      textView2.setTextColor(Color.parseColor("#FFFEEE"));
	      	      imageView.setImageResource(R.drawable.ic_rooms_light);
	            }
	            else {
	          	  imageView.setImageResource(R.drawable.ic_rooms_dark);
	            }
	      	  break;
      }
      return rowView;
    }
    

    @Override
    public boolean hasStableIds() {
      return true;
    }
}
