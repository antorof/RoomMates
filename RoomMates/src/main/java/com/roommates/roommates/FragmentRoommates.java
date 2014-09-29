package com.roommates.roommates;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.roommates.roommates.R;
 
public class FragmentRoommates extends Fragment {
 
    //ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    

	private String username;
	private String password;
	private String casa;
	private View view;
	private LayoutInflater inflater;
	private MainActivity mainActivity;
 
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
    	this.inflater = inflater;
		this.view = this.inflater.inflate(R.layout.fragment_roommates, container, false);
		
		mainActivity = (MainActivity) getActivity();
		username = mainActivity.username;
		password = mainActivity.password;
		casa = mainActivity.idViviendaActual;
//		lista = (ListView) view.findViewById(R.id.listaRoommates);

		actualizarLista();

		return view;
    	
    	
//    	this.inflater = inflater;
//		this.view = this.inflater.inflate(R.layout.fragment_roommates, container, false);
//		mainActivity = (MainActivity) getActivity();
//		
//        // get the listview
//        expListView = (ExpandableListView) view.findViewById(R.id.lvExp);
// 
//        // preparing list data
//        prepareListData();
//    
//        listAdapter = new ExpandableListAdapter(mainActivity, listDataHeader, listDataChild, null);
//
//        // setting list adapter
//        expListView.setAdapter(listAdapter);
//        
//        return view;
    }

    /**
     *  Vuelve a consultar la BD y actualiza la lista
     */
    public void actualizarLista(){
        view.findViewById(R.id.progressBarListaRoommates).setVisibility(View.VISIBLE);
        new WebDatabaseBackground().execute("recuperarRoommates", mainActivity, username, password, casa,
                "actualizarListaRoommates");
    }
}