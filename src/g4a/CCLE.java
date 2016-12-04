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

	/**
	 * 
	 * @param CCLEgct the original gct file from CCLE
	 */
	public CCLE(String CCLEgct) {
		super();
		// TODO Auto-generated constructor stub
		this.CCLEgct = CCLEgct;
	}

	/**
	 * @return the original gct file from CCLE
	 */
	public String getCCLEgct() {
		return this.CCLEgct;
	}

	/**
	 * @param CCLEgct the original gct file from CCLE
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
	public String[] getListOfSamples() throws IOException{
		
		String [] sampleList=null;
		
		LineNumberReader lnr = new LineNumberReader(new FileReader(this.CCLEgct));
		
		for(String line=null; (line=lnr.readLine())!=null;){
			if(lnr.getLineNumber()==3){
				sampleList=line.split("\\t");
				break;				
			}
		}
		
		return(sampleList);

	}
	
	/**
	 * Gets the list of sample types
	 *
	 * @author ograna
	 * @date Dec 1, 2016
	 *
	 * @return A list with all the sample types
	 * @throws IOException
	 */
	public ArrayList <String> getListOfSampleTypes() throws IOException{
		
		ArrayList <String> sampleTypes=new ArrayList<String>();
		String alreadyIncluded="";
		
		LineNumberReader lnr = new LineNumberReader(new FileReader(this.CCLEgct));
		
		for(String line=null; (line=lnr.readLine())!=null;){
			if(lnr.getLineNumber()==3){
				String [] sampleList=line.split("\\t");
				
				for(int i=0; i<sampleList.length;i++){
					String type=(sampleList[i].split("_",2))[0];
					System.out.println(sampleList[i]+"\t"+type);
					if(!alreadyIncluded.contains("="+type+"=")){
						alreadyIncluded+="="+type+"=";
						sampleTypes.add(type);
					}
				}	
				
				break;				
			}
		}
		
		return(sampleTypes);

	}
	
}
