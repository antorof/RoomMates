package com.roommates.roommates;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import yuku.ambilwarna.AmbilWarnaDialog;
import yuku.ambilwarna.AmbilWarnaDialog.OnAmbilWarnaListener;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.roommates.roommates.R;

public class RegisterActivity extends ActionBarActivity {

	private String URL_connect = Constantes.REGISTER_URL;
	private String correo;
	private String nombre;
	private String apellidos;
	private String contrasena;
	private String color_usuario;
	
	private Button btnColor;
	
	private Httppostaux post;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		// Show the Up button in the action bar.
		setupActionBar();
		
		color_usuario = "#000000";
		btnColor = (Button) findViewById(R.id.btColor);

		// Setup the register button
		findViewById(R.id.btRegister).setOnClickListener(
			new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					if(comprobarCampos())
						new asyncSignUp().execute("");
				}
			});
		
		// Setup the Color button
		btnColor.setOnClickListener(
			new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					mostrarColorPicker();
				}
			});
	}
	
	private void mostrarColorPicker() {
		AmbilWarnaDialog dialog = new AmbilWarnaDialog(this, Color.parseColor(color_usuario),
	            new OnAmbilWarnaListener() {
	        @Override
	        public void onOk(AmbilWarnaDialog dialog, int color) {
	                // color is the color selected by the user.
	        	RegisterActivity.this.color_usuario = String.format("#%06X", (0xFFFFFF & color));
	    		//Toast.makeText(RegisterActivity.this, "Color: "+RegisterActivity.this.color_usuario,Toast.LENGTH_LONG).show();
	    		btnColor.setBackgroundColor(color);
	        }
	        @Override
	        public void onCancel(AmbilWarnaDialog dialog) {
	                // cancel was selected by the user
	        }
	    });
	    dialog.show();
	}
	
	private boolean comprobarCampos(){
		EditText etCorreo = (EditText) findViewById(R.id.etEmail);
		EditText etNombre = (EditText) findViewById(R.id.etFirstname);
		EditText etApellidos = (EditText) findViewById(R.id.etLastname);
		EditText etContrasena = (EditText) findViewById(R.id.etPassword);

		correo = etCorreo.getText().toString();
		nombre = etNombre.getText().toString();
		apellidos = etApellidos.getText().toString();
		contrasena = etContrasena.getText().toString();
		
		boolean valido = true;
		
		// Comprobamos los campos
		if (TextUtils.isEmpty(correo)) {
			etCorreo.setError(getString(R.string.error_field_required));
			valido = false;
		}
		if (TextUtils.isEmpty(nombre)) {
			etNombre.setError(getString(R.string.error_field_required));
			valido = false;
		} 
		if (TextUtils.isEmpty(apellidos)) {
			etApellidos.setError(getString(R.string.error_field_required));
			valido = false;
		} 
		if (contrasena.length() < 3) {
			etContrasena.setError(getString(R.string.error_invalid_password));
			valido = false;
		}
	    		
		return valido;
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
		getMenuInflater().inflate(R.menu.register, menu);
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
     * Clase que se encarga de la peticion asincrona para el registro
     * 
     */
	class asyncSignUp extends AsyncTask< String, String, String > {
		private ProgressDialog pDialog;
    	
    	protected void onPreExecute() {
	    	//para el progress dialog
	    	pDialog = new ProgressDialog(RegisterActivity.this);
	    	pDialog.setMessage("Signing Up...");
	    	pDialog.setIndeterminate(false);
	    	pDialog.setCancelable(false);
	    	pDialog.show();
	    	
	    	post = new Httppostaux();
    	}

    	protected String doInBackground(String... params) {
    		//enviamos y recibimos y analizamos los datos en segundo plano.
    		int res = registrar();
    		
    		if (res==1){    		    		
    			return "ok"; // Usuario aniadido
    		} else if (res==0){    		
    			return "ko"; // Usuario no aniadido  	          	  
    		}
    		else if (res==-1)
    			return "err";
    		else
    			return "other_error";
    	}
    	
    	protected void onPostExecute(String result) {
    		pDialog.dismiss();//ocultamos progess dialog.
    		Log.v("[asyncConsult] onPostExecute=",""+result);

    		if (result.equals("ok")){
    			Toast.makeText(getApplicationContext(), "New user registered", Toast.LENGTH_LONG).show();
    			finish();
    		} else if (result.equals("ko")){
    			Toast.makeText(getApplicationContext(), "Error: Email already in use", Toast.LENGTH_LONG).show();
    		} else if (result.equals("err")){
				Toast.makeText(getApplicationContext(), getString(R.string.error_unable_to_connect), Toast.LENGTH_LONG).show();
			}

    	}

    	public int registrar() {
    		int estado = -2;
    		ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();

    		postparameters2send.add(new BasicNameValuePair("Correo",correo));
    		postparameters2send.add(new BasicNameValuePair("Nombre",nombre));
    		postparameters2send.add(new BasicNameValuePair("Apellidos",apellidos));
    		postparameters2send.add(new BasicNameValuePair("Contrasena",contrasena));
    		postparameters2send.add(new BasicNameValuePair("Color",color_usuario));

    		//realizamos una peticion y como respuesta obtenes un array JSON
    		JSONArray jdata=post.getserverdata(postparameters2send, URL_connect);

    		//si lo que obtuvimos no es null
    		if (jdata!=null && jdata.length() > 0){
    			JSONObject json_data; //creamos un objeto JSON
    			
    			try {
    				json_data = jdata.getJSONObject(0); //leemos el primer segmento en nuestro caso el unico
    				estado=json_data.getInt("SignUp");//accedemos al valor 
    				Log.i("SignUp","SignUp = "+estado);//muestro por log que obtuvimos
    			} catch (JSONException e) { 
    				estado =0; e.printStackTrace(); }		            

    			//validamos el valor obtenido
    			if (estado==0){
    				Log.e("SignUp ", "no valido");
    				return 0;
    			}
    			else{
    				Log.i("SignUp ", "valido");
    				return 1;
    			}

    		}else{	//json obtenido invalido verificar parte WEB.
    			Log.e("JSON  ", "ERROR");
    			return -1;
    		}
    	}
    }

}
