/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spamfilterapplication;
import java.util.Comparator;
/**
 *
 * @author ganga
 */
public class SortBySpamMeasure implements Comparator{
    int totalSpam;
    public SortBySpamMeasure(int total){
        this.totalSpam=total;
    }
    @Override
    public int compare(Object w1, Object w2){
        Word word1=(Word)w1;
        Word word2 =(Word)w2;
        if(word1.spamMeasure(this.totalSpam) == word2.spamMeasure(this.totalSpam)) return 0;
        else if(word1.spamMeasure(this.totalSpam) > word2.spamMeasure(this.totalSpam)) return 1;
        else return -1;
    }
    
}
