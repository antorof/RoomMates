package com.roommates.roommates;

import java.util.HashMap;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
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

//      if(position == selectedItem)
//          rowView = inflater.inflate(R.layout.list_item_navigation_drawer_sel, parent, false);

      TextView textView = (TextView) rowView.findViewById(R.id.firstLine);
      ImageView imageView = (ImageView) rowView.findViewById(R.id.listIcon);
      
      textView.setText(values[position]);

      if(position == selectedItem) {
          textView.setTypeface(null, Typeface.BOLD);
      }
      // Apariencia:
      switch(position){
	      case 0:
	    	  if(position == selectedItem) {
//	    	      textView.setTextColor(Color.parseColor("#FFFFFF"));
//	    	      imageView.setImageResource(R.drawable.ic_home_light);
                  imageView.setImageResource(R.drawable.ic_home_dark);
	          }
	          else {
	        	  imageView.setImageResource(R.drawable.ic_home_dark);
//                  textView.setTextColor(Color.parseColor("#FFFFFF"));
//                  imageView.setImageResource(R.drawable.ic_home_light);
	          }
	    	  break;
	      case 1:
	    	  if(position == selectedItem) {
//	    	      textView.setTextColor(Color.parseColor("#FFFFFF"));
//	    	      imageView.setImageResource(R.drawable.ic_bills_light);
                  imageView.setImageResource(R.drawable.ic_bills_dark);
	          }
	          else {
	        	  imageView.setImageResource(R.drawable.ic_bills_dark);
//                  textView.setTextColor(Color.parseColor("#FFFFFF"));
//                  imageView.setImageResource(R.drawable.ic_bills_light);
	          }
	    	  break;
	      case 2:
	    	  if(position == selectedItem) {
//	    	      textView.setTextColor(Color.parseColor("#FFFFFF"));
//	    	      imageView.setImageResource(R.drawable.ic_shopping_light);
                  imageView.setImageResource(R.drawable.ic_shopping_dark);
	          }
	          else {
	        	  imageView.setImageResource(R.drawable.ic_shopping_dark);
//                  textView.setTextColor(Color.parseColor("#FFFFFF"));
//                  imageView.setImageResource(R.drawable.ic_shopping_light);
	          }
	    	  break;
	      case 3:
	    	  if(position == selectedItem) {
//	    	      textView.setTextColor(Color.parseColor("#FFFFFF"));
//	    	      imageView.setImageResource(R.drawable.ic_tasks_light);
                  imageView.setImageResource(R.drawable.ic_tasks_dark);
	          }
	          else {
	        	  imageView.setImageResource(R.drawable.ic_tasks_dark);
//                  textView.setTextColor(Color.parseColor("#FFFFFF"));
//                  imageView.setImageResource(R.drawable.ic_tasks_light);
	          }
	    	  break;
	      case 4:
	    	  if(position == selectedItem) {
//	    	      textView.setTextColor(Color.parseColor("#FFFFFF"));
//	    	      imageView.setImageResource(R.drawable.ic_roommates_light);
                  imageView.setImageResource(R.drawable.ic_roommates_dark);
	          }
	          else {
	        	  imageView.setImageResource(R.drawable.ic_roommates_dark);
//                  imageView.setImageResource(R.drawable.ic_roommates_light);
//                  textView.setTextColor(Color.parseColor("#FFFFFF"));
	          }
	    	  break;
	      case 5:
	      	  if(position == selectedItem) {
//	      	      textView.setTextColor(Color.parseColor("#FFFFFF"));
//	      	      imageView.setImageResource(R.drawable.ic_rooms_light);
                  imageView.setImageResource(R.drawable.ic_rooms_dark);
	            }
	            else {
	          	  imageView.setImageResource(R.drawable.ic_rooms_dark);
//                  imageView.setImageResource(R.drawable.ic_rooms_light);
//                  textView.setTextColor(Color.parseColor("#FFFFFF"));
	            }
	      	  break;
      }

        imageView.setColorFilter(Color.parseColor("#555555"));
        textView.setTextColor(Color.BLACK);
        if(position == selectedItem) {
//            textView.setTypeface(null, Typeface.BOLD);
//            textView.setTextColor(Color.parseColor("#FF8800"));
//            rowView.findViewById(R.id.listItem1).setBackgroundResource(0);
//            rowView.findViewById(R.id.listItem1).setBackgroundColor(Color.parseColor("#FFB129"));
            rowView.findViewById(R.id.listItem1).setBackgroundResource(R.drawable.lista_item_pressed);
            textView.setTextColor(Color.parseColor("#ffc107"));
            imageView.setColorFilter(Color.parseColor("#ffc107"));
        }
      return rowView;
    }
    

    @Override
    public boolean hasStableIds() {
      return true;
    }
}
