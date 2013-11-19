package config;

import java.io.FileOutputStream;
import java.io.IOException;

import org.jdom2.Element;
import org.jdom2.output.XMLOutputter;

/**
 * @author sephiroth
 */
public class GenerateXML {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(final String[] args) {

		final GenerateXML exemple = new GenerateXML();
		exemple.setPathFile(args[1]);
		exemple.genererXML();

	}

	private final Element	racine;

	private String			pathFile;

	/**
	 */
	public GenerateXML() {

		this.racine = new Element("config");

	}

	/**
	 * @param config
	 */
	public GenerateXML(final Element config) {

		this.racine = config;

	}

	/**
	 * @throws IOException 
	 * 
	 */

	public void genererXML() {

		/*
		 * EPIC SHIT HERE
		 */
		this.racine.addContent((new TempsGConfig()).toXML());
		this.racine.addContent((new BarreMenusGConfig()).toXML());
		try {
			this.racine.addContent((new FenetreCoConfig()).toXML());
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.racine.addContent((new FenetrePropConfig()).toXML());
		try {
			this.racine.addContent((new InfosConfig()).toXML());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			this.racine.addContent((new ImageConfig()).toXML());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.racine.addContent((new ControllerConfig()).toXML());
		this.sauvegarderXML();

	}

	/**
	 * @return String
	 */
	public String getPathFile() {

		return this.pathFile;

	}

	/**
	 * 
	 */
	public void sauvegarderXML() {

		try {

			final XMLOutputter sortie = new XMLOutputter(
							org.jdom2.output.Format.getPrettyFormat());
			sortie.output(this.racine, new FileOutputStream(this.pathFile));

		} catch (final java.io.IOException e) {

			System.out.println("Shit: " + e.getMessage());

		}

	}

	/**
	 * @param pathFile
	 */
	public void setPathFile(final String pathFile) {

		this.pathFile = pathFile;

	}
}
