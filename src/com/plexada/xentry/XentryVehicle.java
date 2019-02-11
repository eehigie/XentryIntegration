/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.plexada.xentry;

/**
 *
 * @author efosa
 */
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
import java.util.logging.Logger;

public class XentryVehicle {
    
    private final SiebelPropertySet psVehicle;
    private Map xentryVehiclerMap;
    private static final StringWriter ERRORS = new StringWriter();
    
    public XentryVehicle(SiebelPropertySet spsVehicle) {
        this.psVehicle = spsVehicle;
        xentryVehiclerMap = new HashMap();
        xentryVehiclerMap = this.converSiebelPropertySetToMap(this.psVehicle);
    }
    
    private String convertDateToProperFormat(String dateStr) throws ParseException{                    
        DateFormat srcDf = new SimpleDateFormat("MM/dd/yyyy");             
        // parse the date string into Date object
        Date date = srcDf.parse(dateStr);             
        DateFormat destDf = new SimpleDateFormat("yyyy-MM-dd");             
        // format the date into another format
        dateStr = destDf.format(date);             
        MyLogging.log(Level.INFO,"Converted date is : " + dateStr); 
        return dateStr;
    }
    
    private Map converSiebelPropertySetToMap(SiebelPropertySet spsVehicle){
       Map tmpMap = new HashMap();
       //Map tmpMap_2 = new HashMap();
       
       tmpMap.put("FinOrVin", spsVehicle.getProperty("FinOrVin"));
        try {
            tmpMap.put("FirstRegistrationDate", convertDateToProperFormat(spsVehicle.getProperty("FirstRegistrationDate")));
        } catch (ParseException ex) {
            ex.printStackTrace(new PrintWriter(ERRORS));                                                            
            MyLogging.log(Level.SEVERE, "converSiebelPropertySetToMap....."+ ERRORS.toString()); 
        }
       tmpMap.put("OdometerReading", spsVehicle.getProperty("OdometerReading"));
       tmpMap.put("OdometerUnit", spsVehicle.getProperty("OdometerUnit"));       
       tmpMap.put("OperatingHours", spsVehicle.getProperty("OperatingHours"));
       tmpMap.put("RegistrationNumber", spsVehicle.getProperty("RegistrationNumber"));
       /*tmpMap.put("Salutation", spsVehicle.getProperty("Salutation"));
       tmpMap.put("Title", spsVehicle.getProperty("Title"));
       tmpMap.put("Preferences", spsVehicle.getProperty("Preferences"));       
       tmpMap.put("Number", spsVehicle.getProperty("Number"));
       tmpMap.put("IdSource", spsVehicle.getProperty("IdSource"));
       tmpMap.put("UCID", spsVehicle.getProperty("UCID"));*/               
       return tmpMap;
    }

    public Map getXentryVehiclerMap() {
        return xentryVehiclerMap;
    }
    
    
    
}
