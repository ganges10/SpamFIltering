/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spamfilterapplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import static java.lang.Thread.sleep;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ganga
 */

class Categoriser implements Runnable{
    WordCategoriser categoriser;
    public Categoriser(WordCategoriser wc){
        this.categoriser=wc;
    }
    @Override
    public void run() {
        this.categoriser.SelectFeatures(500, 500);
        
    }
    
}

class Classifier implements Runnable{
    NaiveBayesClassifier classifier;
    Categoriser cat;
    public Classifier(NaiveBayesClassifier nbc){
        this.classifier=nbc;
        cat=new Categoriser(new WordCategoriser("spamMessages" , "hamMessages" , 5));
    }
    public void checkFiles(){
         File directory = new File("newMessages");
        File[] files = directory.listFiles();
        for(File f :files)
        {
            try {
                BufferedReader  br = new BufferedReader(new FileReader(f));              
                String msg="";
                String line = br.readLine();
                while(line!=null) 
                {
                        msg+=line;
                        line=br.readLine();
                }
                Path temp;
                if(classifier.classify(msg)){
                    temp=Files.move(Paths.get(f.getAbsolutePath()), Paths.get("spamMessages//"+f.getName()));
                }
                else{
                    temp=Files.move(Paths.get(f.getAbsolutePath()), Paths.get("hamMessages//"+f.getName()));
                }
            } 
            catch (IOException ex) {
                System.err.println("ERROR : "+ex.getMessage());
            }
        }
    }
    
    @Override
    public void run() {
        this.classifier.train("hamMessages", "spamMessages","hamKeywords.txt", "spamKeywords.txt");
        try {
            while(true){
                this.checkFiles();
                this.cat.run();
                sleep(5000);
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Classifier.class.getName()).log(Level.SEVERE, null, ex);
        }
       }
}

public class SpamFilterApplication {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        WordCategoriser  wc = new WordCategoriser("spamMessages" , "hamMessages" , 5);
        Categoriser categoriser=new Categoriser(wc);
        NaiveBayesClassifier nbc =new NaiveBayesClassifier();
        Classifier classifier1=new Classifier(nbc);
        Thread t1=new Thread(categoriser);
        Thread t2 =new Thread(classifier1);
        t1.start();
        try {
            t1.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(SpamFilterApplication.class.getName()).log(Level.SEVERE, null, ex);
        }
        t2.start();
       
    }
    
}
