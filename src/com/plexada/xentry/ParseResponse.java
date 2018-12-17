/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.plexada.xentry;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import javax.xml.bind.DatatypeConverter;
import javax.xml.namespace.QName;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

/**
 *
 * @author efosa
 */
public class ParseResponse {

    private static final StringWriter ERRORS = new StringWriter();
   
    private SOAPMessage soap_response = null;
    private SOAPPart response_soapPart = null;
    private SOAPEnvelope response_envelope = null;
    private static SOAPBody response_soapBody = null;
    private static String raw_xml;
    private static Document raw_xml_doc;
    private static String compressedData;
    private String responseCode ;
    private String BrowserUrl;
    private String JobId;

    public ParseResponse(String str) throws ParserConfigurationException {
        handleResponse(str);
    }
            
    
    public ParseResponse(SOAPMessage soap_response ) throws SOAPException, IOException, ParserConfigurationException, UnsupportedEncodingException, SAXException {
        //String format1 = "ISO-8859-1";
        this.soap_response = soap_response;
        this.response_soapPart = this.soap_response.getSOAPPart();
        this.response_envelope = this.response_soapPart.getEnvelope();
        this.response_soapBody = this.response_envelope.getBody();
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        soap_response.writeTo(out);
        raw_xml = new String(out.toByteArray());
        handleXMLDoc();             
        getResponseNodes();
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
    
    public void handleResponse(String str) throws ParserConfigurationException{
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = convertStringToDocument(str) ;
        doc.getDocumentElement().normalize();

	System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
			
	NodeList nList = doc.getElementsByTagName("InitJobResponse");
			
	System.out.println("----------------------------");
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = (Node)nList.item(temp);
            System.out.println("\nCurrent Element :" + nNode.getNodeName());				
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
              Element eElement = (Element) nNode;
              MyLogging.log(Level.INFO,"BrowserUrl : " + eElement.getAttribute("BrowserUrl"));
              MyLogging.log(Level.INFO,"JobId : " + eElement.getAttribute("JobId"));  
              this.BrowserUrl = eElement.getAttribute("BrowserUrl");
              this.JobId = eElement.getAttribute("JobId");
            }
        }

    }
    
    private static Document convertStringToDocument(String xmlStr) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
        DocumentBuilder builder;  
        try  
        {  
            builder = factory.newDocumentBuilder();  
            Document doc = builder.parse( new InputSource( new StringReader( xmlStr ) ) ); 
            return doc;
        } catch (Exception e) {  
            e.printStackTrace();  
        } 
        return null;
    }
    
    public void getMsgNodes(){
      QName name = new QName("sendSyncDataResponse");
      java.util.Iterator iterator = response_soapBody.getChildElements(name);
      while (iterator.hasNext()) {
        SOAPBodyElement bodyElement = (SOAPBodyElement) iterator.next();
        String val = bodyElement.getValue();
        MyLogging.log(Level.INFO,"The Value is:" + val);
      }
    }
    
       
    public List getResponseNodes(){
       List<String> nodeList = new ArrayList<String>();
       Iterator<Node> itr= response_soapBody.getChildElements();
       Element ele;
       while (itr.hasNext()) {
        Node node=(Node)itr.next();
        if (node.getNodeType()==Node.ELEMENT_NODE) {
            MyLogging.log(Level.INFO,"reading Node.ELEMENT_NODE");
            ele=(Element)node;
            MyLogging.log(Level.INFO,"Body childs : "+ele.getLocalName());
            nodeList.add(ele.getLocalName());
            NodeList statusNodeList = ele.getChildNodes();
             for(int i=0;i<statusNodeList.getLength();i++){                 
                Node node2=(Node)statusNodeList.item(i);
                if (node2.getNodeType()==Node.ELEMENT_NODE) {
                 Element elChld = (Element) statusNodeList.item(i);                 
                 MyLogging.log(Level.INFO,"child : "+elChld.getLocalName()+" Value:"+elChld.getTextContent());
                 if(elChld.getLocalName().equalsIgnoreCase("data")){
                    this.compressedData =  elChld.getTextContent();
                    this.compressedData = compressedData.replaceAll("\\s","");
                    this.compressedData = compressedData.replaceAll("\\n", "");
                    this.compressedData = compressedData.replaceAll("\\r", "");
                    MyLogging.log(Level.INFO,"compressedData : "+compressedData);
                 }else if(elChld.getLocalName().equalsIgnoreCase("responseCode")){
                    this.responseCode = elChld.getTextContent();
                 }   
               }
            }    
        }
        
            
       }        
       return nodeList;
    }
    
    
    public void getResponseNodes(String msg_doc) throws ParserConfigurationException, SAXException, IOException{
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();                      
        Document the_raw_xml_doc = builder.parse(msg_doc);
        the_raw_xml_doc.getDocumentElement().normalize();

	MyLogging.log(Level.INFO,"Root element :" + the_raw_xml_doc.getDocumentElement().getNodeName());
			
	//NodeList nList = the_raw_xml_doc.getElementsByTagName("data");   
        //System.out.println("----------------------------");
        //for (int temp = 0; temp < nList.getLength(); temp++) {
        //    Node nNode = (Node)nList.item(temp);				
        //    MyLogging.log(Level.INFO,"\nCurrent Element :" + nNode.getNodeName());
        //}
        
    }
    
    private void handleXMLDoc() throws ParserConfigurationException, UnsupportedEncodingException, SAXException, IOException{
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        
        StringBuilder xmlStringBuilder = new StringBuilder();
        xmlStringBuilder.append(raw_xml);
        ByteArrayInputStream input = new ByteArrayInputStream(xmlStringBuilder.toString().getBytes("UTF-8"));        
        raw_xml_doc = builder.parse(input);
    }
    
    public boolean isResponseSuccess(){
        NodeList nList = raw_xml_doc.getElementsByTagName("compressedData");       
        MyLogging.log(Level.INFO,"----------------------------");
        for (int temp = 0; temp < nList.getLength(); temp++) {
           org.w3c.dom.Node nNode = nList.item(temp);
            MyLogging.log(Level.INFO,"\nCurrent Element :" + nNode.getNodeName());
            MyLogging.log(Level.INFO,"----------------------------");
            if (nNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode; 
                NodeList nList2 = eElement.getElementsByTagName("responseCode");
                if(nList2.getLength()>0){
                    MyLogging.log(Level.INFO,"Not empty");
                }else{
                    MyLogging.log(Level.INFO,"Empty");
                }
            }
        }
        return true;
    }
    
    
    public static String printSoapMessage(final SOAPMessage soapMessage) throws TransformerFactoryConfigurationError,
            TransformerConfigurationException, SOAPException, TransformerException
    {
        final TransformerFactory transformerFactory = TransformerFactory.newInstance();
        final Transformer transformer = transformerFactory.newTransformer();

        // Format it
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

        final Source soapContent = soapMessage.getSOAPPart().getContent();

        final ByteArrayOutputStream streamOut = new ByteArrayOutputStream();
        final StreamResult result = new StreamResult(streamOut);
        transformer.transform(soapContent, result);

        return streamOut.toString();
    }

    public String getCompressedData() {
        return compressedData;
    }
    
    
    public String decompress(String str) throws Exception {
        if (str == null || str.length() == 0) {
            return str;
        }
        
        //byte[] decodedBytes = Base64.decodeBase64(str);
        byte[] decodedBytes = DatatypeConverter.parseBase64Binary(str);
        ByteArrayInputStream bis = new ByteArrayInputStream(decodedBytes);
        GZIPInputStream gzipStream = new GZIPInputStream(bis);    
        Reader decoder = new InputStreamReader(gzipStream, "UTF-8"); 
        BufferedReader buffered = new BufferedReader(decoder);
        //String content;
        //while ((content = buffered.readLine()) != null)
        //    System.out.println(content);
        StringBuilder sb = new StringBuilder();
        String line;
	while((line = buffered.readLine()) != null) {
            sb.append(line);
	}
	buffered.close();
	gzipStream.close();
	bis.close();
	return sb.toString();
        /*GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(str.getBytes("ISO-8859-1")));
        BufferedReader bf = new BufferedReader(new InputStreamReader(gis, "ISO-8859-1"));
        String outStr = "";
        String line;
        while ((line=bf.readLine())!=null) {
          outStr += line;
        }*/
//        MyLogging.log(Level.INFO,"Output String length : " + content.length());
        //return content;
    }

    public String getBrowserUrl() {
        return BrowserUrl;
    }

    public String getJobId() {
        return JobId;
    }

    public String getResponseCode() {
        return responseCode;
    }
    
    
    
    
    public static void main(String[] args){  
        
        try {
            InputStream is = new ByteArrayInputStream(TestString.initSuccessResp.getBytes());
            //SOAPMessage response = MessageFactory.newInstance().createMessage(null, is);
            SOAPMessage sm = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL).createMessage(new MimeHeaders(), is);
            ParseResponse pr = new ParseResponse(sm);
            //pr.decompress(compressedData);
            //pr.getResponseNodes(TestString.initSuccessResp) ;
            //MyLogging.log(Level.INFO,"msg::"+pr.printSoapMessage(sm));
            //pr.getResponseNodes();
            //MyLogging.log(Level.INFO,"compresses Data : " + compressedData);
            //pr.decompress(compressedData);
            
            //MyLogging.log(Level.INFO,"data :: "+compressedData);
            //pr.getMsgNodes();
            pr.isResponseSuccess();
            //pr.getResponseNodes();
            //List ls = pr.getResponseNodes();
            //MyLogging.log(Level.INFO,"D::"+raw_xml);
        } catch (SOAPException ex) {
            ex.printStackTrace(new PrintWriter(ERRORS));                                                            
            MyLogging.log(Level.SEVERE, "InitJob....."+ ERRORS.toString());
        } catch (IOException ex) {
            ex.printStackTrace(new PrintWriter(ERRORS));                                                            
            MyLogging.log(Level.SEVERE, "InitJob....."+ ERRORS.toString());
        } catch (ParserConfigurationException ex) {
            ex.printStackTrace(new PrintWriter(ERRORS));                                                            
            MyLogging.log(Level.SEVERE, "InitJob....."+ ERRORS.toString());
        } catch (Exception ex) {
            ex.printStackTrace(new PrintWriter(ERRORS));                                                            
            MyLogging.log(Level.SEVERE, "InitJob....."+ ERRORS.toString());
        }
        
    }
    
}
