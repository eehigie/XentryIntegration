/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xentry.plexada;

/**
 *
 * @author efosa
 */
import com.siebel.data.SiebelPropertySet;
import java.util.HashMap;
import java.util.Map;

public class XentryCustomer {
    
    private final SiebelPropertySet psCustomer;
    private Map XentryCustomerMap;
    
    public XentryCustomer(SiebelPropertySet spsCustomer) {
        this.psCustomer = spsCustomer;
        this.XentryCustomerMap = this.converSiebelPropertySetToMap(this.psCustomer);
        
    }
    
    private Map converSiebelPropertySetToMap(SiebelPropertySet spsCustomer){
       Map tmpMap = new HashMap();
       //Map tmpMap_2 = new HashMap();
       
       tmpMap.put("FirstName", spsCustomer.getProperty("FirstName"));
       tmpMap.put("LastName", spsCustomer.getProperty("LastName"));
       tmpMap.put("CallbackFlag", spsCustomer.getProperty("CallbackFlag"));
       tmpMap.put("WaitingFlag", spsCustomer.getProperty("WaitingFlag"));       
       tmpMap.put("EmailAddress", spsCustomer.getProperty("EmailAddress"));
       tmpMap.put("PhoneNumber", spsCustomer.getProperty("PhoneNumber"));
       tmpMap.put("Salutation", spsCustomer.getProperty("Salutation"));
       tmpMap.put("Title", spsCustomer.getProperty("Title"));
       tmpMap.put("Preferences", spsCustomer.getProperty("Preferences"));       
       tmpMap.put("Number", spsCustomer.getProperty("Number"));
       tmpMap.put("IdSource", spsCustomer.getProperty("IdSource"));
       tmpMap.put("UCID", spsCustomer.getProperty("UCID"));                    
       return tmpMap;
    }

    public Map getXentryCustomerMap() {
        return XentryCustomerMap;
    }

    
    
    
}
