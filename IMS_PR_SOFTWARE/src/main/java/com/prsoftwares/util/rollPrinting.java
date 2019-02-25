package com.prsoftwares.util;


	
	import java.awt.Font;
	import java.awt.Graphics;
	import java.awt.print.PageFormat;
	import java.awt.print.Paper;
	import java.awt.print.Printable;
	import java.awt.print.PrinterException;
	import java.awt.print.PrinterJob;

	public class rollPrinting  implements Printable {

	public rollPrinting (){
	super();
	}
	public void print() {
	PrinterJob job = PrinterJob.getPrinterJob();
	PageFormat pageFormat = createFormat();
	try {
	job.setPrintable(this,pageFormat);
	if (job.printDialog()) {
	job.print();
	}
	} catch (PrinterException e) {
	e.printStackTrace();
	}
	}

	public int print(Graphics graphics, PageFormat pageFormat, int pageIndex)
	throws PrinterException {
	if (pageIndex == 0) {
	painter(graphics);
	return PAGE_EXISTS;
	}
	return NO_SUCH_PAGE;
	}

	void painter(Graphics graphics) {
	graphics.setFont(new Font("Sans Serif", Font.PLAIN, 11));
	char[] data = "Printing an area. This is a test that is driving me crazy. It is time to come out...."
	.toCharArray();
	graphics.drawChars(data, 0, data.length, 25, 70);
	}

	public static void main(String[] args){
	rollPrinting  printTest=new rollPrinting ();
	printTest.print();
	}

	private PageFormat createFormat() {
	PageFormat format = new PageFormat();
	format.setPaper(new Custom());
	return format;
	}

	private class Custom extends Paper {

	public Custom() {
	super();
	setSize(345,345);
	}
	}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	


