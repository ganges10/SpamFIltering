/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spamfilterapplication;
import java.io.*;
import java.util.*;
/**
 *
 * @author ganga
 */
public class NaiveBayesClassifier {
    private ArrayList<Word> FeatureRepository;
    private HashSet<Word> hamKeywords;
    private HashSet<Word> spamKeywords;
    private double spamProbability;
    private double hamProbability;
    private int numberOfHamMessages;
    private int numberOfSpamMessages;
    private int numberOfMessages;
    
    public NaiveBayesClassifier(){
        this.FeatureRepository = new ArrayList<Word>();
        this.hamKeywords = new HashSet<Word>();
        this.spamKeywords = new HashSet<Word>();
        this.numberOfHamMessages=0;
        this.numberOfSpamMessages=0;
        this.numberOfMessages=0;
        this.spamProbability =0.0;
        this.hamProbability =0.0;
    }
    
   
   public boolean classify(String msg){
            double hamSum = 0.0;
            double spamSum = 0.0;
            //the map will contain all the words that were found in the message with their frequencies. 
            HashMap<String , Integer> msgWords = new HashMap<String , Integer>();
            StringTokenizer token = new StringTokenizer(msg);
             while(token.hasMoreTokens())
            {
                    String word = token.nextToken();
                    msgWords.put(word , msgWords.containsKey(word)?msgWords.get(word)+1:1);
            }
             for(int i=0; i<FeatureRepository.size(); i++)
            {
                    Word w = FeatureRepository.get(i);
                    int exists=msgWords.containsKey(w.getWord())?msgWords.get(w.getWord()):0;
                    hamSum  =  hamSum + ( exists*Math.log(w.getHamProbability()) );
                    spamSum = spamSum + ( exists*Math.log(w.getSpamProbability()) );

            }
            hamSum += Math.log(hamProbability);
            spamSum += Math.log(spamProbability);
            if(hamSum >= spamSum) 
                return false;
            return true;
    }
   
   
  private void ComputeProbability(){
            int numOfHam = 0;
            int numOfSpam = 0;

            for(int i = 0; i<FeatureRepository.size(); i++)
            {
                    numOfHam  += FeatureRepository.get(i).getHamCount();
                    numOfSpam +=  FeatureRepository.get(i).getSpamCount();
            }
            
            for(int i = 0; i<FeatureRepository.size(); i++)
            {
                    FeatureRepository.get(i).setHamProbability(FeatureRepository.size() , numOfHam);
                    FeatureRepository.get(i).setSpamProbability(FeatureRepository.size() , numOfSpam);
            }
    }
  
  	
    public void train(String hamDirectoryPath , String spamDirectoryPath , String hamFilePath , String spamFilePath)
    {
            this.trainHamWithKeywords(hamFilePath,hamDirectoryPath);
            this.trainSpamWithKeywords(spamFilePath,spamDirectoryPath);
            this.numberOfMessages=this.numberOfHamMessages+this.numberOfSpamMessages;
           this.hamProbability=(double)this.numberOfHamMessages/(double)this.numberOfMessages;
            this.spamProbability=(double)this.numberOfSpamMessages/(double)this.numberOfMessages;
            this.ComputeProbability();
    }


    private void trainHamWithKeywords(String hamFilePath,String hamFilesPath)
    {
        
          try
            {
                    BufferedReader br = new BufferedReader(new FileReader(new File(hamFilePath)));
                    String word = br.readLine();
                    while(word != null)
                    {
                            if(!word.trim().equals("")) hamKeywords.add(new Word(word.trim()));
                            word = br.readLine();
                    }
                    br.close();
            }
            catch(IOException e)
            {
                    System.err.println("ERROR : "+e.getMessage());
            }
            
          
            try
            {			
                    File directory = new File(hamFilesPath);
                    File[] files = directory.listFiles();
                   this.numberOfMessages=files.length;
                    for(File f :files)
                    {
                            BufferedReader reader = new BufferedReader(new FileReader(f));
                            String line = reader.readLine();
                            while(line!=null)
                            {
                                    StringTokenizer token= new StringTokenizer(line);

                                    while(token.hasMoreTokens())
                                    {
                                            Word w = new Word(token.nextToken());

                                            int pos = FeatureRepository.indexOf(w);

                                            if(pos==-1) 
                                            {
                                                    if(hamKeywords.contains(w))
                                                    {
                                                            w.incHamCount(1);
                                                            FeatureRepository.add(w);
                                                    }
                                            }

                                            else FeatureRepository.get(pos).incHamCount(pos);
                                    }

                                    line=reader.readLine();
                            }

                            reader.close();
                    }
            }
            catch(IOException e)
            {
                    System.err.println("ERROR : "+e.getMessage());
            }
    }




    private void trainSpamWithKeywords(String spamFilePath,String spamFilesPath)
    {
          try{
                    BufferedReader br = new BufferedReader(new FileReader(new File(spamFilePath)));
                    String word = br.readLine();
                    while(word != null)
                    {
                            if(!word.trim().equals("")) spamKeywords.add(new Word(word.trim()));
                            word = br.readLine();
                    }
                    br.close();
            }
            catch(IOException e)
            {
                    System.err.println("ERROR : "+e.getMessage());
            }
          
          
            try
            {			
                    File directory = new File(spamFilesPath);
                    File[] files = directory.listFiles();
                   this.numberOfMessages=files.length;
                    for(File f :files)
                    {
                            BufferedReader reader = new BufferedReader(new FileReader(f));
                            String line = reader.readLine();
                            while(line!=null)
                            {
                                    StringTokenizer token = new StringTokenizer(line);
                                    while(token.hasMoreTokens())
                                    {
                                            Word w = new Word(token.nextToken());
                                            int pos = FeatureRepository.indexOf(w);
                                            if(pos==-1 ) 
                                            {
                                                    if(spamKeywords.contains(w))
                                                    {
                                                            w.incSpamCount(1);
                                                            FeatureRepository.add(w);
                                                    }
                                            }

                                            else FeatureRepository.get(pos).incSpamCount(1);
                                    }

                                    line=reader.readLine();
                            }

                            reader.close();
                    }
            }
            catch(IOException e)
            {
                    System.err.println("ERROR : "+e.getMessage());
            }
    }
    
}
