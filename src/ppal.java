
/**
 * @author ograna
 * @date Dec 1, 2016
 *
 */

import java.io.IOException;

import g4a.CCLE;
public class ppal {

	/**
	 * 
	 */
	public ppal() {
		// TODO Auto-generated constructor stub
	}

	/**
	 *
	 * @author ograna
	 * @date Dec 1, 2016
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CCLE tabla=new CCLE(new String("/home/ograna/Projects/GSEA_pathways_definitions/CCLE/CCLE_Expression_Entrez_2012_09_29.gct"));
		
		try {
			
			tabla.getListOfSamples();
			
			
			//System.out.println("\n\n\n");
			
			for(String type:tabla.getListOfSampleTypes()){
				//System.out.println(type);
			
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
