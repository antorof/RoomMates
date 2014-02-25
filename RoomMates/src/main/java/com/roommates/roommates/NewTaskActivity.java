package com.roommates.roommates;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.roommates.roommates.R;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class NewTaskActivity extends ActionBarActivity {

	private String username;
	private String password;
	private String idVivienda;
//    String IP_Server="pruebasout.hol.es";//IP DE NUESTRO PC
//    String URL_connect="http://"+IP_Server+"/addtarea.php";
	private String URL_connect = "http://"+"roommate.hol.es"+"/aniadir_task_android.php";
	private Httppostaux post;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_task);
		// Show the Up button in the action bar.
		setupActionBar();
		
		Bundle extras = getIntent().getExtras();
        //Obtenemos datos enviados en el intent.
        if (extras != null) {
        	username = extras.getString("USERNAME");
        	password = extras.getString("PASSWORD");
        	idVivienda = extras.getString("ID_VIVIENDA");
        }
        else {
        	username   = "error";
        	password   = "error";
        	idVivienda = "error";
        }
        
        post = new Httppostaux();
        
	}

	/**
	 * Set up the ActionBar
	 */
	private void setupActionBar() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_task, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Funcion llamada cuando pulsamos el boton de aniadir tarea.
	 * @param v <tt>View</tt> que genera el evento
	 */
	public void add_task_button_onClick(View v) {
		new asyncConsult().execute("");
	} 


    /**
     * Clase que se encarga de la peticion asincrona para aniadir una tarea
     * 
     */
	class asyncConsult extends AsyncTask< String, String, String > {
		private ProgressDialog pDialog;
    	
    	protected void onPreExecute() {
	    	//para el progress dialog
	    	pDialog = new ProgressDialog(NewTaskActivity.this);
	    	pDialog.setMessage("Adding task....");
	    	pDialog.setIndeterminate(false);
	    	pDialog.setCancelable(false);
	    	pDialog.show();
    	}

    	protected String doInBackground(String... params) {
    		//enviamos y recibimos y analizamos los datos en segundo plano.
    		if (hacerCosas(username)){
    			return "ok"; // tarea aniadida
    		} else{    		
    			return "err"; // tarea no aniadida   	          	  
    		}
    	}
    	
    	protected void onPostExecute(String result) {
    		pDialog.dismiss();//ocultamos progess dialog.
    		Log.v("[asyncConsult] onPostExecute=",""+result);

    		if (result.equals("ok")){
    			Toast.makeText(getApplicationContext(), "Task added", Toast.LENGTH_LONG).show();
    			finish();
    		} else{
    			Toast.makeText(getApplicationContext(), "Error: task not added", Toast.LENGTH_LONG).show();
    		}

    	}

    	public boolean hacerCosas(String username) {
    		int estado = -1;
    		EditText etNombre = (EditText) findViewById(R.id.editTextNombreTarea);
//    		EditText etDescripcion = (EditText) findViewById(R.id.editTextDescripcion);
    		EditText etFrom = (EditText) findViewById(R.id.editTextFechaInicio);
    		EditText etTo = (EditText) findViewById(R.id.editTextFechaFin);

    		if( etNombre.getText().toString().length() == 0   ||
    				etFrom.getText().toString().length() == 0 ||
    				etTo.getText().toString().length() == 0 ) {
				Log.e("error datos", "campos no rellenados");
    			return false;	
    		}
    		
    		ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();

//    		postparameters2send.add(new BasicNameValuePair("usuario",username)); 
//    		postparameters2send.add(new BasicNameValuePair("nombretarea",etNombre.getText().toString())); 
//    		postparameters2send.add(new BasicNameValuePair("descripcion",etDescripcion.getText().toString()));

    		postparameters2send.add(new BasicNameValuePair("Correo",NewTaskActivity.this.username));
    		postparameters2send.add(new BasicNameValuePair("Contrasena",NewTaskActivity.this.password));
    		postparameters2send.add(new BasicNameValuePair("nombreTarea",etNombre.getText().toString()));
    		postparameters2send.add(new BasicNameValuePair("idVivienda",idVivienda));
    		postparameters2send.add(new BasicNameValuePair("Repeticion","3"));
    		postparameters2send.add(new BasicNameValuePair("Inicio",etFrom.getText().toString()));
    		postparameters2send.add(new BasicNameValuePair("Fin",etTo.getText().toString()));

    		//realizamos una peticion y como respuesta obtenes un array JSON
    		JSONArray jdata=post.getserverdata(postparameters2send, URL_connect);

    		//si lo que obtuvimos no es null
    		if (jdata!=null && jdata.length() > 0){
    			JSONObject json_data; //creamos un objeto JSON
    			
    			try {
    				json_data = jdata.getJSONObject(jdata.length()-1); //leemos el primer segmento en nuestro caso el unico
    				estado=json_data.getInt("tareaAniadida");//accedemos al valor 
    				Log.i("tareaAniadida","tareaAniadida= "+estado);//muestro por log que obtuvimos
    			} catch (JSONException e) { e.printStackTrace(); }		            

    			//validamos el valor obtenido
    			if (estado==0){
    				Log.e("tareaAniadida ", "invalido");
    				return false;
    			}
    			else{
    				Log.i("tareaAniadida ", "valido");
    				return true;
    			}

    		}else{	//json obtenido invalido verificar parte WEB.
    			Log.e("JSON  ", "ERROR");
    			return false;
    		}
    	}
    }
}
