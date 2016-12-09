
/**
 * @author Osvaldo Gra&ntilde;a
 * @date Dec 7, 2016
 *
 */

import java.io.IOException;
import java.util.ArrayList;

import g4a.CCLE;
public class ppal {

	/**
	 * 
	 * @author Osvaldo Gra&ntilde;a
	 * @date Dec 7, 2016
	 *
	 */
	public ppal() {
		// TODO Auto-generated constructor stub
	}

	/**
	 *
	 * @author Osvaldo Gra&ntilde;a
	 * @date Dec 7, 2016
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CCLE tabla=new CCLE(new String("/home/ograna/Projects/GSEA_pathways_definitions/CCLE/CCLE_Expression_Entrez_2012_09_29.gct"));
		
		try {
			
			ArrayList <String> listOfSamples=tabla.getListOfSamples();			
/*			for(String type:tabla.getListOfSampleTypes()){
				//System.out.println(type);
			
			}*/
			
			tabla.setFirstSampleTypeToIncludeInHeatmap("KIDNEY");
			tabla.setNameforClassesFile("/home/ograna/eclipse_workspace/3rd/classes.cls");			
			tabla.createClassFile(listOfSamples);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
