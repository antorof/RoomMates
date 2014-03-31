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
	private String username;
	private String password;

	private View view;
	private MainActivity mainActivity;

	String inicial;
	ArrayList<String> listaUsuarios;
	ArrayList<String> listaApartamentos;
	ArrayList<String> listaBills;
	ArrayList<String> listaTasks;
	ArrayList<String> listaShopping;

	@Override
	public View onCreateView(
		LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {	

		view = inflater.inflate(R.layout.fragment_newhome, container, false);
		
		mainActivity = (MainActivity) getActivity();
		username = mainActivity.username;
		password = mainActivity.password;
		

		// Buscamos en las preferencias la ultima vivienda:
	    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mainActivity);
	    String idViviendaActual = sharedPref.getString("id_vivienda", "-1");
        String nombreViviendaActual = sharedPref.getString("nombre_vivienda", "");
        String rolEnViviendaActual = sharedPref.getString("rol_en_vivienda", "-1");

	    mainActivity.idViviendaActual = idViviendaActual;
        mainActivity.nombreViviendaActual = nombreViviendaActual;
        mainActivity.rolEnViviendaActual = rolEnViviendaActual;
	    
	    if (!mainActivity.idViviendaActual.equals("-1")
                && !mainActivity.nombreViviendaActual.equals("")
                && !mainActivity.rolEnViviendaActual.equals("-1") ) {
			// Se llama a las distintas consultas de la base de datos 
			new WebDatabaseBackground().execute("recuperarFacturas", mainActivity, username, password, 
											idViviendaActual, "actualizarHomeFacturas");
	
			new WebDatabaseBackground().execute("recuperarCompras", mainActivity, username, password, 
											idViviendaActual, "actualizarHomeCompras");
	
			new WebDatabaseBackground().execute("recuperarTareas", mainActivity, username, password, 
											idViviendaActual, "actualizarHomeTareas");
	
			setNombreVivienda();
	    }
		setNombreEInicial();
		
		return view;
	}
	
	/** 
	 * Funcion que pone el nombre y la inicial de el usuario en el Home
	 */
	private void setNombreEInicial() {
		String nombre = mainActivity.nombre;
		String apellidos = mainActivity.apellidos;
		String color = mainActivity.color;
		
		TextView tvNombre = (TextView) view.findViewById(R.id.cardHomeCurrentUser);
		tvNombre.setText(nombre+" "+apellidos);

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
		tvNombreVivienda.setText(mainActivity.nombreViviendaActual);
	}
}