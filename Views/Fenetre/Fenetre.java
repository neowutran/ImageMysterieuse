package views;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import org.jdom2.Element;

/**
 * @author sephiroth
 */
public class Fenetre extends JFrame implements ActionListener, Observer,
				KeyListener {

	private JPanel		contentPanel;
	private Element		config;
	private Master		hamecon;

	private static final long	serialVersionUID	= 1L;

	// ---                                                           Constructeur normal
	//
	public Fenetre(Element configuration, Master hamecon) {
		
		if (configuration == null) {
			return;
		}
		if (hamecon == null) {
			return;
		}
		
		// Recuperer les parametres
		//
		this.config = configuration;
		this.hamecon = hamecon;
		
		// Initialiser la fenetre
		//
		this.setSize(this.getToolkit().getScreenSize());
		JFrame.setDefaultLookAndFeelDecorated(true);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
		// Ajouter un panneau support
		//
		this.contentPanel = new JPanel();
		this.contentPanel.setLayout(new BorderLayout());
		this.add(this.contentPanel);

	}
	
	// --- Action
	//
	public void actionPerformed(final ActionEvent e) {}
	
	// --- Action quitter 
	//
	private void actionQuitter() {
		System.exit(0);
	}
	
	// --- Accesseur panneau support
	//
	public JPanel getPanel() {
		return this.contentPanel;
	}

	public void keyPressed(final KeyEvent e) {}

	public void keyReleased(final KeyEvent e) {}

	public void keyTyped(final KeyEvent e) {}

	protected void processWindowEvent(final WindowEvent e) {

		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			this.actionQuitter();
		}
	}

	public void update(final Observable o, final Object arg) {}

}
