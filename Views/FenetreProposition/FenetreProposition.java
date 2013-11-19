package views;

import java.awt.event.ActionListener;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jdom2.Element;

public class FenetreProposition extends JDialog {

	private static final long	serialVersionUID	= 1L;
	private Master				hamecon;
	private Element				configuration;
	private JPanel				contentPanel;
	private JButton				proposer;
	private JTextField			text;

	public FenetreProposition(final Master hamecon, final Element config) {

		this.hamecon = hamecon;
		if (config == null) {
			return;
		}
		this.configuration = config.getChild("fenetreProp");

		// Assigner une taille a la fenetre
		//
		final Integer width = Integer.valueOf(this.configuration.getChild(
						"size").getChildText("width"));
		final Integer height = Integer.valueOf(this.configuration.getChild(
						"size").getChildText("height"));
		this.setSize(width, height);
		this.contentPanel = new JPanel();

		// Creer les panneaux
		//
		final JPanel panneauProposition = new JPanel();
		final JPanel panneauTour = new JPanel();

		// Mettre en place le label du pseudo
		//
		final JLabel proposition = new JLabel(
						this.configuration.getChildText("label"));
		final JLabel tour = new JLabel(
						this.configuration.getChildText("labelTour"));

		// Mettre en place la text area
		//
		final Integer colonne = Integer.valueOf(this.configuration.getChild(
						"text").getChildText("colonne"));
		this.text = new JTextField(colonne);

		// Ajouter au panneau central
		//
		panneauTour.add(tour);
		panneauProposition.add(proposition);
		panneauProposition.add(this.text);

		// Creer les boutons
		//
		this.proposer = new JButton(this.configuration.getChildText("proposer"));

		// Ajouter les boutons
		//
		panneauProposition.add(this.proposer);

		// Ajouter les différents panneaux
		//
		this.contentPanel.add(panneauTour);
		this.contentPanel.add(panneauProposition);

		this.setContentPane(this.contentPanel);
		this.setLocationRelativeTo(this.hamecon.getFenetre());

		this.setVisible(true);
	}

	// ---                                                           Methode ajouterControleur
	//
	public void ajouterControleur(final ActionListener espion,
					final KeyListener key) {

		this.proposer.addActionListener(espion);
		this.text.addKeyListener(key);
	}

	public String getText() {

		return this.text.getText();
	}
}
