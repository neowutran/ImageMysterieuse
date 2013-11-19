package controllers;

//
// IUT de Nice / Departement informatique / Module APO-Java
// Annee 2011_2012 - Composants generiques
//
// Classe ControleurG - Controleur de composants graphiques MVC
//
// Edition A : externalisation depuis les controleurs de panneaux et de
// barre de menus
//
// + Version 1.0.0 : couverture des besoins initiaux des controleurs
// des classes PanneauG et BarreMenusG
// + Version 1.1.0 : redefinition methode mouseClicked pour mettre a
// jour les attributs "positionDernierClic" et
// "nbClics"
// + Version 1.2.0 : ajout methode protegee attendre
// + Version 1.3.0 : ajout nouvelle signature methode attendreClic
//
// Auteur : A. Thuaire
//

import java.awt.Dimension;
import java.awt.event.MouseEvent;

public class ControleurG extends EspionG {

	private static class Chrono {

		public static void attendre(final int tms) {

			// Attendre tms millisecondes, en bloquant le thread courant
			//
			try {
				Thread.currentThread();
				Thread.sleep(tms);
			} catch (final InterruptedException e) {
			}
		}
	}

	private Dimension	positionDernierClic;

	// --- Methode obtenirDernierClic

	private int			nbClics	= 0;

	// --- Methode obtenirNombreClics

	protected void attendre(final int duree) {

		if (duree <= 0) {
			return;
		}

		Chrono.attendre(duree);
	}

	// --- Methode attendreClic

	public Dimension attendreClic() {

		final int nbClics = this.obtenirNombreClics();

		while (nbClics != (this.obtenirNombreClics() - 1)) {
			Chrono.attendre(100);
		}
		return this.obtenirDernierClic();
	}

	// --- Methode attendreClic

	public Dimension attendreClic(final int tempsMaximum) throws Throwable {

		final int nbClics = this.obtenirNombreClics();

		// Controler la validite du parametre
		//
		if (tempsMaximum < 0) {
			throw new Throwable("-2.1");
		}

		// Attendre le prochain clic operateur dans la limite du
		// temps maximum fixe par le parametre
		//
		int tempsEcoule = 0;
		while ((nbClics != (this.obtenirNombreClics() - 1))
						&& (tempsEcoule < tempsMaximum)) {

			Chrono.attendre(100);
			tempsEcoule += 100;
		}

		// Traiter le cas ou le time out est atteint
		//
		if (tempsEcoule >= tempsMaximum) {
			return null;
		}

		// Restituer le resultat
		//
		return this.obtenirDernierClic();
	}

	// --- Methode mouseClicked

	@Override
	public void mouseClicked(final MouseEvent e) {

		// Appeler l'ecouteur de la classe mere
		//
		super.mouseClicked(e);

		// Memoriser la position du dernier clic
		//
		this.positionDernierClic = new Dimension(e.getX(), e.getY());

		// Incrementer le nombre de clics
		//
		this.nbClics++;
	}

	// --- Methode attendre

	public Dimension obtenirDernierClic() {

		return this.positionDernierClic;
	}

	// ------------------------------------- Classe interne privee Chrono

	public int obtenirNombreClics() {

		return this.nbClics;
	}
}
