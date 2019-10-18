package uk.ac.le.CO3098;

import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

//Note: you need to implement either ParserDOM.java OR ParserSAX.java

public class ParserDOM {
	//****************************************
	// CHANGE INPUT BACK TO CONSOLE ARG[0] NOT DIRECTLY FROM XML FILE!!!!!!!!!!!!!
	//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	//****************************************

	/*
	 * Will contain information about the methods that are parsed.
	 * Each important tag of method information will be stored in a different column
	 * for that method's row.
	 */
	static String [][] methodArray = new String [20][20]; 
	static int dataIndex = -1;// the index for the column for particular data in the array 
	static int methodIndex = -1;// the index of the method the parser comes across
	/*
	 * Each integer represents a dataIndex for a particular type of data in the methodArray.
	 */
	static int access = 0, include = 1, abstract_method = 2, parameter = 3, _throws = 4, _return = 5;
	
	
	public void parse(String xml) {
	  try{
	  		DOMParser parser = new DOMParser();
	  		parser.parse(xml);
	  		Document doc = parser.getDocument();
	  		traverse_tree(doc);
	  		printMethods();
	       }
	       catch(Exception e){
	          e.printStackTrace(System.err);
	       }
    }
	
	static int numberOfSpaces=0;
	public static void traverse_tree(Node node){
		numberOfSpaces++;
		if(node == null) {
			return;
		}
		int type = node.getNodeType();
		
		switch (type) {
			case Node.DOCUMENT_NODE: {
				traverse_tree(((Document)node).getDocumentElement());
				break;
			}
			
			case Node.ELEMENT_NODE: {
				handleElement(node);
				break;
				}
			
			case Node.TEXT_NODE: {
				handleText(node);
			}
				
			}
		}
	
	/**
	 * A method which selects an element node and calls  other methods to retrieve that elements information.
	 * The mothods also called another method which traverse child nodes of the element node (see traverseChild)
	 * Only retrieves element information that matches specific nodes:
	 *  - include
	 *  - abstract_method
	 *  - parameter
	 *  - return
	 * @param node
	 */
	private static void handleElement(Node node) {
		dataIndex = -1;
		String elementName = node.getNodeName();
		System.out.println(elementName);
		NamedNodeMap attributes = node.getAttributes();
		if (elementName.equals("access")) {
			dataIndex = access;
		}
		if (elementName.equals("include")) { 
			dataIndex = include;
		}
		if (elementName.equals("abstract_method")) {
			System.out.println("\nhandleElement: methodIndex before = "+ methodIndex + "\n");
			++methodIndex;
			System.out.println("\nhandleElement: methodIndex after = "+ methodIndex + "\n");
			dataIndex =  abstract_method;
			methodArray[methodIndex][dataIndex] = getAttributeValue("id", attributes);
		}
		if(elementName.equals("parameter")) {
			dataIndex = parameter;
			methodArray[methodIndex][dataIndex] +=  getAttributeValue("type", attributes)+" ";
		}
		if(elementName.equals("throws")) {
			dataIndex = _throws;
		}
		if(elementName.equals("return")) {
			dataIndex = _return;
		}
		traverseChildNodes(node);
		

	}
	
	/**
	 * Take data from text nodes and adds it to the methodArray.
	 * @param node
	 */
	private static void handleText(Node node) {
		String chData = node.getNodeValue().trim(); // removes all leading and trailing space
		if (chData.indexOf("\n") < 0 && chData.length() > 0 && dataIndex > -1 && methodIndex > -1) { //there are no line breaks & if it contains something
			if(dataIndex == access) 		  { methodArray[methodIndex][dataIndex] = chData; }
			if (dataIndex == include)         {methodArray[methodIndex][dataIndex] += chData+":";}//access + delimeter
//			if (dataIndex == abstract_method) {/*abstract method tag contains no text*/};
			if (dataIndex == parameter)       {methodArray[methodIndex][dataIndex] += chData+": ";}//parameter + delimeter and space
			if (dataIndex == _throws)         {methodArray[methodIndex] [dataIndex] += chData+":";}//
		}
	}
	
	
	/**
	 * A method which select an attribute out of the list of element attributes.
	 * It then takes returns the attribute's value as a string
	 * @param attrString - the name of the attribute you would like to select and recover information in the current element node
	 * @param attributes - the attributes of the current node
	 * @return String attrValue - value of the selected attribute
	 */
	private static String getAttributeValue(String attrString, NamedNodeMap attributes) {
		Attr attr = (Attr) attributes.getNamedItem(attrString); // get the attribtue from the element 
		String attrValue = attr.getValue(); // gets the value of the selected attribute
		return attrValue;
	}
	
	/**
	 * A method used to traverse the childNodes of a traversed node.
	 * @param node - the node who's childNodes will be traversed
	 */
	private static void traverseChildNodes (Node node) {
		NodeList childNodes = node.getChildNodes();
		if(childNodes != null) {
			int length = childNodes.getLength();
			for (int i= 0; i < length; i++) {
				traverse_tree(childNodes.item(i));
			}
		}
	}

	/**
	 * A method to return the information of methods that are in the xml tree.
	 * @return
	 */
	private static void printMethods () {
		//****************************************
		// CHANGE INPUT BACK TO CONSOLE ARG[0] NOT DIRECTLY FROM XML FILE!!!!!!!!!!!!!
		//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		//****************************************
		System.out.println("\nprintMethods: methodIndex before = "+ methodIndex + "\n");
		for (int mIndex = 0; mIndex < methodIndex; mIndex++) {
			System.out.println("\nprintMethods: methodIndex after = "+ methodIndex + "\n");
			for (int dIndex = 0; dIndex < 20; dIndex++) {
				System.out.print(methodArray[mIndex][dIndex]);
			}
//			System.out.println("\n");
			
		}
	}
	
	//******** Helper Methods to retrieve specific data from each method*********
	
	/**
	 * Returns a string containing the access modifier of the method.
	 * @return String containing access level of method
	 */
	private static String getAccess() {
		return methodArray[methodIndex][access];
	}
	
	
	/**
	 * Returns a String containing the 
	 * @return
	 */
	private static String getInclude() {
		return methodArray[methodIndex][include];
	}
}
