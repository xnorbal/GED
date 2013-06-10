package fenetre;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
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
public class BrowserPanel extends JPanel implements ActionListener{
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
	 * Conteneur parent
	 */
	GEDPanel parent;

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
	public BrowserPanel() {
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
		String[] listeItems = {"Date", "Lieu" };
		DefaultComboBoxModel model = new DefaultComboBoxModel();
		for (int i = 0;i<listeItems.length;i++) {
			model.addElement(listeItems[i]);
		}
		tri = new JComboBox();
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



	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource() == rechercher){
			
		}
	}
}
