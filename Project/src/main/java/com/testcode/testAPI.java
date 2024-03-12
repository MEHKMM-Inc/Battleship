package com.testcode;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import org.json.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class testAPI extends HttpServlet {

  private static final long serialVersionUID = 1L;

  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

      ProbabilityCalculator Calculator = new ProbabilityCalculator();
      Boolean hit = Boolean.parseBoolean(request.getParameter("hit"));
      Boolean sunk = Boolean.parseBoolean(request.getParameter("sunk"));

      double testCalculation[][] = Calculator.calculate(hit, 0, 0, shipSunk.none);
      JSONArray mJSONArray = new JSONArray(Arrays.asList(testCalculation));

      response.setContentType("text/html");

      PrintWriter out = response.getWriter();
      out.println(mJSONArray.toString());
      //out.println("./api works. Try ./api/getMessage");      
   }

   public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

   } 
}