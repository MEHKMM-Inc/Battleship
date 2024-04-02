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
      int x = Integer.parseInt(request.getParameter("x"));
      int y = Integer.parseInt(request.getParameter("y"));
      int ship = Integer.parseInt(request.getParameter("shipType"));

      if (x >= 0 && y >= 0) {
         int calculate = Calculator.calculate(hit, x, y, shipSunk.values()[ship]);
      }
      JSONArray mJSONArray = new JSONArray(Arrays.asList(Calculator.getProbabilityArray()));

      response.setContentType("text/html");

      PrintWriter out = response.getWriter();
      out.println(mJSONArray.toString());
   }

   public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

   } 
}