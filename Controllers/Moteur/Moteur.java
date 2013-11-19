package controllers;

import java.util.LinkedList;
import java.util.List;

public class Moteur {
	
	private ImageMysterieuseController controlleur;
	private LinkedList<String>	reponse;
	
	public Moteur(ImageMysterieuseController im){
		
		this.controlleur = im;		
		reponse = new LinkedList<String>();
		reponse.add("Menton");
		reponse.add("sephiroth");
		
		this.controlleur.nouvellePartie(); 
		
		this.nouveauTour();
	}
	
	public void proposer(final String proposition) {
		int i;
		for (i = 0; i < this.reponse.size(); i++) {
			if (reponse.get(i).equals(proposition)) {
				this.controlleur.victory(i);
			}
		}
	}
	
	public void nouveauTour(){
		try {
			this.controlleur.votreTour(15);
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
}
