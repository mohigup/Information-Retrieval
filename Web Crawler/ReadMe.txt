										README
==============================================================================================================
				    				WebCrawler 1.0				
==============================================================================================================

The WebCrawler is build in Java and works with the help of 3 Classes:

1. WebCrawlerMain:
This class is the starting point of Crawler that requires 2 arguments at command line

A. First Argument is the SEED URL
B. Second Argument is the Keyphrase ( This is optional in-case the unfocused crawling is required) 

2. WebCrawlerHTMLParser:
This class parses the HTML response of the crawled URLS and href's.

3. WebCrawlerHelper:
This class determines if  the search is focused or unfocused based on the arguments received from WebCrawlerMain.

MAX_PAGES_TO_SEARCH - holds the maximum limit of URLS to be crawled -1000
MAX_DEPTH- holds the maximum depth that can be reached- 5

Also, the results of crawl are stored in the following files:

Focused Crawl:
=================
PagesCrawledUnique_WithKeyPhrase.txt- The relevant crawled URLS that had ‘concordance’ in the HTML response

VisitedPagesWithKeyPhrase.txt- The Total number of URLS that were fetched by Crawler. (LIMIT-1000)

UnFocused Crawl:
================
PagesCrawledUnique_No_KeyPhrase.txt-The Total number of URLS that were fetched by Crawler.

There 2 external libraries used for this project:

A. jsoup-1.8.3- This is used for scraping and parsing HTML from a URL and find, extract data, using DOM traversal for URL's
B. commons-lang3-3.4- This is for using StringUtils.containsIgnoreCase that searches the keyphrase in a URL html response. This  method is faster than conventional String.contains()

==============================================================================================================

==============================================================================================================
HOW TO RUN:
==============================================================================================================

1. The WebCrawler is zipped in WebCrawler.zip. The same can imported as a project in Eclipse.( The Project name is WebCrawler)
2. The 2 libraries that are required to be imported are placed inside the lib directory inside the WebCrawler.zip
3. If there are error seen with the usage of libraries, please have the libraries added in the build path of the project by specifying the path of lib directory.
4. The Crawler can be started by running WebCrawlerMain.java. Before running, please provide the command line arguments in the 'Run Configurations' "Arguments Tab".

The results retrieved for the Crawler are saved in TestResults directory for reference.

==============================================================================================================

==============================================================================================================
Results:
==============================================================================================================

1. The pages crawled when the crawler is run with no keyphrase=1000
2. The relevant pages crawled when the keyphrase is ‘concordance’ until the the total pages crawled reached 1000 limit or depth 5= 56
3. The proportion of the total pages that were retrieved by the focused crawler for ‘concordance’ is 56/1000=0.056

Please note: there were some URLS on which the connection had timed out due to network issues and high contigency. These could affect the proportion mentioned above.

==============================================================================================================


    

