package fenetre;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 * Tag browser de la GED
 * @author Xavier
 *
 */
public class TagBrowser extends JPanel{
	/**
	 * Zone de recherche
	 */
	private JTextArea critere;
	/**
	 * Bouton lan�ant la racherche
	 */
	private JButton rechercher;
	/**
	 * contraintes utilis�es pour le placement des objets SWING
	 */
	private GridBagConstraints cons;
	
	/**
	 * Constructeur du browser
	 */
	public TagBrowser(){
		//initialisation du Layout et des contraintes
		setLayout(new GridBagLayout());
		cons = new GridBagConstraints();
		cons.insets=new Insets(2,2,2,2);//D�finit une marge entre chaque elt � 2px
		
		//Initialisation et placement de la zone de recherche
		critere = new JTextArea("", 1, 10);
		setConstraints(0, 0, 2, 1);
		add(critere, cons);
		
		//Initialisation et placement du bouton rechercher
		rechercher = new JButton(new ImageIcon("images\\loupe.png"));
		setConstraints(2, 0, 1, 1);
		add(rechercher, cons);
	}
	
	/**
	 * Initialise les contraintes de placement pour les �l�ments SWING
	 * @param x abcisse de l'�l�ment
	 * @param y ordonn�e de l'�l�ment
	 * @param w largeur de l'�l�ment
	 * @param h hauteur de l'�l�ment
	 */
	public void setConstraints(int x, int y, int w, int h) {
		cons.gridx = x;
		cons.gridy = y;
		cons.gridwidth = w;
		cons.gridheight = h;
	}
}
