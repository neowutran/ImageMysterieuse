package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import library.PlaySound;

import org.jdom2.Element;

import views.FenetreConnexion;
import views.FenetreProposition;
import views.Master;
import views.Menus;
import demonstrateurs.ImageMysterieuse;

/**
 * @author sephiroth
 */
public class ImageMysterieuseController extends ControleurG implements
				Observer, ActionListener, KeyListener {

	private static Element		configuration;
	private final String		pathConfig;
	private final Master		view;
	private ControleurTempsG	tempsG;
	private FenetreConnexion	fenetreConnexion;
	private FenetreProposition	fenetreProp;
	private Moteur 				moteur;

	private boolean				canClickChrono	= false;
	private boolean				canClickServeur	= false;

	// ---                                                           Constructeur normal
	//
	public ImageMysterieuseController(final Element config,
					final String pathConfig) {
		
		// Recuperer la configuration
		//
		ImageMysterieuseController.configuration = config;
		this.pathConfig = pathConfig;
		
		// Creer la vue
		//
		this.view = new Master(ImageMysterieuseController.configuration, this);
		this.tempsG();
		
		// Ajouter le controleur a la barre de menus
		//
		final Menus barreMenus = this.view.getBarreMenus();
		barreMenus.ajouterControleur(this);
		
		// Creer le moteur (demonstrateur)
		this.moteur = new Moteur(this);

	}
	
	// Methode votreTour
	//
	public void votreTour(int temps) throws Exception {

		this.canClickChrono = true;
		this.fenetreProp = this.view.showFenetreProposition();
		
		if (this.fenetreProp != null) {
			this.fenetreProp.ajouterControleur(this, this);
		}
	}
	
	// Ecouteurs
	//
	public void actionPerformed(final ActionEvent e) {

		final String action = e.getActionCommand();

		if (action.equals(configuration.getChild("controller").getChildText("connexion"))) {
			this.fenetreConnexion = this.view.showConnectWindow(ImageMysterieuseController.configuration);
			this.fenetreConnexion.ajouterControleur(this, this);
		}

		if (action.equals(configuration.getChild("controller").getChildText("quitter"))) {
			System.exit(0);
		}

		if (action.equals(configuration.getChild("controller").getChildText("a_propos"))) {
			this.view.showCreditWindow(ImageMysterieuseController.configuration);
		}

		if (action.equals(configuration.getChild("controller").getChildText("se_connecter"))) {
			final String pseudo = this.fenetreConnexion.getPseudoText();
			final String serveur = this.fenetreConnexion.getServeurText();
			final String port = this.fenetreConnexion.getPortText();
			this.view.modifierPseudo(pseudo);
			this.fenetreConnexion.dispose();
		}

		if (action.equals(configuration.getChild("controller").getChildText("annuler"))) {
			this.fenetreConnexion.dispose();
		}

		if (action.equals(configuration.getChild("controller").getChildText("proposer"))) {
			final String proposition = this.fenetreProp.getText();
			this.canClickServeur = true;
			this.moteur.proposer(proposition); 
			this.fenetreProp.dispose();
		}

	}

	// Methode declencherChrono
	//
	public void declencherChrono(final int temps) throws Exception {

		if (temps <= 0) {
			throw new Exception("temps doit etre superieur a 0");
		}
		try {
			this.tempsG.demarrer(temps);
			this.canClickChrono = true;
		} catch (final Throwable e) {
			e.printStackTrace();
		}

	}
	
	// Methode demandeDevoiler
	//
	public void demandeDevoiler(final int identifiant, final int colonne,
					final int ligne) throws Throwable {

		if (this.canClickServeur && this.canClickChrono) {

			if(this.view.getImages().getMosaique().get(identifiant).obtenirCheminImage(colonne, ligne) == null){
				this.view.getImages().devoiler(identifiant, colonne, ligne);
				this.canClickServeur = false;
				this.canClickChrono = false;
				this.tempsG.detruire();
	
				this.tempsG = null;
	
				try {
					this.tempsG = new ControleurTempsG(this.view.getInfos(),
									ImageMysterieuseController.configuration);
					this.tempsG.demarrer(12);
					this.tempsG.ajouterObservateur(this);
					this.view.getFenetre().setVisible(true);
					
				} catch (final Throwable e) {
					e.printStackTrace();
				}
				
				this.moteur.nouveauTour();
			}
		}

	}
	
	// Accesseur getCanClickChrono
	//
	public boolean getCanClickChrono() {
		return this.canClickChrono;
	}
	
	// Accesseur getCanClickServeur
	//
	public boolean getCanClickServeur() {
		return this.canClickServeur;
	}

	// KeyListener
	//
	public void keyPressed(final KeyEvent arg0) {

		if (KeyEvent.getKeyText(arg0.getKeyCode()).equals(configuration.getChild("controller").getChildText("entree"))) {
			arg0.consume();
		}

	}

	public void keyReleased(final KeyEvent e) {}

	public void keyTyped(final KeyEvent e) {}
	
	// Accesseur setCanClickServeur
	//
	public void setCanClickServeur(final boolean b) {
		this.canClickServeur = b;
	}

	// Methode tempsG
	//
	public void tempsG() {

		try {
			this.tempsG = new ControleurTempsG(this.view.getInfos(),
							ImageMysterieuseController.configuration);
			this.tempsG.demarrer(21);
			this.canClickChrono = true;
		} catch (final Throwable e) {
			e.printStackTrace();
		}
	}
	
	// Methode nouvellePartie
	//
	public void nouvellePartie(){
		
		this.view.getImages().injecteur();
		this.view.getImages().setVisible(true);
		
	}
	
	// Methode getTempsG
	//
	public ControleurTempsG getTempsG(){
		
		return this.tempsG;
		
	}

	// Methode update
	//
	public void update(final Observable o, final Object arg) {

		if (arg.getClass().getSimpleName().equals("HashMap")) {
			if ((Boolean) (((HashMap) arg).get(configuration.getChild("controller").getChildText("butee"))) == true) {
				this.canClickChrono = false;
			}	
		}
	}
	
	// Methode victory
	//
	public void victory(int image) {

		  this.view.getImages().afficherMosaique(image);

		  this.view.getSon().stop();
		  this.tempsG.detruire();

		  this.tempsG = null;
		  final PlaySound sound = new PlaySound(ImageMysterieuse.FOLDER
		      + ImageMysterieuse.FOLDER_SOUND
		      + ImageMysterieuse.SOUND_VICTORY);
		  final Thread t1 = new Thread(sound);
		  t1.start();

	}
	
	// Methode votreTour
	//
	public void votreTour() {

		this.tempsG.detruire();
		
		this.tempsG = null;
		this.view.resetInfos();

		try {
			this.tempsG = new ControleurTempsG(this.view.getInfos(),
							ImageMysterieuseController.configuration);
			this.tempsG.demarrer(15);
			this.tempsG.ajouterObservateur(this);
			this.view.getFenetre().setVisible(true);
		} catch (final Throwable e) {
			e.printStackTrace();
		}
		
		if (this.fenetreProp != null) {
			this.fenetreProp.ajouterControleur(this, this);
		}
		this.fenetreProp = this.view.showFenetreProposition();
	}
}
