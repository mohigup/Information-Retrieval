package assignmentIR01;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WebCrawlerHTMLParser
{
	/**
     * This class makes Http request and parses the HTML content received. 
     * JSOUP- external library was used for the same.
              
     * 
     */
	private  int maxTries=0;
	private  int maxTries2=0;
     private static final String USER_AGENT ="Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36";
    private List<String> links = new LinkedList<String>();
    private Document htmlDocument;
    String url_crawled;
    Connection connection;
    Document htmlDocumentSearcher;
    /**
     * This method is responsible for crawling the URL received and applying various filters
     *     -
     * 
     */
    public boolean crawl(String url)
    {
        try
        {	url_crawled=url;
        	System.out.println("Connect and Retrievee href");
             connection = Jsoup.connect(url).userAgent(USER_AGENT);
             htmlDocument = connection.get();
            this.htmlDocument = htmlDocument;
            
            if(!connection.response().contentType().contains("text/html"))
            {
                System.out.println("HTML content was not received in response");
                return false;
            }
            Elements linksOnPage = htmlDocument.select("a[href]");
            System.out.println("Found (" + linksOnPage.size() + ") links");
            System.out.println("links size before crawling"+this.links.size());
            for(Element link : linksOnPage)
            {//
            	String tempurl= link.absUrl("href");
            	if(  tempurl.startsWith("https://en.wikipedia.org/wiki/")==false || tempurl.length() - tempurl.replace(":", "").length() >=2 || tempurl.startsWith("http://en.wikipedia.org/wiki/Main_Page") || tempurl.startsWith("https://en.wikipedia.org/wiki/Main_Page"))
                continue;
            	else
            		this.links.add(tempurl);
            }
            maxTries=0;
            
            System.out.println("SIze of filtered URLS is"+this.links.size());
          
            return true;
            
        }
        catch(IOException e)
        {
            
        	System.out.println("Issue in getting URL connection"+e.getMessage());
        	System.out.println("RETRYING again");
        	if(maxTries <=3) 
        		{maxTries=maxTries+1;crawl(url_crawled); 
        		}
        	System.out.println("ALl tries failed with max reached "+maxTries);
        	return false;
        	
        }
   
    }


    /**
     * Performs a search on the body of on the HTML document that is retrieved. .
     *     -
     * 
     */
    public boolean searchForWord(String searchWord, String url)
    {
    	String linkText="",linkHref="";
    	String keyphrase=searchWord;
    	String URL=url;
    	try{
    		  String bodyText;
         System.out.println("URL WHERE HTML IS TO BE RETREIVED IS "+URL);
          htmlDocumentSearcher = Jsoup.connect(URL).userAgent(USER_AGENT).get();
       
        if(htmlDocumentSearcher == null)
        {
            System.out.println("ERROR DOC is null");
            return false;
        }
        System.out.println("Searching for the word " + searchWord);
         bodyText = htmlDocumentSearcher.body().text();
        
      
         maxTries2=0;
        if(StringUtils.containsIgnoreCase(bodyText,keyphrase)){
         System.out.println("keyphrase  founded");
         
        	 return true;
         }
         else {System.out.println("keyphrase search failed");return false;}
      
    	}
    	
    	catch(IOException e)
        {
            System.out.println("IOException observed "+e.getMessage());
            System.out.println("Retrying search");
        	if(maxTries2 <=3) 
        		{ maxTries2=maxTries2+1;searchForWord(keyphrase,URL);
        		}
        	System.out.println("Retry's failed with max as"+maxTries2);
        	return false;
        }
    	
    }

    /**
     * This method returns the list of href found on the URL that was crawled.
     *     -
     * 
     */
    public List<String> getLinks()
    {
        return this.links;
    }

}