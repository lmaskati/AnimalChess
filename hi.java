import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Random;

/**
* This class contains all the functionality for the WalkSAT algorithm. 
*/
public class WalkSAT {

    /**
     * HashMap, where the key is a clause and the value represents the number of literals in the clause that satisfy the clause
    */
    public static HashMap<ArrayList<Integer>, Integer> numSatisfiedLitsPerClause = new HashMap<ArrayList<Integer>, Integer>();
    /**
     * An arraylist containing all the clauses
    */
    public static ArrayList<ArrayList<Integer>> clauses = new ArrayList<ArrayList<Integer>>();
    /**
     * An arraylist containing all the unsatisfied clauses
    */
    public static ArrayList<ArrayList<Integer>> falseClauses = new ArrayList<ArrayList<Integer>>();
    /**
     * HashMap, where the key is a literal and the value determines the truth value assigned to the literal
    */
    public static HashMap<Integer, Boolean> truthState = new HashMap<Integer, Boolean>();
    /**
     * HashMap, where the key is a literal and the value is a hashset of all the clauses which contain 
     * that literal
    */
    public static HashMap<Integer, HashSet<ArrayList<Integer>>> literals = new HashMap<Integer, HashSet<ArrayList<Integer>>>();
    /**
     * the number of variables
    */
    public static int numVar;
    /**
     * the number of clauses
    */
    public static int numClause;
    /**
     * the probability set by the user, used in pickVar
    */
    public static Double p;
    /**
     * the time the SAT loop begins
    */
    public static long startTime;
    /**
     * the maximum CPU time to be used, set by the user
    */
    public static int maxTime;
    /**
     * the maximum CPU time to be used, set by the user
    */
    public static long curTime;
    /**
     * the number of flips made
    */
    public static int numFlips = 0;

    public static void main(String[] args) {
        //get the file path
        System.out.println("Enter the file path:");
        Scanner scanner = new Scanner(System.in);
        String s = scanner.nextLine();
        //error check to see if the file path is valid
        while (readInput(s) == 0) {
            System.out.println("The file path was wrong. Please try again.");
            s = scanner.nextLine();
        }
        //get a value for p
        System.out.println("Please enter a value for p:");
        s = scanner.nextLine();

        int isValid = 0;
        //error check to make sure value for p is valid
        while (isValid == 0) {
            try {
                Double pVal = Double.parseDouble(s);
                if (pVal < 0.0 || pVal > 1.0) {
                    System.out.println("The p-value must be between 0 and 1. Please try again.");
                    s = scanner.nextLine();
                }
                else {
                    p = pVal;
                    isValid = 1;
                }
            } catch(NullPointerException | NumberFormatException e) {
                System.out.println("Please format p correctly.");
            }
        }
        //get the max CPU time
        System.out.println("Please enter the maximum CPU time (in milliseconds) you would like the program to use:");
        s = scanner.nextLine();
        isValid = 0;
        //error check
        while (isValid == 0) {
            try {
                Integer cpuTime = Integer.parseInt(s);
                if (cpuTime < 0) {
                    System.out.println("The CPU time must be a posutive number. Please try again.");
                    s = scanner.nextLine();
                }
                else {
                    isValid = 1;
                    maxTime = cpuTime;
                }
            } catch(NullPointerException | NumberFormatException e) {
                System.out.println("Please format the CPU time correctly.");
            }
        }
        scanner.close();
        runTests();
    }
    public static void runTests() {
        createContainingClauses();
        //determine false clauses
        setFalseClauses();
        //set start time
        startTime = System.currentTimeMillis();
        //try to find a solution
        SATloop();
        // if solution is found within specified max CPU time  
        if (curTime < maxTime) {
            System.out.println("SATISFIALBE:");
            System.out.println("Time taken (ms): " + Long.toString(curTime));
            System.out.println("Number of flips: " + Integer.toString(numFlips));
            //print solution
            printHashMap();
        }
        else {
            System.out.println("UNSATISFIALBE:");
            System.out.println("Time exceeded maximum CPU time.");
            System.out.println("Number of flips: " + Integer.toString(numFlips));
        }
    }

    public static Integer readInput(String filePath) {
        File file = new File(filePath);
        try (Scanner inputStream = new Scanner(file);) {
            //number of clauses read so far
            int numClausesProcessed = 0;
            //initialize new clause
            ArrayList<Integer> clause = new ArrayList<Integer>();
            //iterate through the lines in the file
            while (inputStream.hasNextLine()) {
                String data = inputStream.nextLine();
                //we have reached the end of the file
                if (data.equals(""))
                    break;
                //if the first character of the line is p
                if (data.charAt(0) == 'p') {
                    String[] stuff = data.split(" ");
                    //first index is p, second is num variables, fourth is num clauses (3rd is whitespace)
                    numVar = Integer.parseInt(stuff[2]);
                    numClause = Integer.parseInt(stuff[4]);
                }
                //if the line is not a comment
                else if (data.charAt(0) != 'c') {
                    String[] stuff = data.split(" ");
                    //add each literal 
                    for (int i = 0; i < stuff.length; i++) {
                        if (!stuff[i].equals("")) { 
                            clause.add(Integer.parseInt(stuff[i]));
                        }
                    }
                }
            }

            //form the clauses
            //curList is an arraylist representing the current clause
            ArrayList<Integer> curList = new ArrayList<>();
            for (int i = 0; i < clause.size(); i++) {
                //if num clauses processed is greated than specified number of clauses
                //then break
                if (numClausesProcessed >= numClause) {
                    break;
                }
                //if the current element is a literal
                else if (clause.get(i) != 0) {
                    curList.add(clause.get(i));
                } else {
                    //if the current element is a 0
                    clauses.add(curList);
                    numClausesProcessed += 1;
                    curList = new ArrayList<>();
                }
            }
            //check the last clause
            if (curList != null && (numClausesProcessed < numClause)) {
                clauses.add(curList);
            }
            inputStream.close();
            //set initial truth values 
            setTruth();
            return 1;
        } catch (FileNotFoundException e) {
            return 0;
        }
    }

    public static void setTruth() {
        // assign random truth value to each variable and add to hashmap 
        for (int i = 1; i <= numVar; i++) {
            Random rd = new Random(); 
            Boolean boolRand = rd.nextBoolean();
            truthState.put(i, boolRand);  
            truthState.put(-i, !boolRand);            
        } 
    }

    public static void SATloop() {
        //CPU time used so far
        curTime = System.currentTimeMillis() - startTime;
        //while unsatisfied
        while (falseClauses.size() > 0 && (curTime < maxTime)) {
            curTime = System.currentTimeMillis() - startTime;
            //choose an unsatisfied clause C at random
            ArrayList<Integer> unsatisfiedClause = pickClause();
            Random rand = new Random();
            //random probability, to determine whether to use pickVar or not
            float floatRandom = rand.nextFloat();
            //variable to be flipped
            int toFlip;
            if (floatRandom > p)
                toFlip = pickVar(unsatisfiedClause);
            else {
                int index = rand.nextInt(unsatisfiedClause.size());
                toFlip = unsatisfiedClause.get(index);
            }
            flip(toFlip);
        }
    }

    public static ArrayList<Integer> pickClause(){
        Random rand = new Random();
        int index = rand.nextInt(falseClauses.size());
        return falseClauses.get(index);
    }

    public static void createContainingClauses() {
        //iterate through clauses
        for (int i = 0; i < clauses.size(); i++) {
            ArrayList<Integer> curClause = clauses.get(i);
            for (int j = 0; j < curClause.size(); j++) {
                //current literal being considered
                int curLit = curClause.get(j);
                //check if curLit is a key in literals
                if (literals.containsKey(curLit)) {
                    //add current clause to current literal's value
                    HashSet<ArrayList<Integer>> curVal = literals.get(curLit);
                    curVal.add(curClause);
                    literals.put(curLit, curVal);
                }
                else {
                    //add current clause to current literal's value
                    HashSet<ArrayList<Integer>> curVal = new HashSet<ArrayList<Integer>>();
                    curVal.add(curClause);
                    literals.put(curLit, curVal);
                }
            }
        }
    }

    public static void setFalseClauses() {
        for (int i = 0; i < clauses.size(); i++) {
            //iterate through clausea
            ArrayList<Integer> curClause = clauses.get(i);
            //number of literals that satisfy the clause
            int numSatisfied = 0;
            
            for (int j = 0; j < curClause.size(); j++) {
                int curVar = curClause.get(j);
                //check truth assignment of curVal
                if (truthState.get(curVar) == true) {
                    numSatisfied += 1;
                } 
            }
            //if no literals satisy the clause, it is a false clause
            if (numSatisfied == 0){
                falseClauses.add(curClause);
            }
            numSatisfiedLitsPerClause.put(curClause, numSatisfied);

        }
    }

    public static void flip(int v) {
        //increment numFlips
        numFlips += 1;
        //flip truth value of v and -v 
        truthState.replace(v, !truthState.get(v));
        truthState.replace(-1*v, !truthState.get(-1*v));
        
        //consider all clauses that contain v
        HashSet<ArrayList<Integer>> vClauses = literals.get(v);
        //iterate through clauses in this hash set
        if (vClauses != null) {
            for (ArrayList<Integer> curClause : vClauses) {
                //one more literal satisfies the clause, so increment curNum
                int curNum = numSatisfiedLitsPerClause.get(curClause);
                numSatisfiedLitsPerClause.replace(curClause, curNum + 1);
                if (curNum == 0) {
                    //remove clause from false clauses
                    falseClauses.remove(curClause);
                }
            }
        }

         //consider all clauses that contain -v
        HashSet<ArrayList<Integer>> vCompClauses = literals.get(-1*v);
        //iterate through clauses this hash set
        if (vCompClauses != null) {
            for (ArrayList<Integer> curClause : vCompClauses) {
                //one less literal satisfies the clause, so decrement curNum
                int curNum = numSatisfiedLitsPerClause.get(curClause);
                numSatisfiedLitsPerClause.replace(curClause, curNum - 1);
                if (curNum == 1) {
                    //add clause to false clauses
                    falseClauses.add(curClause);
                }
            }
        }
    }


    public static int pickVar(ArrayList<Integer> curClause) {
        int res = Integer.MIN_VALUE;
        int finalVar = 0;

        for (Integer varNow : curClause) {
            int breaks = 0;
            int makes = 0;

            //look at all the clauses containing varNow
            HashSet<ArrayList<Integer>> vClauses = literals.get(varNow);
            //iterate through clauses this hash set
            if (vClauses != null) {
                for (ArrayList<Integer> myClause : vClauses) {
                    //increment makes, because flipping varNow would make this clause true
                    int curNum = numSatisfiedLitsPerClause.get(myClause);
                    if (curNum == 0) {
                        makes += 1;
                    }
                }
            }

            //look at all the clauses containing -varNow
            HashSet<ArrayList<Integer>> vCompClauses = literals.get(-1*varNow);
            //iterate through clauses this hash set
            if (vCompClauses != null) {
                for (ArrayList<Integer> myClause : vCompClauses) {
                    //increment breaks, because flipping varNow would make this clause false
                    int curNum = numSatisfiedLitsPerClause.get(myClause);
                    if (curNum == 1) {
                        breaks += 1;
                    }
                }
            }
            //evaluate makes and breaks
            int temp = makes - breaks;
            if (temp > res){
                res = temp;
                finalVar = varNow;
            }
        }
        return finalVar; 
    }

    public static void printHashMap() {
        for (HashMap.Entry<Integer, Boolean> entry : truthState.entrySet()) {
            System.out.println(entry.getKey()+" : "+entry.getValue());
        }
    }
}
