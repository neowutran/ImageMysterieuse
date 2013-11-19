package views;

//
// IUT de Nice / Departement informatique / Module APO-Java
// Annee 2011_2012 - Composants generiques
//
// Classe VuePanneauG - Panneau generique de visualisation (MVC - partie Vue)
//
// Edition Draft : mise en place du panneau
//
// + Version 0.0.0 : chargement du fichier de configuration
// + Version 0.1.0 : ajout d'une methode de chargement d'une image
// + Version 0.2.0 : ajout d'une surcharge de la methode paint
// + Version 0.3.0 : modification de la reference fournie au mediaTracker
// (OPERATIONNELLE !)
// + Version 0.4.0 : demonstrateur (main) avec deux instances
// + Version 0.5.0 : demonstrateur avec une vignette
//
// Edition A : visualisation d'une image de fond
//
// + Version 1.0.0 : image parametree depuis le fichier de configuration
// + Version 1.1.0 : ajout de la methode modifierImage
// + Version 1.2.0 : le panneau s'accroche automatiquement au cadre support
// + Version 1.3.0 : simplification du second constructeur normal par
// invocation du premier
// + Version 1.4.0 : ajout d'un constructeur pour le chargement automatique
// de la configuration
// + Version 1.5.0 : factorisation des traitements communs au second et au
// troisieme constructeur
//
// Edition B : dessin d'un titre dans un panneau
//
// + Version 2.0.0 : limitee aux panneaux sans image de fond
// + Version 2.1.0 : extension aux panneaux avec image de fond
//
// Edition C : gestion de la souris par un controleur approprie
//
// + Version 3.0.0 : "call back" independantes du contexte graphique du
// panneau observe et en classe interne
// + Version 3.1.0 : externalisation des "call back" attachees a la
// gestion de toutes les actions souris dans un
// controleur de panneau
// + Version 3.2.0 : externalisation complete du controleur (modele MVC),
// qui devient une classe generique a part entiere
// + Version 3.3.0 : memorisation du controleur sous forme d'attribut et
// modification du point d'entree pour montrer les
// capacites d'espionnage
//
// Edition D : evolutions fonctionnelles correlees avec celles du
// modele et/ou du controleur (MVC)
//
// + Version 4.0.0 : externalisation du demonstrateur(main) pour faciliter
// la gestion externe des trois composants MVC
//
// Edition E : extension de la configuration a la description
// (eventuellement recursive) de sous panneaux
//
// + Version 5.0.0 : ajout d'un attribut sousPanneaux et suppression
// provisoire de l'accrochage automatique au cadre
// support
// + Version 5.1.0 : mise en place d'un type generique pour l'hamecon et
// restauration de l'accrochage automatique au cadre
// + Version 5.2.0 : introduction de la methode ajouterPanneau
// + Version 5.3.0 : ajout des accesseurs obtenirNbSP, obtenirOrigineSP,
// obtenirTailleSP et obtenirSP (protected !)
// + Version 5.4.0 : les sous panneaux deviennent des instances de la
// classe PanneauG
// + Version 5.5.0 : introduction d'une nouvelle signature de la methode
// ajouterPanneau pour prolonger l'evolution precedente
// + Version 5.6.0 : gestion d'une nouvelle cle "sousPanneaux" (eventuelle)
// dans le fichier de configuration et ajout correle de
// la methode setSP (N niveaux de sous panneaux)
//
// Edition F : complements et extensions des methodes d'acces a tous les
// sous panneaux
//
// + Version 6.0.0 : extension de la methode modifierImage et ajout de la
// methode presenceImage (limitation au niveau 1)
// + Version 6.1.0 : extension de la methode obtenirSP a N niveaux de sous
// panneaux
// + Version 6.2.0 : introduction de la methode installerSP pour l'ajout
// dynamique de sous panneaux (sans recurrence) dans le
// panneau principal (niveau 1 seulement)
// + Version 6.3.0 : ajout d'une nouvelle signature de installerSP pour
// une extension a tout sous panneau (sans recurrence)
// + Version 6.4.0 : la methode ajouterPanneau devient privee
// + Version 6.5.0 : introduction de la methode deployerSP pour l'ajout
// dynamique de sous panneaux (avec recurrence) dans le
// panneau principal
// + Version 6.6.0 : ajout d'une nouvelle signature de deployerSP pour
// une extension a tout sous panneau (avec recurrence)
// + Version 6.7.0 : suppression de l'attribut controleur et modification
// du type du premier parametre du 3eme constructeur
//
// Edition G : fin de la mise en conformite MVC
//
// + Version 7.0.0 : introduction du prefixe Vue dans l'identificateur
// de la classe
// + Version 7.1.0 : consolidation des constructeurs et mise en place
// d'une gestion d'exceptions
// + Version 7.2.0 : extension des accesseurs de modification a tout
// sous panneau et ajout d'un accesseur public
// reconfigurer (panneau principal et sous panneaux)
// + modification signature ajouterTitre
// + acces "protected" pour l'attribut hamecon
// + la methode chargerImage devient privee
// + Version 7.3.0 : acces "protected" pour la methode configurer
//
// Auteur : A. Thuaire

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.MediaTracker;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;

import library.Config;

public class VuePanneauG extends JPanel {

	private static final long					serialVersionUID	= 1L;
	protected Object							hamecon;
	private Image								imageFond;

	private String								texteTitre;
	private Color								couleurTitre;
	private Font								policeTitre;

	private final LinkedHashMap<String, JPanel>	sousPanneaux;

	// ---                                                           Premier constructeur normal
	//
	public VuePanneauG(final Object conteneur) throws Throwable {

		// Controler la validite du parametre
		//
		if (conteneur == null) {
			throw new Throwable("-2.1");
		}

		// Memoriser l'hamecon vers le conteneur amont
		//
		this.hamecon = conteneur;

		// Definir un arriere plan jaune
		//
		this.setBackground(Color.yellow);

		// Definir un avant plan rouge
		//
		this.setForeground(Color.red);

		// Installer un gestionnaire de presentation par defaut
		//
		this.setLayout(new GridLayout(1, 0));

		// Accrocher le panneau au conteneur amont
		//
		final String typeConteneur = conteneur.getClass().getName();

		if (typeConteneur.equals("javax.swing.JFrame")) {
			((JFrame) conteneur).getContentPane().add(this);

		} else if (typeConteneur.equals("views.Images")
						|| typeConteneur.equals("javax.swing.JPanel")) {
			((JPanel) conteneur).add(this);

		} else {
			((VuePanneauG) conteneur).add(this);
		}
		// Creer la description interne des sous panneaux eventuels
		//
		this.sousPanneaux = new LinkedHashMap<String, JPanel>();
	}

	// ---                                                           Second constructeur normal

	public VuePanneauG(final Object hamecon, final HashMap config)
					throws Throwable {

		// Executer une configuration par defaut
		//
		this(hamecon);

		// Controler la validite du second parametre
		//
		if (config == null) {
			throw new Throwable("-2.2");
		}

		// Configurer le panneau cree
		//
		this.configurer(config);
	}

	// ---                                                           Troisieme constructeur normal
	//
	public VuePanneauG(final Object hamecon, final String chemin,
					final String version) throws Throwable {

		// Executer le premier constructeur normal
		//
		this(hamecon);

		// Controler la validite des deux derniers parametres
		//
		if (chemin == null) {
			throw new Throwable("-2.2");
		}
		if (version == null) {
			throw new Throwable("-2.3");
		}

		// Charger la configuration du panneau
		//
		final Object config = Config.load(chemin, version);
		if (config == null) {
			new Throwable("-3.2");
		}
		// Controler la nature de la configuration
		//
		if (config != null) {
			final String typeD = config.getClass().getName();

			final String refD = "java.util.HashMap";

			if (!typeD.equals(refD)) {
				throw new Throwable("-3.5");
			}

			// Configurer le panneau cree
			//
		}
		this.configurer((HashMap) config);

	}

	// ---                                                           Methode setBackground
	//
	public void ajouterControleur(final Object c) throws Throwable {

		if (c == null) {
			throw new Throwable("-2.1");
		}

		this.addMouseListener((MouseListener) c);
		this.addMouseMotionListener((MouseMotionListener) c);
	}

	// ---                                                            Methode ajouterControleur
	//
	public void ajouterControleur(final String nomSP, final Object c)
					throws Throwable {

		final Object w;

		// Controler la validite des parametres
		//
		if (nomSP == null) {
			throw new Throwable("-2.1");
		}
		if (c == null) {
			throw new Throwable("-2.2");
		}

		// Obtenir la reference Java du sous panneau cible
		//
		final VuePanneauG cible = this.obtenirSP(nomSP);
		if (cible == null) {
			throw new Throwable("-3.4");
		}

		// Ajouter le controleur a la cible
		//
		cible.ajouterControleur(c);
	}
	
	// ---                                                           ajouterPanneau
	//
	private void ajouterPanneau(final String nomPanneau, final HashMap config)
					throws Throwable {

		VuePanneauG p;

		// Creer le panneau cible
		//
		if (config == null) {
			p = new VuePanneauG(this);
		} else {
			p = new VuePanneauG(this, config);
		}

		// Ajouter le panneau au nouveau conteneur amont
		//
		this.ajouterPanneau(nomPanneau, p);
	}

	// ---                                                           Methode setForeground
	//
	private void ajouterPanneau(final String nomPanneau, final JPanel p) {

		// Memoriser le panneau cible dans le dictionnaire des panneaux
		//
		this.sousPanneaux.put(nomPanneau, p);

		// Ajouter le panneau au conteneur amont
		//
		this.add(p);
	}

	// ---                                                           Methode ajouterTitre
	//
	public void ajouterTitre(final String titre) throws Throwable {

		// Controler la validite du parametre
		//
		if (titre == null) {
			throw new Throwable("-2.1");
		}

		// Renseigner les attributs associes au titre
		//
		this.texteTitre = titre;

		// Repeindre le panneau
		//
		this.repaint();
	}

	// ---                                                           Methode ajouterTitre
	//
	public void ajouterTitre(final String titre, final Color couleur,
					final Font police) throws Throwable {

		// Controler la validite des parametres
		//
		if (titre == null) {
			throw new Throwable("-2.1");
		}
		if (couleur == null) {
			throw new Throwable("-2.2");
		}
		if (police == null) {
			throw new Throwable("-2.3");
		}

		// Renseigner les attributs associes au titre
		//
		this.texteTitre = titre;
		this.couleurTitre = couleur;
		this.policeTitre = police;

		// Repeindre le panneau
		//
		this.repaint();
	}

	// ---                                                           Methode setFont
	//
	public void ajouterTitre(final String nomSP, final String titre)
					throws Throwable {

		final Object w;

		// Controler la validite des parametres
		//
		if (nomSP == null) {
			throw new Throwable("-2.1");
		}
		if (titre == null) {
			throw new Throwable("-2.2");
		}

		// Obtenir la reference Java du sous panneau cible
		//
		final VuePanneauG cible = this.obtenirSP(nomSP);
		if (cible == null) {
			throw new Throwable("-3.4");
		}

		// Appliquer la modification sur la cible
		//
		cible.ajouterTitre(titre);
	}

	// ---                                                           Methode ajouterTitre
	//
	public void ajouterTitre(final String nomSP, final String titre,
					final Color couleur, final Font police) throws Throwable {

		final Object w;

		// Controler la validite des parametres
		//
		if (nomSP == null) {
			throw new Throwable("-2.1");
		}
		if (titre == null) {
			throw new Throwable("-2.2");
		}
		if (couleur == null) {
			throw new Throwable("-2.3");
		}
		if (police == null) {
			throw new Throwable("-2.4");
		}

		// Obtenir la reference Java du sous panneau cible
		//
		final VuePanneauG cible = this.obtenirSP(nomSP);
		if (cible == null) {
			throw new Throwable("-3.4");
		}

		// Appliquer la modification sur la cible
		//
		cible.ajouterTitre(titre, couleur, police);
	}
	
	// ---                                                           Methode chargerImage
	//
	private void chargerImage(final String cheminImage) throws Throwable {

		// Controler la validite du parametre
		//
		if (cheminImage == null) {
			throw new Throwable("-2.1");
		}

		// Charger une image de fond depuis un fichier de type jpeg
		//
		this.imageFond = Toolkit.getDefaultToolkit().getImage(cheminImage);
		if (this.imageFond == null) {
			throw new Throwable("-3.3");
		}

		// Construire un media tracker pour controler le chargement de l'image
		//
		final MediaTracker mt = new MediaTracker(this);

		// Attendre la fin du chargement effectif de l'image
		//
		mt.addImage(this.imageFond, 0);
		try {
			mt.waitForAll();
		} catch (final Exception e) {
		}
	}

	// ---                                                           Methode setLayout
	//
	private void configurer(final HashMap config) throws Throwable {

		this.setBackground(config);
		this.setForeground(config);
		this.setFont(config);
		this.setLayout(config);
		this.setImage(config);
		this.setTitre(config);
		this.setSP(config);
	}
	
	// ---                                                           Methode deployerSP
	//
	public boolean deployerSP(final HashMap donnees) throws Throwable {

		// Controler la validite du parametre
		//
		if (donnees == null) {
			throw new Throwable("-2.1");
		}

		// Appliquer la strategie eventuelle de placement
		//
		final LayoutManager strategie = (LayoutManager) donnees
						.get("placement");
		if (strategie != null) {
			this.setLayout(strategie);
		}

		// Obtenir la description eventuelle de sous panneaux
		//
		final Object description = donnees.get("sousPanneaux");
		if (description == null) {
			return true;
		}

		// Controler le type de cette description
		//
		final String typeD = description.getClass().getName();
		final String refD = "java.util.LinkedHashMap";
		if (!typeD.equals(refD)) {
			throw new Throwable("-3.5");
		}

		// Parcourir le dictionnaire des sous panneaux du niveau courant
		//
		final LinkedHashMap niveauC = (LinkedHashMap) description;
		final Iterator i = niveauC.keySet().iterator();
		String cle = null;
		HashMap associe = null, configCourante = null;
		VuePanneauG cible;
		boolean status = false;

		while (i.hasNext()) {

			// Acquerir le nom du sous panneau courant
			//
			cle = (String) i.next();

			// Acquerir l'associe courant
			//
			associe = (HashMap) niveauC.get(cle);

			// Dupliquer l'associe
			//
			configCourante = (HashMap) associe.clone();

			// Retirer du clone une eventuelle cle "sousPanneaux" pour eviter
			// une recursion prematuree par les constructeurs
			//
			configCourante.remove("sousPanneaux");

			// Ajouter le panneau courant
			//
			this.ajouterPanneau(cle, configCourante);

			// Traiter le cas d'une recursion vers les niveaux inferieurs
			//
			if (associe.containsKey("sousPanneaux")) {

				// Obtenir la reference du sous panneau courant
				//
				cible = this.obtenirSP(cle);
				if (cible == null) {
					return false;
				}

				// Deployer par recursion tous les sous panneaux de niveaux
				// inferieurs
				//
				status = cible.deployerSP(associe);
				if (!status) {
					return false;
				}
			}
		}

		return true;
	}

	public boolean deployerSP(final String nomSP, final HashMap donnees)
					throws Throwable {

		// Controler la validite des parametres
		//
		if (nomSP == null) {
			throw new Throwable("-2.1");
		}
		if (donnees == null) {
			throw new Throwable("-2.2");
		}

		// Obtenir la reference du sous panneau cible
		//
		final VuePanneauG cible = this.obtenirSP(nomSP);

		// Traiter le cas de l'absence du sous panneau cible
		//
		if (cible == null) {
			throw new Throwable("-3.4");
		}

		// Installer les donnees dans la cible
		//
		return cible.deployerSP(donnees);
	}

	// --- Methode setImage

	private void dessinerTitre(final Graphics g) {

		// Controler la presence d'un titre
		//
		if (this.texteTitre == null) {
			return;
		}

		// Calculer les dimensions du titre
		//
		final FontMetrics fm = g.getFontMetrics();

		final int largeurTitre = fm.stringWidth(this.texteTitre);
		final int hauteurTitre = fm.getHeight();

		// Modifier la couleur courante de dessin dans le panneau
		//
		if (this.couleurTitre != null) {
			g.setColor(this.couleurTitre);
		}

		// Modifier la police courante du panneau
		//
		if (this.policeTitre != null) {
			g.setFont(this.policeTitre);
		}

		// Traiter le cas de l'absence d'image de fond
		//
		if (this.imageFond == null) {

			// Calculer les coordonnees du centre du panneau
			//
			final int abscisseCentre = this.getWidth() / 2;
			final int ordonneeCentre = this.getHeight() / 2;

			// Dessiner le texte
			//
			g.drawString(this.texteTitre, (this.getWidth() - largeurTitre) / 2,
							(this.getHeight() - hauteurTitre) / 2);
		} else {

			// Traiter le cas de la presence d'une image de fond
			//
			g.drawString(this.texteTitre, (this.getWidth() - largeurTitre) / 2,
							this.getHeight() - 10);
		}
	}

	public boolean installerSP(final LinkedHashMap description)
					throws Throwable {

		// Controler la validite des parametres
		//
		if (description == null) {
			throw new Throwable("-2.1");
		}

		// Parcourir la description des sous panneaux
		//
		final Iterator i = description.keySet().iterator();
		String cle = null;
		Object associe = null;
		String typeA = null;
		final String refA = "java.util.HashMap";
		final Object configCourante = null;

		while (i.hasNext()) {

			// Acquerir la cle courante
			//
			cle = (String) i.next();

			// Acquerir l'associe courant
			//
			associe = description.get(cle);

			// Controler le type de l'associe
			//
			typeA = associe.getClass().getName();
			if (!typeA.equals(refA)) {
				throw new Throwable("-3.5");
			}

			// Ajouter le sous panneau courant au panneau cible
			//
			this.ajouterPanneau(cle, (HashMap) configCourante);
		}

		return true;
	}

	public boolean installerSP(final String nomSP,
					final LinkedHashMap description) throws Throwable {

		// Controler la validite des parametres
		//
		if (nomSP == null) {
			throw new Throwable("-2.1");
		}
		if (description == null) {
			throw new Throwable("-2.2");
		}

		// Obtenir la reference du sous panneau cible
		//
		final VuePanneauG cible = this.obtenirSP(nomSP);

		// Traiter le cas de l'absence du sous panneau cible
		//
		if (cible == null) {
			throw new Throwable("-3.4");
		}

		// Installer la description dans la cible
		//
		return cible.installerSP(description);
	}

	// ---- Methode chargerImage

	public void modifierImage(final String cheminImage) throws Throwable {

		if (cheminImage == null) {
			this.imageFond = null;
			this.repaint();
			return;
		}

		this.chargerImage(cheminImage);

		this.repaint();
	}

	// ---- Methode paint

	public void modifierImage(final String nomSP, final String cheminImage)
					throws Throwable {

		// Traiter le cas particulier du panneau principal
		//
		if (nomSP == null) {
			this.modifierImage(cheminImage);
			this.repaint();
			return;
		}

		// Obtenir la reference Java du sous panneau cible
		//
		final VuePanneauG cible = this.obtenirSP(nomSP);
		if (cible == null) {
			throw new Throwable("-3.4");
		}

		// Appliquer la modification d'image sur la cible
		//
		cible.modifierImage(cheminImage);
		cible.repaint();
	}

	// --- Methode modifierImage

	public void modifierTitre(final String titre) {

		if (titre != null) {
			this.texteTitre = titre;
		} else {

			this.texteTitre = null;
			this.couleurTitre = null;
			this.policeTitre = null;
		}

		this.repaint();
	}

	public void modifierTitre(final String nomSP, final String titre)
					throws Throwable {

		final Object w;

		// Controler la validite des parametres
		//
		if (nomSP == null) {
			throw new Throwable("-2.1");
		}
		if (titre == null) {
			throw new Throwable("-2.2");
		}

		// Obtenir la reference Java du sous panneau cible
		//
		final VuePanneauG cible = this.obtenirSP(nomSP);
		if (cible == null) {
			throw new Throwable("-3.4");
		}

		// Appliquer la modification sur la cible
		//
		cible.modifierTitre(titre);
	}

	// --- Methode configurer

	public int obtenirNbSP() {

		return this.sousPanneaux.size();
	}

	// --- Methode setTitre

	public Point obtenirOrigineSP(final String nomPanneau) throws Throwable {

		if (nomPanneau == null) {
			throw new Throwable("-2.1");
		}

		final VuePanneauG cible = (VuePanneauG) this.sousPanneaux
						.get(nomPanneau);
		if (cible == null) {
			throw new Throwable("-3.4");
		}

		return cible.getLocation();
	}

	protected VuePanneauG obtenirSP(final String nomSP) throws Throwable {

		// Controler la validite du parametre
		//
		if (nomSP == null) {
			throw new Throwable("-2.1");
		}

		// Parcourir l'arborescence de tous les sous panneaux
		//
		final Iterator i = this.sousPanneaux.keySet().iterator();
		String cle = null;
		VuePanneauG associe = null;
		VuePanneauG resultat = null;

		while (i.hasNext()) {

			// Acquerir la cle courante
			//
			cle = (String) i.next();

			// Acquerir l'associe courant
			//
			associe = (VuePanneauG) this.sousPanneaux.get(cle);

			// Traiter la premiere condition d'arret de la recursion
			//
			if (cle.equals(nomSP)) {
				return associe;
			}

			// Traiter le niveau immediatement inferieur eventuel (recursion)
			//
			if (associe != null) {
				resultat = associe.obtenirSP(nomSP);
			}
			if (resultat != null) {
				return resultat;
			}
		}

		// Traiter le cas ou la cible est absente de l'arborescence
		//
		return null;
	}

	public Dimension obtenirTailleSP(final String nomPanneau) throws Throwable {

		if (nomPanneau == null) {
			throw new Throwable("-2.1");
		}

		final VuePanneauG cible = (VuePanneauG) this.sousPanneaux
						.get(nomPanneau);
		if (cible == null) {
			throw new Throwable("-3.4");
		}

		return cible.getSize();
	}

	// --- Methode ajouterTitre

	@Override
	public void paint(final Graphics g) {

		super.paint(g);

		// Dessiner l'image de fond eventuelle
		//
		if (this.imageFond != null) {
			g.drawImage(this.imageFond, 0, 0, this.getWidth(),
							this.getHeight(), null);
		}

		// Dessiner le titre eventuel
		//
		if (this.texteTitre != null) {
			this.dessinerTitre(g);
		}
	}

	public boolean presenceImage() {

		return this.imageFond != null;
	}

	// --- Methode ajouterTitre

	public boolean presenceImage(final String nomSP) throws Throwable {

		// Traiter le cas particulier du panneau principal
		//
		if (nomSP == null) {
			return this.presenceImage();
		}

		// Obtenir la reference Java du sous panneau cible
		//
		final VuePanneauG cible = this.obtenirSP(nomSP);
		if (cible == null) {
			return false;
		}

		// Rechercher la presence d'une image sur le sous panneau cible
		//
		return cible.presenceImage();
	}

	public void reconfigurer(final HashMap config) throws Throwable {

		this.setBackground(config);
		this.setForeground(config);
		this.setFont(config);
		this.setLayout(config);
		this.setImage(config);
		this.setTitre(config);
	}

	// --- Methode modifierTitre

	public void reconfigurer(final String nomSP, final HashMap config)
					throws Throwable {

		// Controler la validite du parametre
		//
		if (nomSP == null) {
			throw new Throwable("-2.1");
		}

		// Obtenir la reference du sous panneau cible
		//
		final VuePanneauG cible = this.obtenirSP(nomSP);

		// Traiter le cas de l'absence du sous panneau cible
		//
		if (cible == null) {
			throw new Throwable("-3.4");
		}

		// Reconfigurer la cible
		//
		cible.reconfigurer(config);
	}

	public void setBackground(final HashMap config) throws Throwable {

		Object w;

		if (config == null) {
			throw new Throwable("-2.1");
		}

		w = config.get("arrierePlan");
		if (w == null) {
			this.setBackground(Color.yellow);
			return;
		}
		this.setBackground((Color) w);
	}

	// --- Methode dessinerTitre

	public void setBackground(final String nomSP, final Color couleur)
					throws Throwable {

		// Controler la validite des parametres
		//
		if (nomSP == null) {
			throw new Throwable("-2.1");
		}
		if (couleur == null) {
			throw new Throwable("-2.2");
		}

		// Obtenir la reference Java du sous panneau cible
		//
		final VuePanneauG cible = this.obtenirSP(nomSP);
		if (cible == null) {
			throw new Throwable("-3.4");
		}

		// Appliquer la modification sur la cible
		//
		cible.setBackground(couleur);
	}

	// --- Methode ajouterControleur

	public void setBackground(final String nomSP, final HashMap config)
					throws Throwable {

		// Controler la validite des parametres
		//
		if (nomSP == null) {
			throw new Throwable("-2.1");
		}
		if (config == null) {
			throw new Throwable("-2.2");
		}

		// Obtenir la reference Java du sous panneau cible
		//
		final VuePanneauG cible = this.obtenirSP(nomSP);
		if (cible == null) {
			throw new Throwable("-3.4");
		}

		// Appliquer la modification sur la cible
		//
		cible.setBackground(config);
	}

	public void setFont(final HashMap config) throws Throwable {

		Object w;

		if (config == null) {
			throw new Throwable("-2.1");
		}

		w = config.get("police");
		if (w == null) {
			return;
		}
		this.setFont((Font) w);
	}

	// --- Methode ajouterPanneau

	public void setFont(final String nomSP, final Font police) throws Throwable {

		// Controler la validite des parametres
		//
		if (nomSP == null) {
			throw new Throwable("-2.1");
		}
		if (police == null) {
			throw new Throwable("-2.2");
		}

		// Obtenir la reference Java du sous panneau cible
		//
		final VuePanneauG cible = this.obtenirSP(nomSP);
		if (cible == null) {
			throw new Throwable("-3.4");
		}

		// Appliquer la modification sur la cible
		//
		cible.setFont(police);
	}

	public void setFont(final String nomSP, final HashMap config)
					throws Throwable {

		// Controler la validite des parametres
		//
		if (nomSP == null) {
			throw new Throwable("-2.1");
		}
		if (config == null) {
			throw new Throwable("-2.2");
		}

		// Obtenir la reference Java du sous panneau cible
		//
		final VuePanneauG cible = this.obtenirSP(nomSP);
		if (cible == null) {
			throw new Throwable("-3.4");
		}

		// Appliquer la modification sur la cible
		//
		cible.setFont(config);
	}

	// --- Methode obtenirNbSP

	public void setForeground(final HashMap config) throws Throwable {

		Object w;

		if (config == null) {
			throw new Throwable("-2.1");
		}

		w = config.get("avantPlan");
		if (w == null) {
			this.setForeground(Color.red);
			return;
		}
		this.setForeground((Color) w);
	}

	// --- Methode obtenirOrigineSP

	public void setForeground(final String nomSP, final Color couleur)
					throws Throwable {

		// Controler la validite des parametres
		//
		if (nomSP == null) {
			throw new Throwable("-2.1");
		}
		if (couleur == null) {
			throw new Throwable("-2.2");
		}

		// Obtenir la reference Java du sous panneau cible
		//
		final VuePanneauG cible = this.obtenirSP(nomSP);
		if (cible == null) {
			throw new Throwable("-3.4");
		}

		// Appliquer la modification sur la cible
		//
		cible.setForeground(couleur);
	}

	// --- Methode obtenirTailleSP

	public void setForeground(final String nomSP, final HashMap config)
					throws Throwable {

		// Controler la validite des parametres
		//
		if (nomSP == null) {
			throw new Throwable("-2.1");
		}
		if (config == null) {
			throw new Throwable("-2.2");
		}

		// Obtenir la reference Java du sous panneau cible
		//
		final VuePanneauG cible = this.obtenirSP(nomSP);
		if (cible == null) {
			throw new Throwable("-3.4");
		}

		// Appliquer la modification sur la cible
		//
		cible.setForeground(config);
	}

	// --- Methode obtenirSP

	public void setImage(final HashMap config) throws Throwable {

		Object w;

		if (config == null) {
			throw new Throwable("-2.1");
		}

		w = config.get("image");
		if (w != null) {
			this.chargerImage((String) w);
		}
	}

	// --- Methode setSP

	public void setImage(final String nomSP, final HashMap config)
					throws Throwable {

		// Controler la validite des parametres
		//
		if (nomSP == null) {
			throw new Throwable("-2.1");
		}
		if (config == null) {
			throw new Throwable("-2.2");
		}

		// Obtenir la reference Java du sous panneau cible
		//
		final VuePanneauG cible = this.obtenirSP(nomSP);
		if (cible == null) {
			throw new Throwable("-3.4");
		}

		// Appliquer la modification sur la cible
		//
		cible.setImage(config);
	}

	// --- Methode presenceImage

	public void setImage(final String nomSP, final String chemin)
					throws Throwable {

		// Controler la validite des parametres
		//
		if (nomSP == null) {
			throw new Throwable("-2.1");
		}
		if (chemin == null) {
			throw new Throwable("-2.2");
		}

		// Obtenir la reference Java du sous panneau cible
		//
		final VuePanneauG cible = this.obtenirSP(nomSP);
		if (cible == null) {
			throw new Throwable("-3.4");
		}

		// Appliquer la modification sur la cible
		//
		cible.chargerImage(chemin);
	}

	public void setLayout(final HashMap config) throws Throwable {

		Object w;

		if (config == null) {
			throw new Throwable("-2.1");
		}

		w = config.get("placement");
		if (w == null) {
			this.setLayout(new GridLayout(1, 0));
			return;
		}
		this.setLayout((LayoutManager) w);
	}

	// --- Methode installerSP

	public void setLayout(final String nomSP, final HashMap config)
					throws Throwable {

		// Controler la validite des parametres
		//
		if (nomSP == null) {
			throw new Throwable("-2.1");
		}
		if (config == null) {
			throw new Throwable("-2.2");
		}

		// Obtenir la reference Java du sous panneau cible
		//
		final VuePanneauG cible = this.obtenirSP(nomSP);
		if (cible == null) {
			throw new Throwable("-3.4");
		}

		// Appliquer la modification sur la cible
		//
		cible.setLayout(config);
	}

	public void setLayout(final String nomSP, final LayoutManager placement)
					throws Throwable {

		// Controler la validite des parametres
		//
		if (nomSP == null) {
			throw new Throwable("-2.1");
		}
		if (placement == null) {
			throw new Throwable("-2.2");
		}

		// Obtenir la reference Java du sous panneau cible
		//
		final VuePanneauG cible = this.obtenirSP(nomSP);
		if (cible == null) {
			throw new Throwable("-3.4");
		}

		// Appliquer la modification sur la cible
		//
		cible.setLayout(placement);
	}

	// --- Methode deployerSP

	private void setSP(final HashMap config) throws Throwable {

		Object w;

		// Controler la presence eventuelle de la cle "sousPanneaux"
		//
		if (config == null) {
			throw new Throwable("-2.1");
		}
		w = config.get("sousPanneaux");
		if (w == null) {
			return;
		}

		// Controler la nature de l'associe
		//
		final String typeD = w.getClass().getName();
		final String refD = "java.util.LinkedHashMap";

		if (!typeD.equals(refD)) {
			throw new Throwable("-3.5");
		}

		// Convertir l'associe pour le parcourir
		//
		final LinkedHashMap description = (LinkedHashMap) w;

		// Parcourir la description des sous panneaux
		//
		final Iterator i = description.keySet().iterator();
		String cle = null;
		String version = null;
		String chemin = null;
		HashMap configCourante = null;

		while (i.hasNext()) {

			// Acquerir la cle courante
			//
			cle = (String) i.next();

			// Acquerir l'associe courant
			//
			version = (String) description.get(cle);

			// Designer le fichier de configuration du sous panneau courant
			//
			chemin = "Config/ConfigSP" + cle;

			// Charger la configuration du sous panneau courant
			//
			configCourante = (HashMap) Config.load(chemin, version);

			// Ajouter le sous panneau courant au panneau cible
			//
			this.ajouterPanneau(cle, configCourante);
		}
	}

	public void setTitre(final HashMap config) throws Throwable {

		Object w;

		if (config == null) {
			throw new Throwable("-2.1");
		}

		w = config.get("titre");
		if (w == null) {
			return;
		}
		this.ajouterTitre((String) w);
	}

	// --- Methode reconfigurer

	public void setTitre(final String nomSP, final HashMap config)
					throws Throwable {

		// Controler la validite des parametres
		//
		if (nomSP == null) {
			throw new Throwable("-2.1");
		}
		if (config == null) {
			throw new Throwable("-2.2");
		}

		// Obtenir la reference Java du sous panneau cible
		//
		final VuePanneauG cible = this.obtenirSP(nomSP);
		if (cible == null) {
			throw new Throwable("-3.4");
		}

		// Appliquer la modification sur la cible
		//
		cible.setTitre(config);
	}

	public void setTitre(final String nomSP, final String titre)
					throws Throwable {

		// Controler la validite des parametres
		//
		if (nomSP == null) {
			throw new Throwable("-2.1");
		}
		if (titre == null) {
			throw new Throwable("-2.2");
		}

		// Obtenir la reference Java du sous panneau cible
		//
		final VuePanneauG cible = this.obtenirSP(nomSP);
		if (cible == null) {
			throw new Throwable("-3.4");
		}
		//
		cible.ajouterTitre(titre);
	}
}
