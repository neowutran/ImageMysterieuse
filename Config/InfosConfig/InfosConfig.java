package config;

import java.awt.GridLayout;
import java.io.IOException;

import library.ObjectString;

import org.jdom2.Element;

public class InfosConfig {

	private Element	racine;

	/**
	 * @throws IOException
	 */
	public void config() throws IOException {

		this.racine = new Element("infos");

		final Element placement = new Element("placement");
		final Element pseudo = new Element("pseudo");
		final Element theme = new Element("theme");
		final Element score = new Element("score");
		final Element separateur = new Element("separateur");

		placement.addContent(ObjectString.toString(new GridLayout(1, 4)));

		pseudo.addContent("Pseudo :");
		theme.addContent("Theme :");
		score.addContent("Score :");
		separateur.addContent("-----");

		this.racine.addContent(placement);
		this.racine.addContent(pseudo);
		this.racine.addContent(theme);
		this.racine.addContent(score);
		this.racine.addContent(separateur);
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