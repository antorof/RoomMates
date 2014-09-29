package com.roommates.roommates;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.roommates.roommates.R;
 
public class ExpandableListAdapterTasks extends BaseExpandableListAdapter {
 
    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;
    private ArrayList<Object> listInterna;
    
    public ExpandableListAdapterTasks(Context context, List<String> listDataHeader,
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
        
        if(childText.equals("Done"))
        	textViewDate.setText(_context.getString(R.string.expandablelist_markdone_text));
        else if(childText.equals("Delete"))
        	textViewDate.setText(_context.getString(R.string.expandablelist_deletetask_text));
        
        ImageView icono = (ImageView) convertView.findViewById(R.id.listIcon);
        if(childText.equals("Done"))
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
            convertView = infalInflater.inflate(R.layout.list_group_tasks, null);
        }

    	 TextView textViewDate = (TextView) convertView.findViewById(R.id.date);
         TextView textViewFirstLine = (TextView) convertView.findViewById(R.id.firstLine);
         TextView textViewSecondLine = (TextView) convertView.findViewById(R.id.secondLine);
//         ImageView imageView = (ImageView) rowView.findViewById(R.id.listIcon);
         
         Object[] value = (Object[]) listInterna.get(groupPosition);
         
         textViewDate.setText(value[5]+" \u21E8 "+value[6]); //" \u2192 "
         textViewFirstLine.setText(value[1].toString());
         textViewSecondLine.setText(
                 parent.getResources().getString(R.string.tasklist_every) + " " +
                 value[5] +
                 " " + parent.getResources().getStringArray(R.array.tipo_intervalo_tarea)[Integer.parseInt(value[6].toString())]
         );
         
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