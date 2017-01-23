
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
			/*for(String type:listOfSamples){
				System.out.println(type);
			
			}*/
			
			//The following steps are required, following the order below
			//1.-
			tabla.setFirstSampleTypeToIncludeInHeatmap("KIDNEY");
			//2.-
			tabla.setNameforClassesFile("/home/ograna/eclipse_workspace/classes.cls");
			//3.-
			tabla.createClassFile(listOfSamples);
			//4.-
			tabla.set_resortedCCLEgctMatrix_outputFileName("resorted.gct");
			//5.-
			tabla.createResortedGCTfile(listOfSamples);
			//6.-
			tabla.set_gmtORgmxFileWithGenesetToPlot("/home/ograna/Projects/GSEA_pathways_definitions/h.all.v5.2.symbols.gmt");
			//7.-
			tabla.executeGSEA();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
