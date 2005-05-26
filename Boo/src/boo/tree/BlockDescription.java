/*
 * Copyright © Domenico Carbotta, 2005.
 * Code released under the GNU General Public License.
 */

package boo.tree;

/**
 * Descrizione di un blocco del file sottoposto a controllo di accesso da
 * CollabEdit. Vengono indicati la riga di inizio e fine, il nome del
 * blocco, lo stato del nodo ed eventualmente l'identificativo dell'utente
 * che sta direttamente bloccando il nodo.
 */
public class BlockDescription {
	
	public static final int STATUS_FREE = 1;
	public static final int STATUS_LOCKED = 2;
	public static final int STATUS_CHILD_IS_LOCKED = 3;
	public static final int STATUS_PARENT_IS_LOCKED = 4;
	
	protected String name;
	protected int startLine;
	protected int endLine;
	protected String lockedBy;
	protected int status;
	
	/**
	 * Costruisce la descrizione di un blocco.
	 * @param name Nome del blocco.
	 * @param startLine Numero della riga in cui inizia il blocco.
	 * @param endLine Numero della riga in cui finisce il blocco.
	 */
	public BlockDescription(String name, int startLine, int endLine) {
		this.name = name;
		this.startLine = startLine;
		this.endLine = endLine;
		this.lockedBy = null;
	}
	
	public String getName() {
		return name;
	}
	
	public int getStartLine() {
		return startLine;
	}
	
	public void setEndLine(int endLine) {
		this.endLine = endLine;
	}
	public int getEndLine() {
		return endLine;
	}
	
	public String getLockedBy() {
		return lockedBy;
	}
	
	public boolean isLocked() {
		return (lockedBy == null);
	}
	
	public void setLockedBy(String lockedBy) {
		this.lockedBy = lockedBy;
	}
	
	public void setUnlocked() {
		lockedBy = null;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
	
	public int getStatus() {
		return status;
	}
	
	public String toString() {
		String res = "Block(\"" + name;
		res += "\", lines " + startLine + " to " + endLine;
		if (lockedBy != null)
			res += ", locked by " + lockedBy;
		res += ")";
		return res;
	}
	
	public static void main(String args[]) {
		BlockDescription b = new BlockDescription(
				"/proj/ciccio/pippo.java::sez1::ciccio",
				0, 10);
		System.out.println(b.toString());
	}
	
}
