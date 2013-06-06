package fenetre;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuBar;

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
	private JButton aide;
	/**
	 * Bouton ouvrant l'image dans le programme par défaut
	 */
	private JButton ouvrir;
	/**
	 * Bouton permettant d editer les infos de base
	 */
	private JButton editer;
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
		suppression = new JButton("", new ImageIcon("images\\supprimer.png"));
		aide = new JButton("", new ImageIcon("images\\help.png"));
		ouvrir = new JButton("", new ImageIcon("images\\ouvrir.png"));
		editer = new JButton("", new ImageIcon("images\\editer.png"));

		ouvrir.addActionListener(this);
		aide.addActionListener(this);
		suppression.addActionListener(this);
		ajout.addActionListener(this);

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
		menuBar.add(aide);
		menuBar.add(ouvrir);
		menuBar.add(editer);

		return menuBar;
	}

	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource() instanceof JButton) {
			JButton button = (JButton) arg0.getSource();
			if (button == ouvrir) {
				System.out.println(button.getIcon());
				int row = myGED.getTable().getSelectedRow();
				String chemin = (String) myGED.getTable().getValueAt(row, 6);
				if (row >= 0) {
					// Récupération du chemin associé à la ligne
					// dans la 4eme colonne le chemin d'accè du fichier
					OpenPictureWindowsView(chemin);
					// Attention si pas de ligne selectionné ca ne marche pas
				}

			} else if (button == ajout) {
				OpenPicture();
				// A la sorie enregistrer dans la TABLE
			} else if (button == aide) {
				// Ouverture du fichier README
			} else if (button == suppression) {

				// supprimer la ligne le lien vers le fichier
				// Dans la table
			} else if (button == editer){
				//Ouverture de la fenetre d'edition
			}
		}
	}

	public void OpenPicture() {
		JFileChooser fc = new JFileChooser();
		FiltreImage filtre = new FiltreImage(new String[]{"gif","png","jpeg","jpg"}, "fichiers image (*.gif, *.png,*.jpeg)");
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
					d =  new Date(selectedfiles[i].lastModified());
					request = "INSERT INTO IMAGE VALUES(NULL ,\""
							+ selectedfiles[i].getName() + "\",\""
							+ selectedfiles[i].getAbsolutePath().replace("\\", "\\\\") + "\",\""
							+ d.toString() + "\","
							+ icon.getIconWidth() + "," + icon.getIconHeight()
							+ "," + (int)selectedfiles[i].length() + "," + 0
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
