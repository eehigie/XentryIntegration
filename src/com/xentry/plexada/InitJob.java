/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xentry.plexada;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPElement;
//import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
//import javax.xml.soap.SOAPPart;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author efosa
 */
public class InitJob {
    private static final StringWriter ERRORS = new StringWriter();
    private static SOAPMessage soap_message = null;
    private static SOAPElement PartElement;
    private static SOAPElement WorkItemElement;
    private static SOAPElement ServicePackageElement;
    private static SOAPElement ServiceMeasurePackageElement ;
    
    public SOAPElement getPartElement(){
        return PartElement;
    }
    
    public SOAPElement getWorkItemElement(){
        return WorkItemElement;
    }
    
    public SOAPElement getServicePackageElement(){
        return ServicePackageElement;
    }
    
    private static SOAPElement AddDataElement(SOAPBodyElement sendSyncDataRequestElement,String URIName, String xsdName, String elementName, String elementValue) throws SOAPException{                
        QName name = new QName(URIName,elementName,xsdName);       
        SOAPElement symbol = sendSyncDataRequestElement.addChildElement(name);
        if(!elementValue.isEmpty()){
          symbol.addTextNode(elementValue);  
        }        
        return symbol;
    }
    
    private static SOAPElement AddDataElement(SOAPElement dataElement,String URIName, String xsdName, String elementName, String elementValue) throws SOAPException{                
        QName name = new QName(URIName,elementName,xsdName);       
        SOAPElement symbol = dataElement.addChildElement(name);
        if(!elementValue.isEmpty()){
          symbol.addTextNode(elementValue);  
        }        
        return symbol;
    }
    
    private static void AddAttributes(SOAPElement dataElement, Map elementAttributes) throws SOAPException{
        Iterator iter = elementAttributes.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry mEntry = (Map.Entry) iter.next();            
            QName q_name = new QName(String.valueOf(mEntry.getKey()));            
            dataElement.addAttribute(q_name, String.valueOf(mEntry.getValue()));            
	}
    }
    
    private static void AddAttributes(SOAPElement dataElement, Map elementAttributes, String subMapName,String subElement) throws SOAPException{
        Iterator iter = elementAttributes.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry mEntry = (Map.Entry) iter.next();            
            QName q_name = new QName(String.valueOf(mEntry.getKey()));
            
            dataElement.addAttribute(q_name, String.valueOf(mEntry.getValue()));    
            
            SOAPElement theSubElement = AddDataElement(dataElement,"http://dms.ri.as.daimler.com/DMSService/types","types",subElement,""); 
            Map tmpMap = (Map)elementAttributes.get(subMapName);
            if(!tmpMap.isEmpty()){
               AddAttributes(theSubElement, tmpMap);  
            }            
	}
    }
    
    private static void AddAttributes(SOAPElement dataElement, Map elementAttributes, String IgnoreString) throws SOAPException{
        Iterator iter = elementAttributes.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry mEntry = (Map.Entry) iter.next();            
            QName q_name = new QName(String.valueOf(mEntry.getKey())); 
            if(!String.valueOf(q_name).equalsIgnoreCase(IgnoreString))
                dataElement.addAttribute(q_name, String.valueOf(mEntry.getValue()));                                                   
	}
    }
    
    private static void AddAttributes(SOAPElement dataElement, Map elementAttributes, List lst) throws SOAPException{
        Iterator iter = elementAttributes.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry mEntry = (Map.Entry) iter.next();            
            QName q_name = new QName(String.valueOf(mEntry.getKey())); 
            if(!lst.contains(String.valueOf(q_name)))
                dataElement.addAttribute(q_name, String.valueOf(mEntry.getValue()));                                                   
	}
    }
    
    public static SOAPBodyElement getChildNode(SOAPBodyElement element, String name) throws SOAPException {

    @SuppressWarnings("unchecked")
    Iterator<SOAPBodyElement> elements = element.getChildElements();

    while (elements.hasNext()) {
        SOAPBodyElement childNode = elements.next();

        if (childNode.getNodeName().contains(name)) {
            return childNode;
        }
    }

        throw new SOAPException("Child element: " + name + " not found.");
    }
    
    
    public static void parseThroughDocument(Node node) {
    // do something with the current node instead of System.out
    System.out.println("Name: "+node.getNodeName());
    System.out.print("Value: "+node.getNodeValue());
    NodeList nodeList = node.getChildNodes();
    for (int i = 0; i < nodeList.getLength(); i++) {
        Node currentNode = nodeList.item(i);
        if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
            //calls this method for all the children which is Element
            parseThroughDocument(currentNode);
        }
    }
    }
    
    public static String getTextValue(Node node) {
    StringBuffer textValue = new StringBuffer();
    int length = node.getChildNodes().getLength();
    for (int i = 0; i < length; i ++) {
      Node c = node.getChildNodes().item(i);
      if (c.getNodeType() == Node.TEXT_NODE) {
        textValue.append(c.getNodeValue());
      }
    }
    return textValue.toString().trim();
  }
    /**
     * @param args the command line arguments
     * @return 
     * @throws javax.xml.soap.SOAPException 
     */
    
    public SOAPMessage getSOAPMessage (){
        return soap_message;
    }
    
    public InitJob() throws SOAPException {
        MessageFactory factory = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
        soap_message = factory.createMessage();        
    }
    
    public void putSOAPHeader(SOAPMessage soap_message,String xentry_userName, String xentry_password) throws SOAPException{
        SOAPHeader header = soap_message.getSOAPHeader();
        QName headerName = new QName("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd","Security", "wsse");
        QName userNameToken =  new QName("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd","UsernameToken","wsse");
        QName userName = new QName("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd","Username","wsse");
        QName pssWd = new QName("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd","Password","wsse");
        
        SOAPHeaderElement headerElement = header.addHeaderElement(headerName);
        SOAPElement userNameTokenElement = headerElement.addChildElement(userNameToken);
        
        SOAPElement userNameElement = userNameTokenElement.addChildElement(userName);
        SOAPElement pssWdElement = userNameTokenElement.addChildElement(pssWd);
        userNameElement.addTextNode(xentry_userName);
        QName psswdType = new QName("Type");
        pssWdElement.addAttribute(psswdType,"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText");
        pssWdElement.addTextNode(xentry_password);
    }
    
    public SOAPBodyElement putSoapBody(SOAPMessage soap_message,String type, String event, String operation) throws SOAPException{
        SOAPBody body = soap_message.getSOAPBody();
        QName sendSyncDataRequest =  new QName("http://stc.daimler.com/2009/08/DS/ext", "sendSyncDataRequest", "ext");                   
        SOAPBodyElement sendSyncDataRequestElement = body.addBodyElement(sendSyncDataRequest);
        QName sendSyncDataRequestVersion = new QName("ext:version");
        QName sendSyncDataRequestSchemaLocation = new QName("xsi:schemaLocation");
        QName sendSyncDataRequestDs = new QName("xmlns:ds");
        QName sendSyncDataRequestXsi = new QName("xmlns:xsi");
        sendSyncDataRequestElement.addAttribute(sendSyncDataRequestVersion, "1.4");
        sendSyncDataRequestElement.addAttribute(sendSyncDataRequestSchemaLocation, "http://stc.daimler.com/2009/08/DS/ext file:///C:/Xentry_Portal/Schnittstellen_Spezifikationen/XML-Schema_V_1.4.5/XML-Instanzen-V1.4.5/03_DataServer_Instanzen/XML-Artefacts/XML-Artefacts/StarConnectDataServerExt.xsd");
        sendSyncDataRequestElement.addAttribute(sendSyncDataRequestDs, "http://stc.daimler.com/2009/08/DS");
        sendSyncDataRequestElement.addAttribute(sendSyncDataRequestXsi, "http://www.w3.org/2001/XMLSchema-instance");
        AddDataElement(sendSyncDataRequestElement,"http://stc.daimler.com/2009/08/DS/ext","ext","type",type);
        AddDataElement(sendSyncDataRequestElement,"http://stc.daimler.com/2009/08/DS/ext","ext","event",event);
        AddDataElement(sendSyncDataRequestElement,"http://stc.daimler.com/2009/08/DS/ext","ext","operation",operation);
        return sendSyncDataRequestElement;
    }
    
    public SOAPElement putDataSynch(SOAPBodyElement sendSyncDataRequestElement) throws SOAPException{
        SOAPElement dataSynchElement = AddDataElement(sendSyncDataRequestElement,"http://stc.daimler.com/2009/08/DS/ext","ext","data","");
        return dataSynchElement;
    } 
    
    
    public SOAPElement putStructuredData(SOAPElement dataSynchElement) throws SOAPException{
        SOAPElement structuredDataElement = AddDataElement(dataSynchElement,"http://stc.daimler.com/2009/08/DS","ds","structuredData","");
        return structuredDataElement;
    }
    
    public SOAPElement putMessage(SOAPElement structuredDataElement) throws SOAPException{
        SOAPElement messageElement = AddDataElement(structuredDataElement,"http://dms.ri.as.daimler.com/DMSService","env","Message","");                        
        QName messageElementSchemaLocation = new QName("xsi:schemaLocation");
        QName messageElementDms = new QName("xmlns:dms");
        QName messageElementTypes = new QName("xmlns:types"); 
        QName messageElementCurr = new QName("xmlns:cur");
        QName messageElementXp = new QName("xmlns:xp");
            
        messageElement.addAttribute(messageElementSchemaLocation, "http://dms.ri.as.daimler.com/DMSService IF_XP_DMS_2_4_message_envelope.xsd http://dms.ri.as.daimler.com/DMSService/dms_sending IF_XP_DMS_2_4_dms_sending.xsd http://dms.ri.as.daimler.com/DMSService/types IF_XP_DMS_2_4_types.xsd http://dms.ri.as.daimler.com/DMSService/currencies IF_XP_DMS_2_4_currencies.xsd http://dms.ri.as.daimler.com/DMSService/xp_sending IF_XP_DMS_2_4_xp_sending.xsd http://ws.eskulabdec.daimler.com/ IF_XP_DMS_2_4_eskulab_decision.xsd");
        messageElement.addAttribute(messageElementCurr, "http://dms.ri.as.daimler.com/DMSService/currencies");
        messageElement.addAttribute(messageElementDms, "http://dms.ri.as.daimler.com/DMSService/dms_sending");
        messageElement.addAttribute(messageElementTypes, "http://dms.ri.as.daimler.com/DMSService/types");
        messageElement.addAttribute(messageElementXp, "http://dms.ri.as.daimler.com/DMSService/xp_sending");
        return messageElement;
    }
    
    public SOAPElement putBusinessContext(SOAPElement messageElement, String DmsType,String DmsVersion,String ServiceVersion,String Type  ) throws SOAPException{
        SOAPElement businessContextElement = AddDataElement(messageElement,"http://dms.ri.as.daimler.com/DMSService","env","BusinessContext","");
        AddDataElement(businessContextElement,"http://dms.ri.as.daimler.com/DMSService","env","DmsType",DmsType);
        AddDataElement(businessContextElement,"http://dms.ri.as.daimler.com/DMSService","env","DmsVersion",DmsVersion);
        AddDataElement(businessContextElement,"http://dms.ri.as.daimler.com/DMSService","env","ServiceVersion",ServiceVersion);
        AddDataElement(businessContextElement,"http://dms.ri.as.daimler.com/DMSService","env","Type",Type);        
        return businessContextElement;
    }
    
    public SOAPElement putUserContext(SOAPElement messageElement, String DaimlerUserId,String DmsUserId,String UserLocale  ) throws SOAPException{
        SOAPElement userContextElement = AddDataElement(messageElement,"http://dms.ri.as.daimler.com/DMSService","env","UserContext","");
        AddDataElement(userContextElement,"http://dms.ri.as.daimler.com/DMSService","env","DaimlerUserId",DaimlerUserId);
        AddDataElement(userContextElement,"http://dms.ri.as.daimler.com/DMSService","env","DmsUserId",DmsUserId);
        AddDataElement(userContextElement,"http://dms.ri.as.daimler.com/DMSService","env","UserLocale",UserLocale);
        return userContextElement;
    }
    
    public SOAPElement putProcessContext(SOAPElement messageElement, String Timestamp,String TrackingId) throws SOAPException{
        SOAPElement processContextElement = AddDataElement(messageElement,"http://dms.ri.as.daimler.com/DMSService","env","ProcessContext","");
        AddDataElement(processContextElement,"http://dms.ri.as.daimler.com/DMSService","env","Timestamp",Timestamp);
        AddDataElement(processContextElement,"http://dms.ri.as.daimler.com/DMSService","env","TrackingId",TrackingId);       
        return processContextElement;
    }
    
    public SOAPElement putServiceMessage(SOAPElement messageElement) throws SOAPException{
        SOAPElement serviceMessageElement = AddDataElement(messageElement,"http://dms.ri.as.daimler.com/DMSService","env","ServiceMessage","");
        return serviceMessageElement;
    }
    
    public SOAPElement putInitJobRequest(SOAPElement serviceMessageElement) throws SOAPException{
        SOAPElement initJobRequestElement = AddDataElement(serviceMessageElement,"http://dms.ri.as.daimler.com/DMSService","env","InitJobRequest","");
        return initJobRequestElement;
    }
    
    public SOAPElement putJob(SOAPElement initJobRequestElement,String initjob_jobId, String currency) throws SOAPException{
        SOAPElement jobElement = AddDataElement(initJobRequestElement,"http://dms.ri.as.daimler.com/DMSService/dms_sending","dms","Job","");
        QName jobElementCurrency = new QName("Currency");
        jobElement.addAttribute(jobElementCurrency, currency);
        if(!initjob_jobId.isEmpty()){
           QName jobElementJobId = new QName("JobId");
           jobElement.addAttribute(jobElementJobId, initjob_jobId); 
        }
            
        return jobElement;
    }
    
    public SOAPElement putCustomerConcern(SOAPElement jobElement,Map customerConcernMap, Map notesMap, Map defectKeyMap, Map partMap, Map workItemMap,Map servicePackageMap) throws SOAPException{
        SOAPElement customerConcernElement = AddDataElement(jobElement,"http://dms.ri.as.daimler.com/DMSService/dms_sending","dms","CustomerConcern","");
        if(!customerConcernMap.isEmpty()){
           AddAttributes(customerConcernElement, customerConcernMap); 
        }        
        SOAPElement notesElement = AddDataElement(customerConcernElement,"http://dms.ri.as.daimler.com/DMSService/types","types","Notes","");
        if(!notesMap.isEmpty()){
          AddAttributes(notesElement, notesMap);  
        }
        SOAPElement defectKeyElement = AddDataElement(customerConcernElement,"http://dms.ri.as.daimler.com/DMSService/types","types","DefectKey","");       
        if(!defectKeyMap.isEmpty()){
          AddAttributes(defectKeyElement, defectKeyMap);  
        }
        @SuppressWarnings("UnusedAssignment")
        SOAPElement partElement = AddDataElement(customerConcernElement,"http://dms.ri.as.daimler.com/DMSService/types","types","Part","");
        partElement = PartElement;
        if(!partMap.isEmpty()){
          AddAttributes(partElement, partMap);  
        }
        @SuppressWarnings("UnusedAssignment")
        SOAPElement workItemElement = AddDataElement(customerConcernElement,"http://dms.ri.as.daimler.com/DMSService/types","types","WorkItem","");
        workItemElement = WorkItemElement;
        if(!workItemMap.isEmpty()){
          AddAttributes(workItemElement, workItemMap);  
        }
        @SuppressWarnings("UnusedAssignment")
        SOAPElement servicePackageElement = AddDataElement(customerConcernElement,"http://dms.ri.as.daimler.com/DMSService/types","types","ServicePackage","");
        servicePackageElement = ServicePackageElement;
        if(!servicePackageMap.isEmpty()){
          AddAttributes(servicePackageElement, servicePackageMap);
        }
        
        return customerConcernElement;
    }
    
    public SOAPElement putCustomerConcern(SOAPElement jobElement,Map xentryInitJobcustomerConcernMap) throws SOAPException{       
       SOAPElement customerConcernElement = null;
       SOAPElement notesElement;
       SOAPElement defectKeyElement ;
       SOAPElement partElement;
       SOAPElement workItemElement ;
       SOAPElement servicePackageElement ;
       List listI = new ArrayList();
       listI.add("WorkItemPrice");           
       listI.add("PackagePrice");
       listI.add("PackagePart");
       listI.add("PackageWorkItem");
       for(int j = 0; j < xentryInitJobcustomerConcernMap.size();++j){
           MyLogging.log(Level.INFO, "COunt :"+j);
           Map xijccMap = (Map)xentryInitJobcustomerConcernMap.get(j);
           Map ccMap = (Map)xijccMap.get("XentryCustomerConcernMap");
           customerConcernElement = AddDataElement(jobElement,"http://dms.ri.as.daimler.com/DMSService/dms_sending","dms","CustomerConcern","");
           if(!ccMap.isEmpty()){
              AddAttributes(customerConcernElement, ccMap); 
           }
           notesElement = AddDataElement(customerConcernElement,"http://dms.ri.as.daimler.com/DMSService/types","types","Notes","");
           Map notesMap = (Map)xijccMap.get("PlxCustomerConcernNotesMap");
           if(!notesMap.isEmpty()){
              AddAttributes(notesElement, notesMap);  
           }
           defectKeyElement = AddDataElement(customerConcernElement,"http://dms.ri.as.daimler.com/DMSService/types","types","DefectKey",""); 
           Map defectKeyMap = (Map)xijccMap.get("PlxCustomerConcernDefectKeyMap");
           if(!defectKeyMap.isEmpty()){
               AddAttributes(defectKeyElement, defectKeyMap);  
           }
           
           Map pMap = (Map)xijccMap.get("PlxCustomerConcernPartsMap");
           for(int j2 = 0; j2 < pMap.size();++j2){
              partElement = AddDataElement(customerConcernElement,"http://dms.ri.as.daimler.com/DMSService/types","types","Part",""); 
              Map partMap = (Map)pMap.get(j2);
              AddAttributes(partElement, partMap,"PartPrice");
              if(!partMap.isEmpty()){                                        
                    SOAPElement partPriceElement = AddDataElement(partElement,"http://dms.ri.as.daimler.com/DMSService/types","types","Prices",""); 
                    Map partPriceMap = (Map)partMap.get("PartPrice");
                    if(!partPriceMap.isEmpty()){
                        AddAttributes(partPriceElement, partPriceMap);  
                    }   
              }
           }
          
           
           Map wkItmMap = (Map)xijccMap.get("PlxCustomerConcernWorkItemMap");
           for(int j2 = 0; j2 < wkItmMap.size();++j2){
              workItemElement = AddDataElement(customerConcernElement,"http://dms.ri.as.daimler.com/DMSService/types","types","WorkItem","");
              Map workItemMap = (Map)wkItmMap.get(j2);              
              
              if(!workItemMap.isEmpty()){  
                    AddAttributes(workItemElement, workItemMap,listI);
                    SOAPElement workItemPriceElement = AddDataElement(workItemElement,"http://dms.ri.as.daimler.com/DMSService/types","types","Prices",""); 
                    Map workItemPriceMap = (Map)workItemMap.get("WorkItemPrice");
                    if(!workItemPriceMap.isEmpty()){
                        AddAttributes(workItemPriceElement, workItemPriceMap);  
                    }   
              }
           }
                                            
           Map srvPkgMap = (Map)xijccMap.get("PlxCustomerConcernServicePackageMap");           
           for(int j2 = 0; j2 < srvPkgMap.size();++j2){
               servicePackageElement = AddDataElement(customerConcernElement,"http://dms.ri.as.daimler.com/DMSService/types","types","ServicePackage","");
               Map servicePackageMap = (Map)srvPkgMap.get(j2);
               if(!servicePackageMap.isEmpty()){
                  AddAttributes(servicePackageElement, servicePackageMap,listI);
                  SOAPElement servicePackagePriceElement = AddDataElement(servicePackageElement,"http://dms.ri.as.daimler.com/DMSService/types","types","Prices",""); 
                  Map servicePackagePriceMap = (Map)servicePackageMap.get("PackagePrice");
                  if(!servicePackagePriceMap.isEmpty())
                    AddAttributes(servicePackagePriceElement, servicePackagePriceMap);
                  SOAPElement servicePackagePartElement = AddDataElement(servicePackageElement,"http://dms.ri.as.daimler.com/DMSService/types","types","Part",""); 
                  Map servicePackagePartMap = (Map)servicePackageMap.get("PackagePart");
                  if(!servicePackagePartMap.isEmpty())
                    AddAttributes(servicePackagePartElement, servicePackagePartMap);
                  SOAPElement servicePackageWorkItemElement = AddDataElement(servicePackageElement,"http://dms.ri.as.daimler.com/DMSService/types","types","WorkItem",""); 
                  Map servicePackageWorkItemMap = (Map)servicePackageMap.get("PackageWorkItem");
                  if(!servicePackageWorkItemMap.isEmpty())
                    AddAttributes(servicePackageWorkItemElement, servicePackageWorkItemMap);
               }   
           }
       }
       
       return customerConcernElement;
    }
    
    public SOAPElement putServiceMeasure(SOAPElement jobElement,Map xentryInitJobServiceMeasureMap) throws SOAPException{
       List listI = new ArrayList();
       listI.add("ServiceMeasurePackageParts");           
       listI.add("ServiceMeasurePackageWorkItem");
       listI.add("PackagePart");
       listI.add("PackageWorkItem");
       SOAPElement serviceMeasureElement = null;
       for(int i=0; i < xentryInitJobServiceMeasureMap.size(); ++i){
           Map tmpMap = (Map)xentryInitJobServiceMeasureMap.get(i);
           Map serviceMeasureMap = (Map)tmpMap.get("XentryServiceMeasureMap");
           serviceMeasureElement =  AddDataElement(jobElement,"http://dms.ri.as.daimler.com/DMSService/dms_sending","dms","ServiceMeasure","");
           if(!serviceMeasureMap.isEmpty())
            AddAttributes(serviceMeasureElement, serviceMeasureMap);            
           
           Map notesMap = (Map)tmpMap.get("PlxServiceMeasureNotesMap");
           SOAPElement notesElement = AddDataElement(serviceMeasureElement,"http://dms.ri.as.daimler.com/DMSService/types","types","Notes","");
           if(!notesMap.isEmpty()){
            AddAttributes(notesElement, notesMap);  
           }
           
           Map defectKeyMap = (Map)tmpMap.get("PlxServiceMeasureDefectKeyMap");
           SOAPElement defectKeyElement =  AddDataElement(serviceMeasureElement,"http://dms.ri.as.daimler.com/DMSService/types","types","DefectKey","");
           if(!defectKeyMap.isEmpty())
               AddAttributes(defectKeyElement, defectKeyMap); 
           
           
                   
           Map partMap = (Map)tmpMap.get("PlxServiceMeasurePartsMap");
           if(!partMap.isEmpty()){
               for(int j = 0; j < partMap.size();++j){
                Map partMap_2 =  (Map)partMap.get(j);
                SOAPElement partElement = AddDataElement(serviceMeasureElement,"http://dms.ri.as.daimler.com/DMSService/types","types","Part","");
                if(!partMap_2.isEmpty())
                    AddAttributes(partElement, partMap_2); 
               }
           }
        
        Map workItemMap = (Map)tmpMap.get("PlxServiceMeasureWorkItemMap");                       
        if(!workItemMap.isEmpty()){
            for(int j = 0; j < workItemMap.size();++j){
               Map workItemMap_2 = (Map)workItemMap.get(j);
               SOAPElement workItemElement = AddDataElement(serviceMeasureElement,"http://dms.ri.as.daimler.com/DMSService/types","types","WorkItem","");                
               if(!workItemMap_2.isEmpty())
                  AddAttributes(workItemElement, workItemMap_2);  
            }
           
        }
        
        Map serviceMeasurePackageMap = (Map)tmpMap.get("PlxServiceMeasurePackageMap");
        if(!serviceMeasurePackageMap.isEmpty()){
           for(int j = 0; j < serviceMeasurePackageMap.size();++j){
              Map srvPkgMap = (Map)serviceMeasurePackageMap.get(j);
              Map serviceMeasurePackageMap_2 = (Map)srvPkgMap.get("ServiceMeasurePackage");
              SOAPElement serviceMeasurePackageElement = AddDataElement(serviceMeasureElement,"http://dms.ri.as.daimler.com/DMSService/types","types","ServiceMeasurePackage","");
              if(!serviceMeasurePackageMap_2.isEmpty()){                  
                  AddAttributes(serviceMeasurePackageElement, serviceMeasurePackageMap_2,listI);
                  SOAPElement serviceMeasurePackagePartsElement = AddDataElement(serviceMeasurePackageElement,"http://dms.ri.as.daimler.com/DMSService/types","types","Part","");
                  Map serviceMeasurePackagePartMap_2 = (Map)srvPkgMap.get("ServiceMeasurePackageParts");
                  if(!serviceMeasurePackagePartMap_2.isEmpty()){
                     for(int k = 0; k < serviceMeasurePackagePartMap_2.size();++k ){
                         Map serviceMeasurePackagePartMap_3 = (Map)serviceMeasurePackagePartMap_2.get(k);
                         if(!serviceMeasurePackagePartMap_3.isEmpty())
                            AddAttributes(serviceMeasurePackagePartsElement, serviceMeasurePackagePartMap_3);
                     }
                     
                  }
                  
                  SOAPElement serviceMeasurePackageWorkItemElement = AddDataElement(serviceMeasurePackageElement,"http://dms.ri.as.daimler.com/DMSService/types","types","WorkItem","");
                  Map serviceMeasurePackageWorkItemMap = (Map)srvPkgMap.get("ServiceMeasurePackageWorkItem");
                  if(!serviceMeasurePackageWorkItemMap.isEmpty()){
                     for(int k = 0; k < serviceMeasurePackageWorkItemMap.size();++k ){
                        Map serviceMeasurePackageWorkItemMap_2 = (Map)serviceMeasurePackageWorkItemMap.get(k);
                        if(!serviceMeasurePackageWorkItemMap_2.isEmpty()) 
                            AddAttributes(serviceMeasurePackageWorkItemElement, serviceMeasurePackageWorkItemMap_2); 
                     }
                      
                  }
              }
              
           } 
        }
      }
       
       
       return serviceMeasureElement;
    }
    
    public SOAPElement putServiceMeasure(SOAPElement jobElement,Map serviceMeasureMap, Map notesMap, Map defectKeyMap, Map partMap, Map workItemMap,Map serviceMeasurePackageMap) throws SOAPException{
        SOAPElement serviceMeasureElement =  AddDataElement(jobElement,"http://dms.ri.as.daimler.com/DMSService/dms_sending","dms","ServiceMeasure","");
        AddAttributes(serviceMeasureElement, serviceMeasureMap);            
        SOAPElement defectKeyElement =  AddDataElement(serviceMeasureElement,"http://dms.ri.as.daimler.com/DMSService/types","types","DefectKey","");
        AddAttributes(defectKeyElement, defectKeyMap);
        
        SOAPElement notesElement = AddDataElement(serviceMeasureElement,"http://dms.ri.as.daimler.com/DMSService/types","types","Notes","");
        if(!notesMap.isEmpty()){
          AddAttributes(notesElement, notesMap);  
        }        
        @SuppressWarnings("UnusedAssignment")
        SOAPElement partElement = AddDataElement(serviceMeasureElement,"http://dms.ri.as.daimler.com/DMSService/types","types","Part","");        
        if(!partMap.isEmpty()){
          AddAttributes(partElement, partMap);  
        }
        
        @SuppressWarnings("UnusedAssignment")
        SOAPElement workItemElement = AddDataElement(serviceMeasureElement,"http://dms.ri.as.daimler.com/DMSService/types","types","WorkItem","");        
        if(!workItemMap.isEmpty()){
          AddAttributes(workItemElement, workItemMap);  
        }
        
        @SuppressWarnings("UnusedAssignment")
        SOAPElement serviceMeasurePackageElement = AddDataElement(serviceMeasureElement,"http://dms.ri.as.daimler.com/DMSService/types","types","ServiceMeasurePackage","");
        serviceMeasurePackageElement = ServiceMeasurePackageElement;
        if(!serviceMeasurePackageMap.isEmpty()){
          AddAttributes(serviceMeasurePackageElement, serviceMeasurePackageMap);
        }
        return serviceMeasureElement;
    }
    
    public SOAPElement putOrder(SOAPElement jobElement,Map orderMap,Map serviceAdvisorMap ) throws SOAPException{
        SOAPElement orderElement =  AddDataElement(jobElement,"http://dms.ri.as.daimler.com/DMSService/dms_sending","dms","Order","");
        List listI = new ArrayList();
        listI.add("OrderServiceAdvisor");    
        if(!serviceAdvisorMap.isEmpty()){
            AddAttributes(orderElement, orderMap,listI);
        }
        SOAPElement serviceAdvisorElement = AddDataElement(orderElement,"http://dms.ri.as.daimler.com/DMSService/types","types","ServiceAdvisor","");        
        if(!serviceAdvisorMap.isEmpty()){
          AddAttributes(serviceAdvisorElement, serviceAdvisorMap);  
        }
        
        return orderElement;
    }
    
    public SOAPElement putVehicle(SOAPElement jobElement,Map orderMap,Map vehicleMap ) throws SOAPException{
       SOAPElement vehicleElement =  AddDataElement(jobElement,"http://dms.ri.as.daimler.com/DMSService/dms_sending","dms","Vehicle","");
       if(!vehicleMap.isEmpty()){
         AddAttributes(vehicleElement, vehicleMap);
       } 
       return vehicleElement;
    }
    
     public SOAPElement putVehicle(SOAPElement jobElement,Map vehicleMap ) throws SOAPException{
       SOAPElement vehicleElement =  AddDataElement(jobElement,"http://dms.ri.as.daimler.com/DMSService/dms_sending","dms","Vehicle","");
       if(!vehicleMap.isEmpty()){
         AddAttributes(vehicleElement, vehicleMap);
       } 
       return vehicleElement;
    }
    
    /**
     *
     * @param jobElement
     * @param orderMap
     * @param customerMap
     * @return
     * @throws SOAPException
     */
    public SOAPElement putCustomer(SOAPElement jobElement,Map orderMap,Map customerMap ) throws SOAPException{
       SOAPElement customerElement =  AddDataElement(jobElement,"http://dms.ri.as.daimler.com/DMSService/dms_sending","dms","Customer","");
       if(!customerMap.isEmpty()){
         AddAttributes(customerElement, customerMap);
       } 
       return customerElement;
    }
    
    /**
     *
     * @param jobElement
     * @param customerMap
     * @return
     * @throws SOAPException
     */
    public SOAPElement putCustomer(SOAPElement jobElement,Map customerMap ) throws SOAPException{
       SOAPElement customerElement =  AddDataElement(jobElement,"http://dms.ri.as.daimler.com/DMSService/dms_sending","dms","Customer","");
       if(!customerMap.isEmpty()){
         AddAttributes(customerElement, customerMap);
       } 
       return customerElement;
    }
    
    public SOAPElement putPriceChildElement(SOAPElement msglement,Map priceMap) throws SOAPException{
         SOAPElement pricesElement = AddDataElement(msglement,"http://dms.ri.as.daimler.com/DMSService/types","types","Prices","");
         if(!priceMap.isEmpty()){
          AddAttributes(pricesElement, priceMap);
        }
        return pricesElement; 
    }    
    
    public SOAPElement putPartChildElement(SOAPElement msglement,Map partMap) throws SOAPException{
         SOAPElement partElement = AddDataElement(msglement,"http://dms.ri.as.daimler.com/DMSService/types","types","Part","");
         if(!partMap.isEmpty()){
          AddAttributes(partElement, partMap);
        }
        return partElement; 
    } 
    
    public SOAPElement putWorkItemChildElement(SOAPElement msglement,Map workItemMap) throws SOAPException{
         SOAPElement workItemElement = AddDataElement(msglement,"http://dms.ri.as.daimler.com/DMSService/types","types","Part","");
         if(!workItemMap.isEmpty()){
          AddAttributes(workItemElement, workItemMap);
        }
        return workItemElement; 
    }
    
    
    public static void main(String[] args) throws SOAPException, IOException {
        String xmldoc = new TestString().xc2;
        String xmldoc2 = new TestString().xd;
        MyLogging.log(Level.INFO, xmldoc);
        Map XentryInitJobCustomerConcernMap;
        Map XentryInitJobserviceMeasureMap;
        XentryCustomerConcern xcc = new XentryCustomerConcern(xmldoc);
        XentryServiceMeasure xsmm = new XentryServiceMeasure(xmldoc2);
        XentryInitJobCustomerConcernMap = xcc.getXentryInitJobCustomerConcernMap();
        XentryInitJobserviceMeasureMap = xsmm.getXentryInitJobServiceMeasureMap();
        InitJob initJob = new InitJob();
                SOAPMessage soap_message = initJob.getSOAPMessage();
                initJob.putSOAPHeader(soap_message, "xxxxx", "xxxxxx");
                SOAPBodyElement sendSyncDataRequestElement = initJob.putSoapBody(soap_message, "SERVICE_JOB_7", "UPDATE", "InitJob");
                SOAPElement dataSynchElement = initJob.putDataSynch(sendSyncDataRequestElement);
                SOAPElement structuredDataElement = initJob.putStructuredData(dataSynchElement);
                SOAPElement messageElement = initJob.putMessage(structuredDataElement);
                SOAPElement businessContextElement = initJob.putBusinessContext(messageElement, "WestStar DMS", "V1", "2.4", "REQUEST");
                SOAPElement userContextElement = initJob.putUserContext(messageElement, "P0012CO", "mustermann", "de_DE");
                SOAPElement processContextElement = initJob.putProcessContext(messageElement, "xxxx", "xxxx");
                SOAPElement serviceMessageElement = initJob.putServiceMessage(messageElement);
                SOAPElement initJobRequestElement = initJob.putInitJobRequest(serviceMessageElement);
                SOAPElement jobElement = initJob.putJob(initJobRequestElement, "EUR","");
                SOAPElement customerConcernElement = initJob.putCustomerConcern(jobElement,XentryInitJobCustomerConcernMap);
                SOAPElement serviceMeasureElement = initJob.putServiceMeasure(jobElement, XentryInitJobserviceMeasureMap);
        soap_message.writeTo(System.out);
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
        
        MessageFactory factory = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
        soap_message = factory.createMessage();
        //SOAPPart soapPart = soap_message.getSOAPPart();
        
        
        SOAPHeader header = soap_message.getSOAPHeader();
        QName headerName = new QName("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd","Security", "wsse");
        QName userNameToken =  new QName("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd","UsernameToken","wsse");
        QName userName = new QName("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd","Username","wsse");
        QName pssWd = new QName("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd","Password","wsse");
        
        SOAPHeaderElement headerElement = header.addHeaderElement(headerName);
        SOAPElement userNameTokenElement = headerElement.addChildElement(userNameToken);
        
        SOAPElement userNameElement = userNameTokenElement.addChildElement(userName);
        SOAPElement pssWdElement = userNameTokenElement.addChildElement(pssWd);
        userNameElement.addTextNode("TD02136");
        QName psswdType = new QName("Type");
        pssWdElement.addAttribute(psswdType,"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText");
        pssWdElement.addTextNode("9Lh8hdyDtY");
        
        SOAPBody body = soap_message.getSOAPBody();
        QName sendSyncDataRequest =  new QName("http://stc.daimler.com/2009/08/DS/ext", "sendSyncDataRequest", "ext");                   
        SOAPBodyElement sendSyncDataRequestElement = body.addBodyElement(sendSyncDataRequest);
        QName sendSyncDataRequestVersion = new QName("ext:version");
        QName sendSyncDataRequestSchemaLocation = new QName("xsi:schemaLocation");
        QName sendSyncDataRequestDs = new QName("xmlns:ds");
        QName sendSyncDataRequestXsi = new QName("xmlns:xsi");
        sendSyncDataRequestElement.addAttribute(sendSyncDataRequestVersion, "1.4");
        sendSyncDataRequestElement.addAttribute(sendSyncDataRequestSchemaLocation, "http://stc.daimler.com/2009/08/DS/ext file:///C:/Xentry_Portal/Schnittstellen_Spezifikationen/XML-Schema_V_1.4.5/XML-Instanzen-V1.4.5/03_DataServer_Instanzen/XML-Artefacts/XML-Artefacts/StarConnectDataServerExt.xsd");
        sendSyncDataRequestElement.addAttribute(sendSyncDataRequestDs, "http://stc.daimler.com/2009/08/DS");
        sendSyncDataRequestElement.addAttribute(sendSyncDataRequestXsi, "http://www.w3.org/2001/XMLSchema-instance");*/
        
        
       
        
        
       // try{
            /*AddDataElement(sendSyncDataRequestElement,"http://stc.daimler.com/2009/08/DS/ext","ext","type","SERVICE_JOB_7");
            AddDataElement(sendSyncDataRequestElement,"http://stc.daimler.com/2009/08/DS/ext","ext","event","UPDATE");
            AddDataElement(sendSyncDataRequestElement,"http://stc.daimler.com/2009/08/DS/ext","ext","operation","InitJob");
            SOAPElement dataSynchElement = AddDataElement(sendSyncDataRequestElement,"http://stc.daimler.com/2009/08/DS/ext","ext","data","");
            SOAPElement structuredDataElement = AddDataElement(dataSynchElement,"http://stc.daimler.com/2009/08/DS","ds","structuredData","");
            SOAPElement messageElement = AddDataElement(structuredDataElement,"http://dms.ri.as.daimler.com/DMSService","env","Message","");
            
            
            QName messageElementSchemaLocation = new QName("xsi:schemaLocation");
            QName messageElementDms = new QName("xmlns:dms");
            QName messageElementTypes = new QName("xmlns:types"); 
            QName messageElementCurr = new QName("xmlns:cur");
            QName messageElementXp = new QName("xmlns:xp");
            
            messageElement.addAttribute(messageElementSchemaLocation, "http://dms.ri.as.daimler.com/DMSService IF_XP_DMS_2_4_message_envelope.xsd http://dms.ri.as.daimler.com/DMSService/dms_sending IF_XP_DMS_2_4_dms_sending.xsd http://dms.ri.as.daimler.com/DMSService/types IF_XP_DMS_2_4_types.xsd http://dms.ri.as.daimler.com/DMSService/currencies IF_XP_DMS_2_4_currencies.xsd http://dms.ri.as.daimler.com/DMSService/xp_sending IF_XP_DMS_2_4_xp_sending.xsd http://ws.eskulabdec.daimler.com/ IF_XP_DMS_2_4_eskulab_decision.xsd");
            messageElement.addAttribute(messageElementCurr, "http://dms.ri.as.daimler.com/DMSService/currencies");
            messageElement.addAttribute(messageElementDms, "http://dms.ri.as.daimler.com/DMSService/dms_sending");
            messageElement.addAttribute(messageElementTypes, "http://dms.ri.as.daimler.com/DMSService/types");
            messageElement.addAttribute(messageElementXp, "http://dms.ri.as.daimler.com/DMSService/xp_sending");
            
            SOAPElement businessContextElement = AddDataElement(messageElement,"http://dms.ri.as.daimler.com/DMSService","env","BusinessContext","");
            AddDataElement(businessContextElement,"http://dms.ri.as.daimler.com/DMSService","env","DmsType","SampleDMS SOAPUI");
            AddDataElement(businessContextElement,"http://dms.ri.as.daimler.com/DMSService","env","DmsVersion","Sample DMS Version 10.5a");
            AddDataElement(businessContextElement,"http://dms.ri.as.daimler.com/DMSService","env","ServiceVersion","2.4");
            AddDataElement(businessContextElement,"http://dms.ri.as.daimler.com/DMSService","env","Type","REQUEST");
            
            SOAPElement userContextElement = AddDataElement(messageElement,"http://dms.ri.as.daimler.com/DMSService","env","UserContext","");
            AddDataElement(userContextElement,"http://dms.ri.as.daimler.com/DMSService","env","DaimlerUserId","testerde");
            AddDataElement(userContextElement,"http://dms.ri.as.daimler.com/DMSService","env","DmsUserId","mustermann");
            AddDataElement(userContextElement,"http://dms.ri.as.daimler.com/DMSService","env","UserLocale","de_DE");
            
            SOAPElement processContextElement = AddDataElement(messageElement,"http://dms.ri.as.daimler.com/DMSService","env","ProcessContext","");
            AddDataElement(processContextElement,"http://dms.ri.as.daimler.com/DMSService","env","Timestamp","2001-12-31T12:00:00");
            AddDataElement(processContextElement,"http://dms.ri.as.daimler.com/DMSService","env","TrackingId","TJ24_1.1");
            
            SOAPElement serviceMessageElement = AddDataElement(messageElement,"http://dms.ri.as.daimler.com/DMSService","env","ServiceMessage","");
            SOAPElement initJobRequestElement = AddDataElement(serviceMessageElement,"http://dms.ri.as.daimler.com/DMSService","env","InitJobRequest","");
            SOAPElement jobElement = AddDataElement(initJobRequestElement,"http://dms.ri.as.daimler.com/DMSService/dms_sending","dms","Job","");
            QName jobElementCurrency = new QName("Currency");
            jobElement.addAttribute(jobElementCurrency, "EUR");
            
            /*SOAPElement customerConcernElement = AddDataElement(jobElement,"http://dms.ri.as.daimler.com/DMSService/dms_sending","dms","CustomerConcern","");
            AddAttributes(customerConcernElement, customerConcernMap);
            SOAPElement notesElement = AddDataElement(customerConcernElement,"http://dms.ri.as.daimler.com/DMSService/types","types","Notes","");
            AddAttributes(notesElement, notesMap);
            
            SOAPElement serviceMeasureElement =  AddDataElement(jobElement,"http://dms.ri.as.daimler.com/DMSService/dms_sending","dms","ServiceMeasure","");
            AddAttributes(serviceMeasureElement, serviceMeasureMap);            
            SOAPElement defectKeyElement =  AddDataElement(serviceMeasureElement,"http://dms.ri.as.daimler.com/DMSService/types","types","DefectKey","");
            AddAttributes(defectKeyElement, defectKeyMap);
            
            SOAPElement orderElement =  AddDataElement(jobElement,"http://dms.ri.as.daimler.com/DMSService/dms_sending","dms","Order","");
            AddAttributes(orderElement, orderMap);
            
            SOAPElement vehicleElement =  AddDataElement(jobElement,"http://dms.ri.as.daimler.com/DMSService/dms_sending","dms","Vehicle","");
            
            SOAPElement customerElement =  AddDataElement(jobElement,"http://dms.ri.as.daimler.com/DMSService/dms_sending","dms","Customer","");
            
            
            
            soap_message.writeTo(System.out);
            
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection connection = soapConnectionFactory.createConnection();
            URL endpoint = new URL("https://srs-ds-int1.i.daimler.com/STARCDS/services/ExternalInterface");
            SOAPMessage response = connection.call(soap_message, endpoint);
            connection.close();
            System.out.println();
            System.out.println("RESPONSE::");
            response.writeTo(System.out);
            SOAPBody soapBody = response.getSOAPBody();
            
            
            System.out.println();
            Document respDocument = soapBody.extractContentAsDocument();
            
            parseThroughDocument(respDocument.getDocumentElement());    
            
            Iterator iter = soapBody.getChildElements();
            
            while (iter.hasNext()) {
                SOAPBodyElement bodyElement = (SOAPBodyElement)iter.next();
                QName elName =  bodyElement.getElementQName();
                String nName = bodyElement.getNodeName();
                //Map.Entry mEntry = (Map.Entry) iter.next();
                //System.out.println("value: "+String.valueOf(mEntry.getValue()));
                System.out.println("value element: "+elName.toString());
                System.out.println("value node: "+nName); 
                if(nName.equalsIgnoreCase("soap:fault")){
                    Iterator iter2 = bodyElement.getChildElements();
                    while (iter.hasNext()) {
                        SOAPBodyElement bodyElement2 = (SOAPBodyElement)iter.next();
                    }
                }
            }
            
            Iterator iter2 = soapBody.getChildElements();
            SOAPBodyElement bodyElement = (SOAPBodyElement)iter2.next();
            String lastPrice = bodyElement.getValue();
            System.out.print("The last price for SUNW is ");*/
            
            
       // }catch(SOAPException ex){
       //     ex.printStackTrace(new PrintWriter(ERRORS));                                                            
         //   System.out.println("RefreshPrices ERROR::FileNotFoundException....."+ ERRORS.toString());
        //}
    }
    
}
