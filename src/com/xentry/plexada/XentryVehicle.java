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

public class XentryVehicle {
    
    private final SiebelPropertySet psVehicle;
    private Map xentryVehiclerMap;

    public XentryVehicle(SiebelPropertySet spsVehicle) {
        this.psVehicle = spsVehicle;
        xentryVehiclerMap = new HashMap();
        xentryVehiclerMap = this.converSiebelPropertySetToMap(this.psVehicle);
    }
    
    private Map converSiebelPropertySetToMap(SiebelPropertySet spsVehicle){
       Map tmpMap = new HashMap();
       //Map tmpMap_2 = new HashMap();
       
       tmpMap.put("FinOrVin", spsVehicle.getProperty("FinOrVin"));
       tmpMap.put("FirstRegistrationDate", spsVehicle.getProperty("FirstRegistrationDate"));
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
