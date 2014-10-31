package com.roommates.roommates;

import java.util.ArrayList;
import java.util.Calendar;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class NewBillActivity extends ActionBarActivity {
	private String URL_connect = Constantes.NUEVA_FACTURA_URL;
	private Httppostaux post;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_bill);

		setupActionBar();
        
        post = new Httppostaux();
        
	}

	/**
	 * Set up the ActionBar.
	 */
	private void setupActionBar() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
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
		getMenuInflater().inflate(R.menu.new_bill, menu);
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
	 * Funcion llamada cuando pulsamos el boton de aniadir factura.
	 * @param v <tt>View</tt> que genera el evento
	 */
	public void add_bill_button_onClick(View v) {
		new asyncCreateBill().execute("");
	} 


    /**
     * Clase que se encarga de la peticion asincrona para aniadir una tarea
     * 
     */
	class asyncCreateBill extends AsyncTask< String, String, String > {
		private ProgressDialog pDialog;
    	
    	protected void onPreExecute() {
	    	//para el progress dialog
	    	pDialog = new ProgressDialog(NewBillActivity.this);
	    	pDialog.setMessage("Adding Bill....");
	    	pDialog.setIndeterminate(false);
	    	pDialog.setCancelable(false);
	    	pDialog.show();
    	}

    	protected String doInBackground(String... params) {
    		//enviamos y recibimos y analizamos los datos en segundo plano.
    		if (hacerCosas()){
    			return "ok"; // tarea aniadida
    		} else{    		
    			return "err"; // tarea no aniadida   	          	  
    		}
    	}
    	
    	protected void onPostExecute(String result) {
    		pDialog.dismiss();//ocultamos progess dialog.
    		Log.v("[asyncCreateBill] onPostExecute=",""+result);

    		if (result.equals("ok")){
    			Toast.makeText(getApplicationContext(), "Bill added", Toast.LENGTH_LONG).show();
                setResult(ActionBarActivity.RESULT_OK);
    			finish();
                overridePendingTransition(R.anim.right_in, R.anim.right_out);
    		} else{
    			Toast.makeText(getApplicationContext(), "Error: bill not added", Toast.LENGTH_LONG).show();
    		}

    	}

    	public boolean hacerCosas() {
    		int estado = -1;
    		EditText etNombre = (EditText) findViewById(R.id.editTextNombreFactura);
    		EditText etDescripcion = (EditText) findViewById(R.id.editTextDescripcion);
    		EditText etTotal = (EditText) findViewById(R.id.editTextTotal);

    		if( etNombre.getText().toString().length() == 0   ||
    				etTotal.getText().toString().length() == 0 ) {
				Log.e("error datos", "campos no rellenados");
    			return false;	
    		}
    		
    		ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();
    		
    	    Calendar cal = Calendar.getInstance();
    	    int year = cal.get(Calendar.YEAR);
    	    int month = cal.get(Calendar.MONTH) + 1;
    	    int day = cal.get(Calendar.DAY_OF_MONTH);

    		postparameters2send.add(new BasicNameValuePair("Correo",Session.email));
    		postparameters2send.add(new BasicNameValuePair("Contrasena",Session.password));
            postparameters2send.add(new BasicNameValuePair("idVivienda",Session.currentApartmentID));
    		postparameters2send.add(new BasicNameValuePair("nombreFactura",etNombre.getText().toString()));
    		postparameters2send.add(new BasicNameValuePair("Descripcion",etDescripcion.getText().toString()));
    		postparameters2send.add(new BasicNameValuePair("Total",etTotal.getText().toString()));
    		postparameters2send.add(new BasicNameValuePair("Fecha",year+"-"+month+"-"+day));

    		//realizamos una peticion y como respuesta obtenes un array JSON
    		JSONArray jdata=post.getServerData(postparameters2send, URL_connect);

    		//si lo que obtuvimos no es null
    		if (jdata!=null && jdata.length() > 0){
    			JSONObject json_data; //creamos un objeto JSON
    			
    			try {
    				json_data = jdata.getJSONObject(jdata.length()-1); //leemos el primer segmento en nuestro caso el unico
    				estado=json_data.getInt("facturaAniadida");//accedemos al valor 
    				Log.i("facturaAniadida","facturaAniadida= "+estado);//muestro por log que obtuvimos
    			} catch (JSONException e) {
    				estado = 0;//accedemos al valor 
    				e.printStackTrace(); }		            

    			//validamos el valor obtenido
    			if (estado==0){
    				Log.e("facturaAniadida ", "invalido");
    				return false;
    			}
    			else{
    				Log.i("facturaAniadida ", "valido");
    				return true;
    			}

    		}else{	//json obtenido invalido verificar parte WEB.
    			Log.e("JSON  ", "ERROR");
    			return false;
    		}
    	}
    }
}
