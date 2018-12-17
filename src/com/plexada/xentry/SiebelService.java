/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.plexada.xentry;

import com.siebel.data.SiebelBusComp;
import com.siebel.data.SiebelBusObject;
import com.siebel.data.SiebelDataBean;
import com.siebel.data.SiebelException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author efosa
 */
public class SiebelService {
    
    private static SiebelDataBean sieb_data_bean = null;
    private static String responseCodeDescription;
    private static String responseCodeGroup;
    private static boolean responseStatus;
    
    public SiebelService(SiebelDataBean sdb,String code) throws SiebelException {
        sieb_data_bean = sdb;
        responseCodeDescription = "";
        responseCodeGroup = "";
        responseStatus = true;
        getCodeGroup(code);        
    }
    
    private static void getCodeGroup(String code) throws SiebelException{
        SiebelBusObject boObj = sieb_data_bean.getBusObject("eAuto Fault Trouble");
        SiebelBusComp bcObj = boObj.getBusComp("eAuto Fault Code");        
        bcObj.activateField("Description");
        bcObj.activateField("Group Code"); 
        bcObj.setViewMode(3);
        bcObj.clearToQuery();
        bcObj.setSearchSpec("Code Name", code);
        bcObj.executeQuery2(true, true);						
	if(bcObj.firstRecord()){
            System.out.println("In record:");            
            responseCodeDescription = bcObj.getFieldValue("Description");
            responseCodeGroup = bcObj.getFieldValue("Group Code");
            MyLogging.log(Level.INFO,"responseCodeDescription:"+responseCodeDescription);
            MyLogging.log(Level.INFO,"responseCodeGroup:"+responseCodeGroup);
            if(responseCodeGroup.equalsIgnoreCase("ERROR")){
                responseStatus = false;
            }
        }                
    }

    public String getResponseCodeDescription() {
        return responseCodeDescription;
    }

    public String getResponseCodeGroup() {
        return responseCodeGroup;
    }

    public boolean isResponseStatus() {
        return responseStatus;
    }
    
    public static void main(String[] args) {
        try {
            SiebelService ss = new SiebelService(ApplicationsConnection.connectSiebelServer(),"STARCDS001002");
            System.out.println("responseStatus:"+responseStatus);
        } catch (SiebelException ex) {
            Logger.getLogger(SiebelService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SiebelService.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
