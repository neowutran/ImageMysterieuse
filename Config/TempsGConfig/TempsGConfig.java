package config;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.IOException;

import library.ObjectString;

import org.jdom2.Element;

/**
 * @author sephiroth
 */
public class TempsGConfig {

	private Element	racine;

	/**
	 * 
	 */
	public void config() {

		this.racine = new Element("tempsG");

		final Element nomInstance = new Element("nomInstance");
		final Element mode = new Element("mode");
		final Element butee = new Element("butee");
		final Element tempsTotal = new Element("tempsTotal");
		final Element classeVue = new Element("classeVue");
		final Element arrierePlan = new Element("arrierePlan");
		final Element avantPlan = new Element("avantPlan");
		final Element police = new Element("police");
		final Element separateur = new Element("separateur");
		final Element labelInitial = new Element("labelInitial");
		final Element placement = new Element("placement");
		final Element limite = new Element("limite");

		nomInstance.addContent("H1");
		mode.addContent("sablier");
		butee.addContent("0 : 00 : 00");
		tempsTotal.addContent("45");
		classeVue.addContent("views.VueTempsG");
		arrierePlan.addContent(String.valueOf(Color.pink));
		avantPlan.addContent(String.valueOf(Color.black));
		separateur.addContent(" : ");
		labelInitial.addContent("XX : XX : XX");
		limite.addContent("10");

		try {
			placement.addContent(ObjectString.toString(new GridLayout(1, 0)));
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			police.addContent(ObjectString.toString(new Font("DS-digital",
							Font.TYPE1_FONT, 60)));
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.racine.addContent(nomInstance);
		this.racine.addContent(mode);
		this.racine.addContent(butee);
		this.racine.addContent(tempsTotal);
		this.racine.addContent(classeVue);
		this.racine.addContent(arrierePlan);
		this.racine.addContent(avantPlan);
		this.racine.addContent(separateur);
		this.racine.addContent(labelInitial);
		this.racine.addContent(placement);
		this.racine.addContent(police);
		this.racine.addContent(limite);

	}

	/**
	 * @return Element
	 */
	public Element toXML() {

		this.config();
		return this.racine;

	}

}
