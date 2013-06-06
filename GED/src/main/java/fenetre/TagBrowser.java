package fenetre;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * Tag browser de la GED
 * 
 * @author Xavier
 * 
 */
public class TagBrowser extends JPanel {
	/**
	 * Zone de recherche
	 */
	private JTextField critere;
	/**
	 * Bouton lançant la racherche
	 */
	private JButton rechercher;
	/**
	 * contraintes utilisées pour le placement des objets SWING
	 */
	private GridBagConstraints cons;

	/**
	 * 
	 */
	private JComboBox tri;

	/**
	 * 
	 */
	private JTextArea texte;
	JScrollPane textContainer;

	/**
	 * Constructeur du browser
	 */
	public TagBrowser() {
		// initialisation du Layout et des contraintes
		setLayout(new BorderLayout());

		JPanel barreRecherche = new JPanel();
		// Initialisation et placement de la zone de recherche
		critere = new JTextField("", 10);
		barreRecherche.add(critere, BorderLayout.EAST);

		// Initialisation et placement du bouton rechercher
		rechercher = new JButton(new ImageIcon("images\\loupe.png"));
		barreRecherche.add(rechercher, BorderLayout.WEST);

		add(barreRecherche, BorderLayout.NORTH);

		// Resultat recherche
		texte = new JTextArea("");
		textContainer = new JScrollPane(texte);
		textContainer.setPreferredSize(new Dimension(200, 200));
		add(textContainer, BorderLayout.CENTER);

		// bouton tri
		String[] listeItems = { "Trier par", "Tag", "Date", "Lieu" };
		tri = new JComboBox(listeItems);
		add(tri, BorderLayout.SOUTH);
	}

	/**
	 * Initialise les contraintes de placement pour les éléments SWING
	 * 
	 * @param x
	 *            abcisse de l'élément
	 * @param y
	 *            ordonnée de l'élément
	 * @param w
	 *            largeur de l'élément
	 * @param h
	 *            hauteur de l'élément
	 */
	public void setConstraints(int x, int y, int w, int h) {
		cons.gridx = x;
		cons.gridy = y;
		cons.gridwidth = w;
		cons.gridheight = h;
	}
}
