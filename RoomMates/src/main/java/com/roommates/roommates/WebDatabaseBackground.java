package com.roommates.roommates;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class WebDatabaseBackground extends AsyncTask<Object, Boolean, Object[] >{
	
	MainActivity callerActivity;
	int numItemsObtenidos;

	private String URL_consultaApartamentos = Constantes.CONSULTA_CASAS_URL;
	private String URL_consultaFacturas = Constantes.CONSULTA_FACTURAS_URL;
	private String URL_consultaCompras = Constantes.CONSULTA_COMPRAS_URL;
	private String URL_consultaTareas = Constantes.CONSULTA_TAREAS_URL;
	private String URL_consultaRoommates = Constantes.CONSULTA_COMPANEROS_URL;

	String postMethod;
	String consulta;
	String username;
	String password;
	String vivienda;
	
	/**
	 * 
	 * Los parametros son:
	 * [0] -> consulta que queremos realizar
	 * [1] -> actividad que ha llamado a la consulta
	 * [2] -> username para realizar la consulta
	 * [3] -> password para realizar la consulta
	 * [4] -> vivienda actual
	 * [5] -> metodo que se llamara cuando acabe la consulta
	 * */	
	@Override
	protected Object[] doInBackground(Object... params) {
		Httppostaux post = new Httppostaux();
		String URL_todo = null;
		
		consulta = (String) params[0];
		callerActivity = (MainActivity) params[1];
		username = Session.email;
		password = Session.password;
		vivienda = Session.currentApartmentID;
		
		postMethod = (String) params[5];
		
		if(consulta.equals("recuperarApartamentos"))
			URL_todo = URL_consultaApartamentos;
		else if(consulta.equals("recuperarRoommates"))
			URL_todo = URL_consultaRoommates;
		else if(consulta.equals("recuperarFacturas"))
			URL_todo = URL_consultaFacturas;
		else if(consulta.equals("recuperarCompras"))
			URL_todo = URL_consultaCompras;
		else if(consulta.equals("recuperarTareas"))
			URL_todo = URL_consultaTareas;

		// Creamos un ArrayList con los parametros de la consulta:
		ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();

		postparameters2send.add(new BasicNameValuePair("Correo", username));
		postparameters2send.add(new BasicNameValuePair("Contrasena", password));
		postparameters2send.add(new BasicNameValuePair("idVivienda",vivienda));

		// Realizamos una peticion y como respuesta obtenemos un array JSON
		JSONArray jdata=post.getServerData(postparameters2send, URL_todo);

		//si lo que obtuvimos no es null
		if (jdata!=null && jdata.length() > 0){
			JSONObject json_data;

			try {
				json_data = jdata.getJSONObject(0);
				JSONArray jarr = json_data.getJSONArray("Datos");

				numItemsObtenidos = jarr.length();
				
				Log.v("numItems-jarr",""+jarr.length());
				Log.v("Items-jarr",""+jarr);
				ArrayList<String> l = new ArrayList<String>();
				for(int i=0; i<jarr.length();i++)
					l.add(jarr.get(i).toString());
				
				Object[] arr = null;
				{
					if(consulta.equals("recuperarApartamentos")) {
						arr = convertToObject(l, 5);
					}
                    else if(consulta.equals("recuperarRoommates")) {
						arr = convertToObject(l, 6);
					}
                    else if(consulta.equals("recuperarFacturas")) {
						arr = convertToObject(l, 9);
					}
                    else if(consulta.equals("recuperarCompras")) {
						arr = convertToObject(l, 7);
					}
                    else if(consulta.equals("recuperarTareas")) {
						arr = convertToObject(l, 7);
					}
				}
				final Object[] values = arr;
				
    			return values;
			} catch (JSONException e) { e.printStackTrace();  }
		}else{	//json obtenido invalido: verificar parte WEB.
			Log.e("JSON", "ERROR");
            if( post.error != Constantes.NO_ERROR ) {
                callerActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(callerActivity, R.string.error_unable_to_connect, Toast.LENGTH_LONG).show();
                    }
                });
            }
			return null;
		}
		
		return null;
	}
	
	@Override
	protected void onPostExecute(Object[] result)
	{
		// Si hemos obtenido algun resultado en la consulta llamamos al método
		// que se ha especificado
		if(result != null)
		{
			Class c = callerActivity.getClass();
			Method m = null;

			//El arreglo de parametros
			Object[] args_value = { result };
			
			//El arreglo con los tipos de datos de los par�metros
			Class[] args_class = { Object.class };	
			
			try {
				m = c.getMethod(postMethod, Object[].class, int.class);
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}

			try {
				 m.invoke(callerActivity, new Object[]{ result, numItemsObtenidos} );
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}
	
	private Object[] convertToObject(ArrayList<String> src, int numItemsPerRow)
	{
		Object[] oValues= src.toArray();
		Object[] nValues = new Object[oValues.length/numItemsPerRow];

		for (int index = 0; index < oValues.length; index+=numItemsPerRow) 
		{
			Object[] item = new Object[numItemsPerRow];
            System.arraycopy(oValues, index, item, 0, numItemsPerRow);
			nValues[index/numItemsPerRow] = item;
		}
		return nValues;
	}
}
