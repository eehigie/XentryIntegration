/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.plexada.xentry;

import com.siebel.data.SiebelPropertySet;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 *
 * @author efosa
 */
public class XentryOrder {
    
    private final SiebelPropertySet psOrder;
    private Map XentryOrderMap;
    private Map XentryOrder_ServiceAdvisorMap;
    private static final StringWriter ERRORS = new StringWriter();
    
    
    public XentryOrder(SiebelPropertySet spsOrder){
        this.psOrder = spsOrder;
        this.XentryOrderMap = new HashMap();
        this.XentryOrder_ServiceAdvisorMap = new HashMap();
        XentryOrderMap = converSiebelPropertySetToMap(this.psOrder);
    }
    
    private String convertDateToProperFormat(String dateStr) throws ParseException{
        //String dateStr = "21/20/2011";             
        DateFormat srcDf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");             
        // parse the date string into Date object
        Date date = srcDf.parse(dateStr);             
        DateFormat destDf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");             
        // format the date into another format
        dateStr = destDf.format(date);             
        MyLogging.log(Level.INFO,"Converted date is : " + dateStr); 
        return dateStr;
    }
    
    private Map converSiebelPropertySetToMap(SiebelPropertySet spsOrder){
      Map tmpMap = new HashMap();
      try
      {
       
       Map tmpMap_2 = new HashMap();
       SiebelPropertySet spsOrderServiceAdvisor = new SiebelPropertySet();
       tmpMap.put("OrderId", spsOrder.getProperty("OrderId"));
       tmpMap.put("PaymentMethod", spsOrder.getProperty("PaymentMethod"));
       tmpMap.put("ReceptionDateTime", convertDateToProperFormat(spsOrder.getProperty("ReceptionDateTime")));
       tmpMap.put("ReturnDateTime", convertDateToProperFormat(spsOrder.getProperty("ReturnDateTime")));
       
       spsOrderServiceAdvisor = spsOrder.getChild(0);
       tmpMap_2.put("FirstName", spsOrderServiceAdvisor.getProperty("FirstName"));
       tmpMap_2.put("LastName", spsOrderServiceAdvisor.getProperty("LastName"));
       tmpMap_2.put("Abbreviation", spsOrderServiceAdvisor.getProperty("Abbreviation"));
       XentryOrder_ServiceAdvisorMap = tmpMap_2;
       tmpMap.put("OrderServiceAdvisor", tmpMap_2);
      }catch(ParseException ex){
         ex.printStackTrace(new PrintWriter(ERRORS));                                                            
         MyLogging.log(Level.SEVERE, "converSiebelPropertySetToMap....."+ ERRORS.toString()); 
      }
       
       return tmpMap;
    }
    
    public Map getXentryOrderMap() {
        return XentryOrderMap;
    }

    public Map getXentryOrder_ServiceAdvisorMap() {
        return XentryOrder_ServiceAdvisorMap;
    }
    
}
