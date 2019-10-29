/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spamfilterapplication;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.StringTokenizer;


/**
 *
 * @author ganga
 */
public class WordCategoriser {
    private String spamDirectoryPath, hamDirectoryPath;
    private int threshold,numberOfHam,numberOfSpam;
    private ArrayList<Word> spamWords;
    private ArrayList<Word> hamWords;
    private ArrayList<Word> words;
    
    public WordCategoriser(String spamFilesPath , String hamFilesPath , int threshold)
   {
	this.spamDirectoryPath = spamFilesPath;
	this.hamDirectoryPath = hamFilesPath;
	this.threshold = threshold;
	numberOfSpam = 0;
	numberOfHam = 0;
	spamWords = new ArrayList<Word>();
	hamWords = new ArrayList<Word>();
	words = new ArrayList<Word>();
    }
    
    private void identifySpamWords(){
       
           //stores all the unique words that were found in a single message. 
           HashSet<Word> uniqueWords = new HashSet<Word>();	
            try{
                File directory = new File(this.spamDirectoryPath);
                File[] files = directory.listFiles();
                this.numberOfSpam = files.length;
                //for each available file...
                for(File file : files){
                    BufferedReader br = new BufferedReader(new FileReader(file));			
	String line = br.readLine();		
	//for each line that the file contains...
	while(line != null){
                               StringTokenizer token = new StringTokenizer(line);
	          //for every word of the current line...
	          while(token.hasMoreTokens())
                              {
		uniqueWords.add(new Word(token.nextToken()));
	          }
					
	          line = br.readLine();
	}
				
				
	Iterator<Word> iter1 = uniqueWords.iterator();
	while(iter1.hasNext())
	{
                               Word w = iter1.next();
                               w.setHamCount(0);
	           w.setSpamCount(1);
					
                                int position = spamWords.indexOf(w);
	           //if the word was not in the set , add it.
	           if(position < 0)
                                        spamWords.add(w);
                               else 
                                        spamWords.get(position).incSpamCount(1);
	}
				
	br.close();
	uniqueWords.clear();
                    }
          }
		
           catch(IOException e)
          {
	System.err.println("ERROR : "+e.getMessage());
          }
   }
	
	
	
	
	//finds all the words that are contained in ham messages.
private void FindHamKeywords()
    {
            //stores all the unique words that were found in a single message. 
            HashSet<Word> uniqueWords = new HashSet<Word>();

            try
            {
                    File f = new File(this.hamDirectoryPath);

                    File[] files = f.listFiles();

                    this.numberOfHam = files.length;

                    //for each available file...
                    for(File file : files)
                    {
                            BufferedReader br = new BufferedReader(new FileReader(file));

                            String line = br.readLine();

                            //for each line that the file contains...
                            while(line != null)
                            {
                                    StringTokenizer token = new StringTokenizer(line);

                                    //for every word of the current line...
                                    while(token.hasMoreTokens())
                                    {
                                        Word str=new Word(token.nextToken());
                                        uniqueWords.add(str);
                                    }

                                    line = br.readLine();
                            }


                            Iterator<Word> iter1 = uniqueWords.iterator();

                            while(iter1.hasNext())
                            {
                                    Word w = iter1.next();
                                    w.setHamCount(1);
                                    w.setSpamCount(0);

                                    int position = hamWords.indexOf(w);

                                    //if the word was not in the set , add it.
                                    if(position < 0) hamWords.add(w);

                                    //else increase it's Hw.
                                    else hamWords.get(position).incHamCount(1);
                            }

                            br.close();
                            uniqueWords.clear();
                    }
            }

            catch(IOException e)
            {
                    System.err.println("ERROR : "+e.getMessage());
            }
    }


    //keeps all the words that appear in at least threshold messages.
    private void selectSuitableWords()
    {
            for(int i=0; i<spamWords.size(); i++)
            {
                    Word word = spamWords.get(i);
                    if( (word.getSpamCount()+ word.getHamCount())>this.threshold ) words.add(word);
            }

            for(int i=0; i<hamWords.size(); i++)
            {
                    Word word = hamWords.get(i);
                    if( (word.getSpamCount()+ word.getHamCount())>this.threshold ) words.add(word);
            }
    }
    
    public void SelectFeatures(int hams , int spams)
    {
            this.identifySpamWords();
            this.FindHamKeywords();
            this.selectSuitableWords();
            
            File spmfile=new File("spamKeywords.txt");
            boolean delete = spmfile.delete();
            
            File hmfile=new File("hamKeyWords.txt");
            boolean delete1=hmfile.delete();
            
            
            try
            {
                    File outFile = new File("spamKeywords.txt");
                    BufferedWriter bw = new BufferedWriter(new FileWriter(outFile));
                    bw.flush();

                    //now we sort the words ascending according to their SpamFMeasure.
                    SortBySpamMeasure spamcmp = new SortBySpamMeasure(spams);
                    Collections.sort(words , spamcmp);

                    int count = (spams>=words.size())?0:(words.size() - spams);

                    for(int i=words.size()-1; i>=count; i--)
                    {
                            bw.write(words.get(i).getWord()+"\n");
                    }

                    bw.close();


                    outFile = new File("hamKeywords.txt");
                    bw = new BufferedWriter(new FileWriter(outFile));
                    //now we sort the words ascending according to their HamFMeasure.
                    SortByHamMeasure hamcmp = new SortByHamMeasure(hams);
                    Collections.sort(words , hamcmp);

                    count = (hams>=words.size())?0:(words.size() - hams);

                    for(int i=words.size()-1; i>=count; i--)
                    {
                            bw.write(words.get(i).getWord()+"\n");
                    }

                    bw.close();

            }

            catch(IOException e)
            {
                    System.err.println("ERROR : "+e.getMessage());
            }
    }

    
}
