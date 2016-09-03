package assignmentIR01;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class WebCrawlerMain
{
    /**
     * This is our Launcher class for Web Crawler. 
     * It uses WebCrawlerHelper that perform the crawling operations
     * 
     * parameters that can be passed are 
     * http://en.wikipedia.org/wiki/Hugh_of_Saint-Cher
     * concordance
     *            
     */
    public static void main(String[] args)
    {
    	
    	String url="";
    	String keyPhrase="";
        WebCrawlerHelper helper = new WebCrawlerHelper();
        url=args[0];
        if(args.length==1)
        	helper.search(url,"");
        else
        keyPhrase=args[1];
        	helper.search(url,keyPhrase);
    
    }
   
} 