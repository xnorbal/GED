package fenetre;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;

import donnees.Document;
import donnees.FiltreImage;

/**
 * Fenetre de l'application GED c'est elle qui contient la barre de menu
 * 
 * @author Xavier
 * 
 */
public class GEDFrame extends JFrame implements ActionListener {
	/**
	 * Bouton d'ajout d'un ou plusieurs fichiers à la GED
	 */
	private JButton ajout;
	/**
	 * Bouton de suppression d'un fichier à la GED
	 */
	private JButton suppression;
	/**
	 * Bouton d'aide de la GED
	 */
	private JButton info;
	/**
	 * Bouton ouvrant l'image dans le programme par défaut
	 */
	private JButton ouvrir;
	/**
	 * Bouton permettant d editer les infos de base
	 */
	private JButton editer;
	/**
	 * Bouton permettant d'ajouter tags et series
	 */
	private JButton ajoutFav;
	/**
	 * Panel de la JFrame
	 */
	private GEDPanel myGED;

	/**
	 * Constructeur de la fenêtre
	 * 
	 * @param titre
	 *            titre de la fenêtre
	 */
	public GEDFrame(String titre) {
		super(titre);// appel au constructeur de la classe mère

		setLocation(100, 100);// place la fenêtre à 100 px du bord haut de
								// l'écran, et 100 px du bord droit

		setDefaultCloseOperation(EXIT_ON_CLOSE);// Pour kill le processus quand
												// on ferme la fenêtre

		// initialisation des trois boutons avec des images
		ajout = new JButton("", new ImageIcon("images\\plus.png"));
		ajout.setToolTipText("ajouter une/des image(s)");
		suppression = new JButton("", new ImageIcon("images\\supprimer.png"));
		suppression.setToolTipText("supprimer une/des image(s)");
		info = new JButton("", new ImageIcon("images\\help.png"));
		ouvrir = new JButton("", new ImageIcon("images\\ouvrir.png"));
		ouvrir.setToolTipText("Ouvrir une image avec le programme par défaut");
		editer = new JButton("", new ImageIcon("images\\editer.png"));
		editer.setToolTipText("Editer les données de l'image");
		ajoutFav = new JButton("", new ImageIcon("images\\ajout_fav.png"));
		ajoutFav.setToolTipText("Ajouter des tags et/ou séries");

		ouvrir.addActionListener(this);
		info.addActionListener(this);
		suppression.addActionListener(this);
		ajout.addActionListener(this);
		ajoutFav.addActionListener(this);
		editer.addActionListener(this);

		// initialisation de la barre de menu
		setJMenuBar(initAndSetMenuBar());

		// ajout du JPanel
		myGED = new GEDPanel();
		setContentPane(myGED);

		// Finalisation de la fenêtre : taille, visibilité, et redimensionnement
		pack();
		setVisible(true);
		setResizable(false);
	}

	/**
	 * Initialise la JMenuBar pour le constructeur
	 * 
	 * @return la JMenuBar de la fenêtre
	 */
	public JMenuBar initAndSetMenuBar() {
		JMenuBar menuBar = new JMenuBar();// initialisation de la JMenuBar

		// Ajout des boutons
		menuBar.add(ajout);
		menuBar.add(suppression);
		menuBar.add(ouvrir);
		menuBar.add(editer);
		menuBar.add(ajoutFav);
		menuBar.add(new JSeparator(JSeparator.VERTICAL));
		menuBar.add(info);

		return menuBar;
	}

	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource() instanceof JButton) {
			JButton button = (JButton) arg0.getSource();
			if (button == ouvrir) {
				System.out.println(button.getIcon());
				int row = myGED.getTable().getSelectedRow();
				String chemin = (String) myGED.getTable().getValueAt(row, 3);
				if (row >= 0) {
					// Récupération du chemin associé à la ligne
					// dans la 4eme colonne le chemin d'accè du fichier
					OpenPictureWindowsView(chemin);
					// Attention si pas de ligne selectionné ca ne marche pas
				}

			} else if (button == ajout) {
				OpenPicture();
				// A la sorie enregistrer dans la TABLE
			} else if (button == info) {
				try {
					FileReader fic = new FileReader("infos.txt");
					BufferedReader buff = new BufferedReader(fic);
					String ligne = new String("");
					String texte = new String("");
					int cpt = 0;
					while ((ligne = buff.readLine()) != null) {
						texte+=ligne+"\n";
					}
					fic.close();
					JOptionPane.showMessageDialog(this, texte);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else if (button == suppression) {
				// supprimer la ligne le lien vers le fichier
				// Dans la table
			} else if (button == editer) {
				int row = myGED.getTable().getSelectedRow();
				int id =Integer.parseInt((String) myGED.getTable().getValueAt(row, 0));
				try {
					Class.forName("com.mysql.jdbc.Driver");
					Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/ged",
							"root", "");
					// Extraction des données
					Document d = null;
					Statement stmt = conn.createStatement();
					ResultSet rs = stmt.executeQuery("SELECT * FROM IMAGE WHERE I_ID="
							+ id);
					while (rs.next()) {
						d = new Document(rs.getInt("I_ID"), rs.getDate("I_DATE"),
								rs.getInt("SIZE"), rs.getString("NOM"),
								rs.getString("CHEMIN"), rs.getInt("WIDTH"),
								rs.getInt("HEIGHT"), rs.getInt("NOTE"));
					}
					rs = stmt
							.executeQuery("SELECT NOM FROM IMTAG I, TAG T WHERE I.T_ID=T.T_ID AND I.I_ID="
									+ id);
					while (rs.next()) {
						d.addTag(rs.getString("NOM"));
					}
					rs = stmt
							.executeQuery("SELECT NOM FROM IMSERIE I, SERIE S WHERE I.S_ID=S.S_ID AND I.I_ID="
									+ id);
					while (rs.next()) {
						d.addTag(rs.getString("NOM"));
					}
					conn.close();
					new EditImageFrame("", d);
				} catch (SQLException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			} else if (button == ajoutFav) {
				new AddSeriesAndTags();
				// Ouverture de la fenêtre d'ajout de series et tags
			}
		}
	}

	public void OpenPicture() {
		JFileChooser fc = new JFileChooser();
		FiltreImage filtre = new FiltreImage(new String[] { "gif", "png",
				"jpeg", "jpg" }, "fichiers image (*.gif, *.png,*.jpeg)");
		fc.setMultiSelectionEnabled(true);
		fc.setCurrentDirectory(new File("C:\\tmp"));
		fc.setAcceptAllFileFilterUsed(false);
		fc.addChoosableFileFilter(filtre);

		int retVal = fc.showOpenDialog(this);
		if (retVal == JFileChooser.APPROVE_OPTION) {
			File[] selectedfiles = fc.getSelectedFiles();
			ImageIcon icon;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				System.out.println("driver OK");
				java.sql.Connection conn = DriverManager.getConnection(
						"jdbc:mysql://localhost/ged", "root", "");
				System.out.println("connection OK");
				// Extraction des données
				Statement stmt = conn.createStatement();
				String request;
				Date d;
				for (int i = 0; i < selectedfiles.length; i++) {
					icon = new ImageIcon(selectedfiles[i].getAbsolutePath());
					d = new Date(selectedfiles[i].lastModified());
					request = "INSERT INTO IMAGE VALUES(NULL ,\""
							+ selectedfiles[i].getName()
							+ "\",\""
							+ selectedfiles[i].getAbsolutePath().replace("\\",
									"\\\\") + "\",\"" + d.toString() + "\","
							+ icon.getIconWidth() + "," + icon.getIconHeight()
							+ "," + (int) selectedfiles[i].length() + "," + 0
							+ ")";
					System.out.println(request);
					stmt.executeUpdate(request);
				}
				conn.close();
				myGED.updateTable();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	public void OpenPictureWindowsView(String chemin) {

		String fileName = chemin;
		String[] commands = { "cmd.exe", "/c", "start", "\"DummyTitle\"",
				"\"" + fileName + "\"" };
		Process p;
		try {
			p = Runtime.getRuntime().exec(commands);
			p.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Done.");
	}
}
