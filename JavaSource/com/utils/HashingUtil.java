package com.utils;

import org.mindrot.jbcrypt.BCrypt;

public class HashingUtil {
	
	 /**
     * Número de rondas de hashing (a mayor número, más seguro pero más lento).
     */
    private static final int ROUNDS = 12;

    /**
     * Genera un hash de la contraseña dada.
     *
     * @param password La contraseña que se va a hashear.
     * @return El hash de la contraseña generada.
     */
    public static String hash(String password) {
        String salt = BCrypt.gensalt(ROUNDS);
        return BCrypt.hashpw(password, salt);
    }

    /**
     * Verifica si la contraseña dada coincide con el hash almacenado.
     *
     * @param password     La contraseña que se va a verificar.
     * @param storedHash   El hash almacenado contra el cual se va a verificar la contraseña.
     * @return True si la contraseña coincide con el hash almacenado, de lo contrario False.
     */
    public static boolean verify(String password, String storedHash) {
        return BCrypt.checkpw(password, storedHash);
    }
	
}
