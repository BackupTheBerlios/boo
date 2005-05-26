/*
 * Copyright © Domenico Carbotta, 2005.
 * Code released under the GNU General Public License.
 */

package boo.tree;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.tree.TreeNode;

import boo.util.Utilities;

/**
 * Nodo dell'albero che rappresenta la struttura del documento.
 * Contiene un riferimento al nodo padre, la descrizione del blocco
 * a cui &egrave; associato e un vettore contenente i nodi figli.
 */
public class BlockTreeNode implements TreeNode {
	
	protected BlockTreeNode parent;
	protected BlockDescription value;
	protected Vector children;
	
	/**
	 * Come fanno i programmatori seri a documentare un costruttore?
	 * @param value Descrittore del blocco indicato dal nodo
	 * @param parent Nodo padre. Per indicare il nodo radice dell'albero,
	 * settare questo parametro a <code>null</code>.
	 */
	public BlockTreeNode(BlockDescription value, BlockTreeNode parent) {
		this.value = value;
		this.parent = parent;
		this.children = new Vector();
	}
	
	// implementazione dei metodi di javax.swing.tree.TreeNode
	
	/**
	 * Restituisce il nodo padre.
	 */
	public TreeNode getParent() {
		return parent;
	}
	
	/**
	 * Restituisce una <code>Enumeration</code> sui nodi figli.
	 */
	public Enumeration children() {
		return children.elements();
	}
	
	/**
	 * Restituisce sempre <code>true</code>.
	 */
	public boolean getAllowsChildren() {
		return true;
	}
	
	/**
	 * Restituisce il nodo figlio alla posizione specificata.
	 */
	public TreeNode getChildAt(int pos) {
		return (TreeNode) children.elementAt(pos);
	}
	
	/**
	 * Restituisce il numero di nodi figlio presenti.
	 */
	public int getChildCount() {
		return children.size();
	}
	
	/**
	 * Restituisce l'indice del nodo figlio passato come parametro.
	 */
	public int getIndex(TreeNode node) {
		return children.indexOf(node);
	}
	
	/**
	 * Restituisce sempre <code>false</code>.
	 */
	public boolean isLeaf() {
		return false;
	}
	
	// metodi specifici
	
	/**
	 * Aggiunge un nodo figlio. Perch? la struttura dell'albero rimanga
	 * coerente, controllare che l'inserimento di questo nodo non crei
	 * anomalie (niente cicli) e che dichiari correttamente <code>this</code>
	 * come suo nodo padre. <br>
	 * Precondizioni: <br>
	 * <code>newChild</code> non ? ancestor di <code>this</code>; <br>
	 * <code>newChild</code> non ? nodo figlio di altri nodi; <br>
	 * <code>newChild.parent</code> ? <code>this</code>.
	 */
	public boolean addChild(BlockTreeNode newChild) {
		return children.add(newChild);
	}
	
	/**
	 * Restituisce la <code>BlockDescription</code> associata a questo nodo.
	 */
	public BlockDescription getValue() {
		return value;
	}
	
	/**
	 * Trova il nodo figlio corrispondente al percorso indicato.
	 * Ad esempio, nella struttura
	 * <blockquote><pre>
	 * + prova.java
	 * |--+ sezioneA
	 * |  |--- sezioneB
	 * |  |--+ sezione C
	 * |  |  +--- sezione D
	 * |  +--- sezioneE
	 * +--- sezioneF
	 * </pre></blockquote>
	 * richiamando il metodo <code>find(new String[]
	 * {"sezioneA", "sezioneC", "sezioneD"}) </code> dell'oggetto
	 * corrispondente al nodo chiamato prova.java viene restituito il nodo
	 * sezioneD; richiamando il metodo <code>find(new String[] {"sezioneD"})
	 * </code> dello stesso nodo verr&agrave; restituito <code>null</code>.
	 * @param nodePath Percorso completo del nodo da cercare relativo al nodo
	 * <code>this</code>.
	 * @return Un riferimento al nodo corrispondente al percorso richiesto;
	 * <code>null</code> se non esiste un nodo che soddisfa la richiesta.
	 */
	public BlockTreeNode find(String nodePath[]) {
		String nextNode = nodePath[0];
		String tail[] = (String[]) Utilities.extractTail(nodePath);
		Iterator i = children.iterator();
		BlockTreeNode treePtr;
		while (i.hasNext()) {
			treePtr = (BlockTreeNode) i.next();
			if (treePtr.getValue().getName() == nextNode)
				if (tail.length != 0)
					return treePtr.find(tail);
				else
					return treePtr;
		}
		return null;
	}
	
	/**
	 * Effettua il lock di questo nodo e ne segnala il locking ai nodi figli
	 * e ai nodi genitori.
	 */
	public void lockMe() {
	    value.setStatus(BlockDescription.STATUS_LOCKED);
	    Iterator i = children.iterator();
		BlockTreeNode treePtr;
		while (i.hasNext()) {
			treePtr = (BlockTreeNode) i.next();
			treePtr.parentGotLocked();
		}
		if (parent != null) {
	        parent.childGotLocked();
	    }
	}	
	
	/**
	 * Metodo invocato quando un nodo genitore &egrave; stato bloccato.
	 * Segnala il blocco indotto di questo nodo e propaga l'informazione a
	 * tutti i nodi figli.
	 */
	public void parentGotLocked() {
	    value.setStatus(BlockDescription.STATUS_PARENT_IS_LOCKED);
	    Iterator i = children.iterator();
		BlockTreeNode treePtr;
		while (i.hasNext()) {
			treePtr = (BlockTreeNode) i.next();
			treePtr.parentGotLocked();
		}
	}
	
	/**
	 * Metodo invocato quando un nodo figlio &egrave; stato bloccato.
	 * Segnala il blocco parziale di questo nodo e propaga l'informazione
	 * al nodo padre.
	 */
	public void childGotLocked() {
	    value.setStatus(BlockDescription.STATUS_CHILD_IS_LOCKED);
	    if (parent != null) {
	        parent.childGotLocked();
	    }	
	}
	
	/**
	 * Sblocca questo nodo e segnala l'informazione a tutti i nodi figli ed
	 * al nodo padre.
	 * Questo metodo deve essere invocato solo per l'oggetto che rappresenta
	 * il nodo che &egrave; stato effettivamente bloccato dall'utente.
	 */
	public void unlockMe() {
		if (value.getStatus() != BlockDescription.STATUS_LOCKED)
			return;
	    value.setStatus(BlockDescription.STATUS_FREE);
	    Iterator i = children.iterator();
		BlockTreeNode treePtr;
		while (i.hasNext()) {
			treePtr = (BlockTreeNode) i.next();
			treePtr.parentGotUnlocked();
		}
		if (parent != null) {
	        parent.childGotUnlocked();
	    }
	}	
	
	/**
	 * Metodo invocato quando un nodo genitore, precedentemente bloccato,
	 * viene sbloccato.
	 * Sblocca questo nodo e segnala a tutti i nodi figli di sbloccarsi.
	 */
	public void parentGotUnlocked() {
	    value.setStatus(BlockDescription.STATUS_FREE);
	    Iterator i = children.iterator();
		BlockTreeNode treePtr;
		while (i.hasNext()) {
			treePtr = (BlockTreeNode) i.next();
			treePtr.parentGotUnlocked();
		}
	}
	
	/**
	 * Metodo invocato quando un nodo figlio, precedentemente bloccato,
	 * viene sbloccato.
	 * Se tutti i nodi figli sono liberi, annulla il blocco parziale di questo
	 * nodo e segnala la nuova situazione al nodo padre, invocando lo stesso
	 * metodo.
	 */
	public void childGotUnlocked() {
	    Iterator i = children.iterator();
		BlockTreeNode treePtr;
		while (i.hasNext()) {
			treePtr = (BlockTreeNode) i.next();
			if (treePtr.value.getStatus() != BlockDescription.STATUS_FREE)
			    return;
		}
		value.setStatus(BlockDescription.STATUS_FREE);
	    if (parent != null) {
	        parent.childGotLocked();
	    }
	}
	
	
}
