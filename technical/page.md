# Lab Report 3

Professor Joe Politz has provided the class with codes, files and directories for our lab through github. I forked the repository to have it on my github account. I then cloned that repository to my local computer and did the different activities as stated in week 4 lab instructions.

The technical/ directory is the sample data that was provided for us to explore how to search through files.

The class was tasked to do two main things:
1. Answer several questions about the dataset by using command-line tools and bash scripts.
2. Write a web server that can respond to queries for files within this directory

## Counting Text Files
The class was asked how many text files (files ending in .txt) are there in the given directory?
> Since my local computer has windows as my OS, I don't have the `find` command installed by default.

> So I first logged in to the school's remote computer using the `ssh` command so I could do the necessary actions to find the answer. 

>Once logged in, I then use the `find` command in my terminal and provided the path as an argument.

        find technical/

 >`find` will list all the files and directories inside the technical directory.

> I then use redirection to store the output of `find` into a text file where I can manipulate it more easily.

        find technical/ > find-result.txt

> The > character above redirects and stores the output of find into a newly created text file called find-result.txt

> And finally, I use the `wc` command to count the lines, words and character in that order of the the text file.

        wc find-result.txt












How many text files are there?

There are a few ways we could do this. Since we’d (eventually) like an answer that works in a script, it would be useful to find a command that does this, rather than, say, counting them by hand or using the line number in a text editor. That leads us to introduce one more command, wc, which stands for “word count”. wc takes a path and prints out some information about that file.

Try this:

wc find-results.txt
You’ll see output that looks something like this:

    1402    1402   54468 find-results.txt
The first is the number of lines in the file. The second is the number of words (wc uses a pretty simple definition of words – strings separated by whitespace; since the paths don’t have spaces, each counts as one word). The third is the number of characters in the file.

Since there’s one line per path, it seems like 1402 is our answer. We used a few commands and concepts to get here:

find «directory-path», which searches (recursively) in a directory for files and lists them all
less «file-path», which helps explore files from the command line
wc «file-path», which counts words in a file
«any-command» > «a-file», which isn’t a command, but we can put after a command to redirect its output to a file
Write down in notes: Show screenshots of using the above commands to get to this answer. Are you sure it’s the right answer? How do you know? Can you see anything that might be inconsistent about that answer when you use less?

 

 

 

 

 

 

 

 

 

 

Turns out this answer (1402) is wrong. You might say it’s only a little bit wrong, but it’s still not right! It’s wrong because find includes all of the directory names as well as the file names. (It would also be wrong if there were non-.txt files in the directory structure – are there any?)

There are a lot of ways we can do this—I encourage you to do a web search for the -name and -type options for find—we will use it as an excuse to introduce one more really cool command: grep.

At its simplest, grep takes a string and a file, and prints out all the lines in that file that match the string. Try:

grep ".txt" find-results.txt
Then, let’s store the results in a file so we can work with them:

grep ".txt" find-results.txt > grep-results.txt
The, use wc to check the line count in this new file (you try that yourself!)

Write down in notes: What’s the actual count of .txt files?

Putting it Into a Script
That’s a lot of exploration at the terminal! It’s useful to also consider how to turn this into a script that prints the answers. Let’s see what that might look like. We can put the commands in a row in a file called count-txts.sh:

find technical > find-results.txt
grep ".txt" find-results.txt > grep-results.txt
wc grep-results.txt
Then we can run it with count-txts.sh.

$ bash count-txts.sh
    1391    1391   54178 grep-results.txt
Write down in notes: Show putting this into a script and running it to get this answer.

Sometimes it’s useful to parameterize a script with command line arguments. Make it so this script takes the name of the directory to traverse as the first command-line argument, so you use it like this instead:

bash count-txts.sh technical
Then, use it to count the number of files in the subdirectories for biomed and plos.

Write down in notes: How many files are in those directories?

Write down in notes: What happens to the find-results.txt and grep-results.txt files when you run the script? What are some consequences of that for where you should be careful when using output redirection?

Counting Sizes of Text Files
Here’s another question that would be nice to answer: How many total words are in the files in technical/biomed?

For this, it would be nice to be able to use wc on all the files in that directory. wc can take multiple filenames. For example, we could give two paths, and wc will tell us the number of lines, words, and characters in each:

$ wc technical/biomed/1468-6708-3-1.txt technical/biomed/1468-6708-3-3.txt 
     432    3380   24112 technical/biomed/1468-6708-3-1.txt
     296    2166   16882 technical/biomed/1468-6708-3-3.txt
     728    5546   40994 total
We can use a * pattern to make wc work on all the files in that directory:

$ wc technical/biomed/*.txt
     432    3380   24112 technical/biomed/1468-6708-3-1.txt
     296    2166   16882 technical/biomed/1468-6708-3-3.txt
     547    4301   31378 technical/biomed/1468-6708-3-4.txt
     317    2312   18114 technical/biomed/1468-6708-3-7.txt
     533    3630   29585 technical/biomed/1468-6708-3-10.txt
     ... lots of lines! ...
  490673 3437323 26328271 total
Here we have our answer – 3437323. That’s a lot of words!

Write down in notes How many total words are in technical/plos? How many total characters?

Another related question we might want to answer is which file in technical/biomend has the most lines? If wc reported the files’ counts in order, we could simply read off the first or last one. But we can see in the output above that there is no particular ordering relative to line, word, or character counts in the output.

There’s another command that’s great for many situations like this: sort. That’s right – there’s a sorting command built-in! sort takes a file and prints out the lines in that file in sorted string order. The way wc is designed, this ends up exactly matching a sort based on line number!

Let’s try it:

$ wc technical/biomed/*.txt > biomed-sizes.txt
$ sort biomed-sizes.txt
... a bunch of lines ...
    1656   12212   89104 technical/biomed/1472-6904-2-5.txt
    1773   10309   83990 technical/biomed/gb-2002-3-12-research0086.txt
    1803    8968   73428 technical/biomed/gb-2002-3-7-research0036.txt
    2236    9393   78562 technical/biomed/1471-2105-3-18.txt
    2359   17408  136424 technical/biomed/1471-2105-3-2.txt
  490673 3437323 26328271 total
The last file output has 2359 lines, and it’s technical/biomed/1471-2105-3-2.txt.

Write down in notes: What is the article in that file about?

Write down in notes: Answer the following questions using grep, find, ** patterns, > redirection, wc, and sort:

What is the file with the fewest words in technical/plos? What are the first few lines of that file? (Hint: the line count comes first. You can make wc report just the word count with the -w option)
What is the file with the most characters in either technical/plos or technical/biomed? What are the first few lines of that file? (Hint: try the -c option to wc)
How many lines in technical/plos contain the string "base pair"? What about in technical/biomed? (Hint: look up the -l option to grep)
How many files in technical/plos contain the string "base pair"? What about in technical/biomed? (Hint: look up the -l option to grep)
Copy the commands you used to get these answers along with the answers themselves! You can make scripts out of them (especially if they needed multiple commands).

Discuss: What other interesting questions can you answer with what you know?

Write down in notes: What’s a question you want to answer, but aren’t sure how to answer about these files with the commands you have? Maybe someone in your group or your lab tutor would have good guesses!

A Search Server
The repository also has a file DocSearchServer.java, which has a (fixed) version of getFiles from last week’s lab.

The handleRequest method is left for you to fill in.

First:

Add a file called DocSearchTest.java and add a unit test to it that demonstrates the current behavior of handleURL (which just reports that no path works yet, because you haven’t implemented it yet)
Write start.sh and test.sh scripts as we did in lecture, and make sure they start the server and run the tests, respectively.
Then, you should make it so the following URL paths have the described behavior:

/ prints "There are NNNN files to search" where NNNN is the total number of files returned by getFiles
/search?q=search-term prints "There were NNNN files found:" and then a list of all the paths of files that contain that search term. For example, if the search term is base pair it should print the same paths you found in your search above.
Add a few tests that give meaningful search results (you can use some of the ideas from using grep above), and take some screenshots of the working server loaded from a browser.

Write down in notes: How long did it take you to make the scripts? Now that you’ve made them how long does it take you to run the tests and start the server? Was that an overall savings on your time? What if we run the tests and server 100 more times this quarter, will it be worth it?

Push to Github - The scripts you added, new test file, and updates to DocSearchServer.java
