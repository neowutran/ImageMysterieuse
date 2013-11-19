//
// IUT de Nice / Departement informatique / Module APO-Java
// Annee 2011_2012 - Composants generiques
//
// Classe ControleurTempsG : partie Controleur (MVC) d'un compteur/
// decompteur de temps
//
// Edition A : controle et pilotage externes
//
// + Version 1.0.0 : report des besoins des demonstrateurs V 2.X.0
// + Version 1.1.0 : introduction des methodes publiques demarrer,
// attendre et arreter
// + Version 1.2.0 : introduction nouvelle signature de demarrer
//
// Edition B : identification et choix de la vue par parametre de
// configuration et reflexivite
//
// + Version 2.0.0 : organisation du dictionnaire de configuration a
// deux niveaux (fonctionnel et vue) pour avoir
// la possibilite de ne pas creer de vue
// + Version 2.1.0 : creation de la vue a partir du nom de classe
// fourni par parametre de configuration
// + Version 2.2.0 : introduction methodes publiques presenceVue
// et ajouterObservateur
// + Version 2.3.0 : introduction d'un parametre de configuration
// pour designer de fa�on symbolique l'instance
// courante de TempsG
// (couverture du besoin d'une application qui
// exploite en parallele plusieurs instances du
// composant TempsG)
// + ajout accesseur public getNomInstance
// + Version 2.4.0 : introduction d'un parametre de configuration
// pour modifier la priorite par defaut (P= 4)
// du thread qui execute le modele
// + ajout accesseur public getPrioriteInstance
// + correction bug dans la methode demarrer
// (cf S3D - 2011_2012 - Premiere signature)
//
// Auteur : A. Thuaire
//
package controllers;

import java.lang.reflect.Constructor;
import java.util.Observer;

import models.ModeleTempsG;

import org.jdom2.Element;

import views.VueTempsG;

public class ControleurTempsG {

	public static void attendre(final int duree) throws Throwable {

		// Controler la validite du parametre
		//
		if (duree < 0) {
			System.out.println("pb");
			throw new Throwable("-3.4");
		}

		// Suspendre le thread courant
		//
		try {
			Thread.sleep(duree);
		} catch (final InterruptedException e) {
			System.out.println(e.getMessage());
		}
	}

	private final Element		config;
	private Observer			vue;
	private models.ModeleTempsG	modele;
	private String				mode;
	private final String		nomInstance;

	// --- Constructeur normal

	private final Integer		prioriteInstance;

	// --- Methode getTempsCourant

	/**
	 * @param hamecon
	 * @param config
	 * @throws Throwable
	 */
	public ControleurTempsG(final Object hamecon, final Element config)
					throws Throwable {

		// Memoriser la configuration courante en attribut
		//
		this.config = config;

		// Extraire et memoriser le mode de fonctionnement
		//
		this.mode = config.getChild("tempsG").getChildText("mode");
		if (this.mode == null) {
			this.mode = "horloge";
		}

		// Controler la validite du mode cible
		//
		final boolean p1 = this.mode.equals("horloge");
		final boolean p2 = this.mode.equals("chronometre");
		final boolean p3 = this.mode.equals("sablier");

		if (!p1 && !p2 && !p3) {
			System.out.println("bug");
			throw new Throwable("-3.1");
		}

		// Creer une vue configuree
		//
		this.vue = this.construireVue(hamecon, config);
		if (this.vue == null) {

			System.out.println("PBb");

		}
		// Extraire du dictionnaire de configuration le nom symbolique
		// eventuel du composant TempsG (cas d'une application qui
		// exploite en parallele plusieurs instances du composant)
		//
		this.nomInstance = config.getChild("tempsG")
						.getChildText("nomInstance");

		// Extraire du dictionnaire de configuration la priorite du
		// thread qui execute le modele
		//
		if (config.getChild("tempsG").getChildText("prioriteInstance") == null) {
			this.prioriteInstance = null;
		} else {
			this.prioriteInstance = Integer.valueOf(config.getChild("tempsG")

			.getChildText("prioriteInstance"));
		}
	}

	// --- Methode getNomInstance

	/**
	 * @param obs
	 * @return Observer
	 */
	public boolean ajouterObservateur(final Observer obs) {

		// Controler la validite du parametre
		//
		if (obs == null) {
			return false;
		}

		// Ajouter le parametre comme observateur du modele
		//
		this.modele.addObserver(obs);
		return true;
	}

	/**
	 * 
	 */
	public void arreter() {

		this.modele.resetStatus();
	}

	// --- Methode getPrioriteInstance

	private Observer construireVue(final Object hamecon, final Element config)
					throws Throwable {

		// Extraire l'identificateur de la classe de la vue
		//
		final String classeVue = config.getChild("tempsG").getChildText(
						"classeVue");
		if (classeVue == null) {
			return null;
		}

		// Creer la vue cible par usage de la reflexivite
		//
		final Class cible = Class.forName(classeVue);
		final Constructor[] constructeurs = cible.getDeclaredConstructors();
		return (Observer) constructeurs[0].newInstance(hamecon, config);
	}

	// --- Methode arreter

	/**
	 * @throws Throwable
	 */
	public void demarrer() throws Throwable {

		// Creer le modele configure pour les modes autres que "sablier"
		//
		if (!this.mode.equals("sablier")) {
			System.out.println("sablier");
			this.modele = new ModeleTempsG(this.config);
		} else {

			// Extraire du dictionnaire de configuration le temps total
			// du sablier
			//
			Integer tempsTotal = Integer.valueOf(this.config.getChild("tempsG")
							.getChildText("tempsTotal"));
			if (tempsTotal == null) {
				tempsTotal = new Integer(0);
			}

			// Controler la validite de ce parametre
			//
			if (tempsTotal.intValue() < 0) {
				System.out.println("bug ligne 214");
				throw new Throwable("-3.3");
			}

			// Creer le modele configure d'un sablier
			//
			this.modele = new ModeleTempsG(this.config, tempsTotal.intValue());
		}

		// Ajouter la vue comme observateur des modifications du
		// modele de donnees
		//
		this.modele.addObserver(this.vue);

		// Lancer le thread sous jacent
		//
		final Thread t1 = new Thread(this.modele);
		t1.start();

		// Renommer le thread sous jacent dans le cas d'instances
		// multiples du composant TempsG
		//
		if (this.nomInstance != null) {
			t1.setName(this.nomInstance);
		}
	}

	// --- Methode setButee

	/**
	 * @param tempsTotal
	 * @throws Throwable
	 */
	public void demarrer(final int tempsTotal) throws Throwable {

		// Controler la validite du parametre
		//
		if (tempsTotal < 0) {
			System.out.println("bug ligne 252");
			throw new Throwable("-3.3");
		}

		// Creer un modele configure d'un sablier
		//
		this.modele = new ModeleTempsG(this.config, tempsTotal);
		((VueTempsG) this.vue).setMax(tempsTotal);
		// Ajouter la vue comme observateur des modifications
		// du modele de donnees
		//
		if (this.vue != null) {
			this.modele.addObserver(this.vue);
		}

		if (this.vue == null) {

			System.out.println("null point except");

		}

		// Lancer le thread sous jacent
		//
		final Thread t1 = new Thread(this.modele);
		t1.start();

		// Renommer le thread sous jacent dans le cas d'instances
		// multiples du composant TempsG
		//
		if (this.nomInstance != null) {
			t1.setName(this.nomInstance);
		}
	}

	// --- Methode attendre

	public void detruire() {

		((VueTempsG) this.vue).destroy();
		this.vue = null;
		this.modele = null;

	}

	// --- Methode demarrer

	/**
	 * @return String
	 */
	public String getNomInstance() {

		return this.nomInstance;
	}

	/**
	 * @return Integer
	 */
	public int getPrioriteInstance() {

		return this.prioriteInstance;
	}

	// --- Methode construireVue

	/**
	 * @return String
	 */
	public String getTempsCourant() {

		return this.modele.getTempsCourant();
	}

	/**
	 * @return boolean
	 */
	// --- Methode presenceVue

	public boolean presenceVue() {

		return this.vue != null;
	}

	// --- Methode ajouterObservateur

	/**
	 * @param cible
	 */
	public void setButee(final String cible) {

		this.modele.setButee(cible);
	}
}
