package com.roommates.roommates;

import java.util.ArrayList;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
    private SwipeRefreshLayout swipeLayout;
    private MainActivity mainActivity;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.fragment_newhome, container, false);
        mainActivity = (MainActivity) getActivity();

		// Buscamos en las preferencias la ultima vivienda:
	    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mainActivity);
	    String idViviendaActual = sharedPref.getString("id_vivienda", "-1");
        String nombreViviendaActual = sharedPref.getString("nombre_vivienda", "");
        String rolEnViviendaActual = sharedPref.getString("rol_en_vivienda", "-1");

        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container_home);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                actualizarLista();
            }
        });
        swipeLayout.setColorScheme(R.color.naranja_android,
                R.color.gris_muy_claro,
                R.color.naranja_claro_android,
                R.color.gris_muy_claro);

	    if ( !idViviendaActual.equals("-1") &&
             !nombreViviendaActual.equals("") &&
             !rolEnViviendaActual.equals("-1") )
        {

            Session.currentApartmentID   = idViviendaActual;
            Session.currentApartmentName = nombreViviendaActual;
            Session.currentRole          = rolEnViviendaActual;
            actualizarLista();

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
        tvInicial.setTextColor(Utilities.getContrastYIQ(color)>=128?Color.BLACK:Color.WHITE);
		
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

    private void actualizarLista() {
        swipeLayout.setRefreshing(true);
        mainActivity.actualizandoHomeFacturas = true;
        mainActivity.actualizandoHomeCompras = true;
        mainActivity.actualizandoHomeTareas = true;

        // Se llama a las distintas consultas de la base de datos
        new WebDatabaseBackground().execute("recuperarFacturas", mainActivity, Session.email, Session.password,
                Session.currentApartmentID, "actualizarHomeFacturas");

        new WebDatabaseBackground().execute("recuperarCompras", mainActivity, Session.email, Session.password,
                Session.currentApartmentID, "actualizarHomeCompras");

        new WebDatabaseBackground().execute("recuperarTareas", mainActivity, Session.email, Session.password,
                Session.currentApartmentID, "actualizarHomeTareas");

        setNombreVivienda();

        new Thread(new Runnable() {
            public void run() {
                boolean parar = false;
                while (!parar) {
                    if (!mainActivity.actualizandoHomeTareas &&
                            !mainActivity.actualizandoHomeFacturas &&
                            !mainActivity.actualizandoHomeCompras)
                        parar = true;
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException e) {e.printStackTrace();}
                }
                swipeLayout.setRefreshing(false);
            }
        }).start();
    }
}