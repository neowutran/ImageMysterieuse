package views;

import java.awt.BorderLayout;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import library.PlaySound;

import org.jdom2.Element;

import controllers.ImageMysterieuseController;
import demonstrateurs.ImageMysterieuse;

/**
 * @author sephiroth
 */
public class Master extends Observable implements Observer {

	private static Element						configuration;
	private final Fenetre						fenetre;
	private Infos								infos;
	private final ImageMysterieuseController	hamecon;
	private final Images						images;
	private final Menus							barreMenus;
	private FenetreConnexion					fenetreConnexion;
	private FenetreProposition					fenetreProposition;
	private Thread fond 						;

	// ---                                                           Constructeur normal
	//
	public Master(final Element config, final ImageMysterieuseController hamecon) {
		
		// Recuperer les parametres
		//
		Master.configuration = config;
		this.hamecon = hamecon;
		
		// Mettre en place la fenetre
		//
		this.fenetre = new Fenetre(config, this);
		
		// Mettre en place la toolbar
		//
		this.infos = new Infos(config, this);
		
		// Mettre en place le panneau image
		//
		this.images = new Images(config, this);
		this.images.setFocusable(true);
		
		// Mettre en place la barre de menus
		//
		this.barreMenus = new Menus(this.fenetre, Master.configuration);
		
		// Ajouter les panneaux a la fenetre
		//
		this.fenetre.getPanel().add(this.infos, BorderLayout.NORTH);
		this.fenetre.getPanel().add(this.images, BorderLayout.CENTER);
		
		// Demarrer le son de fond
		//
		PlaySound im = new PlaySound(ImageMysterieuse.FOLDER
						+ ImageMysterieuse.FOLDER_SOUND
						+ ImageMysterieuse.SOUND_FOND);
		fond = new Thread(im);
		fond.start();
		
		// Afficher
		//
		this.fenetre.setVisible(true);

	}
	
	// Accesseur getSon
	//
	public Thread getSon(){
		return fond;
	}
	
	// Methode resetInfos
	//
	public void resetInfos(){
		
		this.infos = new Infos(configuration, this);
		
	}
	
	// Accesseur getBarreMenus
	//
	public Menus getBarreMenus() {

		return this.barreMenus;
	}

	// Accesseur getFenetre
	//
	public Fenetre getFenetre() {

		return this.fenetre;

	}
	
	// Accesseur getFenetreProp
	//
	public FenetreProposition getFenetreProp() {

		if (this.fenetreProposition == null) {
			return null;
		}
		return this.fenetreProposition;
	}

	// Accesseur getHamecon
	//
	public ImageMysterieuseController getHamecon() {

		return this.hamecon;

	}
	
	// Accesseur getImages()
	//
	public Images getImages() {

		return this.images;

	}

	// Accesseur getInfos
	//
	public Infos getInfos() {

		return this.infos;

	}
	
	// Methode modifierImage
	//
	public void modifierImage(final String path, final String type, final int nbLigne, final int nbColonne) throws IOException {

		this.images.ajouterImage(path, type, nbColonne, nbLigne);
	}
	
	// Methode modifierPseudo
	//
	public void modifierPseudo(final String pseudo) {

		this.infos.setPseudo(pseudo);
	}
	
	// Methode modifierScore
	//
	public void modifierScore(final String score) {

		this.infos.setScore(score);
	}
	
	// Methode modifierTheme
	//
	public void modifierTheme(final String theme) {

		this.infos.setTheme(theme);
	}
	
	// Methode showConnectWindow
	//
	public FenetreConnexion showConnectWindow(final Element config) {

		try {
			this.fenetreConnexion = new FenetreConnexion(this, config);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return this.fenetreConnexion;
	}
	
	// Methode showCreditWindow
	//
	public void showCreditWindow(final Element config) {}
	
	// Methode showFenetreProposition()
	//
	public FenetreProposition showFenetreProposition() {

		this.fenetreProposition = new FenetreProposition(this,
						Master.configuration);
		return this.fenetreProposition;
	}
	
	// Methode update
	//
	public void update(final Observable o, final Object arg) {}

}
