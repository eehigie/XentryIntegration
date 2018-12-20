/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.plexada.xentry;

import com.siebel.data.SiebelDataBean;
import com.siebel.data.SiebelException;
import com.siebel.data.SiebelPropertySet;
import com.siebel.eai.SiebelBusinessService;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.xml.sax.SAXException;

/**
 *
 * @author efosa
 */
public class XentryIntegration extends SiebelBusinessService{
    private Map XentryInitJobCustomerConcernMap;
    private Map PlxXentryInitJobServiceMeasureMap;
    private Map PlxXentryOrderMap = new HashMap();
    private Map PlxXentryOrder_ServiceAdvisorMap = new HashMap();
    private Map PlxXentryVehicleMap = new HashMap();
    private Map PlxXentryCustomerMap = new HashMap();
    private SiebelPropertySet psOrder;
    private SiebelPropertySet psCustomer;
    private SiebelPropertySet psVehicle;
    private SiebelDataBean sdb;
    private String browserURL;
    private String jobId;   
    private static final StringWriter ERRORS = new StringWriter();    
    
    
    private String getCurrentTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date now = new Date();
        String strDate = sdfDate.format(now);
        return strDate;
    }
    
    @Override
    public void doInvokeMethod(String methodName, SiebelPropertySet input, SiebelPropertySet output) {
        
        if(methodName.equalsIgnoreCase("InitJob")){            
            MyLogging.log(Level.INFO, "------In InitJob--------");
            MyLogging.log(Level.INFO, "Getting Order,Customer and Vehicle PS from Input--------");
            assignPropertySetsFromInpusPS(input);
               
            MyLogging.log(Level.INFO, "getting Order Map and Service Advisor Map..... ");
            XentryOrder xo = new XentryOrder(psOrder);
            PlxXentryOrderMap = xo.getXentryOrderMap();
            PlxXentryOrder_ServiceAdvisorMap = xo.getXentryOrder_ServiceAdvisorMap();
            
            MyLogging.log(Level.INFO, "getting Customer Map..... ");
            XentryCustomer xc = new XentryCustomer(psCustomer);
            PlxXentryCustomerMap = xc.getXentryCustomerMap();
            
            MyLogging.log(Level.INFO, "getting Vehicle Map..... ");
            XentryVehicle xv = new XentryVehicle(psVehicle);
            PlxXentryVehicleMap = xv.getXentryVehiclerMap();
            
            MyLogging.log(Level.INFO, "building request initjob xml ..... ");
            SiebelPropertySet tmpPs = input.getChild(0);
            SiebelPropertySet initParamPs = tmpPs.getChild(0);
            String siebel_login_name = initParamPs.getProperty("SiebelLoginName");
            String initjob_currency = initParamPs.getProperty("Currency");            
            String tracking_id = initParamPs.getProperty("TrackingId");
            String xentry_user = initParamPs.getProperty("XentryUser");
            String xentry_userpwd = initParamPs.getProperty("XentryUserPwd");
            String xentry_url_endpoint = initParamPs.getProperty("XentryURLEndpoint");
            String initjob_timestamp = getCurrentTimeStamp();
            
            
            MyLogging.log(Level.INFO, "initjob_currency: "+initjob_currency);
            MyLogging.log(Level.INFO, "tracking_id: "+tracking_id);
            MyLogging.log(Level.INFO, "initjob_timestamp: "+initjob_timestamp);
            MyLogging.log(Level.INFO, "xentry_url_endpoint: "+xentry_url_endpoint);
            MyLogging.log(Level.INFO, "siebel_login_name: "+siebel_login_name);
            
            try {
                MyLogging.log(Level.INFO, "Building INIT JOB XML--------");
                InitJob initJob = new InitJob();
                SOAPMessage soap_message = initJob.getSOAPMessage();
                initJob.putSOAPHeader(soap_message, xentry_user, xentry_userpwd);
                SOAPBodyElement sendSyncDataRequestElement = initJob.putSoapBody(soap_message, "SERVICE_JOB_7", "UPDATE", "InitJob");
                SOAPElement dataSynchElement = initJob.putDataSynch(sendSyncDataRequestElement);
                SOAPElement structuredDataElement = initJob.putStructuredData(dataSynchElement);
                SOAPElement messageElement = initJob.putMessage(structuredDataElement);
                SOAPElement businessContextElement = initJob.putBusinessContext(messageElement, "WestStar DMS(Siebel)", "IP2016", "2.4", "REQUEST");
                SOAPElement userContextElement = initJob.putUserContext(messageElement, "P0012CO", siebel_login_name, "en_NG");
                SOAPElement processContextElement = initJob.putProcessContext(messageElement, initjob_timestamp, tracking_id);
                SOAPElement serviceMessageElement = initJob.putServiceMessage(messageElement);
                SOAPElement initJobRequestElement = initJob.putInitJobRequest(serviceMessageElement);                
                SOAPElement jobElement = initJob.putJob(initJobRequestElement, initjob_currency);
                SOAPElement orderElement = initJob.putOrder(jobElement, PlxXentryOrderMap, PlxXentryOrder_ServiceAdvisorMap);
                SOAPElement vehicleElement = initJob.putVehicle(jobElement, PlxXentryVehicleMap);
                SOAPElement customerElement = initJob.putCustomer(jobElement, PlxXentryCustomerMap);
                                
                MyLogging.log(Level.INFO, "OUTPUT REQUEST XML ......................"); 
                final StringWriter sw = new StringWriter();
                TransformerFactory.newInstance().newTransformer().transform(new DOMSource(soap_message.getSOAPPart()),new StreamResult(sw));
                soap_message.writeTo(System.out);
                MyLogging.log(Level.INFO, sw.toString()); 
            
                SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
                SOAPConnection connection = soapConnectionFactory.createConnection();
                URL endpoint = new URL(xentry_url_endpoint);
                SOAPMessage response = connection.call(soap_message, endpoint);
                connection.close();
            
                ParseResponse response_msg = new ParseResponse(response);                
                String compressedData = response_msg.getCompressedData();
                String decompressedData = response_msg.decompress(compressedData);
                String responseCode = response_msg.getResponseCode();                
                MyLogging.log(Level.INFO, "responseCode....."+ responseCode);                
                sdb = ApplicationsConnection.connectSiebelServer();
                SiebelService ssr = new SiebelService(sdb,responseCode);
                if(ssr.isResponseStatus()){
                    MyLogging.log(Level.INFO,"Webservice call completed.....");
                    MyLogging.log(Level.INFO,"Check response.....");
                    response_msg.handleResponse(decompressedData);
                    if(response_msg.isResponseMessageStatus()){
                        MyLogging.log(Level.INFO,"InitJob Response Message Success.....");
                        browserURL = response_msg.getBrowserUrl();
                        jobId = response_msg.getJobId();
                        output.setProperty("Response_Status", "Success");
                        output.setProperty("BrowserURL", browserURL);
                        output.setProperty("JobId", jobId);
                    }else{
                        MyLogging.log(Level.INFO,"InitJob Response Message Failed.....");
                        output.setProperty("Response_Status", "Error");
                        output.setProperty("ErrorCode", response_msg.getErrorId());
                        output.setProperty("ErrorMessage", response_msg.getErrorText()+"::::::"+response_msg.getTechnicalDetails());
                    }
                }else{
                    MyLogging.log(Level.INFO,"InitJob Failed.....");
                    output.setProperty("Response_Status", "Error");
                    output.setProperty("ErrorCode", responseCode);
                    output.setProperty("ErrorMessage", ssr.getResponseCodeDescription());
                }                
            } catch (SOAPException ex) {
                ex.printStackTrace(new PrintWriter(ERRORS));                                                            
                MyLogging.log(Level.SEVERE, "InitJob....."+ ERRORS.toString());
            } catch (IOException ex) {
                ex.printStackTrace(new PrintWriter(ERRORS));                                                            
                MyLogging.log(Level.SEVERE, "InitJob....."+ ERRORS.toString());
            } catch (ParserConfigurationException ex) {
                ex.printStackTrace(new PrintWriter(ERRORS));                                                            
                MyLogging.log(Level.SEVERE, "InitJob....."+ ERRORS.toString());
            } catch (SAXException ex) {
                ex.printStackTrace(new PrintWriter(ERRORS));                                                            
                MyLogging.log(Level.SEVERE, "InitJob....."+ ERRORS.toString());
            } catch (Exception ex) {
                ex.printStackTrace(new PrintWriter(ERRORS));                                                            
                MyLogging.log(Level.SEVERE, "InitJob....."+ ERRORS.toString());
            }finally{
                if(sdb != null){
                    try {
                        sdb.logoff();
                    } catch (SiebelException ex) {
                        ex.printStackTrace(new PrintWriter(ERRORS));                                                            
                        MyLogging.log(Level.SEVERE, "Error, Logging off....."+ ERRORS.toString());
                    }
                }
            }                        
        }else if(methodName.equalsIgnoreCase("InitJobErepko")){
            MyLogging.log(Level.INFO, "------In InitJob--------");
            MyLogging.log(Level.INFO, "Getting Order,Customer and Vehicle PS from Input--------");
            assignPropertySetsFromInpusPS(input);
            String customer_concern_xml = input.getProperty("CustomerConcernXML");
            //MyLogging.log(Level.INFO, "CustomerConcernXML : "+customer_concern_xml);
            MyLogging.log(Level.INFO, "getting Customer Concern data from customer_concern_xml ..... ");
            XentryCustomerConcern xcc = new XentryCustomerConcern(customer_concern_xml);
            XentryInitJobCustomerConcernMap = xcc.getXentryInitJobCustomerConcernMap();
            /*XentryCustomerConcernMap = xcc.getCustomerConcern();
            PlxCustomerConcernNotesMap = xcc.getPlxCustomerConcernNotes();
            PlxCustomerConcernDefectKeyMap = xcc.getPlxCustomerConcernDefectKey();
            PlxCustomerConcernPartsMap = xcc.getPlxCustomerConcernParts();
            PlxCustomerConcernWorkItemMap = xcc.getPlxCustomerConcernWorkItem();
            PlxCustomerConcernServicePackageMap = xcc.getPlxCustomerConcernServicePackage();*/
            
            String service_measure_xml = input.getProperty("ServiceMeasureXML");
            MyLogging.log(Level.INFO, "getting Service Measure data from init_job_xml ..... ");
            XentryServiceMeasure xsm = new XentryServiceMeasure(service_measure_xml);
            PlxXentryInitJobServiceMeasureMap = xsm.getXentryInitJobServiceMeasureMap();
            /*PlxXentryServiceMeasureMap = xsm.getXentryServiceMeasure();
            PlxServiceMeasureNotesAndDefectKeyMap = xsm.getServiceMeasureNotesAndDefectKey();
            PlxServiceMeasurePartsMap = xsm.getPlxServiceMeasureParts();
            PlxServiceMeasureWorkItem = xsm.getPlxServiceMeasureWorkItem();
            PlxServiceMeasurePackageMap = xsm.getPlxServiceMeasurePackageMap();*/
            
            MyLogging.log(Level.INFO, "getting Order Map and Service Advisor Map..... ");
            XentryOrder xo = new XentryOrder(psOrder);
            PlxXentryOrderMap = xo.getXentryOrderMap();
            PlxXentryOrder_ServiceAdvisorMap = xo.getXentryOrder_ServiceAdvisorMap();
            
            MyLogging.log(Level.INFO, "getting Customer Map..... ");
            XentryCustomer xc = new XentryCustomer(psCustomer);
            PlxXentryCustomerMap = xc.getXentryCustomerMap();
            
            MyLogging.log(Level.INFO, "getting Vehicle Map..... ");
            XentryVehicle xv = new XentryVehicle(psVehicle);
            PlxXentryVehicleMap = xv.getXentryVehiclerMap();
            
            MyLogging.log(Level.INFO, "building request initjob xml ..... ");
            String siebel_login_name = input.getProperty("SiebelLoginName");
            String initjob_currency = input.getProperty("Currency");
            //String initjob_jobId = input.getProperty("JobId");
            String tracking_id = input.getProperty("TrackingId");
            String xentry_user = input.getProperty("XentryUser");
            String xentry_userpwd = input.getProperty("XentryUserPwd");
            String xentry_url_endpoint = input.getProperty("XentryURLEndpoint");
            String initjob_timestamp = getCurrentTimeStamp();
            
            
            MyLogging.log(Level.INFO, "initjob_currency: "+initjob_currency);
            MyLogging.log(Level.INFO, "tracking_id: "+tracking_id);
            MyLogging.log(Level.INFO, "initjob_timestamp: "+initjob_timestamp);
            MyLogging.log(Level.INFO, "xentry_url_endpoint: "+xentry_url_endpoint);
            MyLogging.log(Level.INFO, "siebel_login_name: "+siebel_login_name);
            //MyLogging.log(Level.INFO, "initjob_jobId: "+initjob_jobId);
            try {
                InitJob initJob = new InitJob();
                SOAPMessage soap_message = initJob.getSOAPMessage();
                initJob.putSOAPHeader(soap_message, xentry_user, xentry_userpwd);
                SOAPBodyElement sendSyncDataRequestElement = initJob.putSoapBody(soap_message, "SERVICE_JOB_7", "UPDATE", "InitJob");
                SOAPElement dataSynchElement = initJob.putDataSynch(sendSyncDataRequestElement);
                SOAPElement structuredDataElement = initJob.putStructuredData(dataSynchElement);
                SOAPElement messageElement = initJob.putMessage(structuredDataElement);
                SOAPElement businessContextElement = initJob.putBusinessContext(messageElement, "WestStar DMS(Siebel)", "IP2016", "2.4", "REQUEST");
                SOAPElement userContextElement = initJob.putUserContext(messageElement, "P0012CO", siebel_login_name, "en_NG");
                SOAPElement processContextElement = initJob.putProcessContext(messageElement, initjob_timestamp, tracking_id);
                SOAPElement serviceMessageElement = initJob.putServiceMessage(messageElement);
                SOAPElement initJobRequestElement = initJob.putInitJobRequest(serviceMessageElement);
                //SOAPElement jobElement = initJob.putJob(initJobRequestElement, initjob_currency,initjob_jobId);
                SOAPElement jobElement = initJob.putJob(initJobRequestElement, initjob_currency);
                SOAPElement customerConcernElement = initJob.putCustomerConcern(jobElement,XentryInitJobCustomerConcernMap);
                SOAPElement serviceMeasureElement = initJob.putServiceMeasure(jobElement, PlxXentryInitJobServiceMeasureMap);
                SOAPElement orderElement = initJob.putOrder(jobElement, PlxXentryOrderMap, PlxXentryOrder_ServiceAdvisorMap);
                SOAPElement vehicleElement = initJob.putVehicle(jobElement, PlxXentryVehicleMap);
                SOAPElement customerElement = initJob.putCustomer(jobElement, PlxXentryCustomerMap);
                //SOAPElement customerConcernElement = initJob.putCustomerConcern(jobElement,XentryCustomerConcernMap, PlxCustomerConcernNotesMap, PlxCustomerConcernDefectKeyMap,PlxCustomerConcernPartsMap, PlxCustomerConcernWorkItemMap,PlxCustomerConcernServicePackageMap);
                //SOAPElement serviceMeasureElement = initJob.putServiceMeasure(jobElement, serviceMeasureMap, notesMap, defectKeyMap, emptyMap, emptyMap, emptyMap);
                //SOAPElement orderElement = initJob.putOrder(jobElement, orderMap, emptyMap);
                //SOAPElement vehicleElement = initJob.putVehicle(jobElement, emptyMap, emptyMap);
                //SOAPElement customerElement = initJob.putCustomer(jobElement, emptyMap, emptyMap);
                
                MyLogging.log(Level.INFO, "OUTPUT REQUEST XML ......................");
                soap_message.writeTo(System.out);
            
                SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
                SOAPConnection connection = soapConnectionFactory.createConnection();
                URL endpoint = new URL(xentry_url_endpoint);
                SOAPMessage response = connection.call(soap_message, endpoint);
                connection.close();
            
                ParseResponse response_msg = new ParseResponse(response);
                //response_msg.getResponseNodes();
                String compressedData = response_msg.getCompressedData();
                //String decompressedData = response_msg.decompress(compressedData);
                //byte[] resultData = response_msg.unzipData(compressedData);
               // String sResultData = new String(resultData);
               //MyLogging.log(Level.INFO, "decompressedData....."+ sResultData);
            } catch (SOAPException ex) {
                ex.printStackTrace(new PrintWriter(ERRORS));                                                            
                MyLogging.log(Level.SEVERE, "InitJob....."+ ERRORS.toString());
            } catch (IOException ex) {
                ex.printStackTrace(new PrintWriter(ERRORS));                                                            
                MyLogging.log(Level.SEVERE, "InitJob....."+ ERRORS.toString());
            } catch (ParserConfigurationException ex) {
                ex.printStackTrace(new PrintWriter(ERRORS));                                                            
                MyLogging.log(Level.SEVERE, "InitJob....."+ ERRORS.toString());
            } catch (SAXException ex) {
                ex.printStackTrace(new PrintWriter(ERRORS));                                                            
                MyLogging.log(Level.SEVERE, "InitJob....."+ ERRORS.toString());
            } catch (Exception ex) {
                ex.printStackTrace(new PrintWriter(ERRORS));                                                            
                MyLogging.log(Level.SEVERE, "InitJob....."+ ERRORS.toString());
            }finally{
                if(sdb != null){
                    try {
                        sdb.logoff();
                    } catch (SiebelException ex) {
                        ex.printStackTrace(new PrintWriter(ERRORS));                                                            
                        MyLogging.log(Level.SEVERE, "Error, Logging off....."+ ERRORS.toString());
                    }
                }
            }
        }
        
    }
    
    private void assignPropertySetsFromInpusPS(SiebelPropertySet sps){
        int psChild = sps.getChildCount();
        MyLogging.log(Level.SEVERE, "assignPropertySetsFromInpusPS Method.....Child Count:"+ psChild);        
        SiebelPropertySet grandChildPS = sps.getChild(0);
        MyLogging.log(Level.SEVERE, "assignPropertySetsFromInpusPS Method.....GrandChild Count:"+ grandChildPS.getChildCount());
        int psGrandChild = grandChildPS.getChildCount();
        SiebelPropertySet greatGrandChildPS = grandChildPS.getChild(0);
        MyLogging.log(Level.SEVERE, "assignPropertySetsFromInpusPS Method.....GreatGrandChild Count:"+ greatGrandChildPS.getChildCount());
        int psGreatGrandChild = greatGrandChildPS.getChildCount();
        for(int i = 0; i < psGreatGrandChild; ++i){
          SiebelPropertySet tmpPs =  greatGrandChildPS.getChild(i);
          if(tmpPs.getType().equalsIgnoreCase("Order")){
              psOrder = greatGrandChildPS.getChild(i);
              MyLogging.log(Level.SEVERE, "Order retreived");
          }else if(tmpPs.getType().equalsIgnoreCase("Vehicle")){
              psVehicle = greatGrandChildPS.getChild(i);
              MyLogging.log(Level.SEVERE, "Vehicle retreived");
          }else if(tmpPs.getType().equalsIgnoreCase("Customer")){
              psCustomer = greatGrandChildPS.getChild(i);
              MyLogging.log(Level.SEVERE, "Customer retreived");
          }
        }
    }
    
    public static void main(String[] args) {
        SiebelPropertySet spsInput = new SiebelPropertySet();
        SiebelPropertySet spsOutput = new SiebelPropertySet();
        SiebelPropertySet tempPS = new SiebelPropertySet();
        SiebelPropertySet tempPS2 = new SiebelPropertySet();
        tempPS.setProperty("methodName", "InitJob");
        tempPS.setProperty("SiebelLoginName", "EEHIGIE");
        tempPS.setProperty("XentryUser", "TD02136");
        tempPS.setProperty("XentryUserPwd", "9Lh8hdyDtY");
        tempPS.setProperty("CustomerConcernXML", new TestString().xc2);
        tempPS.setProperty("ServiceMeasureXML", new TestString().xd);
        tempPS.setProperty("Currency","EUR");
        tempPS.setProperty("TrackingId","TJ24_1.1");
        tempPS.setProperty("XentryURLEndpoint","https://srs-ds-int1.i.daimler.com/STARCDS/services/ExternalInterface");
        
        SiebelPropertySet psI = new SiebelPropertySet();
        SiebelPropertySet psOrder = new SiebelPropertySet();
        psOrder.setType("Order");
        /*psOrder.setProperty("OrderId", "");
        psOrder.setProperty("PaymentMethod", "optional payment");
        psOrder.setProperty("ReceptionDateTime", "");
        psOrder.setProperty("ReturnDateTime", "");*/
        psOrder.setProperty("OrderId", "1234");
        psOrder.setProperty("PaymentMethod", "optional payment");
        psOrder.setProperty("ReceptionDateTime", "2016-05-01T12:00:00");
        psOrder.setProperty("ReturnDateTime", "2016-05-02T12:00:00");
        
        
        SiebelPropertySet psOrderServiceAdvisor = new SiebelPropertySet();
        psOrderServiceAdvisor.setProperty("FirstName", "Max");
        psOrderServiceAdvisor.setProperty("LastName", "Mustermann");
        psOrderServiceAdvisor.setProperty("Abbreviation", "testerde");
        psOrder.addChild(psOrderServiceAdvisor);
        
        
        SiebelPropertySet psVehicle = new SiebelPropertySet();
        psVehicle.setType("Vehicle");
        psVehicle.setProperty("FinOrVin", "WDD2040221A000658");
        psVehicle.setProperty("FirstRegistrationDate", "2001-12-31");
        psVehicle.setProperty("OdometerReading", "51002");
        psVehicle.setProperty("OdometerUnit", "km");
        psVehicle.setProperty("OperatingHours", "90000");
        psVehicle.setProperty("RegistrationNumber", "ES NT 2511");
        
        SiebelPropertySet psCustomer = new SiebelPropertySet();
        psCustomer.setType("Customer");
        psCustomer.setProperty("FirstName", "Max");
        psCustomer.setProperty("LastName", "Mustermann");
        psCustomer.setProperty("CallbackFlag", "false");
        psCustomer.setProperty("WaitingFlag", "false");
        psCustomer.setProperty("EmailAddress", "max.mustermann@org.com");
        psCustomer.setProperty("PhoneNumber", "123456789");
        psCustomer.setProperty("Salutation", "Herr");
        psCustomer.setProperty("Title", "Prof Dr");
        psCustomer.setProperty("Preferences", "car wash");
        psCustomer.setProperty("Number", "dmsNumber");
        psCustomer.setProperty("IdSource", "central system");
        psCustomer.setProperty("UCID", "1234567890123456789");
        
        tempPS.addChild(psOrder);
        tempPS.addChild(psVehicle);
        tempPS.addChild(psCustomer);
        tempPS2.addChild(tempPS); 
        spsInput.addChild(tempPS2);
        
        new XentryIntegration().doInvokeMethod("InitJob", spsInput, spsOutput);
        
        
        
        
        /*Map customerConcernMap = new HashMap();
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
        }*/
    }
}
