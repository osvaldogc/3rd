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
	private String [][] resortedCCLEgct;
	private String firstSampleType;
	private String geneSetFile;
	private String classesFileName;
	private ArrayList <String> resortedSampleTypes;
	private int nRowsCCLEgct,nColsCCLEgct;
	
		
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
		this.resortedCCLEgct=null;
		this.nRowsCCLEgct=0;
		this.nColsCCLEgct=0;
		
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
		String columnsToConsiderWhenCreatingTheMatrix="";
		
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
					// so, if the sample was still not included, it is added here
					if(-1==alreadyIncluded.indexOf("="+currentSample+"=")){
						alreadyIncluded+="="+currentSample+"=";
						samples.add(currentSample);
						
						columnsToConsiderWhenCreatingTheMatrix+="-"+i+"-";						
					}
				}				
			}
		}
		
		this.nRowsCCLEgct=lnr.getLineNumber();
		this.nColsCCLEgct=samples.size()+2; // (+2 -> name+description)
		
		
		//reads again the CCLEgct file completely to create the matrix derived from the gct file, but excluding all repeated samples
		this.resortedCCLEgct=new String[this.nRowsCCLEgct][this.nColsCCLEgct];
		for(int row=0;row<this.nRowsCCLEgct;row++)
				for(int column=0;column<this.nColsCCLEgct;column++)
						this.resortedCCLEgct[row][column]="";
		
		
		lnr = new LineNumberReader(new FileReader(this.CCLEgct));		
		for(String line=null; (line=lnr.readLine())!=null;){			
			//splits the current line
			String [] fullLine=line.split("\\t");			
			
			if(lnr.getLineNumber()==1){//adds the first line (with only one column)
				this.resortedCCLEgct[0][0]=fullLine[0];
			}else if(lnr.getLineNumber()==2){//adds the second line (with two columns)
				this.resortedCCLEgct[1][0]=fullLine[0];
				this.resortedCCLEgct[1][1]=fullLine[1];
			}else{//for the rest of the lines
				
				//DEBE AÑADIR LAS MUESTRAS EXCLUYENDO LAS REPETIDAS, es decir:
				//DEBE INCLUIR sÓLO LAS COLUMNAS INDICADAS EN columnsToConsiderWhenCreatingTheMatrix
				
				
				for(int i=0;i<fullLine.length;i++){
					if(columnsToConsiderWhenCreatingTheMatrix.contains("="+i+"=")){
						//At this point lnr.getLineNumber() returns one more position (one more line),
						//because the current one has already been read -> line=lnr.readLine()
						//so we must subtract 1
						this.resortedCCLEgct[lnr.getLineNumber()-1][i]=fullLine[i];
					}
						
				}
				
			}			
		}
		
		for(int row=0;row<this.nRowsCCLEgct;row++)
			System.out.println(this.resortedCCLEgct[row][0]);

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
	 * @date Dec 9, 2016
	 *
	 * @param listOfSamples
	 */
	public void createClassFile(ArrayList <String> listOfSamples){
		if(this.resortedSampleTypes!=null){
		
			BufferedWriter writer=null;
			
			try {
				String firstLine=listOfSamples.size()+" "+this.getNumberOfSampleTypes()+" 1";
				String secondLine=new String("#");
				String thirdLine=new String("");
				
						
				//for each sample type
				for(String sampleType:this.resortedSampleTypes){
					secondLine+=" "+sampleType;
					
					//for each sample of each sample type
					for(String sample:listOfSamples){
						if(sample.matches("(.*)"+sampleType+"$")){
							thirdLine+=sampleType+" ";
						}
					}
				}				
			
				//saves it in a class file (cls)
				writer=new BufferedWriter(new FileWriter(this.getClassesFileName()));
				writer.write(firstLine);
				writer.newLine();
				writer.write(secondLine);
				writer.newLine();
				writer.write(thirdLine);
				
					
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				try {
	                // Close the writer regardless of what happens...
	                writer.close();
	            } catch (Exception e) {
	            }			
			}

			
		}else{
			System.err.println("createClassFile: The sample type that should go in first place has not been set");
			System.exit(1);			
		}
		
	}
	
	/**
	 * Creates a new GCT file derived from the original GCT (CCLE).
	 * This GCT contains first the samples that belong to the sample type chosen to be in first place.
	 * By the way, it excludes lines where there is no gene information in the description column.
	 *
	 * @author Osvaldo Gra&ntilde;a
	 * @date Dec 9, 2016
	 *
	 * @param listOfSamples
	 */
	public void createResortedGCTfile(ArrayList <String> listOfSamples){	
		
		if(this.resortedSampleTypes!=null){
			
			//this.resortedCCLEgct=new String[this.nColsCCLEgct][this.nRowsCCLEgct];
				
		
			

			
		}else{
			System.err.println("createResortedGCTfile: The sample type that should go in first place has not been set");
			System.exit(1);			
		}		
		
	}
	
	
}
