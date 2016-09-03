package assignmentIR01;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;


public class WebCrawlerHelper
{ 
	 static final int MAX_PAGES_TO_SEARCH = 1000;
	 static final int MAX_DEPTH = 5;
	 List<String> pagesToCrawlUnique = new LinkedList<String>();
	 List<String> pagesToCrawlUnique_URLS = new LinkedList<String>();
	 List<String> pagesCrawledWithoutKeyPhrase = new LinkedList<String>();
	 List<String> pagesWithKeyPhrase= new LinkedList<String>();
	 List<String> tempList = new LinkedList<String>();
	 int depth=1;
	String nextUrl,nextUrlad,temp,temp2,keyPhrase;
	Integer counter=0;
	Boolean unfocusedCrawl;
	int next_counter, saved_counter=0;



	/**
	 * This method decides whether the crawling is with a keyphrase or without.
	 * 
	 * 
	 */
	public void search(String url, String searchWord)
	{   
		keyPhrase=searchWord;
		if(searchWord.length()!=0) {
			// Comment/UnComment this line for search until 1000 URLS with concordance
			tempList=this.pagesToCrawlUnique; System.out.println("focused keyword search");  
			//Comment/UnComment this line for search until 1000 relevant URLS with concordance
			//tempList =this.pagesWithKeyPhrase; System.out.println("focused keyword search");  
			unfocusedCrawl=false;
			
		}
		else {
			tempList =this.pagesCrawledWithoutKeyPhrase;
			System.out.println("unfocused search");
			unfocusedCrawl=true;
		}
		while(tempList.size() <= MAX_PAGES_TO_SEARCH )
		{   

			System.out.println("DEPTH IS "+depth);
			String currentUrl;
			WebCrawlerHTMLParser leg = new WebCrawlerHTMLParser();
			if(this.pagesToCrawlUnique.isEmpty())
			{
				currentUrl =  url;

				this.pagesToCrawlUnique.add(depth + " "+ url.replace("http", "https"));
				
				
				System.out.println("SEED URL IS "+this.pagesToCrawlUnique);	


			}
			else
			{  



				if ((tempList.size() <= MAX_PAGES_TO_SEARCH && searchWord.length()!=0) )
				{   
					System.out.println("KEYPHRASE SEARCH");
					

					try{
						temp=this.pagesToCrawlUnique.get(counter).split(" ")[1];
						temp2=this.pagesToCrawlUnique.get(counter);}
					catch(IndexOutOfBoundsException e){
						System.out.println("All elements are fetched");
						break;}
					
					System.out.println("Crawling starting now. TimeStamp is "+ new Date( ) + "\n"); 
					if(counter==0) next_counter=counter+1;
					else {
						next_counter=saved_counter;
						

					}
					if(searchforKeyPhrase(leg,searchWord,temp)){
						if(tempList.size()==MAX_PAGES_TO_SEARCH){
							System.out.println("breaking as tempList.size() reached"+tempList.size());
							break;}
						System.out.println("BUILDING UNIQUE LIST");
						pagesToCrawl(leg);

						
						
						while(next_counter<this.pagesToCrawlUnique.size() ){
							if(tempList.size()>MAX_PAGES_TO_SEARCH) break;
							System.out.println("scanning element at "+next_counter);
							System.out.println("LIST SIZE "+this.pagesToCrawlUnique.size());
							System.out.println(" URL IS  ----"+this.pagesToCrawlUnique.get(next_counter));
							System.out.println("NEXT URL IS"+this.pagesToCrawlUnique.get(next_counter).split(" ")[1]);
							searchforKeyPhrase(leg,searchWord,this.pagesToCrawlUnique.get(next_counter).split(" ")[1]);
							next_counter=next_counter+1;
							saved_counter=next_counter;}
						System.out.println("searched all items, next iteration starts with counter value "+saved_counter);

						
					}
					else {
						if(tempList.size()==MAX_PAGES_TO_SEARCH)break;
						System.out.println("no match counter incremented");
						
						counter=counter+1;}

				}

				if((tempList.size() <= MAX_PAGES_TO_SEARCH && searchWord.length()==0) ){
					if ((tempList.size() == MAX_PAGES_TO_SEARCH)) break;
					System.out.println("NO KEYPHRASE SEARCH");
					
					temp=this.pagesToCrawlUnique.get(counter).split(" ")[1];
					System.out.println("the element of pagesToCrawlUnique at "+counter+"position is"+temp);
					System.out.println("Crawling starting now. TimeStamp is "+ new Date( ) + "\n");

					pagesToCrawl(leg);

				}

			}


			System.out.println("\n**Done** Count of pages in pagesToCrawlUnique is " + this.pagesToCrawlUnique.size() + " web page(s)");
			System.out.println("\n**Done** Count of pages in pagesWithKeyPhrase is " + this.pagesWithKeyPhrase.size() + " web page(s)");
			System.out.println("\n**Done** Count of pages in pagesCrawledWithoutKeyPhrase is " + this.pagesCrawledWithoutKeyPhrase.size() + " web page(s)");



			if (depth > MAX_DEPTH)break;
		}
		

		System.out.println("writing to file ");
		writeToTotalPagesUnique();
		System.out.println("writing to file ");
		writeToFileWithKeyPhrase();
		System.exit(0);

	}

	/**
	 * This methods is responsible for building list of urls to be crawled with their depth
	 * It also controls that only unique URL's are added in the list of URLS to be crawled.
	 * 
	 *
	 */


	private void pagesToCrawl(WebCrawlerHTMLParser leg){

		try{
			String newURL="";
			String newURLL="";
			// CRAWLING URL 
			WebCrawlerHTMLParser leg1=leg;
			System.out.println("Sleeping for 1 second");
			Thread.sleep(1000);
			leg.crawl(temp);
			if(keyPhrase.length()==0)this.pagesCrawledWithoutKeyPhrase.add(temp);
			// ELEMENTS IN URLS TO BE CRAWLED
			



			// DEFINING THE DEPTH OF NEW URLS GOT
			depth=Integer.parseInt(this.pagesToCrawlUnique.get(counter).split(" ")[0]) + 1;
			counter= counter + 1;  
			//depth= depth +1;
			System.out.println("depth is "+depth);
			System.out.println("counter is "+counter);
			System.out.println("this.pagesToCrawlUnique.size() is "+this.pagesToCrawlUnique.size());

			for(int i=0; i< leg.getLinks().size();i++){
				if(unfocusedCrawl && this.pagesToCrawlUnique.size()==MAX_PAGES_TO_SEARCH) 
				{System.out.println("Not adding elements as the BaseList is already reached 1000 ");break;}

				if(depth > MAX_DEPTH && counter != this.pagesToCrawlUnique.size()) {
					System.out.println("reached the limit of depth for "+temp);
					depth=Integer.parseInt((this.pagesToCrawlUnique.get(counter)).split(" ")[0]);
					System.out.println("changed depth to that of next element in pagesToCrawlUnique. So depth is "+depth);
					break;
				}
				if(depth > MAX_DEPTH && counter == this.pagesToCrawlUnique.size()){
					System.out.println("scanned all elements of pagesToCrawlUnique and depth 5 reached as well ");
					break;
				}

				newURL = leg.getLinks().get(i);
				newURLL= depth + " "+ newURL;
				if(i==2692) System.out.println("newURL"+newURL+" and newURLL"+newURLL);
				if(unfocusedCrawl==false && tempList.size()==MAX_PAGES_TO_SEARCH ){
					System.out.println("Reached MAX limit of "+MAX_PAGES_TO_SEARCH+" no more elements will be added"); break;
				}
				if(containsCaseInsensitive(newURL,this.pagesToCrawlUnique) == false){
					if(newURL.contains("#") == true){
						
						if(containsCaseInsensitive(newURL.split("#")[0],this.pagesToCrawlUnique) == false){
							
							this.pagesToCrawlUnique.add(newURLL);
							
						}
						else continue;


					}
					else{

						

						this.pagesToCrawlUnique.add(newURLL);
					

					}
					


				} else continue;


			}
			if(this.pagesToCrawlUnique.size()> 2694)System.out.println("newURL"+newURL+" and newURLL"+newURLL+"   "+this.pagesToCrawlUnique.get(2692));
			System.out.println("No of  elements in pagesToCrawlUnique "+this.pagesToCrawlUnique.size() );
			
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}

	}

	/**
	 * This methods is responsible for building list of unique URL where the keyphrase was found.
	 * 
	 *
	 */
	private boolean searchforKeyPhrase(WebCrawlerHTMLParser leg, String s,String temp){
		System.out.println("URL for searchforKeyPhrase is "+temp);
		boolean success = leg.searchForWord(s,temp);
		
		if(success== true && tempList.size() <= MAX_PAGES_TO_SEARCH)
		{
			
			if(this.pagesWithKeyPhrase.contains(temp)==false) {
				if(temp.contains("#") == true){
					
					if(containsCaseInsensitive2(temp.split("#")[0],this.pagesWithKeyPhrase) == false){
							System.out.println("1--");
						this.pagesWithKeyPhrase.add(temp);
						
						
					}
					else {System.out.println("2--");}
				}
				else{this.pagesWithKeyPhrase.add(temp);
				System.out.println("3--");
				System.out.println("pagesWithKeyPhrase size"+this.pagesWithKeyPhrase.size());
				
				
				}
				//if(temp.equals("https://en.wikipedia.org/wiki/Hugh_of_Saint-Cher"))this.pagesWithKeyPhrase.remove(temp);
				
			}
			else {System.out.println("4--");}
			return true;
		}
		else {
			System.out.println(" max size reached or no success "+ "FOUND  KeyPhrase "+s+" on page+"+temp+ " where Search Status is "+success);
			System.out.println("5--");
			return false;
		}

	}

	/**
	 * This methods is responsible for writing the list of URLS crawled in a txt file.
	 * 
	 *
	 */
	private void writeToTotalPagesUnique(){
		try {


			if(keyPhrase.length()!=0) 
			{

				PrintWriter writer1 = new PrintWriter("VisitedPagesWithKeyPhrase.txt", "UTF-8");
				System.out.println("Iterating for file VisitedPagesWithKeyPhrase");
				for(int i=0;i< this.pagesToCrawlUnique.size();i++) {

					writer1.println(this.pagesToCrawlUnique.get(i).split(" ")[1]);

				}
				writer1.close();
			}

			else{
				PrintWriter writer = new PrintWriter("PagesCrawledUnique_No_KeyPhrase.txt", "UTF-8");
				System.out.println("Iterating for file PagesCrawledUnique_No_KeyPhrase");
				for(int i=0;i< this.pagesCrawledWithoutKeyPhrase.size();i++) {
					System.out.println(this.pagesCrawledWithoutKeyPhrase.get(i));
					writer.println(this.pagesCrawledWithoutKeyPhrase.get(i));

				}
				writer.close();
			}


		} 

		catch (IOException e) {
			// 
			e.printStackTrace();
		} 
	}

	/**
	 * This methods is responsible for writing the list of URLS crawled with keyphrase 
	 * in a txt file.
	 * 
	 *
	 */
	private void writeToFileWithKeyPhrase(){
		try {
			PrintWriter writer2;
			if(pagesWithKeyPhrase.size()!=0){
				writer2 = new PrintWriter("PagesCrawledUnique_WithKeyPhrase.txt", "UTF-8");
				if(keyPhrase.length()!=0){

					for(String str: this.pagesWithKeyPhrase) {

						writer2.println(str);

					} 
					writer2.close();	
				}
			}}
		catch (IOException e) {
			// 
			e.printStackTrace();
		}
	}

	/**
	 * This methods is responsible for comparing if the URLs added in the pagesToCrawlUnique are Unique.
	 * 
	 *
	 */
	public boolean containsCaseInsensitive(String s, List<String> l){
		for (String string : l){
			if ((string.split(" ")[1]).equalsIgnoreCase(s)){
				return true;
			}
		}
		return false;
	}
	
	public boolean containsCaseInsensitive2(String s, List<String> l){
		for (String string : l){
			if ((string.split("#")[0]).equalsIgnoreCase(s)){
				return true;
			}
		}
		return false;
	}




}

