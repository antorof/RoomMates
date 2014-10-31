package com.roommates.roommates;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class NewTaskActivity extends ActionBarActivity {
	private String URL_connect = Constantes.NUEVA_TAREA_URL;
	private Httppostaux post;

    private EditText inputDateFrom;
    private Spinner spinnerTipoIntervalo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_task);

		setupActionBar();
        
        post = new Httppostaux();

        inputDateFrom = (EditText) findViewById(R.id.editTextFechaInicio);

        final Calendar c = Calendar.getInstance();
        inputDateFrom.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatePickerDialog dpd = new DatePickerDialog(
                        NewTaskActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                inputDateFrom.setText(
                                        year+"-"+(monthOfYear+1)+"-"+dayOfMonth
                                );
                            }
                        },
                        c.get(Calendar.YEAR),
                        c.get(Calendar.MONTH),
                        c.get(Calendar.DAY_OF_MONTH)
                    );
                    dpd.show();
                }
            });

        spinnerTipoIntervalo = (Spinner) findViewById(R.id.spinnerTipoIntervalo);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.tipo_intervalo_tarea, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipoIntervalo.setAdapter(spinnerAdapter);
        spinnerTipoIntervalo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
//                Toast.makeText(NewTaskActivity.this,
//                        ":"+pos+":"+" "+parent.getItemAtPosition(pos),
//                        Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

	/**
	 * Set up the ActionBar
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
		getMenuInflater().inflate(R.menu.new_task, menu);
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
    		if (realizarFuncion()){
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
                setResult(ActionBarActivity.RESULT_OK);
    			finish();
                overridePendingTransition(R.anim.right_in, R.anim.right_out);
    		} else{
    			Toast.makeText(getApplicationContext(), "Error: task not added", Toast.LENGTH_LONG).show();
    		}

    	}

    	public boolean realizarFuncion() {
    		int estado = -1;
    		EditText etNombre = (EditText) findViewById(R.id.editTextNombreTarea);
            EditText etFrom = (EditText) findViewById(R.id.editTextFechaInicio);
            EditText etIntervalo = (EditText) findViewById(R.id.editTextIntervalo);
            Spinner spTipoIntervalo = (Spinner) findViewById(R.id.spinnerTipoIntervalo);

    		if( etNombre.getText().toString().length() == 0   ||
                    etFrom.getText().toString().length() == 0 ||
                    etIntervalo.getText().toString().length() == 0 ) {
				Log.e("error datos", "campos no rellenados");
    			return false;	
    		}
    		
    		ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();

    		postparameters2send.add(new BasicNameValuePair("Correo",Session.email));
    		postparameters2send.add(new BasicNameValuePair("Contrasena",Session.password));
            postparameters2send.add(new BasicNameValuePair("idVivienda",Session.currentApartmentID));
    		postparameters2send.add(new BasicNameValuePair("nombreTarea",etNombre.getText().toString()));
            postparameters2send.add(new BasicNameValuePair("inicio",etFrom.getText().toString()));
            postparameters2send.add(new BasicNameValuePair("intervalo",etIntervalo.getText().toString()));
            postparameters2send.add(new BasicNameValuePair("tipoIntervalo",""+spTipoIntervalo.getSelectedItemPosition()));

    		//realizamos una peticion y como respuesta obtenes un array JSON
    		JSONArray jdata=post.getServerData(postparameters2send, URL_connect);

    		//si lo que obtuvimos no es null
    		if (jdata!=null && jdata.length() > 0){
    			JSONObject json_data; //creamos un objeto JSON
    			
    			try {
    				json_data = jdata.getJSONObject(jdata.length()-1); //leemos el primer segmento en nuestro caso el unico
    				estado    = json_data.getInt("tareaAniadida");//accedemos al valor
    				Log.i("tareaAniadida",""+estado);//muestro por log que obtuvimos
    			} catch (JSONException e) { e.printStackTrace(); }		            

    			//validamos el valor obtenido
    			if (estado == 0) {
    				Log.e("tareaAniadida", "invalido");
    				return false;
    			}
    			else {
    				Log.i("tareaAniadida", "valido");
    				return true;
    			}

    		}else{	//json obtenido invalido verificar parte WEB.
    			Log.e("JSON", "ERROR");
    			return false;
    		}
    	}
    }
}
