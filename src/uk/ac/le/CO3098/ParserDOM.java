package uk.ac.le.CO3098;

import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

//Note: you need to implement either ParserDOM.java OR ParserSAX.java

/**
 * Class with implements the DOM Parser.
 * The class parses a provided XML that follows a specific
 * interface schema (IAuthService.xsd) and prints out any defined classes.
 * @author zacharyishmael
 *
 */
public class ParserDOM {

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
	static int access = 0, _return = 1,  abstract_method = 2, parameter = 3, _throws = 4;
	
	/**
	 * A method used to parse the XML.
	 * Calls the traverse_tree.
	 * @param xml
	 */
	public void parse(String xml) {
		initialiseArray(methodArray);
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
	
	/**
	 * A method used to traverse all tree nodes.
	 * @param node - a node of the XML document
	 */
	public static void traverse_tree(Node node){
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
	 * The mothods also called another method which traverse child nodes of the element node (see traverseChild).
	 * Only retrieves element information that matches specific nodes:
	 *  - access
	 *  - abstract_method
	 *  - parameter
	 *  - return
	 * @param node
	 */
	private static void handleElement(Node node) {
		dataIndex = -1;
		String elementName = node.getNodeName();
		NamedNodeMap attributes = node.getAttributes();
		if (elementName.equals("access")) {
			dataIndex = access;
		}
		if (elementName.equals("abstract_method")) {
			++methodIndex;
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
		if (chData.length() > 0 && dataIndex > -1 && methodIndex > -1) { //there are no line breaks & if it contains something
			if(dataIndex == access) 		  { methodArray[methodIndex][dataIndex] = chData; }
//			if (dataIndex == abstract_method) {/*abstract method tag contains no text*/};
			if (dataIndex == parameter)       {methodArray[methodIndex][dataIndex] += chData+", ";}//parameter + delimeter and space
			if (dataIndex == _throws)         {methodArray[methodIndex] [dataIndex] += chData+", ";}//
			if (dataIndex == _return)		  {methodArray[methodIndex][dataIndex] =  chData;
			}
		}
	}
	
	
	/**
	 * A method which select an attribute out of the list of element attributes.
	 * It then takes returns the attribute's value as a string.
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
	 * @param node - the node who's childNodes will be traversed.
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
	 * A method to print contents of the methodArray.
	 * Prints access modifier, return type, parameters and 
	 * exceptions of methods inside the array.
	 */
	private static void printMethods () {
		for (int mIndex = 0; mIndex <= methodIndex; mIndex++) {
			for (int dIndex = 0; dIndex < 20; dIndex++) {
				if(dIndex!= 0 && dIndex != _throws) {
					System.out.print(" ");// put a space between each methodArray section
				}
				if(dIndex == parameter) { //brackets for parameters
					System.out.print("("); 
					String s = methodArray[mIndex][dIndex];
					methodArray[mIndex][dIndex] = s.substring(0, methodArray[mIndex][dIndex].length()-2);
					//to remove the ", " from the end of the parameter string
				}
				if(dIndex == _throws) {
					System.out.print(")");//closing bracket for parameters
					if(!methodArray[mIndex][dIndex].isEmpty()) {
						System.out.print("\n     throws ");
						String s = methodArray[mIndex][dIndex];
						methodArray[mIndex][dIndex] = s.substring(0, methodArray[mIndex][dIndex].length()-2);
						System.out.print(methodArray[mIndex][dIndex]);
						System.out.print(";");
					} else {
						System.out.print(";");
					}
				} 	
				if (dIndex != _throws)
				System.out.print(methodArray[mIndex][dIndex]);
				}
			System.out.println("\n");
		}
	}
	
	/**
	 * A method to initalise the methodArray with empty strings.
	 * Needed to prevent null input being stored with with array output.
	 * 
	 * @param arr - the array to be initialised
	 */
	private static void initialiseArray(String[][] arr) {
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr.length; j++) {
				arr[i][j] = "";
			}
		}
	}
	
}
