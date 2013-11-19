package config;

import java.awt.GridLayout;
import java.io.IOException;

import library.ObjectString;

import org.jdom2.Element;

public class FenetreCoConfig {

	private Element	racine;

	/**
	 * @throws IOException
	 */
	public void config() throws IOException {

		this.racine = new Element("fenetreCo");

		final Element width = new Element("width");
		final Element height = new Element("height");
		final Element size = new Element("size");
		final Element labelPseudo = new Element("labelPseudo");
		final Element labelServeur = new Element("labelServeur");
		final Element labelPort = new Element("labelPort");
		final Element placement = new Element("placement");
		final Element ligneText = new Element("ligne");
		final Element colonneText = new Element("colonne");
		final Element text = new Element("text");
		final Element boutonConnexion = new Element("connexion");
		final Element boutonAnnuler = new Element("annuler");

		placement.addContent(ObjectString.toString(new GridLayout(6, 1)));

		width.addContent("450");
		height.addContent("240");
		size.addContent(width);
		size.addContent(height);

		labelPseudo.addContent("Entrez un pseudonyme : ");
		labelServeur.addContent("Entrez l'IP du serveur : ");
		labelPort.addContent("Entrez le port de connexion : ");

		ligneText.addContent("1");
		colonneText.addContent("10");
		text.addContent(ligneText);
		text.addContent(colonneText);

		boutonConnexion.addContent("Se connecter");
		boutonAnnuler.addContent("Annuler");

		this.racine.addContent(placement);
		this.racine.addContent(size);
		this.racine.addContent(labelPseudo);
		this.racine.addContent(labelServeur);
		this.racine.addContent(labelPort);
		this.racine.addContent(text);
		this.racine.addContent(boutonConnexion);
		this.racine.addContent(boutonAnnuler);

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
