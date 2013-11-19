package config;

import org.jdom2.Element;

public class ControllerConfig {

	private Element	racine;

	/**
	 * 
	 */
	public void config() {

		this.racine = new Element("controller");

		final Element connexion = new Element("connexion");
		final Element quitter = new Element("quitter");
		final Element aPropos = new Element("a_propos");
		final Element annuler = new Element("annuler");
		final Element seConnecter = new Element("se_connecter");
		final Element proposer = new Element("proposer");
		final Element butee = new Element("butee");
		final Element entree = new Element("entree");

		connexion.addContent("Connexion");
		quitter.addContent("Quitter");
		aPropos.addContent("A propos");
		annuler.addContent("Annuler");
		seConnecter.addContent("Se connecter");
		proposer.addContent("Proposer");
		butee.addContent("buteeAtteinte");
		entree.addContent("Entrée");

		this.racine.addContent(connexion);
		this.racine.addContent(quitter);
		this.racine.addContent(aPropos);
		this.racine.addContent(annuler);
		this.racine.addContent(seConnecter);
		this.racine.addContent(proposer);
		this.racine.addContent(butee);
		this.racine.addContent(entree);
		
	}

	/**
	 * @return Element
	 */
	public Element toXML() {

		this.config();
		return this.racine;

	}

}
