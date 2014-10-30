package com.roommates.roommates;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private static final int BILLS = 1;
	private static final int SHOPPING = 2;
	private static final int TASKS = 3; 
	private static final int ROOMMATES = 4;
	private static final int APARTMENTS = 5;
	
	private List<String> listDataHeader;
    private HashMap<String, Object[]> listDataHeaderElems;
    private HashMap<String, List<String>> listDataChild;

	
	private String[] opcionesMenu;				// Las opciones diferentes que tendra el menu
	private DrawerLayout drawerLayout;			// Layout principal
	private ListView drawerList;				// Menu en si
    private CharSequence tituloSeccion; 		// Titulo de la seccion, coincide con opcionesMenu
    private CharSequence tituloApp;
	private ActionBarDrawerToggle drawerToggle; // Boton que abre el menu
	private ArrayAdapterNavigationDrawer adapter;
	private String[] tituloOpcionesMenu;
    private Toolbar toolbar;

    public Drawable inicial;
	
	private FragmentBills fragmentBills;
	private FragmentApartments fragmentApartments;
	private FragmentShopping fragmentShopping;
    private FragmentTasks fragmentTasks;
    private FragmentRoommates fragmentRoommates;

	private Menu menu;
	protected String username = null;
	protected String password = null;
	protected String nombre = null;
	protected String apellidos = null;
	protected String color = null;
	protected String idViviendaActual = null;
    protected String nombreViviendaActual = null;
    protected String rolEnViviendaActual = null;
    protected boolean actualizandoHomeFacturas = false;
    protected boolean actualizandoHomeCompras = false;
    protected boolean actualizandoHomeTareas = false;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Bundle extras = getIntent().getExtras();
        //Obtenemos datos enviados en el intent.
        if (extras != null) {
        	username  = extras.getString("USERNAME");
        	password  = extras.getString("PASSWORD");
        	nombre    = extras.getString("NOMBRE");
        	apellidos = extras.getString("APELLIDOS");
        	color     = extras.getString("COLOR");
        }else{
        	username  = "error";
     	   	password  = "error";
     	   	nombre    = "error";
     	   	apellidos = "error";
     	   	color     = "error";
        }
        
        /*	SOLO PARA EL PROTOTIPO	*/
        idViviendaActual = "35";
		/*							*/
		
        opcionesMenu = new String[] {
                getString(R.string.drawer_name_home),
                getString(R.string.drawer_name_bills),
                getString(R.string.drawer_name_shopping),
                getString(R.string.drawer_name_tasks),
                getString(R.string.drawer_name_roommates),
                getString(R.string.drawer_name_rooms)
        };
		
		tituloOpcionesMenu = new String[] {
				(String) getTitle(), opcionesMenu[1],opcionesMenu[2],
				opcionesMenu[3],opcionesMenu[4],opcionesMenu[5] 
		};
		
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawerList = (ListView) findViewById(R.id.left_drawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
		
		adapter = new ArrayAdapterNavigationDrawer(getSupportActionBar().getThemedContext(),
							    android.R.layout.simple_list_item_1,
							    opcionesMenu);
		
		drawerList.setAdapter(adapter);
        drawerLayout.setScrimColor(Color.TRANSPARENT);
		   
		// Aniadimos un listener para cuando clickemos en el menu
		drawerList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView parent, View view,
					int position, long id) {

				Fragment fragment;

				switch (position) {
					case 0:
						fragment = new FragmentHome();
						break;
					case BILLS:
                        if (fragmentBills == null) {
                            fragment = new FragmentBills();
                            fragmentBills = (FragmentBills)fragment;
                        }
						else{
                            fragment = fragmentBills;
                            fragmentBills.actualizarLista();
                        }
						break;
					case SHOPPING:
                        if (fragmentShopping == null) {
                            fragment = new FragmentShopping();
                            fragmentShopping = (FragmentShopping)fragment;
                        }
                        else {
                            fragment = fragmentShopping;
                            fragmentShopping.actualizarLista();
                        }
						break;
					case TASKS:
                        if (fragmentTasks == null) {
                            fragment = new FragmentTasks();
                            fragmentTasks = (FragmentTasks)fragment;
                        }
                        else {
                            fragment = fragmentTasks;
                            fragmentTasks.actualizarLista();
                        }
						break;
					case ROOMMATES:
                        if (fragmentRoommates == null) {
                            fragment = new FragmentRoommates();
                            fragmentRoommates = (FragmentRoommates) fragment;
                        }
                        else {
                            fragment = fragmentRoommates;
                            fragmentRoommates.actualizarLista();
                        }
						break;
					case APARTMENTS:
                        if (fragmentApartments == null) {
                            fragmentApartments = new FragmentApartments();
                            fragment = fragmentApartments;
                        }
                        else {
                            fragment = fragmentApartments;
                            fragmentApartments.actualizarLista();
                        }
						break;
					default:
						fragment = new FragmentHome();
				}
				
				adapter.selectedItem = position;
				
				FragmentManager fragmentManager = getSupportFragmentManager();

				fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

				drawerList.setItemChecked(position, true);

				tituloSeccion = tituloOpcionesMenu[position];
				getSupportActionBar().setTitle(tituloSeccion);
                getSupportActionBar().setSubtitle(null);
//                getSupportActionBar().setIcon(R.drawable.ic_launcher);
				
				onPrepareOptionsMenu(menu);

				drawerLayout.closeDrawer(drawerList);
			}
		});
		
		// Marco por defecto el primer item del menu:
		drawerList.setItemChecked(0, true);
		tituloSeccion = tituloOpcionesMenu[0];
		// Pongo directamente el primer fragment para que se muestre por defecto:
		getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new FragmentHome()).commit();
		
		tituloApp = getTitle();
		drawerToggle = new ActionBarDrawerToggle(
				this,
				drawerLayout,
				toolbar,
                R.string.app_name, // Drawer open
                R.string.app_name) // Drawer close
		{
			
			// Cuando el menu esta cerrado pongo como titulo el titulo de la seccion:
			public void onDrawerClosed(View view) {
				getSupportActionBar().setTitle(tituloSeccion);
                getSupportActionBar().setSubtitle(null);
//                getSupportActionBar().setIcon(R.drawable.ic_launcher);
				ActivityCompat.invalidateOptionsMenu(MainActivity.this);
			}
			// Cuando el menu esta abierto pongo como titulo el titulo de la aplicacion:
			public void onDrawerOpened(View drawerView) {
				getSupportActionBar().setTitle(Session.name);
                getSupportActionBar().setSubtitle(Session.currentApartmentName);
//                getSupportActionBar().setIcon(getDrawableInicial());
				ActivityCompat.invalidateOptionsMenu(MainActivity.this);
			}
		};

		drawerLayout.setDrawerListener(drawerToggle);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		getOverflowMenu();
	}

    private Drawable getDrawableInicial() {
        Bitmap snapshot;
        if (inicial == null) {
            View viewInicial = findViewById(R.id.homeCardNameBackground);
            viewInicial.setDrawingCacheEnabled(true);
            viewInicial.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW); //Quality of the snpashot
            try {
                Bitmap img = viewInicial.getDrawingCache();
                int width = img.getWidth(),
                    height = img.getHeight();
                float ratio = 1.0f*height/width;

                int x = width/2 - height/2;

                Log.w("ratio",ratio+"");
                snapshot = Bitmap.createBitmap(img,x,0,(int)(width*ratio),height); // You can tell how to crop the snapshot and whatever in this method
                inicial = new BitmapDrawable(getResources(),snapshot);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                viewInicial.setDrawingCacheEnabled(false);
            }
        }
        return inicial;
    }

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.clear();
		getMenuInflater().inflate(R.menu.main, menu);
		
//		boolean menuAbierto = drawerLayout.isDrawerOpen(drawerList);
		int elementoSeleccionado = drawerList.getCheckedItemPosition();
		
//		if(menuAbierto)
//			menu.findItem(R.id.action_about).setVisible(false);
//		else
//			menu.findItem(R.id.action_about).setVisible(true);

		switch(elementoSeleccionado) {
			case SHOPPING:
				getMenuInflater().inflate(R.menu.shopping, menu);
				break;
			case TASKS:
				getMenuInflater().inflate(R.menu.tasks, menu);
				break;
			case APARTMENTS:
				getMenuInflater().inflate(R.menu.apartments, menu);
				break;
            case BILLS:
                getMenuInflater().inflate(R.menu.bills, menu);
                break;
            case ROOMMATES:
                getMenuInflater().inflate(R.menu.roommates, menu);
                if( Session.currentRole.equals("1") )
                    getMenuInflater().inflate(R.menu.roommates_plus, menu);
                break;
			default:
				break;
		}
		
		return super.onPrepareOptionsMenu(menu);
	}

	/*
	 * Funcion aniadir los elementos del menu.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		this.menu = menu;
		return true;
	}
	
	/*
	 * Para menus contextuales 
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo)
	{
		super.onCreateContextMenu(menu, v, menuInfo);
	    MenuInflater inflater = getMenuInflater();
	    switch (v.getId()){
	    	case R.id.listaTareas:
	    	    inflater.inflate(R.menu.menu_contextual_tasks, menu);
	    		break;
	    	case R.id.listaCompras:
	    	    inflater.inflate(R.menu.menu_contextual_shopping, menu);
	    		break;
	    	case R.id.listaFacturas:
	    	    inflater.inflate(R.menu.menu_contextual_bills, menu);
	    		break;
	    }
	}

	/*
	 * Funci&oacute;n manejadora del evento de seleccionar 
	 * un elemento del men&uacute;.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (drawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		Intent intent;
		switch(item.getItemId())
		{
			case R.id.action_settings:
				intent = new Intent(this, Preferencias.class);
		    	startActivity(intent);
				break;
            case R.id.action_about:
                Toast.makeText(this, "RoomMates est\u00E1 todav\u00EDa en fase de prototipo.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_logout:
                SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                SharedPreferences.Editor Ed=sp.edit();
                Ed.putString("password","-1");
                Ed.putString("id_vivienda","-1");
                Ed.putString("nombre_vivienda","-1");
                Ed.commit();

                intent = new Intent(this, LoginActivity.class);
                intent.putExtra("LOGOGUT",true);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.right_out);
                finish();
                break;
			case R.id.action_add_task:
				if(!idViviendaActual.equals("-1")){
					intent = new Intent(this,NewTaskActivity.class);
                    startActivityForResult(intent, TASKS);
                    overridePendingTransition(R.anim.left_in, R.anim.left_out);
				}
				break;
			case R.id.action_add_product:
				if(!idViviendaActual.equals("-1")){
					intent = new Intent(this,NewProductActivity.class);
                    startActivityForResult(intent, SHOPPING);
                    overridePendingTransition(R.anim.left_in, R.anim.left_out);
				}
				break;
			case R.id.action_add_apartment:
				intent = new Intent(this,NewApartmentActivity.class);
                startActivityForResult(intent, APARTMENTS);
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
				break;
            case R.id.action_add_bill:
                if(!idViviendaActual.equals("-1")){
                    intent = new Intent(this,NewBillActivity.class);
                    startActivityForResult(intent, BILLS);
                    overridePendingTransition(R.anim.left_in, R.anim.left_out);
                }
                break;
            case R.id.action_add_roommate:
                if(!idViviendaActual.equals("-1")){
                    intent = new Intent(this,AddRoommateActivity.class);
                    startActivityForResult(intent, ROOMMATES);
                    overridePendingTransition(R.anim.left_in, R.anim.left_out);
                }
                break;
			case R.id.action_sync_tasks:
//				fragmentTasks = new FragmentTasks();
//				getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragmentTasks).commit();
                fragmentTasks.actualizarLista();
				break;
			case R.id.action_sync_shopping:
//				fragmentShopping = new FragmentShopping();
//				getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragmentShopping).commit();
                fragmentShopping.actualizarLista();
				break;
			case R.id.action_sync_apartments:
//				fragmentApartments = new FragmentApartments();
//				getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragmentApartments).commit();
                fragmentApartments.actualizarLista();
				break;
            case R.id.action_sync_bills:
//                fragmentBills = new FragmentBills();
//                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragmentBills).commit();
                fragmentBills.actualizarLista();
                break;
            case R.id.action_sync_roommates:
//                fragmentRoommates = new FragmentRoommates();
//                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragmentRoommates).commit();
                fragmentRoommates.actualizarLista();
                break;
			default:
				return super.onOptionsItemSelected(item);
		}

		return true;
	}
	
	/*
	 * Acciones de los botones del menu contextual,
	 * cuando mantenemos pulsado un item.
	 */
	@Override
	public boolean onContextItemSelected(MenuItem item) {
	 Log.v("hola","hola");
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
//		getListAdapter().getItem(info.position);
		switch (item.getItemId()) {
	        default:
	            return super.onContextItemSelected(item);
	    }
	}
	
	/* Funcion que no se para que es pero se pone porque lo dice la guia */
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		drawerToggle.syncState();
	}
	/* Funcion que no se para que es pero se pone porque lo dice la guia */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		drawerToggle.onConfigurationChanged(newConfig);
	}
	/** 
	 * <p> Funcion para a&ntilde;dir los puntitos, el menu Overflow.
	 * <p> Supuestamente no es bueno hacer esto porque rompe 
	 *     con la guia de dise&ntilde;o de Android, en la que pone
	 *     que solo deben aparecer en los tlfonos sin boton
	 *     f&iacute;sico de menu.*/
	private void getOverflowMenu() {
	     try {
	        ViewConfiguration config = ViewConfiguration.get(this);
	        Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
	        if(menuKeyField != null) {
	            menuKeyField.setAccessible(true);
	            menuKeyField.setBoolean(config, false);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	/**
	 * <p> Funci&oacute;n encargada de manejar el evento click
	 *     de las distintas vistas que la puedan llamar.
	 * <p> Evento <tt>onClick</tt>.
	 * @param v <tt>View</tt> que ha producido el evento
	 */
	public void fragmentHome_onClick(View v) {
		int itemASeleccionar = 0;
		switch(v.getId()){
			case R.id.txtView_settings_fragment_home:
				Intent intent = new Intent(this, Preferencias.class);
		    	startActivity(intent);
				break;
			case R.id.txtView_rooms_fragment_home:
				itemASeleccionar = APARTMENTS;
				fragmentApartments = new FragmentApartments();
				getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragmentApartments).commit();
				break;
			case R.id.billsCardTitle:
			case R.id.cardListBills:
				itemASeleccionar = BILLS;
				fragmentBills = new FragmentBills();
				getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragmentBills).commit();
				break;
			case R.id.tasksCardTitle:
			case R.id.cardListTasks:
				itemASeleccionar = TASKS;
				fragmentTasks = new FragmentTasks();
				getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,fragmentTasks ).commit();
				break;
			case R.id.shoppingCardTitle:
			case R.id.cardListShopping:
				itemASeleccionar = SHOPPING;
				fragmentShopping = new FragmentShopping();
				getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragmentShopping).commit();
				break;
			case R.id.txtView_roommates_fragment_home:
				itemASeleccionar = ROOMMATES;
				getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new FragmentRoommates()).commit();
				break;
		}
		adapter.selectedItem = itemASeleccionar;
		drawerList.setItemChecked(itemASeleccionar, true);
		tituloSeccion = tituloOpcionesMenu[itemASeleccionar];
		getSupportActionBar().setTitle(tituloSeccion);
		onPrepareOptionsMenu(menu);
	} 
	
	/*
	 * Sobrescribe el metodo ejecutado al pulsar el boton de "atras"
	 */
	@Override
	public void onBackPressed() {
		// Si no estamos en el Home vuelve al Home
		if(adapter.selectedItem != 0){
			getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new FragmentHome()).commit();
			adapter.selectedItem = 0;
			drawerList.setItemChecked(0, true);
			tituloSeccion = tituloOpcionesMenu[0];
			getSupportActionBar().setTitle(tituloSeccion);
			onPrepareOptionsMenu(menu);
		}
		else
			super.onBackPressed();
	}

    /**
     * Clase que se encarga de la peticion as&iacute;ncrona para eliminar
     * un elemento.
     */
	class AsyncRemove extends AsyncTask< String, String, String > {
		private ProgressDialog pDialog;
		private String URL_CONNECT;
		private String elemento_a_borrar;
		private String tipo_elemento;
		private String[] parametrosRecibidos;
		private Httppostaux post;
    	
    	protected void onPreExecute() {
	    	//para el progress dialog
	    	pDialog = new ProgressDialog(MainActivity.this);
	    	pDialog.setMessage("Deleting....");
	    	pDialog.setIndeterminate(false);
	    	pDialog.setCancelable(false);
	    	pDialog.show();
    	}

    	protected String doInBackground(String... params) {
    		elemento_a_borrar = params[0];
    		parametrosRecibidos = params;
    		
    		if( params[0].equals("BILLS") ) {
    			tipo_elemento = "Bill";
    		    URL_CONNECT=Constantes.ELIMINAR_FACTURA_URL;
    		}
    		else if( params[0].equals("TASKS") ) {
    			tipo_elemento = "Task";
    		    URL_CONNECT=Constantes.ELIMINAR_TAREA_URL;
    		}
    		else if( params[0].equals("SHOPPING") ) {
    			tipo_elemento = "Product";
    		    URL_CONNECT=Constantes.ELIMINAR_PRODUCTO_URL;
    		}
    		// enviamos y recibimos y analizamos los datos en segundo plano.
    		if (eliminarElemento()){
    			return "ok";
    		} else{    		
    			return "err";
    		}
    	}
    	
    	protected void onPostExecute(String result) {
    		pDialog.dismiss();//ocultamos progess dialog.
    		Log.v("[asyncRemove] onPostExecute=",""+result);

    		if (result.equals("ok")){
    			Toast.makeText(getApplicationContext(), tipo_elemento+" deleted", Toast.LENGTH_LONG).show();
        		if( elemento_a_borrar.equals("BILLS") ) {
                    fragmentBills.actualizarLista();
        		}
        		else if( elemento_a_borrar.equals("TASKS") ) {
                    fragmentTasks.actualizarLista();
        		}
        		else if( elemento_a_borrar.equals("SHOPPING") ) {
                    fragmentShopping.actualizarLista();
        		}
    		} else{
    			Toast.makeText(getApplicationContext(), "Error: "+tipo_elemento+" not deleted", Toast.LENGTH_LONG).show();
    		}

    	}

    	public boolean eliminarElemento() {
    		int estado = -1;

    		ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();
    		postparameters2send.add(new BasicNameValuePair("Correo",Session.email));
    		postparameters2send.add(new BasicNameValuePair("Contrasena",Session.password)); 
    		
    		if( elemento_a_borrar.equals("BILLS") ) {
        		postparameters2send.add(new BasicNameValuePair("idFactura",parametrosRecibidos[1]));
    		}
    		else if( elemento_a_borrar.equals("TASKS") ) {
        		postparameters2send.add(new BasicNameValuePair("idVivienda",Session.currentApartmentID));
        		postparameters2send.add(new BasicNameValuePair("nombreTarea",parametrosRecibidos[2]));
    		}
    		else if( elemento_a_borrar.equals("SHOPPING") ) {
        		postparameters2send.add(new BasicNameValuePair("idVivienda",Session.currentApartmentID));
        		postparameters2send.add(new BasicNameValuePair("nombreProducto",parametrosRecibidos[2]));
    		}

    		post = new Httppostaux();
    		//realizamos una peticion y como respuesta obtenes un array JSON
    		JSONArray jdata=post.getServerData(postparameters2send, URL_CONNECT);

    		//si lo que obtuvimos no es null
    		if (jdata!=null && jdata.length() > 0){
    			JSONObject json_data; //creamos un objeto JSON
    			
    			try {
    				json_data = jdata.getJSONObject(jdata.length()-1); //leemos el primer segmento en nuestro caso el unico
    				estado=json_data.getInt("Resultado");//accedemos al valor 
    				Log.i("Resultado","Resultado= "+estado);//muestro por log que obtuvimos
    			} catch (JSONException e) { e.printStackTrace(); }		            

    			//validamos el valor obtenido
    			if (estado==0){
    				Log.e("Resultado ", "invalido");
    				return false;
    			}
    			else{
    				Log.i("Resultado ", "valido");
    				return true;
    			}

    		}else{	//json obtenido invalido verificar parte WEB.
    			Log.e("JSON  ", "ERROR");
    			return false;
    		}
    	}
    }

	/**
	 * Clase que se encarga de la actualizaci&oacute;n de un elemento.
	 * 
	 */
	class AsyncUpdate extends AsyncTask< String, String, String > {
		private ProgressDialog pDialog;
		private String URL_CONNECT;
		private String elemento_a_actualizar;
		private String tipo_elemento;
		private String[] parametrosRecibidos;
		private Httppostaux post;
    	
    	protected void onPreExecute() {
	    	//para el progress dialog
	    	pDialog = new ProgressDialog(MainActivity.this);
	    	pDialog.setMessage("Updating....");
	    	pDialog.setIndeterminate(false);
	    	pDialog.setCancelable(false);
	    	pDialog.show();
    	}

    	protected String doInBackground(String... params) {
    		elemento_a_actualizar = params[0];
    		parametrosRecibidos = params;
    		
    		if( params[0].equals("BILLS") ) {
    			tipo_elemento = "Bill";
                URL_CONNECT=Constantes.ACTUALIZAR_FACTURA_URL;
    		}
    		else if( params[0].equals("TASKS") ) {
    			tipo_elemento = "Task";
    		    //URL_CONNECT="http://"+IP_Server+"/eliminar_task_android.php";
    		}
    		else if( params[0].equals("SHOPPING") ) {
    			tipo_elemento = "Product";
    		    URL_CONNECT=Constantes.ACTUALIZAR_PRODUCTO_URL;
    		}
    		
    		//enviamos y recibimos y analizamos los datos en segundo plano.
    		if (actualizarElemento()){
    			return "ok"; // tarea aniadida
    		} else{    		
    			return "err"; // tarea no aniadida   	          	  
    		}
    	}
    	
    	protected void onPostExecute(String result) {
    		pDialog.dismiss();//ocultamos progess dialog.
    		Log.v("[asyncUpdate] onPostExecute=",""+result);

    		if (result.equals("ok")){
    			Toast.makeText(getApplicationContext(), tipo_elemento+" updated", Toast.LENGTH_LONG).show();
        		if( elemento_a_actualizar.equals("BILLS") )  {
                    fragmentBills.actualizarLista();
        		}
        		else if( elemento_a_actualizar.equals("TASKS") ) {
                    fragmentTasks.actualizarLista();
        		}
        		else if( elemento_a_actualizar.equals("SHOPPING") ) {
                    fragmentShopping.actualizarLista();
        		}
    		} else{
    			Toast.makeText(getApplicationContext(), "Error: "+tipo_elemento+" not updated", Toast.LENGTH_LONG).show();
    		}

    	}

    	public boolean actualizarElemento() {
    		int estado = -1;

    		ArrayList<NameValuePair> postparameters2send= new ArrayList<NameValuePair>();
    		postparameters2send.add(new BasicNameValuePair("Correo",Session.email)); 
    		postparameters2send.add(new BasicNameValuePair("Contrasena",Session.password));
    		
    		if( elemento_a_actualizar.equals("BILLS") ) {
                postparameters2send.add(new BasicNameValuePair("idVivienda",Session.currentApartmentID));
                postparameters2send.add(new BasicNameValuePair("idFactura",parametrosRecibidos[2]));
                postparameters2send.add(new BasicNameValuePair("Pagado",parametrosRecibidos[3]));
                Calendar cal = new GregorianCalendar();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH) + 1;
                int day = cal.get(Calendar.DAY_OF_MONTH);
                postparameters2send.add(new BasicNameValuePair("Fecha",
                        Integer.toString(year)+"-"+Integer.toString(month)
                                +"-"+Integer.toString(day)));
    		}
    		else if( elemento_a_actualizar.equals("TASKS") ) {
                // TODO Marcar como hecha una tarea, hará falta también el PHP
        		//postparameters2send.add(new BasicNameValuePair("idVivienda",parametrosRecibidos[1]));
        		//postparameters2send.add(new BasicNameValuePair("nombreTarea",parametrosRecibidos[2]));
    		}
    		else if( elemento_a_actualizar.equals("SHOPPING") ) {
        		postparameters2send.add(new BasicNameValuePair("idVivienda",Session.currentApartmentID));
        		postparameters2send.add(new BasicNameValuePair("nombreProducto",parametrosRecibidos[2]));
        		postparameters2send.add(new BasicNameValuePair("urgente",parametrosRecibidos[3]));
        		if(this.parametrosRecibidos[3].equals("0")){ // si vamos a marcar como comprado
        			Calendar cal = new GregorianCalendar();
            	    int year = cal.get(Calendar.YEAR);
            	    int month = cal.get(Calendar.MONTH) + 1;
            	    int day = cal.get(Calendar.DAY_OF_MONTH);
            	    postparameters2send.add(new BasicNameValuePair("Fecha",
            	    		Integer.toString(year)+"-"+Integer.toString(month)
            	    		+"-"+Integer.toString(day)));
        		}
    		}

    		post = new Httppostaux();
    		
    		//realizamos una peticion y como respuesta obtenes un array JSON
    		JSONArray jdata = post.getServerData(postparameters2send, URL_CONNECT);

    		//si lo que obtuvimos no es null
    		if (jdata!=null && jdata.length() > 0){
    			JSONObject json_data; //creamos un objeto JSON
    			
    			try {
    				json_data = jdata.getJSONObject(jdata.length()-1); //leemos el primer segmento en nuestro caso el unico
    				estado=json_data.getInt("Resultado");//accedemos al valor 
    				Log.i("Resultado","Resultado= "+estado);//muestro por log que obtuvimos
    			} catch (JSONException e) { e.printStackTrace(); estado=0; }

    			//validamos el valor obtenido
    			if (estado==0){
    				Log.e("Resultado ", "invalido");
    				return false;
    			}
    			else{
    				Log.i("Resultado ", "valido");
    				return true;
    			}

    		}else{	//json obtenido invalido verificar parte WEB.
    			Log.e("JSON  ", "ERROR");
    			return false;
    		}
    	}
    }
	
	public void collapseBills(View view)
	{
		collapse(R.id.cardListBills, view);
	}
	public void collapseShopping(View view)
	{
		collapse(R.id.cardListShopping, view);
	}
	public void collapseTasks(View view)
	{
		collapse(R.id.cardListTasks, view);
	}
	/**
	 * Funci&oacute;n que abre y cierra las tarjetas del Home
	 * @param id ID de la tarjeta
	 * @param view Bot&oacute;n de la tarjeta en el que hemos pinchado
	 */
	private void collapse(int id, View view)
	{
		ImageButton btn = (ImageButton) view;
		LinearLayout linear = (LinearLayout) findViewById(id);
		
		if(linear.getVisibility() == View.VISIBLE)
		{
			linear.setVisibility(View.GONE); 
			btn.setImageResource(R.drawable.arrow_right);
		}
		else
		{
			linear.setVisibility(View.VISIBLE);
			btn.setImageResource(R.drawable.arrow_down);
		}
	}
	
	/**
	 * Actualiza la lista de apartamentos de la seccion "apartamentos"
	 * y le asocia un clickListener.
	 * @param values: elementos obtenidos en la consulta
	 * @param numItems: numero de elementos
	 */
	public void actualizarListaApartamentos(Object[] values, int numItems)
	{
		ArrayAdapterApartments adapter = new ArrayAdapterApartments(
				this, android.R.layout.simple_list_item_1, values);
		ListView listaApartamentos = (ListView) findViewById(R.id.listaApartments);
		listaApartamentos.setAdapter(adapter);
		listaApartamentos.setClickable(true);
		listaApartamentos.setOnItemClickListener(new AdapterView.OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) 
			{
				ListView prueba = (ListView) findViewById(R.id.listaApartments);

				final Object[] value = (Object[]) prueba.getItemAtPosition(position);
				final AlertDialog.Builder myAB = new AlertDialog.Builder(MainActivity.this);
			
				myAB.setTitle(getString(R.string.alert_selectapartment_tittle))
			    	.setMessage(getString(R.string.alert_selectapartment_message))
			    	.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
			    		public void onClick(DialogInterface dialog, int which) {
				        	Toast.makeText(MainActivity.this, 
				        	"Apartment \""+value[2]+"\" selected", 
				        	Toast.LENGTH_LONG).show();
				        	MainActivity.this.idViviendaActual = (String) value[1];
				        	MainActivity.this.nombreViviendaActual = (String) value[2];
				        	MainActivity.this.rolEnViviendaActual = (String) value[4];

				        	// Almacenamos la vivienda actual en las preferencias:
				    		SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
				    		SharedPreferences.Editor Ed=sp.edit();
				    		Ed.putString("id_vivienda",MainActivity.this.idViviendaActual );
                            Ed.putString("nombre_vivienda",MainActivity.this.nombreViviendaActual );
                            Ed.putString("rol_en_vivienda",MainActivity.this.rolEnViviendaActual );
				    		Ed.commit();

                            Log.i("rol_en_vivienda",MainActivity.this.rolEnViviendaActual);

                            onBackPressed(); // Volvemos al HOME
			    		}
			    	})
			    	.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
			    		public void onClick(DialogInterface dialog, int which) {
			    		}
			    	}).show();
			}
		});
        ((SwipeRefreshLayout) findViewById(R.id.swipe_container_apartments)).setRefreshing(false);
	}

	/**
	 * Actualiza la lista de Roommates de la seccion "roommates"
	 * y le asocia un clickListener.
	 * @param values: elementos obtenidos en la consulta
	 * @param numItems: numero de elementos
	 */
	public void actualizarListaRoommates(Object[] values, int numItems)
	{
		ArrayAdapterRoommates adapter = new ArrayAdapterRoommates(
				this, android.R.layout.simple_list_item_1, values);
		ListView listaRoommates = (ListView) findViewById(R.id.listaRoommates);
		listaRoommates.setAdapter(adapter);

        ((SwipeRefreshLayout) findViewById(R.id.swipe_container_roommates)).setRefreshing(false);
	}
	
	/**
	 * Actualiza la lista de Compras de la seccion "compra"
	 * y le asocia un clickListener.
	 * @param values: elementos obtenidos en la consulta
	 * @param numItems: numero de elementos
	 */
	public void actualizarListaExpBills(Object[] values, int numItems)
	{
        int itemsPerBill = 9; // numero de elementos de cada factura

        ExpandableListAdapterBills listAdapter;
		ExpandableListView expListView;
		    
		listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        listDataHeaderElems = new HashMap<String, Object[]>();
	 
	    // Adding child data
	    for(int i=0; i<numItems/itemsPerBill; i++)
	    {
	    	Object[] value = (Object[]) values[i];
	    	listDataHeader.add(value[1].toString());
            listDataHeaderElems.put(value[1].toString(),value);
	    	Log.v("nombre", value[1].toString());
	    }
	    
	    // Añadir los hijos a cada Grupo
	    for(int i=0; i<numItems/itemsPerBill; i++)
	    {
            List<String> subitems = new ArrayList<String>();
            if(listDataHeaderElems.get(listDataHeader.get(i))[8].equals("0"))
                subitems.add("Pay");
            else
                subitems.add("Cancel");
            subitems.add("Delete");
	    	listDataChild.put(listDataHeader.get(i), subitems);
	    }
	    
	    // get the listview
	    expListView = (ExpandableListView)findViewById(R.id.listaFacturas);
    
	    listAdapter = new ExpandableListAdapterBills(this, listDataHeader, listDataChild, new ArrayList<Object>(Arrays.asList(values)));

	    // setting list adapter
	    expListView.setAdapter(listAdapter);
	    
	    // ClickListener para los subitems
	    expListView.setOnChildClickListener(new OnChildClickListener() {
	    	private String header;
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
            		int groupPosition, int childPosition, long id) {
    			header = listDataHeader.get(groupPosition);
        		if(listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Pay"))
        		{
        			Log.v("Click","Pay");
                    new DialogoAlerta(MainActivity.this, MainActivity.this,
                            getString(R.string.alert_paybill_tittle),
                            getString(R.string.alert_paybill_message),
                            "pay_bill",
                            new Object[]{header}).show();
                    return true;
        		}
                else if(listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Cancel"))
                {
                    Log.v("Click","Cancel");
                    new DialogoAlerta(MainActivity.this, MainActivity.this,
                            getString(R.string.alert_cancelpaymentbill_tittle),
                            getString(R.string.alert_cancelpaymentbill_message),
                            "cancel_payment_bill",
                            new Object[]{header}).show();
                    return true;
                }
                else if(listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Delete"))
        		{
        			Log.v("Click","Delete");
        			new DialogoAlerta(MainActivity.this, MainActivity.this, 
        					getString(R.string.alert_removebill_tittle),
                            getString(R.string.alert_removebill_message),
                            DialogoAlerta.WARNING,
        					"remove_bill",
                            new Object[]{header}).show();
        			return true;
        		}    
           
               	return false;
            }
        });
        ((SwipeRefreshLayout) findViewById(R.id.swipe_container_bills)).setRefreshing(false);
	}
    public void remove_bill(String idFactura){
        new AsyncRemove().execute("BILLS", idFactura);
    }
    public void pay_bill(String idFactura){
        new AsyncUpdate().execute("BILLS", idViviendaActual, idFactura,"1");
    }
    public void cancel_payment_bill(String idFactura){
        new AsyncUpdate().execute("BILLS", idViviendaActual, idFactura,"0");
    }
	
	/**
	 * Actualiza la lista de Compras de la seccion "compra"
	 * y le asocia un clickListener.
	 * @param values: elementos obtenidos en la consulta
	 * @param numItems: numero de elementos
	 */
	public void actualizarListaExpTareas(Object[] values, int numItems)
	{
        int itemsPerTask = 7; // numero de elementos de cada tarea

		ExpandableListAdapterTasks listAdapter;
		ExpandableListView expListView;
		    
		listDataHeader = new ArrayList<String>();
	    listDataChild = new HashMap<String, List<String>>();
	 
	    // Adding child data
	    for(int i=0; i<numItems/itemsPerTask; i++)
	    {
	    	Object[] value = (Object[]) values[i];
	    	listDataHeader.add(value[1].toString());
	    	Log.v("nombre", value[1].toString());
	    }
	 
	    // Adding child data
	    List<String> subitems = new ArrayList<String>();
	    subitems.add("Done");
	    subitems.add("Delete");
	    
	    // Añadir los hijos a cada Grupo
	    for(int i=0; i<numItems/itemsPerTask; i++)
	    {
	    	listDataChild.put(listDataHeader.get(i), subitems);
	    }
	    
	    // get the listview
	    expListView = (ExpandableListView)findViewById(R.id.listaTareas);
    
	    listAdapter = new ExpandableListAdapterTasks(this, listDataHeader, listDataChild, new ArrayList<Object>(Arrays.asList(values)));

	    // setting list adapter
	    expListView.setAdapter(listAdapter);
	    
	    // ClickListener para los subitems
	    expListView.setOnChildClickListener(new OnChildClickListener() {
	    	private String header;
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
            		int groupPosition, int childPosition, long id) {
    			header = listDataHeader.get(groupPosition);
        		if(listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Done"))
        		{
        			Log.v("Click","Done");
    	        	Toast.makeText(MainActivity.this, 
    	        			"Task \""+listDataHeader.get(groupPosition)+"\" done", 
    	        			Toast.LENGTH_LONG).show();
         			return true;
        		}else if(listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Delete"))
        		{
        			Log.v("Click","Delete");
        			new DialogoAlerta(MainActivity.this, MainActivity.this, 
        					getString(R.string.alert_removetask_tittle),
                            getString(R.string.alert_removetask_message),
                            DialogoAlerta.WARNING,
        					"remove_task",new Object[]{header}).show();
        			return true;
        		}    
           
               	return false;
            }
        });
        ((SwipeRefreshLayout) findViewById(R.id.swipe_container_tasks)).setRefreshing(false);
	}
    public void remove_task(String header){
		new AsyncRemove().execute("TASKS", idViviendaActual, header);
    }
	
	/**
	 * Actualiza la lista de Compras de la seccion "compra"
	 * y le asocia un clickListener.
	 * @param values: elementos obtenidos en la consulta
	 * @param numItems: numero de elementos
	 */
	public void actualizarListaExpCompras(Object[] values, int numItems)
	{
        int itemsPerProduct = 7; // numero de elementos de cada producto

		ExpandableListAdapterShopping listAdapter;
		ExpandableListView expListView;
		    
		listDataHeader = new ArrayList<String>();
	    listDataChild = new HashMap<String, List<String>>();
	 
	    // Adding child data
	    for(int i=0; i<numItems/itemsPerProduct; i++)
	    {
	    	Object[] value = (Object[]) values[i];
	    	listDataHeader.add(value[1].toString());
	    	Log.v("nombre", value[1].toString());
	    }
	 
	    // Adding child data
	    List<String> subItems = new ArrayList<String>();
	    subItems.add("Buy");
	    subItems.add("Urgent");
	    subItems.add("Delete");
	    
	    for(int i=0; i<numItems/itemsPerProduct; i++)
	    {
	    	listDataChild.put(listDataHeader.get(i), subItems);
	    }
	    // get the listview
	    expListView = (ExpandableListView)findViewById(R.id.listaCompras);
    
	    listAdapter = new ExpandableListAdapterShopping(this, listDataHeader, listDataChild, new ArrayList<Object>(Arrays.asList(values)));

	    // setting list adapter
	    expListView.setAdapter(listAdapter);
	    
	    expListView.setOnChildClickListener(new OnChildClickListener() {
	    	private String header;
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
            		int groupPosition, int childPosition, long id) {
    			header = listDataHeader.get(groupPosition);
        		if(listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Buy"))
        		{
        			new DialogoAlerta(MainActivity.this, MainActivity.this, 
        					getString(R.string.alert_markpurchased_tittle),
                            getString(R.string.alert_markpurchased_message),
        					"buy_product",new Object[]{header}).show();
         			return true;
        		}else if(listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Urgent"))
        		{
        			new DialogoAlerta(MainActivity.this, MainActivity.this, 
        					getString(R.string.alert_markurgentproduct_tittle),
                            getString(R.string.alert_markurgentproduct_message),
        					"urgent_product",new Object[]{header}).show();
        			return true;
        		}else if(listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).equals("Delete"))
        		{
        			new DialogoAlerta(MainActivity.this, MainActivity.this, 
        					getString(R.string.alert_deleteproduct_tittle),
                            getString(R.string.alert_deleteproduct_message),
                            DialogoAlerta.WARNING,
        					"remove_product",new Object[]{header}).show();
        			return true;
        		}         
           
               	return false;
            }
        });
        ((SwipeRefreshLayout) findViewById(R.id.swipe_container_shopping)).setRefreshing(false);
	}
    public void buy_product(String nombre){
    	new AsyncUpdate().execute("SHOPPING", idViviendaActual, nombre,"0");
    }
    public void urgent_product(String nombre){
        new AsyncUpdate().execute("SHOPPING", idViviendaActual, nombre,"1");
    }
    public void remove_product(String nombre){
		new AsyncRemove().execute("SHOPPING", idViviendaActual, nombre);
    }

	/**
	 * Actualiza la lista de facturas del resumen en Home.
	 * 
	 * @param values: elementos obtenidos en la consulta
	 * @param numItems: numero de elementos
	 */
	public void actualizarHomeFacturas(Object[] values, int numItems)
	{
		LinearLayout ll = (LinearLayout) findViewById(R.id.cardListBills);
        ll.removeAllViews();
		
		ArrayList<ArrayList<String> > lista = convertObjectToArrayList(values, numItems, 8);
		
		for(ArrayList<String> elem : lista)
		{
			LinearLayout linearLayout = (LinearLayout) View.inflate(this, R.layout.list_home, null);

			ImageView icono = (ImageView) linearLayout.findViewById(R.id.iconoListaSimple);
			icono.setImageResource(R.drawable.ic_bills_dark_mini);
			
			TextView nombreFactura = (TextView) linearLayout.findViewById(R.id.textoCentral) ;
			nombreFactura.setText(elem.get(2));
	
			TextView precioFactura = (TextView) linearLayout.findViewById(R.id.textoDerecha) ;
			precioFactura.setTextColor(Color.RED);	
		
			// Formateamos para que tenga menos decimales
//			DecimalFormat df = new DecimalFormat("0.00");
            NumberFormat format = NumberFormat.getCurrencyInstance();
			String result = format.format( Double.valueOf( elem.get(5) ) );

			precioFactura.setText( result );
			ll.addView(linearLayout);
		}
        actualizandoHomeFacturas = false;
	}
	
	/**
	 * Actualiza la lista de compras del resumen en Home.
     * Se utiliza en WebDatabaseBackground.
	 * 
	 * @param values: elementos obtenidos en la consulta
	 * @param numItems: numero de elementos
	 */
	public void actualizarHomeCompras(Object[] values, int numItems)
	{
		LinearLayout ll = (LinearLayout) findViewById(R.id.cardListShopping);
        ll.removeAllViews();
		
		ArrayList<ArrayList<String> > lista = convertObjectToArrayList(values, numItems, 7);
		
		for(ArrayList<String> elem : lista)
		{
			LinearLayout linearLayout = (LinearLayout) View.inflate(this, R.layout.list_home, null);

			ImageView icono = (ImageView) linearLayout.findViewById(R.id.iconoListaSimple);
			icono.setImageResource(R.drawable.ic_shopping_dark_mini);
			
			TextView nombreCompra = (TextView) linearLayout.findViewById(R.id.textoCentral) ;
			nombreCompra.setText(elem.get(1));
	
			TextView fechaCompra = (TextView) linearLayout.findViewById(R.id.textoDerecha) ;
			fechaCompra.setText(elem.get(2));
			ll.addView(linearLayout);
		}
        actualizandoHomeCompras = false;
	}

	/**
	 * Actualiza la lista de tareas del resumen en Home.
	 * 
	 * @param values: elementos obtenidos en la consulta
	 * @param numItems: numero de elementos
	 */
	public void actualizarHomeTareas(Object[] values, int numItems)
	{
		LinearLayout ll = (LinearLayout) findViewById(R.id.cardListTasks);
        ll.removeAllViews();

		ArrayList<ArrayList<String> > lista = convertObjectToArrayList(values, numItems, 7);
		
		for(ArrayList<String> elem : lista)
		{
			LinearLayout linearLayout = (LinearLayout) View.inflate(this, R.layout.list_home, null);
			
			ImageView icono = (ImageView) linearLayout.findViewById(R.id.iconoListaSimple);
			icono.setImageResource(R.drawable.ic_tasks_dark_mini);
			
			TextView nombreTarea = (TextView) linearLayout.findViewById(R.id.textoCentral) ;
			nombreTarea.setText(elem.get(1));
	
			TextView fechaTarea = (TextView) linearLayout.findViewById(R.id.textoDerecha) ;
			fechaTarea.setText(elem.get(6));
			ll.addView(linearLayout);
		}
        actualizandoHomeTareas = false;
	}

	/**
	 * Convierte un Object[] que fue devuelto por una consulta a la base de datos
	 * a un ArrayList<ArrayList<String> > que entienden nuestros metodos.
	 * 
	 * @param src: objeto a convertir
	 * @param numItems: numero de elementos total
	 * @param numItemsPerArray: numero de elementos por array
	 * @return lista de listas de String. Cada lista interna representa una fila de una consulta
	 */
	private ArrayList<ArrayList<String> > convertObjectToArrayList(Object[] src, int numItems, int numItemsPerArray )
	{
		ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
		
		for(int i=0; i<numItems/numItemsPerArray; i++)
		{
			Object[] objAux = (Object[]) src[i];
			ArrayList<String> aux = new ArrayList<String>();
			for(int j=0; j<numItemsPerArray; j++)
			{
				aux.add(objAux[j].toString());
			}
			list.add(aux);
		}
		
		return list;
	}

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        switch (requestCode) {
            case ROOMMATES:
                if (resultCode == ActionBarActivity.RESULT_OK)
                    fragmentRoommates.actualizarLista();
                break;
            case BILLS:
                if (resultCode == ActionBarActivity.RESULT_OK)
                    fragmentBills.actualizarLista();
                break;
            case APARTMENTS:
                if (resultCode == ActionBarActivity.RESULT_OK)
                    fragmentApartments.actualizarLista();
                break;
            case TASKS:
                if (resultCode == ActionBarActivity.RESULT_OK)
                    fragmentTasks.actualizarLista();
                break;
            case SHOPPING:
                if (resultCode == ActionBarActivity.RESULT_OK)
                    fragmentShopping.actualizarLista();
                break;
        }
    }

}
