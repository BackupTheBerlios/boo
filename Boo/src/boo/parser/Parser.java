/*
 * Copyright © Domenico Carbotta, 2005. Code released under the GNU General
 * Public License.
 */

package boo.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import boo.tree.BlockDescription;
import boo.tree.BlockTreeNode;

/**
 * Parser che costruisce l'albero dei blocchi corrispondente al file.
 */
public class Parser {

    /**
     * Lexer utilizzato per effettuare la scansione delle righe.
     */
    protected Lexer lexer;

    /**
     * Nome da assegnare al nodo radice.
     */
    protected String rootName;

    /**
     * Eccezione sollevata dal parser in caso di errore di sintassi.
     */
    public class SyntaxError extends Exception {}

    /**
     * Costruttore... che non ha nulla di strano.
     * 
     * @param lexer
     *            Lexer da utilizzare per la scansione del file.
     * @param rootName
     *            Nome da assegnare al nodo radice.
     */
    public Parser(Lexer lexer, String rootName) {
        this.lexer = lexer;
        this.rootName = rootName;
    }

    /**
     * Effettua il parsing del file passato come parametro.
     * 
     * @param file
     *            Il file di cui effettuare il parsing.
     * @return Nodo radice dell'albero risultante dal parsing del file.
     * @throws IOException
     *             Eccezione sollevata dalle operazioni di lettura da file.
     * @throws SyntaxError
     *             Errore di sintassi nel file indicato.
     */
    public synchronized BlockTreeNode parse(File fileName) throws IOException,
            SyntaxError {
        BufferedReader source = new BufferedReader(new FileReader(fileName));
        return parse(source);
    }

    /**
     * Effettua il parsing di quanto &egrave; contenuto nel BufferedReader
     * passato come parametro.
     * 
     * @param source
     *            Testo di cui effettuare il parsing.
     * @return Nodo radice dell'albero risultante dal parsing del file.
     * @throws IOException
     *             Eccezione sollevata dalle operazioni di lettura da file.
     * @throws SyntaxError
     *             Errore di sintassi nel file indicato.
     */
    public synchronized BlockTreeNode parse(BufferedReader source)
            throws IOException, SyntaxError {
        BlockDescription fileBlock = new BlockDescription(rootName, 0, -1);
        BlockTreeNode root = new BlockTreeNode(fileBlock, null);
        BlockTreeNode treePtr = root;
        String newLine;
        int n = 0;
        while (true) {
            newLine = source.readLine();
            if (newLine == null) break;
            Token token = lexer.matchLine(newLine, n);
            switch (token.getType()) {
                case Token.TEXT:
                    break;

                case Token.END:
                    if (treePtr.getParent() == null) throw new SyntaxError();
                    treePtr.getValue().setEndLine(n - 1);
                    treePtr = (BlockTreeNode) treePtr.getParent();
                    break;

                case Token.BEGIN:
                    BlockDescription b = new BlockDescription(token
                            .getContents(), n, -1);
                    BlockTreeNode t = new BlockTreeNode(b, treePtr);
                    treePtr.addChild(t);
                    treePtr = t;
                    break;
            }
            n += 1;
        }
        if (treePtr != root) throw new SyntaxError();
        root.getValue().setEndLine(n);
        return root;
    }

    public static void main(String args[]) throws IOException, SyntaxError {
        BufferedReader f = new BufferedReader(new InputStreamReader(ClassLoader
                .getSystemResourceAsStream("boo/test/Prova.txt")));
        Parser p = new Parser(new Lexer(), "prova.txt");
        BlockTreeNode t = p.parse(f);
        System.out.println("Esegui questo modulo in modalitˆ debug "
                + "ed ispeziona il contenuto della variabile t.");
    }

}
