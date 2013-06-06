package donnees;

import java.util.*;
import javax.swing.ImageIcon;

public class Document extends ImageIcon {

	private static final long serialVersionUID = 1L;
	public List<String> tags;
	public List<Serie> series;
	public int id;
	public String title;
	public String path;
	public String desc; // Facultatif
	public int width;
	public int height;
	public int size;
	public int note; // Facultatif, entre 0 et 5
	public Date date; // Facultatif

	/**
	 * Constructeur principal de l'objet Document Initialisation des champs
	 * obligatoires
	 */
	public Document(int id, String title, String chemin, int width, int height,
			int size) {
		super(chemin);
		this.id = id;
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
	public Document(int id, String title, String chemin, int width, int height,
			int size, int note) {
		this(id, title, chemin, width, height, size);
		this.setNote(note);
	}

	/**
	 * Constructeur de l'objet Document en initialisant la description
	 */
	public Document(int id, String title, String chemin, int width, int height,
			int size, String description) {
		this(id, title, chemin, width, height, size);
		this.setDesc(description);
	}

	/**
	 * Constructeur de l'objet Document en initialisant la date
	 */
	public Document(int id, Date date, String title, String chemin, int width,
			int height, int size) {
		this(id, title, chemin, width, height, size);
		this.setDate(date);
	}

	/**
	 * Constructeur complet de l'objet Document (sauf description)
	 */
	public Document(int id, Date date, int size, String title, String chemin,
			int width, int height, int note) {
		this(id, title, chemin, width, height, size);
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

	public void setDate(Date D) {
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

	public Date getDate() {
		return this.date;
	}

	public String getTags() {
		Iterator<String> i = this.tags.iterator();
		String s = new String();
		while (i.hasNext()) {
			s += i.next();
			if (i.hasNext()) {
				s += "\n";
			}
		}
		return s;
	}

	public String getSeries() {
		Iterator<Serie> i = this.series.iterator();
		String s = new String();
		while (i.hasNext()) {
			s += i.next().getTitle();
			if (i.hasNext()) {
				s += "\n";
			}
		}
		return s;
	}

	public int getId() {
		return id;
	}
	
	public String getDesc(){
		return desc;
	}
}
