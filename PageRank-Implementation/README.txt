									README
==============================================================================================================
				    			PageRank 1.0				
==============================================================================================================

The solution to the problem is build in python.
There are 2 .py files:

1. PageRank.py -calculates PageRank for 6 node directed graph
2. PageRankWithPerplexity.py -calculates PageRank for wt2g_inlinks.txt

==============================================================================================================
PageRank.py
==============================================================================================================

It requires the path of dataset against which PageRank Algorithm will be computed.
The required path is asked from the user at command prompt.
There is one function main() that drives the entire computation.

main()
It builds the following from the 6 vertices graph and then calls pageRank() function:

dictionary of Sources and their inlinks-GRAPH
dictionary of Sources and their count of outlinks-L
set of all nodes -P
set of sink nodes-S

pageRank()
It calculates the PR (PageRank) for each of the values in P and prints the PageRank for Iteration 1, 10, 100.
The output is placed in the following file:

"PageRank-6-Vertices.txt"

==============================================================================================================
PageRankWithPerplexity.py
==============================================================================================================

It requires the path of dataset (wt2g_inlinks.txt) against which PageRank Algorithm will be computed.
The required path is asked from the user at command prompt.
There is one function main() that drives the entire computation.

main()
It builds the following from the 6 vertices graph and then calls pageRank() function:

dictionary of Sources and their inlinks-GRAPH
dictionary of Sources and their count of outlinks-L
set of all nodes -P
set of sink nodes-S

pageRank()

1. calculates the page-rank of all the pages in P until converagence is reached.

2. computes the following and writes in "PerplexityValuesAfterConvergence.txt":

-> a list of the perplexity values obtained in each round until convergence

3. computes the following and writes in "Misc Statistics.txt"

-> a list of the document IDs of the top 50 pages as sorted by PageRank, together with their PageRank values;
-> a list of the document IDs of the top 50 pages by in-link count, together with their in-link counts;
-> the proportion of pages with no in-links (sources);
-> the proportion of pages with no out-links (sinks); and
-> the proportion of pages whose PageRank is less than their initial, uniform value

4. computes the following and writes in "All_Pages_PageRanks.txt" 

5. There are 2 sub-functions called within pageRank

calPreplexity()
It calculates the new perplexity

isConvergence()
It decides if convergence has reached or not.
a List named perplexityL is build inside this function that keeps track of last 4 perplexity values.

==============================================================================================================

==============================================================================================================
HOW TO RUN:
==============================================================================================================

1.For 6 node directed graph as input ,run following at the command prompt and enter file path when prompted

python PageRank.py

A. Below is example of the prompt that will be seen and sample value for file path

Please enter file path without space after colon:D:/NEU/IR/directed_graph.txt

Note- The script execution time is less than 8 seconds.

2.For wt2g_inlinks.txt as input run following at the command prompt and enter file path when prompted

python PageRankWithPerplexity.py

A. elow is example of the prompt that will be seen and sample value for file path

Please enter file path without space after colon:D:/NEU/IR/wt2g_inlinks.txt

Note- The script execution time is between 1.5 to 2.5 mins.
==============================================================================================================

==============================================================================================================
Results:
==============================================================================================================

A. For 6 node directed graph

PageRank-6-Vertices.txt

B. For wt2g_inlinks.txt

PerplexityValuesAfterConvergence.txt
Misc Statistics.txt
All_Pages_PageRanks.txt

C. The analysis of the PageRank results is provided in the "Analysis of the PageRank results.doc"

==============================================================================================================
==============================================================================================================

