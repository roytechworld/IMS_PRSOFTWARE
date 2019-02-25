package com.prsoftwares.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;


public class SMSSender1 {
public static void main(String[] args) throws IOException {
String result =sendSms1();
System.out.println("result :" + result);




}
public static String sendSms1(){
String sResult = null;
try
{
// Construct data
String phonenumbers="8961280870";
String data="user=" + URLEncoder.encode("pradipto", "UTF-8");
data +="&password=" + URLEncoder.encode("pradipto@_12345", "UTF-8");
data +="&message=" + URLEncoder.encode("hi hello", "UTF-8");
data +="&sender=" + URLEncoder.encode("INVITE", "UTF-8");
data +="&mobile=" + URLEncoder.encode(phonenumbers, "UTF-8");
data +="&type=" + URLEncoder.encode("3", "UTF-8");
 URL url = new URL("http://login.bulksmsgateway.in/sendmessage.php?"+data);
URLConnection conn = url.openConnection();
conn.setDoOutput(true);
OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
wr.write(data);
wr.flush();
// Get the response
BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
String line;
String sResult1="";
while ((line = rd.readLine()) != null)
{
// Process line...
sResult1=sResult1+line+" ";
}
wr.close();
rd.close();




return sResult1;
}
catch (Exception e)
{
System.out.println("Error SMS "+e);
return "Error "+e;
}
}

public static String sendSmsGateway(String mobileno,String message){
String sResult = null;
try
{
// Construct data
String phonenumbers=mobileno;
String data="user=" + URLEncoder.encode("pradiptoroy", "UTF-8");
data +="&password=" + URLEncoder.encode("7685076984", "UTF-8");
data +="&message=" + URLEncoder.encode(message, "UTF-8");
data +="&sender=" + URLEncoder.encode("NRSHOE", "UTF-8");
data +="&mobile=" + URLEncoder.encode(phonenumbers, "UTF-8");
data +="&type=" + URLEncoder.encode("3", "UTF-8");
 URL url = new URL("http://login.bulksmsgateway.in/sendmessage.php?"+data);
URLConnection conn = url.openConnection();
conn.setDoOutput(true);
OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
wr.write(data);
wr.flush();
// Get the response
BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
String line;
String sResult1="";
while ((line = rd.readLine()) != null)
{
// Process line...
sResult1=sResult1+line+" ";
}
wr.close();
rd.close();




return sResult1;
}
catch (Exception e)
{
System.out.println("Error SMS "+e);
return "Error "+e;
}
}





















// TODO Auto-generated method stub
}