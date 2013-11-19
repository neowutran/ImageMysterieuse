package views;

//
// IUT de Nice / Departement informatique / Module APO-Java
// Annee 2012_2013 - Composants generiques
//
// Composant BarreMenusG : gestion generique de menus deroulants
//
// Edition Draft : externalisation depuis l'application FenetreSaisieTexte
//
// + Version 0.0.0 : limitee au menu "Fichier" du bloc notes Windows
// + Version 0.1.0 : integration du package SWING
// + Version 0.2.0 : ajout d'un hamecon vers le cadre d'accueil
//
// Edition A : mise en place de la gestion de plusieurs menus
//
// + Version 1.0.0 : limitee aux labels des menus, sans gestion des items
// + Version 1.1.0 : mise a niveau par rapport a V 0.2.0
// + Version 1.2.0 : gestion generique des items de menus
// + Version 1.3.0 : ajout de la methode ajouterEcouteur et retirerEcouteur
// + Version 1.4.0 : ajout des methodes validerItem et invaliderItem
//
// Edition B : mise en place du modele MVC
//
// + Version 2.0.0 : introduction du prefixe Vue et externalisation du
// demonstrateur
// + Version 2.1.0 : introduction d'un second constructeur normal et de la
// methode privee configurer
// + Version 2.2.0 : ajout de l'attribut "espion",
// + modification du second constructeur,
// + introduction de la methode privee ajouterControleur
// + suppression du premier constructeur
// + Version 2.3.0 : modification de la signature du constructeur normal
// + suppression de l'attribut espion
// + suppression de la methode ajouterEcouteur
// + suppression de la methode retirerEcouteur
// + la methode obtenirItem devient publique
// + la methode ajouterControleur devient publique
//
//
// L'attribut "menus" est un dictionnaire ordonne dont les cles sont les labels
// textuels des menus et dont les associes sont des references (au sens Java du
// terme) sur les instances de la classe Menu, a integrer a la barre principale
//
// L'attribut "items" est un dictionnaire dont les cles sont aussi les noms des
// menus et dont les associes sont eux memes des dictionnaires. Chaque sous-
// dictionnaire a pour cles les noms des items du menu cible et pour associes
// des references (au sens Java du terme) sur les instances de la classe
// JMenuItem, ajoutees au menu cible.
//
// Auteur : A. Thuaire
//

import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.LinkedHashMap;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import library.Config;

import org.jdom2.Element;

public class VueBarreMenusG extends JMenuBar {

	private static final long	serialVersionUID	= 1L;
	private JFrame				hamecon;
	private LinkedHashMap		menus;
	private LinkedHashMap		items;
	private LinkedHashMap		itemsDescription;
	private Element				config;

	// --- Constructeur normal

	public VueBarreMenusG(final JFrame hamecon, final Element config) {

		if (config == null) {
			return;
		}

		this.config = config;

		this.configurer(hamecon, config);
	}

	// --- Second constructeur normal

	public VueBarreMenusG(final JFrame hamecon, final String cheminConfig,
					final String version) {

		// Controler la validite des parametres
		//
		if (cheminConfig == null) {
			return;
		}
		if (version == null) {
			return;
		}

		// Charger le dictionnaire de configuration de premier niveau (menus)
		//
		final LinkedHashMap config = (LinkedHashMap) Config.load(cheminConfig,
						version);

		// Configurer la barre de menus
		//
		this.configurer(hamecon, config);
	}

	// --- Methode configurer

	public void ajouterControleur(final ActionListener espion) {

		// Controler l'existence de l'espion
		//
		if (espion == null) {
			return;
		}

		// Parcourir le dictionnaire de tous les items de tous les menus
		//
		final Iterator i = this.items.keySet().iterator();
		String nomMenu = null;
		LinkedHashMap associe = null;
		Iterator j = null;

		while (i.hasNext()) {

			// Acquerir le nom du menu courant
			//
			nomMenu = (String) i.next();

			// Acquerir l'associe (dictionnaire des items)
			//
			associe = (LinkedHashMap) this.items.get(nomMenu);

			// Parcourir le dictionnaire des items du menu courant
			//
			j = associe.keySet().iterator();
			String nomItem = null;
			JMenuItem item = null;

			while (j.hasNext()) {

				// Acquerir le nom de l'item courant
				//
				nomItem = (String) j.next();

				// Acquerir la reference de l'item associe
				//
				item = (JMenuItem) associe.get(nomItem);

				// Ajouter le controleur comme espion a l'item courant
				//
				if (item != null) {
					item.addActionListener(espion);
				}
			}
		}
	}

	// --- Methode configurer

	private boolean ajouterItem(final String nomMenu, final String nomItem,
					final Boolean etatInitial) {

		JMenu menuCourant = null;
		JMenuItem itemCourant = null;
		LinkedHashMap w = null;

		// Controler la validite des parametres
		//
		if (nomMenu == null) {
			return false;
		}
		if (nomItem == null) {
			return false;
		}

		// Obtenir la reference du menu cible
		//
		menuCourant = (JMenu) this.menus.get(nomMenu);
		if (menuCourant == null) {
			return false;
		}

		// Traiter le cas particulier d'une barre de separation
		//
		if (nomItem.substring(0, 1).equals("-")) {
			menuCourant.addSeparator();
			itemCourant = null;
		} else {

			// Creer et ajouter le nouvel item au menu cible
			//
			itemCourant = new JMenuItem(nomItem);
			menuCourant.add(itemCourant);

			// Traiter l'etat initial de l'item
			//
			if ((etatInitial != null) && !etatInitial.booleanValue()) {
				itemCourant.setEnabled(false);
			}
		}

		// Ajouter la reference du nouvel item dans le dictionnaire des items
		//
		w = (LinkedHashMap) this.items.get(nomMenu);
		if (w == null) {
			return false;
		}

		w.put(nomItem, itemCourant);

		return true;
	}

	// --- Methode ajouterMenu

	private boolean ajouterItems(final String nomMenu) {

		LinkedHashMap config = null;

		// Controler la validite du parametre
		//
		if (nomMenu == null) {
			return false;
		}

		// Obtenir le dictionnaire de configuration des items du menu support
		//
		config = (LinkedHashMap) this.items.get(nomMenu);
		if (config == null) {
			return false;
		}

		// Creer et ajouter chaque item au menu cible
		//
		final Iterator i = config.keySet().iterator();
		String cle = null;
		boolean ok = true;
		Boolean associe = null;

		while (i.hasNext()) {

			// Acquerir la cle courante (label de l'item)
			//
			cle = (String) i.next();

			// Acquerir l'associe (etat initial de l'item)
			//
			associe = (Boolean) config.get(cle);

			// Creer et ajouter un nouvel item au menu cible
			//
			ok = this.ajouterItem(nomMenu, cle, associe);
			if (!ok) {
				return false;
			}
		}

		return true;
	}

	// --- Methode ajouterMenusItems

	private boolean ajouterMenu(final String nomMenu, final String associe) {

		LinkedHashMap config = null;

		// Controler la validite du nom du menu
		//
		if (nomMenu == null) {
			return false;
		}

		// Controler l'absence du menu dans le dictionnaire des menus
		//
		if (this.menus.containsKey(nomMenu)) {
			return false;
		}

		// Creer et ajouter le nouveau menu a la barre de menus
		//
		final JMenu menu = new JMenu(nomMenu);
		this.add(menu);

		// Ajouter le nouveau menu dans le dictionnaire des menus
		//
		this.menus.put(nomMenu, menu);

		// Traiter le cas particulier de l'absence d'items
		//
		if (associe == null) {
			this.items.put(nomMenu, null);
			return true;
		}

		// Charger le dictionnaire de configuration des items du menu
		//
		final String cle = "Config/Menu" + nomMenu;
		config = (LinkedHashMap) Config.load(cle, associe);
		if (config == null) {
			return false;
		}

		// Memoriser la configuration chargee dans le dictionnaire des items
		//
		this.items.put(nomMenu, config);

		// Creer et ajouter tous les items au menu courant
		//
		return this.ajouterItems(nomMenu);
	}

	// --- Methode ajouterItems

	private boolean ajouterMenuItems(final String nomMenu, final String associe) {

		// Controler la validit� du nom du menu
		//
		if (nomMenu == null) {
			return false;
		}

		// Controler la validit� de l'associ� du menu
		//
		if (associe == null) {
			return false;
		}

		JMenu menu;

		// Creer et ajouter le nouveau menu a la barre de menus
		//
		if (!this.menus.containsKey(nomMenu)) {

			menu = new JMenu(nomMenu);
			this.add(menu);

			// Ajouter le menu a la liste des menus
			//
			this.menus.put(nomMenu, menu);
		}

		// Creer et ajouter le nouvel item au menu cible
		//
		menu = (JMenu) this.menus.get(nomMenu);
		final JMenuItem itemCourant = new JMenuItem(associe);
		menu.add(itemCourant);

		// Ajouter l'item a la liste des items
		//
		this.itemsDescription.put(associe, itemCourant);
		return true;
	}

	// --- Methode ajouterItem

	private void configurer(final JFrame hamecon, final Element config) {

		// Memoriser la reference vers le cadre d'accueil
		//
		this.hamecon = hamecon;

		// Accrocher la barre a son cadre d'accueil
		//
		hamecon.setJMenuBar(this);

		// Creer le dictionnaire interne des menus
		//
		this.menus = new LinkedHashMap();

		// Creer le dictionnaire internes des items de menus
		//
		this.items = new LinkedHashMap();

		// Creer le dictionnaire de description des items de menus
		//
		this.itemsDescription = new LinkedHashMap();

		// Parcourir tout le dictionnaire de configuration des menus
		//
		final Iterator i = config.getChild("menusG").getChildren().iterator();

		String cle = null;
		String associe = null;
		boolean ok = true;

		while (i.hasNext()) {

			// Acquerir la cle courante
			//
			final Element cleElem = (Element) i.next();
			cle = cleElem.getName();

			// Acquerir les items associ�s
			//
			final Iterator i2 = cleElem.getChildren().iterator();

			while (i2.hasNext()) {

				// Acquerir l'associ� courant
				//
				final Element associeElem = (Element) i2.next();
				associe = cleElem.getChildText(associeElem.getName());
				// Ajouter les menus et les items
				//
				ok = this.ajouterMenuItems(cle, associe);
			}

			if (!ok) {
				return;
			}
		}
		this.items.put(cle, this.itemsDescription);
	}

	// --- Methode obtenirItem

	private void configurer(final JFrame hamecon, final LinkedHashMap config) {

		// Memoriser la reference vers le cadre d'accueil
		//
		this.hamecon = hamecon;

		// Accrocher la barre a son cadre d'accueil
		//
		hamecon.setJMenuBar(this);

		// Creer le dictionnaire interne des menus
		//
		this.menus = new LinkedHashMap();

		// Creer le dictionnaire internes des items de menus
		//
		this.items = new LinkedHashMap();

		// Parcourir tout le dictionnaire de configuration des menus
		//
		final Iterator i = config.keySet().iterator();
		String cle = null;
		String associe = null;
		boolean ok = true;

		while (i.hasNext()) {

			// Acquerir la cle courante
			//
			cle = (String) i.next();

			// Acquerir l'associe courant
			//
			associe = (String) config.get(cle);

			// Creer et ajouter le nouveau menu a la barre de menus
			//
			ok = this.ajouterMenu(cle, associe);
			if (!ok) {
				return;
			}
		}
	}

	// --- Methode validerItem

	public boolean invaliderItem(final String nomMenu, final String nomItem) {

		// Controler la validite des parametres
		//
		if (nomMenu == null) {
			return false;
		}
		if (nomItem == null) {
			return false;
		}

		// Obtenir la reference de l'item cible
		//
		final JMenuItem itemCible = this.obtenirItem(nomMenu, nomItem);
		if (itemCible == null) {
			return false;
		}

		// Desactiver l'item cible
		//
		itemCible.setEnabled(false);
		return true;
	}

	// --- Methode invaliderItem

	public JMenuItem obtenirItem(final String nomMenu, final String nomItem) {

		// Controler la validite des parametres
		//
		if (nomMenu == null) {
			return null;
		}
		if (nomItem == null) {
			return null;
		}

		// Obtenir le dictionnaire des items du menu cible
		//
		final LinkedHashMap w = (LinkedHashMap) this.items.get(nomMenu);
		if (w == null) {
			return null;
		}

		// Obtenir la reference de l'item cible
		//
		return (JMenuItem) w.get(nomItem);
	}

	// --- Methode ajouterControleur

	public boolean validerItem(final String nomMenu, final String nomItem) {

		// Controler la validite des parametres
		//
		if (nomMenu == null) {
			return false;
		}
		if (nomItem == null) {
			return false;
		}

		// Obtenir la reference de l'item cible
		//
		final JMenuItem itemCible = this.obtenirItem(nomMenu, nomItem);
		if (itemCible == null) {
			return false;
		}

		// Activer l'item cible
		//
		itemCible.setEnabled(true);
		return true;
	}
}
