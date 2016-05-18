/**
* GrepPatern.java
*
* @version   $Id: GrepPatern.java,v_1.4 2014/12/01 17:20:00
*
* @author    hhk9433 (Hrishikesh Karale)
*
* Revisions:
*      Initial revision
*/

import java.io.*;

/**
 * This program recursively iterates through the html file and creates a text
 * file from the given html file and then greps a pattern from the text file.
 */
public class GrepPatern 
{
	//keeps track of By Division phase.
	private static int bydivision= 0;
	//keeps track of endfile phase.
	private static int endfile= 0;
	//creates a new file of the given parameter name
	private File file_object_1= new File("ConvertedTextFile.txt");
	//creates a new file of the given parameter name
	private File file_object_2= new File("PatternTextFile.txt");
	//an instance of filewriter is created
	private FileWriter filewriter_object;
	//an instance of bufferedwriter is created
	private BufferedWriter bufferedwriter_object1 ;
	//an instance of bufferedwriter is created
	private BufferedWriter bufferedwriter_object2 ;
	//used to detect a newline without tags from html file
	private boolean showline= false;
	//used to detect presence of a string of interest
	private static boolean str_found= false;
	//counter for initializing buffered writer
	private static int count_1=0;
	//counter for initializing buffered writer
	private static int count_2=0;
	
	/**
	 * This method reads the html file line by line and then seperates  the html
	 * tags from the rest of the text from the line and sends it to convertToText()
	 * for further processing.
	 * 
	 * @param bufferedreader_object
	 */
	private void readFromhtml(BufferedReader bufferedreader_object)
	{
		//keeps track of closing tag
		int start_position= 0;
		//keeps track of opening tag
		int end_position= 0;
		//used to store new line being read from the html file
		String line= "";
		//used to store concenated text after eliminating html tags
		String str= "";
		//keeps track of start and end of html tags
		boolean tag = false;
		//keeps track if a string has been sent to convertToText()
		boolean found= false;
		
		try 
		{
			//stores result returned from readline() on bufferedreader
			line= bufferedreader_object.readLine();
		} 
		catch (IOException e) 
		{
			System.err.println("File is Empty");
		}
		
		while(line!= "")
		{
			//reinitializes end of tag to zero
			start_position= 0;
			//reinitializes start of tag to zero
			end_position= 0;
			//reinitializes string to empty
			str= "";
			//reinitializes string found to false
			found= false;
			
			/*
			 * this block of code scans through the line fetched from html file
			 * and looks for html tags. If tag found then it calculates the 
			 * remaining string and stores it in str. 
			 * 
			 */
			for(int i=0; i<line.length(); i++)
			{
				//checks for a starting html tag
				if(line.charAt(i)== '<')
				{
					found= true;
					tag= true;
					end_position= i;
				}
				//checks for ending html tag
				else if(tag && line.charAt(i)== '>' )
				{
					tag= false;
					start_position= i+1;
				}
				//checks for end of line
				else if(i==line.length()-1 && !tag)
				{
					end_position= i+1;
					found=true;
					//sets showline variable true if no tags are found in the line
					if(start_position==0)
						showline=true;
				}
				
				//checks if string is ready to be concanated
				if(found && (tag || showline) && start_position < end_position)
				{
					found= false;
					showline= false;
					str+= line.substring( start_position, end_position);
				}
			}
			
			//sends the string to convertToText() as parameter.
			this.convertToText(str);
			try 
			{
				//new line is read and stored in line
				line= bufferedreader_object.readLine();
				if(line==null)
					break;
			} 
			catch (IOException e) 
			{
				System.err.println("No New line");	
			}
		}
		
		try 
		{
			//PatternTextFile file is closed
			bufferedwriter_object2.close();
		} 
		catch (IOException e) 
		{
		System.err.println("Problem Closing pattern text file");
		}
		
		try 
		{
			//ConvertedTextFile file is closed
			bufferedwriter_object1.close();
		} 
		catch (IOException e) 
		{
		System.err.println("Problem Closing text file");
		}
		
	}
	
	/**
	 * This method takes in a string as a parameter from readFromhtml() and
	 * checks for the begining and end of a patters using phases. and sends
	 * the text in between phases to gre()
	 * 
	 * @param line
	 */
	private void convertToText(String line) 
	{
		// if file doesn't exists, then create it
		if (!file_object_1.exists()) 
		{
			try
			{
				file_object_1.createNewFile();
			} 
			catch (IOException e) 
			{
				System.err.println("Unable to create text File");;
			}
		}
 
		//absolute address of the above file is given to filewriter as parameter
		try 
		{
			filewriter_object = new FileWriter(file_object_1.getAbsoluteFile());
		} 
		catch (IOException e) 
		{
			System.err.println("Problem initializing fileWriter object");
		}
		
		//initializes the bufferedwriter for given condition
		if(count_1==0)
		{
			bufferedwriter_object1 = new BufferedWriter(filewriter_object);
			count_1 =1;
		}
		
		//writes the line into the file and goes to next line
		try 
		{
			bufferedwriter_object1.write(line);
			bufferedwriter_object1.newLine();
		} 
		catch (IOException e) 
		{
			System.err.println("Unable to Write the specified line in file: "+line);
		}
	
		/*
		 * looks for phases and accordingly sends strings to grep to be written
		 * to the PatternTextFile
		 * 
		 */
		for(int i=0; i<line.length(); i++)
		{
			//checks for start phase
			switch(bydivision)
			{
				case 0:
					if(line.charAt(i)== 'B')
						bydivision++;
					break;
				case 1:
					if(line.charAt(i)== 'y')
						bydivision++;
					else
						bydivision= 0;
					break;
				case 2:
					if(line.charAt(i)== ' ')
						bydivision++;
					else
						bydivision= 0;
					break;
				case 3:
					if(line.charAt(i)== 'D')
						bydivision++;
					else
						bydivision= 0;
					break;
				case 4:
					if(line.charAt(i)== 'i')
						bydivision++;
					else
						bydivision= 0;
					break;
				case 5:
					if(line.charAt(i)== 'v')
						bydivision++;
					else
						bydivision= 0;
					break;
				case 6:
					if(line.charAt(i)== 'i')
						bydivision++;
					else
						bydivision= 0;
					break;
				case 7:
					if(line.charAt(i)== 's')
						bydivision++;
					else
						bydivision= 0;
					break;
				case 8:
					if(line.charAt(i)== 'i')
						bydivision++;
					else
						bydivision= 0;
					break;
				case 9:
					if(line.charAt(i)== 'o')
						bydivision++;
					else
						bydivision= 0;
					break;
				case 10:
					if(line.charAt(i)== 'n')
					{
						bydivision++;
						str_found=true;
					}
					else
						bydivision= 0;
					break;
				case 11:
					break;
				default:
					System.err.println("Something went WRONG!");
			}
			
			//checks for end phase
			switch(endfile)
			{
				case 0:
					if(line.charAt(i)== 'e')
						endfile++;
					break;
				case 1:
					if(line.charAt(i)== 'n')
						endfile++;
					else
						endfile= 0;
					break;
				case 2:
					if(line.charAt(i)== 'd')
						endfile++;
					else
						endfile= 0;
					break;
				case 3:
					if(line.charAt(i)== 'f')
						endfile++;
					else
						endfile= 0;
					break;
				case 4:
					if(line.charAt(i)== 'i')
						endfile++;
					else
						endfile= 0;
					break;
				case 5:
					if(line.charAt(i)== 'l')
						endfile++;
					else
						endfile= 0;
					break;
				case 6:
					if(line.charAt(i)== 'e')							
					{
						endfile++;
					}
					else
						endfile= 0;
					break;
				default:
						System.err.println("Something went WRONG!");
			}
			
		}
		//if string is found then passses the string to grepatern()
		if(str_found)
		{
			this.grepattern(line);
		}
	}

	private void grepattern(String line) 
	{
		// if file doesn't exists, then create it
		if (!file_object_2.exists()) 
		{
			try
			{
				file_object_2.createNewFile();
			} 
			catch (IOException e) 
			{
				System.err.println("Unable to create pattern text File");;
			}
		}
		
		//absolute address of the above file is given to filewriter as parameter
		try 
		{
			filewriter_object = new FileWriter(file_object_2.getAbsoluteFile());
		} 
		catch (IOException e) 
		{
			System.err.println("Problem initializing fileWriter object");
		}
		
		//initializes the bufferedwriter for given condition
		if(count_2==0)
		{
			bufferedwriter_object2 = new BufferedWriter(filewriter_object);
			count_2 =1;
		}
		
		//writes the line into the file and goes to next line
		try 
		{
			bufferedwriter_object2.write(line);
			bufferedwriter_object2.newLine();
		} 
		catch (IOException e) 
		{
			System.err.println("Unable to Write the specified line in file: "+line);
		}
	}
	/**
	 * This is the main block of our program which reads from NFA.html file and 
	 * then calls readFromhtml() and send buffered reader as parameter for 
	 * further processing.
	 * 
	 * @param args
	 */
	public static void main(String args[])
	{
		//stores filename
		String file= "NFL.html";
		FileReader file_object = null;
		//object of grePatern is created and stores in reference
		GrepPatern greppatern_object = new GrepPatern();
		
		//file is read using filereader
		try 
		{
			file_object = new FileReader(file);
		}
		catch (FileNotFoundException e) 
		{
			System.err.println("File Not Found");
		}
		
		//filereader reference is used as parameter and an object of bufferedreader is created
		BufferedReader bufferedreader_object = new BufferedReader(file_object);
		
		//method calls to readFromhtml is made
		greppatern_object.readFromhtml(bufferedreader_object);
	}
}