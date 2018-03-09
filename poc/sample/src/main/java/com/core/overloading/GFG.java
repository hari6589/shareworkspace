package com.core.overloading;

//Example to show error when method signature is same 
//and return type is different.
import java.io.*;

class Addition
{
 // adding two integer value.
 public int add(int a, int b)
 {
      
     int sum = a+b;
     return sum;
 }
  
 // adding three integer value.
 //public double add(int a, int b) // We can not overload a method by its returntype
 public double add(int a, int b, int c)
 {
     double sum = a+b+0.0;
     return sum;
 }

}

class GFG 
{
 public static void main (String[] args) 
 {
     Addition ob = new Addition();
      
     int sum1 = ob.add(1,2);
     System.out.println("sum of the two integer value :" + sum1);
      
     int sum2 = ob.add(1,2);
     System.out.println("sum of the three integer value :" + sum2);
      
 }
}