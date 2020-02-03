/**
 * This Class reads a music text file.
 * The first two lines indicate the melody title and artist.
 * Each succeeding line is another note the song.
 * Each note is instantiated in the Note Class, and added to the Melody arrayList.
 * 
 *  @author Anthony Wocken
 */

import java.util.*;
import java.io.*;
import melody.audio.*;

public class Melody {
    
    /**
     * Instance Variables
     */
    public double currentDuration;          
    public Pitch currentPitch;
    public int currentOctave;
    public Accidental currentAccidental;
    public boolean currentRepeat;
    public String artistName;
    public String melodyTitle;
    public String currentLine;
    public Note currentNote;
    private double melodyDuration;
    ArrayList<Note>list = new ArrayList<Note>();
    public String stringRepresentation;
    Note noteTemp1;
    
    
    
    /**
     * This constructor reads the entire song text file and converts it to a listArray of notes.
     * 
     * @param file accepts a song text file 
     */
    public Melody(File file) throws FileNotFoundException{
        Scanner scanner = new Scanner(file);
        melodyTitle = scanner.nextLine();
        artistName = scanner.nextLine();
        
        while(scanner.hasNextLine()){
            currentLine = scanner.nextLine();
            Scanner lineScanner = new Scanner(currentLine);
            currentDuration = lineScanner.nextDouble();
            currentPitch = Pitch.getValueOf(lineScanner.next());
            
            if(currentPitch != Pitch.R){
                currentOctave = lineScanner.nextInt();
                currentAccidental = Accidental.getValueOf(lineScanner.next());
                currentNote = new Note(currentDuration, currentPitch, currentOctave, 
                                        currentAccidental, currentRepeat);
                list.add(currentNote);
            } else if(currentPitch == Pitch.R){
                if(lineScanner.next().equals("false")){
                    currentRepeat = false;
                }else if(lineScanner.next().equals("true")){
                    currentRepeat = true;
                }
                currentNote = new Note(currentDuration, currentRepeat);
                list.add(currentNote);
            }
            lineScanner.close();
        }
        scanner.close();
        System.out.println();
    }

   
    /**
     * Scale (multiply) the duration of each Note in this Melody by the given ratio.  
     * For example, passing a ratio of 1.0 will do nothing, 
     * while a ratio of 2.0 will make each note's duration twice as long (slow down the song), 
     * or a ratio of 0.5 will make each note half as long (speed up the song).
     * 
     * @param ratio accepts multiplier
     * 
    */
    public void changeTempo(double ratio) {
        for(int i = 0; i < list.size(); i++){
            list.get(i).setDuration(list.get(i).getDuration() * ratio);
        }
    }
    /**
     * returns artist name
     * @return artist name
     */
    public String getArtist() {
        return artistName;
    }
    
    /**
     * returns melody title
     * @return song title
     */
    public String getTitle() {
        return melodyTitle;
    }
    
    /**
     * Return this Melody's total duration (length) in seconds.  
     * In general this is equal to the sum of the durations of the song's notes, 
     * but if some sections of the song are repeated, those parts count twice toward the total. 
     * 
     * @return duration of melody
     */
    public double getTotalDuration() {
        for(int i = 0; i < list.size();i++){
            melodyDuration += list.get(i).getDuration();
        }
        return melodyDuration;
    }
    
    /**
     * Checks that melody is not at lowest limit of octave range.
     * If not, it lowers each note by 1 octave
     * @return true, or false if range is already at lowest limit
     */
    public boolean octaveDown() {
        for(int i = 0; i < list.size(); i++){
            if(list.get(i).getOctave() == (Note.OCTAVE_MIN)){
                return false;
            }
        }
        // reaches this point if no MIN octave in melody
        for(int i = 0; i < list.size(); i++){
            list.get(i).setOctave(list.get(i).getOctave() - 1);
        }
        return true;
    }
    
    /**
     * Checks that melody is not at highest limit of octave range.
     * If not, it raises each note by 1 octave.
     * @ return true, or false if range is already at upper limit
     */
    public boolean octaveUp() {
        for(int i = 0; i < list.size(); i++){
            if(list.get(i).getOctave() == (Note.OCTAVE_MAX)){
                return false;
            }
        }
        // reaches this point if no MAX octave in melody
        for(int i = 0; i < list.size(); i++){
            list.get(i).setOctave(list.get(i).getOctave() + 1);
        }
        return true;
    }
    
    /**
     * Play this Melody so that it can be heard on the computer's speakers.  
     * Essentially this consists of calling the play method on each Note.  
     * The notes should be played from the beginning of the list to the end, 
     * unless there are notes that are marked as being part of a repeated section.  
     * If a series of notes represents a repeated section, that sequence is played twice. 
     */
    public void play() {
        for(int i = 0; i < list.size(); i++){
            list.get(i).play();
        }
    }
    
    /**
     * Reverse the order of the Notes in this Melody, 
     * so that future calls to play would play the notes 
     * in the opposite of the order they were in before the call.   
     * Two calls to reverse() puts the Notes back in their original order.  
     */
    public void reverse() {
        for(int i = 0; i < list.size()/2; i++){
            noteTemp1 = list.get(i);
            list.set(i, list.get(list.size()-1-i));
            list.set(list.size()-1-i, noteTemp1);
        }
    }
    
    /**
     * Return a String representation of this Melody. 
     * It should include the title, artist, and all the Notes, each separated by \n
     * @return string representation of melody
     */
    public String toString() {
        stringRepresentation += "\n" + melodyTitle + "\n";
        stringRepresentation += artistName + "\n";
        for(int i = 0; i < list.size(); i++){
            stringRepresentation += list.get(i).getOctave() + " " + list.get(i).getPitch() + "\n";
        }
        return stringRepresentation;
    }
}