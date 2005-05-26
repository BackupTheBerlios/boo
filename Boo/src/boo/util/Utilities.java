/*
 * Copyright © Domenico Carbotta, 2005.
 * Code released under the GNU General Public License.
 */

package boo.util;

/**
 * Classe non istanziabile. Contiene funzioni utilizzate in varie parti del
 * codice, o comunque non relative ad un particolare aspetto del programma.
 */
public class Utilities {

    private Utilities() {} // classe non istanziabile

    /**
     * Restituisce un nuovo array (la <i>coda </i>) contenente tutti gli
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

}
