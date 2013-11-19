package config;

import org.jdom2.Element;

public class BarreMenusGConfig {

	private Element	racine;

	/**
	 * 
	 */
	public void config() {

		this.racine = new Element("menusG");

		final Element jeu = new Element("Jeu");
		final Element aide = new Element("Aide");
		final Element connexion = new Element("connexion");
		final Element quitter = new Element("quitter");
		final Element aPropos = new Element("apropos");

		connexion.addContent("Connexion");
		quitter.addContent("Quitter");
		aPropos.addContent("A propos");

		jeu.addContent(connexion);
		jeu.addContent(quitter);

		aide.addContent(aPropos);

		this.racine.addContent(jeu);
		this.racine.addContent(aide);

	}

	/**
	 * @return Element
	 */
	public Element toXML() {

		this.config();
		return this.racine;

	}

}
