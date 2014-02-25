package com.roommates.roommates;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.roommates.roommates.R;

public class FragmentShopping extends Fragment {

	private String username;
	private String password;
	private String vivienda;
//	private String URL_connect = "http://"+"pruebasout.hol.es"+"/consultar_cosas.php";
	private String URL_connect = "http://"+"roommate.hol.es"+"/consultar_shopping_android.php";
	private Httppostaux post;
	
	private View view;
	private LayoutInflater inflater;
	private MainActivity mainActivity;
	private ExpandableListView lista;
	
	@Override
	public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		this.inflater = inflater;
		this.view = inflater.inflate(R.layout.fragment_shopping, container, false);

		mainActivity = (MainActivity) getActivity();
		username = mainActivity.username;
		password = mainActivity.password;
		vivienda = mainActivity.idViviendaActual;
		
		// Es necesario para que funcione el click. Si lo quitamos falla el método
		// getListItem(int).
		// TODO: revisar todo esto a ver si se puede hacer algo o dejarlo así.
		lista = (ExpandableListView) view.findViewById(R.id.listaCompras);

		new WebDatabaseBackground().execute("recuperarCompras", mainActivity, username, password,  
									vivienda, "actualizarListaExpCompras");	
		
		return view;
	}
	
//	private void setListData(Object[] values,Context ctx){
//		ArrayAdapterShopping adapter = new ArrayAdapterShopping(
//		ctx, android.R.layout.simple_list_item_1, values);
//		ListView lista = (ListView) mainActivity.findViewById(R.id.listaCompras);
//		lista.setAdapter(adapter);
//	}
	
	public Object getListItem(int pos){
		return lista.getItemAtPosition(pos);
	}
}