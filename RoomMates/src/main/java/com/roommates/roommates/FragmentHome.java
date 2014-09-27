package com.roommates.roommates;

import java.util.ArrayList;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.roommates.roommates.R;

public class FragmentHome extends Fragment {
	private View view;

    String inicial;
	ArrayList<String> listaUsuarios;
	ArrayList<String> listaApartamentos;
	ArrayList<String> listaBills;
	ArrayList<String> listaTasks;
	ArrayList<String> listaShopping;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.fragment_newhome, container, false);
        MainActivity mainActivity = (MainActivity) getActivity();

		// Buscamos en las preferencias la ultima vivienda:
	    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mainActivity);
	    String idViviendaActual = sharedPref.getString("id_vivienda", "-1");
        String nombreViviendaActual = sharedPref.getString("nombre_vivienda", "");
        String rolEnViviendaActual = sharedPref.getString("rol_en_vivienda", "-1");

	    Session.currentApartmentID   = idViviendaActual;
        Session.currentApartmentName = nombreViviendaActual;
        Session.currentRole          = rolEnViviendaActual;
	    
	    if ( !idViviendaActual.equals("-1") &&
             !nombreViviendaActual.equals("") &&
             !rolEnViviendaActual.equals("-1") )
        {
			// Se llama a las distintas consultas de la base de datos 
			new WebDatabaseBackground().execute("recuperarFacturas", mainActivity, Session.email, Session.password,
                    Session.currentApartmentID, "actualizarHomeFacturas");
	
			new WebDatabaseBackground().execute("recuperarCompras", mainActivity, Session.email, Session.password,
                    Session.currentApartmentID, "actualizarHomeCompras");
	
			new WebDatabaseBackground().execute("recuperarTareas", mainActivity, Session.email, Session.password,
                    Session.currentApartmentID, "actualizarHomeTareas");
	
			setNombreVivienda();
	    }
		setNombreEInicial();
		
		return view;
	}
	
	/** 
	 * Funcion que pone el nombre y la inicial de el usuario en el Home
	 */
	private void setNombreEInicial() {
		String nombre = Session.name;
		String color = Session.color;
		
		TextView tvNombre = (TextView) view.findViewById(R.id.cardHomeCurrentUser);
		tvNombre.setText(nombre);

		TextView tvInicial = (TextView) view.findViewById(R.id.homeCardName);
		tvInicial.setText(nombre.substring(0, 1));
		
		LinearLayout llBackgroundInicial = (LinearLayout) view.findViewById(R.id.homeCardNameBackground);
		llBackgroundInicial.setBackgroundColor(Color.parseColor(color));
	}
	
	/** 
	 * Funcion que pone el nombre de la vivienda en el Home
	 */
	private void setNombreVivienda() {		
		TextView tvNombreVivienda = (TextView) view.findViewById(R.id.cardHomeCurrentHome);
		tvNombreVivienda.setText(Session.currentApartmentName);
	}
}