package views;

//
// IUT de Nice / Departement informatique / Module APO-Java
// Annee 2011_2012 - Composants generiques
//
// Classe VueMosaiqueG - Mosaique generique de visualisation (MVC - partie Vue)
//
// Edition A : extension de l'ancienne classe MosaiqueG
// (V 2.4.0 / 2010_2011)
//
// + Version 1.0.0 : version initiale adaptee au composant _PanneauG (MVC)
// + Version 1.1.0 : suppression du constructeur normal existant
// + ajout constructeurs analogues a VuePanneauG
// + ajout methode privee exploiterConfig
// + ajout methode privve controlerCle
// + ajout methode privee configurer
// + ajout methode privee setMatrice
// + Version 1.2.0 : introduction de la gestion d'exceptions dans les
// methodes ajouterImage et retirerImage
// + accesseurs obtenirNbLignes et obtenirNbColonnes
// + ajout de la methode publique installerImages
// + Version 1.3.0 : ajout accesseurs obtenirNbCellules et obtenirNbImages
// + Version 1.4.0 : ajout des methodes publiques obtenirCheminImage et
// permuterImages
// + Version 1.5.0 : ajout methode publique rassemblerImages
// + ajout classe interne privee Position
// + ajout accesseurs prives obtenirPositionPCL,
// obtenirPositionCLS,obtenirPositionPCO et
// obtenirPositionCOS
//
// Edition B : gestion des clics souris
//
// + Version 2.0.0 : ajout methode publique obtenirPositionCellule
// + Version 2.1.0 : ajout methode publique observerCellule
//
// Auteur : A. Thuaire

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import javax.swing.BorderFactory;

import library.Config;

public class VueMosaiqueG extends VuePanneauG {

	class CelluleG extends VuePanneauG {

		private static final long	serialVersionUID	= 1L;
		private Object				mosaique;
		private Dimension			position;
		private boolean				etatCellule;
		private String				designationImage;
		private String				titre;

		public CelluleG(final Object hamecon,
						final HashMap<String, Integer> config,
						final Dimension position) throws Throwable {

			super(hamecon, config);
			this.setBorder(BorderFactory.createLineBorder(new Color(100, 100,
							100, 255)));
			// Controler la validite des parametres
			//
			if (hamecon == null) {
				return;
			}
			if (config == null) {
				return;
			}
			if (position == null) {
				return;
			}

			// Memoriser les attributs transmis par parametre
			//
			this.mosaique = hamecon;
			this.position = position;

			// Fixer l'etat par defaut de la cellule
			//
			this.etatCellule = false;

			// Ajouter le panneau sous jacent au panneau principal
			//
			((VuePanneauG) this.mosaique).add(this);
		}

		// --- Accesseurs de consultation

		public boolean ajouterImage(final String cheminImage) throws Throwable {

			// Controler la validite du parametre
			//
			if (cheminImage == null) {
				return false;
			}

			// Valuer l'attribut designationImage
			//
			this.designationImage = cheminImage;

			// Ajouter l'image au panneau sous jacent
			//
			this.modifierImage(this.designationImage);
			return true;
		}

		public void fixerEtat(final boolean etat) {

			this.etatCellule = etat;
		}

		public String obtenirCheminImage() {

			return this.designationImage;
		}

		public boolean obtenirEtat() {

			return this.etatCellule;
		}

		// --- Accesseurs de modification

		public Dimension obtenirPosition() {

			return this.position;
		}

		// --- Methode ajouterImage

		public String obtenirTitre() {

			return this.titre;
		}

		// --- Methode retirerImage

		public boolean retirerImage() throws Throwable {

			// Valuer l'attribut designation
			//
			this.designationImage = null;

			// Retirer l'image au panneau sous jacent
			//
			this.modifierImage(null);
			return true;
		}
	}

	private class Position extends Dimension {

		/**
		 * 
		 */
		private static final long	serialVersionUID	= 1L;
		private boolean				status;

		public Position(final int ligne, final int colonne) {

			super(ligne, colonne);

			// Controler la validite des parametres
			//
			if ((ligne <= 0) || (ligne > VueMosaiqueG.this.nbLignes)) {
				this.status = false;
				return;
			}

			if ((colonne <= 0) || (colonne > VueMosaiqueG.this.nbColonnes)) {
				this.status = false;
				return;
			}

			// Valider le status
			//
			this.status = true;
		}

		// --- Methode valide

		public Position suivante() {

			// Obtenir les coordonnees de la position support
			//
			final int x = (int) this.getWidth();
			final int y = (int) this.getHeight();

			// Restituer le resultat
			//
			if ((x == VueMosaiqueG.this.nbLignes)
							&& (y == VueMosaiqueG.this.nbColonnes)) {
				return new Position(0, 0);
			}
			if (y < VueMosaiqueG.this.nbColonnes) {
				return new Position(x, y + 1);
			}
			return new Position(x + 1, 1);
		}

		// --- Methode suivante

		public boolean valide() {

			return this.status;
		}
	}

	/**
	 * 
	 */

	private static final long	serialVersionUID	= 1L;

	private int					nbLignes;

	// --- Premier constructeur normal

	private int					nbColonnes;

	// --- Second constructeur normal

	private CelluleG[][]		matriceCellules;

	// --- Troisieme constructeur normal

	/**
	 * @param conteneur
	 * @throws Throwable
	 */
	public VueMosaiqueG(final Object conteneur) throws Throwable {

		super(conteneur);

		// Controler la validite du parametre
		//
		if (conteneur == null) {
			throw new Throwable("-2.1");
		}

		// Construire une pseudo configuration
		//
		final HashMap<String, Integer> config = new HashMap<String, Integer>();

		config.put("nbLignes", new Integer(1));
		config.put("nbColonnes", new Integer(1));

		// Exploiter la pseudo configuration de la mosaique
		//
		this.exploiterConfig(config);

		// Configurer la mosaique par defaut
		//
		this.configurer(config);
	}

	// --- Quatrieme constructeur normal

	/**
	 * @param conteneur
	 * @param config
	 * @throws Throwable
	 */
	public VueMosaiqueG(final Object conteneur, final HashMap config)
					throws Throwable {

		super(conteneur);

		// Controler la validite des parametres
		//
		if (conteneur == null) {
			throw new Throwable("-2.1");
		}
		if (config == null) {
			throw new Throwable("-2.2");
		}

		// Exploiter la configuration de la mosaique
		//
		this.exploiterConfig(config);

		// Configurer la mosaique
		//
		this.configurer(config);
	}

	// --- Methode obtenirNbLignes

	/**
	 * @param conteneur
	 * @param nbL
	 * @param nbC
	 * @throws Throwable
	 */
	public VueMosaiqueG(final Object conteneur, final int nbL, final int nbC)
					throws Throwable {

		super(conteneur);

		// Controler la validite des parametres
		//
		if (conteneur == null) {
			throw new Throwable("-2.1");
		}
		if (nbL < 1) {
			throw new Throwable("-2.2");
		}
		if (nbC < 1) {
			throw new Throwable("-2.3");
		}

		// Construire une pseudo configuration
		//
		final HashMap<String, Integer> config = new HashMap<String, Integer>();

		config.put("nbLignes", new Integer(nbL));
		config.put("nbColonnes", new Integer(nbC));

		// Exploiter la pseudo configuration de la mosaique
		//
		this.exploiterConfig(config);

		// Configurer la mosaique avec nbL lignes et nbC colonnes
		//
		this.configurer(config);
	}

	// --- Methode obtenirNbColonnes

	/**
	 * @param conteneur
	 * @param chemin
	 * @param version
	 * @throws Throwable
	 */
	public VueMosaiqueG(final Object conteneur, final String chemin,
					final String version) throws Throwable {

		super(conteneur);

		// Charger la configuration cible
		//
		final Object w = Config.load(chemin, version);
		if (w == null) {
			throw new Throwable("-3.0");
		}

		// Controler le type de dictionnaire cible
		//
		final String typeConfig = w.getClass().getName();
		if (!typeConfig.equals("java.util.HashMap")) {
			throw new Throwable("-3.1");
		}

		final HashMap<String, Integer> config = (HashMap<String, Integer>) w;

		// Exploiter la configuration de la mosaique
		//
		this.exploiterConfig(config);

		// Configurer la mosaique
		//
		this.configurer(config);
	}

	// --- Methode obtenirNbCellules

	/**
	 * @param ligne
	 * @param colonne
	 * @param cheminImage
	 * @return
	 * @throws Throwable
	 */
	public boolean ajouterImage(final int ligne, final int colonne,
					final String cheminImage) throws Throwable {

		// Controler la validite des parametres
		//
		if ((ligne < 1) || (ligne > this.nbLignes)) {
			throw new Throwable("-2.1");
		}
		if ((colonne < 1) || (colonne > this.nbColonnes)) {
			throw new Throwable("-2.2");
		}
		if (cheminImage == null) {
			throw new Throwable("-2.3");
		}

		// Ajouter l'image a la cellule cible
		//
		final CelluleG celluleCible = this.matriceCellules[ligne - 1][colonne - 1];
		return celluleCible.ajouterImage(cheminImage);
	}

	// --- Methode obtenirNbImages

	private void configurer(final HashMap<String, Integer> config)
					throws Throwable {

		this.setBackground(config);
		this.setForeground(config);

		this.setFont(config);
		this.setImage(config);
		this.setTitre(config);

		// Installer le gestionnaire de placement du panneau principal
		//
		final GridLayout gestPlacement = new GridLayout(this.nbLignes,
						this.nbColonnes);
		this.setLayout(gestPlacement);

		// Creer et remplir la matrice de cellules sous jacente
		//
		this.setMatrice(config);
	}

	// --- Methode obtenirCheminImage

	private int controlerCle(final HashMap<String, Integer> config,
					final String cle) {

		Object w;
		String typeAssocie;
		Integer nb;

		// Controler la presence de la cle
		//
		if (!config.containsKey(cle)) {
			return -1;
		}

		// Controler le type d'associe de la cle
		//
		w = config.get(cle);
		typeAssocie = w.getClass().getName();
		if (!typeAssocie.equals("java.lang.Integer")) {
			return -2;
		}

		// Controler la valeur de l'associe
		//
		nb = (Integer) w;
		if (nb.intValue() < 1) {
			return -3;
		}

		// Restituer la valeur associe a la cle cible
		// ;
		return nb.intValue();
	}

	// --- Methode exploiterConfig

	private void exploiterConfig(final HashMap<String, Integer> config)
					throws Throwable {

		// Controler la validite du parametre
		//
		if (config == null) {
			throw new Throwable("-2.1");
		}

		// Controler la validite du nombre de lignes
		//
		final int nbL = this.controlerCle(config, "nbLignes");
		if (nbL <= 0) {
			throw new Throwable("-3.2");
		}

		// Memoriser en attribut le nombre de lignes
		//
		this.nbLignes = nbL;

		// Controler la validite du nombre de colonnes
		//
		final int nbC = this.controlerCle(config, "nbColonnes");
		if (nbC <= 0) {
			throw new Throwable("-3.3");
		}

		// Memoriser en attribut le nombre de colonnes
		//
		this.nbColonnes = nbC;
	}

	// --- Methode controlerCle

	/**
	 * @param images
	 * @throws Throwable
	 */
	public void installerImages(final LinkedHashMap<?, ?> images)
					throws Throwable {

		// Controler la validite du parametre
		//
		if (images == null) {
			throw new Throwable("-2.1");
		}

		// Controler l'existence de la matrice de cellules
		//
		if (this.matriceCellules == null) {
			throw new Throwable("-3.0");
		}

		// Controler que la matrice est suffisante pour heberger
		// tout le lot d'images
		//
		if ((this.nbLignes * this.nbColonnes) < images.size()) {
			throw new Throwable("-3.1");
		}

		// Initialiser les coordonnees de la cellule d'accueil
		//
		int i = 1, j = 1;

		// Parcourir le dictionnaire des images
		//
		String cle = null;
		String chemin = null;

		final Iterator k = images.keySet().iterator();

		while (k.hasNext()) {

			// Obtenir la cle courante
			//
			cle = (String) k.next();

			// Obtenir l'associe correspondant
			//
			chemin = (String) images.get(cle);

			// Ajouter l'image courante a la mosaique
			//
			this.ajouterImage(i, j, chemin);

			// Determiner les coordonnees de la cellule suivante
			//
			if ((j % this.nbColonnes) == 0) {
				i++;
				j = 1;
			} else {
				j++;
			}
		}
	}

	// --- Methode configurer

	/**
	 * @param ligne
	 * @param colonne
	 * @param c
	 * @throws Throwable
	 */
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

		// Obtenir la reference Java du panneau cible
		//
		final VuePanneauG vueCible = this.matriceCellules[ligne - 1][colonne - 1];

		// Ajouter le controleur a la vue du panneau
		//
		vueCible.ajouterControleur(c);
	}

	// --- Methode setMatrice

	/**
	 * @param ligne
	 * @param colonne
	 * @return String
	 * @throws Throwable
	 */
	public String obtenirCheminImage(final int ligne, final int colonne)
					throws Throwable {

		// Controler la validite des parametres
		//
		if ((ligne < 1) || (ligne > this.nbLignes)) {
			throw new Throwable("-2.1");
		}
		if ((colonne < 1) || (colonne > this.nbColonnes)) {
			throw new Throwable("-2.2");
		}
		if (this.matriceCellules == null) {
			throw new Throwable("-3.0");
		}

		return this.matriceCellules[ligne - 1][colonne - 1]
						.obtenirCheminImage();

	}

	// --- Methode ajouterImage

	/**
	 * @return int
	 */
	public int obtenirNbCellules() {

		return this.nbLignes * this.nbColonnes;
	}

	// --- Methode retirerImage

	/**
	 * @return obtenirNbColonnes
	 */
	public int obtenirNbColonnes() {

		return this.nbColonnes;
	}

	// --- Methode installerImages

	/**
	 * @return int
	 */
	public int obtenirNbImages() {

		VuePanneauG panneauCourant;

		// Parcourir l'ensemble des cellules
		//
		int nbImages = 0;
		for (int i = 0; i < this.nbLignes; i++) {
			for (int j = 0; j < this.nbColonnes; j++) {

				// Obtenir la cellule courante
				//
				panneauCourant = this.matriceCellules[i][j];

				// Determiner la presence ou pas d'une image
				//
				if (panneauCourant.presenceImage()) {
					nbImages++;
				}
			}
		}

		// Restituer le resultat
		//
		return nbImages;
	}

	// --- Methode permuterImages

	/**
	 * @return int
	 */
	public int obtenirNbLignes() {

		return this.nbLignes;
	}

	// --- Methode rassemblerImages

	/**
	 * @param positionClic
	 * @return Point
	 * @throws Throwable
	 */
	public Point obtenirPositionCellule(final Dimension positionClic)
					throws Throwable {

		// Controler la validite du parametre
		//
		if (positionClic == null) {
			throw new Throwable("-2.1");
		}

		// Controler les coordonnees du clic
		//
		final int x = (int) positionClic.getWidth();
		final int y = (int) positionClic.getHeight();

		// Obtenir les dimensions du panneau principal
		//
		final int largeur = this.getWidth();
		final int hauteur = this.getHeight();

		// Calculer la largeur et la hauteur de chaque cellule
		//
		final int largeurPanneau = largeur / this.nbColonnes;
		final int hauteurPanneau = hauteur / this.nbLignes;

		// Calculer les coordonnees de la cellule cible
		//
		final int i = (y / hauteurPanneau) + 1;
		final int j = (x / largeurPanneau) + 1;

		// Restituer le resultat
		//
		return new Point(i, j);
	}

	// --- Methode obtenirPositionPCL

	private Position obtenirPositionCLS(final Dimension positionDepart) {

		CelluleG celluleCourante;

		// Controler la validite du parametre
		//
		if (positionDepart == null) {
			return null;
		}

		// Controler la validite de la ligne de depart
		//
		final int ligneDepart = (int) positionDepart.getWidth();
		if ((ligneDepart < 1) || (ligneDepart > this.nbLignes)) {
			return null;
		}

		// Controler la validite de la colonne de depart
		//
		final int colonneDepart = (int) positionDepart.getHeight();
		if ((colonneDepart < 1) || (colonneDepart > this.nbColonnes)) {
			return null;
		}

		// Parcourir la matrice depuis la position de depart
		//
		final int xDepart = ligneDepart - 1;
		final int yDepart = colonneDepart - 1;

		for (int i = xDepart; i < this.nbLignes; i++) {
			for (int j = yDepart; j < this.nbColonnes; j++) {

				// Obtenir la cellule courante
				//
				celluleCourante = this.matriceCellules[i][j];

				// Determiner presence ou pas d'une image dans
				// la cellule courante
				//
				if (!celluleCourante.presenceImage()) {
					return new Position(i + 1, j + 1);
				}
			}
		}

		// Restituer le resultat en cas de saturation
		//
		return new Position(0, 0);
	}

	// --- Methode obtenirPositionCLS

	private Position obtenirPositionCOS(final Position positionDepart) {

		CelluleG celluleCourante;

		// Controler la validite du parametre
		//
		if (positionDepart == null) {
			return null;
		}

		// Controler la validite de la ligne de depart
		//
		final int ligneDepart = (int) positionDepart.getWidth();
		if ((ligneDepart < 1) || (ligneDepart > this.nbLignes)) {
			return null;
		}

		// Controler la validite de la colonne de depart
		//
		final int colonneDepart = (int) positionDepart.getHeight();
		if ((colonneDepart < 1) || (colonneDepart > this.nbColonnes)) {
			return null;
		}

		// Parcourir la matrice depuis la position de depart
		//
		final int xDepart = ligneDepart - 1;
		final int yDepart = colonneDepart - 1;

		for (int i = xDepart; i < this.nbLignes; i++) {
			for (int j = yDepart; j < this.nbColonnes; j++) {

				// Obtenir la cellule courante
				//
				celluleCourante = this.matriceCellules[i][j];

				// Determiner presence ou pas d'une image
				// dans la cellule courante
				//
				if (celluleCourante.presenceImage()) {
					return new Position(i + 1, j + 1);
				}
			}
		}

		// Restituer le resultat d'un solde vide
		//
		return new Position(0, 0);
	}

	// --- Methode obtenirPositionPCO

	private Position obtenirPositionPCL() {

		return this.obtenirPositionCLS(new Position(1, 1));
	}

	// --- Methode obtenirPositionCOS

	private Position obtenirPositionPCO() {

		return this.obtenirPositionCOS(new Position(1, 1));
	}

	// --- Methode obtenirPositionCellule

	/**
	 * @param p1
	 * @param p2
	 * @throws Throwable
	 */
	public void permuterImages(final Dimension p1, final Dimension p2)
					throws Throwable {

		int i1, i2, j1, j2;

		// Controler la validite des parametres
		//
		if (p1 == null) {
			throw new Throwable("-2.1");
		}
		if (p2 == null) {
			throw new Throwable("-2.2");
		}

		// Detailler les coordonnees des deux cellules cibles
		//
		i1 = (int) p1.getWidth() - 1;
		j1 = (int) p1.getHeight() - 1;
		i2 = (int) p2.getWidth() - 1;
		j2 = (int) p2.getHeight() - 1;

		// Obtenir les cellules cibles
		//
		final CelluleG cellule_1 = this.matriceCellules[i1][j1];
		final CelluleG cellule_2 = this.matriceCellules[i2][j2];

		// Determiner la presence d'images dans les cellules cibles
		//
		final boolean presenceImage_1 = cellule_1.presenceImage();
		final boolean presenceImage_2 = cellule_2.presenceImage();

		// Recueillir les chemins d'acces aux images presentes
		//
		String cheminImage_1 = null, cheminImage_2 = null;
		if (presenceImage_1) {
			cheminImage_1 = cellule_1.obtenirCheminImage();
		}
		if (presenceImage_2) {
			cheminImage_2 = cellule_2.obtenirCheminImage();
		}

		// Retirer les anciennes images des panneaux sous jacents
		//
		if (presenceImage_1) {
			this.matriceCellules[i1][j1].retirerImage();
		}
		if (presenceImage_2) {
			this.matriceCellules[i2][j2].retirerImage();
		}

		// Ajouter les nouvelles images dans les panneaux sous jacents
		//
		if (presenceImage_2) {
			this.matriceCellules[i1][j1].ajouterImage(cheminImage_2);
		}
		if (presenceImage_1) {
			this.matriceCellules[i2][j2].ajouterImage(cheminImage_1);
		}
	}

	// --- Methode observerCellule

	/**
	 * @throws Throwable
	 */
	public void rassemblerImages() throws Throwable {

		Position positionCOS;

		// Traiter le cas particulier d'une mosaique vide de toute image
		//
		if (this.obtenirNbImages() == 0) {
			return;
		}

		// Traiter le cas particulier d'une saturation de la mosaique
		//
		if (this.obtenirNbCellules() == this.obtenirNbImages()) {
			return;
		}

		// Obtenir la position de la premiere cellule libre
		//
		Position positionPCL = this.obtenirPositionPCL();

		// Obtenir la position de depart des permutations
		//
		Position positionDepart = positionPCL.suivante();

		// Traiter toutes les cellules occupees non encore rassemblees
		//

		while (positionDepart.valide()) {

			// Obtenir la position de la premiere cellule occupee au dela de
			// la premiere cellule libre
			//
			positionCOS = this.obtenirPositionCOS(positionPCL.suivante());
			if (!positionCOS.valide()) {
				return;
			}

			// Permuter la premiere cellule libre et la cellule occupee suivante
			//
			this.permuterImages(positionPCL, positionCOS);

			// Obtenir la position de la premiere cellule libre
			//
			positionPCL = this.obtenirPositionPCL();

			// Obtenir la nouvelle position de depart des permutations
			//
			positionDepart = positionPCL.suivante();
		}
	}

	// ----------------------------------------------- Classe interne CelluleG

	/**
	 * @param ligne
	 * @param colonne
	 * @return boolean
	 * @throws Throwable
	 */
	public boolean retirerImage(final int ligne, final int colonne)
					throws Throwable {

		// Controler la validite des parametres
		//
		if ((ligne < 1) || (ligne > this.nbLignes)) {
			throw new Throwable("-2.1");
		}
		if ((colonne < 1) || (colonne > this.nbColonnes)) {
			throw new Throwable("-2.2");
		}

		// Ajouter l'image au panneau cible
		//
		final CelluleG celluleCible = this.matriceCellules[ligne - 1][colonne - 1];
		return celluleCible.retirerImage();
	}

	// ------------------------------------ Classe interne Position

	private void setMatrice(final HashMap<String, Integer> config)
					throws Throwable {

		// Creer la matrice des cellules internes
		//
		this.matriceCellules = new CelluleG[this.nbLignes][this.nbColonnes];

		// Remplir cette matrice et ajouter chaque sous panneau au
		// panneau principal
		//
		CelluleG celluleCible;
		for (int i = 0; i < this.nbLignes; i++) {
			for (int j = 0; j < this.nbColonnes; j++) {

				// Construire la cellule cible
				//
				celluleCible = new CelluleG(this, config, new Dimension(i + 1,
								j + 1));

				// Affecter la nouvelle cellule a la matrice
				//
				this.matriceCellules[i][j] = celluleCible;
			}
		}
	}
}
