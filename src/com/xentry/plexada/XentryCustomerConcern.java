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
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author efosa
 */
public class XentryCustomerConcern {
    
    private static String customer_concern_xml;
    private static Document customer_concern_xml_doc = null;    
    private final Map XentryInitJobCustomerConcernMap;
    private final Map XentryCustomerConcernMap;
    private final Map PlxCustomerConcernNotesMap;
    private final Map PlxCustomerConcernDefectKeyMap;
    private final Map PlxCustomerConcernPartsMap;
    private final Map PlxCustomerConcernServicePackageMap;
    private final Map PlxCustomerConcernWorkItemMap;

    private static final StringWriter ERRORS = new StringWriter();

    public XentryCustomerConcern(String xmlDoc) {
        XentryCustomerConcern.customer_concern_xml = xmlDoc;
        XentryInitJobCustomerConcernMap = new HashMap();
        XentryCustomerConcernMap = new HashMap();
        PlxCustomerConcernNotesMap = new HashMap();
        PlxCustomerConcernDefectKeyMap = new HashMap();
        PlxCustomerConcernPartsMap = new HashMap();        
        PlxCustomerConcernServicePackageMap = new HashMap();
        PlxCustomerConcernWorkItemMap = new HashMap();

        try {
           this.handleXMLDoc();   
           this.parseXentryInitJobCustomerConcern();
           this.parseCustomerConcern();
           this.parseCustomerConcernNotesAndDefectKey();
           this.parsePlxCustomerConcernParts();
           this.parsePlxCustomerConcernServicePackage();
           this.parsePlxCustomerConcernWorkItem();
       } catch (ParserConfigurationException ex) {
           ex.printStackTrace(new PrintWriter(ERRORS));
           MyLogging.log(Level.SEVERE, "ERROR :"+ERRORS.toString());           
       } catch (SAXException ex) {
           ex.printStackTrace(new PrintWriter(ERRORS));
           MyLogging.log(Level.SEVERE, "ERROR :"+ERRORS.toString());
       } catch (IOException ex) {
           ex.printStackTrace(new PrintWriter(ERRORS));
           MyLogging.log(Level.SEVERE, "ERROR :"+ERRORS.toString());
       }
    }
    
    private void handleXMLDoc() throws ParserConfigurationException, UnsupportedEncodingException, SAXException, IOException{
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        
        StringBuilder xmlStringBuilder = new StringBuilder();
        xmlStringBuilder.append(customer_concern_xml);
        ByteArrayInputStream input = new ByteArrayInputStream(xmlStringBuilder.toString().getBytes("UTF-8"));        
        customer_concern_xml_doc = builder.parse(input);
    }

    private Map parseXentryInitJobCustomerConcern()  throws ParserConfigurationException, SAXException, IOException{
        NodeList nList = customer_concern_xml_doc.getElementsByTagName("PlxXentryCustomerConcern");
        
        MyLogging.log(Level.INFO,"----------------------------");
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Map tmpMap = new HashMap();
            Map tmpMap_2 = new HashMap();
            Map tmpMap_3 = new HashMap();
            Map outputMap = new HashMap();
            
            Node nNode = nList.item(temp);
            MyLogging.log(Level.INFO,"\nCurrent Element :" + nNode.getNodeName());
            MyLogging.log(Level.INFO,"----------------------------");
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                MyLogging.log(Level.INFO,"Classification : " + eElement.getElementsByTagName("Classification").item(0).getTextContent());
                tmpMap.put("Classification", eElement.getElementsByTagName("Classification").item(0).getTextContent());
                //MyLogging.log(Level.INFO,"Name : " + eElement.getElementsByTagName("Name").item(0).getTextContent());
                //tmpMap.put("Name", eElement.getElementsByTagName("Name").item(0).getTextContent());
                MyLogging.log(Level.INFO,"ExternalId : " + eElement.getElementsByTagName("ExternalId").item(0).getTextContent());
                tmpMap.put("ExternalId", eElement.getElementsByTagName("ExternalId").item(0).getTextContent());
                MyLogging.log(Level.INFO,"Title : " + eElement.getElementsByTagName("Title").item(0).getTextContent());
                tmpMap.put("Title", eElement.getElementsByTagName("Title").item(0).getTextContent());
                //MyLogging.log(Level.INFO,"Type : " + eElement.getElementsByTagName("Type").item(0).getTextContent());
                //tmpMap.put("Type", eElement.getElementsByTagName("Type").item(0).getTextContent());                                
                MyLogging.log(Level.INFO,"----------------------------");
                MyLogging.log(Level.INFO,"****************************");
                //XentryCustomerConcernMap.clear();
                //XentryCustomerConcernMap.put(temp, tmpMap);
                outputMap.put("XentryCustomerConcernMap", tmpMap);
                        
                NodeList PlxServiceMeasureNotesAndDefectKeyElement = eElement.getElementsByTagName("PlxCustomerConcernNotes");
                for (int temp3 = 0; temp3 < PlxServiceMeasureNotesAndDefectKeyElement.getLength(); temp3++) {
                    tmpMap_2 = new HashMap();
                    tmpMap_3 = new HashMap();
                    Node nNode3 = PlxServiceMeasureNotesAndDefectKeyElement.item(temp3);
                    MyLogging.log(Level.INFO,"Current Element :" + nNode3.getNodeName());
                    Element eElement3 = (Element)nNode3;
                    MyLogging.log(Level.INFO,"Description : " + eElement3.getElementsByTagName("Area").item(0).getTextContent());
                    tmpMap_3.put("Description", eElement.getElementsByTagName("Area").item(0).getTextContent());
                    MyLogging.log(Level.INFO,"Cause : " + eElement3.getElementsByTagName("Cause").item(0).getTextContent());
                    tmpMap_2.put("Cause", eElement.getElementsByTagName("Cause").item(0).getTextContent());
                    MyLogging.log(Level.INFO,"Correction : " + eElement3.getElementsByTagName("Correction").item(0).getTextContent());
                    tmpMap_2.put("Correction", eElement.getElementsByTagName("Correction").item(0).getTextContent());
                    MyLogging.log(Level.INFO,"Code : " + eElement3.getElementsByTagName("DefectKey").item(0).getTextContent());
                    tmpMap_3.put("Code", eElement.getElementsByTagName("DefectKey").item(0).getTextContent());
                    //MyLogging.log(Level.INFO,"Note : " + eElement3.getElementsByTagName("Area").item(0).getTextContent());
                    //tmpMap_2.put("Note", eElement.getElementsByTagName("Note").item(0).getTextContent());
                    //MyLogging.log(Level.INFO,"NoteType : " + eElement3.getElementsByTagName("Cause").item(0).getTextContent());
                    //tmpMap_2.put("NoteType", eElement.getElementsByTagName("NoteType").item(0).getTextContent());
                    //MyLogging.log(Level.INFO,"Private : " + eElement3.getElementsByTagName("Correction").item(0).getTextContent());
                    //tmpMap_2.put("Private", eElement.getElementsByTagName("Private").item(0).getTextContent());  
                    //PlxCustomerConcernNotesMap.clear();
                    //PlxCustomerConcernDefectKeyMap.clear();
                    //PlxCustomerConcernNotesMap.put(temp3,tmpMap_2);
                    //PlxCustomerConcernDefectKeyMap.put(temp3,tmpMap_3);
                }
                outputMap.put("PlxCustomerConcernNotesMap", tmpMap_2);
                outputMap.put("PlxCustomerConcernDefectKeyMap", tmpMap_3);
                
                Map pccp = new HashMap();
                NodeList PlxCustomerConcernPartsElement = eElement.getElementsByTagName("PlxCustomerConcernParts");                
                for (int temp2 = 0; temp2 < PlxCustomerConcernPartsElement.getLength(); temp2++) {
                    Node nNode2 = PlxCustomerConcernPartsElement.item(temp2);
                    MyLogging.log(Level.INFO,"\nCurrent Element :" + nNode2.getNodeName());
                    Element eElement2 = (Element)nNode2; 
                    Map tmpMap_4  = new HashMap();
                    Map tmpMap_5  = new HashMap();
                    MyLogging.log(Level.INFO,"EditType : " + eElement2.getElementsByTagName("EditType").item(0).getTextContent());                    
                    tmpMap_4.put("EditType", eElement2.getElementsByTagName("EditType").item(0).getTextContent());
                    MyLogging.log(Level.INFO,"Origin : " + eElement2.getElementsByTagName("Origin").item(0).getTextContent());
                    tmpMap_4.put("Origin", eElement2.getElementsByTagName("Origin").item(0).getTextContent());
                    MyLogging.log(Level.INFO,"Quantity : " + eElement2.getElementsByTagName("Quantity").item(0).getTextContent());
                    tmpMap_4.put("Quantity", eElement2.getElementsByTagName("Quantity").item(0).getTextContent());
//                    MyLogging.log(Level.INFO,"Name : " + eElement2.getElementsByTagName("PartName").item(0).getTextContent());
//                    tmpMap_4.put("Name", eElement2.getElementsByTagName("PartName").item(0).getTextContent());                    
//                    MyLogging.log(Level.INFO,"PartNumber : " + eElement2.getElementsByTagName("PartNumber").item(0).getTextContent());
//                    tmpMap_4.put("PartNumber", eElement2.getElementsByTagName("PartNumber").item(0).getTextContent());
                    
                    
                    NodeList PlxCustomerConcernPriceElement = eElement2.getElementsByTagName("PlxCustomerConcernPrice");
                    for (int temp3 = 0; temp3 < PlxCustomerConcernPriceElement.getLength(); temp3++) {
                        Node nNode3 = PlxCustomerConcernPriceElement.item(temp3);
                        MyLogging.log(Level.INFO,"Current Element :" + nNode3.getNodeName());
                        Element eElement3 = (Element)nNode3;
                        MyLogging.log(Level.INFO,"GrossPrice : " + eElement3.getElementsByTagName("GrossPrice").item(0).getTextContent());
                        tmpMap_5.put("GrossPrice", eElement3.getElementsByTagName("GrossPrice").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"NetPrice : " + eElement3.getElementsByTagName("NetPrice").item(0).getTextContent());
                        tmpMap_5.put("NetPrice", eElement3.getElementsByTagName("NetPrice").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"PriceName : " + eElement3.getElementsByTagName("PriceName").item(0).getTextContent());
                        tmpMap_5.put("PriceName", eElement3.getElementsByTagName("PriceName").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"Origin : " + eElement3.getElementsByTagName("Origin").item(0).getTextContent());
                        tmpMap_5.put("Origin", eElement3.getElementsByTagName("Origin").item(0).getTextContent());                                                                                                                                                
                    }
                    tmpMap_4.put("PartPrice", tmpMap_5);  
                    //PlxCustomerConcernPartsMap.clear();
                    pccp.put(temp2,tmpMap_4);
                }
                outputMap.put("PlxCustomerConcernPartsMap", pccp);
                
                Map pccwip = new HashMap();
                NodeList PlxCustomerConcernWorkItemElement = eElement.getElementsByTagName("PlxCustomerConcernWorkItem");                
                for (int temp2 = 0; temp2 < PlxCustomerConcernWorkItemElement.getLength(); temp2++) {
                    Node nNode2 = PlxCustomerConcernWorkItemElement.item(temp2);
                    MyLogging.log(Level.INFO,"\nCurrent Element :" + nNode2.getNodeName());
                    Element eElement2 = (Element)nNode2; 
                    Map tmpMap_6  = new HashMap();
                    Map tmpMap_7  = new HashMap();
                    MyLogging.log(Level.INFO,"EditType : " + eElement2.getElementsByTagName("EditType").item(0).getTextContent());                    
                    tmpMap_6.put("EditType", eElement2.getElementsByTagName("EditType").item(0).getTextContent());
                    MyLogging.log(Level.INFO,"Origin : " + eElement2.getElementsByTagName("Origin").item(0).getTextContent());
                    tmpMap_6.put("Origin", eElement2.getElementsByTagName("Origin").item(0).getTextContent());
                    MyLogging.log(Level.INFO,"Description : " + eElement2.getElementsByTagName("Description").item(0).getTextContent());
                    tmpMap_6.put("Description", eElement2.getElementsByTagName("Description").item(0).getTextContent());
                    MyLogging.log(Level.INFO,"Number : " + eElement2.getElementsByTagName("Number2").item(0).getTextContent());
                    tmpMap_6.put("Number", eElement2.getElementsByTagName("Number2").item(0).getTextContent());
                    MyLogging.log(Level.INFO,"Unit : " + eElement2.getElementsByTagName("Unit").item(0).getTextContent());
                    tmpMap_6.put("Unit", eElement2.getElementsByTagName("Unit").item(0).getTextContent());
                    MyLogging.log(Level.INFO,"Value : " + eElement2.getElementsByTagName("Value").item(0).getTextContent());
                    tmpMap_6.put("Value", eElement2.getElementsByTagName("Value").item(0).getTextContent());
                    
                    
                    NodeList PlxCustomerConcernWorkItemPriceElement = eElement2.getElementsByTagName("PlxCustomerConcernWorkItemPrice");
                    for (int temp3 = 0; temp3 < PlxCustomerConcernWorkItemPriceElement.getLength(); temp3++) {
                        Node nNode3 = PlxCustomerConcernWorkItemPriceElement.item(temp3);
                        MyLogging.log(Level.INFO,"Current Element :" + nNode3.getNodeName());
                        Element eElement3 = (Element)nNode3;
                        MyLogging.log(Level.INFO,"GrossPrice : " + eElement3.getElementsByTagName("GrossPrice2").item(0).getTextContent());
                        tmpMap_7.put("GrossPrice", eElement3.getElementsByTagName("GrossPrice2").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"NetPrice : " + eElement3.getElementsByTagName("NetPrice2").item(0).getTextContent());
                        tmpMap_7.put("NetPrice", eElement3.getElementsByTagName("NetPrice2").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"PriceName : " + eElement3.getElementsByTagName("PriceName").item(0).getTextContent());
                        tmpMap_7.put("PriceName", eElement3.getElementsByTagName("PriceName").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"Origin : " + eElement3.getElementsByTagName("Origin").item(0).getTextContent());
                        tmpMap_7.put("Origin", eElement3.getElementsByTagName("Origin").item(0).getTextContent());                                                                                                                                                
                    }
                    tmpMap_6.put("WorkItemPrice", tmpMap_7);  
                    //PlxCustomerConcernWorkItemMap.clear();
                    pccwip.put(temp2,tmpMap_6);
                }
                outputMap.put("PlxCustomerConcernWorkItemMap", pccwip);
                
                Map pccspm = new HashMap();
                NodeList PlxCustomerConcernServicePackageElement = eElement.getElementsByTagName("PlxCustomerConcernServicePackage");                
                for (int temp2 = 0; temp2 < PlxCustomerConcernServicePackageElement.getLength(); temp2++) {
                    Node nNode2 = PlxCustomerConcernServicePackageElement.item(temp2);
                    MyLogging.log(Level.INFO,"\nCurrent Element :" + nNode2.getNodeName());
                    Element eElement2 = (Element)nNode2; 
                    Map tmpMap_8  = new HashMap();
                    Map tmpMap_9  = new HashMap();
                    Map tmpMap_10  = new HashMap();
                    Map tmpMap_11  = new HashMap();
                    
                    MyLogging.log(Level.INFO,"Description : " + eElement2.getElementsByTagName("Description").item(0).getTextContent());                    
                    tmpMap_8.put("Description", eElement2.getElementsByTagName("Description").item(0).getTextContent());
                    MyLogging.log(Level.INFO,"DmsStatus : " + eElement2.getElementsByTagName("DmsStatus").item(0).getTextContent());
                    tmpMap_8.put("DmsStatus", eElement2.getElementsByTagName("DmsStatus").item(0).getTextContent());
                    MyLogging.log(Level.INFO,"EditType : " + eElement2.getElementsByTagName("EditType").item(0).getTextContent());
                    tmpMap_8.put("EditType", eElement2.getElementsByTagName("EditType").item(0).getTextContent());
                    MyLogging.log(Level.INFO,"ExternalId : " + eElement2.getElementsByTagName("ExternalId2").item(0).getTextContent());
                    tmpMap_8.put("ExternalId", eElement2.getElementsByTagName("ExternalId2").item(0).getTextContent());
                    MyLogging.log(Level.INFO,"Number : " + eElement2.getElementsByTagName("Number").item(0).getTextContent());
                    tmpMap_8.put("Number", eElement2.getElementsByTagName("Number").item(0).getTextContent());
                    MyLogging.log(Level.INFO,"Origin : " + eElement2.getElementsByTagName("Origin").item(0).getTextContent());
                    tmpMap_8.put("Origin", eElement2.getElementsByTagName("Origin").item(0).getTextContent());
                    MyLogging.log(Level.INFO,"PositionNumber : " + eElement2.getElementsByTagName("PositionNumber").item(0).getTextContent());
                    tmpMap_8.put("PositionNumber", eElement2.getElementsByTagName("PositionNumber").item(0).getTextContent());
                    
                    
                    NodeList PlxCustomerConcernServicePackagePriceElement = eElement2.getElementsByTagName("PlxCustomerConcernServicePackagePrice");                     
                    for (int temp3 = 0; temp3 < PlxCustomerConcernServicePackagePriceElement.getLength(); temp3++) {
                        
                        Node nNode3 = PlxCustomerConcernServicePackagePriceElement.item(temp3);                    
                        MyLogging.log(Level.INFO,"Current Element :" + nNode3.getNodeName());
                        Element eElement3 = (Element)nNode3;                           
                   
                        MyLogging.log(Level.INFO,"GrossPrice : " + eElement3.getElementsByTagName("GrossPrice2").item(0).getTextContent());
                        tmpMap_9.put("GrossPrice", eElement3.getElementsByTagName("GrossPrice2").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"NetPrice : " + eElement3.getElementsByTagName("NetPrice2").item(0).getTextContent());
                        tmpMap_9.put("NetPrice", eElement3.getElementsByTagName("NetPrice2").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"PriceName : " + eElement3.getElementsByTagName("PriceName").item(0).getTextContent());
                        tmpMap_9.put("PriceName", eElement3.getElementsByTagName("PriceName").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"Origin : " + eElement3.getElementsByTagName("Origin").item(0).getTextContent());
                        tmpMap_9.put("Origin", eElement3.getElementsByTagName("Origin").item(0).getTextContent()); 
                        
                    }
                    tmpMap_8.put("PackagePrice", tmpMap_9);                    
                    
                    
                    NodeList PlxCustomerConcernServicePackagePartsElement = eElement2.getElementsByTagName("PlxCustomerConcernServicePackageParts");
                    for (int temp3 = 0; temp3 < PlxCustomerConcernServicePackagePartsElement.getLength(); temp3++) {
                        Node nNode3 = PlxCustomerConcernServicePackagePartsElement.item(temp3);
                        MyLogging.log(Level.INFO,"Current Element :" + nNode3.getNodeName());
                        Element eElement3 = (Element)nNode3;
                        MyLogging.log(Level.INFO,"EditType : " + eElement3.getElementsByTagName("EditType").item(0).getTextContent());
                        tmpMap_10.put("EditType", eElement3.getElementsByTagName("EditType").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"Origin : " + eElement3.getElementsByTagName("Origin").item(0).getTextContent());
                        tmpMap_10.put("Origin", eElement3.getElementsByTagName("Origin").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"Quantity : " + eElement3.getElementsByTagName("Quantity").item(0).getTextContent());
                        tmpMap_10.put("Quantity", eElement3.getElementsByTagName("Quantity").item(0).getTextContent());                                                                                                                                                                      
                    }
                    tmpMap_8.put("PackagePart", tmpMap_10);
                    
                    NodeList PlxCustomerConcernServicePackageWorkItemElement = eElement2.getElementsByTagName("PlxCustomerConcernServicePackageWorkItem");
                    for (int temp3 = 0; temp3 < PlxCustomerConcernServicePackageWorkItemElement.getLength(); temp3++) {
                        Node nNode3 = PlxCustomerConcernServicePackageWorkItemElement.item(temp3);
                        MyLogging.log(Level.INFO,"Current Element :" + nNode3.getNodeName());
                        Element eElement3 = (Element)nNode3;
                        MyLogging.log(Level.INFO,"Description : " + eElement3.getElementsByTagName("Description").item(0).getTextContent());
                        tmpMap_11.put("Description", eElement3.getElementsByTagName("Description").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"EditType : " + eElement3.getElementsByTagName("EditType").item(0).getTextContent());
                        tmpMap_11.put("EditType", eElement3.getElementsByTagName("EditType").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"Number : " + eElement3.getElementsByTagName("Number2").item(0).getTextContent());
                        tmpMap_11.put("Number", eElement3.getElementsByTagName("Number2").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"Origin : " + eElement3.getElementsByTagName("Origin").item(0).getTextContent());
                        tmpMap_11.put("Origin", eElement3.getElementsByTagName("Origin").item(0).getTextContent());  
                        MyLogging.log(Level.INFO,"Unit : " + eElement3.getElementsByTagName("Unit").item(0).getTextContent());
                        tmpMap_11.put("Unit", eElement3.getElementsByTagName("Unit").item(0).getTextContent());  
                        MyLogging.log(Level.INFO,"Value : " + eElement3.getElementsByTagName("Value").item(0).getTextContent());
                        tmpMap_11.put("Value", eElement3.getElementsByTagName("Value").item(0).getTextContent());  
                    }
                    tmpMap_8.put("PackageWorkItem", tmpMap_11); 
                    //PlxCustomerConcernServicePackageMap.clear();
                    pccspm.put(temp2,tmpMap_8);
                }
                outputMap.put("PlxCustomerConcernServicePackageMap", pccspm);
            }
            XentryInitJobCustomerConcernMap.put(temp, outputMap);
        }
        return XentryInitJobCustomerConcernMap;
    }
    
    private Map parseCustomerConcern() throws ParserConfigurationException, SAXException, IOException{       
        NodeList nList = customer_concern_xml_doc.getElementsByTagName("PlxXentryCustomerConcern");
        
        MyLogging.log(Level.INFO,"----------------------------");
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Map tmpMap = new HashMap();
            Node nNode = nList.item(temp);
            MyLogging.log(Level.INFO,"\nCurrent Element :" + nNode.getNodeName());
            MyLogging.log(Level.INFO,"----------------------------");
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                MyLogging.log(Level.INFO,"Classification : " + eElement.getElementsByTagName("Classification").item(0).getTextContent());
                tmpMap.put("Classification", eElement.getElementsByTagName("Classification").item(0).getTextContent());
                //MyLogging.log(Level.INFO,"Name : " + eElement.getElementsByTagName("Name").item(0).getTextContent());
                //tmpMap.put("Name", eElement.getElementsByTagName("Name").item(0).getTextContent());
                MyLogging.log(Level.INFO,"ExternalId : " + eElement.getElementsByTagName("ExternalId").item(0).getTextContent());
                tmpMap.put("ExternalId", eElement.getElementsByTagName("ExternalId").item(0).getTextContent());
                MyLogging.log(Level.INFO,"Title : " + eElement.getElementsByTagName("Title").item(0).getTextContent());
                tmpMap.put("Title", eElement.getElementsByTagName("Title").item(0).getTextContent());
                //MyLogging.log(Level.INFO,"Type : " + eElement.getElementsByTagName("Type").item(0).getTextContent());
               // tmpMap.put("Type", eElement.getElementsByTagName("Type").item(0).getTextContent());                                
                MyLogging.log(Level.INFO,"----------------------------");
                MyLogging.log(Level.INFO,"****************************");
                
                XentryCustomerConcernMap.put(temp, tmpMap);                                
            }
            
        }
        return XentryCustomerConcernMap;
    }
    
    private Map parseCustomerConcernNotesAndDefectKey() throws ParserConfigurationException, SAXException, IOException{        
        NodeList nList = customer_concern_xml_doc.getElementsByTagName("PlxXentryCustomerConcern");
        MyLogging.log(Level.INFO,"----------------------------");
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            MyLogging.log(Level.INFO,"\nCurrent Element :" + nNode.getNodeName());
            MyLogging.log(Level.INFO,"----------------------------");
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;                
                NodeList ListOfPlxServiceMeasureNotesAndDefectKeyElement = eElement.getElementsByTagName("ListOfPlxCustomerConcernNotes");                
                for (int temp2 = 0; temp2 < ListOfPlxServiceMeasureNotesAndDefectKeyElement.getLength(); temp2++) {
                    Node nNode2 = ListOfPlxServiceMeasureNotesAndDefectKeyElement.item(temp2);
                    MyLogging.log(Level.INFO,"\nCurrent Element :" + nNode2.getNodeName());
                    Element eElement2 = (Element)nNode2; 
                    NodeList PlxServiceMeasureNotesAndDefectKeyElement = eElement2.getElementsByTagName("PlxCustomerConcernNotes");
                    for (int temp3 = 0; temp3 < PlxServiceMeasureNotesAndDefectKeyElement.getLength(); temp3++) {
                        Node nNode3 = PlxServiceMeasureNotesAndDefectKeyElement.item(temp3);
                        MyLogging.log(Level.INFO,"Current Element :" + nNode3.getNodeName());
                        Element eElement3 = (Element)nNode3;
                        MyLogging.log(Level.INFO,"Area : " + eElement3.getElementsByTagName("Area").item(0).getTextContent());
                        PlxCustomerConcernNotesMap.put("Area", eElement.getElementsByTagName("Area").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"Cause : " + eElement3.getElementsByTagName("Cause").item(0).getTextContent());
                        PlxCustomerConcernNotesMap.put("Cause", eElement.getElementsByTagName("Cause").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"Correction : " + eElement3.getElementsByTagName("Correction").item(0).getTextContent());
                        PlxCustomerConcernNotesMap.put("Correction", eElement.getElementsByTagName("Correction").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"DefectKey : " + eElement3.getElementsByTagName("DefectKey").item(0).getTextContent());
                        PlxCustomerConcernDefectKeyMap.put("DefectKey", eElement.getElementsByTagName("DefectKey").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"Note : " + eElement3.getElementsByTagName("Area").item(0).getTextContent());
                        PlxCustomerConcernNotesMap.put("Note", eElement.getElementsByTagName("Note").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"NoteType : " + eElement3.getElementsByTagName("Cause").item(0).getTextContent());
                        PlxCustomerConcernNotesMap.put("NoteType", eElement.getElementsByTagName("NoteType").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"Private : " + eElement3.getElementsByTagName("Correction").item(0).getTextContent());
                        PlxCustomerConcernNotesMap.put("Private", eElement.getElementsByTagName("Private").item(0).getTextContent());                                              
                    }
                }
                                                                                               
                MyLogging.log(Level.INFO,"----------------------------");
                MyLogging.log(Level.INFO,"****************************");                                
            }                        
        }
        return PlxCustomerConcernNotesMap;
    }
    
    private Map parsePlxCustomerConcernParts() throws  ParserConfigurationException, UnsupportedEncodingException, SAXException, IOException{                
        Map tmpMap_2  = new HashMap();         
        NodeList nList = customer_concern_xml_doc.getElementsByTagName("ListOfPlxCustomerConcernParts");
        MyLogging.log(Level.INFO,"----------------------------");
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            MyLogging.log(Level.INFO,"\nCurrent Element :" + nNode.getNodeName());
            MyLogging.log(Level.INFO,"----------------------------");
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                              
                NodeList PlxCustomerConcernPartsElement = eElement.getElementsByTagName("PlxCustomerConcernParts");
                
                for (int temp2 = 0; temp2 < PlxCustomerConcernPartsElement.getLength(); temp2++) {
                    Node nNode2 = PlxCustomerConcernPartsElement.item(temp2);
                    MyLogging.log(Level.INFO,"\nCurrent Element :" + nNode2.getNodeName());
                    Element eElement2 = (Element)nNode2; 
                    Map tmpMap  = new HashMap();
                    MyLogging.log(Level.INFO,"EditType : " + eElement2.getElementsByTagName("EditType").item(0).getTextContent());
                    
                    tmpMap.put("EditType", eElement2.getElementsByTagName("EditType").item(0).getTextContent());
                    MyLogging.log(Level.INFO,"Origin : " + eElement2.getElementsByTagName("Origin").item(0).getTextContent());
                    tmpMap.put("Origin", eElement2.getElementsByTagName("Origin").item(0).getTextContent());
                    MyLogging.log(Level.INFO,"Quantity : " + eElement2.getElementsByTagName("Quantity").item(0).getTextContent());
                    tmpMap.put("Quantity", eElement2.getElementsByTagName("Quantity").item(0).getTextContent());
                    
                    NodeList PlxCustomerConcernPriceElement = eElement2.getElementsByTagName("PlxCustomerConcernPrice");
                    for (int temp3 = 0; temp3 < PlxCustomerConcernPriceElement.getLength(); temp3++) {
                        Node nNode3 = PlxCustomerConcernPriceElement.item(temp3);
                        MyLogging.log(Level.INFO,"Current Element :" + nNode3.getNodeName());
                        Element eElement3 = (Element)nNode3;
                        MyLogging.log(Level.INFO,"GrossPrice : " + eElement3.getElementsByTagName("GrossPrice").item(0).getTextContent());
                        tmpMap_2.put("GrossPrice", eElement3.getElementsByTagName("GrossPrice").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"NetPrice : " + eElement3.getElementsByTagName("NetPrice").item(0).getTextContent());
                        tmpMap_2.put("NetPrice", eElement3.getElementsByTagName("NetPrice").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"PriceName : " + eElement3.getElementsByTagName("PriceName").item(0).getTextContent());
                        tmpMap_2.put("PriceName", eElement3.getElementsByTagName("PriceName").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"Origin : " + eElement3.getElementsByTagName("Origin").item(0).getTextContent());
                        tmpMap_2.put("Origin", eElement3.getElementsByTagName("Origin").item(0).getTextContent());                                                                                                                                                
                    }
                    tmpMap.put("PartPrice", tmpMap_2);                    
                    PlxCustomerConcernPartsMap.put(temp2,tmpMap);
                }
                
                MyLogging.log(Level.INFO,"----------------------------");
                MyLogging.log(Level.INFO,"****************************");
                
                
            }                        
        }
        return PlxCustomerConcernPartsMap;
    }
    
    private Map parsePlxCustomerConcernServicePackage() throws  ParserConfigurationException, UnsupportedEncodingException, SAXException, IOException{
        
        Map tmpMap_2  = new HashMap();
        Map tmpMap_3  = new HashMap(); 
        Map tmpMap_4  = new HashMap();
        
        NodeList nList = customer_concern_xml_doc.getElementsByTagName("ListOfPlxCustomerConcernServicePackage");
        MyLogging.log(Level.INFO,"----------------------------");
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            MyLogging.log(Level.INFO,"\nCurrent Element :" + nNode.getNodeName());
            MyLogging.log(Level.INFO,"----------------------------");
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                              
                NodeList PlxCustomerConcernServicePackageElement = eElement.getElementsByTagName("PlxCustomerConcernServicePackage");
                
                for (int temp2 = 0; temp2 < PlxCustomerConcernServicePackageElement.getLength(); temp2++) {
                    Node nNode2 = PlxCustomerConcernServicePackageElement.item(temp2);
                    MyLogging.log(Level.INFO,"\nCurrent Element :" + nNode2.getNodeName());
                    Element eElement2 = (Element)nNode2; 
                    Map tmpMap  = new HashMap();
                    MyLogging.log(Level.INFO,"Description : " + eElement2.getElementsByTagName("Description").item(0).getTextContent());                    
                    tmpMap.put("Description", eElement2.getElementsByTagName("Description").item(0).getTextContent());
                    MyLogging.log(Level.INFO,"DmsStatus : " + eElement2.getElementsByTagName("DmsStatus").item(0).getTextContent());
                    tmpMap.put("DmsStatus", eElement2.getElementsByTagName("DmsStatus").item(0).getTextContent());
                    MyLogging.log(Level.INFO,"EditType : " + eElement2.getElementsByTagName("EditType").item(0).getTextContent());
                    tmpMap.put("EditType", eElement2.getElementsByTagName("EditType").item(0).getTextContent());
                    MyLogging.log(Level.INFO,"ExternalId : " + eElement2.getElementsByTagName("ExternalId2").item(0).getTextContent());
                    tmpMap.put("ExternalId", eElement2.getElementsByTagName("ExternalId2").item(0).getTextContent());
                    MyLogging.log(Level.INFO,"Number : " + eElement2.getElementsByTagName("Number").item(0).getTextContent());
                    tmpMap.put("Number", eElement2.getElementsByTagName("Number").item(0).getTextContent());
                    MyLogging.log(Level.INFO,"Origin : " + eElement2.getElementsByTagName("Origin").item(0).getTextContent());
                    tmpMap.put("Origin", eElement2.getElementsByTagName("Origin").item(0).getTextContent());
                    MyLogging.log(Level.INFO,"PositionNumber : " + eElement2.getElementsByTagName("PositionNumber").item(0).getTextContent());
                    tmpMap.put("PositionNumber", eElement2.getElementsByTagName("PositionNumber").item(0).getTextContent());
                    
                    
                    NodeList PlxCustomerConcernServicePackagePriceElement = eElement2.getElementsByTagName("PlxCustomerConcernServicePackagePrice"); 
                    
                    for (int temp3 = 0; temp3 < PlxCustomerConcernServicePackagePriceElement.getLength(); temp3++) {
                        
                        Node nNode3 = PlxCustomerConcernServicePackagePriceElement.item(temp3);                    
                        MyLogging.log(Level.INFO,"Current Element :" + nNode3.getNodeName());
                        Element eElement3 = (Element)nNode3;                           
                   
                        MyLogging.log(Level.INFO,"GrossPrice : " + eElement3.getElementsByTagName("GrossPrice2").item(0).getTextContent());
                        tmpMap_2.put("GrossPrice", eElement3.getElementsByTagName("GrossPrice2").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"NetPrice : " + eElement3.getElementsByTagName("NetPrice2").item(0).getTextContent());
                        tmpMap_2.put("NetPrice", eElement3.getElementsByTagName("NetPrice2").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"PriceName : " + eElement3.getElementsByTagName("PriceName").item(0).getTextContent());
                        tmpMap_2.put("PriceName", eElement3.getElementsByTagName("PriceName").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"Origin : " + eElement3.getElementsByTagName("Origin").item(0).getTextContent());
                        tmpMap_2.put("Origin", eElement3.getElementsByTagName("Origin").item(0).getTextContent()); 
                        
                    }
                    tmpMap.put("PackagePrice", tmpMap_2);                    
                    
                    
                    NodeList PlxCustomerConcernServicePackagePartsElement = eElement2.getElementsByTagName("PlxCustomerConcernServicePackageParts");
                    for (int temp3 = 0; temp3 < PlxCustomerConcernServicePackagePartsElement.getLength(); temp3++) {
                        Node nNode3 = PlxCustomerConcernServicePackagePartsElement.item(temp3);
                        MyLogging.log(Level.INFO,"Current Element :" + nNode3.getNodeName());
                        Element eElement3 = (Element)nNode3;
                        MyLogging.log(Level.INFO,"EditType : " + eElement3.getElementsByTagName("EditType").item(0).getTextContent());
                        tmpMap_3.put("EditType", eElement3.getElementsByTagName("EditType").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"Origin : " + eElement3.getElementsByTagName("Origin").item(0).getTextContent());
                        tmpMap_3.put("Origin", eElement3.getElementsByTagName("Origin").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"Quantity : " + eElement3.getElementsByTagName("Quantity").item(0).getTextContent());
                        tmpMap_3.put("Quantity", eElement3.getElementsByTagName("Quantity").item(0).getTextContent());                                                                                                                                                                      
                    }
                    tmpMap.put("PackagePart", tmpMap_3);
                    
                    NodeList PlxCustomerConcernServicePackageWorkItemElement = eElement2.getElementsByTagName("PlxCustomerConcernServicePackageWorkItem");
                    for (int temp3 = 0; temp3 < PlxCustomerConcernServicePackageWorkItemElement.getLength(); temp3++) {
                        Node nNode3 = PlxCustomerConcernServicePackageWorkItemElement.item(temp3);
                        MyLogging.log(Level.INFO,"Current Element :" + nNode3.getNodeName());
                        Element eElement3 = (Element)nNode3;
                        MyLogging.log(Level.INFO,"Description : " + eElement3.getElementsByTagName("Description").item(0).getTextContent());
                        tmpMap_4.put("Description", eElement3.getElementsByTagName("Description").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"EditType : " + eElement3.getElementsByTagName("EditType").item(0).getTextContent());
                        tmpMap_4.put("EditType", eElement3.getElementsByTagName("EditType").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"Number : " + eElement3.getElementsByTagName("Number2").item(0).getTextContent());
                        tmpMap_4.put("Number", eElement3.getElementsByTagName("Number2").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"Origin : " + eElement3.getElementsByTagName("Origin").item(0).getTextContent());
                        tmpMap_4.put("Origin", eElement3.getElementsByTagName("Origin").item(0).getTextContent());  
                        MyLogging.log(Level.INFO,"Unit : " + eElement3.getElementsByTagName("Unit").item(0).getTextContent());
                        tmpMap_4.put("Unit", eElement3.getElementsByTagName("Unit").item(0).getTextContent());  
                        MyLogging.log(Level.INFO,"Value : " + eElement3.getElementsByTagName("Value").item(0).getTextContent());
                        tmpMap_4.put("Value", eElement3.getElementsByTagName("Value").item(0).getTextContent());  
                    }
                    tmpMap.put("PackageWorkItem", tmpMap_4);
                    
                    PlxCustomerConcernPartsMap.put(temp2,tmpMap);
                }
                
                MyLogging.log(Level.INFO,"----------------------------");
                MyLogging.log(Level.INFO,"****************************");
                
                
            }                        
        }
        return PlxCustomerConcernPartsMap;
    }
    
    private Map parsePlxCustomerConcernWorkItem() throws  ParserConfigurationException, UnsupportedEncodingException, SAXException, IOException{
        Map tmpMap_2  = new HashMap();         
        NodeList nList = customer_concern_xml_doc.getElementsByTagName("ListOfPlxCustomerConcernWorkItem");
        MyLogging.log(Level.INFO,"----------------------------");
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            MyLogging.log(Level.INFO,"\nCurrent Element :" + nNode.getNodeName());
            MyLogging.log(Level.INFO,"----------------------------");
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                              
                NodeList PlxCustomerConcernWorkItemElement = eElement.getElementsByTagName("PlxCustomerConcernWorkItem");
                
                for (int temp2 = 0; temp2 < PlxCustomerConcernWorkItemElement.getLength(); temp2++) {
                    Node nNode2 = PlxCustomerConcernWorkItemElement.item(temp2);
                    MyLogging.log(Level.INFO,"\nCurrent Element :" + nNode2.getNodeName());
                    Element eElement2 = (Element)nNode2; 
                    Map tmpMap  = new HashMap();
                    MyLogging.log(Level.INFO,"EditType : " + eElement2.getElementsByTagName("EditType").item(0).getTextContent());                    
                    tmpMap.put("EditType", eElement2.getElementsByTagName("EditType").item(0).getTextContent());
                    MyLogging.log(Level.INFO,"Origin : " + eElement2.getElementsByTagName("Origin").item(0).getTextContent());
                    tmpMap.put("Origin", eElement2.getElementsByTagName("Origin").item(0).getTextContent());
                    MyLogging.log(Level.INFO,"Description : " + eElement2.getElementsByTagName("Description").item(0).getTextContent());
                    tmpMap.put("Description", eElement2.getElementsByTagName("Description").item(0).getTextContent());
                    MyLogging.log(Level.INFO,"Number : " + eElement2.getElementsByTagName("Number2").item(0).getTextContent());
                    tmpMap.put("Number", eElement2.getElementsByTagName("Number2").item(0).getTextContent());
                    MyLogging.log(Level.INFO,"Unit : " + eElement2.getElementsByTagName("Unit").item(0).getTextContent());
                    tmpMap.put("Unit", eElement2.getElementsByTagName("Unit").item(0).getTextContent());
                    MyLogging.log(Level.INFO,"Value : " + eElement2.getElementsByTagName("Value").item(0).getTextContent());
                    tmpMap.put("Value", eElement2.getElementsByTagName("Value").item(0).getTextContent());
                    
                    
                    NodeList PlxCustomerConcernWorkItemPriceElement = eElement2.getElementsByTagName("PlxCustomerConcernWorkItemPrice");
                    for (int temp3 = 0; temp3 < PlxCustomerConcernWorkItemPriceElement.getLength(); temp3++) {
                        Node nNode3 = PlxCustomerConcernWorkItemPriceElement.item(temp3);
                        MyLogging.log(Level.INFO,"Current Element :" + nNode3.getNodeName());
                        Element eElement3 = (Element)nNode3;
                        MyLogging.log(Level.INFO,"GrossPrice : " + eElement3.getElementsByTagName("GrossPrice2").item(0).getTextContent());
                        tmpMap_2.put("GrossPrice", eElement3.getElementsByTagName("GrossPrice2").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"NetPrice : " + eElement3.getElementsByTagName("NetPrice2").item(0).getTextContent());
                        tmpMap_2.put("NetPrice", eElement3.getElementsByTagName("NetPrice2").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"PriceName : " + eElement3.getElementsByTagName("PriceName").item(0).getTextContent());
                        tmpMap_2.put("PriceName", eElement3.getElementsByTagName("PriceName").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"Origin : " + eElement3.getElementsByTagName("Origin").item(0).getTextContent());
                        tmpMap_2.put("Origin", eElement3.getElementsByTagName("Origin").item(0).getTextContent());                                                                                                                                                
                    }
                    tmpMap.put("WorkItemPrice", tmpMap_2);                    
                    PlxCustomerConcernWorkItemMap.put(temp2,tmpMap);
                }
                
                MyLogging.log(Level.INFO,"----------------------------");
                MyLogging.log(Level.INFO,"****************************");
                
                
            }                        
        }
        return PlxCustomerConcernWorkItemMap;
    }
    
    public Map getCustomerConcern(){
        return XentryCustomerConcernMap;
    }
    
    public Map getPlxCustomerConcernNotes(){
        return PlxCustomerConcernNotesMap;
    }
    
    public Map getPlxCustomerConcernDefectKey(){
        return PlxCustomerConcernDefectKeyMap;
    }
    
    public Map getPlxCustomerConcernParts(){
        return PlxCustomerConcernPartsMap;
    }
    
    public Map getPlxCustomerConcernWorkItem(){
        return PlxCustomerConcernWorkItemMap;
    }
    
    public Map getPlxCustomerConcernServicePackage(){
        return PlxCustomerConcernServicePackageMap;
    }
    
    public Map getXentryInitJobCustomerConcernMap(){
        return XentryInitJobCustomerConcernMap;
    }
    
    public static void main(String[] args) {
        String xmldoc = new TestString().xc2;
        XentryCustomerConcern xc = new XentryCustomerConcern(xmldoc);
        
    }
}
