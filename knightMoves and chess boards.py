from itertools import product
from time import time
import string


#@title flattens the nested list function
def flatten(L):
  for l in L:
    if isinstance(l, list):
      yield from flatten(l)
    else:
      yield l

#@title countvowels in a string function
def countvowels(string):
  num_vowels = 0
  for char in string:
    if char in "aeiouAEIOU":
      num_vowels = num_vowels + 1
  return num_vowels

#@title getBoardLetterByCoordinates function : given board coordinates, gives you a letter
def getBoardLetterByCoordinates(board, xyTuple):
  return board[ xyTuple[0] ] [ xyTuple[1] ]

#@title getAllPossibleMoves function : returns all possible moves of a knight. returns a dictionary { [()] }
'''
@params:
    board : char[][] : board given in the task.
@returns:
    returns a map/dictionary for each key/character/position in the board...from where a knight can move.

function description: using the board and possible moves of a knight logic (x-1,y+2) and etc....
creates a dictionary like structure which stores all the possible moves for a knight.
It also considers invalid moves, so no invalid move (empty cell) is not added in this matrix,
and hence we wont have to check for invalid moves later.
'''
def getAllPossibleMoves(board):
  possibleMoves = {}
  for eachRowIndex,eachRow in enumerate(board):
    for eachRowColumnIndex,eachRowColumn in enumerate(eachRow):
      x = eachRowIndex
      y = eachRowColumnIndex
      if (x,y) not in invalidMoves:

        moves = list(product([x-1, x+1],[y-2, y+2])) + list(product([x-2,x+2],[y-1,y+1]))
        moves = [(x,y) for x,y in moves if x >= 0 and y >= 0 and x < 5 and y < 5]
        moves = [i for i in moves if i not in invalidMoves]
        possibleMoves[eachRowColumn] = moves
  return possibleMoves


# @title calculateKnightMoves function { form-width: "21%" }
'''

@params:
localN : int : n value which represents the number of number of jumps - 1 remaining
key : character : character to work on

@returns:
list of strings : lsit of possible moves with the given key and localN

takes the key and n value. and check if n is 1. if it is then just return  the key because we cant move anywhere else now. 
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

'''
def calculateKnightMoves(localN, key):
  if localN == 0:  # corner case
    return []
  elif localN == 1:  # base condition
    return [key]

  else:  # n>1 then iterate over all possibilities
    scy = []  # scy = string covered yet
    for eachKPM in possibleMoves[
      key]:  # each KPM is a tuple. representing the position in the board. will be converted to letter later

      temp = localN - 1  # have to create one to search in dict keys
      eachKPMLetter = getBoardLetterByCoordinates(board, eachKPM)  # get the letter from the tuple using this function

      if temp in dataList[
        eachKPMLetter]:  # if this eachKPMLetter is alrady calculated then its sub list of strings will be in dataList. if not then go else
        returnedStringList = dataList[eachKPMLetter][temp]
      else:
        returnedStringList = calculateKnightMoves(localN - 1,
                                                  eachKPMLetter)  # recUr$ive caLl!ng baby. with 1 less n (because now it should jump one less than the current key)
        returnedStringList = list((flatten(returnedStringList)))  # too complex. flatten it.

      scy.append(returnedStringList)  # to concat all the possible strings of key with n value = localN
      del returnedStringList  # delete this variable because its a big one and there is no use of it anymore.

    scy = list((flatten(
      scy)))  # too complex. to be exact len(possibleMoves[key]) times. because each time the list is appended. flatten it. scy is now a list of strings

    # scy = list(filter(None, scy))                                     #no need for it as no empty string is returned now. from previous implementation

    scy2 = []
    for eachScy in scy:  # iterate over every item (a string) of scy and append the key to it and if number of vowels dont exceed 2 then add it to a new list.
      tempAppend = key + eachScy
      if countvowels(tempAppend) <= 2:
        scy2.append(key + eachScy)
    del scy  # delete this variable because its a big one and there is no use of it anymore.

    dataList[key][localN] = scy2  # save this flattened-1-dim-without-vowel list for later use above.

    return scy2



#@title initializing board, invalidMoves and vowelsList
board = [ ['a','b','c','d','e'],
          ['f','g','h','i','j'],
          ['k','l','m','n','o'],
          ['p','q','r','s','t'],
          ['u','v','w','x','y']
        ]

invalidMoves = [ (0,3), (1,0), (4,2), (4,3) ]
vowelsList = ["a","e","i","o","u"]

dataList = {k: {} for k in list(string.ascii_lowercase)}
# dataList  = { character_1 :{ number_1 : [string1,string2,...] ,
#                             number_2: [...]
#                             },
#               character_2 : {....}
#             }
#  character_n :  0 < n < 27
#  number_n    :  0 < n < no limit
#  string_n     :  0 < n < no limit (length of string n is independent of number_n)

possibleMoves = getAllPossibleMoves(board)
#possibleMoves = { character_1 : list_1[ sublist_1[], sublist_2[], ... ]
# 			      character_2 : list_1[ sublist_1[], sublist_2[], ... ]
# 				}
#      				   character_n :  0 < n < 27
#      				   list_n :  n = 1
#      				   sublist_n : 2 < n < 7	(specifically for the board given)


n = int(input("Input: n = "))
startTime = time()
totalSum = 0
print("...Wait...")
for eachKey in possibleMoves:
  totalSum += len(calculateKnightMoves(n, eachKey))
endTime = time()
print("For n = ", n, " : totalSum = ", totalSum, " : time taken = ", endTime - startTime)

'''
for n in range(1, 12):
  startTime = time()
  totalSum = 0
  for eachKey in possibleMoves:
    totalSum += len(calculateKnightMoves(n, eachKey))
  endTime = time()
  print("For n = ", n, " : totalSum = ", totalSum, " : time taken = ", endTime - startTime)
'''