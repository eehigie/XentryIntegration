/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.plexada.xentry;

import java.io.IOException;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 *
 * @author SAP Training
 */
public class GetDataByID {

    private final SOAPMessage soap_message;

    public GetDataByID() throws SOAPException {
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
    
    public SOAPBodyElement putSoapBody(SOAPMessage soap_message,String dataID) throws SOAPException{
        SOAPBody body = soap_message.getSOAPBody();
        QName getDataByIDRequest =  new QName("http://stc.daimler.com/2009/08/DS/ext", "getDataByIDRequest", "ext");                   
        SOAPBodyElement getDataByIDRequestElement = body.addBodyElement(getDataByIDRequest);
        QName getDataByIDRequestVersion = new QName("ext:version");        
        QName getDataByIDRequestDs = new QName("xmlns:ds");
        QName getDataByIDRequestXsi = new QName("xmlns:xsi");
        getDataByIDRequestElement.addAttribute(getDataByIDRequestVersion, "1.4");        
        getDataByIDRequestElement.addAttribute(getDataByIDRequestDs, "http://stc.daimler.com/2009/08/DS");
        getDataByIDRequestElement.addAttribute(getDataByIDRequestXsi, "http://www.w3.org/2001/XMLSchema-instance");
        SOAPElement AddDataElement = AddDataElement(getDataByIDRequestElement,"http://stc.daimler.com/2009/08/DS/ext","ext","dataID",dataID);
        return getDataByIDRequestElement;
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
    
    public SOAPMessage getSOAPMessage (){
        return soap_message;
    } 
    
    public static void main(String[] args) throws IOException {
        try {
            GetDataByID getdatabyId = new GetDataByID();
            SOAPMessage soap_message = getdatabyId.getSOAPMessage();
            getdatabyId.putSOAPHeader(soap_message, "TD02136", "9Lh8hdyDtY");
            SOAPBodyElement ssELe = getdatabyId.putSoapBody(soap_message, "101224509");
            MyLogging.log(Level.INFO, "OUTPUT REQUEST XML ......................"); 
            final StringWriter sw = new StringWriter();
            TransformerFactory.newInstance().newTransformer().transform(new DOMSource(soap_message.getSOAPPart()),new StreamResult(sw));
            soap_message.writeTo(System.out);
            MyLogging.log(Level.INFO, sw.toString()); 
        } catch (SOAPException ex) {
            Logger.getLogger(GetDataByID.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(GetDataByID.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
