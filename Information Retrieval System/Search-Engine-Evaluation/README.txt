                                            README
==============================================================================================================
									                 Search-Engine-Evaluation
==============================================================================================================
Goal:

To evaluate retrieval effectiveness using following metrics for Small-Search-Engine-BM25 project.

1- Precision

2- Recall

3- P@K, where K = 20

4- Normalized Discounted Cumulative Gain (NDCG)

5- Mean Average Precision (MAP)

==============================================================================================================

==============================================================================================================
IMPLEMENTATION DETAILS:
==============================================================================================================

The solution to the problem is build in python using previous project- Small-Search-Engine-BM25.
There is one .py file that drives the requirements of the problem.
bm25.py 

index.out -contains index for document collection provided in the file tccorpus.txt

==============================================================================================================
bm25.py
==============================================================================================================

1.The document ID's that have relevance as 1 and were found for each of the following 3 queries are stored as dict()
  relevant_docs

  portable operating systems (12)
  code optimization for space efficiency (13)
  parallel algorithms (19)

2. callDCG and callIDCG compute the value of DCG , IDCG
3. retrieved_docs_sofar holds the retrieved documents as the precision is calculated for top 100 documents for each query.

 
==============================================================================================================													HOW TO RUN:
==============================================================================================================

1)  Enter the following command to compute various metrics for the provided queries.txt and index.out

./bm25.py index.out queries.txt 100 > results.eval

Note:The output from this run, with the top 100 document IDs and and the following requested metrics:
    Rank
    Document ID
    Document score
    Relevance level
    Precision
    Recall
    NDCG

==============================================================================================================
Results:
==============================================================================================================

The metrics requested for the provided 3 queries are stored in "Results_Table.xlsx" with each tab representing
details for the query. 
The remaining requested values are as follows:

 Values of P@20 
 portable operating systems (12) - .15
 code optimization for space efficiency (13)- .25
 parallel algorithms (19) - .45

 MAP-Mean Average Precision -0.41467527137