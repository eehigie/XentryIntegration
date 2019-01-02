/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.plexada.xentry;

//import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;

/**
 *
 * @author SAP Training
 */
public class GetPricesTechnicalJob {
    //private static final StringWriter ERRORS = new StringWriter();
    private final SOAPMessage soap_message;

    
     
    public GetPricesTechnicalJob() throws SOAPException {
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
    
    
    public SOAPMessage getSOAPMessage (){
        return soap_message;
    } 
}
