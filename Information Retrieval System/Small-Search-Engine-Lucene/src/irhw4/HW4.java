package irhw4;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Fields;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Version;
import org.jsoup.Jsoup;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.Chart;
import org.knowm.xchart.ChartBuilder;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;

/**
 * To create Apache Lucene index in a folder and add files into this index based
 * on the input of the user.
 */
public class HW4 {
	private static Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_47);
	private static Analyzer sAnalyzer = new SimpleAnalyzer(Version.LUCENE_47);
	private static Map<String, Integer> termfq = new HashMap<String, Integer>();
	private IndexWriter writer;
	private ArrayList<File> queue = new ArrayList<File>();
	private static ArrayList<Integer> termsfreq = new ArrayList<Integer>();
	private static List<String> indexedterms = new ArrayList<String>();
	private static List<Float> termprob = new ArrayList<Float>();
	private static List<Integer> termrank = new ArrayList<Integer>();
	private static List<Integer> termrank2 = new ArrayList<Integer>();

	static <K, V extends Comparable<? super V>> List<Entry<K, V>> sortTerm_FrequencyByFrequency(Map<K, V> map) {

		List<Entry<K, V>> sortedEntries = new ArrayList<Entry<K, V>>(map.entrySet());

		Collections.sort(sortedEntries, new Comparator<Entry<K, V>>() {
			@Override
			public int compare(Entry<K, V> ob1, Entry<K, V> ob2) {
				return ob2.getValue().compareTo(ob1.getValue());
			}
		});

		return sortedEntries;
	}

	static void buildZipfCurvesLinearScale(List<Integer> xdata, List<Float> ydata) {

		System.out.println("Building Zipfian curve, right click on graph to save");
		List<Integer> xData = xdata;
		List<Float> yData = ydata;

		Chart chart = new ChartBuilder().xAxisTitle("Rank").yAxisTitle("Probability").width(800).height(600).build();
		chart.addSeries("Zipfian curve ", xData, yData);
		new SwingWrapper(chart).displayChart();

	}

	static void buildZipfCurvesLogarithmicScale(List<Integer> xdata, List<Float> ydata) {

		System.out.println("Building Zipfian curve (logarithmic scale), right click on graph to save");
		List<Integer> xData = xdata;
		List<Float> yData = ydata;

		Chart chart = new ChartBuilder().xAxisTitle("Log (Rank)").yAxisTitle("Log (Probability)").width(800).height(600)
				.build();
		chart.getStyleManager().setYAxisLogarithmic(true);
		chart.getStyleManager().setXAxisLogarithmic(true);
		chart.addSeries("Zipfian curve (logarithmic scale) ", xData, yData);
		new SwingWrapper(chart).displayChart();

	}

	public static void main(String[] args) throws IOException {
		int counter = 1;
		int totaltermoccurence = 0;
		System.out.println("Enter the FULL path where the index will be created: (e.g. /Usr/index or c:\\temp\\index)");

		String indexLocation = null;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String s = br.readLine();

		HW4 indexer = null;
		try {
			indexLocation = s;
			indexer = new HW4(s);
		} catch (Exception ex) {
			System.out.println("Cannot create index..." + ex.getMessage());
			System.exit(-1);
		}

		// ===================================================
		// read input from user until he enters q for quit
		// ===================================================
		/* while (!s.equalsIgnoreCase("q")) { */
		try {
			System.out.println(
					"Enter the FULL path to add into the index (q=quit): (e.g. /home/mydir/docs or c:\\Users\\mydir\\docs)");
			System.out.println("[Acceptable file types: .xml, .html, .html, .txt]");
			s = br.readLine();
			/*
			 * if (s.equalsIgnoreCase("q")) { break; }
			 */

			// try to add file into the index
			indexer.indexFileOrDirectory(s);
		} catch (Exception e) {
			System.out.println("Error indexing " + s + " : " + e.getMessage());
		}

		// ===================================================
		// after adding, we always have to call the
		// closeIndex, otherwise the index is not created
		// ===================================================
		indexer.closeIndex();

		// =========================================================
		// Now search
		// =========================================================
		String term = "";
		String result1 = "";
		FileWriter file = new FileWriter("term-frequency.txt");
		PrintWriter pw = new PrintWriter(file);
		IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(indexLocation)));
		IndexSearcher searcher = new IndexSearcher(reader);
		Fields fields = MultiFields.getFields(reader);
		Terms terms = fields.terms("contents");
		TermsEnum iterator = terms.iterator(null);
		BytesRef byteRef = null;
		// Generate Term frequency for each of unique terms present in the CACM corpus
		while ((byteRef = iterator.next()) != null) {
			term = new String(byteRef.bytes, byteRef.offset, byteRef.length);
			Term termInstance = new Term("contents", term);
			long termFreq = reader.totalTermFreq(termInstance);
			result1 = term + "\t" + termFreq;
			termfq.put(term, (int) termFreq);

		}
		// Generate Term and its Frequency sorted by Frequency
		List<Entry<String, Integer>> sortedtermfq = sortTerm_FrequencyByFrequency(termfq);
		// Generate list of unique terms and their frequency
		for (Entry<String, Integer> el : sortedtermfq) {
			totaltermoccurence = totaltermoccurence + el.getValue();
			pw.println(String.format("%1$-" + 20 + "s", el.getKey()) + String.format("%1$" + 15 + "s", el.getValue()));
			termsfreq.add(el.getValue());
			indexedterms.add(el.getKey());
		}
		pw.close();
		// pw.println();
		float probability = 0;
		int term_rank = 1;
		//  Generate list of rank of each of the index terms and their probabilities
		// termprob- is list of probability of each of the index term ((frequency/Total word occurrences))
		// termrank- is list of rank of each of the index term
		for (Integer i : termsfreq) {
			probability = (float) i / (float) totaltermoccurence;
			termprob.add(probability);
			termrank.add(term_rank);
			term_rank = term_rank + 1;
		}
		int a = 0;

		FileWriter file2 = new FileWriter("MOST 100 docIDs ranked by score with text snippet.txt", true);
		PrintWriter pw2 = new PrintWriter(file2);

		s = "";
		// Loop for each of the query and generate list of top 100 docID's ranked by score and 
		// add a text snippet of 200 chars along the DocID
		while (!s.equalsIgnoreCase("q")) {
			try {
				System.out.println("Enter the search query (q=quit):");
				s = br.readLine();

				pw2.println("======================================");
				if (s.equalsIgnoreCase("q")) {
					break;
				}
				TopScoreDocCollector collector = TopScoreDocCollector.create(50000, true);
				Query q = new QueryParser(Version.LUCENE_47, "contents", sAnalyzer).parse(s);
				searcher.search(q, collector);
				ScoreDoc[] hits = collector.topDocs().scoreDocs;
				String temp[] = null;
				// 4. display results
				System.out.println("Found " + hits.length + " hits.");
				pw2.println("Query is " + s);
				pw2.println("======================================");
				for (int i = 0; i < 100; ++i) {
					int docId = hits[i].doc;
					Document d = searcher.doc(docId);
					temp = d.get("path").split("/");
					System.out.println((i + 1) + ". " + "DOC ID:" + temp[temp.length - 1].replace(".html", "")
							+ " score=" + hits[i].score);
					pw2.println((i + 1) + ". " + "DOC ID:" + temp[temp.length - 1].replace(".html", "") + " score="
							+ hits[i].score);
					pw2.println("Text snippet is as below:");
					if ((d.get("contents")).length() <= 200)
						pw2.println((d.get("contents")));
					else
						pw2.println((d.get("contents")).substring(0, 200));
					pw2.println("======================================");
				}

			} catch (Exception e) {
				System.out.println("Error searching " + s + " : " + e.getMessage());
				e.printStackTrace();
				break;
			}

		}

		pw2.close();
		// Generate ZipfCurves LinearScale
		// Generate ZipfCurves LogarithmicScale
		buildZipfCurvesLinearScale(termrank, termprob);
		buildZipfCurvesLogarithmicScale(termrank, termprob);

	}

	/**
	 * Constructor
	 * 
	 * @param indexDir
	 *            the name of the folder in which the index should be created
	 * @throws java.io.IOException
	 *             when exception creating index.
	 */
	HW4(String indexDir) throws IOException {

		FSDirectory dir = FSDirectory.open(new File(indexDir));

		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_47, sAnalyzer);

		writer = new IndexWriter(dir, config.setOpenMode(IndexWriterConfig.OpenMode.CREATE));
	}

	/**
	 * Indexes a file or directory
	 * 
	 * @param fileName
	 *            the name of a text file or a folder we wish to add to the
	 *            index
	 * @throws java.io.IOException
	 *             when exception
	 */
	public void indexFileOrDirectory(String fileName) throws IOException {
		// ===================================================
		// gets the list of files in a folder (if user has submitted
		// the name of a folder) or gets a single file name (is user
		// has submitted only the file name)
		// ===================================================
		addFiles(new File(fileName));

		int originalNumDocs = writer.numDocs();
		String delimiter = "\\Z";
		for (File f : queue) {
			FileReader fr = null;
			try {
				Document doc = new Document();
				// Read all content of a file as string and pass to Jsoup to remove HTML tags
				String content = new Scanner(f).useDelimiter(delimiter).next();
				String nohtml = Jsoup.parse(content).text();

				fr = new FileReader(f);

				doc.add(new TextField("contents", nohtml, Field.Store.YES));
				doc.add(new StringField("path", f.getPath(), Field.Store.YES));
				doc.add(new StringField("filename", f.getName(), Field.Store.YES));

				writer.addDocument(doc);
				System.out.println("Added: " + f);
			} catch (Exception e) {
				System.out.println("Could not add: " + f);
			} finally {
				fr.close();
			}
		}

		int newNumDocs = writer.numDocs();
		System.out.println("");
		System.out.println("************************");
		System.out.println((newNumDocs - originalNumDocs) + " documents added.");
		System.out.println("************************");

		queue.clear();
	}

	private void addFiles(File file) {

		if (!file.exists()) {
			System.out.println(file + " does not exist.");
		}
		if (file.isDirectory()) {
			for (File f : file.listFiles()) {
				addFiles(f);
			}
		} else {
			String filename = file.getName().toLowerCase();
			// ===================================================
			// Only index text files
			// ===================================================
			if (filename.endsWith(".htm") || filename.endsWith(".html") || filename.endsWith(".xml")
					|| filename.endsWith(".txt")) {
				queue.add(file);
			} else {
				System.out.println("Skipped " + filename);
			}
		}
	}

	/**
	 * Close the index.
	 * 
	 * @throws java.io.IOException
	 *             when exception closing
	 */
	public void closeIndex() throws IOException {
		writer.close();
	}
}