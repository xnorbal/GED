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
	 * Bouton lançant la racherche
	 */
	private JButton rechercher;
	/**
	 * contraintes utilisées pour le placement des objets SWING
	 */
	private GridBagConstraints cons;
	
	/**
	 * Constructeur du browser
	 */
	public TagBrowser(){
		//initialisation du Layout et des contraintes
		setLayout(new GridBagLayout());
		cons = new GridBagConstraints();
		cons.insets=new Insets(2,2,2,2);//Définit une marge entre chaque elt à 2px
		
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
	 * Initialise les contraintes de placement pour les éléments SWING
	 * @param x abcisse de l'élément
	 * @param y ordonnée de l'élément
	 * @param w largeur de l'élément
	 * @param h hauteur de l'élément
	 */
	public void setConstraints(int x, int y, int w, int h) {
		cons.gridx = x;
		cons.gridy = y;
		cons.gridwidth = w;
		cons.gridheight = h;
	}
}
