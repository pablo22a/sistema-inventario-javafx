package mx.unison.util;

import java.security.MessageDigest;

/**
 * Clase para operaciones criptográficas simples.
 *
 * Actualmente contiene el método para generar hash MD5
 * de una cadena de texto.
 */
public class CryptoUtils {

    /**
     * Genera el hash MD5 de una cadena de texto.
     *
     * @param input Texto de entrada a convertir en hash.
     * @return Hash MD5 en formato hexadecimal.
     */
    public static String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(input.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
