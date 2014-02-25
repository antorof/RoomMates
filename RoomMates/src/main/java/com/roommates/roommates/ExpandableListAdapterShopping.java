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
 
public class ExpandableListAdapterShopping extends BaseExpandableListAdapter {
 
    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;
    private ArrayList<Object> listInterna;
    
    public ExpandableListAdapterShopping(Context context, List<String> listDataHeader,
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
        
        if(childText.equals("Buy"))
        	textViewDate.setText(_context.getString(R.string.expandablelist_markpurchased_text));
        else if(childText.equals("Urgent"))
        	textViewDate.setText(_context.getString(R.string.expandablelist_markurgent_text));
        else if(childText.equals("Delete"))
        	textViewDate.setText(_context.getString(R.string.expandablelist_deleteproduct_text));
        
        ImageView icono = (ImageView) convertView.findViewById(R.id.listIcon);
        if(childText.equals("Buy"))
        	icono.setImageResource(R.drawable.ic_tick_dark);
        else if(childText.equals("Urgent"))
        	icono.setImageResource(R.drawable.ic_urgent_dark);
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
            convertView = infalInflater.inflate(R.layout.list_group_shopping, null);
        }

        TextView textViewDate = (TextView) convertView.findViewById(R.id.date);
    	TextView textViewFirstLine = (TextView) convertView.findViewById(R.id.firstLine);
    	TextView textViewSecondLine = (TextView) convertView.findViewById(R.id.secondLine);
    	TextView textViewInicialNombre = (TextView) convertView.findViewById(R.id.tvInicial);
    	LinearLayout llUrgent = (LinearLayout)convertView.findViewById(R.id.urgent);
    	LinearLayout llinicialBack = (LinearLayout)convertView.findViewById(R.id.inicial_background);

    	// value: { $i, Compras.Nombre,Fecha,Usuarios.Nombre,ID_Vivienda,Urgente,Color }
    	Object[] value = (Object[]) listInterna.get(groupPosition);
      
    	textViewDate.setText(value[2].toString());
    	textViewFirstLine.setText(value[1].toString());
      	textViewSecondLine.setText("");
      	
      	if( value[5].toString().equals("1") )
      	{
      		llUrgent.setBackgroundColor(Color.parseColor("#FF7373"));
      	}
      	else {
      		llUrgent.setBackgroundColor(Color.TRANSPARENT);
      	}

    	// Cambio la inicial y el color:
    	textViewInicialNombre.setText(value[3].toString().substring(0, 1));
    	llinicialBack.setBackgroundColor( Color.parseColor(value[6].toString()) );
        
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