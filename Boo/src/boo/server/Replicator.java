/*
 * Copyright © Domenico Carbotta, 2005.
 * Code released under the GNU General Public License.
 */

package boo.server;

import java.io.File;
import java.io.FileNotFoundException;

import org.tigris.subversion.javahl.ClientException;
import org.tigris.subversion.javahl.Revision;
import org.tigris.subversion.javahl.SVNClient;

/**
 * Sincronizza tramite Subversion una directory locale rispetto ad un repository
 * remoto.
 */
public class Replicator {

    SVNClient client;
    String workingCopy;

    /**
     * Costruttore. Un banalissimo costruttore. Non scocciatelo.
     * 
     * @param workingCopy
     *            Directory in cui risiede la working copy.
     */
    public Replicator(String workingCopy) {
        this.workingCopy = workingCopy;
        client = new SVNClient();
        System.out.println(SVNClient.version());
    }

    /**
     * Effettua il checkout di un repository remoto usando le credenziali
     * specificate.
     * 
     * @param repository
     *            URL del repository.
     * @param username
     *            Nome utente sul server Subversion.
     * @param password
     *            Password dell'utente sul server Subversion.
     */
    public long checkout(String repository, String username, String password)
            throws ClientException {
        client.username(username);
        client.password(password);
        return client.checkout(repository, workingCopy, Revision.HEAD, true);
    }

    /**
     * Aggiorna la working copy.
     * 
     * @throws ClientException
     *             Eccezione sollevata dalla libreria di collegamento a
     *             Subversion.
     * @throws FileNotFoundException
     *             Eccezione sollevata nel caso il percorso della working copy
     *             non esista.
     */
    public long update() throws ClientException, FileNotFoundException {
        File f = new File(workingCopy);
        if (!f.exists()) throw new FileNotFoundException();
        return client.update(workingCopy, Revision.HEAD, true);
    }

    public static void main(String[] args) throws Exception {
        System.out.println(System.getProperty("java.library.path"));
        Replicator r = new Replicator(
                "/Users/Domenico/Polimi/IngSw/Eclipse Workspace/Boo");
        long l = r.update();
        System.out.println(l);
    }

}
