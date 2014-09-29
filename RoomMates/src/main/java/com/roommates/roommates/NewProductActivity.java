package com.roommates.roommates;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

public class NewProductActivity extends ActionBarActivity {

	private String username;
	private String password;
	private String idVivienda;
	private String URL_connect = Constantes.NUEVO_PODUCTO_URL;
	private Httppostaux post;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_product);
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
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
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
            overridePendingTransition(R.anim.right_in, R.anim.right_out);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Funcion llamada cuando pulsamos el boton de aniadir tarea.
	 * @param v <tt>View</tt> que genera el evento
	 */
	public void add_product_button_onClick(View v) {
		EditText etNombre = (EditText) findViewById(R.id.editTextNombreProducto);
		if(etNombre.getText().toString().length() == 0){
			etNombre.setError(getResources().getString(R.string.error_field_required));
		}
		else {
			new asyncConsult().execute("");
			etNombre.setError(null);
		}
		
	} 


    /**
     * Clase que se encarga de la peticion asincrona para aniadir una tarea
     * 
     */
	class asyncConsult extends AsyncTask< String, String, String > {
		private ProgressDialog pDialog;
    	
    	protected void onPreExecute() {
	    	//para el progress dialog
	    	pDialog = new ProgressDialog(NewProductActivity.this);
	    	pDialog.setMessage("Adding product....");
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
    			Toast.makeText(getApplicationContext(), "Product added", Toast.LENGTH_LONG).show();
                setResult(ActionBarActivity.RESULT_OK);
    			finish();
                overridePendingTransition(R.anim.right_in, R.anim.right_out);
    		} else{
    			Toast.makeText(getApplicationContext(), "Error: product not added", Toast.LENGTH_LONG).show();
    		}

    	}

    	public boolean hacerCosas(String username) {
    		int estado = -1;
    		EditText etNombre = (EditText) findViewById(R.id.editTextNombreProducto);

    		if(etNombre.getText().toString().length() == 0 ) {
				Log.e("error datos", "campos no rellenados");
    			return false;	
    		}
    		
    	    Calendar cal = new GregorianCalendar();
    	    int year = cal.get(Calendar.YEAR);
    	    int month = cal.get(Calendar.MONTH) + 1;
    	    int day = cal.get(Calendar.DAY_OF_MONTH);
    	    		
    		ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();
    		
    		postparameters2send.add(new BasicNameValuePair("Correo",NewProductActivity.this.username));
    		postparameters2send.add(new BasicNameValuePair("Contrasena",NewProductActivity.this.password));
    		postparameters2send.add(new BasicNameValuePair("nombreProducto",etNombre.getText().toString()));
    		postparameters2send.add(new BasicNameValuePair("idVivienda",idVivienda));
    		postparameters2send.add(new BasicNameValuePair("Fecha",year+"-"+month+"-"+day));
    		postparameters2send.add(new BasicNameValuePair("Urgente","0")); //0->false, 1->true

    		//realizamos una peticion y como respuesta obtenes un array JSON
    		JSONArray jdata=post.getServerData(postparameters2send, URL_connect);

    		//si lo que obtuvimos no es null
    		if (jdata!=null && jdata.length() > 0){
    			JSONObject json_data; //creamos un objeto JSON
    			
    			try {
    				json_data = jdata.getJSONObject(jdata.length()-1); //leemos el primer segmento en nuestro caso el unico
    				estado=json_data.getInt("productoAniadido");//accedemos al valor 
    				Log.i("productoAniadido","productoAniadido= "+estado);//muestro por log que obtuvimos
    			} catch (JSONException e) { 
    				estado =0; e.printStackTrace(); }		            

    			//validamos el valor obtenido
    			if (estado==0){
    				Log.e("productoAniadido ", "invalido");
    				return false;
    			}
    			else{
    				Log.i("productoAniadido ", "valido");
    				return true;
    			}

    		}else{	//json obtenido invalido verificar parte WEB.
    			Log.e("JSON  ", "ERROR");
    			return false;
    		}
    	}
    }

}
