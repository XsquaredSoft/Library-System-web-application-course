/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author MSI GT72 DRAGON
 */
public class login extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
       
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        String connectionURL = "jdbc:mysql://localhost/project";
        Connection conn = null;

        String usr = request.getParameter("user_name");
        String pass = request.getParameter("password");
        String hashed_pass = md5(pass);

        request.getRequestDispatcher("header.html").include(request, response);
        request.getRequestDispatcher("body.html").include(request, response);

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(connectionURL, "admin", "123456");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("select user_name,password from users where user_name='" + usr + "' and password='" + hashed_pass + "';");
            if(rs.next()){
                    out.println("<h1 class=\"gen\">Welocme "+usr+"</h1>");
                    request.getRequestDispatcher("footer.html").include(request, response);
                    HttpSession session = request.getSession();
                    session.setAttribute("user", usr);
                    
                    out.println("<script>\n" +
"    setTimeout(function(){location.href=\"http://localhost:8084/WebApplication_project1/index.html\"} , 3000);\n" +
"</script>");
                
                
            }
            else{
            stmt.close();
            conn.close();
            out.println("<h1 class=\"gen\">invalid credintials please check your username or password</h1>");
            out.println("<script>\n" +
"    setTimeout(function(){location.href=\"http://localhost:8084/WebApplication_project1/index.html/login.html\"} , 3000);\n" +
"</script>");
            request.getRequestDispatcher("footer.html").include(request, response);
            }
        } catch (ClassNotFoundException e) {
            out.println("Couldn't load database driver: "
                    + e.getMessage());
        } catch (SQLException e) {
            out.println("SQLException caught: "
                    + e.getMessage());
        } catch (Exception e) {
            out.println(e);
        }
    }
        
    

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
    
    public static String md5(String input) {
        String md5 = null;
        if (null == input) {
            return null;
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(input.getBytes(), 0, input.length());
            md5 = new BigInteger(1, digest.digest()).toString(16);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return md5;
    }

}
