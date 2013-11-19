package views;

//
// IUT de Nice / Departement informatique / Module APO-Java
// Annee 2011_2012 - Composants generiques
//
// Classe VueCalqueG - Panneau generique de dessin au format objets
// (MVC - partie Vue)
//
// Edition A : extension de l'ancienne classe ClaqueG
// (V 1.1.0 / 2010_2011)
//
// + Version 1.0.0 : version initiale adaptee au composant _PanneauG (MVC)
// (le dessin d'images ne fonctionne pas !)
// + Version 1.1.0 : introduction de la gestion d'exceptions
// + Version 1.2.0 : transfert des methodes publiques enregistrerDessin
// et chargerdessin vers la partie Modele d'un calque
// + suppression de la methode publique chargerDessin
// + ajout de la methode publique modifierDessin
// + Version 1.3.0 : modification signature de la methode creerOG pour
// adaptation au nouveau modele
// + modification signature methode ajouterComposant
// + suppression provisoire methode retirerComposant
// + transfert methode chargerImage dans ComposantG
// + ajout attribut image a la classe ComposantG et
// modification signature chargerImage
// + Version 1.4.0 : ajout d'un nouveau constructeur
// + Version 1.5.0 : la classe interne ComposantG devient privee
// + ajout de la methode obtenirTypeSource
// + simplification methode privee dessiner
// + ajout dessin des cercles (methode dessinerCercle)
// + Version 1.6.0 : la methode ajouterComposant devient ajouterOG
// + la methode creerOG devient privee
//
// Edition B : gestion de la suppression de composants
//
// + Version 2.0.0 : introduction de la methode retirerOG (anciennement
// retirerComposant), avec designation par l'origine
// + ajout accesseur obtenirNbOG
// + ajout methode publique chargerDessin
// + modification methode publique modifierDessin,
// qui remplace maintenant la liste courante des
// composants par une nouvelle
// + Version 2.1.0 : ajout suppression par le type (autre signature de
// la methode retirerOG)
// + Version 2.2.0 : ajout suppression combinee par le type et par
// l'origine
//
// Edition C : possibilite de maillage rectangulaire regulier
//
// + Version 3.0.0 : introduction methode publique ajouterMaillage
// + modification methode paint pour dessin maillage
// + ajout d'une classe interne dediee Trame
// + Version 3.1.0 : modification du second constructeur normal
// + finalisation de la gestion d'exceptions
// + Version 3.2.0 : ajout methode publique retirerMaillage
// + Version 3.3.0 : ajout accesseurs publics obtenirNbLignesMaillage
// et obtenirNbColonnesMaillage
// + ajout methodes masquerMaille et montrerMaille
// + ajout dans la classe interne Trame accesseurs
// obtenirNbLignes et obtenirNbColonnes
// + Version 3.4.0 : ajout accesseurs publics presenceMaillage,
// obtenirOrigineMaille et obtenirTailleMaille
// + ajout associe dans les classes internes
// + Version 3.5.0 : introduction couleur de fond propre a chaque
// maille (classe interne Maille)
// + ajout methodes colorierMaille et gommerMaille
// + ajouts induits dans les classes internes
// + Version 3.6.0 : ajout signature methode ajouterMaillage pour
// prise en compte d'un parametre tailleMaille
// + ajout constructeur normal classe interne Trame
//
//
// Edition D : mise en place identification des OG par rapport aux
// positions dans le calque
//
// + Version 4.0.0 : introduction accesseur obtenirPositionMaille
// + ajouts induits dans les classes internes
// Trame et Maille
//
// Auteur : A. Thuaire

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JFrame;

public class VueCalqueG extends VuePanneauG {

	private class ComposantG {

		private Dimension	coinNO;
		private Dimension	gabarit;
		private Object		source;
		private Color		couleurTrait;
		private Image		image;

		// --- Constructeur de points

		public ComposantG(final Dimension position) {

			this.coinNO = position;
		}

		// --- Constructeur de segments

		public ComposantG(final Dimension origine, final Dimension extremite) {

			this.coinNO = origine;
			this.source = extremite;
		}

		// --- Constructeur de rectangles

		public ComposantG(final Dimension origine, final Dimension gabarit,
						final String cheminImage) throws Throwable {

			// Charger l'image cible depuis son fichier d'origine
			//
			this.chargerImage(cheminImage);

			// Memoriser les attributs
			//
			this.coinNO = origine;
			this.gabarit = gabarit;
			this.source = this.image;
		}

		// --- Constructeur de labels textuels

		public ComposantG(final Dimension origine, final int rayon) {

			this.coinNO = origine;
			this.source = new Integer(rayon);
		}

		// --- Constructeur d'images

		public ComposantG(final Dimension origine, final int longueur,
						final int largeur) {

			this.coinNO = origine;
			this.gabarit = new Dimension(longueur, largeur);
		}

		// --- Constructeur de polygones

		public ComposantG(final Dimension origine, final String texte) {

			this.coinNO = origine;
			this.source = texte;
		}

		// --- Constructeur de cercles

		public ComposantG(final Polygon polygone) {

			this.source = polygone;
		}

		// --- Methode chargerImage

		private void chargerImage(final String cheminImage) throws Throwable {

			// Controler la validite du parametre
			//
			if (cheminImage == null) {
				throw new Throwable("-2.1");
			}

			// Charger l'image cible (format jpeg)
			//
			this.image = Toolkit.getDefaultToolkit().getImage(cheminImage);

			// Construire un media tracker pour controler le chargement de
			// l'image
			//
			final MediaTracker mt = new MediaTracker(
							(JFrame) VueCalqueG.this.hamecon);

			// Attendre la fin du chargement effectif de l'image
			//
			mt.addImage(this.image, 0);
			try {
				mt.waitForAll();
			} catch (final Exception e) {
			}
		}

		// --- Methode dessiner

		public void dessiner(final Graphics g) throws Throwable {

			// Obtenir le type de la source
			//
			final String typeSource = this.obtenirTypeSource();

			// Traiter le composant suivant son type
			//
			if (typeSource.equals("Point")) {
				this.dessinerPoint(g);
				return;
			}
			if (typeSource.equals("Segment")) {
				this.dessinerSegment(g);
				return;
			}
			if (typeSource.equals("Rectangle")) {
				this.dessinerRectangle(g);
				return;
			}
			if (typeSource.equals("Label")) {
				this.dessinerLabel(g);
				return;
			}
			if (typeSource.equals("Image")) {
				this.dessinerImage(g);
				return;
			}
			if (typeSource.equals("Polygone")) {
				this.dessinerPolygone(g);
				return;
			}
			if (typeSource.equals("Cercle")) {
				this.dessinerCercle(g);
				return;
			}

			throw new Throwable("-3.0.2");
		}

		// --- Methode dessinerPoint

		private void dessinerCercle(final Graphics g) {

			Color couleurInitiale = null;

			// Sauvegarder la couleur initiale de trait
			//
			couleurInitiale = g.getColor();

			// Obtenir la couleur du cercle a dessiner
			//
			final Color couleurCourante = this.couleurTrait;

			// Modifier la couleur courante de dessin dans le calque
			//
			if (couleurCourante != null) {
				g.setColor(couleurCourante);
			}

			// Obtenir les coordonnees du centre du cercle
			//
			final int x1 = (int) this.coinNO.getWidth();
			final int y1 = (int) this.coinNO.getHeight();

			// Obtenir le rayon du cercle
			//
			final int rayon = ((Integer) this.source).intValue();

			// Dessiner le cercle cible
			//
			g.drawOval(x1 - rayon, y1 - rayon, rayon * 2, rayon * 2);

			// Restaurer la couleur initiale de dessin dans le calque
			//
			if (couleurCourante != null) {
				g.setColor(couleurInitiale);
			}
		}

		// --- Methode dessinerSegment

		private void dessinerImage(final Graphics g) {

			// Obtenir les coordonnees du coin Nord Ouest de la zone
			// de dessin
			//
			final int x0 = (int) this.coinNO.getWidth();
			final int y0 = (int) this.coinNO.getHeight();

			// Obtenir les coordonnees du point extremite du rectangle
			// de dessin
			//
			final int largeur = (int) this.gabarit.getWidth();
			final int hauteur = (int) this.gabarit.getHeight();

			// Dessiner l'image sur le calque
			//
			if (this.image == null) {
				return;
			}
			g.drawImage(this.image, x0, y0, largeur, hauteur, null);

		}

		// --- Methode dessinerRectangle

		private void dessinerLabel(final Graphics g) {

			Color couleurInitiale = null;

			// Sauvegarder la couleur initiale de trait
			//
			couleurInitiale = g.getColor();

			// Obtenir la couleur du segment a dessiner
			//
			final Color couleurCourante = this.couleurTrait;

			// Modifier la couleur courante de dessin dans le calque
			//
			if (couleurCourante != null) {
				g.setColor(couleurCourante);
			}

			// Obtenir le texte du label
			//
			final String label = (String) this.source;

			// Obtenir les coordonnees du coin Nord Ouest de la zone de dessin
			//
			final int x = (int) this.coinNO.getWidth();
			final int y = (int) this.coinNO.getHeight();

			// Dessiner le label depuis l'origine de la zone de dessin
			//
			g.drawString(label, x, y);

			// Restaurer la couleur initiale de dessin dans le calque
			//
			if (couleurCourante != null) {
				g.setColor(couleurInitiale);
			}
		}

		// --- Methode dessinerLabel

		private void dessinerPoint(final Graphics g) {

			Color couleurInitiale = null;

			// Sauvegarder la couleur initiale de trait
			//
			couleurInitiale = g.getColor();

			// Obtenir la couleur du segment a dessiner
			//
			final Color couleurCourante = this.couleurTrait;

			// Modifier la couleur courante de dessin dans le calque
			//
			if (couleurCourante != null) {
				g.setColor(couleurCourante);
			}

			// Obtenir les coordonnees du point cible
			//
			final int x1 = (int) this.coinNO.getWidth();
			final int y1 = (int) this.coinNO.getHeight();

			// Dessiner le point cible
			//
			g.drawLine(x1, y1, x1, y1);

			// Restaurer la couleur initiale de dessin dans le calque
			//
			if (couleurCourante != null) {
				g.setColor(couleurInitiale);
			}
		}

		// --- Methode dessinerPolygone

		private void dessinerPolygone(final Graphics g) {

			Color couleurInitiale = null;

			// Sauvegarder la couleur initiale de trait
			//
			couleurInitiale = g.getColor();

			// Obtenir la couleur du segment a dessiner
			//
			final Color couleurCourante = this.couleurTrait;

			// Modifier la couleur courante de dessin dans le calque
			//
			if (couleurCourante != null) {
				g.setColor(couleurCourante);
			}

			// Obtenir le polygone a dessiner
			//
			final Polygon polygone = (Polygon) this.source;

			// Obtenir le sommet origine du polygone
			//
			final int x0 = polygone.xpoints[0];
			final int y0 = polygone.ypoints[0];

			// Parcourir tous les sommets du polygone
			//
			int x1, y1, x2, y2;

			x1 = x0;
			y1 = y0;
			for (int i = 1; i < polygone.npoints; i++) {

				// Obtenir le sommet courant
				//
				x2 = polygone.xpoints[i];
				y2 = polygone.ypoints[i];

				// Dessiner l'arete courante
				//
				g.drawLine(x1, y1, x2, y2);

				// Modifier le sommet origine pour l'arete suivante
				//
				x1 = x2;
				y1 = y2;
			}

			// Dessiner la derniere arete
			//
			g.drawLine(x1, y1, x0, y0);

			// Restaurer la couleur initiale de dessin dans le calque
			//
			if (couleurCourante != null) {
				g.setColor(couleurInitiale);
			}
		}

		// --- Methode dessinerImage

		private void dessinerRectangle(final Graphics g) {

			Color couleurInitiale = null;

			// Sauvegarder la couleur initiale de trait
			//
			couleurInitiale = g.getColor();

			// Obtenir la couleur du segment a dessiner
			//
			final Color couleurCourante = this.couleurTrait;

			// Modifier la couleur courante de dessin dans le calque
			//
			if (couleurCourante != null) {
				g.setColor(couleurCourante);
			}

			// Obtenir les coordonnees du coin Nord Ouest de la zone de dessin
			//
			final int x1 = (int) this.coinNO.getWidth();
			final int y1 = (int) this.coinNO.getHeight();

			// Obtenir la longueur et la largeur
			//
			final int longueur = (int) this.gabarit.getWidth();
			final int largeur = (int) this.gabarit.getHeight();

			// Dessiner le rectangle cible
			//
			g.drawLine(x1, y1, x1 + longueur, y1);
			g.drawLine(x1 + longueur, y1, x1 + longueur, y1 + largeur);
			g.drawLine(x1 + longueur, y1 + largeur, x1, y1 + largeur);
			g.drawLine(x1, y1 + largeur, x1, y1);

			// Restaurer la couleur initiale de dessin dans le calque
			//
			if (couleurCourante != null) {
				g.setColor(couleurInitiale);
			}
		}

		// --- Methode dessinerCercle

		private void dessinerSegment(final Graphics g) {

			Color couleurInitiale = null;

			// Sauvegarder la couleur initiale de trait
			//
			couleurInitiale = g.getColor();

			// Obtenir la couleur du segment a dessiner
			//
			final Color couleurCourante = this.couleurTrait;

			// Modifier la couleur courante de dessin dans le calque
			//
			if (couleurCourante != null) {
				g.setColor(couleurCourante);
			}

			// Obtenir les coordonnees du coin Nord Ouest de la zone de dessin
			//
			final int x1 = (int) this.coinNO.getWidth();
			final int y1 = (int) this.coinNO.getHeight();

			// Obtenir les coordonnees du point extremite
			//
			final int x2 = (int) ((Dimension) this.source).getWidth();
			final int y2 = (int) ((Dimension) this.source).getHeight();

			// Dessiner le segment cible
			//
			g.drawLine(x1, y1, x2, y2);

			// Restaurer la couleur initiale de dessin dans le calque
			//
			if (couleurCourante != null) {
				g.setColor(couleurInitiale);
			}
		}

		// --- Methode fixerCouleurTrait

		private void fixerCouleurTrait(final HashMap description) {

			// Obtenir la couleur de trait
			//
			final Color couleur = (Color) description.get("couleur");
			if (couleur == null) {
				return;
			}

			this.couleurTrait = couleur;
		}

		// --- Accesseur obtenirTypeSource

		public String obtenirTypeSource() throws Throwable {

			// Traiter le cas d'un simple point
			//
			if ((this.source == null) && (this.gabarit == null)) {
				return "Point";
			}

			// Traiter le cas d'un rectangle
			//
			if ((this.source == null) && (this.gabarit != null)) {
				return "Rectangle";
			}

			// Obtenir le type de la source du composant a dessiner
			//
			String typeSource = null;
			if (this.source != null) {
				typeSource = this.source.getClass().getName();
			}

			if (typeSource == null) {

				throw new NullPointerException("typeSource est null");

			}
			// Traiter le cas d'un segment
			//
			if ((this.gabarit == null)
							&& typeSource.equals("java.awt.Dimension")) {
				return "Segment";
			}

			// Traiter le cas d'un label textuel
			//
			if ((this.gabarit == null) && typeSource.equals("java.lang.String")) {
				return "Label";
			}

			// Traiter le cas d'une image
			//
			if ((this.gabarit != null)
							&& typeSource.equals("sun.awt.windows.WImage")) {
				return "Image";
			}

			// Traiter le cas d'un polygone
			//
			if (typeSource.equals("java.awt.Polygon")) {
				return "Polygone";
			}

			// Traiter le cas d'un cercle
			//
			if (typeSource.equals("java.lang.Integer")) {
				return "Cercle";
			}

			throw new Throwable("-3.0");
		}
	}

	private class Trame {

		private class Maille {

			private final int	positionLigne;
			private final int	positionColonne;
			private Color		couleurFond;

			public Maille(final int ligne, final int colonne) {

				// Memoriser les attributs transmis par parametre
				//
				this.positionLigne = ligne;
				this.positionColonne = colonne;
			}

			// --- Methode setCouleurFond

			public void dessiner(final Graphics g) {

				// Construire le polygone attache a la maille support
				//
				final Polygon polygone = this.obtenirPolygone();

				// Traiter le cas d'un maillage couvrant colore
				//
				if (this.couleurFond != null) {
					g.setColor(this.couleurFond);
					g.fillPolygon(polygone.xpoints, polygone.ypoints,
									Trame.this.aritePolygone);
				}

				// Modifier la couleur courante du trait de dessin
				//
				g.setColor(Trame.this.couleurTrait);

				// Dessiner les bords de la maille courante
				//
				g.drawPolygon(polygone);
			}

			// --- Methode obtenirPolygone

			public boolean obtenirMaille(final int x, final int y) {

				// Construire le polygone attache a la maille cible
				//
				final Polygon polygone = this.obtenirPolygone();

				// Determiner si le point cible (x, y) appartient a
				// cette maille
				//
				if (polygone.contains(x, y)) {
					return true;
				}

				return false;
			}

			// --- Methode dessiner

			private Polygon obtenirPolygone() {

				// Obtenir la taille courante du panneau support
				//
				final int largeurPanneau = Trame.this.hamecon.getWidth();
				final int hauteurPanneau = Trame.this.hamecon.getHeight();

				// Determiner les dimensions de l'enveloppe de la maille
				// elementaire
				//
				final int largeurMaille = largeurPanneau
								/ Trame.this.nbColonnes;
				final int hauteurMaille = hauteurPanneau / Trame.this.nbLignes;

				// Determiner les abscisses des sommets de la maille cible
				//
				final int[] abscisses = new int[Trame.this.aritePolygone];

				abscisses[0] = (this.positionColonne - 1) * largeurMaille;
				abscisses[1] = abscisses[0];
				abscisses[2] = abscisses[0] + largeurMaille;
				abscisses[3] = abscisses[2];

				// Determiner les ordonnees des sommets de la maille cible
				//
				final int[] ordonnees = new int[Trame.this.aritePolygone];

				ordonnees[0] = (this.positionLigne - 1) * hauteurMaille;
				ordonnees[1] = ordonnees[0] + hauteurMaille;
				ordonnees[2] = ordonnees[1];
				ordonnees[3] = ordonnees[0];

				// Construire le polygone associe a la maille cible
				//
				final Polygon polygone = new Polygon(abscisses, ordonnees,
								Trame.this.aritePolygone);

				// Restituer le resultat
				//
				return polygone;
			}

			// --- Methode obtenirMaille

			public Point obtenirPositionMaille(final int x, final int y) {

				// Construire le polygone attache a la maille cible
				//
				final Polygon polygone = this.obtenirPolygone();

				// Determiner si le point cible (x, y) appartient au polygone
				//
				if (polygone.contains(x, y)) {
					return new Point(this.positionLigne, this.positionColonne);
				}

				return null;
			}

			// --- Methode obtenirPositionMaille

			public void setCouleurFond(final Color couleur) {

				this.couleurFond = couleur;
			}
		}

		private final VuePanneauG	hamecon;
		private final Color			couleurTrait;
		private final Color			couleurFond;
		private final int			nbLignes;
		private final int			nbColonnes;
		private final int			aritePolygone	= 4;

		// --- Premier constructeur normal

		private Maille[][]			matriceMailles;

		// --- Second constructeur normal

		public Trame(final VuePanneauG hamecon, final Color couleurTrait,
						final Color couleurFond, final Dimension tailleMaille)
						throws Throwable {

			// Memoriser les attributs associes aux trois premiers
			// parametres
			//
			this.hamecon = hamecon;
			this.couleurTrait = couleurTrait;
			this.couleurFond = couleurFond;

			// Calculer et meoriser le nombre de lignes
			//
			this.nbLignes = this.calculerNbLignes(tailleMaille);

			// Calculer et meoriser le nombre de colonness
			//
			this.nbColonnes = this.calculerNbColonnes(tailleMaille);

			// Creer et initialiser la matrice des mailles
			//
			this.creerMatriceMailles();
		}

		// --- Methode creerMatriceMailles

		public Trame(final VuePanneauG hamecon, final Color couleurTrait,
						final Color couleurFond, final int nbLignes,
						final int nbColonnes) {

			// Memoriser les attributs transmis par parametre
			//
			this.hamecon = hamecon;
			this.couleurTrait = couleurTrait;
			this.couleurFond = couleurFond;
			this.nbLignes = nbLignes;
			this.nbColonnes = nbColonnes;

			// Creer et initialiser la matrice des mailles
			//
			this.creerMatriceMailles();
		}

		// --- Methode calculerNbLignes

		private boolean ajouterMaille(final int ligne, final int colonne) {

			// Controler la validite (absence prealable) de l'ajout
			//
			if (this.matriceMailles[ligne - 1][colonne - 1] != null) {
				return false;
			}

			// Creer et affecter la nouvelle maille
			//
			this.matriceMailles[ligne - 1][colonne - 1] = new Maille(ligne,
							colonne);

			// Repeindre le panneau support
			//
			this.hamecon.repaint();

			return true;
		}

		// --- Methode calculerNbColonnes

		private int calculerNbColonnes(final Dimension tailleMaille)
						throws Throwable {

			// Obtenir la largeur du panneau support
			//
			final int largeurPanneau = this.hamecon.getWidth();

			// Extraire la largeur de maille imposee
			//
			final int largeurMaille = (int) tailleMaille.getWidth();

			// Controler la validite de la largeur imposee
			//
			if ((largeurMaille <= 0) || (largeurMaille > largeurPanneau)) {
				throw new Throwable("-3.1.1");
			}

			// Calculer et restituer le nombre de colonnes
			//
			return (largeurPanneau / largeurMaille) + 1;
		}

		// --- Methode obtenirNbLignes

		private int calculerNbLignes(final Dimension tailleMaille)
						throws Throwable {

			// Obtenir la hauteur du panneau support
			//
			final int hauteurPanneau = this.hamecon.getHeight();

			// Extraire la hauteur de maille imposee
			//
			final int hauteurMaille = (int) tailleMaille.getHeight();

			// Controler la validite de la hauteur imposee
			//
			if ((hauteurMaille <= 0) || (hauteurMaille > hauteurPanneau)) {
				throw new Throwable("-3.1.2");
			}

			// Calculer et restituer le nombre de lignes
			//
			return (hauteurPanneau / hauteurMaille) + 1;
		}

		// --- Methode obtenirNbColonnes

		public void colorierMaille(final Color couleur, final int ligne,
						final int colonne) {

			// Obtenir la reference Java de la maille cible
			//
			final Maille cible = this.matriceMailles[ligne - 1][colonne - 1];

			// Controler la presence effective de la maille cible
			//
			if (cible == null) {
				return;
			}

			// Modifier la couleur de fond de la cible
			//
			cible.setCouleurFond(couleur);

			VueCalqueG.this.repaint();
		}

		// --- Methode obtenirCouleurTrait

		private void creerMatriceMailles() {

			// Construire l'attribut correspondant
			//
			this.matriceMailles = new Maille[this.nbLignes][this.nbColonnes];

			// Remplir cette matrice en y ajoutant chacune des mailles
			//
			int i, j;
			for (i = 1; i <= this.nbLignes; i++) {
				for (j = 1; j <= this.nbColonnes; j++) {
					this.ajouterMaille(i, j);
				}
			}

			// Affecter la couleur de fond a chaque maille
			//
			Maille mailleCourante;
			for (i = 1; i <= this.nbLignes; i++) {

				for (j = 1; j <= this.nbColonnes; j++) {

					mailleCourante = this.matriceMailles[i - 1][j - 1];
					mailleCourante.setCouleurFond(this.couleurFond);
				}
			}
		}

		// --- Methode obtenirCouleurFond

		private void dessiner(final Graphics g) {

			// Dessiner chacune des mailles presentes
			//
			for (int i = 0; i < this.nbLignes; i++) {
				for (int j = 0; j < this.nbColonnes; j++) {
					if (this.matriceMailles[i][j] != null) {
						this.matriceMailles[i][j].dessiner(g);
					}
				}
			}
		}

		// --- Methode obtenirOrigineMaille

		public Color obtenirCouleurFond() {

			return this.couleurFond;
		}

		// --- Methode obtenirTailleMaille

		public Color obtenirCouleurTrait() {

			return this.couleurTrait;
		}

		// --- Methode obtenirPositionMaille

		private Dimension obtenirMaille(final int x, final int y) {

			// Parcourir la matrice des mailles
			//
			Maille mailleCourante = null;
			boolean designation = false;

			for (int i = 0; i < this.nbLignes; i++) {

				for (int j = 0; j < this.nbColonnes; j++) {

					// Obtenir la maille courante
					//
					mailleCourante = this.matriceMailles[i][j];

					// Determiner si la maille courante a ete neutralisee
					//
					if (mailleCourante == null) {
						continue;
					}

					// Determiner si le point cible (x, y) appartient
					// a la maille courante
					//
					designation = mailleCourante.obtenirMaille(x, y);
					if (designation) {
						return new Dimension(i + 1, j + 1);
					}
				}
			}

			return null;
		}

		// --- Methode ajouterMaille

		public int obtenirNbColonnes() {

			return this.nbColonnes;
		}

		// --- Methode retirerMaille

		public int obtenirNbLignes() {

			return this.nbLignes;
		}

		// --- Methode colorierMaille

		public Point obtenirOrigineMaille(final int ligne, final int colonne) {

			// Obtenir la reference Java de la maille cible
			//
			final Maille cible = this.matriceMailles[ligne - 1][colonne - 1];
			if (cible == null) {
				return null;
			}

			// Obtenir le polygone associe a la maille
			//
			final Polygon polygone = cible.obtenirPolygone();

			// Restituer l'origine du polygone
			//
			return new Point(polygone.xpoints[0], polygone.ypoints[0]);
		}

		// --- Methode dessiner

		public Point obtenirPositionMaille(final int x, final int y) {

			// Parcourir la matrice des mailles
			//
			Maille mailleCourante = null;
			Point position;

			for (int i = 0; i < this.nbLignes; i++) {

				for (int j = 0; j < this.nbColonnes; j++) {

					// Obtenir la maille courante
					//
					mailleCourante = this.matriceMailles[i][j];

					// Determiner si la maille courante a ete neutralisee
					//
					if (mailleCourante == null) {
						continue;
					}

					// Determiner si le point cible (x, y) appartient
					// a la maille courante
					//
					position = mailleCourante.obtenirPositionMaille(x, y);
					if (position != null) {
						return position;
					}
				}
			}

			return null;
		}

		// --- Methode obtenirMaille

		public Dimension obtenirTailleMaille() {

			// Obtenir la taille courante du panneau support
			//
			final int largeurPanneau = this.hamecon.getWidth();
			final int hauteurPanneau = this.hamecon.getHeight();

			// Determiner les dimensions de l'enveloppe de la maille elementaire
			//
			final int largeurMaille = largeurPanneau / this.nbColonnes;
			final int hauteurMaille = hauteurPanneau / this.nbLignes;

			// Restituer le resultat
			//
			return new Dimension(largeurMaille, hauteurMaille);
		}

		// ------------------------------------------- Classe interne Maille

		private boolean retirerMaille(final int ligne, final int colonne) {

			// Controler la validite (absence prealable) de l'ajout
			//
			if (this.matriceMailles[ligne - 1][colonne - 1] == null) {
				return false;
			}

			// Retirer la maille cible
			//
			this.matriceMailles[ligne - 1][colonne - 1] = null;

			// Repeindre le panneau support
			//
			this.hamecon.repaint();

			return true;
		}
	}

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	// --- Premier constructeur normal

	private LinkedList			composants;

	// --- Second constructeur normal

	private Trame				maillage;

	// --- Troisieme constructeur normal

	public VueCalqueG(final Object conteneur) throws Throwable {

		// Invoquer le constructeur de la classe mere
		//
		super(conteneur);

		// Initialiser la liste des composants graphiques
		//
		this.composants = new LinkedList();
	}

	// --- Methode obtenirNbOG

	public VueCalqueG(final Object conteneur, final HashMap config)
					throws Throwable {

		// Invoquer le constructeur de la classe mere
		//
		super(conteneur, config);

		// Controler la validite du second parametre
		//
		if (config == null) {
			throw new Throwable("-2.2");
		}

		// Initialiser la liste des composants graphiques
		//
		this.composants = new LinkedList();
	}

	// --- Methode presenceMaillage

	public VueCalqueG(final Object conteneur, final String cheminConfig,
					final String versionConfig) throws Throwable {

		// Invoquer le constructeur de la classe mere
		//
		super(conteneur, cheminConfig, versionConfig);

		// Initialiser la liste des composants graphiques
		//
		this.composants = new LinkedList();
	}

	// --- Methode obtenirNbLignesMaillage

	public void ajouterMaillage(final Color couleurTrait,
					final Color couleurFond, final Dimension tailleMaille)
					throws Throwable {

		// Controler la validite des parametres
		//
		if (couleurTrait == null) {
			throw new Throwable("-2.1");
		}
		if (tailleMaille == null) {
			throw new Throwable("-2.3");
		}

		// Construire et memoriser le maillage
		//
		this.maillage = new Trame(this, couleurTrait, couleurFond, tailleMaille);

		// Repeindre le panneau
		//
		this.repaint();
	}

	// --- Methode obtenirNbColonnesMaillage

	public void ajouterMaillage(final Color couleurTrait,
					final Color couleurFond, final int nbLignes,
					final int nbColonnes) throws Throwable {

		// Controler la validite des parametres
		//
		if (couleurTrait == null) {
			throw new Throwable("-2.1");
		}
		if (nbLignes <= 0) {
			throw new Throwable("-2.3");
		}
		if (nbColonnes <= 0) {
			throw new Throwable("-2.4");
		}

		// Construire et memoriser le maillage
		//
		this.maillage = new Trame(this, couleurTrait, couleurFond, nbLignes,
						nbColonnes);

		// Repeindre le panneau
		//
		this.repaint();
	}

	// --- Methode obtenirOrigineMaille

	public void ajouterOG(final HashMap description) throws Throwable {

		// Controler la validite du parametre
		//
		if (description == null) {
			throw new Throwable("-2.1");
		}

		// Creer le composant graphique qui correspond a la description
		//
		final ComposantG composant = this.creerOG(description);

		// Ajouter ce composant dans la liste courante
		//
		this.composants.addLast(composant);

		// Provoquer le rafraichissement du dessin
		//
		this.repaint();
	}

	// --- Methode obtenirTailleMaille

	public void chargerDessin(final LinkedList source) throws Throwable {

		// Controler la validite du parametre
		//
		if (source == null) {
			throw new Throwable("-2.1");
		}

		// Ajouter les composants de la source dans le calque courant
		//
		Object composant = null;

		final Iterator i = source.iterator();
		String typeOG = null;
		final String refOG = "java.util.HashMap";

		while (i.hasNext()) {

			// Obtenir le composant graphique courant dans la source
			//
			composant = i.next();

			// Controler le type de ce composant
			//
			typeOG = composant.getClass().getName();
			if (!typeOG.equals(refOG)) {
				throw new Throwable("-3.1");
			}

			// Ajouter ce composant dans la vue
			//
			this.ajouterOG((HashMap) composant);
		}
	}

	// --- Methode obtenirPositionMaille

	public void colorierMaille(final Color couleur, final int ligne,
					final int colonne) throws Throwable {

		// Controler la presence effective d'un maillage
		//
		if (!this.presenceMaillage()) {
			throw new Throwable("-3.0");
		}

		// Controler la validite des parametres
		//
		if (couleur == null) {
			throw new Throwable("-2.1");
		}
		final int nbLignes = this.maillage.obtenirNbLignes();
		if ((ligne <= 0) || (ligne > nbLignes)) {
			throw new Throwable("-2.2");
		}

		final int nbColonnes = this.maillage.obtenirNbColonnes();
		if ((colonne <= 0) || (ligne > nbColonnes)) {
			throw new Throwable("-2.3");
		}

		// Commander l'action a la classe interne Trame
		//
		this.maillage.colorierMaille(couleur, ligne, colonne);
	}

	// --- Methode paint

	private ComposantG construireCercle(final HashMap description)
					throws Throwable {

		// Obtenir la centre du cercle
		//
		final Dimension centre = (Dimension) description.get("centre");
		if (centre == null) {
			throw new Throwable("-3.7.1");
		}

		// Obtenir et controler le rayon du cercle
		//
		final Integer rayon = (Integer) description.get("rayon");
		if (rayon == null) {
			throw new Throwable("-3.7.2");
		}
		if (rayon.intValue() <= 0) {
			throw new Throwable("-3.7.3");
		}

		// Construire le cercle cible
		//
		final ComposantG composant = new ComposantG(centre, rayon.intValue());

		// Fixer la couleur de trait du composant
		//
		composant.fixerCouleurTrait(description);

		return composant;
	}

	// --- Methode ajouterOG

	private ComposantG construireImage(final HashMap description)
					throws Throwable {

		// Obtenir l'origine de l'image
		//
		final Dimension origine = (Dimension) description.get("origine");
		if (origine == null) {
			throw new Throwable("-3.5.1");
		}

		// Obtenir le gabarit de l'image
		//
		final Dimension gabarit = (Dimension) description.get("gabarit");
		if (gabarit == null) {
			throw new Throwable("-3.5.2");
		}

		// Obtenir le chemin d'acces au fichier image
		//
		final String cheminImage = (String) description.get("chemin");
		if (cheminImage == null) {
			throw new Throwable("-3.5.3");
		}

		// Construire l'image cible
		//
		final ComposantG composant = new ComposantG(origine, gabarit,
						cheminImage);

		return composant;
	}

	// --- Methode retirerOG

	// --- Suppression par l'origine

	private ComposantG construireLabel(final HashMap description)
					throws Throwable {

		// Obtenir l'origine du label
		//
		final Dimension origine = (Dimension) description.get("origine");
		if (origine == null) {
			throw new Throwable("-3.4.1");
		}

		// Obtenir le texte du label
		//
		final String texte = (String) description.get("texte");
		if (texte == null) {
			throw new Throwable("-3.4.2");
		}

		// Construire le label cible
		//
		final ComposantG composant = new ComposantG(origine, texte);

		// Fixer la couleur de trait du composant
		//
		composant.fixerCouleurTrait(description);

		return composant;
	}

	// --- Suppression par le type

	private ComposantG construirePoint(final HashMap description)
					throws Throwable {

		// Obtenir la position du point
		//
		final Dimension position = (Dimension) description.get("position");
		if (position == null) {
			throw new Throwable("-3.1");
		}

		// Construire le point cible
		//
		final ComposantG composant = new ComposantG(position);

		// Fixer la couleur de trait du composant
		//
		composant.fixerCouleurTrait(description);

		return composant;
	}

	// --- Suppression combinee par le type et par l'origine

	private ComposantG construirePolygone(final HashMap description)
					throws Throwable {

		// Obtenir la description du polygone
		//
		final Polygon polygone = (Polygon) description.get("polygone");
		if (polygone == null) {
			throw new Throwable("-3.6");
		}

		// Construire le polygone cible
		//
		final ComposantG composant = new ComposantG(polygone);

		// Fixer la couleur de trait du composant
		//
		composant.fixerCouleurTrait(description);

		return composant;
	}

	// --- Methode modifierDessin

	private ComposantG construireRectangle(final HashMap description)
					throws Throwable {

		// Obtenir l'origine du rectangle
		//
		final Dimension origine = (Dimension) description.get("origine");
		if (origine == null) {
			throw new Throwable("-3.3.1");
		}

		// Obtenir et controler la longueur du rectangle
		//
		final Integer longueur = (Integer) description.get("longueur");
		if (longueur == null) {
			throw new Throwable("-3.3.2");
		}
		if (longueur.intValue() <= 0) {
			throw new Throwable("-3.3.3");
		}

		// Obtenir et controler la largeur du rectangle
		//
		final Integer largeur = (Integer) description.get("largeur");
		if (largeur == null) {
			throw new Throwable("-3.3.4");
		}
		if (largeur.intValue() <= 0) {
			throw new Throwable("-3.3.5");
		}

		// Construire le rectangle cible
		//
		final ComposantG composant = new ComposantG(origine,
						longueur.intValue(), largeur.intValue());

		// Fixer la couleur de trait du composant
		//
		composant.fixerCouleurTrait(description);

		return composant;
	}

	// --- Methode chargerDessin

	private ComposantG construireSegment(final HashMap description)
					throws Throwable {

		// Obtenir l'origine du segment
		//
		final Dimension origine = (Dimension) description.get("origine");
		if (origine == null) {
			throw new Throwable("-3.2.1");
		}

		// Obtenir l'extremite du segment
		//
		final Dimension extremite = (Dimension) description.get("extremite");
		if (extremite == null) {
			throw new Throwable("-3.2.2");
		}

		// Construire le segment cible
		//
		final ComposantG composant = new ComposantG(origine, extremite);

		// Fixer la couleur de trait du composant
		//
		composant.fixerCouleurTrait(description);

		return composant;
	}

	// --- Methode creerOG

	private ComposantG creerOG(final HashMap description) throws Throwable {

		String type;
		final String texte, cheminImage;
		final Dimension position, origine, extremite, gabarit, centre;
		final Integer longueur, largeur, rayon;
		final Polygon polygone;

		final ComposantG composant;

		// Controler la validite du parametre
		//
		if (description == null) {
			throw new Throwable("-2.1");
		}

		// Obtenir le type du composant
		//
		type = (String) description.get("type");
		if (type == null) {
			throw new Throwable("-3.0.1");
		}

		// Construire le composant suivant son type
		//
		if (type.equals("Point")) {
			return this.construirePoint(description);
		}
		if (type.equals("Segment")) {
			return this.construireSegment(description);
		}
		if (type.equals("Rectangle")) {
			return this.construireRectangle(description);
		}
		if (type.equals("Label")) {
			return this.construireLabel(description);
		}
		if (type.equals("Image")) {
			return this.construireImage(description);
		}
		if (type.equals("Polygone")) {
			return this.construirePolygone(description);
		}
		if (type.equals("Cercle")) {
			return this.construireCercle(description);
		}

		throw new Throwable("-3.0.2");
	}

	// --- Methode construirePoint

	public void gommerMaille(final int ligne, final int colonne)
					throws Throwable {

		// Controler la presence effective d'un maillage
		//
		if (!this.presenceMaillage()) {
			throw new Throwable("-3.0");
		}

		// Controler la validite des parametres
		//
		final int nbLignes = this.maillage.obtenirNbLignes();
		if ((ligne <= 0) || (ligne > nbLignes)) {
			throw new Throwable("-2.1");
		}

		final int nbColonnes = this.maillage.obtenirNbColonnes();
		if ((colonne <= 0) || (ligne > nbColonnes)) {
			throw new Throwable("-2.2");
		}

		// Obtenir la couleur de fond du maillage courant
		//
		final Color couleur = this.maillage.obtenirCouleurFond();

		// Commander l'action a la classe interne Trame
		//
		this.maillage.colorierMaille(couleur, ligne, colonne);
	}

	// --- Methode construireSegment

	public void masquerMaille(final int ligne, final int colonne)
					throws Throwable {

		// Obtenir le nombre de lignes et de colonnes du maillage
		//
		final int nbLignes = this.maillage.obtenirNbLignes();
		final int nbColonnes = this.maillage.obtenirNbColonnes();

		// Controler la validite des parametres
		//
		if ((ligne < 1) || (ligne > nbLignes)) {
			throw new Throwable("-2.1");
		}
		if ((colonne < 1) || (colonne > nbColonnes)) {
			throw new Throwable("-2.2");
		}

		// Commander l'action a la classe interne Trame
		//
		this.maillage.retirerMaille(ligne, colonne);
	}

	// --- Methode construireRectangle

	public void modifierDessin(final LinkedList source) throws Throwable {

		// Controler la validite du parametre
		//
		if (source == null) {
			throw new Throwable("-2.1");
		}

		// Reinitialiser la liste des composants
		//
		this.composants = new LinkedList();

		// Charger la nouvelle description
		//
		this.chargerDessin(source);

		this.repaint();
	}

	// --- Methode construireLabel

	public void montrerMaille(final int ligne, final int colonne)
					throws Throwable {

		// Obtenir le nombre de lignes et de colonnes du maillage
		//
		final int nbLignes = this.maillage.obtenirNbLignes();
		final int nbColonnes = this.maillage.obtenirNbColonnes();

		// Controler la validite des parametres
		//
		if ((ligne < 1) || (ligne > nbLignes)) {
			throw new Throwable("-2.1");
		}
		if ((colonne < 1) || (colonne > nbColonnes)) {
			throw new Throwable("-2.1");
		}

		// Commander l'action a la classe interne Trame
		//
		this.maillage.ajouterMaille(ligne, colonne);
	}

	// --- Methode construireImage

	public int obtenirNbColonnesMaillage() throws Throwable {

		if (!this.presenceMaillage()) {
			throw new Throwable("-3.0");
		}
		return this.maillage.obtenirNbColonnes();
	}

	// --- Methode construirePolygone

	public int obtenirNbLignesMaillage() throws Throwable {

		if (!this.presenceMaillage()) {
			throw new Throwable("-3.0");
		}
		return this.maillage.obtenirNbLignes();
	}

	// --- Methode construireCercle

	public int obtenirNbOG() {

		return this.composants.size();
	}

	// --- Methode ajouterMaillage

	public Point obtenirOrigineMaille(final int ligne, final int colonne)
					throws Throwable {

		// Controler la presence effective d'un maillage
		//
		if (!this.presenceMaillage()) {
			throw new Throwable("-3.0");
		}

		// Controler la validite des parametres
		//
		final int nbLignes = this.maillage.obtenirNbLignes();
		if ((ligne <= 0) || (ligne > nbLignes)) {
			throw new Throwable("-2.1");
		}

		final int nbColonnes = this.maillage.obtenirNbColonnes();
		if ((colonne <= 0) || (ligne > nbColonnes)) {
			throw new Throwable("-2.2");
		}

		// Restituer le resultat
		//
		return this.maillage.obtenirOrigineMaille(ligne, colonne);
	}

	public Point obtenirPositionMaille(final int x, final int y)
					throws Throwable {

		if (!this.presenceMaillage()) {
			throw new Throwable("-3.0");
		}
		return this.maillage.obtenirPositionMaille(x, y);
	}

	// --- Methode retirerMaillage

	public Dimension obtenirTailleMaille() throws Throwable {

		if (!this.presenceMaillage()) {
			throw new Throwable("-3.0");
		}
		return this.maillage.obtenirTailleMaille();
	}

	// --- Methode montrerMaille

	@Override
	public void paint(final Graphics g) {

		super.paint(g);

		// Parcourir la liste des composants a dessiner
		//
		ComposantG composant = null;

		final Iterator i = this.composants.iterator();

		while (i.hasNext()) {

			// Dessiner le composant graphique courant
			//
			composant = (ComposantG) i.next();
			try {
				composant.dessiner(g);
			} catch (final Throwable e) {
			}
		}

		// Dessiner le maillage (eventuel) du calque
		//
		if (this.maillage != null) {
			this.maillage.dessiner(g);
		}
	}

	// --- Methode masquerMaille

	public boolean presenceMaillage() {

		return this.maillage != null;
	}

	// --- Methode colorierMaille

	public void retirerMaillage() {

		// Reinitialiser l'attribut
		//
		this.maillage = null;

		// Repeindre le panneau
		//
		this.repaint();
	}

	// --- Methode gommerMaille

	public LinkedList retirerOG(final Point cible) throws Throwable {

		// Controler la validite du parametre
		//
		if (cible == null) {
			throw new Throwable("-2.1");
		}

		// Creer la nouvelle description, expurgee de la cible du
		// retrait (eventuellement multiple !)
		//
		final LinkedList nouvelleDescription = new LinkedList();

		// Parcourir la liste de tous les composants existants
		//
		ComposantG composant = null;
		Point origine = null;
		Dimension coinNO = null;
		Polygon polygone;

		final Iterator i = this.composants.iterator();

		while (i.hasNext()) {

			// Obtenir le composant courant
			//
			composant = (ComposantG) i.next();

			// Obtenir le coinNO de ce composant
			//
			coinNO = composant.coinNO;

			// Traiter le cas particulier du polygone
			//
			if (coinNO == null) {

				// Obtenir l'origine du polygone (xpoints[0], ypoints[0])
				//
				polygone = (Polygon) composant.source;
				origine = new Point(polygone.xpoints[0], polygone.ypoints[0]);
			}

			else {

				// Transcoder le coin N/O en un point origine
				//
				origine = new Point((int) coinNO.getWidth(),
								(int) coinNO.getHeight());
			}

			// Comparer cette origine avec la cible de destruction
			//
			if (!origine.equals(cible)) {

				// Ajouter le composant courant a la nouvelle description
				//
				nouvelleDescription.addLast(composant);
			}
		}

		// Substituer le nouvelle description a l'ancienne
		//
		this.composants = nouvelleDescription;

		this.repaint();
		return this.composants;
	}

	// ---------------------------------------- Classe interne ComposantG

	public LinkedList retirerOG(final String typeCible) throws Throwable {

		// Controler la validite du parametre
		//
		if (typeCible == null) {
			throw new Throwable("-2.1");
		}

		// Creer la nouvelle description, expurgee de la cible du
		// retrait (eventuellement multiple !)
		//
		final LinkedList nouvelleDescription = new LinkedList();

		// Parcourir la liste de tous les composants existants
		//
		ComposantG composant = null;
		String typeCourant = null;

		final Iterator i = this.composants.iterator();

		while (i.hasNext()) {

			// Obtenir le composant courant
			//
			composant = (ComposantG) i.next();

			// Obtenir le type de ce composant
			//
			typeCourant = composant.obtenirTypeSource();

			// Comparer cette origine avec la cible de destruction
			//
			if (!typeCourant.equals(typeCible)) {

				// Ajouter le composant courant a la nouvelle description
				//
				nouvelleDescription.addLast(composant);
			}
		}

		// Substituer le nouvelle description a l'ancienne
		//
		this.composants = nouvelleDescription;

		this.repaint();
		return this.composants;
	}

	// ---------------------------------------- Classe interne Trame

	public LinkedList retirerOG(final String typeCible, final Point cible)
					throws Throwable {

		// Controler la validite des parametres
		//
		if (typeCible == null) {
			throw new Throwable("-2.1");
		}
		if (cible == null) {
			throw new Throwable("-2.2");
		}

		// Creer la nouvelle description, expurgee de la cible du
		// retrait (eventuellement multiple !)
		//
		final LinkedList nouvelleDescription = new LinkedList();

		// Parcourir la liste de tous les composants existants
		//
		ComposantG composant = null;
		String typeCourant = null;

		final Iterator i = this.composants.iterator();

		Point origine = null;
		Dimension coinNO = null;
		Polygon polygone;

		while (i.hasNext()) {

			// Obtenir le composant courant
			//
			composant = (ComposantG) i.next();

			// Obtenir le type de ce composant
			//
			typeCourant = composant.obtenirTypeSource();

			// Comparer cette origine avec la cible de destruction
			//
			if (!typeCourant.equals(typeCible)) {

				// Ajouter le composant courant a la nouvelle description
				//
				nouvelleDescription.addLast(composant);
			}

			else {

				// Obtenir le coinNO de ce composant
				//
				coinNO = composant.coinNO;

				// Traiter le cas particulier du polygone
				//
				if (coinNO == null) {

					// Obtenir l'origine du polygone (xpoints[0], ypoints[0])
					//
					polygone = (Polygon) composant.source;
					origine = new Point(polygone.xpoints[0],
									polygone.ypoints[0]);
				}

				else {

					// Transcoder le coin N/O en un point origine
					//
					origine = new Point((int) coinNO.getWidth(),
									(int) coinNO.getHeight());
				}

				// Comparer cette origine avec la cible de destruction
				//
				if (!origine.equals(cible)) {

					// Ajouter le composant courant a la nouvelle description
					//
					nouvelleDescription.addLast(composant);
				}
			}
		}

		// Substituer le nouvelle description a l'ancienne
		//
		this.composants = nouvelleDescription;

		this.repaint();
		return this.composants;
	}
}
