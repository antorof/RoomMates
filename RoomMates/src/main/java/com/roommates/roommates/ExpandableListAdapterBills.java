package com.roommates.roommates;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.roommates.roommates.R;
 
public class ExpandableListAdapterBills extends BaseExpandableListAdapter {
 
    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;
    private ArrayList<Object> listInterna;
    
    public ExpandableListAdapterBills(Context context, List<String> listDataHeader,
            HashMap<String, List<String>> listChildData, ArrayList<Object> list) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        listInterna = list;
    }
 
    @Override
    public Object getChild(int groupPosition, int childPosititon) {
    	 return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                 .get(childPosititon);
    }
 
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
 
    @Override
    public View getChildView(int groupPosition, final int childPosition,
            boolean isLastChild, View convertView, ViewGroup parent) {
    	final String childText = (String) getChild(groupPosition, childPosition);
    	 
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_subitem, null);
        }
        
        TextView textViewDate = (TextView) convertView.findViewById(R.id.opcionItem);
        
        if(childText == "Done")
        	textViewDate.setText("Mark as done");
        else if(childText == "Delete")
        	textViewDate.setText("Delete the product");
        
        ImageView icono = (ImageView) convertView.findViewById(R.id.listIcon);
        if(childText == "Done")
        	icono.setImageResource(R.drawable.ic_tick_dark);
        else if(childText == "Delete")
        	icono.setImageResource(R.drawable.ic_delete_dark);
		
        return convertView;
    }
 
    @Override
    public int getChildrenCount(int groupPosition) {
    	return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                 .size();
    }
 
    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }
 
    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }
 
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
 
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
            View convertView, ViewGroup parent) {
        
    	if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group_bills, null);
        }

    	TextView textViewName = (TextView) convertView.findViewById(R.id.tvNombreBill);
        TextView textViewFecha = (TextView) convertView.findViewById(R.id.tvFecha);
        TextView textViewParcial = (TextView) convertView.findViewById(R.id.tvParcial);
        TextView textViewTotal = (TextView) convertView.findViewById(R.id.tvTotal);
//        ImageView imageView = (ImageView) rowView.findViewById(R.id.listIcon);
        
        Object[] value = (Object[]) listInterna.get(groupPosition);

        textViewName.setText(value[2].toString());
        textViewFecha.setText(value[4].toString());
        
        if(value[5].toString().contains("\u20AC"))
        	textViewParcial.setText(value[5].toString());
        else
      	  	textViewParcial.setText(value[5].toString()+"\u20AC");
       
        if(value[5].toString().contains("\u20AC"))
      	  	textViewTotal.setText(value[6].toString());
        else
      	  	textViewTotal.setText(value[6].toString()+"\u20AC");
        
         return convertView;
    }
 
    @Override
    public boolean hasStableIds() {
        return false;
    }
 
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}