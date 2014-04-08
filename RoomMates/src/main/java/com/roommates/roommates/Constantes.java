package com.roommates.roommates;

/**
 * Clase que encapsula constantes que se utilizan en varias partes del c&oacute;digo
 */
public class Constantes {
    // Httppostaux
    public static final int NO_ERROR = 0;
    public static final int TIMEOUT = 1;
    public static final int UNKNOWN_HOST = 2;
    public static final String DIA = "DIA";
    public static final String MES = "MES";
    public static final int DIAS = 0;
    public static final int SEMANAS = 1;
    public static final int MESES = 1;

    // URLS
    public static final String HTTP_PROTOCOL = "https://";
    public static final String HOST = "roommates-antorofdev.rhcloud.com";
    public static final String NUEVA_TAREA_URL = HTTP_PROTOCOL+HOST+"/aniadir_task_android.php";
    public static final String NUEVA_CASA_URL=HTTP_PROTOCOL+HOST+"/aniadir_apartment_android.php";
    public static final String NUEVA_FACTURA_URL = HTTP_PROTOCOL+HOST+"/aniadir_bill_android.php";
    public static final String NUEVO_PODUCTO_URL = HTTP_PROTOCOL+HOST+"/aniadir_product_android.php";
    public static final String NUEVO_COMPANERO = HTTP_PROTOCOL+HOST+"/aniadir_roommate_android.php";
    public static final String CONSULTA_CASAS_URL = HTTP_PROTOCOL+HOST+"/consultar_apartments_android.php";
    public static final String CONSULTA_FACTURAS_URL = HTTP_PROTOCOL+HOST+"/consultar_bills_android.php";
    public static final String CONSULTA_COMPRAS_URL = HTTP_PROTOCOL+HOST+"/consultar_shopping_android.php";
    public static final String CONSULTA_TAREAS_URL = HTTP_PROTOCOL+HOST+"/consultar_tasks_android.php";
    public static final String CONSULTA_COMPANEROS_URL = HTTP_PROTOCOL+HOST+"/consultar_roomates_android.php";
    public static final String LOGIN_URL = HTTP_PROTOCOL+HOST+"/login_android.php";
    public static final String REGISTER_URL = HTTP_PROTOCOL+HOST+"/registro_android.php";

}
