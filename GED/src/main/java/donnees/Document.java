package donnees;

import java.util.*;
import javax.swing.ImageIcon;

public class Document extends ImageIcon {

	private static final long serialVersionUID = 1L;
	public List<String> tags;
	public List<Serie> series;
	public String title;
	public String path;
	public String desc; // Facultatif
	public int width;
	public int height;
	public int size;
	public int note; // Facultatif, entre 0 et 5
	public int date; // Facultatif

	/**
	 * Constructeur principal de l'objet Document Initialisation des champs
	 * obligatoires
	 */
	public Document(String image, String title, String chemin, int width, int height, int size) {

		this.tags = new ArrayList<String>();
		this.series = new ArrayList<Serie>();
		this.path = chemin;
		this.title = title;
		this.width = width;
		this.height = height;
		this.size = size;
	}

	/**
	 * Constructeur de l'objet Document en initialisant la note
	 */
	public Document(String image, String title, String chemin, int width, int height, int size, int note) {
		this(image, title, chemin, width, height, size);
		this.setNote(note);
		date = 0;
	}

	/**
	 * Constructeur de l'objet Document en initialisant la description
	 */
	public Document(String image, String title, String chemin, int width, int height, int size, String description) {
		new Document(image, title, chemin, width, height, size);
		this.setDesc(description);
	}

	/**
	 * Constructeur de l'objet Document en initialisant la date
	 */
	public Document(int da, String image, String title, String chemin, int width, int height, int size) {
		new Document(image, title, chemin, width, height, size);
		this.setDate(da);
	}

	/**
	 * Constructeur complet de l'objet Document
	 */
	public Document(String image, String title, String chemin, int width, int height, int size,
			int Da, String description) {
		new Document(image, title, chemin, width, height, size);
		this.setDesc(description);
		this.setNote(note);
		this.setDate(date);
	}

	public void setNote(int N) {
		if ((N >= 0) && (N <= 5))
			this.note = N;
		else
			System.out.println("Donnez à votre image une note entre 0 et 5");
	}

	public void addTag(String T) {
		this.tags.add(T);
	}

	public void addSerie(Serie S) {
		this.series.add(S);
	}

	public void setDesc(String D) {
		this.desc = D;
	}

	public void setDate(int D) {
		this.date = D;
	}

	/*
	 * GETTERS
	 */

	public String getTitle() {
		return this.title;
	}

	public String getPath() {
		return this.path;
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public int getSize() {
		return this.size;
	}

	public int getNote() {
		return this.note;
	}
	
	public int getDate() {
		return this.date;
	}

	public String getTags() {
		Iterator<String> i = this.tags.iterator();
		String s = new String();
		while (i.hasNext()) {
			s += i.next();
			s += "\n";
		}
		return s;
	}

	public String getSeries() {
		Iterator<Serie> i = this.series.iterator();
		String s = new String();
		while (i.hasNext()) {
			s += i.next().getTitle();
			s += "\n";
		}
		return s;
	}
}
