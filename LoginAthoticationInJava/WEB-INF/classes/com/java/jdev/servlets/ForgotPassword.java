package com.java.jdev.servlets;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.sql.DriverManager;
import java.sql.Connection;
import java.io.PrintStream;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class ForgotPassword extends HttpServlet {
	
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//System.setErr(new PrintStream(new FileOutputStream("../../err.log")));
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "db_user1", "db1");
            PreparedStatement ps = con.prepareStatement("SELECT * FROM users_auth WHERE username=?");
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
				
               PreparedStatement ps1 = con.prepareStatement("update users_auth set password=? where username=? ");
			   ps1.setString(1,password);
			   ps1.setString(2,username);
			   int rs1 = ps1.executeUpdate();
				if(rs1>0){
					PrintWriter out=response.getWriter();
					out.println("<img src='./loading.gif' alt='Loading...' width='50' height='50'>");
					try{
					Thread.sleep(3000);
					}catch (InterruptedException e) {
					 out.println("Thread was interrupted.");
				}
					 response.sendRedirect("./login.html");
				}
            } else {
                response.getWriter().println("Invalid Credentials");
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
}