package library;

//
// IUT de Nice / Departement informatique / Module APO-Java
// Annee 2007_2008 - Gestion des fichiers de donnees
//
// Classe Data - Services de gestion des fichiers de donnees
//
// Edition A : enregistrement et chargement d'un fichier de donnees
// + Version 1.0.0 : version initiale
// + version 1.1.0 : chargement d'un fichier de donnees depuis le sous
// repertoire Data du repertoire courant, a defaut de le
// trouver dans ce dernier
//
/**
 * La classe Data fournit deux services destin�s � simplifier et �
 * uniformiser la gestion des fichiers de donnees (objets Java).
 * Les services fournis sont :
 * load : charger un fichier de donnees depuis le repertoire courant,
 * store : enregistrer un fichier de donnees dans le repertoire courant.
 * 
 * @author Alain Thuaire - Universite de Nice/IUT - Departement informatique
 **/

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

abstract public class Data {

	/**
	 * La methode load charge le contenu d'un fichier de donnees depuis le
	 * repertoire courant. L'objet resultant est la valeur de retour. Le fichier
	 * origine possede obligatoirement l'extension .data. Le nom du fichier
	 * est forme automatiquement de la facon suivante : p1-p2.data, ou p1 et p2
	 * designent les deux parametres effectifs.
	 **/
	public static Object load(final String name, final String version) {

		String nomFichier;
		FileInputStream f = null;
		ObjectInputStream in = null;
		Object resultat;

		// Construire le nom du fichier source de la configuration
		//
		nomFichier = name + "-" + version + ".data";

		// Construire un fichier logique correspondant
		//
		try {
			f = new FileInputStream(nomFichier);
		} catch (final Exception e1) {
			nomFichier = "Data/" + nomFichier;
			try {
				f = new FileInputStream(nomFichier);
			} catch (final Exception e2) {
				return null;
			}
		}

		// Construire un flux d'entree base sur le fichier logique
		//
		try {
			in = new ObjectInputStream(f);
		} catch (final Exception e) {
			return null;
		}

		// Acquerir et deserialiser le flux d'entree
		//
		try {
			resultat = in.readObject();
		} catch (final Exception e) {
			return null;
		}

		return resultat;
	}

	/**
	 * La methode store enregistre dans un fichier du repertoire courant l'objet
	 * fourni en premier parametre. Le fichier resultant est cree avec
	 * l'extension
	 * .data. Le nom du fichier est forme automatiquement de la facon suivante :
	 * p2-p3.data, o� p2 et p3 designent les deux derniers parametres effectifs.
	 **/
	public static boolean store(final Object data, final String name,
					final String version) {

		String nomFichier;
		FileOutputStream f = null;
		ObjectOutputStream out = null;

		// Controler l'existence de la donnee
		//
		if (data == null) {
			return false;
		}

		// Construire le nom du fichier de donnees
		//
		nomFichier = name + "-" + version + ".data";

		// Construire un fichier logique et le fichier physique associe
		//
		try {
			f = new FileOutputStream(nomFichier);
		} catch (final Exception e) {
			return false;
		}

		// Construire un flux de sortie base sur le fichier logique
		//
		try {
			out = new ObjectOutputStream(f);
		} catch (final Exception e) {
			return false;
		}

		// Serialiser l'objet dans le flux de sortie
		//
		try {
			out.writeObject(data);
		} catch (final Exception e) {
			return false;
		}
		return true;
	}
}
