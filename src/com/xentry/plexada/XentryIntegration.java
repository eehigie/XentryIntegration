/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xentry.plexada;

import com.siebel.data.SiebelPropertySet;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.zip.GZIPInputStream;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import org.xml.sax.SAXException;

/**
 *
 * @author efosa
 */
public class XentryIntegration {
    private Map XentryInitJobCustomerConcernMap;
    private Map PlxXentryInitJobServiceMeasureMap;
    private Map PlxXentryOrderMap = new HashMap();
    private Map PlxXentryOrder_ServiceAdvisorMap = new HashMap();
    private Map PlxXentryVehicleMap = new HashMap();
    private Map PlxXentryCustomerMap = new HashMap();
    private SiebelPropertySet psOrder;
    private SiebelPropertySet psCustomer;
    private SiebelPropertySet psVehicle;
    /*private Map XentryCustomerConcernMap;
    private Map PlxCustomerConcernNotesMap;
    private Map PlxCustomerConcernDefectKeyMap;
    private Map PlxCustomerConcernPartsMap;
    private Map PlxCustomerConcernWorkItemMap;
    private Map PlxCustomerConcernServicePackageMap;
    
    
    private Map PlxServiceMeasureNotesAndDefectKeyMap;
    private Map PlxServiceMeasurePartsMap;
    private Map PlxServiceMeasureWorkItem;
    private Map PlxServiceMeasurePackageMap;*/
    private static final StringWriter ERRORS = new StringWriter();
    //private final Map EMPTY_MAP = new HashMap();
    
    public static String decompress(byte[] bytes) throws Exception {
    
        GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(bytes));
        return gis.toString();
    }
    
    private String getCurrentTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date now = new Date();
        String strDate = sdfDate.format(now);
        return strDate;
    }
    public void doInvokeMethod(String methodName, SiebelPropertySet input, SiebelPropertySet output) {
        
        if(methodName.equalsIgnoreCase("InitJob")){            
            MyLogging.log(Level.INFO, "------In InitJob--------");
            MyLogging.log(Level.INFO, "Getting Order,Customer and Vehicle PS from Input--------");
            assignPropertySetsFromInpusPS(input);
            String customer_concern_xml = input.getProperty("CustomerConcernXML");            
            MyLogging.log(Level.INFO, "getting Customer Concern data from customer_concern_xml ..... ");
            XentryCustomerConcern xcc = new XentryCustomerConcern(customer_concern_xml);
            XentryInitJobCustomerConcernMap = xcc.getXentryInitJobCustomerConcernMap();
                        
            String service_measure_xml = input.getProperty("ServiceMeasureXML");
            MyLogging.log(Level.INFO, "getting Service Measure data from init_job_xml ..... ");
            XentryServiceMeasure xsm = new XentryServiceMeasure(service_measure_xml);
            PlxXentryInitJobServiceMeasureMap = xsm.getXentryInitJobServiceMeasureMap();
                        
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
                SOAPElement jobElement = initJob.putJob(initJobRequestElement, initjob_currency);
                //SOAPElement customerConcernElement = initJob.putCustomerConcern(jobElement,XentryInitJobCustomerConcernMap);
                //SOAPElement serviceMeasureElement = initJob.putServiceMeasure(jobElement, PlxXentryInitJobServiceMeasureMap);
                SOAPElement orderElement = initJob.putOrder(jobElement, PlxXentryOrderMap, PlxXentryOrder_ServiceAdvisorMap);
                SOAPElement vehicleElement = initJob.putVehicle(jobElement, PlxXentryVehicleMap);
                SOAPElement customerElement = initJob.putCustomer(jobElement, PlxXentryCustomerMap);
                                
                MyLogging.log(Level.INFO, "OUTPUT REQUEST XML ......................");
                soap_message.writeTo(System.out);
            
                SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
                SOAPConnection connection = soapConnectionFactory.createConnection();
                URL endpoint = new URL(xentry_url_endpoint);
                SOAPMessage response = connection.call(soap_message, endpoint);
                connection.close();
            
                ParseResponse response_msg = new ParseResponse(response);                
                String compressedData = response_msg.getCompressedData();
                String responseCode = response_msg.getResponseCode();
                
                MyLogging.log(Level.INFO, "responseCode....."+ responseCode);
                //byte[] resultData = response_msg.unzipData(compressedData);
                //String sResultData = new String(resultData);
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
                byte[] resultData = response_msg.unzipData(compressedData);
                String sResultData = new String(resultData);
               MyLogging.log(Level.INFO, "decompressedData....."+ sResultData);
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
            }
        }
        
    }
    
    private void assignPropertySetsFromInpusPS(SiebelPropertySet sps){
        int psChild = sps.getChildCount();
        for(int i = 0; i < psChild; ++i){
          SiebelPropertySet tmpPs =  sps.getChild(i);
          if(tmpPs.getType().equalsIgnoreCase("Order")){
              psOrder = sps.getChild(i);
          }else if(tmpPs.getType().equalsIgnoreCase("Vehicle")){
              psVehicle = sps.getChild(i);
          }else if(tmpPs.getType().equalsIgnoreCase("Customer")){
              psCustomer = sps.getChild(i);
          }
        }
    }
    
    public static void main(String[] args) {
        SiebelPropertySet spsInput = new SiebelPropertySet();
        SiebelPropertySet spsOutput = new SiebelPropertySet();
        spsInput.setProperty("methodName", "InitJob");
        spsInput.setProperty("SiebelLoginName", "EEHIGIE");
        spsInput.setProperty("XentryUser", "TD02136");
        spsInput.setProperty("XentryUserPwd", "9Lh8hdyDtY");
        spsInput.setProperty("CustomerConcernXML", new TestString().xc2);
        spsInput.setProperty("ServiceMeasureXML", new TestString().xd);
        spsInput.setProperty("Currency","EUR");
        spsInput.setProperty("TrackingId","TJ24_1.1");
        spsInput.setProperty("XentryURLEndpoint","https://srs-ds-int1.i.daimler.com/STARCDS/services/ExternalInterface");
        
        SiebelPropertySet psI = new SiebelPropertySet();
        SiebelPropertySet psOrder = new SiebelPropertySet();
        psOrder.setType("Order");
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
        
        spsInput.addChild(psOrder);
        spsInput.addChild(psVehicle);
        spsInput.addChild(psCustomer);
                
        
        
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
