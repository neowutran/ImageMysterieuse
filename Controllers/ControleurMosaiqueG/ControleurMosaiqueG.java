package controllers;

//
// IUT de Nice / Departement informatique / Module APO-Java
// Annee 2011_2012 - Composants generiques
//
// Classe ControleurMosaiqueG - Controleur d'un panneau organise en cellules
// (chaque cellule est elle meme un panneau)
//
// Edition A : chargement et gestion d'un lot ordonne d'images
//
// + Version 1.0.0 : version initiale
// + Version 1.1.0 : ajout de la methode installerImages
// + Version 1.2.0 : ajout des accesseurs obtenirNbCellules et
// obtenirNbImages
// + Version 1.3.0 : introduction methode publique permuterImages
// + ajout accesseur obtenirCheminImage
// + Version 1.4.0 : ajout methodes publiques ajouterImage,
// retirerImage et rassemblerImages
//
// Edition B : mise en place ecouteurs de mosaique, avec exploitation de
// la classe ControleurG version 1.1.0 et superieures
//
// + Version 2.0.0 : ajout methode publique observerMosaique
// + Version 2.1.0 : ajout accesseur obtenirPositionCellule
// + Version 2.2.0 : ajout methode publique observerCellule
//
// Auteur : A. Thuaire
//

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashMap;
import java.util.LinkedHashMap;

import library.Config;
import models.ModeleMosaiqueG;
import views.VueMosaiqueG;

public class ControleurMosaiqueG extends ControleurG implements MouseListener,
				MouseMotionListener {

	private VueMosaiqueG	vueSupport;
	private ModeleMosaiqueG	modeleSupport;

	// --- Premier constructeur normal

	public ControleurMosaiqueG(final Object hamecon, final HashMap config)
					throws Throwable {

		// Controler la validite des parametres
		//
		if (hamecon == null) {
			throw new Throwable("-2.1");
		}

		if (config == null) {
			throw new Throwable("-3.1");
		}
		// Construire la vue support
		//
		this.vueSupport = new VueMosaiqueG(hamecon, config);

		// Construire un modele support par defaut
		//
		this.modeleSupport = new ModeleMosaiqueG();
	}

	public ControleurMosaiqueG(final Object hamecon,
					final String fichierConfig, final String versionConfig)
					throws Throwable {

		// Controler la validite des parametres
		//
		if (hamecon == null) {
			throw new Throwable("-2.1");
		}

		// Traiter le cas de l'absence de configuration
		//
		if (fichierConfig == null) {
			this.vueSupport = new VueMosaiqueG(hamecon);
			return;
		} else if (versionConfig == null) {
			throw new Throwable("-2.3");
		}

		// Charger le dictionnaire de configuration
		//
		final HashMap config = (HashMap) Config.load(fichierConfig,
						versionConfig);
		if (config == null) {
			throw new Throwable("-3.1");
		}
		// Construire la vue support
		//
		this.vueSupport = new VueMosaiqueG(hamecon, config);

		// Construire un modele support par defaut
		//
		this.modeleSupport = new ModeleMosaiqueG();
	}

	// --- Second constructeur normal

	public ControleurMosaiqueG(final Object hamecon, final String configVue,
					final String versionConfig, final String cheminImages,
					final String versionImages) throws Throwable {

		// Invoquer le premier constructeur
		//
		this(hamecon, configVue, versionConfig);

		// Controler la validite des deux derniers parametres
		//
		if (cheminImages == null) {
			throw new Throwable("-2.4");
		}
		if (versionImages == null) {
			throw new Throwable("-2.5");
		}

		// Construire le modele support
		//
		this.modeleSupport = new ModeleMosaiqueG(cheminImages, versionImages);

		// Obtenir le lot courant d'images gerees par le modele
		//
		final LinkedHashMap description = this.modeleSupport.obtenirImages();

		// Installer le lot courant d'images dans la vue
		//
		this.vueSupport.installerImages(description);
	}

	// --- Methode obtenirVue

	public boolean ajouterImage(final int ligne, final int colonne,
					final String cheminImage) throws Throwable {

		// Obtenir les dimensions de la mosaique
		//
		final int nbLignes = this.obtenirNbLignes();
		final int nbColonnes = this.obtenirNbColonnes();

		// Controler la validite des parametres
		//
		if ((ligne < 1) || (ligne > nbLignes)) {
			throw new Throwable("-2.1");
		}
		if ((colonne < 1) || (colonne > nbColonnes)) {
			throw new Throwable("-2.2");
		}
		if (cheminImage == null) {
			throw new Throwable("-2.3");
		}

		// Piloter la vue
		//
		return this.vueSupport.ajouterImage(ligne, colonne, cheminImage);
	}

	// --- Methode obtenirModele

	public void installerImages(final String cheminImages,
					final String versionImages) throws Throwable {

		// Controler la validite des parametres
		//
		if (cheminImages == null) {
			throw new Throwable("-2.1");
		}
		if (versionImages == null) {
			throw new Throwable("-2.2");
		}

		// Piloter le chargement des images par le modele
		//
		LinkedHashMap description;

		description = this.modeleSupport.chargerImages(cheminImages,
						versionImages, null);
		// Installer des images dans la vue
		//
		this.vueSupport.installerImages(description);
	}

	// --- Methode obtenirNbLignes

	public void observerCellule(final int ligne, final int colonne,
					final MouseListener c) throws Throwable {

		// Obtenir les dimensions de la mosaique
		//
		final int nbLignes = this.obtenirNbLignes();
		final int nbColonnes = this.obtenirNbColonnes();

		// Controler la validite des parametres
		//
		if ((ligne < 1) || (ligne > nbLignes)) {
			throw new Throwable("-2.1");
		}
		if ((colonne < 1) || (colonne > nbColonnes)) {
			throw new Throwable("-2.2");
		}
		if (c == null) {
			throw new Throwable("-2.3");
		}

		// Piloter la vue
		//
		this.vueSupport.observerCellule(ligne, colonne, c);
	}

	// --- Methode obtenirNbColonnes

	public void observerMosaique(final MouseListener c) throws Throwable {

		this.vueSupport.ajouterControleur(c);
	}

	// --- Methode obtenirNbImages

	public String obtenirCheminImage(final int ligne, final int colonne)
					throws Throwable {

		// Obtenir les dimensions de la mosaique
		//
		final int nbLignes = this.obtenirNbLignes();
		final int nbColonnes = this.obtenirNbColonnes();

		// Controler la validite des parametres
		//
		if ((ligne < 1) || (ligne > nbLignes)) {
			throw new Throwable("-2.1");
		}
		if ((colonne < 1) || (colonne > nbColonnes)) {
			throw new Throwable("-2.2");
		}

		// Piloter la vue
		//
		return this.vueSupport.obtenirCheminImage(ligne, colonne);
	}

	// --- Methode obtenirNbCellules

	public ModeleMosaiqueG obtenirModele() {

		return this.modeleSupport;
	}

	// --- Methode obtenirCheminImage

	public int obtenirNbCellules() {

		return this.vueSupport.obtenirNbCellules();
	}

	// --- Methode installerImages

	public int obtenirNbColonnes() {

		return this.vueSupport.obtenirNbColonnes();
	}

	// --- Methode permuterImages

	public int obtenirNbImages() {

		return this.vueSupport.obtenirNbImages();
	}

	// --- Methode ajouterImage

	public int obtenirNbLignes() {

		return this.vueSupport.obtenirNbLignes();
	}

	// --- Methode retirerImage

	public Point obtenirPositionCellule(final Dimension positionClic)
					throws Throwable {

		if (positionClic == null) {
			throw new Throwable("-2.1");
		}

		return this.vueSupport.obtenirPositionCellule(positionClic);
	}

	// --- Methode rassemblerImages

	public VueMosaiqueG obtenirVue() {

		return this.vueSupport;
	}

	// --- Methode observerMosaique

	public void permuterImages(final Dimension p1, final Dimension p2)
					throws Throwable {

		// Controler la validite des parametres
		//
		if (p1 == null) {
			throw new Throwable("-2.1");
		}
		if (p2 == null) {
			throw new Throwable("-2.2");
		}

		// Piloter la vue
		//
		this.vueSupport.permuterImages(p1, p2);
	}

	// --- Methode obtenirPositionCellule

	public void rassemblerImages() throws Throwable {

		this.vueSupport.rassemblerImages();
	}

	// --- Methode observerCellule

	public boolean retirerImage(final int ligne, final int colonne)
					throws Throwable {

		// Obtenir les dimensions de la mosaique
		//
		final int nbLignes = this.obtenirNbLignes();
		final int nbColonnes = this.obtenirNbColonnes();

		// Controler la validite des parametres
		//
		if ((ligne < 1) || (ligne > nbLignes)) {
			throw new Throwable("-2.1");
		}
		if ((colonne < 1) || (colonne > nbColonnes)) {
			throw new Throwable("-2.2");
		}

		// Piloter la vue
		//
		return this.vueSupport.retirerImage(ligne, colonne);
	}
}
