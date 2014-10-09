package com.roommates.roommates;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.roommates.roommates.R;

public class FragmentShopping extends Fragment {

	private String username;
	private String password;
	private String vivienda;
	
	private View view;
	private LayoutInflater inflater;
	private MainActivity mainActivity;
	private ExpandableListView lista;
    private SwipeRefreshLayout swipeLayout;

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

        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container_shopping);
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
	
//	private void setListData(Object[] values,Context ctx){
//		ArrayAdapterShopping adapter = new ArrayAdapterShopping(
//		ctx, android.R.layout.simple_list_item_1, values);
//		ListView lista = (ListView) mainActivity.findViewById(R.id.listaCompras);
//		lista.setAdapter(adapter);
//	}


    public void actualizarLista(){
        swipeLayout.setRefreshing(true);
        new WebDatabaseBackground().execute("recuperarCompras", mainActivity, username, password,
                vivienda, "actualizarListaExpCompras");
    }
	
	public Object getListItem(int pos){
		return lista.getItemAtPosition(pos);
	}
}