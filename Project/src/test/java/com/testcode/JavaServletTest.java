package com.testcode;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.*;
import com.testcode.testAPI; 
import java.io.PrintWriter;
import java.io.ByteArrayOutputStream;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JavaServletTest {
    @Test
    public void doSomething() {
       testAPI newTest = new testAPI();

       HttpServletRequest servletRequest = mock(HttpServletRequest.class);
       HttpServletResponse servletRespose = mock(HttpServletResponse.class);
        
       try {
        when(servletRequest.getParameter("hit")).thenReturn("true"); // Can be either true or false
        when(servletRequest.getParameter("x")).thenReturn("1"); // Must be an integer value
        when(servletRequest.getParameter("y")).thenReturn("1"); // Must be an integer value
        when(servletRequest.getParameter("shipType")).thenReturn("5"); // From 0-5, based on the shipSunk java code
        when(servletRespose.getWriter()).thenReturn(new PrintWriter(new ByteArrayOutputStream(248)));
        newTest.doGet(servletRequest, servletRespose);
        } catch(Exception e) {
            System.err.println(e);
        }
    }
}
