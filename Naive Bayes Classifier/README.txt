                                            README
==============================================================================================================
									   Naive Bayes Classifier
==============================================================================================================

GOAL:

Write a naive Bayes text categorization system to predict whether movie reviews are positive or negative. The data for this “sentiment analysis” task were first assembled and published in Bo Pang and Lillian Lee, “A Sentimental Education: Sentiment Analysis Using Subjectivity Summarization Based on Minimum Cuts”, Proceedings of the Association for Computational Linguistics, 2004.

The data in textcat.zip are, when unzipped, organized in the following directory structure:

  train/
  train/neg/
  train/pos/
  dev/
  dev/neg/
  dev/pos/
  test/

Each directory contains some .txt files, with one review per file. For the training and development data, the positive and negative reviews are known, and indicated by the subdirectory names. For the test data, all reviews are mixed together in a single directory.

The text has been pre-tokenized and lower-cased. All you have to do to get the individual terms in a review is to split the tokens by whitespace, a sequence of spaces and/or newlines. You don't need to remove stopwords or do any further stemming. Given the simple smoothing you will implement (see below), you should ignore any terms that occur fewer than 5 times in the combined positive and negative training data.

The main part of this projecy involves implementing a bag-of-words, naive Bayes classifier using 

1. unigram + Laplace smoothing
2. unigram + Removal of special characters (all non-alphabetic characters) + Laplace smoothing
3. Bigram + Laplace smoothing
4. unigram + Jelinek-Mercer (JM) smoothing
5. unigram + Dirichlet (Dir) smoothing

After estimating the parameters of the naive Bayes model using the data in train, you should output your predictions on the files in the test directory.

You should organize your system into two programs, which should be called as follows:

    nbtrain <training-directory> <model-file>
  

This should read all the files in (subdirectories of) the supplied training directory and output the parameters to a model file, whose format is up to you (but see below). Then, you should predict the class of unseen test documents with:

    nbtest <model-file> <test-directory> <predictions-file>
  
==============================================================================================================

Steps for running the program:
==============================================================================================================
1. Install Python if not already installed from https://www.python.org/downloads/  version 2.7.10
2. Unzip the file "Akshaya_Khare_Mohit_Gupta_CS6200_HW6.zip" to get the contents
3. The file 'nbtrain.py' is the file which contains the source code to prepare the model-test file from the given training 
	directory.
	nbtrain <training-directory> <model-file>
   	The file is called as -> 
   	./nbtrain.py <training-directory> <model-file>

   	WHERE: <training-directory> is the path of training-directory
   	        <model-file> is name of the model-file
4. The "model-file.txt" will contain the json dump of two dictionaries containing negative and positive probabilities of all the words except for the words which have frequency less than 5 over the whole collection.
5. The file 'nbtest.py' is the file which contains the source code to classify the given list of documents into either a 
	negative or positive class, using the 'model-file' which was created in 'nbtrain.py'.
	The file is called as -> 
	./nbtest.py <model-file> <input-directory> <predictions-file>

	WHERE: <model-file>  
		   <input-directory> (which contains the folder containing files which need to be classified)
		   <predictions-file>(which contains the file-name and scores  model assigned to the possibilities of positive and negative review)

==============================================================================================================
Results:
==============================================================================================================

A. unigram + add-1 (Laplace) smoothing

1. Naive Bayes classifier using unigram + add-1 (Laplace) smoothing with terms fewer than 5 times ignored

75 % of positive reviews in the development data were correctly classified
80 % of negative reviews in the development data were correctly classified

90 % of reviews in the test data were classified as positive
110 % of reviews in the test data were classified as negative

2. The below requested data is in "A list of the 20 terms with the highest (log).txt"
   A list of the 20 terms with the highest (log) ratio of positive to negative weight.
   A list of the 20 terms with the highest (log) ratio of negative to positive weight.

3. The Prediction file for test and dev have been created separately as mentioned below:
	“Prediction-file-test.txt” contains the predictions for test files
	“Prediction-file-dev.txt” contains the predictions for dev files

NOTE: All the “.txt” files should be opened in Notepad++ or the equivalent editor in Linux machine
In the smoothing folder, the two predictions file have been added a suffix “D” and “JM” to distinguish each smoothing.

B. Other smoothing techniques + features:

NOTE: 
The smoothing techniques are commented out in our code, but can be easily identified and be used, in the folder \Extra-credit\Smoothings.

The following features + smoothing methods were experimented:


1. unigram + Removal of special characters (all non-alphabetic characters) + Laplace smoothing
2. Bigram + Laplace smoothing
3. unigram + Jelinek-Mercer (JM) smoothing
4. unigram + Dirichlet (Dir) smoothing

Accuracy on development data:

1. unigram + Removal of special characters (all non-alphabetic characters) + Laplace smoothing

  79 % of positive reviews in the development data were correctly classified
  77 % of negative reviews in the development data were correctly classified

  94 % of reviews in the test data were classified as positive
  106 % of reviews in the test data were classified as negative

2. Bigram + Laplace smoothing

80 % of positive reviews in the development data were correctly classified
84 % of negative reviews in the development data were correctly classified

89 % of reviews in the test data were classified as positive
111 % of reviews in the test data were classified as negative

3. unigram + Jelinek-Mercer (JM) smoothing

75 % of positive reviews in the development data were correctly classified
80 % of negative reviews in the development data were correctly classified

90 % of reviews in the test data were classified as positive
110 % of reviews in the test data were classified as negative

4. unigram + Dirichlet (Dir) smoothing

69 % of positive reviews in the development data were correctly classified
80 % of negative reviews in the development data were correctly classified

93 % of reviews in the test data were classified as positive
107 % of reviews in the test data were classified as negative

==============================================================================================================
Conclusion:
==============================================================================================================

It is observed that Bigram + Laplace smoothing classifies the development data close to the actual classification of development data compared to other features + smoothing.

The excel file “Smoothing_Graphs.xls” contains the graph which compares the different accuracies for smoothing techniques and other features.

==============================================================================================================
References:
==============================================================================================================
https://web.stanford.edu/class/cs124/lec/naivebayes.pdf -> For implementing normal smoothing and classifier
http://www.ntu.edu.sg/home/gaocong/papers/wpp095-yuan.pdf -> Used to implement Dirichlet and Jelinek-Mercer smoothing


==============================================================================================================