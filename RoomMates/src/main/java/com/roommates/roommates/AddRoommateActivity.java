package com.roommates.roommates;

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

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AddRoommateActivity extends ActionBarActivity {

	private String username;
	private String password;
	private String idVivienda;
	private String URL_connect = Constantes.NUEVO_COMPANERO;
	private Httppostaux post;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_roommate);
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
        	username = "error";
        	password = "error";
        	idVivienda = "";
        }
        
        post = new Httppostaux();
	}

	/**
	 * Set up the ActionBar.
	 */
	private void setupActionBar() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_product, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
            setResult(ActionBarActivity.RESULT_CANCELED);
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Funcion llamada cuando pulsamos el boton de aniadir tarea.
	 * @param v <tt>View</tt> que genera el evento
	 */
	public void add_roommate_button_onClick(View v) {
		EditText etCorreoUsuario = (EditText) findViewById(R.id.editTextCorreoUsuario);
		if(etCorreoUsuario.getText().toString().length() == 0){
			etCorreoUsuario.setError(getResources().getString(R.string.error_field_required));
		}
		else {
			new asyncConsult().execute("");
			etCorreoUsuario.setError(null);
		}
		
	} 


    /**
     * Clase que se encarga de la peticion asincrona para aniadir una tarea
     * 
     */
	class asyncConsult extends AsyncTask< String, String, Integer > {
		private ProgressDialog pDialog;
        private int EXITO = 1;
        private int NO_EXISTE_USUARIO = 0;
        private int ERROR = -1;
        private int ERROR_CAMPOS = -2;
    	
    	protected void onPreExecute() {
	    	//para el progress dialog
	    	pDialog = new ProgressDialog(AddRoommateActivity.this);
	    	pDialog.setMessage("Adding roommate....");
	    	pDialog.setIndeterminate(false);
	    	pDialog.setCancelable(false);
	    	pDialog.show();
    	}

    	protected Integer doInBackground(String... params) {
            return hacerCosas(username);
    	}
    	
    	protected void onPostExecute(Integer result) {
    		pDialog.dismiss();//ocultamos progess dialog.
    		Log.v("[asyncConsult] onPostExecute=",""+result);

    		if (result == EXITO){
    			Toast.makeText(getApplicationContext(), "Roommate added", Toast.LENGTH_LONG).show();
                setResult(ActionBarActivity.RESULT_OK);
    			finish();
    		} else if (result == NO_EXISTE_USUARIO){
                Toast.makeText(getApplicationContext(), "Error: the user given doesn't exist", Toast.LENGTH_LONG).show();
            } else{
                Toast.makeText(getApplicationContext(), "Error: roommate not added", Toast.LENGTH_LONG).show();
            }

    	}

    	public int hacerCosas(String username) {
    		String estado = "-1";
    		EditText etCorreoUsuario = (EditText) findViewById(R.id.editTextCorreoUsuario);

    		if(etCorreoUsuario.getText().toString().length() == 0 ) {
				Log.e("error datos", "campos no rellenados");
    			return ERROR_CAMPOS;
    		}
    	    		
    		ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();
    		
    		postparameters2send.add(new BasicNameValuePair("Correo",AddRoommateActivity.this.username));
    		postparameters2send.add(new BasicNameValuePair("Contrasena",AddRoommateActivity.this.password));
    		postparameters2send.add(new BasicNameValuePair("correoNuevoUsuario",etCorreoUsuario.getText().toString()));
    		postparameters2send.add(new BasicNameValuePair("idVivienda",idVivienda));

    		//realizamos una peticion y como respuesta obtenes un array JSON
    		JSONArray jdata=post.getServerData(postparameters2send, URL_connect);

    		//si lo que obtuvimos no es null
    		if (jdata!=null && jdata.length() > 0){
    			JSONObject json_data; //creamos un objeto JSON
    			
    			try {
    				json_data = jdata.getJSONObject(jdata.length()-1); //leemos el primer segmento en nuestro caso el unico
    				estado=json_data.getString("usuarioAniadido");//accedemos al valor
    				Log.i("usuarioAniadido","usuarioAniadido= "+estado);//muestro por log que obtuvimos
    			} catch (JSONException e) {
    				estado = "ERROR"; e.printStackTrace(); }

    			//validamos el valor obtenido
    			if (estado.equals("EXITO")){
    				Log.i("usuarioAniadido ", "EXITO");
    				return EXITO;
    			}
                else if(estado.equals("NO_EXISTE_USUARIO")){
                    Log.e("usuarioAniadido ", "NO_EXISTE_USUARIO");
                    return NO_EXISTE_USUARIO;
                }
    			else{
                    Log.e("usuarioAniadido ", "ERROR");
    				return ERROR;
    			}

    		}else{	//json obtenido invalido verificar parte WEB.
    			Log.e("JSON  ", "ERROR");
    			return ERROR;
    		}
    	}
    }

}
