package views;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;

import org.jdom2.Element;

public class Menus extends VueBarreMenusG implements Observer {

	private static final long	serialVersionUID	= 1L;
	
	// ---                                                           Constructeur normal
	//
	public Menus(final JFrame hamecon, final Element config) {

		super(hamecon, config);
	}
	
	// Methode update
	//
	public void update(final Observable arg0, final Object arg1) {}
}
