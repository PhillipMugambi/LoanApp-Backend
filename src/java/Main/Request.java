/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import commonOperations.Utilities;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import DB.DBFunctions;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;

/**
 *
 * @author rmunene
 */
@WebServlet(name = "CustomerLogin_Registration", urlPatterns = {"/CustomerLogin_Registration"})
public class Request extends HttpServlet {

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
        //  response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
DBFunctions db = new DBFunctions();
        try {
            StringBuilder jb = new StringBuilder();
            String line;
            try (BufferedReader reader = request.getReader()) {
                while ((line = reader.readLine()) != null) {
                    jb.append(line);
                }
            }
            String message = jb.toString();
            org.json.JSONObject obj = new org.json.JSONObject(message);
            Map<String, String> requestMap = new HashMap<>();
            requestMap = (new Utilities()).parseJSON(obj, requestMap);
            

            //get Request Type
            String requestType = requestMap.get("service");
            if (requestType.equals("login")) {
                String phoneNumber = requestMap.get("phoneNumber");
                String pin = requestMap.get("pin");
                String uuid = requestMap.get("uuid");
                Map<String, String> rs = db.Login(phoneNumber, pin,uuid);
                org.json.JSONObject logResp = new org.json.JSONObject();
                org.json.JSONObject loginResp = new org.json.JSONObject();

                loginResp.put("message", rs.get("message"));
                logResp.put("data", loginResp);
                logResp.put("status", rs.get("status"));

                logResp.toString();
                out.println(logResp.toString());
            }
            else if (requestType.equals("registration")) {
                //int nationaID = Integer.parseInt(requestMap.get("NationalID"));
                String firstName = requestMap.get("firstName");
                String lastName = requestMap.get("lastName");
                String PhoneNumber = requestMap.get("PhoneNumber");
                String nationalID = requestMap.get("nationalID");
                Map<String, String> rs = db.customerRegistration( firstName, lastName, PhoneNumber, nationalID);
                org.json.JSONObject regResp = new org.json.JSONObject();
                org.json.JSONObject registrationResp = new org.json.JSONObject(message);
                obj.put("status", rs.get("status"));
                registrationResp.put("message", rs.get("message"));
                regResp.put("data", registrationResp);
                regResp.put("status", rs.get("status"));
                regResp.toString();
                out.println(regResp.toString()); 
            }
           
             else if(requestType.equals("statement")){
                JSONArray rs = db.Mystatement();
                org.json.JSONObject viewResp = new org.json.JSONObject();
                org.json.JSONObject logResp = new org.json.JSONObject(message);

                viewResp.put("data", rs);
//                viewResp.toString();
//                 out.println(viewResp.toString());
                

                out.println(viewResp);
                
                }
            
            else if(requestType.equals("apply loan")){
             String nationalID = requestMap.get("NationalID");
                String LoanAmount = requestMap.get("LoanAmount");
                String PhoneNumber = requestMap.get("PhoneNumber");
                Map<String, String> rs = db.Loan( LoanAmount, PhoneNumber, nationalID);
                org.json.JSONObject regResp = new org.json.JSONObject();
                org.json.JSONObject registrationResp = new org.json.JSONObject(message);
                obj.put("status", rs.get("status"));
                registrationResp.put("message", rs.get("message"));
                regResp.put("data", registrationResp);
                regResp.put("status", rs.get("status"));
                regResp.toString();
                out.println(regResp.toString()); 
            }
            
            
            
            else if(requestType.equals("apply loan")){
                String LoanAmount = requestMap.get("LoanAmount");
                String PhoneNumber = requestMap.get("PhoneNumber");
                Map<String, String> rs = db.PayLoan( LoanAmount, PhoneNumber);
                org.json.JSONObject regResp = new org.json.JSONObject();
                org.json.JSONObject registrationResp = new org.json.JSONObject(message);
                obj.put("status", rs.get("status"));
                registrationResp.put("message", rs.get("message"));
                regResp.put("data", registrationResp);
                regResp.put("status", rs.get("status"));
                regResp.toString();
                out.println(regResp.toString()); 
            }
            
            else {
                org.json.JSONObject invaResp = new org.json.JSONObject();
                org.json.JSONObject invalidResp = new org.json.JSONObject(message);
                 invaResp.put("status", "0");
                invalidResp.put("Request", "Invalid Request");
                invaResp.put("data", invalidResp);
                invaResp.toString();
                out.println(invaResp.toString());
            }

        } catch (SQLException ex) {
            Logger.getLogger(Request.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            out.close();
        }
      
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

}
