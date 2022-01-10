# Knights-and-Chess-Boards
Finding n possible strings by knights valid movements

Given an integer n , return how many distinct strings can be produced by a knight moving from
square to square for n times. Refer to the pdf file for further details.

A task given to me for a position as a software engineer. I had the option of doing it on java, c or c++. But since I find python better for making logics, I decided to code it up in both languages (I mean why not, its not like i have bunch of other assignments with tomorrow morning's deadline....fun fact: i have). 

Both languages have the same logic/variable names/ function names/ code comments


The code is well commented and i think its self explanatory.
I tried to make it as efficient as possible given the time constraint (not for the task, but cuz assigNment$s$s$ .

I "think" java ran faster than python (which is understandable) but I was running python code in colab so i am not sure.

Also, even though i used dynamic programming, i think i could still make it faster and memory efficient-er. By:

1) I saved strings in the dp table (dataList)...I had to, because i wanted to find the vowels count later (nested stuff)...think of it like this. if 'a' with n=3 has 2 vowels (in case of 'aha') .. this 'aha' is acceptable. but if i am doing 'a' with n=5...when this reaches n=3 and back to 'a' by 'aha'... instead of calculating 'a' with n=3 again. i have 'aha' already eaved from previous 'a' with n=3  so i dont need to calculate all this again. but this time i already have one 'a' and so if I add 'aha'...this string shouldnt be acceptable (vowels >2).  Thats why i had to store previously covered strings..............But i can go around this by storing only the number of vowels in each string. so datalist wont have string list, it will have int list where int will represent the vowels count...so when i reach from 'a' with n=5 to 'a' with =3....I just add the number of vowels in current string and then add it with vowels from the dataList table......I "think" I did a pretty similar thing when trying different logics out....i am not sure though (was too sleepy in the morning so i just thought of a new logic).

I cant remeber the 2nd approach. i had it mind yesterday but cant remember it now. 
