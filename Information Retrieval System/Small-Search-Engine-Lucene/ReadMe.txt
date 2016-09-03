											README
==============================================================================================================
								Small-Search-Engine-Lucene
==============================================================================================================

Goal:

You will need to go through Lucene’s documentation (and the provided code) to perform the following:

1. Index the raw (unpre-processed) CACM corpus http://www.search-engines-book.com/collections/ using Lucene. Make sure to use “SimpleAnalyzer” as your analyzer.

2. Build a list of (unique term, term_frequency) pairs over the entire collection. Sort by frequency.

3. Plot the Zipfian curve based on the list generated in (2) (you do not need Lucene to perform this. You may use any platform/software of your choosing to generate the plot)

4. Perform search for the queries below. You need to return the top 100 results for each query. Use the default document scoring/ranking function provided by Lucene.

portable operating systems

code optimization for space efficiency

parallel algorithms

parallel processor in information retrieval

5.  Compare the total number of matches in 4 to that obtained in Small-Search-Engine-BM25 project.

==============================================================================================================

The Solution is build in Java and requires JDK 1.7
Below is main functionlity of the code:

1. Prompt user to get where the index will be created and path of CACM corpus
2. Index is build using Lucene 4.7.2 and HTML tags removed using Jsoup
3. The Term and its frequency is stored in a Map termfq which is sorted by frequency and stored in    sortedtermfq that is a List<Entry<String, Integer>>
4. For each of the queries, the documents containing query terms are retreived and saved in org.apache.lucene.document.Document. These documents are used to fetch the content and Doc ID's.
5. Two lists are maintained that are send as arguments to org.knowm.xchart.Chart which is used to build Zipfian Curves

termprob- is list of probability of each of the index term ((frequency/Total word occurrences))
termrank- is list of rank of each of the index term

==============================================================================================================

HOW TO RUN:

1. The SmallSearchEngineLucene is zipped in SmallSearchEngineLucene.zip. The same can imported as a project in Eclipse.( The Project name is SmallSearchEngineLucene)
2. The 2 libraries that are required to be imported are placed inside the lib directory inside the SmallSearchEngineLucene

jsoup-1.8.3.jar - It is used to parse the html content
xchart-2.6.0.jar - It is used to build Zipfian curve

3. If there are error seen with the usage of libraries, please have the libraries added in the build path of the project by specifying the path of lib directory.
4. The SmallSearchEngineLucene can be started by running HW4.java. 

==============================================================================================================
Results:

1. A sorted (by frequency) list of (term, term_freq pairs) is stored in term-frequency.txt
2. A plot of the resulting Zipfian curve is stored as 

Zipfian curve.png
Zipfian curve-Logarthmic.png

3. Four lists (one per query) each containing at MOST 100 docIDs ranked by score is stored in 
q1_results.txt
q2_results.txt
q3_results.txt
q4_results.txt

optional: provide a text snippet of 200 chars along the DocID for each of the 4 queries
The results is stored in "MOST 100 docIDs ranked by score with text snippet.txt"

4.table comparing the total number of documents retrieved per query using Lucene’s scoring function vs. using your search engine (index with BM25) from the previous project "Small-Search-Engine-BM25" is stored in 

Comparison_of_results_from_Lucene_and_BM25.odt

The total number of word occurences in the collection is 220757.

==============================================================================================================
    

