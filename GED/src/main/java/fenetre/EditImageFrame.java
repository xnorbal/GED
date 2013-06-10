package fenetre;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

import donnees.Document;
import donnees.SQLConnector;

public class EditImageFrame extends JFrame implements ActionListener {

	private Document doc;
	private JComboBox choixTag;
	private JComboBox choixSerie;
	private JComboBox note;
	private JButton addTag;
	private JButton addSerie;
	private JTextArea description;
	private JButton enregistrer;
	private JButton annuler;
	private JList tagsAppliques;
	private DefaultListModel modelTagsAppliques;
	private JList seriesAppliques;
	private DefaultListModel modelSeriesAppliques;
	private GEDPanel gedPanel;
	private int row;
	private List<String> tagsAjoutes;
	private List<String> seriesAjoutes;

	public EditImageFrame(String parTitre, Document parDoc, GEDPanel gedPanel,
			int row) {
		super(parTitre);// appel au constructeur de la classe mère
		this.doc = parDoc;
		this.gedPanel = gedPanel;
		this.row = row;

		setLocation(100, 100);// place la fenêtre à 100 px du bord haut de
		// l'écran, et 100 px du bord droit


		// ajout du JPanel
		JPanel modifPanel = new JPanel();
		modifPanel.setLayout(new GridBagLayout());
		GridBagConstraints contraintes = new GridBagConstraints();
		contraintes.gridx = 1;
		contraintes.gridy = 1;
		contraintes.gridwidth = 1;
		contraintes.gridheight = 1;
		contraintes.insets = new Insets(2, 2, 2, 2);

		ImageIcon icon = new ImageIcon(doc.getPath());
		icon = new ImageIcon(icon.getImage().getScaledInstance(300,
				300 * icon.getIconHeight() / icon.getIconWidth(),
				Image.SCALE_DEFAULT));

		JLabel miniature = new JLabel(icon);
		contraintes.gridheight = 6;
		modifPanel.add(miniature, contraintes);

		contraintes.gridx = 2;
		contraintes.gridheight = 1;
		contraintes.anchor = GridBagConstraints.EAST;
		modifPanel.add(new JLabel("Tags:"), contraintes);

		/* Combo Box des Tags */
		DefaultComboBoxModel comboModel = new DefaultComboBoxModel();

		contraintes.gridx = 3;
		contraintes.gridheight = 1;
		contraintes.anchor = GridBagConstraints.CENTER;

		Connection conn = SQLConnector.enableConnexion();
		List<String> tags = SQLConnector.selectTags(conn);

		if (tags.size() == 0) {
			comboModel.addElement("Aucun tag");
		} else {
			for (String s : tags) {
				comboModel.addElement(s);
			}
		}
		choixTag = new JComboBox(comboModel);
		choixTag.setPreferredSize(new Dimension(150, 20));

		modifPanel.add(choixTag, contraintes);

		addTag = new JButton("+");
		addTag.addActionListener(this);
		contraintes.gridx = 4;

		modifPanel.add(addTag, contraintes);

		contraintes.gridx = 2;
		contraintes.gridy = 2;
		contraintes.gridheight = 1;
		contraintes.anchor = GridBagConstraints.EAST;
		modifPanel.add(new JLabel("Séries:"), contraintes);

		/* Combo Box des Séries */
		comboModel = new DefaultComboBoxModel();

		contraintes.gridx = 3;
		contraintes.anchor = GridBagConstraints.CENTER;

		List<String> series = SQLConnector.selectSeries(conn);
		SQLConnector.closeConnexion(conn);

		if (series.size() == 0) {
			comboModel.addElement("Aucune série");
		} else {
			for (String s : series) {
				comboModel.addElement(s);
			}
		}
		choixSerie = new JComboBox(comboModel);
		choixSerie.setPreferredSize(new Dimension(150, 20));

		modifPanel.add(choixSerie, contraintes);

		addSerie = new JButton("+");
		addSerie.addActionListener(this);
		contraintes.gridx = 4;

		modifPanel.add(addSerie, contraintes);

		/* Combo Box de la note */
		comboModel = new DefaultComboBoxModel();
		for (int i = 0; i < 6; i++) {
			if (i == 0) {
				comboModel.addElement("pas de note");
			} else {
				comboModel.addElement(i);
			}
		}
		note = new JComboBox(comboModel);
		note.setPreferredSize(new Dimension(100, 20));
		note.getModel().setSelectedItem(
				note.getModel().getElementAt(doc.getNote()));
		((JLabel) note.getRenderer())
				.setHorizontalAlignment(SwingConstants.CENTER);

		contraintes.gridx = 2;
		contraintes.gridy = 3;
		contraintes.anchor = GridBagConstraints.EAST;
		JLabel noteLabel = new JLabel("Note:");
		modifPanel.add(noteLabel, contraintes);
		contraintes.gridx = 3;
		contraintes.anchor = GridBagConstraints.CENTER;
		modifPanel.add(note, contraintes);

		contraintes.gridx = 2;
		contraintes.gridy = 4;
		contraintes.gridwidth = 1;
		contraintes.anchor = GridBagConstraints.SOUTHWEST;
		modifPanel.add(new JLabel("Description:"), contraintes);

		description = new JTextArea(doc.getDesc());
		description.setRows(5);
		description.setColumns(25);
		JScrollPane zoneScrollable = new JScrollPane(description,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		contraintes.gridwidth = 3;
		contraintes.gridy = 5;
		contraintes.anchor = GridBagConstraints.NORTH;
		modifPanel.add(zoneScrollable, contraintes);

		// Initialisation des modèles des listes
		modelTagsAppliques = new DefaultListModel();
		modelSeriesAppliques = new DefaultListModel();

		/* La liste des tags appliqués à l'image */

		tagsAppliques = new JList(modelTagsAppliques); // data has type Object[]
		tagsAppliques
				.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		tagsAppliques.setLayoutOrientation(JList.VERTICAL);
		tagsAppliques.setVisibleRowCount(1);
		JScrollPane listScroller = new JScrollPane(tagsAppliques);
		listScroller.setPreferredSize(new Dimension(100, 80));

		String listeTags = doc.getTags();
		String[] docTags = listeTags.split("\n");

		for (int i = 0; i < docTags.length; i++) {
			modelTagsAppliques.addElement(docTags[i]);
		}

		contraintes.gridwidth = 1;
		contraintes.gridx = 5;
		contraintes.gridy = 1;

		modifPanel.add(listScroller, contraintes);

		/* La liste des séries appliquées à l'image */

		seriesAppliques = new JList(modelSeriesAppliques);
		seriesAppliques
				.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		seriesAppliques.setLayoutOrientation(JList.VERTICAL);
		seriesAppliques.setVisibleRowCount(1);
		listScroller = new JScrollPane(seriesAppliques);
		listScroller.setPreferredSize(new Dimension(100, 80));

		String listeSeries = doc.getSeries();
		String[] docSeries = listeSeries.split("\n");

		for (int i = 0; i < docSeries.length; i++) {
			modelSeriesAppliques.addElement(docSeries[i]);
		}

		contraintes.gridwidth = 1;
		contraintes.gridx = 5;
		contraintes.gridy = 2;

		modifPanel.add(listScroller, contraintes);

		/**
		 * Ajout es boutons Enregisrer et Annuler
		 */

		enregistrer = new JButton("Enregistrer");
		annuler = new JButton("Annuler");
		enregistrer.addActionListener(this);
		annuler.addActionListener(this);

		contraintes.gridwidth = 1;
		contraintes.gridx = 3;
		contraintes.gridy = 6;
		modifPanel.add(enregistrer, contraintes);
		contraintes.gridx = 4;
		modifPanel.add(annuler, contraintes);

		tagsAjoutes = new ArrayList<String>();
		seriesAjoutes = new ArrayList<String>();

		setContentPane(modifPanel);
		pack();
		setVisible(true);
		setResizable(false);
	}

	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource() instanceof JButton) {
			JButton button = (JButton) arg0.getSource();
			if (button == addTag && choixTag.getSelectedItem() != "Aucun tag") {
				if (!modelTagsAppliques.contains(choixTag.getSelectedItem())) {
					modelTagsAppliques.addElement(choixTag.getSelectedItem());
					doc.addTag((String) choixTag.getSelectedItem());
					tagsAjoutes.add((String) choixTag.getSelectedItem());
				}
			} else if (button == addSerie
					&& choixSerie.getSelectedItem() != "Aucune série") {
				if (!modelSeriesAppliques
						.contains(choixSerie.getSelectedItem())) {
					modelSeriesAppliques.addElement(choixSerie
							.getSelectedItem());
					doc.addSerie((String) choixSerie.getSelectedItem());
					seriesAjoutes.add((String) choixSerie.getSelectedItem());
				}
			} else if (button == annuler) {
				this.dispose();
			} else if (button == enregistrer) {
				String requete = new String("");
				Connection conn = SQLConnector.enableConnexion();
				requete += "UPDATE IMAGE SET NOTE=";
				requete += note.getSelectedIndex();
				requete += ", DESCRIPTION=\"";
				requete += description.getText().replace("\"", "\\\"") + "\"";
				requete += " WHERE I_ID=" + doc.getId();
				System.out.println(requete);
				SQLConnector.executeUpdateInsert(conn, requete);
				// Insérer les tags et séries
				String listTags = doc.getTags();

				if (!listTags.equals("")) {
					String[] tags = listTags.split("\n");
					for (int i = 0; i < tags.length; i++) {
						if (tagsAjoutes.contains(tags[i])) {
							int idTag = SQLConnector.getTagIdByName(conn,
									tags[i]);
							requete = "INSERT INTO IMTAG VALUES(" + doc.getId()
									+ "," + idTag + ")";
							SQLConnector.executeUpdateInsert(conn, requete);
						}
					}
				}

				String listSeries = doc.getSeries();
				if (!listSeries.equals("")) {
					String[] series = listSeries.split("\n");
					for (int i = 0; i < series.length; i++) {
						if (seriesAjoutes.contains(series[i])) {
							int idSerie = SQLConnector.getSerieIdByName(conn,
									series[i]);
							requete = "INSERT INTO IMSERIE VALUES("
									+ doc.getId() + "," + idSerie + ")";
							SQLConnector.executeUpdateInsert(conn, requete);
						}
					}
				}

				SQLConnector.closeConnexion(conn);
				gedPanel.updateTable();
				gedPanel.updateDetails(row);
				gedPanel.getBrowserPanel().updateTable();
				this.dispose();
			}
		}
	}
}