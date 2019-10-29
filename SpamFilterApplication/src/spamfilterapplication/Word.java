/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spamfilterapplication;

import java.util.Objects;

/**
 *
 * @author ganga
 */
public class Word {
 private String word;
 private int hamCount,spamCount;
private double hamProbability,spamProbability;
 
 public Word(String wrd){
     this.word=wrd;
    this.hamCount = 0;
    this.spamCount = 0;
    this.spamProbability=0.0;
    this.hamProbability=0.0;
 }
 
 public String getWord(){
     return this.word;
 }
 
 public void setSpamCount(int count){
     this.spamCount=count;
 }
 
 public void setHamCount(int count){
     this.hamCount=count;
 }
  
 public int getSpamCount(){
     return this.spamCount;
 }
 
 public int getHamCount(){
     return this.hamCount;
 }
 
 public int getTotalOccurance(){
     return this.hamCount + this.spamCount;
 }
 
 public double getSpamProbability(){
     return this.spamProbability;
 }
 
  public double getHamProbability(){
     return this.hamProbability;
 }
 
 public void incSpamCount(int count){
     this.spamCount+=count;
 }
 
 public void incHamCount(int count){
     this.hamCount += count;
 }
 
 public double hamMeasure(int totalHam){
     double hamPrecision = (double)this.hamCount /(double) this.getTotalOccurance();
     double hamRecall;
     if(totalHam == 0)
         hamRecall =0;
     else
         hamRecall = (double)this.hamCount/(double)totalHam;
     if(hamPrecision + hamRecall == 0){
         return 0;
     }
     else
         return (2* hamPrecision*hamRecall)/(hamPrecision+hamRecall);
 }
 
public double spamMeasure(int totalSpam){
     double spamPrecision = (double)this.hamCount /(double) this.getTotalOccurance();
     double spamRecall;
     if(totalSpam == 0)
         spamRecall =0;
     else
         spamRecall = (double)this.spamCount/(double)totalSpam;
     if(spamPrecision + spamRecall == 0){
         return 0;
     }
     else
         return (2* spamPrecision*spamRecall)/(spamPrecision+spamRecall);
 }

public void setHamProbability(int number_of_keywords , int sum)
{
          this.hamProbability= (double)(1+this.hamCount)/(double)(number_of_keywords+sum);
}

public void setSpamProbability(int number_of_keywords , int sum)
{
           this.spamProbability=(double)(1+this.spamCount)/(double)(number_of_keywords+sum);
}
 
 @Override
 public boolean equals(Object o){
     if(o instanceof Word){
            Word d=(Word)o;
            return (this.word.equals(d.word));
        }
        return false;
}

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.word);
        return hash;
    }
    
 @Override
    public String toString(){
        return ("Word : "+this.word+"\nNumber of times the word was found in ham messages : " +this.hamCount+"\nNumber of times the word was found in spam messages : "+this.spamCount+"\nProbability of  being present in Spam messages:"+this.spamProbability+"\nProbability of being present in Ham messages: "+this.hamProbability);
}
 
}
