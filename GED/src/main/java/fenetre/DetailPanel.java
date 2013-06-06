package fenetre;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JTextField;

import donnees.Document;

public class DetailPanel extends JPanel {
	private Document d;
	private String[] legendes = { "largeur :", "hauteur :", "taille :", "tags :",
			"séries :" };
	private JLabel[] labels;
	private JLabel[] details;
	private GridBagConstraints cons;

	public DetailPanel(){
	}
	
	public DetailPanel(int id) {
		Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost/ged",
					"root", "");
			// Extraction des données
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
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		labels = new JLabel[legendes.length];
		details = new JLabel[legendes.length];
		for (int i = 0; i < legendes.length; i++) {
			labels[i] = new JLabel(legendes[i]);
			details[i] = new JLabel();
		}
		
		details[0].setText(Integer.toString(d.getWidth())+ " px");
		details[1].setText(Integer.toString(d.getHeight())+ " px");
		details[2].setText(Integer.toString(d.getSize() / 1024) + " Ko");
		details[3].setText(d.getTags().replace("\n", ", "));
		details[4].setText(d.getSeries().replace("\n", ", "));

		setLayout(new GridBagLayout());
		cons = new GridBagConstraints();
		cons.insets=new Insets(2,2,2,2);
		
		for (int i = 0; i < labels.length; i++) {
			setConstraints(0,i,1,1);
			cons.anchor=GridBagConstraints.EAST;
			add(labels[i], cons);
			setConstraints(1, i, 1, 1);
			cons.anchor=GridBagConstraints.WEST;
			add(details[i], cons);
		}
	}
	
	public void setConstraints(int x, int y, int w, int h) {
		cons.gridx = x;
		cons.gridy = y;
		cons.gridwidth = w;
		cons.gridheight = h;
	}
	
	public Document getDocument(){
		return d;
	}
}
