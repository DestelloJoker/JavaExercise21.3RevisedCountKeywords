/*Program Name: RevisedCountKeywords.java
 * Authors: Austin P
 * Date last Updated: 9/12/2024
 * Purpose: This program is the redesigned version of the provided CountKeywords.java
 * and is meant to If a keyword is in a comment or in a string, don’t count it. 
 * Pass the Java file name from the command line. 
 * Assume the Java source code is correct and line comments and paragraph comments do not overlap.
 */
import java.util.*;
import java.io.*;

public class RevisedCountKeywords {

    public static void main(String[] args) throws Exception {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter a Java source file: ");
        String filename = input.nextLine();
        input.close();
        
        // checks to see if the java source file exists
        File file = new File(filename);
        if (file.exists()) {
            System.out.println("The number of keywords in " + filename
                    + " is " + countKeywords(file));
        } else {
            System.out.println("File " + filename + " does not exist");
        }
    }

    public static int countKeywords(File file) throws Exception {
        // Array of all Java keywords that includes true, false and null
        String[] keywordString = {"abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class",
                "const", "continue", "default", "do", "double", "else", "enum", "extends", "for", "final", "finally",
                "float", "goto", "if", "implements", "import", "instanceof", "int", "interface", "long", "native",
                "new", "package", "private", "protected", "public", "return", "short", "static", "strictfp", "super",
                "switch", "synchronized", "this", "throw", "throws", "transient", "try", "void", "volatile", "while",
                "true", "false", "null"};

        Set<String> keywordSet = new HashSet<>(Arrays.asList(keywordString));
        int count = 0;

        Scanner input = new Scanner(file);
        // To track multiline/block comments /* ... */
        boolean inBlockComment = false; 
        // To track string literals "..."
        boolean inStringLiteral = false; 
        input.close();

        while (input.hasNextLine()) {
            String line = input.nextLine().trim();

            // Skips if a line is empty
            if (line.isEmpty()) {
                continue;
            }

            // Removes any line comments starting with "//"
            int lineCommentIndex = line.indexOf("//");
            if (lineCommentIndex != -1) {
                line = line.substring(0, lineCommentIndex);
            }

            // Scan through each character in the line
            StringBuilder token = new StringBuilder();
            for (int i = 0; i < line.length(); i++) {
                char ch = line.charAt(i);

                // Handles multiline/block comments /* ... */
                if (inBlockComment) {
                    if (ch == '*' && i + 1 < line.length() && line.charAt(i + 1) == '/') {
                        inBlockComment = false;
                        // Skip backslash '/'
                        i++; 
                    }
                    continue;
                } else if (ch == '/' && i + 1 < line.length() && line.charAt(i + 1) == '*') {
                    inBlockComment = true;
                    // Skips the pound sign '*'
                    i++; 
                    continue;
                }

                // Handles string literals "..."
                if (inStringLiteral) {
                    if (ch == '"') {
                        inStringLiteral = false;
                    }
                    continue;
                } else if (ch == '"') {
                    inStringLiteral = true;
                    continue;
                }

                // Tokenize words by spaces and special characters
                if (Character.isLetterOrDigit(ch)) {
                    // Builds a/the current token
                    token.append(ch); 
                } else {
                    if (token.length() > 0) {
                        String word = token.toString();
                        if (keywordSet.contains(word)) {
                            count++;
                        }
                        // Resets the token
                        token.setLength(0); 
                    }
                }
            }

            // Checks the last token in the line
            if (token.length() > 0) {
                String word = token.toString();
                if (keywordSet.contains(word)) {
                    count++;
                }
            }
        }

        return count;
    }
}

