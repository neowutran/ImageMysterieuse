package config;

import org.jdom2.Element;

public class FenetrePropConfig {

	private Element	racine;

	/**
	 * 
	 */
	public void config() {

		this.racine = new Element("fenetreProp");

		final Element width = new Element("width");
		final Element height = new Element("height");
		final Element size = new Element("size");
		final Element labelPseudo = new Element("label");
		final Element labelTour = new Element("labelTour");
		final Element colonneText = new Element("colonne");
		final Element text = new Element("text");
		final Element boutonProposition = new Element("proposer");

		width.addContent("450");
		height.addContent("130");
		size.addContent(width);
		size.addContent(height);

		labelPseudo.addContent("Proposition :   ");
		labelTour.addContent("C'est votre tour !");

		colonneText.addContent("20");
		text.addContent(colonneText);

		boutonProposition.addContent("Proposer");

		this.racine.addContent(size);
		this.racine.addContent(labelPseudo);
		this.racine.addContent(labelTour);
		this.racine.addContent(text);
		this.racine.addContent(boutonProposition);

	}

	/**
	 * @return Element
	 */
	public Element toXML() {

		this.config();
		return this.racine;

	}

}
