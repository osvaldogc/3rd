/**
 * @author ograna
 * @date Nov 14, 2016
 *
 */
package g4a;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;

/**
 * @author ograna
 * @date Nov 14, 2016
 *
 */
public class CCLE {
	
	private String CCLEgct;
	private String firstSampleType;
	private String geneSetFile;
	private String classesFileName;

	/**
	 * CLEgct: name of the original CCLEgct file
	 * @param CCLEgct
	 */
	public CCLE(String CCLEgct) {
		super();
		
		// TODO Auto-generated constructor stub
		this.CCLEgct = CCLEgct;
		this.firstSampleType=null;
		this.geneSetFile=null;
		this.classesFileName=null;
	}

	/**
	 * Returns the file name of the original CCLE gct file
	 *
	 * @author ograna
	 * @date Dec 5, 2016
	 *
	 * @return
	 */
	public String getCCLEgct() {
		return this.CCLEgct;
	}

	/**
	 * CLEgct: name of the original CCLEgct file
	 *
	 * @author ograna
	 * @date Dec 5, 2016
	 *
	 * @param CCLEgct
	 */
	public void setCCLEgct(String CCLEgct) {
		this.CCLEgct = CCLEgct;
	}
	
	/**
	 * Gets the list of sample names
	 *
	 * @author ograna
	 * @date Dec 1, 2016
	 *
	 * @return A list with all the sample names
	 * @throws IOException
	 */
	public ArrayList <String> getListOfSamples() throws IOException{
		
		ArrayList <String> samples=new ArrayList<String>();
		
		LineNumberReader lnr = new LineNumberReader(new FileReader(this.CCLEgct));
		
		//locates the third line (with sample headers)
		for(String line=null; (line=lnr.readLine())!=null;){
			if(lnr.getLineNumber()==3){
				String [] sampleList=line.split("\\t");
				String alreadyIncluded="";
				
				//avoids name (position 0) and description (position 1) fields
				for(int i=2;i<sampleList.length;i++){
					String currentSample=sampleList[i];
					
					// controls sample repetitions, like NCIH292_LUNG
					// in this case, it simply considers data from the first appearance
					if(-1==alreadyIncluded.indexOf("="+currentSample+"=")){
						alreadyIncluded+="="+currentSample+"=";
						samples.add(currentSample);
						System.out.println(currentSample);
					}
				}
				
				break;
			}
		}
		
		return(samples);
	}
	
	/**
	 * Gets the total number of samples
	 *
	 * @author ograna
	 * @date Dec 5, 2016
	 *
	 * @return The number of samples
	 * @throws IOException
	 */
	public int getNumberOfSamples() throws IOException{
		
		ArrayList <String> samples=new ArrayList<String>();
		
		LineNumberReader lnr = new LineNumberReader(new FileReader(this.CCLEgct));
		
		//locates the third line (with sample headers)
		for(String line=null; (line=lnr.readLine())!=null;){
			if(lnr.getLineNumber()==3){
				String [] sampleList=line.split("\\t");
				String alreadyIncluded="";
				
				//avoids name (position 0) and description (position 1) fields
				for(int i=2;i<sampleList.length;i++){
					String currentSample=sampleList[i];
					
					// controls sample repetitions, like NCIH292_LUNG
					// in this case, it simply considers data from the first appearance
					if(-1==alreadyIncluded.indexOf("="+currentSample+"=")){
						alreadyIncluded+="="+currentSample+"=";
						samples.add(currentSample);
						System.out.println(currentSample);
					}
				}
				
				break;
			}
		}
		
		return(samples.size());
	}
	
	/**
	 * Returns the list of sample types
	 *
	 * @author ograna
	 * @date Dec 1, 2016
	 *
	 * @return A list with all the sample types
	 * @throws IOException
	 */
	public ArrayList <String> getListOfSampleTypes() throws IOException{
		
		ArrayList <String> sampleTypes=new ArrayList<String>();
		
		
		LineNumberReader lnr = new LineNumberReader(new FileReader(this.CCLEgct));
		
		for(String line=null; (line=lnr.readLine())!=null;){
			if(lnr.getLineNumber()==3){
				String [] sampleList=line.split("\\t");
				String alreadyIncluded="";
				
				//avoids name (position 0) and description (position 1) columns required in a GCT file
				for(int i=2; i<sampleList.length;i++){					
					String type=(sampleList[i].split("_",2))[1];					
					
					//controls if type the current type was already included (to avoid repetitions)
					if(-1==alreadyIncluded.indexOf("="+type+"=")){
						alreadyIncluded+="="+type+"=";
						sampleTypes.add(type);
					}
				}
				
				break;
			}
		}
		
		return(sampleTypes);
	}

	/**
	 * Returns the number of sample types
	 *
	 * @author ograna
	 * @date Dec 5, 2016
	 *
	 * @return The number of sample types
	 * @throws IOException
	 */
	public int getNumberOfSampleTypes() throws IOException{

		ArrayList <String> sampleTypes=new ArrayList<String>();
		
		
		LineNumberReader lnr = new LineNumberReader(new FileReader(this.CCLEgct));
		
		for(String line=null; (line=lnr.readLine())!=null;){
			if(lnr.getLineNumber()==3){
				String [] sampleList=line.split("\\t");
				String alreadyIncluded="";
				
				//avoids name (position 0) and description (position 1) columns required in a GCT file
				for(int i=2; i<sampleList.length;i++){					
					String type=(sampleList[i].split("_",2))[1];					
					
					//controls if type the current type was already included (to avoid repetitions)
					if(-1==alreadyIncluded.indexOf("="+type+"=")){
						alreadyIncluded+="="+type+"=";
						sampleTypes.add(type);
					}
				}
				
				break;
			}
		}
		
		return(sampleTypes.size());
	}
	
	/**
	 * firstSampleType: sample type to go in first place in the heatmap
	 *
	 * @author ograna
	 * @date Dec 5, 2016
	 *
	 * @param firstSampleType
	 */	
	public void setFirstSampleTypeToIncludeInHeatmap (String firstSampleType){
		this.firstSampleType=firstSampleType;
	}
	
	/**
	 * geneSetFile: name of the gmt or gmx file that contains one gene set to be plotted in the heatmap
	 *
	 * @author ograna
	 * @date Dec 5, 2016
	 *
	 * @param geneSetFile
	 */
	public void set_gmtORgmxFileWithGenesetToPlot(String geneSetFile){		
		this.geneSetFile=geneSetFile;		
	}
	
	/**
	 * classesFileName: name for the classes file that will be created
	 *
	 * @author ograna
	 * @date Dec 5, 2016
	 *
	 * @param classesFileName
	 */
	public void setNameforClassesFile(String classesFileName){
		if(!classesFileName.endsWith("cls")) classesFileName+=".cls";
		this.classesFileName=classesFileName;
	
	}
	
}
