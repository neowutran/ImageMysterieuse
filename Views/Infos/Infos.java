package views;

import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.JToolBar;

import library.ObjectString;

import org.jdom2.Element;

/**
 * @author sephiroth
 */
public class Infos extends JToolBar {

	private final Element		configuration;
	private final Master		hamecon;
	private static final long	serialVersionUID	= 1L;
	private final String		pseudoText;
	private String				themeText;
	private String				scoreText;
	private final JLabel		score;
	private final JLabel		pseudo;
	private final JLabel		theme;

	// ---                                                           Constructeur normal
	//
	public Infos(final Element config, final Master hamecon) {

		super();
		
		// Recuperer la configuration XML
		//
		this.configuration = config.getChild("infos");
		this.hamecon = hamecon;

		// Recuperer le layout
		//
		try {
			this.setLayout((LayoutManager)ObjectString.fromString(configuration.getChildText("placement")));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		// Initialiser les valeurs des labels
		//
		this.pseudoText = configuration.getChildText("pseudo");
		this.themeText = configuration.getChildText("theme");
		this.scoreText = configuration.getChildText("score");
		
		this.pseudo = new JLabel(this.pseudoText + configuration.getChildText("separateur"));
		this.theme = new JLabel(this.themeText + configuration.getChildText("separateur"));
		this.score = new JLabel(this.scoreText + configuration.getChildText("separateur"));

		this.add(this.pseudo);
		this.add(this.score);
		this.add(this.theme);
		
		// Afficher
		//
		this.setVisible(true);

	}

	// ---                                                           Accesseurs de consultation
	//
	public Master getHamecon() {

		return this.hamecon;

	}

	public JLabel getPseudo() {

		return this.pseudo;
	}

	public String getPseudoText() {

		return this.pseudoText;
	}

	public JLabel getScore() {

		return this.score;

	}

	public String getScoreText() {

		return this.scoreText;

	}

	public JLabel getTheme() {

		return this.theme;
	}

	public String getThemeText() {

		return this.themeText;
	}

	// ---                                                           Accesseurs de modifications
	//
	public void setPseudo(final String pseudo) {

		this.pseudo.setText(this.pseudoText + pseudo);
	}

	public void setPseudoText(final String pseudoText) {

		this.themeText = pseudoText;

	}

	public void setScore(final String score) {

		this.score.setText(this.scoreText + score);

	}

	public void setScoreText(final String scoreText) {

		this.scoreText = scoreText;

	}

	public void setTheme(final String theme) {

		this.hamecon.getFenetre().setTitle(theme);
		this.theme.setText(this.themeText + theme);
	}

	public void setThemeText(final String themeText) {

		this.themeText = themeText;

	}
}
