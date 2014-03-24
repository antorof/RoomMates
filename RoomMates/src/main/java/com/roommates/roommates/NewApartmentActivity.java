package com.roommates.roommates;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.roommates.roommates.R;

public class NewApartmentActivity extends ActionBarActivity implements OnClickListener{

	private Button botonAceptar;
	private EditText nombre ;
	private EditText direccion ;
	private EditText pass ;
	private EditText rPass ;
	private ImageView imagenAyuda ;

    private String URL_HOST = "roommates-antorofdev.rhcloud.com";
    String URL_connect="http://"+URL_HOST+"/aniadir_apartment_android.php";
    
	private Httppostaux post;
	private String correo;
	private String contrasenia;
	
	private String codigo = "n_code";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_apartment);
		// Show the Up button in the action bar.
		setupActionBar();
		
		imagenAyuda = (ImageView) findViewById (R.id.ayuda);
		imagenAyuda.setOnClickListener(this);
		
		nombre = (EditText) findViewById (R.id.edit_nombre);
		direccion = (EditText) findViewById (R.id.edit_direccion);
		pass = (EditText) findViewById (R.id.edit_pass);
		rPass = (EditText) findViewById (R.id.edit_rpass);
		
		botonAceptar = (Button) findViewById (R.id.boton_hecho);
		botonAceptar.setOnClickListener(this);
		

		Bundle extras = getIntent().getExtras();
        //Obtenemos datos enviados en el intent.
        if (extras != null) {
        	correo  = extras.getString("USERNAME");
        	contrasenia = extras.getString("PASSWORD");
        }
        else {
        	correo  = "error";
        	contrasenia = "error";
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
		getMenuInflater().inflate(R.menu.new_apartment, menu);
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

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.boton_hecho : {
			
			// Comprobamos que todo est� rellenado
			if( nombre.getText().toString().equals("") ) {
				Toast.makeText(this, "Apartment Name missing!", Toast.LENGTH_SHORT).show();
				return;
			} else if( direccion.getText().toString().equals("") ) {
				Toast.makeText(this, "Adress missing!", Toast.LENGTH_SHORT).show();
				return;
			} else if( pass.getText().toString().equals("") ) {
				Toast.makeText(this, "Set the password!", Toast.LENGTH_SHORT).show();
				return;
			} else if( rPass.getText().toString().equals("") ) {
				Toast.makeText(this, "Repeat the password!", Toast.LENGTH_SHORT).show();
				return;
			}
			
			// Comprobamos que la contrase�a sea mayor que 3 y menor que 10 (longitud)
			if( pass.getText().toString().length() < 3 || pass.getText().toString().length()>10) {
				if( pass.getText().toString().length() < 3 ) {
					Toast.makeText(this, "Password too short", Toast.LENGTH_SHORT).show();
					return;
				} else {
					Toast.makeText(this, "Password too long", Toast.LENGTH_SHORT).show();
					return;
				}
			}
			
			// Comprobamos que las contrase�as sean iguales
			if( ! pass.getText().toString().equals( rPass.getText().toString() ) ) {
				Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
				return;
			}
		
			
			// Generaci�n de c�digo: tama�o 10, letras minus mayus y n�s
			codigo = generarCodigo();
			
			// Mensaje de confirmaci�n
			AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
			dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
//					nombre.setText("");
//					direccion.setText("");
//					pass.setText("");
//					rPass.setText("");
					new asyncConsult().execute("");
				}
			});
			dialogBuilder.setNeutralButton("Copy to clipboard", new DialogInterface.OnClickListener() {
				@SuppressLint("NewApi")
				@SuppressWarnings("deprecation")
				public void onClick(DialogInterface dialog, int which) {
					try {
			            int sdk = android.os.Build.VERSION.SDK_INT;
			            if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
			                android.text.ClipboardManager clipboard = (android.text.ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
			                clipboard.setText(codigo);
			            } else {
			                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
			                ClipData clip = ClipData.newPlainText("label", codigo);
			                clipboard.setPrimaryClip(clip);
			            }
			            
			        } catch (Exception e) { e.printStackTrace(); }
					new asyncConsult().execute("");
				}
			});
			dialogBuilder.setMessage("Generated code:\n"+ codigo);
			dialogBuilder.setCancelable(true).setTitle("Registro OK");
			dialogBuilder.setIcon(R.drawable.ic_action_accept);
			dialogBuilder.create().show();

		} break;
		
		
		case R.id.ayuda :{
			//se prepara la alerta creando nueva instancia
			AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
			dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// do nothing
				}
			});
			dialogBuilder.setMessage("Minimum length: 3\nMaximum length: 10");
			dialogBuilder.setCancelable(true).setTitle("Password Help");
			dialogBuilder.setIcon(R.drawable.ic_action_help);
			dialogBuilder.create().show();			
		} break;
	}
		
	}

	/**
	 * Funci�n que genera un c�digo de registro de vivienda.
	 * 
	 */
	private String generarCodigo()
	{
		char [] resultado = new char[10];
		
		for(int i=0; i<10; i++)
		{
			// 0->minuscula 1->mayus 2->numero
			int s = (int) (Math.random()* (2-0+1)+0 ); 
			
			int simbolo ;
			char letra ;
			System.out.println("S: "+s);
			if(s == 0)
			{
				simbolo = (int) (Math.random() * (25-1+1)+1 );
				letra = (char) (simbolo + 97);
				System.out.println("Minuscula "+i+": "+letra);
			}else if(s == 1)
			{
				simbolo = (int) (Math.random() * (25-1+1)+1 );
				letra = (char) (simbolo + 65);
				System.out.println("Mayuscula "+i+": "+letra);
			}else
			{
				simbolo = (int) (Math.random() * (9-1+1)+1 );
				letra = (char) (simbolo + 48);
				
			}
			
			resultado[i] = letra ;
		}
		
		return String.valueOf(resultado);
	} 


    /**
     * Clase que se encarga de la peticion asincrona para aniadir una tarea
     * 
     */
	class asyncConsult extends AsyncTask< String, String, String > {
		private ProgressDialog pDialog;
    	
    	protected void onPreExecute() {
	    	//para el progress dialog
	    	pDialog = new ProgressDialog(NewApartmentActivity.this);
	    	pDialog.setMessage("Adding apartment....");
	    	pDialog.setIndeterminate(false);
	    	pDialog.setCancelable(false);
	    	pDialog.show();
    	}

    	protected String doInBackground(String... params) {
    		//enviamos y recibimos y analizamos los datos en segundo plano.
    		if (hacerCosas(correo)){
    			return "ok"; // tarea aniadida
    		} else{    		
    			return "err"; // tarea no aniadida   	          	  
    		}
    	}
    	
    	protected void onPostExecute(String result) {
    		pDialog.dismiss();//ocultamos progess dialog.
    		Log.v("[asyncConsult] onPostExecute=",""+result);

    		if (result.equals("ok")){
    			Toast.makeText(getApplicationContext(), "Apartment added", Toast.LENGTH_LONG).show();
    			finish();
    		} else{
    			Toast.makeText(getApplicationContext(), "Error: Apartment not added", Toast.LENGTH_LONG).show();
    		}
			finish();

    	}

    	public boolean hacerCosas(String username) {
    		int estado = -1;

    		String nombreVivienda = nombre.getText().toString();
    		String direccionVivienda = direccion.getText().toString();
    		String contrasenaVivienda = pass.getText().toString();
    		String codigoVivienda = codigo;
    		
    		ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();

    		postparameters2send.add(new BasicNameValuePair("Correo",correo)); 
    		postparameters2send.add(new BasicNameValuePair("Contrasena",contrasenia)); 
    		postparameters2send.add(new BasicNameValuePair("NombreVivienda",nombreVivienda));
    		postparameters2send.add(new BasicNameValuePair("DireccionVivienda",direccionVivienda));
    		postparameters2send.add(new BasicNameValuePair("ContrasenaVivienda",contrasenaVivienda));
    		postparameters2send.add(new BasicNameValuePair("CodigoVivienda",codigoVivienda));

    		//realizamos una peticion y como respuesta obtenes un array JSON
    		JSONArray jdata=post.getserverdata(postparameters2send, URL_connect);

    		//si lo que obtuvimos no es null
    		if (jdata!=null && jdata.length() > 0){
    			JSONObject json_data; //creamos un objeto JSON
    			
    			try {
    				json_data = jdata.getJSONObject(jdata.length()-1); //leemos el primer segmento en nuestro caso el unico
    				estado=json_data.getInt("Datos");//accedemos al valor 
    				Log.i("casaAniadida","casaAniadida= "+estado);//muestro por log que obtuvimos
    			} catch (JSONException e) { e.printStackTrace(); }		            

    			//validamos el valor obtenido
    			if (estado==0){
    				Log.e("casaAniadida ", "invalido");
    				return false;
    			}
    			else{
    				Log.i("casaAniadida ", "valido");
    				return true;
    			}

    		}else{	//json obtenido invalido verificar parte WEB.
    			Log.e("JSON  ", "ERROR");
    			return false;
    		}
    	}
    }
}
