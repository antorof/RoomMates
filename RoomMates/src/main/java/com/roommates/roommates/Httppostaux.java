package com.roommates.roommates;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;

import android.util.Log;

/**
 * CLASE AUXILIAR PARA EL ENVIO DE PETICIONES A NUESTRO SISTEMA
 * Y MANEJO DE RESPUESTA.
 */
public class Httppostaux {
	InputStream inputStream = null;
	String result = "";
    public int error = Constantes.NO_ERROR;

	public JSONArray getServerData(ArrayList<NameValuePair> parameters, String serverUrl) {
		//conecta via http y envia un post.
		httppostconnect(parameters,serverUrl);

		if (inputStream != null) { //si obtuvo una respuesta
			responseToString();
			return stringToJSONArray(result);
		} 
		else
			return null;
	}


    /**
     * Realiza la consulta al servicio web y almacena
     * la respuesta en inputStream.
     *
     * @param parameters Parametros que se van a pasar por POST
     * @param serverUrl URL del servicio al que se quiere acceder
     */
	private void httppostconnect(ArrayList<NameValuePair> parameters, String serverUrl){
		//
		try {
			HttpParams httpParameters = new BasicHttpParams();
			int timeoutConnection = 10000;
			HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
			int timeoutSocket = 10000;
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
			
			HttpClient httpclient = new DefaultHttpClient(httpParameters);
			HttpPost httppost = new HttpPost(serverUrl);
			httppost.setEntity(new UrlEncodedFormEntity(parameters));
			//ejecuto peticion enviando datos por POST
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			inputStream = entity.getContent();
        }
        catch(SocketTimeoutException e){
            error = Constantes.ERR_TIMEOUT;
            Log.e("http Error", "Socket timed out "+e.toString());
        } catch(ConnectTimeoutException e){
            error = Constantes.ERR_TIMEOUT;
            Log.e("http Error", "Conection timed out "+e.toString());
		} catch(Exception e){
            error = Constantes.ERR_UNKNOWN_HOST;
			Log.e("http Error", "Error in http connection "+e.toString());
		}
	}

    /**
     * Convierte la respuesta almacenada en inputStream
     * en un String en result.
     */
	private void responseToString() {
		//Convierte respuesta a String
		try{
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"),8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
				Log.v("readLine", " = "+line);
			}
			
			inputStream.close();

			result=sb.toString();

			Log.i("responseToString", sb.toString());
		} catch(Exception e){
			Log.e("result Error", "Error converting result "+e.toString());
		}
	}

    /**
     */
	public static JSONArray stringToJSONArray(String str) {
		try {
			JSONArray jArray = new JSONArray(str);
			return jArray;
		}
		catch(JSONException e) {
			Log.e("Parse JSON", "Error parsing data "+e.toString());
			return null;
		}
	}

}