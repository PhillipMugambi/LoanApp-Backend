/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import java.util.Map;
import DB.DBFunctions;

/**
 *
 * @author ADMIN
 */
public class Simulator {

    public static void main(String[] args) {

        DBFunctions db = new DBFunctions();
        ///String phoneNumber = requestMap.get("phoneNumber");
        String firstName="Phillip";
        String lastName="mugambi";
        String PhoneNumber="011206790";
        String nationalID="37959404";
        String pin = "12345";
        //String uuid = "1234";
       // String phoneNumber = "0712622099";
        //Map<String, String> rs = db.Login(phoneNumber, pin,uuid);
        Map<String, String> rs = db.customerRegistration( firstName,lastName,PhoneNumber,nationalID);
        org.json.JSONObject logResp = new org.json.JSONObject();
        org.json.JSONObject loginResp = new org.json.JSONObject();
    }

}
