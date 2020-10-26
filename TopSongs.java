import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Scanner;

public class TopSongs {
    
    //Create a linked list, read the file names into it, and return the list
    public static LinkedList createFileList() {
        //Assume the list of file names is stored in a text file
        //Ask user for the name of this file
        Scanner keyboard = new Scanner(System.in);
        System.out.print("Name of file containing list of files: ");
        String listName = keyboard.next();
        keyboard.close();
        LinkedList fileNames = new LinkedList();
        try {
            File fileList = new File(listName);
            Scanner scanner = new Scanner(fileList);
            while (scanner.hasNext()) {
                String fileName = scanner.next();
                fileNames.add(fileName);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File containing list of file names not found.");
            e.printStackTrace();
        }
        return fileNames;
    }
    
        //Extract the song title from a line of text in the csv file
    public static String getSongTitle(String line) {
        //we include parentheses
        String[] words = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        String title = words[1];
        //but we eliminate any quotation marks, because in the file only some songs have them
        title = title.replace("\"", "");
        return title;
    }
    
    //Add song title to correct position in sorted linked list
    public static void addSong(LinkedList<String> list, String title) {
            //If the list is empty just add the title at the head of the list
            //This conditional is only true once
            if (list.isEmpty()) {
                list.add(title);
            //Insert the title in the right place
            } else if ( list.get(0).compareToIgnoreCase(title) > 0) {
                list.add(0, title);
            } else if ( list.get(list.size() - 1).compareToIgnoreCase(title) < 0) {
                list.add(list.size(), title);
            } else {
                int i = 0;
                while (list.get(i).compareToIgnoreCase(title) <= 0) {
                    if (list.get(i).compareToIgnoreCase(title) == 0) {
                        return;
                    }
                    i++;
                }
                list.add(i, title);
            }
    }
    
    //Create a sorted list of every song in all the files, without repetition
    public static LinkedList createSongList(LinkedList fileNames) {
        ListIterator<String> listIterator = fileNames.listIterator();
        String line;
        String songTitle;
        LinkedList<String> songTitles = new LinkedList<String>();
        while (listIterator.hasNext()) {
            try {
                File currentFile = new File(listIterator.next());
                Scanner scanner = new Scanner(currentFile);
                //we ignore the first two lines because they do not contain data
                scanner.nextLine();
                scanner.nextLine();
                int i = 0;
                while (scanner.hasNext()) {
                    line = scanner.nextLine();
                    songTitle = getSongTitle(line);
                    addSong(songTitles, songTitle);
                }
            } catch (FileNotFoundException e) {
                System.out.println("Weekly csv file not found.");
                e.printStackTrace();
            }
        }
        return songTitles;
    }
    
    public static void playNext(LinkedList<String> playlist, LinkedList<String> recent) {
        String title = playlist.pop();
        //add whatever code is needed to actually play the song
        //add the song to the top of the "recently played" queue
        recent.addFirst(title);
    }

    public static void main(String[] args) {
        //Create a linked list of file names
        LinkedList<String> fileNames = createFileList();
        //Create a linked list of all song titles contained within the files, without repetition
        LinkedList<String> playlist = createSongList(fileNames);
        //Create a linked list of recently played songs. Linked list implementation of a queue
        LinkedList<String> recent = new LinkedList<>();
    }
    
}
