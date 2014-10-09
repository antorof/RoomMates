package com.roommates.roommates;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.roommates.roommates.R;

public class FragmentApartments extends Fragment {

	private String username;
	private String password;
	private String casa;
	
	private View view;
	private LayoutInflater inflater;
	private MainActivity mainActivity;
    private SwipeRefreshLayout swipeLayout;

    @Override
	public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		this.inflater = inflater;
		this.view = this.inflater.inflate(R.layout.fragment_apartments, container, false);
		
		mainActivity = (MainActivity) getActivity();
		
		username = mainActivity.username;
		password = mainActivity.password;
		casa = mainActivity.idViviendaActual;

        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container_apartments);
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

        actualizarLista();

		return view;
	}
	
	public void actualizarLista(){
        swipeLayout.setRefreshing(true);
		new WebDatabaseBackground().execute("recuperarApartamentos", mainActivity, username, password,
                casa, "actualizarListaApartamentos");
	}
}