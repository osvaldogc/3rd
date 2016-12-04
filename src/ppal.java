
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
		System.out.println("empezando");
		CCLE tabla=new CCLE(new String("/home/ograna/Projects/GSEA_pathways_definitions/CCLE/CCLE_Expression_Entrez_2012_09_29.gct"));
		
		try {
			String [] samples=tabla.getListOfSamples();
			
		/*	for(int i=0; i<samples.length;i++){
				System.out.println(samples[i]);
			}*/
			
			System.out.println("\n\n\n");
			
			for(String type:tabla.getListOfSampleTypes()){
				//System.out.println(type);
			
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
