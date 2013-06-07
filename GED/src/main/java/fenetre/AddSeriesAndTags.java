package fenetre;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument.Content;

import table.TableBibliothequeModel;

import donnees.Document;

public class AddSeriesAndTags extends JFrame implements ActionListener{
	JLabel label1;
	JLabel label2;
	JTextField newTag;
	JTextField newSerie;
	JButton addTag;
	JButton addSerie;
	JTable tags;
	JTable series;
	String[][] tagsData;
	String[][] seriesData;
	String[] colNames = {"nom"};
	
	
	GridBagConstraints cons;
	
	public AddSeriesAndTags(){
		super("Tags & Séries");
		
		label1 = new JLabel("Tags");
		label2 = new JLabel("Séries");
		
		newTag = new JTextField(10);
		newSerie = new JTextField(10);
		addTag = new JButton(new ImageIcon(new ImageIcon("images\\plus.png").getImage().getScaledInstance(15, 15, Image.SCALE_DEFAULT)));
		addTag.addActionListener(this);
		addSerie = new JButton(new ImageIcon(new ImageIcon("images\\plus.png").getImage().getScaledInstance(15, 15, Image.SCALE_DEFAULT)));
		addSerie.addActionListener(this);
		
		tags = new JTable();
		series = new JTable();
		
		updateTables();
		
		JScrollPane pane1 = new JScrollPane(tags);
		pane1.setPreferredSize(new Dimension(220, 300));
		JScrollPane pane2 = new JScrollPane(series);
		pane2.setPreferredSize(new Dimension(220, 300));
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		
		cons = new GridBagConstraints();
		cons.insets = new Insets(2,2,2,2);
		cons.anchor = GridBagConstraints.WEST;
		
		setConstraints(0, 0, 1, 1);
		panel.add(label1, cons);
		setConstraints(1, 0, 1, 1);
		panel.add(newTag, cons);
		setConstraints(2, 0, 1, 1);
		panel.add(addTag, cons);
		setConstraints(3, 0, 1, 1);
		panel.add(label2, cons);
		setConstraints(4, 0, 1, 1);
		panel.add(newSerie, cons);
		setConstraints(5, 0, 1, 1);
		panel.add(addSerie, cons);
		setConstraints(0, 1, 3, 1);
		panel.add(pane1, cons);
		setConstraints(3, 1, 3, 1);
		panel.add(pane2, cons);
		
		setContentPane(panel);
		pack();
		setVisible(true);
	}
	
	public void setConstraints(int x, int y, int w, int h) {
		cons.gridx = x;
		cons.gridy = y;
		cons.gridwidth = w;
		cons.gridheight = h;
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==addTag){
			Connection conn;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager.getConnection("jdbc:mysql://localhost/ged",
						"root", "");

				Statement stmt = conn.createStatement();
				stmt.executeUpdate("INSERT INTO TAG VALUES(NULL, \""+newTag.getText()+"\")");
				conn.close();
			} catch (SQLException exc) {
				exc.printStackTrace();
			} catch (ClassNotFoundException exc) {
				exc.printStackTrace();
			}
		}else if(e.getSource()==addSerie){
			Connection conn;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager.getConnection("jdbc:mysql://localhost/ged",
						"root", "");

				Statement stmt = conn.createStatement();
				stmt.executeUpdate("INSERT INTO SERIE VALUES(NULL, \""+newSerie.getText()+"\")");
				conn.close();
			} catch (SQLException exc) {
				exc.printStackTrace();
			} catch (ClassNotFoundException exc) {
				exc.printStackTrace();
			}
		}
		
		if(e.getSource() instanceof JButton){
			updateTables();
		}
	}
	
	public void updateTables(){
		List<String> allTags = new ArrayList<String>();
		List<String> allSeries = new ArrayList<String>();
		int i = 0;
		Connection conn;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost/ged",
					"root", "");

			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM TAG");
			while (rs.next()) {
				allTags.add(rs.getString("NOM"));
			}
			
			rs = stmt.executeQuery("SELECT * FROM SERIE");
			while (rs.next()) {
				allSeries.add(rs.getString("NOM"));
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		tagsData = new String[allTags.size()][colNames.length];
		seriesData = new String[allSeries.size()][colNames.length];
		
		
		for(int j = 0;j<allTags.size();j++){
			tagsData[j][0]=allTags.get(j);
		}
		
		for(int j = 0;j<allSeries.size();j++){
			seriesData[j][0]=allSeries.get(j);
		}
		
		TableBibliothequeModel model1 = new TableBibliothequeModel(tagsData, colNames);
		TableBibliothequeModel model2 = new TableBibliothequeModel(seriesData, colNames);
		tags.setModel(model1);
		series.setModel(model2);
		model1.fireTableDataChanged();
		model2.fireTableDataChanged();
	}
}
