import java.util.Objects;

public class BookWord {

    private String text;
    private int count;

    public BookWord(String wordText) {
        this.text = wordText;
    }

    public String getText() {
        return text;
    }

    public int getCount() {
        return count;
    }

    public void incrementCount() {
        count++;
    }

    @Override
    public boolean equals(Object wordToCompare) {
        if (wordToCompare != null && wordToCompare.getClass() == this.getClass()) {
            BookWord otherWord = (BookWord)wordToCompare;
            if (text.equalsIgnoreCase(otherWord.getText()))
                return true;
        }
        return false;
    }

    /*
    Polynomial Rolling Hash Function has been implemented here
    https://www.geeksforgeeks.org/string-hashing-using-polynomial-rolling-hash-function/
     */
    @Override
    public int hashCode() {
//        String word = text.toLowerCase();
        int p = 31;
        int m = (int) (Math.pow(10,9) + 9);
        long powerOfP = 1;
        long hashValue = 0;

        for (int i = 0; i < text.length(); i++) {
            hashValue = (hashValue + (text.charAt(i) - 'a' + 1) * powerOfP) % m;
            powerOfP = (powerOfP * p) % m;
        }

        return (int) hashValue;

//        int h = 0;
//        int power = 0;
//        for(int i = 0; i < text.length(); i++){
//            char letter = text.charAt(i);
//            int ascii = letter;
//            h += (ascii * Math.pow(3, power));
//            power++;
//        }
//
//        return h;

    }

    @Override
    public String toString() {
        return "The word is '" + text + "' and the count is " + count;
    }

}
