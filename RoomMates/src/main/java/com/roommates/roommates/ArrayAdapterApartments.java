package com.roommates.roommates;

import com.roommates.roommates.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ArrayAdapterApartments extends ArrayAdapter<Object> {
    private final Context context;
    private final Object[] values;
    int selectedItem = 0;

    public ArrayAdapterApartments(Context context, String[] values) {
      super(context, android.R.layout.simple_list_item_1, values);
      this.context = context;
      this.values = values;
    }
    
    public ArrayAdapterApartments(Context context, int textViewResourceId, Object[] objects) {
        super(context, textViewResourceId,objects);
        this.context = context;
        this.values = objects;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	View rowView = inflater.inflate(R.layout.list_item_apartments, parent, false);
    	
    	TextView textViewName = (TextView) rowView.findViewById(R.id.tvNombreApartment);
    	TextView textViewPlus = (TextView) rowView.findViewById(R.id.tvPlus);
    	
    	// value: { $i, ID_Vivienda, Nombre, Direccion, Permisos.Tipo }
    	Object[] value = (Object[]) values[position];
    	
    	// Pongo el nombre a la vivienda:
    	textViewName.setText(value[2].toString());
    	
    	// Quito el + si el roommate no es roommate+
    	if(value[4].toString().equals("0"))
    		textViewPlus.setText("");
    	
    	return rowView;
    }
    

    @Override
    public boolean hasStableIds() {
      return true;
    }
}
