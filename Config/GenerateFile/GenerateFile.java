package config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Cette classe va copier la carte contenu dans le jar ou le dossier de
 * ressources vers un autre emplacement
 * 
 * @author Cibert Gauthier
 * @author Martini Didier
 * @author Responsable: Martini Didier
 * @version 3.0
 */
public class GenerateFile {

	private String				destination;
	private InputStream			sources;
	private static final int	TAILLEBUFFER	= 1024;

	/**
	 * Copie le fichier
	 * 
	 * @param destination
	 */
	public void copyfile(final String destination, final InputStream sources) {

		this.setDestination(destination);
		this.setSource(sources);

		try {

			final File f2 = new File(destination);

			final OutputStream out = new FileOutputStream(f2);

			final byte[] buf = new byte[GenerateFile.TAILLEBUFFER];
			int len;
			while ((len = this.sources.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			this.sources.close();
			out.close();
		} catch (final FileNotFoundException ex) {
			System.out.println(ex.getMessage());
		} catch (final IOException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * @return the destination
	 */
	public String getDestination() {

		return this.destination;
	}

	/**
	 * @return InputStream
	 */
	public InputStream getSources() {

		return this.sources;
	}

	/**
	 * @param destination
	 *            the destination to set
	 */
	public void setDestination(final String destination) {

		this.destination = destination;
	}

	/**
	 * @param sources
	 */
	public void setSource(final InputStream sources) {

		this.sources = sources;

	}

}
