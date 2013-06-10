package fenetre;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextPane;

import donnees.FlickrAuth;
import donnees.SQLConnector;

public class OptionsFrame extends JFrame implements ActionListener{
	
	private JButton lienAuth;
	private JButton lierCompte;
	private FlickrAuth compte;
	private int autorizeBefore;
	
	public OptionsFrame() {
		
		autorizeBefore = 0;
		
		JPanel optionsPanel= new JPanel();
		optionsPanel.setLayout(new GridBagLayout());
		GridBagConstraints contraintes = new GridBagConstraints();
		contraintes.gridx = 0;
		contraintes.gridy = 0;
		contraintes.gridwidth = 1;
		contraintes.gridheight = 1;
		contraintes.insets = new Insets(2, 2, 2, 2);
		contraintes.anchor = GridBagConstraints.WEST;
		
		optionsPanel.add(new JLabel("Compte Flickr :"), contraintes);
		contraintes.gridy = 1;
		try {
			compte = new FlickrAuth();
		} catch (Exception e) {
			e.printStackTrace();
		}

		JLabel instructions = new JLabel();
		instructions.setText("<html>Pour poster vos images sur Flickr, vous devez autoriser l'application à accéder à votre compte Flickr.<br/>"
		+"Pour cela, connecter vous à votre compte Flickr via votre navigateur puis cliquez sur le bouton suivant:</html>");
		
		optionsPanel.add(instructions, contraintes);
		
		lienAuth = new JButton("Autoriser l'application");
		lienAuth.addActionListener(this);
		contraintes.gridy = 2;
		optionsPanel.add(lienAuth, contraintes);
		
		contraintes.gridy = 3;
		instructions = new JLabel("Après avoir autorisé l'application, cliquez sur le bouton suivant pour lier votre compte Flickr:");
		optionsPanel.add(instructions, contraintes);
		
		lierCompte = new JButton("Lier votre compte Flickr");
		lierCompte.addActionListener(this);
		contraintes.gridy = 4;
		optionsPanel.add(lierCompte, contraintes);
		
		setContentPane(optionsPanel);
		pack();
		setVisible(true);
		setResizable(false);
	}

	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == lienAuth) {
			Properties sys = System.getProperties();
			String os = sys.getProperty("os.name");
			Runtime r = Runtime.getRuntime();
			try
			{
				r.exec("cmd /c start iexplore \""+compte.getLien()+"\"");
				autorizeBefore = 1;
			}
			catch (IOException ex)
			{
			ex.printStackTrace();
			} 
		}
		else if(e.getSource() == lierCompte) {
			if(autorizeBefore != 0) {
				String token = new String();
				try {
					token = compte.getToken();
					Connection conn = SQLConnector.enableConnexion();
					System.out.println("INSERT INTO compte_flickr VALUES(NULL, "+token+")");
					SQLConnector.executeUpdateInsert(conn, "INSERT INTO compte_flickr VALUES(NULL, \""+token+"\")");
					SQLConnector.closeConnexion(conn);
					this.dispose();
					
				} catch (Exception e1) {
					token = "Erreur";
				}
			}
		}
		
	}

}
