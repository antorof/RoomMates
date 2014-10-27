package com.roommates.roommates;

import com.roommates.roommates.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
    private SwipeRefreshLayout swipeLayout;

    @Override
	public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//		this.inflater = inflater;
		this.view = inflater.inflate(R.layout.fragment_tasks, container, false);
		
		mainActivity = (MainActivity) getActivity();
		username = mainActivity.username;
		password = mainActivity.password;
		vivienda= mainActivity.idViviendaActual;
		lista = (ListView) view.findViewById(R.id.listaTareas);

        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container_tasks);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                actualizarLista();
            }
        });
        swipeLayout.setColorSchemeResources(R.color.naranja_android,R.color.azul_claro_android);

        new Thread(new Runnable() {
            public void run() {
                mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        actualizarLista();
                    }
                });
            }
        }).start();

		return view;
	}
	
	public void actualizarLista(){
        swipeLayout.setRefreshing(true);
        new WebDatabaseBackground().execute("recuperarTareas", mainActivity, username, password,
                vivienda, "actualizarListaExpTareas");
    }
	
	public Object getListItem(int pos){
		return lista.getItemAtPosition(pos);
	}
	
}