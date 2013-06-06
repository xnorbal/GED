package fenetre;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import donnees.Document;

import table.TableBibliothequeModel;

/**
 * Panel qui sera contenu dans la fenêtre c'est lui qui contiendra les
 * différentes zones de l'application
 * 
 * @author Xavier
 * 
 */
public class GEDPanel extends JPanel {

	/**
	 * contraintes utilisées pour le placement des objets SWING
	 */
	private GridBagConstraints cons;
	/**
	 * TagBrowser permettant de rechercher les documents par "tag"
	 */
	private TagBrowser browser;

	/**
	 * Colonnes de la table
	 */
	private String[] colNames = { "nom", "date", "note", "largeur", "hauteur", "taille", "chemin"};
	/**
	 * Données de la table
	 */
	private String[][] data;
	/**
	 * table
	 */
	private JTable table;
	private List<Document> docs;
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
		updateTable();
		table.setFillsViewportHeight(true);
		JScrollPane scrollPane = new JScrollPane(table);// Permet de scroller si la table est trop grande

		// Ajout des éléments au panel
		setConstraints(0, 0, 1, 1);
		add(browser, cons);
		setConstraints(1, 0, 1, 1);
		add(scrollPane, cons);
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
				docs.add(new Document(rs.getInt("I_ID"), rs.getDate("I_DATE"), rs.getInt("SIZE"), rs.getString("NOM"), rs.getString("CHEMIN"), rs.getInt("WIDTH"), rs.getInt("HEIGHT"), rs.getInt("NOTE")));
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		data = new String [docs.size()][colNames.length];
		for(int i = 0 ; i<docs.size();i++)
		{
			data[i][0]=docs.get(i).getTitle();
			data[i][1]=docs.get(i).getDate().toLocaleString();
			data[i][2]=Integer.toString(docs.get(i).getNote())+"/5";
			data[i][3]=Integer.toString(docs.get(i).getWidth());
			data[i][4]=Integer.toString(docs.get(i).getHeight());
			data[i][5]=Integer.toString(docs.get(i).getSize()/1024)+" Ko";
			data[i][6]=docs.get(i).getPath();
		}
		TableBibliothequeModel model = new TableBibliothequeModel(data, colNames);
		table.setModel(model);
		model.fireTableDataChanged();
	}
}
