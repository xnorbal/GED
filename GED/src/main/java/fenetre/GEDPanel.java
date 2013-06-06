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
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import donnees.Document;

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
	private TagBrowser browser;
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
	JTextField texte;
	JScrollPane textContainer;

	/**
	 * Constructeur du panel
	 */
	public GEDPanel() {
		// initialisation du Layout et des contraintes
		setLayout(new GridBagLayout());
		cons = new GridBagConstraints();

		browser = new TagBrowser();// initialisation du TagBrowser

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
		add(browser, cons);
		setConstraints(1, 0, 1, 3);
		add(scrollPane, cons);
		// miniature
		setConstraints(2, 0, 1, 1);

		icon = new ImageIcon("images\\unknow.jpg");
		icon = new ImageIcon(icon.getImage().getScaledInstance(250,
				250 * icon.getIconHeight() / icon.getIconWidth(),
				Image.SCALE_DEFAULT));
		miniature = new JLabel(icon);
		cons.anchor = GridBagConstraints.NORTH;
		add(miniature, cons);
		setConstraints(2, 1, 1, 1);
		detailPanel=new DetailPanel();
		add(detailPanel, cons);
		// Texte sous miniature
		setConstraints(2, 2, 1, 1);
		cons.fill = GridBagConstraints.BOTH;
		texte = new JTextField("");
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

	public void updateTable() {
		docs = new ArrayList<Document>();
		// Connection à la BD
		Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("driver OK");
			conn = DriverManager.getConnection("jdbc:mysql://localhost/ged",
					"root", "");
			System.out.println("connection OK");
			// Extraction des données
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM IMAGE");
			while (rs.next()) {
				docs.add(new Document(rs.getInt("I_ID"), rs.getDate("I_DATE"),
						rs.getInt("SIZE"), rs.getString("NOM"), rs
								.getString("CHEMIN"), rs.getInt("WIDTH"), rs
								.getInt("HEIGHT"), rs.getInt("NOTE")));
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		data = new String[docs.size()][colNames.length];
		for (int i = 0; i < docs.size(); i++) {
			data[i][0] = Integer.toString(docs.get(i).getId());
			data[i][1] = docs.get(i).getTitle();
			data[i][2] = Integer.toString(docs.get(i).getNote()) + "/5";
			data[i][3] = docs.get(i).getPath();
		}
		TableBibliothequeModel model = new TableBibliothequeModel(data,
				colNames);
		table.setModel(model);
		model.fireTableDataChanged();
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
			icon = new ImageIcon(icon.getImage().getScaledInstance(250,
					250 * icon.getIconHeight() / icon.getIconWidth(),
					Image.SCALE_DEFAULT));
			remove(miniature);
			remove(detailPanel);
			miniature = new JLabel(icon);
			setConstraints(2, 0, 1, 1);
			cons.anchor = GridBagConstraints.NORTH;
			add(miniature, cons);
			setConstraints(2, 1, 1, 1);
			detailPanel=new DetailPanel(Integer.parseInt((String)table.getValueAt(selectedRow, 0)));
			add(detailPanel, cons);
			texte.setText(detailPanel.getDocument().getDesc());
			repaint();
			revalidate();
		}
	}

}
