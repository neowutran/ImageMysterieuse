package config;

import java.awt.Color;
import java.awt.GridLayout;
import java.io.IOException;

import library.ObjectString;

import org.jdom2.Element;

public class ImageConfig {

	private Element	racine;

	/**
	 * @throws IOException
	 */
	public void config() throws IOException {

		this.racine = new Element("images");

		final Element fond = new Element("couleurFond");
		final Element type = new Element("type");

		fond.addContent(ObjectString.toString(Color.black));
		type.addContent("jpg");
		
		this.racine.addContent(fond);
		this.racine.addContent(type);
	}

	/**
	 * @return Element
	 * @throws IOException
	 */
	public Element toXML() throws IOException {

		this.config();
		return this.racine;

	}

}
