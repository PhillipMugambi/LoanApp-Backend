/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author rmunene
 */
public class DBFunctions {

    String LoanAccount;

    public Map<String, String> customerRegistration(String firstName, String lastName, String PhoneNumber, String nationalID) {
        Map<String, String> responseMap = new HashMap<>();
        // check if customer exists
        boolean custExist = checkIfExist(nationalID);
        if (custExist) {
            responseMap.put("status", "0");
            responseMap.put("message", "Customer Already Exist");
        } else {
            String sql = "Insert into tbcustomers(`NationalID`, `FirstName`, `LastName`,`PhoneNumber`,`Uuid`,`PIN`)values (?,?,?,?,?,?)";
            try (Connection connection = DBconnection.Connect();
                    PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, nationalID);
                ps.setString(2, firstName);
                ps.setString(3, lastName);
                ps.setString(4, PhoneNumber);
                ps.setInt(5, 0);
                ps.setInt(6, 0);
                ps.execute();
                responseMap.put("status", "0");
                responseMap.put("message", "Registration Successful");
            } catch (Exception ex) {
                // log
                responseMap.put("status", "1");
                responseMap.put("message", ex.toString());
            }
            // insert to customer and accounts
            //int pin = 12345;
            // insertToAccount(firstName,lastName,PhoneNumber,nationalID);
            //String sql="Insert into tbCustomer ( `firstName`, `lastName`, `PhoneNumber`, `nationalID`)VAlues('" + firstName + "','" + lastName + "','" + PhoneNumber + "','" + nationalID + "','" + pin + "')";
            //  sql= "insert INTO tbCustomer firstName, lastName, mobilenumber, nationalID";

        }
        return responseMap;
    }

    private boolean checkIfExist(String phonenumber) {
        boolean exist = false;
        String sql = "SELECT ID FROM tbcustomers  WHERE PhoneNumber=?";
        try (Connection connection = DBconnection.Connect();
                PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, phonenumber);
            ps.executeQuery();
            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    exist = true;
                }
            }
        } catch (Exception ex) {

            // log
        }
        return exist;
    }

    public Map<String, String>Loan(String LoanAmount, String PhoneNumber, String nationalID) {
Map<String, String> respMap = new HashMap<>();
        LoanAccount = "BE";
        Random value = new Random();

        //Generate two values to append to 'BE'
        int r1 = value.nextInt(10);
        int r2 = value.nextInt(10);
        LoanAccount += Integer.toString(r1) + Integer.toString(r2) + " ";

        int count = 0;
        int n = 0;
        for (int i = 0; i < 12; i++) {
            if (count == 4) {
                LoanAccount += " ";
                count = 0;
            } else {
                n = value.nextInt(10);
            }
            LoanAccount += Integer.toString(n);
            count++;

        }

// String sql="Insert into tbCustomer ( `firstName`, `lastName`, `PhoneNumber`, `nationalID`)VAlues('" + firstName + "','" + lastName + "','" + PhoneNumber + "','" + nationalID + "','" + pin + "')";
        String sql = "INSERT INTO `tbloans`(`ID`, `NationalID`, `LoanAmount`, `LoanBalance`, `LoanAccount`, `PhoneNumber`, `Active`)values(?,?,?,?,?,?,?)";

        try (Connection connection = DBconnection.Connect();
                PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, 0);
            ps.setString(2, nationalID);
            ps.setString(3, LoanAmount);
            ps.setString(4, LoanAmount);
            ps.setString(5, LoanAccount);
            ps.setString(6, PhoneNumber);
            ps.setInt(7, 0);
            ps.execute();
            respMap.put("message", "Loan Applied");
             respMap.put("status", "1");
        } catch (Exception ex) {
            // log
        }
        return respMap;
    }

    private void insertToCustomer(int nationalID, String firstName, String lastName,
            String phonenumber, String uuid, int pin) {

    }

    private void updateUuid(String uuid, String phoneNumber) {
        String sql = "update tbcustomers set Uuid=?  WHERE PhoneNumber=?";
        try (Connection connection = DBconnection.Connect();
                PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, uuid);
            ps.setString(2, phoneNumber);
            ps.execute();
        } catch (Exception ex) {
            // log
        }
    }

    public Map<String, String> Login(String phoneNumber, String pin, String uuid) {
        Map<String, String> respMap = new HashMap<>();
        String sql = "SELECT Uuid,PIN FROM tbcustomers  WHERE PhoneNumber=(?)";
        try (Connection connection = DBconnection.Connect();
                PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, phoneNumber);
            ps.executeQuery();
            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    String dbUUID = rs.getString("Uuid");
                    String dbPIN = rs.getString("PIN");
                    if (!dbUUID.equalsIgnoreCase(uuid)) {
                        if (dbUUID.equalsIgnoreCase("0")) {
                            //update the uuid after reset
                            updateUuid(uuid, phoneNumber);
                            respMap.put("status", "1");
                            respMap.put("message", "login succesful");

                        } else {
                            respMap.put("status", "1");
//                            respMap.put("message", "Kindly the device does not match that was used previously");
                            respMap.put("message", "login succesful");
                        }
                    } else if (!dbPIN.equals(pin)) {
                        respMap.put("status", "0");
                        respMap.put("message", "wrong credentials");
                    } else {
                        respMap.put("status", "1");
                        respMap.put("message", "login succeful");
                    }
                } else {
                    respMap.put("status", "2");
                    respMap.put("message", "Customer Does Not  Exist.Kindly Register");
                }
            }
        } catch (Exception ex) {
            respMap.put("status", ex.toString());
        }
        return respMap;
    }

}
