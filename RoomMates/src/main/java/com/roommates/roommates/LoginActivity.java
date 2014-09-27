package com.roommates.roommates;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class LoginActivity extends ActionBarActivity {

	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserLoginTask mAuthTask = null;

	// Values for email and password at the time of the login attempt.
	private String mEmail;
	private String mPassword;

	// UI references.
	private EditText mEmailView;
	private EditText mPasswordView;
	private View mLoginFormView;
	private View mLoginStatusView;
	private TextView mLoginStatusMessageView;

	private String username;
	private String password;
	private String nombre;
	private String apellidos;
	private String color;

    String URL_connect = Constantes.LOGIN_URL;
	
    private Httppostaux post;

    private AlertDialog.Builder myAB;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);
		
		// Set up the login form.
		mEmailView = (EditText) findViewById(R.id.email);
		
		mPasswordView = (EditText) findViewById(R.id.password);
		mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.login || id == EditorInfo.IME_NULL) {
							attemptLogin();
							return true;
						}
						return false;
					}
				});

		mLoginFormView = findViewById(R.id.login_form);
		mLoginStatusView = findViewById(R.id.login_status);
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);

		post = new Httppostaux();
		
		/* Set up username and password: */
		// Buscamos en las preferencias, para ver si lo hemos guardado en otro momento:
	    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
	    String usuarioPref = sharedPref.getString("usuario", "-1");
	    String passwordPref = sharedPref.getString("password", "-1");
        
	    // Si esta en las preferencias, lo ponemos:
        if (!usuarioPref.equals("-1"))
        	mEmailView.setText(usuarioPref);
        if (!passwordPref.equals("-1"))
        	mPasswordView.setText(passwordPref);

		if (!usuarioPref.equals("-1") && !passwordPref.equals("-1")) {
            Bundle extras = getIntent().getExtras();
            if(extras == null)
                attemptLogin();
        }

        /* Creamos un Dialog por si luego nos pregunta si queremos guardar
         * las preferencias */
        myAB = new AlertDialog.Builder(this);
		myAB.setTitle(getString(R.string.alert_savecredentials_tittle))
	    .setMessage(getString(R.string.alert_savecredentials_message))
	    .setPositiveButton(getString(android.R.string.yes), new DialogInterface.OnClickListener()
	    {
	        public void onClick(DialogInterface dialog, int which) {
	        	// Almacenamos las credenciales:
    			SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
    			SharedPreferences.Editor Ed=sp.edit();
    			Ed.putString("usuario",mEmailView.getText().toString() );
    			Ed.putString("password",mPasswordView.getText().toString());
    			Ed.putString("id_vivienda","-1");
    			Ed.putString("nombre_vivienda","-1");
    			Ed.commit();

    			tryAsyncLogin();
	        }
	     })
	    .setNegativeButton(getString(android.R.string.no), new DialogInterface.OnClickListener()
	    {
	        public void onClick(DialogInterface dialog, int which) {
	        	// Eliminamos la ultima casa:
    			SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
    			SharedPreferences.Editor Ed=sp.edit();
    			Ed.putString("id_vivienda","-1");
    			Ed.putString("nombre_vivienda","-1");
    			Ed.commit();
	    		tryAsyncLogin();
	        }
	     });


		// Setup the button
		findViewById(R.id.sign_in_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						attemptLogin();
					}
				});

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	/**
	 * <p> Funci&oacute;n que ejecuta la funci&oacute;n as&iacute;ncrona
	 * de iniciar sesi&oacute;n, relacionada con <tt>UserLoginTask</tt>.
	 */
	private void tryAsyncLogin() {
		// Show a progress spinner, and kick off a background task to
		// perform the user login attempt.
		mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
		showProgress(true);
		
		username = mEmail;
		password = mPassword;
		
		mAuthTask = new UserLoginTask();
		mAuthTask.execute((Void) null);
	}
	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {
		if (mAuthTask != null) {
			return;
		}

		// Reset errors.
		mEmailView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		mEmail = mEmailView.getText().toString();
		mPassword = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		} else if (mPassword.length() < 3) {
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}

		// Check for a valid username.
		if (TextUtils.isEmpty(mEmail)) {
			mEmailView.setError(getString(R.string.error_field_required));
			focusView = mEmailView;
			cancel = true;
		} 
		else if (!mEmail.contains("@")) {
			mEmailView.setError(getString(R.string.error_invalid_email));
			focusView = mEmailView;
			cancel = true;
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
    		// Extreamos datos de los EditText:
    		String usuario=mEmailView.getText().toString();
    		String passw=mPasswordView.getText().toString();
    		// Comparamos con los de las preferencias: 
			SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
		    String usuarioPref = sharedPref.getString("usuario", "-1");
		    String passwordPref = sharedPref.getString("password", "-1");
		    
		    // Si no coinciden con los de las preferencias mostramos el Dialog:
		    if (!usuarioPref.equals(usuario) || !passwordPref.equals(passw))
        		myAB.show();
		    else {
		    	tryAsyncLogin();
		    }
		}
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mLoginFormView.setVisibility(View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class UserLoginTask extends AsyncTask<Void, Void, Integer> {
    	private int loginState = 2;
    	
		@Override
		protected Integer doInBackground(Void... params) {

			loginState = login(username, password);
			
			return loginState;
		}

		@Override
		protected void onPostExecute(final Integer success) {
			mAuthTask = null;

			if (success == 1) {
				SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
			    String lastUser = sharedPref.getString("last_user", "-1");
			    // Compruebo si se ha cambiado de usuario:
			    if (!username.equals(lastUser)) {
		        	// Eliminamos la ultima casa y ponemos el nuevo usuario:
	    			SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
	    			SharedPreferences.Editor Ed=sp.edit();
	    			Ed.putString("id_vivienda","-1");
	    			Ed.putString("nombre_vivienda","-1");
	    			Ed.putString("last_user",username);
	    			Ed.commit();
			    }
				Intent intent = new Intent(getApplicationContext(),MainActivity.class);
//		    	intent.putExtra("USERNAME", username);
//                intent.putExtra("PASSWORD", password);
//                intent.putExtra("NOMBRE", nombre);
//                intent.putExtra("APELLIDOS", apellidos);
//                intent.putExtra("COLOR", color);

                Session.email = username;
                Session.password = password;
                Session.name = nombre;
                Session.color = color;
		    	
		    	startActivity(intent);
				finish(); // Cerramos la pantalla de login
				
			} else if (success == 0)  {
				mEmailView.setError(getString(R.string.error_incorrect_username_or_password));
				mPasswordView.setError(getString(R.string.error_incorrect_username_or_password));
				mEmailView.requestFocus();
			} else if (success == -1)  {
				mEmailView.setError(null);
				mPasswordView.setError(null);
				Toast.makeText(
						LoginActivity.this, 
						getString(R.string.error_unable_to_connect), 
						Toast.LENGTH_LONG
						).show();
				final AlertDialog.Builder myAB = new AlertDialog.Builder(LoginActivity.this);
				myAB.setTitle("Error")
			    .setMessage(getString(R.string.error_unable_to_connect))
			    .setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int which) {
			        	// No se hace nada						
			        }
			     }).show();

			}
			showProgress(false);
		}

		@Override
		protected void onCancelled() {
			post = new Httppostaux();
			mAuthTask = null;
			showProgress(false);
		}
	}
	
	/**
	 * Hace login en el servicio web.
	 * 
	 * @param username Nombre de usuario con el que hacer login
	 * @param password Contrase&ntilde;a del usuario con el que hacer login
	 */
	public int login(String username ,String password ) {
		int logstatus=-1;

		/*Creamos un ArrayList del tipo nombre valor para agregar los datos recibidos por los parametros anteriores
		* y enviarlo mediante POST a nuestro sistema para relizar la validacion*/ 
		ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();

		postparameters2send.add(new BasicNameValuePair("Correo",username)); // son los mismos nombres 
		postparameters2send.add(new BasicNameValuePair("Contrasena",password));// que en el php

		//realizamos una peticion y como respuesta obtenes un array JSON
		JSONArray jdata=post.getServerData(postparameters2send, URL_connect);

		//si lo que obtuvimos no es null
		if (jdata!=null && jdata.length() > 0){
			JSONObject json_data; //creamos un objeto JSON
			try {
				json_data = jdata.getJSONObject(0); //leemos el primer segmento en nuestro caso el unico
				JSONArray datosDevueltos = json_data.getJSONArray("Datos");
				
				Log.i("Datos","datos= "+datosDevueltos);//muestro por log que obtuvimos

				nombre = datosDevueltos.getString(0);
				apellidos = datosDevueltos.getString(1);
				color = datosDevueltos.getString(2);
				
				logstatus = 1;
			} catch (JSONException e) {
				 Log.e("Datos","datos= datos no correctos");//muestro por log que obtuvimos
				 logstatus = 0;
				e.printStackTrace();
			}		            

			// Comprobamos si ha habido login correcto
			if (logstatus==0){
				Log.e("loginstatus ", "invalido");
				return 0;
			}
			else{
				Log.i("loginstatus ", "valido");
				return 1;
			}
		}
		else{	//json obtenido invalido verificar parte WEB.
			Log.e("Error al recibir JSON", "ERROR");
			return -1;
		}
	}
	
	public void signUp_button_onClick(View v) {
		Intent intent = new Intent(this, RegisterActivity.class);
	    startActivity(intent);
	} 
}
