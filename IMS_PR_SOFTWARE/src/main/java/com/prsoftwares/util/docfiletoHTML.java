package com.prsoftwares.util;
	
	
	import java.io.BufferedWriter;
	import java.io.DataOutputStream;
	import java.io.File;
	import java.io.FileInputStream;
	import java.io.FileOutputStream;
	import java.io.IOException;
	import java.io.OutputStreamWriter;
	import java.io.StringWriter;
	import javax.swing.JEditorPane;
	import javax.swing.JFrame;
	import javax.swing.JScrollPane;
	import javax.xml.parsers.DocumentBuilderFactory;
	import javax.xml.transform.OutputKeys;
	import javax.xml.transform.Transformer;
	import javax.xml.transform.TransformerFactory;
	import javax.xml.transform.dom.DOMSource;
	import javax.xml.transform.stream.StreamResult;

    import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.extractor.WordExtractor;
//	import org.apache.poi.xwpf.converter.core.FileImageExtractor;
//	import org.apache.poi.xwpf.converter.core.FileURIResolver;
//	import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
	import org.w3c.dom.Document;

	public class docfiletoHTML {
	    private File docFile;
	    private File file;

	    public docfiletoHTML(File docFile) {
	        this.docFile = docFile;
	    }

	    public void convert(File file) {
	    this.file = file;

	        try {
	            FileInputStream finStream=new FileInputStream(docFile.getAbsolutePath()); 
	            HWPFDocument doc=new HWPFDocument(finStream);
	            WordExtractor wordExtract=new WordExtractor(doc);
	            Document newDocument = DocumentBuilderFactory.newInstance() .newDocumentBuilder().newDocument();
	            WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(newDocument) ;
	            wordToHtmlConverter.processDocument(doc);

	            StringWriter stringWriter = new StringWriter();
	            Transformer transformer = TransformerFactory.newInstance().newTransformer();

	            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	            transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
	            transformer.setOutputProperty(OutputKeys.METHOD, "html");
	            transformer.transform(new DOMSource( wordToHtmlConverter.getDocument()), new StreamResult( stringWriter ) );

	            String html = stringWriter.toString();
	            FileOutputStream fos=new FileOutputStream(new File("d:\\JS005.html"));
	            DataOutputStream dos;

	            try {
	                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos,"UTF-8"));    
	                out.write(html);
	                out.close();
	            }
	            catch (IOException e) {
	               e.printStackTrace();
	            }

	           /*JEditorPane editorPane = new JEditorPane();
	           editorPane.setContentType("text/html");
	           editorPane.setEditable(false);

	           editorPane.setPage(file.toURI().toURL());

	           JScrollPane scrollPane = new JScrollPane(editorPane);     
	           JFrame f = new JFrame("Display Html File");
	           f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	           f.getContentPane().add(scrollPane);
	           f.setSize(512, 342);
	           f.setVisible(true);*/

	        } catch(Exception e) {
	            e.printStackTrace();
	        }
	    }  

	    public static void main(String args[]) {
	    	docfiletoHTML TTC=new docfiletoHTML(new File("d:\\JS005.doc"));
	        TTC.convert(TTC.docFile);         
	   }
	}
	
	
	
	
	
	
	
	
	
	
	


