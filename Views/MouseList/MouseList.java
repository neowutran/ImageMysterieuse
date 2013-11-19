package views;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

/**
 * Cette classe va gerer tout ce qui concerne la souris et les traitement a
 * faire dessus
 * 
 * @author Cibert Gauthier
 * @author Martini Didier
 * @author Responsable: Martini Didier
 * @version 3.0
 */

public class MouseList extends MouseAdapter implements MouseMotionListener,
				MouseWheelListener {

	private int					drag;
	private final int			dragFromX					= 0;
	private final int			dragFromY					= 0;
	private static final int	EPSILON						= 10;
	private static final int	DRAGGEDBUTTON1				= 1;
	private static final int	DRAGGEDBUTTON3				= 3;
	private static final String	CONFIG_STEP_SLIDER			= "valeurChangement";
	private static final int	INDEX_X						= 0;
	private static final int	INDEX_Y						= 1;
	private static final int	TRAITEMENT_SOURIS_POINTEUR	= 2;
	private static final int	TRAITEMENT_SOURIS_NORMAL	= 1;
	private final Images		parent;
	private final int			identifiant;
	private final int			colonne;
	private final int			ligne;

	// -------------------------------------------------------- *** Constructeur
	//

	public MouseList(final Images parent, final int identifiant,
					final int colonne, final int ligne) {

		super();
		this.parent = parent;
		this.identifiant = identifiant;
		this.colonne = colonne;
		this.ligne = ligne;

	}

	public void curseur(final Point e) {

	}

	public int getXGap(final Point e, final int gridStep) {

		if ((e.getX() % gridStep) > MouseList.EPSILON) {
			return (int) (((e.getX() % gridStep) - gridStep));
		}

		return (int) (e.getX() % gridStep);
	}

	public int getYGap(final Point e, final int gridStep) {

		if ((e.getY() % gridStep) > MouseList.EPSILON) {
			return (int) (((e.getY() % gridStep) - gridStep));
		}

		return (int) (e.getY() % gridStep);
	}

	// -------------------------------------------------------- *** methode
	// mouseDragged
	//
	public void mouseDragged(final MouseEvent e) {}

	// -------------------------------------------------------- *** methode
	// mouseMoved
	//
	public void mouseMoved(final MouseEvent e) {}

	// -------------------------------------------------------- *** methode
	// mousePressed
	//
	public void mousePressed(final MouseEvent e) {}

	// -------------------------------------------------------- *** methode
	// mouseReleased
	//
	public void mouseReleased(final MouseEvent e) {

		try {
			this.parent.getHamecon()
							.getHamecon()
							.demandeDevoiler(this.identifiant, this.ligne,
											this.colonne);
		} catch (Throwable e1) {
			e1.printStackTrace();
		}

	}

	public void mouseWheelMoved(final MouseWheelEvent e) {}
}
