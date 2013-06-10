package fenetre;

import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import donnees.Document;
import donnees.SQLConnector;

import table.TableBibliothequeModel;

/**
 * Panel qui sera contenu dans la fenêtre c'est lui qui contiendra les
 * différentes zones de l'application
 * 
 * @author Xavier
 * 
 */
public class GEDPanel extends JPanel implements ListSelectionListener {

	/**
	 * contraintes utilisées pour le placement des objets SWING
	 */
	private GridBagConstraints cons;
	/**
	 * TagBrowser permettant de rechercher les documents par "tag"
	 */
	private BrowserPanel browser;
	private DetailPanel detailPanel;
	/**
	 * Colonnes de la table
	 */
	private String[] colNames = { "id", "nom", "note", "chemin" };
	/**
	 * Données de la table
	 */
	private String[][] data;
	/**
	 * table
	 */
	private JTable table;
	private List<Document> docs;
	ImageIcon icon;
	JLabel miniature;
	JTextPane texte;
	JScrollPane textContainer;

	/**
	 * Constructeur du panel
	 */
	public GEDPanel() {
		// initialisation du Layout et des contraintes
		setLayout(new GridBagLayout());
		cons = new GridBagConstraints();

		browser = new BrowserPanel(this);// initialisation du TagBrowser
		
		icon = new ImageIcon("images\\GED.gif");
		icon = new ImageIcon(icon.getImage().getScaledInstance(250,
				250 * icon.getIconHeight() / icon.getIconWidth(),
				Image.SCALE_DEFAULT));
		miniature = new JLabel(icon);//initialisation de la miniature
		
		detailPanel = new DetailPanel();//initialisation du panneau de détails
		
		texte = new JTextPane();//initialisation du panneau de texte de description

		// Initialisation de la table
		table = new JTable();
		table.getSelectionModel().addListSelectionListener(this);
		updateTable();
		table.setFillsViewportHeight(true);
		JScrollPane scrollPane = new JScrollPane(table);// Permet de scroller si
														// la table est trop
														// grande

		/* Ajout des éléments au panel */
		cons.insets = new Insets(2, 2, 2, 2);

		setConstraints(0, 0, 1, 3);
		cons.anchor = GridBagConstraints.NORTH;
		add(browser, cons);
		setConstraints(1, 0, 1, 3);
		add(scrollPane, cons);
		
		// miniature
		setConstraints(2, 0, 1, 1);
		cons.anchor = GridBagConstraints.NORTH;
		add(miniature, cons);
		setConstraints(2, 1, 1, 1);
		add(detailPanel, cons);
		// Texte sous miniature
		setConstraints(2, 2, 1, 1);
		cons.fill = GridBagConstraints.BOTH;
		texte.setText("");
		texte.setPreferredSize(getSize());
		texte.setEditable(false);
		add(texte, cons);
		repaint();
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

	public JTable getTable() {
		return table;
	}

	public void DisplayContent() {
		data = new String[docs.size()][colNames.length];
		for (int i = 0; i < docs.size(); i++) {
			data[i][0] = Integer.toString(docs.get(i).getId());
			data[i][1] = docs.get(i).getTitle();
			if (docs.get(i).getNote() == 0) {
				data[i][2] = "pas de note";
			} else {
				data[i][2] = Integer.toString(docs.get(i).getNote()) + "/5";
			}
			data[i][3] = docs.get(i).getPath();
		}
		TableBibliothequeModel model = new TableBibliothequeModel(data,
				colNames);
		table.setModel(model);
		model.fireTableDataChanged();
		
		remove(miniature);
		remove(detailPanel);
		icon = new ImageIcon("images\\GED.gif");
		icon = new ImageIcon(icon.getImage().getScaledInstance(250,
				250 * icon.getIconHeight() / icon.getIconWidth(),
				Image.SCALE_DEFAULT));
		miniature = new JLabel(icon);
		setConstraints(2, 0, 1, 1);
		cons.anchor = GridBagConstraints.NORTH;
		add(miniature, cons);
		setConstraints(2, 1, 1, 1);
		detailPanel = new DetailPanel();
		add(detailPanel, cons);
		texte.setText("");
		repaint();
		revalidate();
	}

	public void updateTable() {
		Connection conn = SQLConnector.enableConnexion();
		docs = SQLConnector.executeSelectDocuments(conn);
		SQLConnector.closeConnexion(conn);

		DisplayContent();
	}

	public void updateForResearch(String col, String criteria) {
		Connection conn = SQLConnector.enableConnexion();
		docs = SQLConnector.executeResearchByCriteria(col, criteria, conn);
		SQLConnector.closeConnexion(conn);

		DisplayContent();
	}
	
	public void updateForTags(String tag) {
		Connection conn = SQLConnector.enableConnexion();
		docs = SQLConnector.getDocumentsByTag(tag, conn);
		SQLConnector.closeConnexion(conn);

		DisplayContent();		
	}
	
	public void updateForSeries(String serie) {
		Connection conn = SQLConnector.enableConnexion();
		docs = SQLConnector.getDocumentsBySerie(serie, conn);
		SQLConnector.closeConnexion(conn);

		DisplayContent();		
	}

	public void valueChanged(ListSelectionEvent listSelectionEvent) {

		if (listSelectionEvent.getValueIsAdjusting()) {
			return;
		}
		ListSelectionModel lsm = (ListSelectionModel) listSelectionEvent
				.getSource();
		if (lsm.isSelectionEmpty()) {// Pas de lignes selectionné - On affiche
										// un ?

		} else {// ligne selectionné - on affiche l'image de la miniature
			int selectedRow = lsm.getMinSelectionIndex();
			String chemin = (String) table.getValueAt(selectedRow, 3);
			icon = new ImageIcon(chemin);
			if (icon.getIconHeight() < icon.getIconWidth()) {
				icon = new ImageIcon(icon.getImage().getScaledInstance(250,
						250 * icon.getIconHeight() / icon.getIconWidth(),
						Image.SCALE_DEFAULT));
			} else {
				icon = new ImageIcon(icon.getImage().getScaledInstance(
						250 * icon.getIconWidth() / icon.getIconHeight(), 250,
						Image.SCALE_DEFAULT));
			}
			updateDetails(selectedRow);
		}
	}

	public void updateDetails(int selectedRow) {
		remove(miniature);
		remove(detailPanel);
		miniature = new JLabel(icon);
		setConstraints(2, 0, 1, 1);
		cons.anchor = GridBagConstraints.NORTH;
		add(miniature, cons);
		setConstraints(2, 1, 1, 1);
		detailPanel = new DetailPanel(Integer.parseInt((String) table
				.getValueAt(selectedRow, 0)));
		add(detailPanel, cons);
		texte.setText(detailPanel.getDocument().getDesc());
		repaint();
		revalidate();
	}
	
	public BrowserPanel getBrowserPanel(){
		return browser;
	}
}
