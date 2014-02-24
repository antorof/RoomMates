package com.roommates.roommates;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.roommates.roommates.R;

public class DialogoAlerta {
	private final AlertDialog.Builder ad_b;
	private final MainActivity mainActivity;
	private final String accion;
	private final Object[] params;
	
	public static final int WARNING = 0;
	
	/**
	 * <p> Crea un AlertDialog que ejecuta mediante reflexión la función 
	 *     <tt>MainActivity.<i>onOKAction</i></tt> si se pincha en <b>OK</b>.
	 *     
	 * @param ma Referencia a MainActivity
	 * @param context Contexto en el que se mostrará el AlertDialog
	 * @param tittle T&iacute;tulo de la alerta
	 * @param message Mensaje de la alerta
	 * @param onOKAction <tt>String</tt> del nombre del m&eacute;todo a llamar
	 *        cuando se pinche en <b>OK</b>
	 * @param params Par&aacute;metros de la funci&oacute;n <tt>MainActivity.<i>onOKAction</i></tt>
	 */
	public DialogoAlerta(
			MainActivity ma, Context context, 
			String tittle, String message, 
			String onOKAction, Object[] params) {
		this(ma,context, message, onOKAction, params);
		
		ad_b.setTitle(tittle);
	}
	
	/**
	 * <p> Crea un AlertDialog que ejecuta mediante reflexión la función 
	 *     <tt>MainActivity.<i>onOKAction</i></tt> si se pincha en <b>OK</b>.
	 *     
	 * @param ma Referencia a MainActivity
	 * @param context Contexto en el que se mostrará el AlertDialog
	 * @param tittle T&iacute;tulo de la alerta
	 * @param message Mensaje de la alerta
	 * @param onOKAction <tt>String</tt> del nombre del m&eacute;todo a llamar
	 *        cuando se pinche en <b>OK</b>
	 * @param params Par&aacute;metros de la funci&oacute;n <tt>MainActivity.<i>onOKAction</i></tt>
	 */
	public DialogoAlerta(
			MainActivity ma, Context context, 
			String message, 
			String onOKAction, Object[] params) {
		this.ad_b = new AlertDialog.Builder(context);
		this.mainActivity = ma;
		this.accion = onOKAction;
		this.params = params;
		
		ad_b
    	.setMessage(message)
    	.setPositiveButton("OK", new DialogInterface.OnClickListener() {
    		public void onClick(DialogInterface dialog, int which) {
    			if(accion != null && !accion.equals("")){
	    			Class<? extends MainActivity> c = mainActivity.getClass();
	    			Method m = null;
	    			
	    			try {
	    				m = c.getMethod(accion, String.class);
	    			} catch (SecurityException e) {
	    				e.printStackTrace();
	    			} catch (NoSuchMethodException e) {
	    				e.printStackTrace();
	    			}
	    			
	    			try {
	    				m.invoke(mainActivity,DialogoAlerta.this.params );
		   			} catch (IllegalArgumentException e) {
		   				e.printStackTrace();
		   			} catch (IllegalAccessException e) {
		   				e.printStackTrace();
		   			} catch (InvocationTargetException e) {
		   				e.printStackTrace();
		   			} catch (Exception e) {
						e.printStackTrace();
					}
    			}	
    		}
    	})
    	.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
    		public void onClick(DialogInterface dialog, int which) {
    		}
    	});
	}
	
	/**
	 * <p> Crea un AlertDialog que ejecuta mediante reflexión la función 
	 *     <tt>MainActivity.<i>onOKAction</i></tt> si se pincha en <b>OK</b>.
	 *     
	 * @param ma Referencia a MainActivity
	 * @param context Contexto en el que se mostrará el AlertDialog
	 * @param tittle T&iacute;tulo de la alerta
	 * @param message Mensaje de la alerta
	 * @param icon <tt>Int</tt> que representa el icono de la alerta
	 * @param onOKAction <tt>String</tt> del nombre del m&eacute;todo a llamar
	 *        cuando se pinche en <b>OK</b>
	 * @param params Par&aacute;metros de la funci&oacute;n <tt>MainActivity.<i>onOKAction</i></tt>
	 */
	public DialogoAlerta(
			MainActivity ma, Context context, 
			String tittle, String message, int icon, 
			String onOKAction, Object[] params) {
		this(ma,context, tittle, message, onOKAction, params);
		
		switch( icon ){
			case WARNING:
				ad_b.setIcon(R.drawable.ic_action_warning);
				break;
			default:
				break;
		}
	}
	
	public void show(){
		ad_b.show();
	}

}
