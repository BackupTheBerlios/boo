/*
 * Copyright © Domenico Carbotta, 2005.
 * Code released under the GNU General Public License.
 */

package boo.util;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Classe non istanziabile. Contiene funzioni utilizzate in varie parti del
 * codice, o comunque non relative ad un particolare aspetto del programma.
 */
public class Utilities {

    private Utilities() {} // classe non istanziabile

    /**
     * Restituisce un nuovo array (la <i>coda</i>) contenente tutti gli
     * elementi dell'array passato come parametro tranne il primo.
     * 
     * @param array
     *            Array di cui estrarre la coda.
     * @return Coda dell'array.
     */
    public static Object[] extractTail(Object array[]) {
        int n = array.length;
        Object tail[] = new Object[n - 1];
        System.arraycopy(array, 1, tail, 0, n - 1);
        return tail;
    }
    
    /**
     * Restituisce il passcode coddispondente alla password e al salt passati
     * come parametro. Il passcode il risultato dell'applicazione dell'algoritmo
     * di digest SHA alla concatenazione tra la password e il salt.
     * Il salt è  stringa casuale generata e trasmessa dal server per ogni
     * richiesta di login, e valida solo per il tentativo di login immediatamente
     * successivo.
     * L'algoritmo utilizzato consente di ottenere un livello di sicurezza tal
     * da prevenire almeno gli attacchi più banali.
     * 
     * @param password
     * 			La password dell'utente.
     * @param salt
     * 			Il salt casuale.
     * @return Il passcode corrispondente.
     */
    public static byte[] passwordToPasscode(String password, byte[] salt) {
    	String digestAlgorithm = "SHA";
    	String encoding = "UTF-8";
    	try {
    		MessageDigest md = MessageDigest.getInstance(digestAlgorithm);
    		md.update(password.getBytes(encoding));
    		md.update(salt);
    		return md.digest();
    	} catch (NoSuchAlgorithmException _) {
    		return null;
    	} catch (UnsupportedEncodingException _) {
    		return null;
    	}
    }
    
    public static byte[] longToByteArray(long n) {
    	return BigInteger.valueOf(n).toByteArray();
    }
    
    public static String reprByteArray(byte[] a) {
    	return new BigInteger(a).toString();
    }

}
