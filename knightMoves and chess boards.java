import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Scanner;

public class knightMoves {
	

	final static char[][] board = {
            {'a','b','c','d','e'},
            {'f','g','h','i','j'},
            {'k','l','m','n','o'},
            {'p','q','r','s','t'},
            {'u','v','w','x','y'}           
    };
    
    final static int[][] invalidMoves = {
            {0,3},
            {1,0},
            {4,2}, 
            {4,3}
    };
    
    final static char[] vowelsList = {'a','e','i','o','u'};
    
    final static Map<Character, List<int[]>> possibleMoves = getAllPossibleMoves(board);  // all possible moves of a knight on the given board
    
    static Map<Character, Map<Integer, List<String>>> dataList;
    
    static {
        dataList = new HashMap<>();
        for(char c = 'a'; c <='z'; c++ ) {
            dataList.put(c, new HashMap<Integer,List<String> >());
        }
    }
    
    /*
     * 
     * dataList  = { character_1 :{ number_1 : [string1,string2,...] ,
                            number_2: [...]                             },
               		character_2 : {....}
             		}
  					character_n :  0 < n < 27
  					number_n    :  0 < n < virtually no limit but depends on the input
  					string_n     :  0 < n < virtually no limit but depends on the input (length of string n is independent of number_n) 
     * 
     * 
     * possibleMoves = { character_1 : list_1[ sublist_1[], sublist_2[], ... ] 
     * 					 character_2 : list_1[ sublist_1[], sublist_2[], ... ]
     * 				   }
     * 				   character_n :  0 < n < 27
     * 				   list_n :  n = 1
     * 				   sublist_n : 2 < n < 7	(specifically for the board given)
     */
    
    
    static int countvowels(String string) {
        int num_vowels = 0;
        for(char c : string.toCharArray()) {
            if("aeiouAEIOU".indexOf(c) != -1) {
                ++num_vowels;
            }
        }
        return num_vowels;
    }
    
    static char getBoardLetterByCoordinates(char[][] board, int[] xyTuple) {
        int x = xyTuple[0] < 0 ? board.length - xyTuple[0] : xyTuple[0];
        int y = xyTuple[1] < 0 ? board[x].length - xyTuple[1] : xyTuple[1];
        return board[x][y];
    }
    
    static List<int[]> product(int[] a, int[] b) {
        List<int[]> ret = new ArrayList<>();
        for(int _a : a) {
            for(int _b : b) {
                ret.add(new int[] {_a, _b});
            }
        }
        return ret;
    }
    
    static boolean isInInvalidMoves(int[] move) {
        for(int[] invalidMove : invalidMoves) {
            if(invalidMove[0] == move[0] && invalidMove[1] == move[1]) {
                return true;
            }
        }
        return false;
    }

    /*
     * Function : getAllPossibleMoves()
		@params:
	    board : char[][] : board given in the task.

    @returns:
    returns a map/dictionary for each key/character/position in the board...from where a knight can move.
    
    
    function description: using the board and possible moves of a knight logic (x-1,y+2) and etc....creates a dictionary like structure which stores all the possible moves for a knight.
    It also considers invalid moves, so no invalid move (empty cell) is not added in this matrix and hence we wont have to check for invalid moves later.
     * */
    
    static Map<Character, List<int[]>> getAllPossibleMoves(char[][] board) {
        Map<Character, List<int[]>> possibleMoves = new HashMap<>();
        for(int eachRowIndex = 0; eachRowIndex < board.length; ++eachRowIndex) {
            char[] eachRow = board[eachRowIndex];
            
            for(int eachRowColumnIndex = 0; eachRowColumnIndex < eachRow.length; ++eachRowColumnIndex) {
                char eachRowColumn = eachRow[eachRowColumnIndex];
                
                int x = eachRowIndex;
                int y = eachRowColumnIndex;
                
                if(!isInInvalidMoves(new int[] {x, y})) {
                    List<int[]> moves = product(new int[]{x-1,x+1}, new int[]{y-2,y+2});
                    moves.addAll(product(new int[]{x-2,x+2}, new int[]{y-1,y+1}));
                    
                    moves = moves.stream().filter((t) -> t[0] >= 0 && t[1] >= 0 && t[0] < 5 && t[1] < 5 &&!isInInvalidMoves(t)).collect(Collectors.toList());
                    
                    possibleMoves.put(eachRowColumn, moves);
                }
            }
        }
        return possibleMoves;
    }
    
/*
 * 	Function : CalculateKnightMoves()
    @params:
    localN : int : n value which represents the number of number of jumps - 1 remaining
    key : character : character to work on

    @returns:
    list of strings : lsit of possible moves with the given key and localN

    Function description: takes the key and n value. and check if n is 1. if it is then just return  the key because we cant move anywhere else now. 
    if its not 1 then we have to move n times towards each direction. 
    so in the n>1 condition we iterate over all possible moves of the key from argument. 
    we get the tuple from possibleMoves so we get the letter from function getBoardLetterByCoordinates.
    Then we check in dataList global variable function if we have the data stored for the key and n-1 (will come back to what we store in dataList).
    if there is an entry then just get this list and append it to scy (strings covered yet). dataList returns all the possible strings under key and n values as 1 dimension list.
    if not then send eachKPM to same function again but with n-1 value. this will recursively get all the possible strings and return the list of strings. 
    we then add this to scy. we then flatten the list because the list returned is nested upon nested upon.....
    when we have covered all the possible moves of the key then we flatten once again because this time its nested len(possibleMoves[key]) times. 
    scy now represents a 1-dim list of strings. but each element in the scy is without the key letter at its start because thats how algorithm works.
    each function just computes and returns the strings ahead. so when we have all scy then we have to add the key letter to each scy.
    so we do that but before we do that we also have to reject the strings with numebr of vowels >2 so we do that too. 
    Finally when we have this 1-dim-without-vowel list then we save it in dataList [key] [localN] as list. so next time we wont have to caculate it again.
    we also return this list of strings.

*/

    static List<String> calculateKnightMoves(int localN, char key) {
        if(localN == 0) {																// corner case
            return new ArrayList<>();       
        }
        
        if(localN == 1) { 																// base condition
            List<String> ret = new ArrayList<>();			
            ret.add("" + key);
            return ret;
        }
        // n>1 then iterate over all possibilities
        List<String> scy = new ArrayList<>();											// scy = string covered yet
        
        for(int[] eachKPM : possibleMoves.get(key)) {									// each KPM is a tuple. representing the position in the board. will be converted to letter later
            
        	int temp = localN - 1;														// have to create one to search in dataList keys
            char eachKPMLetter = getBoardLetterByCoordinates(board, eachKPM);			// get the letter from the tuple using this function
            
            List<String> returnedStringList;
            
            if(dataList.get(eachKPMLetter).containsKey(temp)) {							// if this eachKPMLetter is alrady calculated then its sub list of strings will be in dataList. if not then go else
                returnedStringList = dataList.get(eachKPMLetter).get(temp);
            }
            else {
                returnedStringList = calculateKnightMoves(localN - 1, eachKPMLetter);	//recUr$ive caLl!ng. with 1 less n (because now it should jump one less than the current key)
            }
            
            scy.addAll(returnedStringList);
        }
        
        List<String> scy2 = new ArrayList<>();											// iterate over every item (a string) of scy and append the key to it and if number of vowels dont exceed 2 then add it to a new list.
        
        for(String eachScy : scy) {
            String tempAppend = "" + key + eachScy;
            if(countvowels(tempAppend) <= 2) {
                scy2.add(tempAppend);
            }
        }
        
        dataList.get(key).put(localN, scy2);											// save this flattened-1-dim-without-vowel list for later use above.					

        return scy2;
    }
    
    
    public static void main(String[] args) {

    	Scanner sc = new Scanner(System.in);
    	System.out.println("Input: n = ");
    	int n = sc.nextInt();
    	System.out.println("...Wait...");
    	
//        for(int n = 1; n < 12; ++n) {		// uncomment this and the ending bracket to run from n = 1 to 12 (any value). might take time foor bigger values
            long startTime = System.currentTimeMillis() / 1000L;
            int totalSum = 0;
            for(Character eachKey : possibleMoves.keySet()) {
            	Character eachKey_lower = Character.toLowerCase(eachKey); 
                totalSum += calculateKnightMoves(n, eachKey_lower).size();
            }
            long endTime = System.currentTimeMillis() / 1000L;
            System.out.println("For n = " + n + " : totalSum = "+ totalSum + " : time taken = " + (endTime - startTime) + " seconds");
//        }
          sc.close();
    }

}
