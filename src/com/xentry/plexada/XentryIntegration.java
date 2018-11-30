/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xentry.plexada;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.zip.GZIPInputStream;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

/**
 *
 * @author efosa
 */
public class XentryIntegration {
    
    private static final StringWriter ERRORS = new StringWriter();
    
    
    public static String decompress(byte[] bytes) throws Exception {
    
        GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(bytes));
        return gis.toString();
    }
    
    public static void main(String[] args) {
        Map customerConcernMap = new HashMap();
        customerConcernMap.put("Classification","Unclassified");
        customerConcernMap.put("Title","Unclassified");
        customerConcernMap.put("ExternalId","1");
        
        Map notesMap = new HashMap();
        notesMap.put("CustomerStatement","Something to do");
        notesMap.put("Cause","want something new");
        notesMap.put("Correction","we will see");
        
        
        Map serviceMeasureMap = new HashMap();
        serviceMeasureMap.put("Description","Replace drive shaft");
        serviceMeasureMap.put("Selected","true");
        serviceMeasureMap.put("Type","KDM");
        
        
        Map orderMap = new HashMap();
        orderMap.put("OrderId","abc");
        orderMap.put("PaymentMethod","optional payment");
        orderMap.put("ReceptionDateTime","2016-05-01T12:00:00");
        orderMap.put("ReturnDateTime","2016-05-01T12:00:00");
        
        
        Map defectKeyMap = new HashMap();
        defectKeyMap.put("Code","123456789");
        defectKeyMap.put("Description","true");
        
        Map emptyMap = new HashMap();
                
     
        try {
            InitJob initJob = new InitJob();
            SOAPMessage soap_message = initJob.getSOAPMessage();
            initJob.putSOAPHeader(soap_message, "TD02136", "9Lh8hdyDtY");
            SOAPBodyElement sendSyncDataRequestElement = initJob.putSoapBody(soap_message, "SERVICE_JOB_7", "UPDATE", "InitJob");
            SOAPElement dataSynchElement = initJob.putDataSynch(sendSyncDataRequestElement);
            SOAPElement structuredDataElement = initJob.putStructuredData(dataSynchElement);
            SOAPElement messageElement = initJob.putMessage(structuredDataElement);
            SOAPElement businessContextElement = initJob.putBusinessContext(messageElement, "SampleDMS SOAPUI", "Sample DMS Version 10.5a", "2.4", "REQUEST");
            SOAPElement userContextElement = initJob.putUserContext(messageElement, "P0012CO", "mustermann", "de_DE");
            SOAPElement processContextElement = initJob.putProcessContext(messageElement, "2016-09-09T11:14:00", "InitJob_1990");
            SOAPElement serviceMessageElement = initJob.putServiceMessage(messageElement);
            SOAPElement initJobRequestElement = initJob.putInitJobRequest(serviceMessageElement);
            SOAPElement jobElement = initJob.putJob(initJobRequestElement, "EUR");
            SOAPElement customerConcernElement = initJob.putCustomerConcern(jobElement,customerConcernMap, notesMap, defectKeyMap,emptyMap, emptyMap,emptyMap);
            SOAPElement serviceMeasureElement = initJob.putServiceMeasure(jobElement, serviceMeasureMap, notesMap, defectKeyMap, emptyMap, emptyMap, emptyMap);
            SOAPElement orderElement = initJob.putOrder(jobElement, orderMap, emptyMap);
            SOAPElement vehicleElement = initJob.putVehicle(jobElement, emptyMap, emptyMap);
            SOAPElement customerElement = initJob.putCustomer(jobElement, emptyMap, emptyMap);
            
            soap_message.writeTo(System.out);
            
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection connection = soapConnectionFactory.createConnection();
            URL endpoint = new URL("https://srs-ds-int1.i.daimler.com/STARCDS/services/ExternalInterface");
            SOAPMessage response = connection.call(soap_message, endpoint);
            connection.close();
            
            ParseResponse response_msg = new ParseResponse(response);
            response_msg.getResponseNodes();
            
            
        } catch (SOAPException ex) {
            ex.printStackTrace(new PrintWriter(ERRORS));                                                            
            MyLogging.log(Level.SEVERE, "RefreshPrices ERROR::FileNotFoundException....."+ ERRORS.toString());
        } catch (IOException ex) {
            ex.printStackTrace(new PrintWriter(ERRORS));                                                            
            MyLogging.log(Level.SEVERE, "RefreshPrices ERROR::FileNotFoundException....."+ ERRORS.toString());
        }
    }
}
