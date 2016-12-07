/**
 * @author Osvaldo Gra&ntilde;a
 * @date Dec 7, 2016
 *
 */
package g4a;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;

/**
 * @author Osvaldo Gra&ntilde;a
 * @date Dec 7, 2016
 *
 */
public class CCLE {
	
	private String CCLEgct;
	private String firstSampleType;
	private String geneSetFile;
	private String classesFileName;
	private ArrayList <String> resortedSampleTypes;

		
	/**
	 * 
	 * @author Osvaldo Gra&ntilde;a
	 * @date Dec 7, 2016
	 *
	 * @param CCLEgct name of the original CCLEgct file
	 */
	public CCLE(String CCLEgct) {
		super();
		
		// TODO Auto-generated constructor stub
		this.CCLEgct = CCLEgct;
		this.firstSampleType=null;
		this.geneSetFile=null;
		this.classesFileName=null;
		this.resortedSampleTypes=null;
	}

	/** 
	 *
	 * @author Osvaldo Gra&ntilde;a
	 * @date Dec 7, 2016
	 *
	 * @return The file name of the original CCLE gct file
	 */
	public String getCCLEgct() {
		return this.CCLEgct;
	}

	/**
	 * @return the classesFileName
	 */
	public String getClassesFileName() {
		return classesFileName;
	}

	/**
	 * @param classesFileName the classesFileName to set
	 */
	public void setClassesFileName(String classesFileName) {
		if(!classesFileName.endsWith("cls")) classesFileName+=".cls";
		this.classesFileName = classesFileName;		
	}
	
	/**
	 * 
	 *
	 * @author Osvaldo Gra&ntilde;a
	 * @date Dec 7, 2016
	 *
	 * @param CCLEgct name of the original CCLEgct file
	 */
	public void setCCLEgct(String CCLEgct) {
		this.CCLEgct = CCLEgct;
	}
	
	/**
	 * 
	 *
	 * @author Osvaldo Gra&ntilde;a
	 * @date Dec 7, 2016
	 *
	 * @return A list with all the sample names<br>Important: If one sample appears more than once, it simply considers data from the first occurrence of that sample
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
					// in this case, it simply considers data from the first occurrence of that sample
					if(-1==alreadyIncluded.indexOf("="+currentSample+"=")){
						alreadyIncluded+="="+currentSample+"=";
						samples.add(currentSample);
						
						//System.out.println(currentSample);
					}
				}
				
				break;
			}
		}
		
		return(samples);
	}
	
	/**
	 * 
	 *
	 * @author Osvaldo Gra&ntilde;a
	 * @date Dec 7, 2016
	 *
	 * @return The number of samples
	 * @throws IOException
	 */
	public int getNumberOfSamples() throws IOException{		
		return(this.getListOfSamples().size());
	}
	
	/**
	 *
	 * @author Osvaldo Gra&ntilde;a
	 * @date Dec 7, 2016
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
	 * 
	 *
	 * @author Osvaldo Gra&ntilde;a
	 * @date Dec 7, 2016
	 *
	 * @return The number of sample types
	 * @throws IOException
	 */
	public int getNumberOfSampleTypes() throws IOException{		
		return(this.getListOfSampleTypes().size());
	}
	
	/**
	 * Creates a new list of resorted sample types with firstSampleType in first place
	 *
	 * @author Osvaldo Gra&ntilde;a
	 * @date Dec 7, 2016
	 *
	 * @param firstSampleType sample type to go in first place in the heatmap
	 * 
	 */	
	public void setFirstSampleTypeToIncludeInHeatmap (String firstSampleType){
		try {
			ArrayList <String> aux=this.getListOfSampleTypes();
			
			//checks if firstSampleType is a valid sample type
			boolean isValid=false;
			for(String sampleType:aux){
				if(sampleType.equals(firstSampleType)){
						isValid=true;
						this.firstSampleType=firstSampleType;
						break;
				}
			}
			
			if(!isValid){
				System.err.println("The sample type '"+firstSampleType+"' set as firstSampleType is not a valid sample type");
				System.exit(1);				
			}else{
				//resorts the sample types, putting firstSampleType in first place
				this.resortedSampleTypes=new ArrayList <String>();				
				this.resortedSampleTypes.add(this.firstSampleType);
				//adds the rest of sample types
				for(String sampleType:aux){
					//if it is not firstSampleType
					if(!sampleType.equals(firstSampleType)){
						this.resortedSampleTypes.add(sampleType);						
					}
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	/**
	 *
	 * @author Osvaldo Gra&ntilde;a
	 * @date Dec 7, 2016
	 *
	 * @param geneSetFile name of the gmt or gmx file that contains one gene set to be plotted in the heatmap
	 */
	public void set_gmtORgmxFileWithGenesetToPlot(String geneSetFile){		
		this.geneSetFile=geneSetFile;		
	}
	
	/**
	 *
	 * @author Osvaldo Gra&ntilde;a
	 * @date Dec 7, 2016
	 *
	 * @param classesFileName name for the classes file that will be created
	 */
	public void setNameforClassesFile(String classesFileName){
		if(!classesFileName.endsWith("cls")) classesFileName+=".cls";
		this.classesFileName=classesFileName;	
	}
	
	/**
	 * Creates the required class file (cls)
	 *
	 * @author Osvaldo Gra&ntilde;a
	 * @date Dec 7, 2016
	 *
	 */
	public void createClassFile(){
		if(this.resortedSampleTypes!=null){
		
			try {
				String firstLine=this.getNumberOfSamples()+" "+this.getNumberOfSampleTypes()+" 1";
				String secondLine=new String("#");
				String thirdLine=new String("");
				
				//for each sample type
				for(String sampleType:this.getListOfSampleTypes()){
					secondLine+=" "+sampleType;
					
					//for each sample of each sample type
					for(String sample:this.getListOfSamples()){
						if(sample.matches("(.*)"+sampleType+"$")){
							thirdLine+=sampleType+" ";
						}
					}
				}
				
				
				//BufferedWritter writer=new BufferedWriter(new FileWriter(this.getClassesFileName()));
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	

			
		}else{
			System.err.println("The sample type that should go in first place has not been set");
			System.exit(1);			
		}
		
	}
	
	
}
