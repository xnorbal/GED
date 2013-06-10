package donnees;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import donnees.Document;

public class SQLConnector {

	public static Connection enableConnexion() {
		Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost/ged",
					"root", "");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return (conn);
	}

	public static int executeUpdateInsert(Connection conn, String requete) {
		Statement stmt;
		int retour = -1;
		try {
			stmt = conn.createStatement();
			retour = stmt.executeUpdate(requete);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return (retour);
	}

	public static List<Document> executeSelectDocuments(Connection conn) {
		Statement stmt;
		List<Document> resultat = new ArrayList<Document>();
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM IMAGE");
			while (rs.next()) {
				resultat.add(new Document(rs.getInt("I_ID"), rs
						.getDate("I_DATE"), rs.getInt("SIZE"), rs
						.getString("NOM"), rs.getString("CHEMIN"), rs
						.getInt("WIDTH"), rs.getInt("HEIGHT"), rs
						.getInt("NOTE"), rs.getString("DESCRIPTION")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultat;
	}

	public static Document executeSelectDocument(Connection conn, int id) {
		Statement stmt;
		Document d = new Document();
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM IMAGE WHERE I_ID="
					+ id);
			rs.next();
			d = new Document(rs.getInt("I_ID"), rs.getDate("I_DATE"),
					rs.getInt("SIZE"), rs.getString("NOM"),
					rs.getString("CHEMIN"), rs.getInt("WIDTH"),
					rs.getInt("HEIGHT"), rs.getInt("NOTE"),
					rs.getString("DESCRIPTION"));
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
				d.addSerie(rs.getString("NOM"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return d;
	}
	
	public static List<String> selectTags(Connection conn){
		Statement stmt;
		List<String> retour = new ArrayList<String>();
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT NOM FROM TAG");
			while (rs.next()) {
				retour.add(rs.getString("NOM"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return retour;
	}
	
	public static List<String> selectSeries(Connection conn){
		Statement stmt;
		List<String> retour = new ArrayList<String>();
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT NOM FROM SERIE");
			while (rs.next()) {
				retour.add(rs.getString("NOM"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return retour;
	}

	public static int getTagIdByName(Connection conn, String name) {
		Statement stmt;
		int retour = -1;
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT T_ID FROM TAG WHERE NOM =\"" + name
							+ "\"");
			while (rs.next()) {
				retour = rs.getInt("T_ID");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return retour;
	}

	public static int getSerieIdByName(Connection conn, String name) {
		Statement stmt;
		int retour = -1;
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT S_ID FROM SERIE WHERE NOM =\"" + name
							+ "\"");
			while (rs.next()) {
				retour = rs.getInt("S_ID");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return retour;
	}
	
	public static String getTokenAccount(Connection conn) {
		Statement stmt;
		String retour = new String("");
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT TOKEN FROM compte_flickr LIMIT 0,1");
			while (rs.next()) {
				retour = rs.getString("TOKEN");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return retour;
	}
	
	public static List<Document> executeResearchByCriteria(String col, String criteria, Connection conn){
		Statement stmt;
		List<Document> resultat = new ArrayList<Document>();
		try {
			stmt = conn.createStatement();
			if(col.equals("Date")){
				col = "I_DATE";
			}
			ResultSet rs = stmt.executeQuery("SELECT * FROM IMAGE WHERE "+col+"=\""+criteria+"\"");
			while (rs.next()) {
				resultat.add(new Document(rs.getInt("I_ID"), rs
						.getDate("I_DATE"), rs.getInt("SIZE"), rs
						.getString("NOM"), rs.getString("CHEMIN"), rs
						.getInt("WIDTH"), rs.getInt("HEIGHT"), rs
						.getInt("NOTE"), rs.getString("DESCRIPTION")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultat;
	}
	
	public static int getNumberOfImagesByTags(String tagName, Connection conn){
		Statement stmt;
		int res=0;
		try {
			stmt = conn.createStatement();
			int tagId;
			ResultSet rs = stmt.executeQuery("SELECT T_ID FROM TAG WHERE NOM=\""+tagName+"\"");
			rs.next();
			tagId=rs.getInt(1);
			rs = stmt.executeQuery("SELECT COUNT(T_ID) FROM IMTAG WHERE T_ID="+tagId+" GROUP BY T_ID");
			if(rs.next()){
				res = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}
	
	public static List<Document> getDocumentsByTag(String tagName, Connection conn){
		Statement stmt;
		List<Document> resultat = new ArrayList<Document>();
		try {
			stmt = conn.createStatement();
			int tagId;
			ResultSet rs = stmt.executeQuery("SELECT T_ID FROM TAG WHERE NOM=\""+tagName+"\"");
			rs.next();
			tagId=rs.getInt(1);
			rs = stmt.executeQuery("SELECT * FROM IMAGE D WHERE EXISTS(SELECT * FROM IMTAG I WHERE I.I_ID = D.I_ID AND T_ID="+tagId+")");
			while (rs.next()) {
				resultat.add(new Document(rs.getInt("I_ID"), rs
						.getDate("I_DATE"), rs.getInt("SIZE"), rs
						.getString("NOM"), rs.getString("CHEMIN"), rs
						.getInt("WIDTH"), rs.getInt("HEIGHT"), rs
						.getInt("NOTE"), rs.getString("DESCRIPTION")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultat;
	}

	public static void closeConnexion(Connection c) {
		try {
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}