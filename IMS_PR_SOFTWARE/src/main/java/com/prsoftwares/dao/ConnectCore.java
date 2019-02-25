/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.prsoftwares.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.naming.InitialContext;
import javax.sql.DataSource;



/**
 *
 * @author mrinmay.santra
 */
public class ConnectCore {

    public static Connection getConnection() throws Exception {
        /*
         * Create a JNDI Initial context to be able to
         *  lookup  the DataSource
         *
         * In production-level code, this should be cached as
         * an instance or static variable, as it can
         * be quite expensive to create a JNDI context.
         *
         * Note: This code only works when you are using servlets
         * or EJBs in a J2EE application server. If you are
         * using connection pooling in standalone Java code, you
         * will have to create/configure datasources using whatever
         * mechanisms your particular connection pooling library
         * provides.
         */

        InitialContext ctx = new InitialContext();

        /*
         * Lookup the DataSource, which will be backed by a pool
         * that the application server provides. DataSource instances
         * are also a good candidate for caching as an instance
         * variable, as JNDI lookups can be expensive as well.
         */

        DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/myDatasource");

        /*
         * The following code is what would actually be in your
         * Servlet, JSP or EJB 'service' method...where you need
         * to work with a JDBC connection.
         */

          Connection conn=null;
         

        try {

        	conn = ds.getConnection();
            System.out.println("88888 conn=" + conn);
            System.out.println("Connected Sucessfully !!");
        } 
        catch (Exception e) {
            /*
             * close any jdbc instances here that weren't
             * explicitly closed during normal code path, so
             * that we don't 'leak' resources...
             */
        	conn.close();
        }
       
//      finally
//        {
//    	  if ( conn != null) 
//    		  
//    		   conn.close();
//        }
        
        return conn;

    }

    public static Boolean closeConnection(Connection connection) {
        Boolean conStatus = false;
        try {
            
        	 
            connection.close();
            conStatus = true;
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conStatus;
    }



/////tesing database connection
////    public void fetchDatafromTable() {
////
////        String query = "";
////        PreparedStatement ps = null;
////        ResultSet rs = null;
////        try {
////            Connection conn = ConnectCore.getConnection();
////            query = "select * from udy_account_details";
////            ps = conn.prepareStatement(query);
////            rs = ps.executeQuery();
////            if (rs != null) {
////                while (rs.next()) {
////                    System.out.println("Debuger#001==>" + rs.getLong(1));
////                }
////            }
////            System.out.println(rs);
////
////        } catch (Exception exception) {
////            exception.printStackTrace();
////        }
//
//    }
}
