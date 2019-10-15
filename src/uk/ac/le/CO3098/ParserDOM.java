package uk.ac.le.CO3098;

import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

//Note: you need to implement either ParserDOM.java OR ParserSAX.java

public class ParserDOM {

	public void parse(String xml) {
	  try{
	  		DOMParser parser = new DOMParser();
	  		parser.parse(xml);
	  		Document doc = parser.getDocument();
	  		traverse_tree(doc);
	       }
	       catch(Exception e){
	          e.printStackTrace(System.err);
	       }
    }

	public static void traverse_tree(Node node){
		//Complete task 3
	}

}
