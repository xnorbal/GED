package fenetre;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.List;

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
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import table.TableBibliothequeModel;

import donnees.SQLConnector;

/**
 * Tag browser de la GED
 * 
 * @author Xavier
 * 
 */
public class BrowserPanel extends JPanel implements ActionListener, ListSelectionListener {
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

	String[][] tagList;
	String[] colNames = { "tags" };
	List<String> tags;
	JTable tagTable;

	/**
	 * 
	 */
	private JComboBox tri;
	JScrollPane textContainer;

	/**
	 * Constructeur du browser
	 */
	public BrowserPanel(GEDPanel parent) {
		this.parent = parent;

		// initialisation du Layout et des contraintes
		setLayout(new GridBagLayout());
		cons = new GridBagConstraints();
		cons.insets = new Insets(2, 2, 2, 2);

		// Initialisation et placement de la zone de recherche
		setConstraints(0, 0, 1, 1);
		critere = new JTextField("", 10);
		add(critere, cons);

		// Initialisation et placement du bouton rechercher
		setConstraints(1, 0, 1, 1);
		rechercher = new JButton(new ImageIcon("images\\loupe.png"));
		rechercher.addActionListener(this);
		add(rechercher, cons);

		// bouton tri
		setConstraints(0, 1, 2, 1);
		cons.fill = GridBagConstraints.HORIZONTAL;
		String[] listeItems = { "Nom", "Date", "Note" };
		DefaultComboBoxModel model = new DefaultComboBoxModel();
		for (int i = 0; i < listeItems.length; i++) {
			model.addElement(listeItems[i]);
		}
		tri = new JComboBox(model);
		add(tri, cons);

		// table de tags
		tagTable = new JTable();
		tagTable.getSelectionModel().addListSelectionListener(this);
		updateTable();
		JScrollPane scrollPane = new JScrollPane(tagTable);
		scrollPane.setPreferredSize(getSize());
		setConstraints(0, 2, 2, 1);
		add(tagTable, cons);
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

	public void updateTable() {
		Connection conn = SQLConnector.enableConnexion();
		tags = SQLConnector.selectTags(conn);

		tagList = new String[tags.size()][colNames.length];
		for (int i = 0; i < tags.size(); i++) {
			tagList[i][0] = tags.get(i) + " ("
					+ SQLConnector.getNumberOfImagesByTags(tags.get(i), conn)
					+ ")";
		}
		TableBibliothequeModel model = new TableBibliothequeModel(tagList,
				colNames);

		tagTable.setModel(model);
		model.fireTableDataChanged();
	}

	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource() == rechercher) {
			System.out.println(tri.getModel().getSelectedItem().toString());
			parent.updateForResearch(tri.getModel().getSelectedItem()
					.toString(), critere.getText());
		}
	}

	public void valueChanged(ListSelectionEvent e) {
		ListSelectionModel lsm = (ListSelectionModel) e.getSource();
		if (!lsm.isSelectionEmpty()) {
			int selectedRow = lsm.getMinSelectionIndex();
			parent.updateForTags(tags.get(selectedRow));
		}
	}
}
