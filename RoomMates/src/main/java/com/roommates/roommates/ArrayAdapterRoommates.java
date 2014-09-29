package com.roommates.roommates;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.roommates.roommates.R;

public class ArrayAdapterRoommates extends ArrayAdapter<Object> {
    private final Context context;
    private final Object[] values;
    int selectedItem = 0;

    public ArrayAdapterRoommates(Context context, String[] values) {
      super(context, android.R.layout.simple_list_item_1, values);
      this.context = context;
      this.values = values;
    }
    
    public ArrayAdapterRoommates(Context context, int textViewResourceId, Object[] objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
        this.values = objects;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	View rowView = inflater.inflate(R.layout.list_item_roommates, parent, false);

    	TextView textViewName = (TextView) rowView.findViewById(R.id.tvNombreRoommate);
    	TextView textViewInicialNombre = (TextView) rowView.findViewById(R.id.tvInicial);
    	TextView textViewPlus = (TextView) rowView.findViewById(R.id.tvPlus);
  
    	// value: { $i, Nombre, Apellidos, Correo, Tipo, Color }
    	Object[] value = (Object[]) values[position];
    	
    	// Cambio el nombre:
    	//                   nombre                  apellidos
    	textViewName.setText(value[1].toString()+" "+value[2].toString());
    	
    	// Cambio la inicial y el color:
    	textViewInicialNombre.setText(value[1].toString().substring(0, 1));
        textViewInicialNombre.setTextColor(Utilities.getContrastYIQ(value[5].toString())>=128?Color.BLACK:Color.WHITE);
    	textViewInicialNombre.setBackgroundColor( Color.parseColor(value[5].toString()));
    	
    	// Quito el + si el roommate no e roommate+
        textViewPlus.setTextColor(Utilities.getContrastYIQ(value[5].toString())>=128?Color.BLACK:Color.WHITE);
    	if(value[4].toString().equals("0"))
    		textViewPlus.setText("");
    	
    	return rowView;
    }
    

    @Override
    public boolean hasStableIds() {
      return true;
    }
}
