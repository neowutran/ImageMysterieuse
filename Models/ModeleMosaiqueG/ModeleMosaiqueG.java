package models;

//
// IUT de Nice / Departement informatique / Module APO-Java
// Annee 2011_2012 - Composants generiques
//
// Classe ModeleMosaiqueG - Gestion des donnees applicatives d'une mosaique
// d'images (Modele MVC)
//
// Edition A : adaptation de l'existant anterieur (MosaiqueG 2010_2011)
//
// + Version 1.0.0 : conformite aux besoins TravelMap
//
// Auteur : A. Thuaire
//

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;

import library.Data;

public class ModeleMosaiqueG {

	private final LinkedHashMap	images;

	// --- Constructeur par defaut

	public ModeleMosaiqueG() {

		this.images = new LinkedHashMap();
	}

	// --- Premier constructeur normal

	public ModeleMosaiqueG(final String nomFichier, final String version)
					throws Throwable {

		super();

		// Controle la validite des parametres
		//
		if (nomFichier == null) {
			throw new Throwable("-2.1");
		}
		if (version == null) {
			throw new Throwable("-2.2");
		}

		// Charger le fichier de description des images
		//
		final Object description = Data.load(nomFichier, version);
		if (description == null) {
			throw new Throwable("-3.0");
		}

		// Controler la conformite de la description
		//
		final String origine = description.getClass().getName();
		final String ref = "java.util.LinkedHashMap";
		if (!origine.equals(ref)) {
			throw new Throwable("-3.1");
		}

		// Conserver les images en attribut
		//
		this.images = (LinkedHashMap) description;
	}

	// --- Second constructeur normal

	public ModeleMosaiqueG(final String cheminRepertoire,
					final String extension, final HashMap filtre)
					throws Throwable {

		super();

		// Controler la validite des parametres
		//
		if (cheminRepertoire == null) {
			throw new Throwable("-2.1");
		}
		if (extension == null) {
			throw new Throwable("-2.2");
		}

		// Charger les images depuis le repertoire source
		//
		this.images = this.chargerImages(cheminRepertoire, extension, filtre);
	}

	// --- Methode chargerImages

	public LinkedHashMap chargerImages(final String cheminRepertoire,
					final String extension, final HashMap filtre)
					throws Throwable {

		File repertoireImages = null;

		// Creer le repertoire abstrait cible
		//
		if (cheminRepertoire == null) {
			throw new Throwable("-2.1");
		} else {
			repertoireImages = new File(cheminRepertoire);
		}

		// Controler l'existence du repertoire cible
		//
		if (repertoireImages == null) {
			throw new Throwable("-3.2");
		}

		// Obtenir la liste de tous les fichiers du repertoire cible
		//
		final String[] nomsFichierImage = repertoireImages.list();

		// Construire le dictionnaire resultant
		//
		final LinkedHashMap resultat = new LinkedHashMap();
		int position = 0;
		String cle = null, associe = null;

		for (final String element : nomsFichierImage) {
			position = element.indexOf(extension);
			if (position > 0) {
				cle = element.substring(0, position);
				associe = cheminRepertoire + File.separator + element;
				resultat.put(cle, associe);
			}
		}

		// Eliminer les images referencees dans le filtre fourni
		//
		this.filtrerImages(filtre);

		return resultat;
	}

	// --- Methode filtrerImages

	public void filtrerImages(final HashMap filtre) {

	}

	// --- Methode obtenirImages

	public LinkedHashMap obtenirImages() {

		return this.images;
	}
}
