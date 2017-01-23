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
	private String [][] CCLEgctMatrix;
	private String [][] resortedCCLEgctMatrix;
	private String resortedCCLEgctMatrix_outputFileName="";
	private String firstSampleType;
	private String geneSetFile;
	private String classesFileName;
	private ArrayList <String> resortedSampleTypes;
	private int nRowsCCLEgct,nColsCCLEgct;
	private String GSEAouptutLabel;
	
		
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
		this.CCLEgctMatrix=null;
		this.resortedCCLEgctMatrix=null;
		this.nRowsCCLEgct=0;
		this.nColsCCLEgct=0;
		this.resortedCCLEgctMatrix_outputFileName=null;
		this.GSEAouptutLabel="CCLE_GSEA";

		
		
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
	 * @return A list with all the sample names. <br>Important: If one sample appears more than once, it simply considers data from the first occurrence in the CCLE file of that sample.<br>Additionally, it creates a new matrix object, in gct format, with the original CCLE table but excluding columns with repeated
	 * samples, and also excluding lines where there is no gene name in the description field.
	 * @throws IOException
	 */
	public ArrayList <String> getListOfSamples() throws IOException{
		
		ArrayList <String> samples=new ArrayList<String>();
		
		LineNumberReader lnr = new LineNumberReader(new FileReader(this.CCLEgct));
		//Forces the inclusion of columns 0 and 1 always, as they contain
		//the Name and Description columns
		String columnsToConsiderWhenCreatingTheMatrix="=0==1="; //adds columns
		//counts the two first lines already (gct headers)
		this.nRowsCCLEgct=2;
		
		//locates the third line (with sample headers)
		for(String line=null; (line=lnr.readLine())!=null;){
			
			String [] tokens=line.split("\\t");
			
			if(lnr.getLineNumber()==3){		
				
				String alreadyIncluded="";
				
				//avoids 'name' (position 0) and 'description' (position 1) fields
				for(int i=2;i<tokens.length;i++){
					String currentSample=tokens[i];
					
					// controls sample repetitions, like NCIH292_LUNG
					// in this case, it simply considers data from the first occurrence of that sample
					// so, if the sample was still not included, it is added here
					if(-1==alreadyIncluded.indexOf("="+currentSample+"=")){
						alreadyIncluded+="="+currentSample+"=";
						samples.add(currentSample);
						
						columnsToConsiderWhenCreatingTheMatrix+="="+i+"=";
						
						//System.out.println(currentSample);
					}
				}				
			}
			
			//it does not check the two first lines (they are already counted)
			//it does not consider/count lines where there is no gene name in the 'Description' field
			if(lnr.getLineNumber()>=3 && !tokens[1].equals("")) this.nRowsCCLEgct++;
		}
		
		this.nColsCCLEgct=samples.size()+2; // (+2 -> name+description)		
		
		//reads again the CCLEgct file completely to create the matrix derived from the gct file,
		// excluding columns with repeated samples and lines with no gene name in the description field
		this.CCLEgctMatrix=new String[this.nRowsCCLEgct][this.nColsCCLEgct];
		for(int row=0;row<this.nRowsCCLEgct;row++)
				for(int column=0;column<this.nColsCCLEgct;column++)
						this.CCLEgctMatrix[row][column]="";
		
		
		lnr = new LineNumberReader(new FileReader(this.CCLEgct));
		int currentRow=0;
		for(String line=null; (line=lnr.readLine())!=null;){			
			//splits the current line
			String [] fullLine=line.split("\\t");			
		
			if(lnr.getLineNumber()==1){//adds the first line (with only one column)
				this.CCLEgctMatrix[0][0]=fullLine[0];
				currentRow++;
			}else if(lnr.getLineNumber()==2){//adds the second line (with two columns)
				this.CCLEgctMatrix[1][0]=(new Integer(this.nRowsCCLEgct-3)).toString();
				this.CCLEgctMatrix[1][1]=(new Integer(samples.size())).toString();
				currentRow++;
			}else{//for the rest of the lines
				
				//in case that there is gene information (in the 'Description' column )
				if(!fullLine[1].equals("")){
					
					//requires a specific counter because there might be less columns than in the original matrix,
					//due to sample repetitions (like NCIH292_LUNG) that are excluded in the resortedCCLEgct
					int currentColumn=0;
					
					
					for(int i=0;i<fullLine.length;i++){
						if(columnsToConsiderWhenCreatingTheMatrix.contains("="+i+"=")){						
							this.CCLEgctMatrix[currentRow][currentColumn]=fullLine[i];
							currentColumn++;
						}											
					}
					
					currentRow++;
				}
			}			
		}
		
		/*for(int row=0;row<this.nRowsCCLEgct;row++){
			for(int column=0; column<this.nColsCCLEgct; column++)
				System.out.print(this.CCLEgctMatrix[row][column]+"\t");
			System.out.println();
		}*/
		
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
	 * Sets the name (path+name) of the output file that contains the resortedCCLEgctMatrix
	 *
	 * @author Osvaldo Gra&ntilde;a
	 * @date Jan 23, 2017
	 *
	 * @param name
	 */
	public void set_resortedCCLEgctMatrix_outputFileName(String name){
		this.resortedCCLEgctMatrix_outputFileName=name;
	}
	
	/**
	 * Gets the name (path+name) of the output file that contains the resortedCCLEgctMatrix
	 *
	 * @author Osvaldo Gra&ntilde;a
	 * @date Jan 23, 2017
	 *
	 * @return
	 */
	public String get_resortedCCLEgctMatrix_outputFileName(){
		return this.resortedCCLEgctMatrix_outputFileName;
	}
	
	/**
	 * Creates a new GCT file derived from the original GCT (CCLE).
	 * This GCT contains first the samples that belong to the sample type chosen to be in first place.
	 *
	 * @author Osvaldo Gra&ntilde;a
	 * @date Dec 9, 2016
	 *
	 * @param listOfSamples
	 */
	public void createResortedGCTfile(ArrayList <String> listOfSamples){	
		
		if(this.resortedSampleTypes!=null){
			this.resortedCCLEgctMatrix=new String[this.nRowsCCLEgct][this.nColsCCLEgct];
			//initialization
			for(int row=0;row<this.nRowsCCLEgct;row++)
					for(int column=0;column<this.nColsCCLEgct;column++)
							this.resortedCCLEgctMatrix[row][column]="";
			
			//first copies the common columns: columns 1 and 2 from the start to the end of the file
			for(int row=0;row<this.nRowsCCLEgct;row++){
				this.resortedCCLEgctMatrix[row][0]=this.CCLEgctMatrix[row][0];
				this.resortedCCLEgctMatrix[row][1]=this.CCLEgctMatrix[row][1];
			}
			
			//now resorts the samples and copies them in the proper order to resortedCCLEgctMatrix
			int columnCounterforResortedMatrix=2;
			//foreach sample type
			for(String type:this.resortedSampleTypes){
				
				//goes through all columns, starting in the third column
				for(int column=2;column<this.CCLEgctMatrix[2].length;column++){
					String thisSampleType=(this.CCLEgctMatrix[2][column].split("_",2))[1];					
					
					//if 'thisSampleType' is the same as the one pointed out by 'type', then this column is added
					//in the resorted matrix
					if(thisSampleType.equals(type)){
						//adds all rows of that column
						for(int row=2; row<this.CCLEgctMatrix.length; row++)
							this.resortedCCLEgctMatrix[row][columnCounterforResortedMatrix]=this.CCLEgctMatrix[row][column];						
						
						//increases one column in resortedCCLEgctMatrix
						columnCounterforResortedMatrix++;					
					}
					
				}				
			}
						
			//writes the resortedCCLEgctMatrix to a file for GSEA
			BufferedWriter writer=null;			
			
			try {
				writer=new BufferedWriter(new FileWriter(this.get_resortedCCLEgctMatrix_outputFileName()));
				
				//writes all the lines to a file
				for(int row=0;row<this.nRowsCCLEgct;row++){
					for(int column=0; column<this.nColsCCLEgct; column++)
						writer.write(this.resortedCCLEgctMatrix[row][column]+"\t");
					writer.newLine();
				}			
			
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				//System.err.println(e.getMessage());
			} catch (java.lang.NullPointerException e){
				System.err.println("ResortedCCLEgctMatrix output file name = null");
				e.printStackTrace();
			} finally{
				try {
	                // Close the writer regardless of what happens...
	                writer.close();
	            } catch (Exception e) {
	            }			
			}
			
			
		}else{
			System.err.println("createResortedGCTfile: The sample type that should go in first place has not been set");
			System.exit(1);			
		}		
		
	}
		
	/*
	 * Executes GSEA on the CCLE gct file (resortedCCLEgctMatrix) using the specified gene set file.
	 * 
	 * @author Osvaldo Gra&ntilde;a
	 * @date Jan 16, 2017
	 * 
	 */
	public void executeGSEA(){
		System.out.println("[executing GSEA]");
		String command="java -cp .:/home/ograna/SOFTWARE/GSEA/gsea2-2.2.2.jar -Xmx4G xtools.gsea.Gsea ";
		command+="-res "+this.resortedCCLEgctMatrix_outputFileName+" ";
		command+="-cls "+this.classesFileName+"#"+this.firstSampleType+"_versus_REST ";
		command+="-gmx "+this.geneSetFile+" ";
		command+="-collapse true -mode Max_probe -norm meandiv -nperm 1000 -permute phenotype -rnd_type no_balance -scoring_scheme weighted ";
		command+="-rpt_label "+GSEAouptutLabel+" ";
		command+="-metric Signal2Noise -sort real -order descending -chip gseaftp.broadinstitute.org://pub/gsea/annotations/AFFYMETRIX.chip -include_only_symbols true -make_sets true -median false -num 100 -plot_top_x 20 -rnd_seed 123 -save_rnd_lists false -set_max 1000 -set_min 15 -zip_report false ";
		command+="-out GSEA ";
		command+="-gui false ";
		
		try {
			Process p=Runtime.getRuntime().exec(command.split(" "));
			p.waitFor();
			System.out.println("Process exit value:"+p.exitValue());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
}
