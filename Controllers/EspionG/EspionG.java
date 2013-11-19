package controllers;

import java.awt.AWTEvent;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseEvent;
import java.util.Date;
import java.util.EventObject;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

@SuppressWarnings("rawtypes")
public class EspionG implements Observer, AWTEventListener {

	/**
	 * Liste des �v�nements enregistr�s
	 */
	private LinkedList		events				= new LinkedList();

	/**
	 * "flag" vrai si l'espion doit espionner
	 */
	private boolean			statusEventsTracker	= true;

	private final EspionG	espionPrincipal;

	/**
	 * Ecart temporel entre le timestamp de l'espion principale et celui de
	 * l'espion secondaire
	 */
	private long			gap					= 0;

	/**
	 * Timestamp correspondant � la date de d�but d'espionnage
	 */
	private long			debutTracking;
	/**
	 * Timestamp correspondant � la date de fin d'espionnage
	 */
	private long			finTracking;

	private LinkedList		idsToSpyOn			= new LinkedList();

	private boolean			demmarageTemp;

	/**
	 * Constructeur � utiliser si on veut construire un espion principal
	 */
	public EspionG() {

		this.espionPrincipal = this;
	}

	/**
	 * Constructeur � utiliser si on veut construire un espion secondaire
	 * 
	 * @param espionPrincipal
	 *            Espion principal associ�
	 */
	public EspionG(final EspionG espionPrincipal) {

		this.espionPrincipal = espionPrincipal;
	}

	/**
	 * Ajouter un type d'�v�nement � �couter
	 * 
	 * @param ID
	 *            ID du type d'�v�nement � rajouter
	 */
	public void addIdToSpyOn(final long ID) {

		// Construire l'ID a ajouter a la liste des ID a observer
		//
		final Long newID = new Long(ID);

		// Controler l'absence de cet ID dans la liste courante
		//
		if (this.idsToSpyOn.contains(newID)) {
			return;
		}

		// Ajouter le nouvel ID a la liste courante
		//
		this.idsToSpyOn.add(newID);

	}

	/**
	 * Construit un dictionnaire contenant l'�v�nement, le timestamp de
	 * r�f�rence de l'�v�nement et un instantan� des
	 * Threads tournant dans la m�me JVM que l'espion
	 * 
	 * @param e
	 *            Ev�nement
	 * @return
	 */
	private HashMap constructEvent(final EventObject e) {

		final HashMap event = new HashMap();

		// On construit l'enregistrement d'un �v�nement avec
		final Thread[] threads = {};
		this.getThreads(threads);
		// Son �v�n�nement(logique), sa date selon l'espion principal et un
		// instatan� des Threads tournant sur la JVM

		event.put("event", e);
		event.put("timestamp", new Date().getTime() - this.gap);
		event.put("threads", threads);

		return event;
	}

	public void eventDispatched(final AWTEvent e) {

		this.updateStatusEventsTracker();
		System.out.println("ID de l'�v�nement: " + e.getID());
		if (this.statusEventsTracker
						&& this.idsToSpyOn.contains(new Long(e.getID()))) {
			this.events.add(this.constructEvent(e));
		}

	}

	// --- Methode stopEventsTracker

	/**
	 * R�cup�ration de la liste des �v�nements collect�s
	 * 
	 * @return
	 */
	public LinkedList getEvents() {

		return this.events;
	}

	/**
		 * 
		 */
	public LinkedList getIdsToSpyOn() {

		return (LinkedList) this.idsToSpyOn.clone();
	}

	/**
	 * R�cup�ration du d�rnier �v�nement collect�
	 * 
	 * @return
	 */
	public Object getLastEvent() {

		return this.events.getLast();
	}

	/**
	 * R�cup�ration de l'�tat actuel de l'espion (actif/inactif)
	 * 
	 * @return
	 */
	public boolean getStatusEventsTracker() {

		return this.statusEventsTracker;
	}

	/**
	 * Prend un clich� des Threads
	 * 
	 * @param tab
	 * @return
	 */
	public int getThreads(final Thread[] tab) {

		return Thread.enumerate(tab);
	}

	/**
	 * R�cup�ration de la date locale
	 * 
	 * @return
	 */
	public Date getTime() {

		return new Date();
	}

	public void mouseClicked(final MouseEvent e) {

		this.eventDispatched(e);
	}

	public void mouseDragged(final MouseEvent e) {

	}

	public void mouseEntered(final MouseEvent e) {

		this.eventDispatched(e);
	}

	public void mouseExited(final MouseEvent e) {

		this.eventDispatched(e);
	}

	public void mouseMoved(final MouseEvent e) {

	}

	public void mousePressed(final MouseEvent e) {

		this.eventDispatched(e);
	}

	public void mouseReleased(final MouseEvent e) {

		this.eventDispatched(e);
	}

	// � enlever si on sort du cadre de ce TP
	// --- Methodes interface MouseListener

	/**
	 * Elimination d'un type d'�v�nement � �couter
	 * 
	 * @param ID
	 *            ID du type d'�v�nement � �liminer
	 */
	public void removeIdToSpyOn(final long ID) {

		// Si l'ID n'est dans la liste, on l'y enl�ve
		if (!this.idsToSpyOn.contains(ID)) {
			this.idsToSpyOn.remove(ID);
		}
	}

	/**
	 * R�initialise la liste des �v�nements collect�s
	 */
	public void resetEvents() {

		this.events = new LinkedList();
	}

	/**
	 * R�initialisation de la liste des types d'�v�nements � �couter
	 */
	public void resetIdsToSpyOn() {

		this.idsToSpyOn = new LinkedList();
	}

	/**
	 * Sert � d�terminer une intervalle(temporelle) durant laquelle l'espion
	 * sera actif
	 * 
	 * @param debutTracking
	 *            Date d'activation de l'�couteur
	 * @param finTracking
	 *            Date d'arr�t de l'�couteur
	 */
	public void setPlage(final Date debutTracking, final Date finTracking) {

		this.demmarageTemp = true;
		this.debutTracking = debutTracking.getTime();
		this.finTracking = finTracking.getTime();
	}

	/**
	 * D�marrage de l'espion.
	 */
	public void startEventsTracker() {

		this.statusEventsTracker = true;
	}

	// --- Methodes interface MouseMotionListener

	/**
	 * Arr�t de l'espion
	 */
	public void stopEventsTracker() {

		this.statusEventsTracker = false;
	}

	public void update(final Observable o, final Object changements) {

		if (this.statusEventsTracker) {
			this.events.add(this.constructEvent(new EventObject(changements)));
		}

	}

	/**
	 * Mise � jour de l'attribut statusEventsTracker pour d�terminer l'�tat de
	 * l'espion (actif ou inactif).
	 */
	public void updateStatusEventsTracker() {

		if (this.demmarageTemp) {
			long timestamp = new Date().getTime();
			this.updateTime(this.espionPrincipal.getTime());
			timestamp -= this.gap;

			if ((timestamp >= this.debutTracking)
							&& (timestamp <= this.finTracking)) {
				this.startEventsTracker();
			} else {
				this.demmarageTemp = false;
				this.stopEventsTracker();
			}

		}

	}

	/**
	 * M�thode de synchronisation de l'espion
	 */
	public void updateTime(final Date date) {

		// Mise � jour de l'�cart
		this.gap = date.getTime() - (new Date().getTime());
	}
}
