package com.roommates.roommates;

import com.roommates.roommates.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class FragmentTasks extends Fragment {

	private String username;
	private String password;
	private String vivienda;
	
	private View view;
//	private LayoutInflater inflater;
	private MainActivity mainActivity;
	private ListView lista;
	
	@Override
	public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//		this.inflater = inflater;
		this.view = inflater.inflate(R.layout.fragment_tasks, container, false);
		
		mainActivity = (MainActivity) getActivity();
		username = mainActivity.username;
		password = mainActivity.password;
		vivienda= mainActivity.idViviendaActual;
		lista = (ListView) view.findViewById(R.id.listaTareas);

        actualizarLista();

		return view;
	}
	
	public void actualizarLista(){
        new WebDatabaseBackground().execute("recuperarTareas", mainActivity, username, password,
                vivienda, "actualizarListaExpTareas");
    }
	
	public Object getListItem(int pos){
		return lista.getItemAtPosition(pos);
	}
	
}