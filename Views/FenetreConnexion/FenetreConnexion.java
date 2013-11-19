package views;

import java.awt.LayoutManager;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import library.ObjectString;

import org.jdom2.Element;

public class FenetreConnexion extends JDialog {

	private static final long	serialVersionUID	= 1L;
	private Master				hamecon;
	private Element				configuration;
	private JPanel				contentPanel;
	private JButton				connexion;
	private JButton				annuler;
	private JTextField			text;
	private JTextField			server;
	private JTextField			port;

	// ---                                                           Premier constructeur normal
	//
	public FenetreConnexion(final Master hamecon, final Element config)
					throws IOException, ClassNotFoundException {

		this.hamecon = hamecon;
		if (config == null) {
			return;
		}
		
		// Recuperer la configuration XML
		//
		this.configuration = config.getChild("fenetreCo");

		// Assigner une taille a la fenetre
		//
		final Integer width = Integer.valueOf(this.configuration.getChild(
						"size").getChildText("width"));
		final Integer height = Integer.valueOf(this.configuration.getChild(
						"size").getChildText("height"));
		this.setSize(width, height);
		this.contentPanel = new JPanel();

		this.contentPanel.setLayout((LayoutManager) ObjectString
						.fromString(this.configuration
										.getChildText("placement")));

		// Creer les panneaux
		//
		final JPanel panneauVide = new JPanel();
		final JPanel panneauPseudo = new JPanel();
		final JPanel panneauServer = new JPanel();
		final JPanel panneauPort = new JPanel();
		final JPanel panneauBouton = new JPanel();
		final JPanel panneauVide2 = new JPanel();

		// Mettre en place le label du pseudo
		//
		final JLabel pseudoLabel = new JLabel(
						this.configuration.getChildText("labelPseudo"));
		final JLabel serveurLabel = new JLabel(
						this.configuration.getChildText("labelServeur"));
		final JLabel portLabel = new JLabel(
						this.configuration.getChildText("labelPort"));

		// Mettre en place la text area
		//
		final Integer ligne = Integer.valueOf(this.configuration.getChild(
						"text").getChildText("ligne"));
		final Integer colonne = Integer.valueOf(this.configuration.getChild(
						"text").getChildText("colonne"));
		this.text = new JTextField(colonne);
		this.server = new JTextField(colonne);
		this.port = new JTextField(colonne);

		// Ajouter au panneau central
		//
		panneauPseudo.add(pseudoLabel);
		panneauPseudo.add(this.text);
		panneauServer.add(serveurLabel);
		panneauServer.add(this.server);
		panneauPort.add(portLabel);
		panneauPort.add(this.port);

		// Creer les boutons
		//
		this.connexion = new JButton(
						this.configuration.getChildText("connexion"));
		this.annuler = new JButton(this.configuration.getChildText("annuler"));

		// Ajouter les boutons
		//
		panneauBouton.add(this.connexion);
		panneauBouton.add(this.annuler);

		// Ajouter les diffïérents panneau
		//
		this.contentPanel.add(panneauVide);
		this.contentPanel.add(panneauPseudo);
		this.contentPanel.add(panneauServer);
		this.contentPanel.add(panneauPort);
		this.contentPanel.add(panneauBouton);

		this.setContentPane(this.contentPanel);
		this.setLocationRelativeTo(this.hamecon.getFenetre());

		this.setVisible(true);
	}

	// ---                                                           Methode ajouterControleur
	//
	public void ajouterControleur(final ActionListener espion,
					final KeyListener key) {

		this.connexion.addActionListener(espion);
		this.annuler.addActionListener(espion);
		this.text.addKeyListener(key);
		this.server.addKeyListener(key);
		this.port.addKeyListener(key);
	}
	
	// ---                                                           Accesseurs de consultation
	
	public String getPortText() {

		return this.server.getText();
	}

	public String getPseudoText() {

		return this.text.getText();
	}

	public String getServeurText() {

		return this.server.getText();
	}
}
