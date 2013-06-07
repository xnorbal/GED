package donnees;

import java.util.*;
import javax.swing.ImageIcon;

public class Document extends ImageIcon {

	private static final long serialVersionUID = 1L;
	public List<String> tags;
	public List<String> series;
	public int id;
	public String title;
	public String path;
	public String desc; // Facultatif
	public int width;
	public int height;
	public int size;
	public int note; // Facultatif, entre 0 et 5
	public Date date; // Facultatif

	public Document(){
	}
	
	/**
	 * Constructeur principal de l'objet Document Initialisation des champs
	 * obligatoires
	 */
	public Document(int id, String title, String chemin, int width, int height,
			int size) {
		super(chemin);
		this.id = id;
		this.tags = new ArrayList<String>();
		this.series = new ArrayList<String>();
		this.path = chemin;
		this.title = title;
		this.width = width;
		this.height = height;
		this.size = size;
	}

	public Document(int id, Date date, int size, String title, String chemin,
			int width, int height, int note, String desc) {
		this(id, title, chemin, width, height, size);
		this.setNote(note);
		this.setDate(date);
		this.setDesc(desc);
	}

	public void setNote(int note) {
		if ((note >= 0) && (note <= 5))
			this.note = note;
		else
			System.out.println("Donnez à votre image une note entre 0 et 5");
	}

	public void addTag(String t) {
		this.tags.add(t);
	}

	public void addSerie(String s) {
		this.series.add(s);
	}

	public void setDesc(String d) {
		this.desc = d;
	}

	public void setDate(Date d) {
		this.date = d;
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
		Iterator<String> i = this.series.iterator();
		String s = new String();
		while (i.hasNext()) {
			s += i.next();
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
