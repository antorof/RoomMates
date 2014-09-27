package com.roommates.roommates;

import static java.lang.Integer.parseInt;

public class Utilities {

    /**
     * Calcula el contraste YIQ de un color para determinar el color del texto.
     * Si el contraste YIQ resulta mayor o igual que 128 el color utilizado en el texto deber&iacute;
     * ser el negro; en caso contrario, el blanco.
     * @param hexcolor Color en hexadecimal
     * @return Contraste YIQ
     */
    public static int getContrastYIQ(String hexcolor){
        hexcolor = hexcolor.contains("#") ? hexcolor.substring(1) : hexcolor;
        hexcolor = hexcolor.length() == 6 ? hexcolor : hexcolor.substring(2);

        int r = parseInt(hexcolor.substring(0,2),16);
        int g = parseInt(hexcolor.substring(2,4),16);
        int b = parseInt(hexcolor.substring(4,6),16);
        int yiq = ((r*299)+(g*587)+(b*114))/1000;
        return yiq;
    }
}
