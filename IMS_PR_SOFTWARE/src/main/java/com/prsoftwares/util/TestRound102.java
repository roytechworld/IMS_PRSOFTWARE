package com.prsoftwares.util;

public class TestRound102 {
    public static void main(String args[]){
      double d = 3.1537;
      // output is 3.2
      System.out.println(d + " : " + round(d, 1));
      // output is 3.15
      System.out.println(d + " : " + round(d, 2));
      // output is 3.154
      System.out.println(d + " : " + round(d, 3));
      d= 9.155;
      // output is 9.2
      System.out.println(d + " : " + round(d, 1));
      // output is 9.16
      System.out.println(d + " : " + round(d, 2));
      // output is 3.155
      System.out.println(d + " : " + round(d, 3));
      d= 1234.156789;
      // output is 1234.2
      System.out.println(d + " : " + round(d, 1));
      // output is 1234.16
      System.out.println(d + " : " + round(d, 2));
      // output is 1234.157
      System.out.println(d + " : " + round(d, 3));
      // output is 1234.1568
      System.out.println(d + " : " + round(d, 4));
      d= 1.5;
      // output is 2
      System.out.println(d + " : " + round(d, 0));
    }

    // positive value only.
    public static double round(double value, int decimalPlace)
    {
      double power_of_ten = 1;
      // floating point arithmetic can be very tricky.
      // that's why I introduce a "fudge factor"
      double fudge_factor = 0.05;
      while (decimalPlace-- > 0) {
         power_of_ten *= 10.0d;
         fudge_factor /= 10.0d;
      }
      return Math.round((value + fudge_factor)* power_of_ten)  / power_of_ten;
    }
}
