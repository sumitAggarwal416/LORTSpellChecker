import java.awt.print.Book;
import java.io.CharConversionException;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class LORTSpellChecker {

    /**
     *
     * Sumit Aggarwal - 000793607 - original work; not copied; not let copied.
     *
     * The starting point of the application
     *
     * The Linear Search took about 5501 ms on my computer. The time taken by the contains() of the ArrayList is of the
     * order O(n) thus depends on the size of the ArrayList.
     *
     * The Binary Search took about 32 ms on my computer. Binary search's time complexity is of the order O(log n). We
     * got a much better result because we used the Collections.sort() to sort the data in the ArrayList.
     *
     * The Hashset Search was the quickest. That is because the search runs in O(1) time. It is always constant
     * regardless of how big the dataset is.
     *
     *
     *
     */
    public static void main(String[] args) {

        SimpleHashSet<BookWord> dictionaryHashSet = new SimpleHashSet<>();
        ArrayList<BookWord> dictArrayListBW = new ArrayList<>();
        ArrayList<String> dictionaryArrayList = new ArrayList<>();
        SimpleHashSet<BookWord> novelWords = new SimpleHashSet<>();
        ArrayList<BookWord> novelWordsList = new ArrayList<>();
        ArrayList<BookWord> book = new ArrayList<>();
        ArrayList<Character> characters = new ArrayList<>();

        ArrayList<Integer> ringPositions = new ArrayList<>();


        // File is stored in a resources folder in the project
        final String novel = "src/TheLordOfTheRings.txt";
        final String dictionary = "src/US.txt";
        final String characterNames = "src/Characters.txt";
        int count = 0;

        try {
            Scanner fin = new Scanner(new File(dictionary));
            while (fin.hasNext()) {
                String w = fin.next().toLowerCase();
                if (w.length() > 0) {
                    dictionaryArrayList.add(w);
                    dictionaryHashSet.insert(new BookWord(w));
                    dictArrayListBW.add(new BookWord(w));
                }
            }
            fin.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Exception caught: " + ex.getMessage());
        }

        try {
            Scanner fin = new Scanner(new File(characterNames));
            while (fin.hasNext()) {
                String name = fin.next().toLowerCase();
                if (name.length() > 0) {
                    characters.add(new Character(name));
                }
            }
            fin.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Exception caught: " + ex.getMessage());
        }


        try {
            Scanner fin = new Scanner(new File(novel));
            fin.useDelimiter("\\s|\"|\\(|\\)|\\.|\\,|\\?|\\!|\\_|\\-|\\:|\\;|\\n");  // Filter - DO NOT CHANGE
            while (fin.hasNext()) {
                String fileWord = fin.next().toLowerCase();
                if (fileWord.length() > 0) {
                    boolean wordExists = false;
                    count++;
                    BookWord w = new BookWord(fileWord);
                    book.add(w);
                    for (BookWord newWord : novelWordsList) {
                        if (newWord.equals(w)) {
                            newWord.incrementCount();
                            wordExists = true;
                            break;
                        }
                    }

                    if (!wordExists) {
                        w.incrementCount();
                        novelWords.insert(w);
                        novelWordsList.add(w);
                    }

                    for (Character couldBeLord : characters) {
                        if (couldBeLord.getName().trim().equalsIgnoreCase(fileWord)) {
                            couldBeLord.incrementOccurrences();
                        }
                    }
                }
            }
            fin.close();
        } catch (FileNotFoundException e) {
            System.out.println("Exception caught: " + e.getMessage());
        }

        Collections.sort(novelWordsList, new Comparator<BookWord>() {
            @Override
            public int compare(BookWord o1, BookWord o2) {
                if (o1.getCount() != o2.getCount())
                    return o1.getCount() - o2.getCount();
                return o2.getText().compareTo(o1.getText());
            }
        }.reversed());

        Collections.sort(dictArrayListBW, new Comparator<BookWord>() {
            @Override
            public int compare(BookWord o1, BookWord o2) {
                if(o1.getCount() != o2.getCount())
                    return o1.getCount() - o2.getCount();
                return o2.getText().compareTo(o1.getText());
            }
        }.reversed());

        System.out.println("Word Analysis / Spell Checker");
        System.out.println("============================= \n ");
        System.out.println("Unique words in the novel: " + novelWords.size());
        System.out.println("There are " + count + " words in the novel");
        System.out.println("The list of the 15 most frequent words and counts: ");
        for (int i = 0; i < 10; i++) {
            System.out.println(i + 1 + " : [" + (novelWordsList.get(i)).getText() + ", " + (novelWordsList.get(i)).getCount() + "]");
        }

        System.out.println("The words below each occur 64 times in the novel");
        for (BookWord word : novelWordsList) {
            if (word.getCount() == 64)
                System.out.println("[" + word.getText() + ", " + word.getCount() + "]");
        }

        count = 0;
        long startTime = System.nanoTime();
        for (BookWord word : novelWordsList) {
            if (!dictionaryArrayList.contains(word.getText()))
                count++;
        }
        long linearSearchTime = (System.nanoTime() - startTime) / 1000;
        System.out.println("LINEAR SEARCH - " + count + " misspelled words - time = " + linearSearchTime + " us");

        dictionaryArrayList.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });


        startTime = System.nanoTime();
        count = 0;
        for (BookWord word : novelWordsList) {
            if (Collections.binarySearch(dictArrayListBW, word, new Comparator<BookWord>() {
                @Override
                public int compare(BookWord o1, BookWord o2) { return (o1.getText()).compareTo(o2.getText()); }
            }) < 0)
                count++;
        }
        long binarySearchTime = (System.nanoTime() - startTime) / 1000;
        System.out.println("BINARY SEARCH - " + count + " misspelled words - time = " + binarySearchTime + " us");

        count = 0;
        startTime = System.nanoTime();
        for (BookWord word : novelWordsList) {
            if (!dictionaryHashSet.contains(word))
                count++;
        }
        long hashsetSearchTime = (System.nanoTime() - startTime) / 1000;
        System.out.println("HASHSET SEARCH - " + count + " missplled words - time = " + hashsetSearchTime + " us");

        System.out.println("Number of buckets: " + dictionaryHashSet.getNumberofBuckets());
        System.out.println("Biggest bucket: " + dictionaryHashSet.getLargestBucketSize());
        System.out.println("The number of empty buckets are: " + dictionaryHashSet.getNumberofEmptyBuckets());
        System.out.printf("Percentage of empty buckets: %2.2f", ((double) dictionaryHashSet.getNumberofEmptyBuckets() / dictionaryHashSet.getNumberofBuckets()) * 100);

        System.out.println("\n=============================");
        System.out.printf("Ratio of Hash to Linear: %3.1f", (double) linearSearchTime / hashsetSearchTime);
        System.out.printf("\nRatio of Hash to Binary: %3.1f \n", (double) binarySearchTime / hashsetSearchTime);

        // ADD other code after here

        System.out.println("ORDER OF WHO REALLY WANTS the RING");
        System.out.println("==================================");

        int CUT_OFF_VALUE = 42;

        for(int i = 0; i < book.size(); i++){
            if(book.get(i).getText().equalsIgnoreCase("ring"))
                ringPositions.add(i);
        }

        startTime = System.nanoTime();
        for(Integer i : ringPositions){
            if(i < CUT_OFF_VALUE){
                for(int j = 0; j <= i + CUT_OFF_VALUE; j++){
                    incrementCall(j, characters, book);
                }
            }
            else if(i + CUT_OFF_VALUE > book.size()){
                for(int j = i - CUT_OFF_VALUE; j < book.size(); j++){
                    incrementCall(j, characters, book);
                }
            }
            else{
                for(int j = i - CUT_OFF_VALUE; j <= i + CUT_OFF_VALUE; j++){
                    incrementCall(j, characters, book);
                }
            }
        }

        Collections.sort(characters, new Comparator<Character>() {
            @Override
            public int compare(Character o1, Character o2) {
                return Double.compare(o1.getClosenessFactor(), o2.getClosenessFactor());
            }
        }.reversed());


        for(Character c : characters)
            System.out.printf("\n[%s,%d] Close to Ring %d ClosenessFactor %2.4f", c.getName(), c.getOccurrences(),+
                    c.getProximity(), c.getClosenessFactor());

        System.out.println("\nTime taken by part B: " + (System.nanoTime() - startTime) / 1000 + " us" );


    }

    public static void incrementCall(int index, ArrayList<Character> characters, ArrayList<BookWord> book){
        for(Character character : characters){
            if(book.get(index).getText().equals(character.getName()))
                character.incrementProximity();
        }
    }

}

