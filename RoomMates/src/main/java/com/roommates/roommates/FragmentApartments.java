package com.roommates.roommates;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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
	
	@Override
	public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		this.inflater = inflater;
		this.view = this.inflater.inflate(R.layout.fragment_apartments, container, false);
		
		mainActivity = (MainActivity) getActivity();
		
		username = mainActivity.username;
		password = mainActivity.password;
		casa = mainActivity.idViviendaActual;
		
		new WebDatabaseBackground().execute("recuperarApartamentos", mainActivity, username, password, 
									casa, "actualizarListaApartamentos");	
		
		return view;
	}
	
	public void refresh(){		
		new WebDatabaseBackground().execute("recuperarApartamentos", mainActivity, username, password, 	 
				"actualizarListaApartamentos");		
	}
}