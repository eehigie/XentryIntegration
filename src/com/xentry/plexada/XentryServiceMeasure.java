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
//import java.util.logging.Logger;
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
public class XentryServiceMeasure {
    
    private static String service_measure_xml;
    private static Document service_measure_xml_doc = null;
    
    private final Map XentryServiceMeasureMap;
    private final Map PlxXentryServiceMeasureMap;
    private final Map PlxServiceMeasureNotesAndDefectKeyMap;
    private final Map PlxServiceMeasurePartsMap;
    private final Map PlxServiceMeasureWorkItem;
    private final Map PlxServiceMeasurePackageMap;
    private final Map PlxServiceMeasurePackage_PlxServiceMeasurePackagePartsMap;
    private final Map PlxServiceMeasurePackage_PlxServiceMeasurePackageWorkItemMap;
    private static final StringWriter ERRORS = new StringWriter();
    
  
    public XentryServiceMeasure(String xmlDoc ) {
        this.PlxServiceMeasurePackage_PlxServiceMeasurePackageWorkItemMap = new HashMap();
        this.PlxServiceMeasurePackage_PlxServiceMeasurePackagePartsMap = new HashMap();
        this.PlxServiceMeasurePackageMap = new HashMap();
        this.PlxServiceMeasureWorkItem = new HashMap();
        this.PlxServiceMeasurePartsMap = new HashMap();
        this.PlxServiceMeasureNotesAndDefectKeyMap = new HashMap();
        this.XentryServiceMeasureMap = new HashMap();
        this.PlxXentryServiceMeasureMap = new HashMap();
        service_measure_xml = xmlDoc;
       try {
           handleXMLDoc();           
           parseXentryServiceMeasure();
           parseServiceMeasureNotesAndDefectKey();
           parsePlxServiceMeasureParts();
           parsePlxServiceMeasureWorkItem();
           parseServiceMeasurePackage();
       } catch (ParserConfigurationException ex) {
           ex.printStackTrace(new PrintWriter(ERRORS));
           MyLogging.log(Level.SEVERE, "ERROR IN Constuctor_1:"+ERRORS.toString());           
       } catch (SAXException ex) {
           ex.printStackTrace(new PrintWriter(ERRORS));
           MyLogging.log(Level.SEVERE, "ERROR IN Constuctor_1:"+ERRORS.toString());
       } catch (IOException ex) {
           ex.printStackTrace(new PrintWriter(ERRORS));
           MyLogging.log(Level.SEVERE, "ERROR IN Constuctor_1:"+ERRORS.toString());
       }
    }
    
    private void handleXMLDoc() throws ParserConfigurationException, UnsupportedEncodingException, SAXException, IOException{
        DocumentBuilderFactory factory =
        DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        
        StringBuilder xmlStringBuilder = new StringBuilder();
        xmlStringBuilder.append(service_measure_xml);
        ByteArrayInputStream input = new ByteArrayInputStream(xmlStringBuilder.toString().getBytes("UTF-8"));        
        service_measure_xml_doc = builder.parse(input);
    }
    
    private String getRootElement(){
        Element root = service_measure_xml_doc.getDocumentElement();
        return root.getNodeName();
    }
    
    private void parseXMLDocument() throws ParserConfigurationException, SAXException, IOException{
        String xmldoc = service_measure_xml;
        XmlFormatter formatter = new XmlFormatter();
        //System.out.println("xe "+xmldoc);
        String formatted_xmldoc = formatter.format(xmldoc);
        //System.out.println("xe "+formatted_xmldoc);
        XentryServiceMeasure xsm = new XentryServiceMeasure(xmldoc);
        xsm.handleXMLDoc();
        String re = xsm.getRootElement();
        System.out.println("RootElement is: "+re);
        //System.out.println("children :"+cust_concern_xml_doc.);
        NodeList nList = service_measure_xml_doc.getElementsByTagName("PlxXentryServiceMeasure");
        System.out.println("----------------------------");
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            System.out.println("\nCurrent Element :" + nNode.getNodeName());
            System.out.println("----------------------------");
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                System.out.println("Description : " + eElement.getElementsByTagName("Description").item(0).getTextContent());
                PlxXentryServiceMeasureMap.put("Description", eElement.getElementsByTagName("Description").item(0).getTextContent());
                System.out.println("Name : " + eElement.getElementsByTagName("Name").item(0).getTextContent());
                PlxXentryServiceMeasureMap.put("Name", eElement.getElementsByTagName("Name").item(0).getTextContent());
                System.out.println("ParentSHId : " + eElement.getElementsByTagName("ParentSHId").item(0).getTextContent());
                PlxXentryServiceMeasureMap.put("ParentSHId", eElement.getElementsByTagName("ParentSHId").item(0).getTextContent());
                System.out.println("Selected : " + eElement.getElementsByTagName("Selected").item(0).getTextContent());
                PlxXentryServiceMeasureMap.put("Selected", eElement.getElementsByTagName("Selected").item(0).getTextContent());
                System.out.println("ServiceMeasureType : " + eElement.getElementsByTagName("ServiceMeasureType").item(0).getTextContent());
                PlxXentryServiceMeasureMap.put("ServiceMeasureType", eElement.getElementsByTagName("ServiceMeasureType").item(0).getTextContent());
                //System.out.println("Description : " + eElement.getElementsByTagName("Description").item(0).getTextContent());
                NodeList ListOfPlxServiceMeasureNotesAndDefectKeyElement = eElement.getElementsByTagName("ListOfPlxServiceMeasureNotesAndDefectKey");                
                for (int temp2 = 0; temp2 < ListOfPlxServiceMeasureNotesAndDefectKeyElement.getLength(); temp2++) {
                    Node nNode2 = ListOfPlxServiceMeasureNotesAndDefectKeyElement.item(temp2);
                    System.out.println("\nCurrent Element :" + nNode2.getNodeName());
                    Element eElement2 = (Element)nNode2; 
                    NodeList PlxServiceMeasureNotesAndDefectKeyElement = eElement2.getElementsByTagName("PlxServiceMeasureNotesAndDefectKey");
                    for (int temp3 = 0; temp3 < PlxServiceMeasureNotesAndDefectKeyElement.getLength(); temp3++) {
                        Node nNode3 = PlxServiceMeasureNotesAndDefectKeyElement.item(temp3);
                        System.out.println("Current Element :" + nNode3.getNodeName());
                        Element eElement3 = (Element)nNode3;
                        System.out.println("Area : " + eElement3.getElementsByTagName("Area").item(0).getTextContent());
                        PlxServiceMeasureNotesAndDefectKeyMap.put("Area", eElement.getElementsByTagName("Area").item(0).getTextContent());
                        System.out.println("Cause : " + eElement3.getElementsByTagName("Cause").item(0).getTextContent());
                        PlxServiceMeasureNotesAndDefectKeyMap.put("Cause", eElement.getElementsByTagName("Cause").item(0).getTextContent());
                        System.out.println("Correction : " + eElement3.getElementsByTagName("Correction").item(0).getTextContent());
                        PlxServiceMeasureNotesAndDefectKeyMap.put("Correction", eElement.getElementsByTagName("Correction").item(0).getTextContent());
                        System.out.println("DefectKey : " + eElement3.getElementsByTagName("DefectKey").item(0).getTextContent());
                        PlxServiceMeasureNotesAndDefectKeyMap.put("DefectKey", eElement.getElementsByTagName("DefectKey").item(0).getTextContent());
                        System.out.println("DefectKeyDescriptioin : " + eElement3.getElementsByTagName("DefectKeyDescriptioin").item(0).getTextContent());
                        PlxServiceMeasureNotesAndDefectKeyMap.put("DefectKeyDescriptioin", eElement.getElementsByTagName("DefectKeyDescriptioin").item(0).getTextContent());
                        System.out.println("Note : " + eElement3.getElementsByTagName("Area").item(0).getTextContent());
                        PlxServiceMeasureNotesAndDefectKeyMap.put("Note", eElement.getElementsByTagName("Note").item(0).getTextContent());
                        System.out.println("NoteType : " + eElement3.getElementsByTagName("Cause").item(0).getTextContent());
                        PlxServiceMeasureNotesAndDefectKeyMap.put("NoteType", eElement.getElementsByTagName("NoteType").item(0).getTextContent());
                        System.out.println("Private : " + eElement3.getElementsByTagName("Correction").item(0).getTextContent());
                        PlxServiceMeasureNotesAndDefectKeyMap.put("Private", eElement.getElementsByTagName("Private").item(0).getTextContent());
                        System.out.println("ServiceMeasureId : " + eElement3.getElementsByTagName("DefectKey").item(0).getTextContent());
                        PlxServiceMeasureNotesAndDefectKeyMap.put("ServiceMeasureId", eElement.getElementsByTagName("ServiceMeasureId").item(0).getTextContent());                        
                    }
                }
                
                NodeList ListOfPlxServiceMeasurePartsKeyElement = eElement.getElementsByTagName("ListOfPlxServiceMeasureParts");                
                for (int temp2 = 0; temp2 < ListOfPlxServiceMeasurePartsKeyElement.getLength(); temp2++) {
                    Node nNode2 = ListOfPlxServiceMeasurePartsKeyElement.item(temp2);
                    System.out.println("\nCurrent Element :" + nNode2.getNodeName());
                    Element eElement2 = (Element)nNode2; 
                    NodeList PlxServiceMeasurePartsElement = eElement2.getElementsByTagName("PlxServiceMeasureParts");
                    for (int temp3 = 0; temp3 < PlxServiceMeasurePartsElement.getLength(); temp3++) {
                        Node nNode3 = PlxServiceMeasurePartsElement.item(temp3);
                        System.out.println("Current Element :" + nNode3.getNodeName());
                        Element eElement3 = (Element)nNode3;
                        System.out.println("AdditionalKey1 : " + eElement3.getElementsByTagName("AdditionalKey1").item(0).getTextContent());
                        PlxServiceMeasurePartsMap.put("AdditionalKey1", eElement.getElementsByTagName("AdditionalKey1").item(0).getTextContent());
                        System.out.println("AdditionalKey2 : " + eElement3.getElementsByTagName("AdditionalKey2").item(0).getTextContent());
                        PlxServiceMeasurePartsMap.put("AdditionalKey2", eElement.getElementsByTagName("AdditionalKey2").item(0).getTextContent());
                        System.out.println("CoverageListId : " + eElement3.getElementsByTagName("CoverageListId").item(0).getTextContent());
                        PlxServiceMeasurePartsMap.put("CoverageListId", eElement.getElementsByTagName("CoverageListId").item(0).getTextContent());
                        System.out.println("DMSStatus2 : " + eElement3.getElementsByTagName("DMSStatus2").item(0).getTextContent());
                        PlxServiceMeasurePartsMap.put("DMSStatus2", eElement.getElementsByTagName("DMSStatus2").item(0).getTextContent());
                        System.out.println("EditType : " + eElement3.getElementsByTagName("EditType").item(0).getTextContent());
                        PlxServiceMeasurePartsMap.put("EditType", eElement.getElementsByTagName("EditType").item(0).getTextContent());
                        System.out.println("ExternalId : " + eElement3.getElementsByTagName("ExternalId").item(0).getTextContent());
                        PlxServiceMeasurePartsMap.put("ExternalId", eElement.getElementsByTagName("ExternalId").item(0).getTextContent());
                        System.out.println("InvoiceCode : " + eElement3.getElementsByTagName("InvoiceCode").item(0).getTextContent());
                        PlxServiceMeasurePartsMap.put("InvoiceCode", eElement.getElementsByTagName("InvoiceCode").item(0).getTextContent());
                        System.out.println("IsCritical : " + eElement3.getElementsByTagName("IsCritical").item(0).getTextContent());
                        PlxServiceMeasurePartsMap.put("IsCritical", eElement.getElementsByTagName("IsCritical").item(0).getTextContent());
                        System.out.println("IsNegative : " + eElement3.getElementsByTagName("IsNegative").item(0).getTextContent());  
                        PlxServiceMeasurePartsMap.put("IsNegative", eElement.getElementsByTagName("IsNegative").item(0).getTextContent());
                        System.out.println("Origin : " + eElement3.getElementsByTagName("Origin").item(0).getTextContent());
                        PlxServiceMeasurePartsMap.put("Origin", eElement.getElementsByTagName("Origin").item(0).getTextContent());
                        System.out.println("PartId : " + eElement3.getElementsByTagName("PartId").item(0).getTextContent());
                        PlxServiceMeasurePartsMap.put("PartId", eElement.getElementsByTagName("PartId").item(0).getTextContent());
                        System.out.println("PositionNumber : " + eElement3.getElementsByTagName("PositionNumber").item(0).getTextContent());
                        PlxServiceMeasurePartsMap.put("PositionNumber", eElement.getElementsByTagName("PositionNumber").item(0).getTextContent());
                        System.out.println("Quantity : " + eElement3.getElementsByTagName("Quantity").item(0).getTextContent());
                        PlxServiceMeasurePartsMap.put("Quantity", eElement.getElementsByTagName("Quantity").item(0).getTextContent());
                        System.out.println("ServiceMeasureId : " + eElement3.getElementsByTagName("ServiceMeasureId").item(0).getTextContent());  
                        PlxServiceMeasurePartsMap.put("ServiceMeasureId", eElement.getElementsByTagName("ServiceMeasureId").item(0).getTextContent());
                    }
                }
                
                
                NodeList ListOfPlxServiceMeasureWorkItemElement = eElement.getElementsByTagName("ListOfPlxServiceMeasureWorkItem");                
                for (int temp2 = 0; temp2 < ListOfPlxServiceMeasureWorkItemElement.getLength(); temp2++) {
                    Node nNode2 = ListOfPlxServiceMeasureWorkItemElement.item(temp2);
                    System.out.println("\nCurrent Element :" + nNode2.getNodeName());
                    Element eElement2 = (Element)nNode2; 
                    NodeList PlxServiceMeasureWorkItemElement = eElement2.getElementsByTagName("PlxServiceMeasureWorkItem");
                    for (int temp3 = 0; temp3 < PlxServiceMeasureWorkItemElement.getLength(); temp3++) {
                        Node nNode3 = PlxServiceMeasureWorkItemElement.item(temp3);
                        System.out.println("Current Element :" + nNode3.getNodeName());
                        Element eElement3 = (Element)nNode3;
                        System.out.println("DMSStatus2 : " + eElement3.getElementsByTagName("DMSStatus2").item(0).getTextContent());
                        PlxServiceMeasureWorkItem.put("DMSStatus", eElement.getElementsByTagName("DMSStatus2").item(0).getTextContent());
                        System.out.println("Description2 : " + eElement3.getElementsByTagName("Description2").item(0).getTextContent());
                        PlxServiceMeasureWorkItem.put("Description", eElement.getElementsByTagName("Description2").item(0).getTextContent());
                        System.out.println("EditType : " + eElement3.getElementsByTagName("EditType").item(0).getTextContent());
                        PlxServiceMeasureWorkItem.put("EditType", eElement.getElementsByTagName("EditType").item(0).getTextContent());
                        System.out.println("ExternalId : " + eElement3.getElementsByTagName("ExternalId").item(0).getTextContent());
                        PlxServiceMeasureWorkItem.put("ExternalId", eElement.getElementsByTagName("ExternalId").item(0).getTextContent());
                        System.out.println("IsNegative : " + eElement3.getElementsByTagName("IsNegative").item(0).getTextContent());
                        PlxServiceMeasureWorkItem.put("IsNegative", eElement.getElementsByTagName("IsNegative").item(0).getTextContent());
                        System.out.println("Number2 : " + eElement3.getElementsByTagName("Number2").item(0).getTextContent());
                        PlxServiceMeasureWorkItem.put("Number", eElement.getElementsByTagName("Number2").item(0).getTextContent());
                        System.out.println("Origin : " + eElement3.getElementsByTagName("Origin").item(0).getTextContent());
                        PlxServiceMeasureWorkItem.put("Origin", eElement.getElementsByTagName("Origin").item(0).getTextContent());
                        System.out.println("PositionNumber : " + eElement3.getElementsByTagName("PositionNumber").item(0).getTextContent());
                        PlxServiceMeasureWorkItem.put("PositionNumber", eElement.getElementsByTagName("PositionNumber").item(0).getTextContent());
                        System.out.println("ServiceMeasureId : " + eElement3.getElementsByTagName("ServiceMeasureId").item(0).getTextContent());
                        PlxServiceMeasureWorkItem.put("ServiceMeasureId", eElement.getElementsByTagName("ServiceMeasureId").item(0).getTextContent());
                        System.out.println("ServicePackageId : " + eElement3.getElementsByTagName("ServicePackageId").item(0).getTextContent());
                        PlxServiceMeasureWorkItem.put("ServicePackageId", eElement.getElementsByTagName("ServicePackageId").item(0).getTextContent());
                        System.out.println("Unit : " + eElement3.getElementsByTagName("Unit").item(0).getTextContent());
                        PlxServiceMeasureWorkItem.put("Unit", eElement.getElementsByTagName("Unit").item(0).getTextContent());
                        System.out.println("Value2 : " + eElement3.getElementsByTagName("Value2").item(0).getTextContent());    
                        PlxServiceMeasureWorkItem.put("Value", eElement.getElementsByTagName("Value2").item(0).getTextContent());
                    }
                }
                
                
                NodeList ListOfPlxServiceMeasurePackageElement = eElement.getElementsByTagName("ListOfPlxServiceMeasurePackage");                
                for (int temp2 = 0; temp2 < ListOfPlxServiceMeasurePackageElement.getLength(); temp2++) {
                    Node nNode2 = ListOfPlxServiceMeasurePackageElement.item(temp2);
                    System.out.println("\nCurrent Element :" + nNode2.getNodeName());
                    Element eElement2 = (Element)nNode2; 
                    NodeList PlxServiceMeasurePackageElement = eElement2.getElementsByTagName("PlxServiceMeasurePackage");
                    for (int temp3 = 0; temp3 < PlxServiceMeasurePackageElement.getLength(); temp3++) {
                        Node nNode3 = PlxServiceMeasurePackageElement.item(temp3);
                        System.out.println("Current Element :" + nNode3.getNodeName());
                        Element eElement3 = (Element)nNode3;
                        System.out.println("Description2 : " + eElement3.getElementsByTagName("Description2").item(0).getTextContent());
                        PlxServiceMeasurePackageMap.put("Description", eElement.getElementsByTagName("Description2").item(0).getTextContent());
                        System.out.println("EditType : " + eElement3.getElementsByTagName("EditType").item(0).getTextContent());
                        PlxServiceMeasurePackageMap.put("EditType", eElement.getElementsByTagName("EditType").item(0).getTextContent());
                        System.out.println("ExternalId : " + eElement3.getElementsByTagName("ExternalId").item(0).getTextContent());
                        PlxServiceMeasurePackageMap.put("ExternalId", eElement.getElementsByTagName("ExternalId").item(0).getTextContent());
                        System.out.println("Origin : " + eElement3.getElementsByTagName("Origin").item(0).getTextContent());
                        PlxServiceMeasurePackageMap.put("Origin", eElement.getElementsByTagName("Origin").item(0).getTextContent());
                        System.out.println("ServiceMeasureId : " + eElement3.getElementsByTagName("ServiceMeasureId").item(0).getTextContent());
                        PlxServiceMeasurePackageMap.put("ServiceMeasureId", eElement.getElementsByTagName("ServiceMeasureId").item(0).getTextContent());
                        System.out.println("Number : " + eElement3.getElementsByTagName("Number").item(0).getTextContent());
                        PlxServiceMeasurePackageMap.put("Number", eElement.getElementsByTagName("Number").item(0).getTextContent());
                        System.out.println("PositionNumber : " + eElement3.getElementsByTagName("PositionNumber").item(0).getTextContent());
                        PlxServiceMeasurePackageMap.put("PositionNumber", eElement.getElementsByTagName("PositionNumber").item(0).getTextContent());
                        System.out.println("Value : " + eElement3.getElementsByTagName("Value").item(0).getTextContent()); 
                        PlxServiceMeasurePackageMap.put("Value", eElement.getElementsByTagName("Value2").item(0).getTextContent());
                        System.out.println("DmsStatus : " + eElement3.getElementsByTagName("DmsStatus").item(0).getTextContent());
                        PlxServiceMeasurePackageMap.put("DmsStatus", eElement.getElementsByTagName("DmsStatus").item(0).getTextContent());
                        System.out.println("IsNegative : " + eElement3.getElementsByTagName("IsNegative").item(0).getTextContent()); 
                        PlxServiceMeasurePackageMap.put("IsNegative", eElement.getElementsByTagName("IsNegative").item(0).getTextContent());
                        System.out.println("Unit : " + eElement3.getElementsByTagName("Unit").item(0).getTextContent()); 
                        PlxServiceMeasurePackageMap.put("Unit", eElement.getElementsByTagName("Unit").item(0).getTextContent());
                        
                        NodeList ListOfPlxServiceMeasurePackagePartsElement = eElement3.getElementsByTagName("ListOfPlxServiceMeasurePackageParts");
                        for (int temp4 = 0; temp4 < ListOfPlxServiceMeasurePackagePartsElement.getLength(); temp4++) {
                            Node nNode4 = ListOfPlxServiceMeasurePackagePartsElement.item(temp4);
                            System.out.println("Current Element :" + nNode4.getNodeName());
                            Element eElement4 = (Element)nNode4;
                            NodeList PlxServiceMeasurePackagePartsElement = eElement4.getElementsByTagName("PlxServiceMeasurePackageParts");
                            for (int temp5 = 0; temp5 < PlxServiceMeasurePackagePartsElement.getLength(); temp5++) {
                                Node nNode5 = PlxServiceMeasurePackagePartsElement.item(temp5);
                                System.out.println("Current Element :" + nNode5.getNodeName());
                                Element eElement5 = (Element)nNode5;
                                System.out.println("CoverageListId : " + eElement5.getElementsByTagName("CoverageListId").item(0).getTextContent());
                                PlxServiceMeasurePackage_PlxServiceMeasurePackagePartsMap.put("CoverageListId", eElement.getElementsByTagName("CoverageListId").item(0).getTextContent());
                                System.out.println("EditType : " + eElement5.getElementsByTagName("EditType").item(0).getTextContent());
                                PlxServiceMeasurePackage_PlxServiceMeasurePackagePartsMap.put("EditType", eElement.getElementsByTagName("EditType").item(0).getTextContent());
                                System.out.println("PartId : " + eElement5.getElementsByTagName("PartId").item(0).getTextContent());
                                PlxServiceMeasurePackage_PlxServiceMeasurePackagePartsMap.put("PartId", eElement.getElementsByTagName("PartId").item(0).getTextContent());
                                System.out.println("Origin : " + eElement5.getElementsByTagName("Origin").item(0).getTextContent());
                                PlxServiceMeasurePackage_PlxServiceMeasurePackagePartsMap.put("Origin", eElement.getElementsByTagName("Origin").item(0).getTextContent());
                                System.out.println("ServiceMeasurePackageId : " + eElement5.getElementsByTagName("ServiceMeasurePackageId").item(0).getTextContent());
                                PlxServiceMeasurePackage_PlxServiceMeasurePackagePartsMap.put("ServiceMeasurePackageId", eElement.getElementsByTagName("ServiceMeasurePackageId").item(0).getTextContent());
                                System.out.println("Quantity : " + eElement5.getElementsByTagName("Quantity").item(0).getTextContent());
                                PlxServiceMeasurePackage_PlxServiceMeasurePackagePartsMap.put("Quantity", eElement.getElementsByTagName("Quantity").item(0).getTextContent());
                            }
                        }
                        
                        NodeList ListOfPlxServiceMeasurePackageWorkItemElement = eElement3.getElementsByTagName("ListOfPlxServiceMeasurePackageWorkItem");
                        for (int temp4 = 0; temp4 < ListOfPlxServiceMeasurePackageWorkItemElement.getLength(); temp4++) {
                            Node nNode6 = ListOfPlxServiceMeasurePackageWorkItemElement.item(temp4);
                            System.out.println("\nCurrent Element :" + nNode6.getNodeName());
                            Element eElement6 = (Element)nNode6;
                            NodeList PlxServiceMeasurePackageWorkItemElement = eElement6.getElementsByTagName("PlxServiceMeasurePackageWorkItem");
                             for (int temp5 = 0; temp5 < PlxServiceMeasurePackageWorkItemElement.getLength(); temp5++) {
                                Node nNode7 = PlxServiceMeasurePackageWorkItemElement.item(temp5);
                                System.out.println("Current Element :" + nNode7.getNodeName());
                                Element eElement7 = (Element)nNode7;
                                System.out.println("Description2 : " + eElement7.getElementsByTagName("Description2").item(0).getTextContent());
                                PlxServiceMeasurePackage_PlxServiceMeasurePackageWorkItemMap.put("Description", eElement.getElementsByTagName("Description2").item(0).getTextContent());
                                System.out.println("EditType : " + eElement7.getElementsByTagName("EditType").item(0).getTextContent());
                                PlxServiceMeasurePackage_PlxServiceMeasurePackageWorkItemMap.put("EditType", eElement.getElementsByTagName("EditType").item(0).getTextContent());
                                System.out.println("Number2 : " + eElement7.getElementsByTagName("Number2").item(0).getTextContent());
                                PlxServiceMeasurePackage_PlxServiceMeasurePackageWorkItemMap.put("Number", eElement.getElementsByTagName("Number2").item(0).getTextContent());
                                System.out.println("Origin : " + eElement7.getElementsByTagName("Origin").item(0).getTextContent());
                                PlxServiceMeasurePackage_PlxServiceMeasurePackageWorkItemMap.put("Origin", eElement.getElementsByTagName("Origin").item(0).getTextContent());
                                System.out.println("ServiceMeasureId : " + eElement7.getElementsByTagName("ServiceMeasureId").item(0).getTextContent());
                                PlxServiceMeasurePackage_PlxServiceMeasurePackageWorkItemMap.put("ServiceMeasureId", eElement.getElementsByTagName("ServiceMeasureId").item(0).getTextContent());
                                System.out.println("ServiceMeasurePackageId : " + eElement7.getElementsByTagName("ServiceMeasurePackageId").item(0).getTextContent());
                                PlxServiceMeasurePackage_PlxServiceMeasurePackageWorkItemMap.put("ServiceMeasurePackageId", eElement.getElementsByTagName("ServiceMeasurePackageId").item(0).getTextContent());
                                System.out.println("Unit : " + eElement7.getElementsByTagName("Unit").item(0).getTextContent()); 
                                PlxServiceMeasurePackage_PlxServiceMeasurePackageWorkItemMap.put("Unit", eElement.getElementsByTagName("Unit").item(0).getTextContent());
                                System.out.println("Value2 : " + eElement7.getElementsByTagName("Value2").item(0).getTextContent());
                                PlxServiceMeasurePackage_PlxServiceMeasurePackageWorkItemMap.put("Value", eElement.getElementsByTagName("Value2").item(0).getTextContent());
                            }
                        }
                    }
                }
                System.out.println("----------------------------");
                System.out.println("****************************");
                
                
            }                        
        }
    }
          
    private Map parseXentryServiceMeasure() throws ParserConfigurationException, SAXException, IOException{
        //String xmldoc = customer_concern_xml;
        //XmlFormatter formatter = new XmlFormatter();
        //System.out.println("xe "+xmldoc);
        //String formatted_xmldoc = formatter.format(xmldoc);
        //System.out.println("xe "+formatted_xmldoc);
        //XentryServiceMeasure xsm = new XentryServiceMeasure(xmldoc);
        //xsm.handleXMLDoc();
        //String re = xsm.getRootElement();
        //System.out.println("RootElement is: "+re);
        //System.out.println("children :"+cust_concern_xml_doc.);
        NodeList nList = service_measure_xml_doc.getElementsByTagName("PlxXentryServiceMeasure");
        System.out.println("----------------------------");
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            System.out.println("\nCurrent Element :" + nNode.getNodeName());
            System.out.println("----------------------------");
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                System.out.println("Description : " + eElement.getElementsByTagName("Description").item(0).getTextContent());
                PlxXentryServiceMeasureMap.put("Description", eElement.getElementsByTagName("Description").item(0).getTextContent());
                System.out.println("Name : " + eElement.getElementsByTagName("Name").item(0).getTextContent());
                PlxXentryServiceMeasureMap.put("Name", eElement.getElementsByTagName("Name").item(0).getTextContent());
                System.out.println("ParentSHId : " + eElement.getElementsByTagName("ParentSHId").item(0).getTextContent());
                PlxXentryServiceMeasureMap.put("ParentSHId", eElement.getElementsByTagName("ParentSHId").item(0).getTextContent());
                System.out.println("Selected : " + eElement.getElementsByTagName("Selected").item(0).getTextContent());
                PlxXentryServiceMeasureMap.put("Selected", eElement.getElementsByTagName("Selected").item(0).getTextContent());
                System.out.println("ServiceMeasureType : " + eElement.getElementsByTagName("ServiceMeasureType").item(0).getTextContent());
                PlxXentryServiceMeasureMap.put("ServiceMeasureType", eElement.getElementsByTagName("ServiceMeasureType").item(0).getTextContent());
                
                System.out.println("----------------------------");
                System.out.println("****************************");                                
            }                        
        }
        return PlxXentryServiceMeasureMap;
    }
    
    private Map parseServiceMeasureNotesAndDefectKey() throws ParserConfigurationException, SAXException, IOException{
        //String xmldoc = customer_concern_xml;
        //XmlFormatter formatter = new XmlFormatter();
        //System.out.println("xe "+xmldoc);
        //String formatted_xmldoc = formatter.format(xmldoc);
        //System.out.println("xe "+formatted_xmldoc);
        //XentryServiceMeasure xsm = new XentryServiceMeasure(xmldoc);
        //xsm.handleXMLDoc();
        //String re = xsm.getRootElement();
        //System.out.println("RootElement is: "+re);
        //System.out.println("children :"+cust_concern_xml_doc.);
        NodeList nList = service_measure_xml_doc.getElementsByTagName("PlxXentryServiceMeasure");
        System.out.println("----------------------------");
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            System.out.println("\nCurrent Element :" + nNode.getNodeName());
            System.out.println("----------------------------");
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;                
                NodeList ListOfPlxServiceMeasureNotesAndDefectKeyElement = eElement.getElementsByTagName("ListOfPlxServiceMeasureNotesAndDefectKey");                
                for (int temp2 = 0; temp2 < ListOfPlxServiceMeasureNotesAndDefectKeyElement.getLength(); temp2++) {
                    Node nNode2 = ListOfPlxServiceMeasureNotesAndDefectKeyElement.item(temp2);
                    System.out.println("\nCurrent Element :" + nNode2.getNodeName());
                    Element eElement2 = (Element)nNode2; 
                    NodeList PlxServiceMeasureNotesAndDefectKeyElement = eElement2.getElementsByTagName("PlxServiceMeasureNotesAndDefectKey");
                    for (int temp3 = 0; temp3 < PlxServiceMeasureNotesAndDefectKeyElement.getLength(); temp3++) {
                        Node nNode3 = PlxServiceMeasureNotesAndDefectKeyElement.item(temp3);
                        System.out.println("Current Element :" + nNode3.getNodeName());
                        Element eElement3 = (Element)nNode3;
                        System.out.println("Area : " + eElement3.getElementsByTagName("Area").item(0).getTextContent());
                        PlxServiceMeasureNotesAndDefectKeyMap.put("Area", eElement.getElementsByTagName("Area").item(0).getTextContent());
                        System.out.println("Cause : " + eElement3.getElementsByTagName("Cause").item(0).getTextContent());
                        PlxServiceMeasureNotesAndDefectKeyMap.put("Cause", eElement.getElementsByTagName("Cause").item(0).getTextContent());
                        System.out.println("Correction : " + eElement3.getElementsByTagName("Correction").item(0).getTextContent());
                        PlxServiceMeasureNotesAndDefectKeyMap.put("Correction", eElement.getElementsByTagName("Correction").item(0).getTextContent());
                        System.out.println("DefectKey : " + eElement3.getElementsByTagName("DefectKey").item(0).getTextContent());
                        PlxServiceMeasureNotesAndDefectKeyMap.put("DefectKey", eElement.getElementsByTagName("DefectKey").item(0).getTextContent());
                        System.out.println("DefectKeyDescriptioin : " + eElement3.getElementsByTagName("DefectKeyDescriptioin").item(0).getTextContent());
                        PlxServiceMeasureNotesAndDefectKeyMap.put("DefectKeyDescriptioin", eElement.getElementsByTagName("DefectKeyDescriptioin").item(0).getTextContent());
                        System.out.println("Note : " + eElement3.getElementsByTagName("Area").item(0).getTextContent());
                        PlxServiceMeasureNotesAndDefectKeyMap.put("Note", eElement.getElementsByTagName("Note").item(0).getTextContent());
                        System.out.println("NoteType : " + eElement3.getElementsByTagName("Cause").item(0).getTextContent());
                        PlxServiceMeasureNotesAndDefectKeyMap.put("NoteType", eElement.getElementsByTagName("NoteType").item(0).getTextContent());
                        System.out.println("Private : " + eElement3.getElementsByTagName("Correction").item(0).getTextContent());
                        PlxServiceMeasureNotesAndDefectKeyMap.put("Private", eElement.getElementsByTagName("Private").item(0).getTextContent());
                        System.out.println("ServiceMeasureId : " + eElement3.getElementsByTagName("DefectKey").item(0).getTextContent());
                        PlxServiceMeasureNotesAndDefectKeyMap.put("ServiceMeasureId", eElement.getElementsByTagName("ServiceMeasureId").item(0).getTextContent());                        
                    }
                }
                                                                                               
                System.out.println("----------------------------");
                System.out.println("****************************");                                
            }                        
        }
        return PlxServiceMeasureNotesAndDefectKeyMap;
    }
    
    private Map parsePlxServiceMeasureParts() throws ParserConfigurationException, SAXException, IOException{        
        NodeList nList = service_measure_xml_doc.getElementsByTagName("PlxXentryServiceMeasure");
        System.out.println("----------------------------");
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            System.out.println("\nCurrent Element :" + nNode.getNodeName());
            System.out.println("----------------------------");
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                                
                NodeList ListOfPlxServiceMeasurePartsKeyElement = eElement.getElementsByTagName("ListOfPlxServiceMeasureParts");                
                for (int temp2 = 0; temp2 < ListOfPlxServiceMeasurePartsKeyElement.getLength(); temp2++) {
                    Node nNode2 = ListOfPlxServiceMeasurePartsKeyElement.item(temp2);
                    System.out.println("\nCurrent Element :" + nNode2.getNodeName());
                    Element eElement2 = (Element)nNode2; 
                    NodeList PlxServiceMeasurePartsElement = eElement2.getElementsByTagName("PlxServiceMeasureParts");
                    for (int temp3 = 0; temp3 < PlxServiceMeasurePartsElement.getLength(); temp3++) {
                        Node nNode3 = PlxServiceMeasurePartsElement.item(temp3);
                        System.out.println("Current Element :" + nNode3.getNodeName());
                        Element eElement3 = (Element)nNode3;
                        System.out.println("AdditionalKey1 : " + eElement3.getElementsByTagName("AdditionalKey1").item(0).getTextContent());
                        PlxServiceMeasurePartsMap.put("AdditionalKey1", eElement.getElementsByTagName("AdditionalKey1").item(0).getTextContent());
                        System.out.println("AdditionalKey2 : " + eElement3.getElementsByTagName("AdditionalKey2").item(0).getTextContent());
                        PlxServiceMeasurePartsMap.put("AdditionalKey2", eElement.getElementsByTagName("AdditionalKey2").item(0).getTextContent());
                        System.out.println("CoverageListId : " + eElement3.getElementsByTagName("CoverageListId").item(0).getTextContent());
                        PlxServiceMeasurePartsMap.put("CoverageListId", eElement.getElementsByTagName("CoverageListId").item(0).getTextContent());
                        System.out.println("DMSStatus2 : " + eElement3.getElementsByTagName("DMSStatus2").item(0).getTextContent());
                        PlxServiceMeasurePartsMap.put("DMSStatus2", eElement.getElementsByTagName("DMSStatus2").item(0).getTextContent());
                        System.out.println("EditType : " + eElement3.getElementsByTagName("EditType").item(0).getTextContent());
                        PlxServiceMeasurePartsMap.put("EditType", eElement.getElementsByTagName("EditType").item(0).getTextContent());
                        System.out.println("ExternalId : " + eElement3.getElementsByTagName("ExternalId").item(0).getTextContent());
                        PlxServiceMeasurePartsMap.put("ExternalId", eElement.getElementsByTagName("ExternalId").item(0).getTextContent());
                        System.out.println("InvoiceCode : " + eElement3.getElementsByTagName("InvoiceCode").item(0).getTextContent());
                        PlxServiceMeasurePartsMap.put("InvoiceCode", eElement.getElementsByTagName("InvoiceCode").item(0).getTextContent());
                        System.out.println("IsCritical : " + eElement3.getElementsByTagName("IsCritical").item(0).getTextContent());
                        PlxServiceMeasurePartsMap.put("IsCritical", eElement.getElementsByTagName("IsCritical").item(0).getTextContent());
                        System.out.println("IsNegative : " + eElement3.getElementsByTagName("IsNegative").item(0).getTextContent());  
                        PlxServiceMeasurePartsMap.put("IsNegative", eElement.getElementsByTagName("IsNegative").item(0).getTextContent());
                        System.out.println("Origin : " + eElement3.getElementsByTagName("Origin").item(0).getTextContent());
                        PlxServiceMeasurePartsMap.put("Origin", eElement.getElementsByTagName("Origin").item(0).getTextContent());
                        System.out.println("PartId : " + eElement3.getElementsByTagName("PartId").item(0).getTextContent());
                        PlxServiceMeasurePartsMap.put("PartId", eElement.getElementsByTagName("PartId").item(0).getTextContent());
                        System.out.println("PositionNumber : " + eElement3.getElementsByTagName("PositionNumber").item(0).getTextContent());
                        PlxServiceMeasurePartsMap.put("PositionNumber", eElement.getElementsByTagName("PositionNumber").item(0).getTextContent());
                        System.out.println("Quantity : " + eElement3.getElementsByTagName("Quantity").item(0).getTextContent());
                        PlxServiceMeasurePartsMap.put("Quantity", eElement.getElementsByTagName("Quantity").item(0).getTextContent());
                        System.out.println("ServiceMeasureId : " + eElement3.getElementsByTagName("ServiceMeasureId").item(0).getTextContent());  
                        PlxServiceMeasurePartsMap.put("ServiceMeasureId", eElement.getElementsByTagName("ServiceMeasureId").item(0).getTextContent());
                    }
                }                                
                System.out.println("----------------------------");
                System.out.println("****************************");
                
                
            }                        
        }
        return PlxServiceMeasurePartsMap;
    }
        
    private Map parsePlxServiceMeasureWorkItem() throws ParserConfigurationException, SAXException, IOException{        
        NodeList nList = service_measure_xml_doc.getElementsByTagName("PlxXentryServiceMeasure");
        System.out.println("----------------------------");
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            System.out.println("\nCurrent Element :" + nNode.getNodeName());
            System.out.println("----------------------------");
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;                                
                NodeList ListOfPlxServiceMeasureWorkItemElement = eElement.getElementsByTagName("ListOfPlxServiceMeasureWorkItem");                
                for (int temp2 = 0; temp2 < ListOfPlxServiceMeasureWorkItemElement.getLength(); temp2++) {
                    Node nNode2 = ListOfPlxServiceMeasureWorkItemElement.item(temp2);
                    System.out.println("\nCurrent Element :" + nNode2.getNodeName());
                    Element eElement2 = (Element)nNode2; 
                    NodeList PlxServiceMeasureWorkItemElement = eElement2.getElementsByTagName("PlxServiceMeasureWorkItem");
                    for (int temp3 = 0; temp3 < PlxServiceMeasureWorkItemElement.getLength(); temp3++) {
                        Node nNode3 = PlxServiceMeasureWorkItemElement.item(temp3);
                        System.out.println("Current Element :" + nNode3.getNodeName());
                        Element eElement3 = (Element)nNode3;
                        System.out.println("DMSStatus2 : " + eElement3.getElementsByTagName("DMSStatus2").item(0).getTextContent());
                        PlxServiceMeasureWorkItem.put("DMSStatus", eElement.getElementsByTagName("DMSStatus2").item(0).getTextContent());
                        System.out.println("Description2 : " + eElement3.getElementsByTagName("Description2").item(0).getTextContent());
                        PlxServiceMeasureWorkItem.put("Description", eElement.getElementsByTagName("Description2").item(0).getTextContent());
                        System.out.println("EditType : " + eElement3.getElementsByTagName("EditType").item(0).getTextContent());
                        PlxServiceMeasureWorkItem.put("EditType", eElement.getElementsByTagName("EditType").item(0).getTextContent());
                        System.out.println("ExternalId : " + eElement3.getElementsByTagName("ExternalId").item(0).getTextContent());
                        PlxServiceMeasureWorkItem.put("ExternalId", eElement.getElementsByTagName("ExternalId").item(0).getTextContent());
                        System.out.println("IsNegative : " + eElement3.getElementsByTagName("IsNegative").item(0).getTextContent());
                        PlxServiceMeasureWorkItem.put("IsNegative", eElement.getElementsByTagName("IsNegative").item(0).getTextContent());
                        System.out.println("Number2 : " + eElement3.getElementsByTagName("Number2").item(0).getTextContent());
                        PlxServiceMeasureWorkItem.put("Number", eElement.getElementsByTagName("Number2").item(0).getTextContent());
                        System.out.println("Origin : " + eElement3.getElementsByTagName("Origin").item(0).getTextContent());
                        PlxServiceMeasureWorkItem.put("Origin", eElement.getElementsByTagName("Origin").item(0).getTextContent());
                        System.out.println("PositionNumber : " + eElement3.getElementsByTagName("PositionNumber").item(0).getTextContent());
                        PlxServiceMeasureWorkItem.put("PositionNumber", eElement.getElementsByTagName("PositionNumber").item(0).getTextContent());
                        System.out.println("ServiceMeasureId : " + eElement3.getElementsByTagName("ServiceMeasureId").item(0).getTextContent());
                        PlxServiceMeasureWorkItem.put("ServiceMeasureId", eElement.getElementsByTagName("ServiceMeasureId").item(0).getTextContent());
                        System.out.println("ServicePackageId : " + eElement3.getElementsByTagName("ServicePackageId").item(0).getTextContent());
                        PlxServiceMeasureWorkItem.put("ServicePackageId", eElement.getElementsByTagName("ServicePackageId").item(0).getTextContent());
                        System.out.println("Unit : " + eElement3.getElementsByTagName("Unit").item(0).getTextContent());
                        PlxServiceMeasureWorkItem.put("Unit", eElement.getElementsByTagName("Unit").item(0).getTextContent());
                        System.out.println("Value2 : " + eElement3.getElementsByTagName("Value2").item(0).getTextContent());    
                        PlxServiceMeasureWorkItem.put("Value", eElement.getElementsByTagName("Value2").item(0).getTextContent());
                    }
                }                
                System.out.println("----------------------------");
                System.out.println("****************************");
                                
            }                        
        }
        
        return PlxServiceMeasureWorkItem;
    }
    
    private Map parseServiceMeasurePackage() throws ParserConfigurationException, SAXException, IOException{
        Map outputMap = new HashMap();
        Map tmpMap  = new HashMap();        
        NodeList nList = service_measure_xml_doc.getElementsByTagName("PlxXentryServiceMeasure");
        System.out.println("----------------------------");
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            System.out.println("\nCurrent Element :" + nNode.getNodeName());
            System.out.println("----------------------------");
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                                
                NodeList ListOfPlxServiceMeasurePackageElement = eElement.getElementsByTagName("ListOfPlxServiceMeasurePackage");                
                for (int temp2 = 0; temp2 < ListOfPlxServiceMeasurePackageElement.getLength(); temp2++) {
                    Node nNode2 = ListOfPlxServiceMeasurePackageElement.item(temp2);
                    System.out.println("\nCurrent Element :" + nNode2.getNodeName());
                    Element eElement2 = (Element)nNode2; 
                    NodeList PlxServiceMeasurePackageElement = eElement2.getElementsByTagName("PlxServiceMeasurePackage");
                    for (int temp3 = 0; temp3 < PlxServiceMeasurePackageElement.getLength(); temp3++) {
                        Node nNode3 = PlxServiceMeasurePackageElement.item(temp3);
                        System.out.println("Current Element :" + nNode3.getNodeName());
                        Element eElement3 = (Element)nNode3;
                        System.out.println("Description2 : " + eElement3.getElementsByTagName("Description2").item(0).getTextContent());
                        outputMap.put("Description", eElement.getElementsByTagName("Description2").item(0).getTextContent());
                        System.out.println("EditType : " + eElement3.getElementsByTagName("EditType").item(0).getTextContent());
                        outputMap.put("EditType", eElement.getElementsByTagName("EditType").item(0).getTextContent());
                        System.out.println("ExternalId : " + eElement3.getElementsByTagName("ExternalId").item(0).getTextContent());
                        outputMap.put("ExternalId", eElement.getElementsByTagName("ExternalId").item(0).getTextContent());
                        System.out.println("Origin : " + eElement3.getElementsByTagName("Origin").item(0).getTextContent());
                        outputMap.put("Origin", eElement.getElementsByTagName("Origin").item(0).getTextContent());
                        System.out.println("ServiceMeasureId : " + eElement3.getElementsByTagName("ServiceMeasureId").item(0).getTextContent());
                        outputMap.put("ServiceMeasureId", eElement.getElementsByTagName("ServiceMeasureId").item(0).getTextContent());
                        System.out.println("Number : " + eElement3.getElementsByTagName("Number").item(0).getTextContent());
                        outputMap.put("Number", eElement.getElementsByTagName("Number").item(0).getTextContent());
                        System.out.println("PositionNumber : " + eElement3.getElementsByTagName("PositionNumber").item(0).getTextContent());
                        outputMap.put("PositionNumber", eElement.getElementsByTagName("PositionNumber").item(0).getTextContent());
                        System.out.println("Value : " + eElement3.getElementsByTagName("Value").item(0).getTextContent()); 
                        outputMap.put("Value", eElement.getElementsByTagName("Value2").item(0).getTextContent());
                        System.out.println("DmsStatus : " + eElement3.getElementsByTagName("DmsStatus").item(0).getTextContent());
                        outputMap.put("DmsStatus", eElement.getElementsByTagName("DmsStatus").item(0).getTextContent());
                        System.out.println("IsNegative : " + eElement3.getElementsByTagName("IsNegative").item(0).getTextContent()); 
                        outputMap.put("IsNegative", eElement.getElementsByTagName("IsNegative").item(0).getTextContent());
                        System.out.println("Unit : " + eElement3.getElementsByTagName("Unit").item(0).getTextContent()); 
                        outputMap.put("Unit", eElement.getElementsByTagName("Unit").item(0).getTextContent());
                        
                        NodeList ListOfPlxServiceMeasurePackagePartsElement = eElement3.getElementsByTagName("ListOfPlxServiceMeasurePackageParts");
                        for (int temp4 = 0; temp4 < ListOfPlxServiceMeasurePackagePartsElement.getLength(); temp4++) {
                            Node nNode4 = ListOfPlxServiceMeasurePackagePartsElement.item(temp4);
                            System.out.println("Current Element :" + nNode4.getNodeName());
                            Element eElement4 = (Element)nNode4;
                            NodeList PlxServiceMeasurePackagePartsElement = eElement4.getElementsByTagName("PlxServiceMeasurePackageParts");
                            for (int temp5 = 0; temp5 < PlxServiceMeasurePackagePartsElement.getLength(); temp5++) {
                                Node nNode5 = PlxServiceMeasurePackagePartsElement.item(temp5);
                                System.out.println("Current Element :" + nNode5.getNodeName());
                                Element eElement5 = (Element)nNode5;
                                System.out.println("CoverageListId : " + eElement5.getElementsByTagName("CoverageListId").item(0).getTextContent());
                                PlxServiceMeasurePackage_PlxServiceMeasurePackagePartsMap.put("CoverageListId", eElement.getElementsByTagName("CoverageListId").item(0).getTextContent());
                                System.out.println("EditType : " + eElement5.getElementsByTagName("EditType").item(0).getTextContent());
                                PlxServiceMeasurePackage_PlxServiceMeasurePackagePartsMap.put("EditType", eElement.getElementsByTagName("EditType").item(0).getTextContent());
                                System.out.println("PartId : " + eElement5.getElementsByTagName("PartId").item(0).getTextContent());
                                PlxServiceMeasurePackage_PlxServiceMeasurePackagePartsMap.put("PartId", eElement.getElementsByTagName("PartId").item(0).getTextContent());
                                System.out.println("Origin : " + eElement5.getElementsByTagName("Origin").item(0).getTextContent());
                                PlxServiceMeasurePackage_PlxServiceMeasurePackagePartsMap.put("Origin", eElement.getElementsByTagName("Origin").item(0).getTextContent());
                                System.out.println("ServiceMeasurePackageId : " + eElement5.getElementsByTagName("ServiceMeasurePackageId").item(0).getTextContent());
                                PlxServiceMeasurePackage_PlxServiceMeasurePackagePartsMap.put("ServiceMeasurePackageId", eElement.getElementsByTagName("ServiceMeasurePackageId").item(0).getTextContent());
                                System.out.println("Quantity : " + eElement5.getElementsByTagName("Quantity").item(0).getTextContent());
                                PlxServiceMeasurePackage_PlxServiceMeasurePackagePartsMap.put("Quantity", eElement.getElementsByTagName("Quantity").item(0).getTextContent());
                            }
                        }
                        
                        NodeList ListOfPlxServiceMeasurePackageWorkItemElement = eElement3.getElementsByTagName("ListOfPlxServiceMeasurePackageWorkItem");
                        for (int temp4 = 0; temp4 < ListOfPlxServiceMeasurePackageWorkItemElement.getLength(); temp4++) {
                            Node nNode6 = ListOfPlxServiceMeasurePackageWorkItemElement.item(temp4);
                            System.out.println("\nCurrent Element :" + nNode6.getNodeName());
                            Element eElement6 = (Element)nNode6;
                            NodeList PlxServiceMeasurePackageWorkItemElement = eElement6.getElementsByTagName("PlxServiceMeasurePackageWorkItem");
                             for (int temp5 = 0; temp5 < PlxServiceMeasurePackageWorkItemElement.getLength(); temp5++) {
                                Node nNode7 = PlxServiceMeasurePackageWorkItemElement.item(temp5);
                                System.out.println("Current Element :" + nNode7.getNodeName());
                                Element eElement7 = (Element)nNode7;
                                System.out.println("Description2 : " + eElement7.getElementsByTagName("Description2").item(0).getTextContent());
                                PlxServiceMeasurePackage_PlxServiceMeasurePackageWorkItemMap.put("Description", eElement.getElementsByTagName("Description2").item(0).getTextContent());
                                System.out.println("EditType : " + eElement7.getElementsByTagName("EditType").item(0).getTextContent());
                                PlxServiceMeasurePackage_PlxServiceMeasurePackageWorkItemMap.put("EditType", eElement.getElementsByTagName("EditType").item(0).getTextContent());
                                System.out.println("Number2 : " + eElement7.getElementsByTagName("Number2").item(0).getTextContent());
                                PlxServiceMeasurePackage_PlxServiceMeasurePackageWorkItemMap.put("Number", eElement.getElementsByTagName("Number2").item(0).getTextContent());
                                System.out.println("Origin : " + eElement7.getElementsByTagName("Origin").item(0).getTextContent());
                                PlxServiceMeasurePackage_PlxServiceMeasurePackageWorkItemMap.put("Origin", eElement.getElementsByTagName("Origin").item(0).getTextContent());
                                System.out.println("ServiceMeasureId : " + eElement7.getElementsByTagName("ServiceMeasureId").item(0).getTextContent());
                                PlxServiceMeasurePackage_PlxServiceMeasurePackageWorkItemMap.put("ServiceMeasureId", eElement.getElementsByTagName("ServiceMeasureId").item(0).getTextContent());
                                System.out.println("ServiceMeasurePackageId : " + eElement7.getElementsByTagName("ServiceMeasurePackageId").item(0).getTextContent());
                                PlxServiceMeasurePackage_PlxServiceMeasurePackageWorkItemMap.put("ServiceMeasurePackageId", eElement.getElementsByTagName("ServiceMeasurePackageId").item(0).getTextContent());
                                System.out.println("Unit : " + eElement7.getElementsByTagName("Unit").item(0).getTextContent()); 
                                PlxServiceMeasurePackage_PlxServiceMeasurePackageWorkItemMap.put("Unit", eElement.getElementsByTagName("Unit").item(0).getTextContent());
                                System.out.println("Value2 : " + eElement7.getElementsByTagName("Value2").item(0).getTextContent());
                                PlxServiceMeasurePackage_PlxServiceMeasurePackageWorkItemMap.put("Value", eElement.getElementsByTagName("Value2").item(0).getTextContent());
                            }
                        }
                        
                        tmpMap.putAll(PlxServiceMeasurePackage_PlxServiceMeasurePackagePartsMap);
                        tmpMap.putAll(PlxServiceMeasurePackage_PlxServiceMeasurePackageWorkItemMap);
                        tmpMap.putAll(outputMap);
                        PlxServiceMeasurePackageMap.put(temp3,tmpMap);
                    }
                }
                System.out.println("----------------------------");
                System.out.println("****************************");
                
                
            }                        
        }
        return PlxServiceMeasurePackageMap;
    }
    
    public Map getXentryServiceMeasure(){
        return PlxXentryServiceMeasureMap;
    }
    
    public Map getServiceMeasureNotesAndDefectKey(){
        return PlxServiceMeasureNotesAndDefectKeyMap;
    }
    
    public Map getPlxServiceMeasureParts(){
        return PlxServiceMeasurePartsMap;
    }
    
    public Map getPlxServiceMeasureWorkItem(){
        return PlxServiceMeasureWorkItem;
    }
    
    public Map getPlxServiceMeasurePackageMap(){
        return PlxServiceMeasurePackageMap;
    }
  
    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        String xmldoc = new TestString().xd;
        //XmlFormatter formatter = new XmlFormatter();
        //System.out.println("xe "+xmldoc);
        //String formatted_xmldoc = formatter.format(xmldoc);
        //System.out.println("xe "+formatted_xmldoc);
        XentryServiceMeasure xsm = new XentryServiceMeasure(xmldoc);
        /*xsm.handleXMLDoc();
        xsm.parseXentryServiceMeasure();
        xsm.parseServiceMeasureNotesAndDefectKey();
        xsm.parsePlxServiceMeasureParts();
        xsm.parsePlxServiceMeasureWorkItem();
        xsm.parseServiceMeasurePackage();*/
        
        
        System.out.println("----------------------------");
        System.out.println("****************************");
                
                
                              
        
    }
    
    
    
}
