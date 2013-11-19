/*
 * 
 * 
 * 
 * 
 * 
 */

package views;

import java.awt.FlowLayout;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

import library.PlaySound;

import org.jdom2.Element;

import demonstrateurs.ImageMysterieuse;

public class VueTempsG extends JComponent implements Observer {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	private JProgressBar		progress;
	private JLabel				label;
	private Icon				image1;
	private Icon				image2;
	private Icon				image3;
	private JComponent			hamecon;
	private Element				config;
	private Integer				max;

	// --- Constructeur normal

	/**
	 * @param hamecon
	 * @param config
	 */
	public VueTempsG(final Object hamecon, final Element config) {

		super();
		this.hamecon = (JComponent) hamecon;
		this.config = config;
		this.setLayout(new FlowLayout());
		this.image1 = new ImageIcon(ImageMysterieuse.FOLDER
						+ ImageMysterieuse.FOLDER_IMG
						+ ImageMysterieuse.GREEN_CIRCLE);
		this.image2 = new ImageIcon(ImageMysterieuse.FOLDER
						+ ImageMysterieuse.FOLDER_IMG
						+ ImageMysterieuse.ORANGE_CIRCLE);
		this.image3 = new ImageIcon(ImageMysterieuse.FOLDER
						+ ImageMysterieuse.FOLDER_IMG
						+ ImageMysterieuse.RED_CIRCLE);

		this.label = new JLabel();
		this.label.setIcon(this.image1);
		this.add(this.label);
		this.hamecon.add(this);

	}

	public void destroy() {

		this.progress = null;
		this.label = null;
		this.image1 = null;
		this.image2 = null;
		this.image3 = null;
		this.hamecon = null;
		this.config = null;
		this.max = null;

		this.setVisible(false);

	}

	/**
	 * @param max
	 */
	public void setMax(final int max) {

		this.max = max;
		this.progress = new JProgressBar(0, max);
		this.progress.setStringPainted(true);
		this.add(this.progress);
	}

	// --- Methode update

	public void update(final Observable o, final Object modifs) {
		
		final Integer time = (Integer) ((HashMap) modifs).get("time");
		this.progress.setString(time + " s");
		this.progress.setValue(time);
		System.out.println(time);
		if (time < Integer.valueOf(config.getChild("tempsG").getChildText("limite"))) {

			this.label.setIcon(this.image2);
			PlaySound im = new PlaySound(ImageMysterieuse.FOLDER
					+ ImageMysterieuse.FOLDER_SOUND
					+ ImageMysterieuse.SOUND_BIP);
			Thread t1 = new Thread(im);
			t1.start();
			
		}
		
		if (time == 0) {

			this.label.setIcon(this.image3);
			((Infos) this.hamecon).getHamecon().notifyObservers("timeout");
			
			PlaySound im = new PlaySound(ImageMysterieuse.FOLDER
					+ ImageMysterieuse.FOLDER_SOUND
					+ ImageMysterieuse.SOUND_GONG);
			Thread t1 = new Thread(im);
			t1.start();
			
		}
		
	}

	// --- Methode setColors

}
