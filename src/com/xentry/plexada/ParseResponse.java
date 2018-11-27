/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xentry.plexada;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.soap.Node;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author efosa
 */
public class ParseResponse {
   
    private SOAPMessage soap_response = null;
    private SOAPPart response_soapPart = null;
    private SOAPEnvelope response_envelope = null;
    private SOAPBody response_soapBody = null;
    
    
    public ParseResponse(SOAPMessage soap_response ) throws SOAPException {
        this.soap_response = soap_response;
        this.response_soapPart = this.soap_response.getSOAPPart();
        this.response_envelope = this.response_soapPart.getEnvelope();
        this.response_soapBody = this.response_envelope.getBody();
    }
    
    public SOAPPart getResponseSoapPart(){
        
        return this.response_soapPart;
    }
    
    public SOAPEnvelope getResponseSoapEnvelope(){
         
         return this.response_envelope;
    }
    
    public SOAPBody getResponseSoapBody(){
        
        return this.response_soapBody;
    }
    
    public List getResponseNodes(){
       List<String> nodeList = new ArrayList<String>();
       Iterator<Node> itr= response_soapBody.getChildElements();
       Element ele;
       while (itr.hasNext()) {
        Node node=(Node)itr.next();
        if (node.getNodeType()==Node.ELEMENT_NODE) {
            System.out.println("reading Node.ELEMENT_NODE");
            ele=(Element)node;
            System.out.println("Body childs : "+ele.getLocalName());
            nodeList.add(ele.getLocalName());
            NodeList statusNodeList = ele.getChildNodes();
             for(int i=0;i<statusNodeList.getLength();i++){
               Element elChld = (Element) statusNodeList.item(i);
               System.out.println("childs : "+elChld.getLocalName());
               
                }    
        }
        
            
       } 
       
       return nodeList;
    }
    
}
