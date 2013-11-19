//
// IUT de Nice / Departement informatique / Module APO-Java
// Annee 2011_2012 - Composants generiques
//
// Classe ModeleTempsG : partie Modele (MVC) d'un compteur/decompteur de
// temps
//
// Edition Draft : externalisation d'un modele de donnees observable
// exploite par les demonstrateurs de la classe EspionG
//
// + Version 0.0.0 : derivation de la classe Observable
// + Version 0.1.0 : l'heure courante est notifiee a l'observateur
// + Version 0.2.0 : exploitation d'un fichier de configuration du
// composant TempsG
// + Version 0.3.0 : ajout d'un attribut de controle d'execution du
// thread et accesseur de modification associe
//
// Edition A : ajout des moyens de pilotage externe du thread sous
// jacent
//
// + Version 1.0.0 : ajout d'un attribut de controle d'execution du
// thread et accesseur de modification associe
// + Version 1.1.0 : ajout d'une butee cible eventuelle pour fin
// d'execution du thread, definie par parametre
// de configuration
// + Version 1.2.0 : notifications simultanees de plusieurs modifs.
// eventuelles de donnees
//
// Edition B : ajout de plusieurs modes de fonctionnement du modele
// (choix par parametre de configuration)
//
// + Version 2.0.0 : reorganisation du code pour preparer l'ajout des
// modes de fonctionnement
// + Version 2.1.0 : introduction du mode "chronometre"
// + Version 2.2.0 : introduction du mode "sablier"
// + Version 2.3.0 : ajout methode privee notifier
// + ajout notification aux observateurs avant la
// premiere attente (correction du decalage initial
// de visualisation)
// + Version 2.4.0 : ajout de l'identificateur symbolique eventuel de
// l'instance courante du composant TempsG dans le
// dictionnaire de notification des evenements
// (nouveau parametre de configuration)
//
// Auteur : A. Thuaire
//
package models;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Observable;

import org.jdom2.Element;

/**
 *
 */
public class ModeleTempsG extends Observable implements Runnable {

	private static boolean controleButee(final String separateur,
					final String temps, final String butee, final int seuil) {

		// Controler la validite du second parametre
		//
		if (butee == null) {
			System.out.println("bug");
			return false;
		}

		// Convertir le label du temps en numerique
		//
		final int op1 = ModeleTempsG.stringToTime(separateur, temps);

		// Convertir le label de la butee en numerique
		//
		final int op2 = ModeleTempsG.stringToTime(separateur, butee);

		// Restituer le resultat
		//
		return Math.abs(op1 - op2) < seuil;
	}

	private static int stringToTime(final String separateur, String label) {

		int heures, minutes, secondes;

		// Fractionner le label en trois champs suivant le separateur
		// et convertir en numerique
		//
		label = label.replaceAll("\\s", "");
		final String[] champsLabel = label.split(separateur);
		heures = Integer.valueOf(champsLabel[0]);
		minutes = new Integer(champsLabel[1]).intValue();
		secondes = new Integer(champsLabel[2]).intValue();

		// Restituer le resultat
		//
		return (3600 * heures) + (60 * minutes) + secondes;
	}

	private static String timeToString(final String separateur,
					final int heures, final int minutes, final int secondes) {

		String resultat = "";

		resultat = heures + separateur;

		if (minutes < 10) {
			resultat += "0" + minutes;
		} else {
			resultat += minutes;
		}

		resultat += separateur;

		if (secondes < 10) {
			resultat += "0" + secondes;
		} else {
			resultat += secondes;
		}

		return resultat;
	}

	private String			tempsCourant	= "0 : 00 : 00";
	private Integer			time			= 0;
	private String			separateur;
	private boolean			statusThread	= true;
	private Integer			increment;

	// --- Premier constructeur normal

	private String			mode;

	// --- Second constructeur normal

	private String			buteeCible;

	// --- Methode getTempsCourant

	private final String	nomInstance;

	// --- Methode resetStatus

	/**
	 * @param config
	 * @throws Throwable
	 */
	public ModeleTempsG(final Element config) throws Throwable {

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
			throw new Throwable("-3.1");
		}

		// Extraire et memoriser le separateur de champs
		//
		this.separateur = config.getChild("tempsG").getChildText("separateur");
		if (this.separateur == null) {
			this.separateur = " : ";
		}

		// Extraire et memoriser l'unite de temps
		//
		if (config.getChild("tempsG").getChildText("increment") == null) {
			this.increment = new Integer(1);
		} else {
			this.increment = Integer.valueOf(config.getChild("tempsG")
							.getChildText("increment"));
		}

		// Extraire et memoriser la butee cible eventuelle
		//
		this.buteeCible = config.getChild("tempsG").getChildText("butee");

		// Extraire du dictionnaire de configuration le nom symbolique
		// eventuel du composant TempsG (cas d'une application qui
		// exploite en parallele plusieurs instances du composant)
		//
		this.nomInstance = config.getChild("tempsG")
						.getChildText("nomInstance");
	}

	// --- Methode setButee

	/**
	 * @param config
	 * @param tempsTotal
	 * @throws Throwable
	 */
	public ModeleTempsG(final Element config, final int tempsTotal)
					throws Throwable {

		// Invoquer le premier constructeur
		//
		this(config);

		// Controler la validite du second parametre
		//
		if (tempsTotal < 0) {
			System.out.println("bug");
			throw new Throwable("-2.2");
		}

		// Decomposer le second parametre en heures, minutes et secondes
		//
		int heures = 0, minutes = 0, secondes = 0;

		secondes = tempsTotal;

		if (secondes >= 60) {
			minutes = secondes / 60;
			secondes -= minutes * 60;
		}

		if (minutes >= 60) {
			heures = minutes / 60;
			minutes -= heures * 60;
		}

		// Memoriser le resultat dans l'attribut "tempsCourant"
		//
		this.time = tempsTotal;
		this.tempsCourant = ModeleTempsG.timeToString(this.separateur, heures,
						minutes, secondes);
	}

	// --- Methode run

	private void executerChronometre() {

		int heures = 0, minutes = 0, secondes = 0;

		// Fractionner le label du temps courant en trois champs et
		// convertir en numerique
		//
		final String[] champsTempsCourant = this.tempsCourant
						.split(this.separateur);

		heures = new Integer(champsTempsCourant[0]).intValue();
		minutes = new Integer(champsTempsCourant[1]).intValue();
		secondes = new Integer(champsTempsCourant[2]).intValue();

		// Determiner le rafraichissement du champ de droite (secondes)
		// du label tempsCourant
		//
		int deltaMinutes = 0;

		secondes += this.increment;

		if (secondes >= 60) {
			deltaMinutes = secondes / 60;
			secondes -= deltaMinutes * 60;
		}

		// Determiner le rafraichissement du champ du centre (minutes)
		// du label tempsCourant
		//
		int deltaHeures = 0;

		minutes += deltaMinutes;
		if (minutes >= 60) {
			deltaHeures = minutes / 60;
			minutes -= deltaHeures * 60;
		}

		// Determiner le rafraichissement du champ de gauche (heures)
		// du label tempsCourant
		//
		heures += deltaHeures;

		// Mettre a jour le temps courant
		//
		this.time = (secondes + (minutes * 60) + (heures * 3600));
		this.tempsCourant = ModeleTempsG.timeToString(this.separateur, heures,
						minutes, secondes);
	}

	// --- Methode timeToString

	private void executerHorloge() {

		Calendar calendrier;
		int heures = 0, minutes = 0, secondes = 0;

		// Acquerir et fractionner la description du temps
		//
		calendrier = Calendar.getInstance();

		heures = calendrier.get(Calendar.HOUR_OF_DAY);
		minutes = calendrier.get(Calendar.MINUTE);
		secondes = calendrier.get(Calendar.SECOND);

		// Mettre a jour le temps courant
		//
		this.time = (secondes + (minutes * 60) + (heures * 3600));
		this.tempsCourant = ModeleTempsG.timeToString(this.separateur, heures,
						minutes, secondes);
	}

	// --- Methode stringToTime

	private void executerSablier() {

		int heures = 0, minutes = 0, secondes = 0;

		// Fractionner le label du temps courant en trois champs et
		// convertir en numerique
		//
		final String[] champsTempsCourant = this.tempsCourant
						.split(this.separateur);

		heures = new Integer(champsTempsCourant[0]).intValue();
		minutes = new Integer(champsTempsCourant[1]).intValue();
		secondes = new Integer(champsTempsCourant[2]).intValue();

		// Traiter le cas de l'arret du sablier pour temps disponible
		// epuise
		//
		if ((heures == 0) && (minutes == 0) && (secondes == 0)) {
			this.resetStatus();
		}

		// Determiner le rafraichissement du champ de droite (secondes)
		// du label tempsCourant
		//
		int deltaMinutes = 0;

		secondes -= this.increment;

		if (secondes < 0) {
			deltaMinutes = 1;
			secondes = 59;
		}

		// Determiner le rafraichissement du champ du centre (minutes)
		// du label tempsCourant
		//
		int deltaHeures = 0;

		minutes -= deltaMinutes;
		if (minutes < 0) {
			deltaHeures = 1;
			minutes = 59;
		}

		// Determiner le rafraichissement du champ de gauche (heures)
		// du label tempsCourant
		//
		heures -= deltaHeures;

		// Mettre a jour le temps courant
		//
		this.time = (secondes + (minutes * 60) + (heures * 3600));
		this.tempsCourant = ModeleTempsG.timeToString(this.separateur, heures,
						minutes, secondes);
	}

	// --- Methode controleButee

	/**
	 * @return String
	 */
	public String getTempsCourant() {

		return this.tempsCourant;
	}

	// --- Methode executerHorloge

	private boolean notifier() {

		// Etablir si la butee eventuelle a ete atteinte
		//
		final boolean buteeAtteinte = ModeleTempsG.controleButee(
						this.separateur, this.tempsCourant, this.buteeCible,
						this.increment);

		// Construire le dictionnaire des modifications
		//
		final HashMap<String, Comparable> modifs = new HashMap<String, Comparable>();
		modifs.put("tempsCourant", this.tempsCourant);
		modifs.put("buteeAtteinte", new Boolean(buteeAtteinte));
		modifs.put("nomInstance", this.nomInstance);
		modifs.put("time", this.time);

		// Fournir l'etat courant aux observateurs
		//
		this.setChanged();
		this.notifyObservers(modifs);

		// Mettre a jour le status du thread courant
		//
		if (buteeAtteinte) {
			this.resetStatus();
		}

		// Restituer le resultat
		//
		return buteeAtteinte;
	}

	// --- Methode executerChronometre

	/**
	 * 
	 */
	public void resetStatus() {

		this.statusThread = false;
	}

	// --- Methode executerSablier

	public void run() {

		boolean buteeAtteinte;

		// Notifier l'etat initial aux observateurs
		//
		buteeAtteinte = this.notifier();

		// Suspendre le thread courant de la duree specifiee par le
		// parametre de configuration "increment"
		//
		if (!buteeAtteinte) {
			try {
				Thread.sleep(this.increment.intValue() * 1000);
			} catch (final InterruptedException e) {
				System.out.println("thread bug");
			}
		}

		while (this.statusThread) {

			// Acquerir et controler le mode de fonctionnement du modele
			//
			if (this.mode.equals("horloge")) {
				// Executer le mode "horloge"
				//
				this.executerHorloge();
			} else if (this.mode.equals("chronometre")) {
				// Executer le mode "chronometre"
				//
				this.executerChronometre();
			} else {
				// Executer le mode "sablier"
				//
				this.executerSablier();
			}

			// Notifier l'etat courant aux observateurs
			//
			buteeAtteinte = this.notifier();

			// Stopper la boucle si la butee cible a ete atteinte
			//
			if (buteeAtteinte) {
				break;
			}

			// Suspendre le thread courant de la duree specifiee par le
			// parametre de configuration "increment"
			//
			try {
				Thread.sleep(this.increment.intValue() * 1000);
			} catch (final InterruptedException e) {
				System.out.println("thread bug");
			}
		}
	}

	// --- Methode notifier

	/**
	 * @param cible
	 */
	public void setButee(final String cible) {

		this.buteeCible = cible;
	}
}
