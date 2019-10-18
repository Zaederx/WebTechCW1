package uk.ac.le.CO3098;


public class XMLParserLauncher {

	//Usage: java XMLParserLauncher xmlfile
	//e.g.   java XMLParserLauncher RESTController.xml
	 public static void main(String[] args){

		 try {

		//Note: you need to implement either XMLParserDOM.java OR XMLParserDOM.java
		//please uncomment accordingly to test.

		 ParserDOM parser=new ParserDOM();
		// or XMLParserSAX parser = new XMLParserSAX();
//		 if(args.length == 0) {
//			 throw new IllegalArgumentException("Usage: java XMLParserLauncher xmlfile");
//		 }
//		 parser.parse(args[0]);
		 parser.parse("IAuthService.xml");
		 }catch(Exception ex) {
			 ex.printStackTrace();
		 }

	 }

}
