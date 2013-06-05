package donnees;
import java.util.*;

public class Serie {

	public List<Document> docs; 
	public String title;
	
	public Serie(String T) {
		this.docs = new ArrayList<Document>();
		this.title = T;
	}

	public void ajoutDoc(Document D) {
		this.docs.add(D);
	}

	public String getTitle() {
		return this.title;
	}
	
	public String getDocuments() {
		Iterator<Document> i = this.docs.iterator();
		String s = new String();
		while(i.hasNext()) {
				s += i.next().getTitle();
				s += "\n";
		}
			return s;
	}
}
