package com.roommates.roommates;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
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

        if(childText.equals("Pay"))
            textViewDate.setText(_context.getString(R.string.expandablelist_markpaid_text));
        if(childText.equals("Cancel"))
            textViewDate.setText(_context.getString(R.string.expandablelist_cancelpayment_text));//expandablelist_markpaid_text
        else if(childText.equals("Delete"))
        	textViewDate.setText(_context.getString(R.string.expandablelist_deletebill_text));
        
        ImageView icono = (ImageView) convertView.findViewById(R.id.listIcon);
        if(childText.equals("Pay") || childText.equals("Cancel"))
        	icono.setImageResource(R.drawable.ic_tick_dark);
        else if(childText.equals("Delete"))
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
        LinearLayout llPaid = (LinearLayout)convertView.findViewById(R.id.color_paid);
//        ImageView imageView = (ImageView) rowView.findViewById(R.id.listIcon);
        
        Object[] value = (Object[]) listInterna.get(groupPosition);

        textViewName.setText(value[2].toString());
        textViewFecha.setText(value[4].toString());

        NumberFormat format = NumberFormat.getCurrencyInstance();
//        if(value[5].toString().contains("\u20AC"))
//        	textViewParcial.setText(value[5].toString());
//        else
            textViewParcial.setText(format.format(Double.parseDouble(value[5].toString())));

//        if(value[5].toString().contains("\u20AC"))
//      	  	textViewTotal.setText(value[6].toString());
//        else
            textViewTotal.setText(format.format(Double.parseDouble(value[6].toString())));
//            textViewTotal.setText(value[6].toString() + "\u20AC");

        if(value[8].toString().equals("0"))
            llPaid.setBackgroundColor(_context.getResources().getColor(R.color.rojo_claro_android));
        else
            llPaid.setBackgroundColor(Color.TRANSPARENT);

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