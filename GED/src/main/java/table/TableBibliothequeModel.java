package table;

import java.util.List;

import javax.swing.table.AbstractTableModel;

/**
 * Mod�le de fabrication pour la table d'affichage des donn�es
 * @author Xavier
 *
 */
public class TableBibliothequeModel extends AbstractTableModel {

	/**
	 * Noms des colonnes de la table
	 */
	private String[] columnNames;
	/**
	 * Donn�es de la table
	 */
	private String[][] data;

	/**
	 * Constructeur de la table
	 * @param pData donn�es
	 * @param colNames nom des colonnes
	 */
	public TableBibliothequeModel(String[][] pData, String[] colNames) {
		columnNames = colNames;
		data = pData;
	}
	
	/**
	 * Retourne le nombre de colonnes de la table
	 */
	public int getColumnCount() {
		return columnNames.length;
	}

	/**
	 * Retourne le nombre de lignes de la table
	 */
	public int getRowCount() {
		return data.length;
	}

	/**
	 * Retourne le nom de la colonne col
	 */
	public String getColumnName(int col) {
		return columnNames[col];
	}

	/**
	 * Retourne la valeur de la ligne row � la colonne col
	 */
	public Object getValueAt(int row, int col) {
		return data[row][col];
	}

	/**
	 * Retourne la classe de la colonne c
	 */
	public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }
}
