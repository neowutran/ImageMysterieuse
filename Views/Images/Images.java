package views;

import java.awt.Color;
import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;

import library.ImageKiller;
import library.ObjectString;
import library.PlaySound;

import org.jdom2.Element;

import controllers.ControleurMosaiqueG;
import demonstrateurs.ImageMysterieuse;

public class Images extends JPanel {

	private List<ControleurMosaiqueG>	mosaiqueController	= new LinkedList<ControleurMosaiqueG>();
	private List<VuePanneauG>			mosaiquePanneaux	= new LinkedList<VuePanneauG>();

	private List<LinkedList<MouseList>>	mouse				= new LinkedList<LinkedList<MouseList>>();
	private final Element				configuration;
	private final Master				hamecon;
	private VuePanneauG					victory;

	private static final long			serialVersionUID	= 1L;

	/**
	 * @param config
	 * @param hamecon
	 */
	public Images(final Element config, final Master hamecon) {

		super();

		this.configuration = config.getChild("images");
		this.hamecon = hamecon;
		this.setVisible(true);

	}
	
	public int getNbPanneau(){
		return this.mosaiquePanneaux.size();
	}

	/**
	 * @param identifiant
	 */
	public void afficherMosaique(final int identifiant) {

		int i;
		int ligne;
		int colonne;
		for (i = 0; i < this.mosaiqueController.get(identifiant)
						.obtenirNbCellules(); i++) {

			colonne = (i % this.mosaiqueController.get(identifiant)
							.obtenirNbColonnes()) + 1;

			ligne = (i / this.mosaiqueController.get(identifiant)
							.obtenirNbColonnes()) + 1;

			try {

				this.mosaiqueController.get(identifiant).ajouterImage(
								ligne,
								colonne,
								ImageMysterieuse.FOLDER
												+ ImageMysterieuse.FOLDER_GAME
												+ identifiant + File.separator
												+ (i + 1) +"."+ configuration.getChildText("type"));
			} catch (final Throwable e) {
				e.printStackTrace();
			}
		}
	}

	
	/**
	 * @param pathImage
	 * @param type
	 * @param nbColonnes
	 * @param nbLignes
	 * @throws IOException
	 */
	public void ajouterImage(final String pathImage, final String type,
					final int nbColonnes, final int nbLignes)
					throws IOException {

		final ImageKiller decoupe = new ImageKiller(pathImage,
						ImageMysterieuse.FOLDER + ImageMysterieuse.FOLDER_GAME
										+ this.mosaiqueController.size()
										+ File.separator, type);

		(new File(ImageMysterieuse.FOLDER + ImageMysterieuse.FOLDER_GAME
						+ this.mosaiqueController.size() + File.separator))
						.mkdirs();

		decoupe.configuration(nbColonnes, nbLignes);
		decoupe.decoupe();
		decoupe.save();

		VuePanneauG nouveauPanneau;
		nouveauPanneau = null;
		try {
			nouveauPanneau = new VuePanneauG(this);
			nouveauPanneau.setVisible(true);
		} catch (final Throwable e) {
			e.printStackTrace();
		}

		this.mosaiquePanneaux.add(nouveauPanneau);
		ControleurMosaiqueG c1 = null;
		final HashMap<String, Serializable> config = new HashMap<String, Serializable>();
		config.put("nbLignes", nbLignes);
		config.put("nbColonnes", nbColonnes);
		try {
			config.put("arrierePlan", (Serializable) ObjectString.fromString(configuration.getChildText("couleurFond")));
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}

		try {
			c1 = new ControleurMosaiqueG(nouveauPanneau, config);
		} catch (final Throwable e2) {
			e2.printStackTrace();
		}

		if (c1 != null) {

			int i, j;
			final LinkedList<MouseList> ecouteurCellule = new LinkedList<MouseList>();
			MouseList m;
			for (i = 1; i <= nbColonnes; i++) {

				for (j = 1; j <= nbLignes; j++) {

					m = new MouseList(this, this.mosaiqueController.size(), j,
									i);
					ecouteurCellule.add(m);
					try {
						c1.observerCellule(j, i, ecouteurCellule.getLast());
					} catch (final Throwable e) {
						e.printStackTrace();
					}
				}
			}

			this.mouse.add(ecouteurCellule);
			this.mosaiqueController.add(c1);
			int ligne, colonne;
			ligne = Math.round(this.mosaiquePanneaux.size() / 2);
			colonne = Math.round(this.mosaiquePanneaux.size() / 2);

			if ((ligne + colonne) < this.mosaiquePanneaux.size()) {
				ligne++;
			}
			this.setLayout(new GridLayout(ligne, colonne));
		}
	}

	/**
	 * @param identifiant
	 * @param colonne
	 * @param ligne
	 */
	public void devoiler(final int identifiant, final int colonne,
					final int ligne) {

	
			try {
				this.mosaiqueController
								.get(identifiant)
								.ajouterImage(ligne,
												colonne,
												ImageMysterieuse.FOLDER
																+ ImageMysterieuse.FOLDER_GAME
																+ identifiant
																+ File.separator
																+ (colonne + ((ligne - 1) * this.mosaiqueController
																				.get(identifiant)
																				.obtenirNbColonnes()))
																
																				 +"."+ configuration.getChildText("type"));
			} catch (final Throwable e) {
				e.printStackTrace();
			}
		
	}

	public Master getHamecon() {

		return this.hamecon;

	}

	// Accesseur mosaique
	//
	public List<ControleurMosaiqueG> getMosaique() {

		return this.mosaiqueController;

	}

	// Methode injecteur
	// Methode utilisée pour générer le contexte d'une partie -> ne sert que pour la démo
	//
	public void injecteur() {
		
		// Ajouter la premiere image
		//
		try {
			this.ajouterImage(
							ImageMysterieuse.FOLDER + ImageMysterieuse.FOLDER_IMG + "a.jpg",
							"jpg", 10, 10);
		} catch (final IOException e) {
			e.printStackTrace();
		}
		
		// Ajouter la seconde image
		//
		try {
			this.ajouterImage(
							ImageMysterieuse.FOLDER + ImageMysterieuse.FOLDER_IMG + "aa.jpg",
							"jpg", 10, 10);
		} catch (final IOException e) {
			e.printStackTrace();
		}
		
		// Ajouter la troisieme image
				//
		try {
			this.ajouterImage(
							ImageMysterieuse.FOLDER + ImageMysterieuse.FOLDER_IMG + "aaa.jpg",
							"jpg", 10, 10);
		} catch (final IOException e) {
			e.printStackTrace();
		}
		
		// Ajouter la quatrieme image
		//
		try {
			this.ajouterImage(
							ImageMysterieuse.FOLDER + ImageMysterieuse.FOLDER_IMG + "aaaa.jpg",
							"jpg", 10, 10);
		} catch (final IOException e) {
			e.printStackTrace();
		}
		
		// Dévoiler une premiere case
		//
		this.devoiler(0, 2, 2);
		
		// Permettre au joueur de cliquer
		//
		this.hamecon.getHamecon().setCanClickServeur(true);

	} 
	
	// Demarrage d'une nouvelle partie (Reset)
	//
	public void newGame() {

		this.setVisible(false);
		this.removeAll();
		this.mosaiqueController = new LinkedList<ControleurMosaiqueG>();
		this.mosaiquePanneaux = new LinkedList<VuePanneauG>();
		this.mouse = new LinkedList<LinkedList<MouseList>>();
		this.setVisible(true);

	}
	
	// Methode showVictory
	//
	public void showVictory() {

		this.setVisible(false);
		this.removeAll();
		
		// Son de victoire
		//
		final PlaySound sound = new PlaySound(ImageMysterieuse.FOLDER
						+ ImageMysterieuse.FOLDER_SOUND
						+ ImageMysterieuse.SOUND_VICTORY);
		final Thread t1 = new Thread(sound);
		t1.start();
		
		// Afficher l'image decouverte
		//
		try {
			this.victory = new VuePanneauG(this);
			this.victory.modifierImage(ImageMysterieuse.FOLDER
							+ ImageMysterieuse.FOLDER_IMG
							+ ImageMysterieuse.VICTORY);
			this.setLayout(new GridLayout(1, 1));
			this.add(this.victory);

		} catch (final Throwable e) {
			e.printStackTrace();
		}
		
		// Afficher
		//
		this.setVisible(true);
	}
}
