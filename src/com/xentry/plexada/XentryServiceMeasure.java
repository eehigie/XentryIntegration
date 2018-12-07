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
    private final Map XentryInitJobServiceMeasureMap;
    private final Map PlxXentryServiceMeasureMap;
    private final Map PlxServiceMeasureNotesAndDefectKeyMap;
    private final Map PlxServiceMeasurePartsMap;
    private final Map PlxServiceMeasureWorkItemMap;
    private final Map PlxServiceMeasurePackageMap;
    private final Map PlxServiceMeasurePackage_PlxServiceMeasurePackagePartsMap;
    private final Map PlxServiceMeasurePackage_PlxServiceMeasurePackageWorkItemMap;
    private static final StringWriter ERRORS = new StringWriter();
    
  
    public XentryServiceMeasure(String xmlDoc ) {
        this.XentryInitJobServiceMeasureMap = new HashMap();
        this.PlxServiceMeasurePackage_PlxServiceMeasurePackageWorkItemMap = new HashMap();
        this.PlxServiceMeasurePackage_PlxServiceMeasurePackagePartsMap = new HashMap();
        this.PlxServiceMeasurePackageMap = new HashMap();
        this.PlxServiceMeasureWorkItemMap = new HashMap();
        this.PlxServiceMeasurePartsMap = new HashMap();
        this.PlxServiceMeasureNotesAndDefectKeyMap = new HashMap();
        this.XentryServiceMeasureMap = new HashMap();
        this.PlxXentryServiceMeasureMap = new HashMap();
        service_measure_xml = xmlDoc;
       try {
           handleXMLDoc();
           parseXentryInitJobServiceMeasure()           ;
           /*parseXentryServiceMeasure();
           parseServiceMeasureNotesAndDefectKey();
           parsePlxServiceMeasureParts();
           parsePlxServiceMeasureWorkItem();
           parseServiceMeasurePackage();*/
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
        //MyLogging.log(Level.INFO,"xe "+xmldoc);
        String formatted_xmldoc = formatter.format(xmldoc);
        //MyLogging.log(Level.INFO,"xe "+formatted_xmldoc);
        XentryServiceMeasure xsm = new XentryServiceMeasure(xmldoc);
        xsm.handleXMLDoc();
        String re = xsm.getRootElement();
        MyLogging.log(Level.INFO,"RootElement is: "+re);
        //MyLogging.log(Level.INFO,"children :"+cust_concern_xml_doc.);
        NodeList nList = service_measure_xml_doc.getElementsByTagName("PlxXentryServiceMeasure");
        MyLogging.log(Level.INFO,"----------------------------");
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            MyLogging.log(Level.INFO,"\nCurrent Element :" + nNode.getNodeName());
            MyLogging.log(Level.INFO,"----------------------------");
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                MyLogging.log(Level.INFO,"Description : " + eElement.getElementsByTagName("Description").item(0).getTextContent());
                PlxXentryServiceMeasureMap.put("Description", eElement.getElementsByTagName("Description").item(0).getTextContent());
                MyLogging.log(Level.INFO,"Name : " + eElement.getElementsByTagName("Name").item(0).getTextContent());
                PlxXentryServiceMeasureMap.put("Name", eElement.getElementsByTagName("Name").item(0).getTextContent());
                MyLogging.log(Level.INFO,"ParentSHId : " + eElement.getElementsByTagName("ParentSHId").item(0).getTextContent());
                PlxXentryServiceMeasureMap.put("ParentSHId", eElement.getElementsByTagName("ParentSHId").item(0).getTextContent());
                MyLogging.log(Level.INFO,"Selected : " + eElement.getElementsByTagName("Selected").item(0).getTextContent());
                PlxXentryServiceMeasureMap.put("Selected", eElement.getElementsByTagName("Selected").item(0).getTextContent());
                MyLogging.log(Level.INFO,"ServiceMeasureType : " + eElement.getElementsByTagName("ServiceMeasureType").item(0).getTextContent());
                PlxXentryServiceMeasureMap.put("ServiceMeasureType", eElement.getElementsByTagName("ServiceMeasureType").item(0).getTextContent());
                //MyLogging.log(Level.INFO,"Description : " + eElement.getElementsByTagName("Description").item(0).getTextContent());
                NodeList ListOfPlxServiceMeasureNotesAndDefectKeyElement = eElement.getElementsByTagName("ListOfPlxServiceMeasureNotesAndDefectKey");                
                for (int temp2 = 0; temp2 < ListOfPlxServiceMeasureNotesAndDefectKeyElement.getLength(); temp2++) {
                    Node nNode2 = ListOfPlxServiceMeasureNotesAndDefectKeyElement.item(temp2);
                    MyLogging.log(Level.INFO,"\nCurrent Element :" + nNode2.getNodeName());
                    Element eElement2 = (Element)nNode2; 
                    NodeList PlxServiceMeasureNotesAndDefectKeyElement = eElement2.getElementsByTagName("PlxServiceMeasureNotesAndDefectKey");
                    for (int temp3 = 0; temp3 < PlxServiceMeasureNotesAndDefectKeyElement.getLength(); temp3++) {
                        Node nNode3 = PlxServiceMeasureNotesAndDefectKeyElement.item(temp3);
                        MyLogging.log(Level.INFO,"Current Element :" + nNode3.getNodeName());
                        Element eElement3 = (Element)nNode3;
                        MyLogging.log(Level.INFO,"Area : " + eElement3.getElementsByTagName("Area").item(0).getTextContent());
                        PlxServiceMeasureNotesAndDefectKeyMap.put("Area", eElement.getElementsByTagName("Area").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"Cause : " + eElement3.getElementsByTagName("Cause").item(0).getTextContent());
                        PlxServiceMeasureNotesAndDefectKeyMap.put("Cause", eElement.getElementsByTagName("Cause").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"Correction : " + eElement3.getElementsByTagName("Correction").item(0).getTextContent());
                        PlxServiceMeasureNotesAndDefectKeyMap.put("Correction", eElement.getElementsByTagName("Correction").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"DefectKey : " + eElement3.getElementsByTagName("DefectKey").item(0).getTextContent());
                        PlxServiceMeasureNotesAndDefectKeyMap.put("DefectKey", eElement.getElementsByTagName("DefectKey").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"DefectKeyDescriptioin : " + eElement3.getElementsByTagName("DefectKeyDescriptioin").item(0).getTextContent());
                        PlxServiceMeasureNotesAndDefectKeyMap.put("DefectKeyDescriptioin", eElement.getElementsByTagName("DefectKeyDescriptioin").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"Note : " + eElement3.getElementsByTagName("Area").item(0).getTextContent());
                        PlxServiceMeasureNotesAndDefectKeyMap.put("Note", eElement.getElementsByTagName("Note").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"NoteType : " + eElement3.getElementsByTagName("Cause").item(0).getTextContent());
                        PlxServiceMeasureNotesAndDefectKeyMap.put("NoteType", eElement.getElementsByTagName("NoteType").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"Private : " + eElement3.getElementsByTagName("Correction").item(0).getTextContent());
                        PlxServiceMeasureNotesAndDefectKeyMap.put("Private", eElement.getElementsByTagName("Private").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"ServiceMeasureId : " + eElement3.getElementsByTagName("DefectKey").item(0).getTextContent());
                        PlxServiceMeasureNotesAndDefectKeyMap.put("ServiceMeasureId", eElement.getElementsByTagName("ServiceMeasureId").item(0).getTextContent());                        
                    }
                }
                
                NodeList ListOfPlxServiceMeasurePartsKeyElement = eElement.getElementsByTagName("ListOfPlxServiceMeasureParts");                
                for (int temp2 = 0; temp2 < ListOfPlxServiceMeasurePartsKeyElement.getLength(); temp2++) {
                    Node nNode2 = ListOfPlxServiceMeasurePartsKeyElement.item(temp2);
                    MyLogging.log(Level.INFO,"\nCurrent Element :" + nNode2.getNodeName());
                    Element eElement2 = (Element)nNode2; 
                    NodeList PlxServiceMeasurePartsElement = eElement2.getElementsByTagName("PlxServiceMeasureParts");
                    for (int temp3 = 0; temp3 < PlxServiceMeasurePartsElement.getLength(); temp3++) {
                        Node nNode3 = PlxServiceMeasurePartsElement.item(temp3);
                        MyLogging.log(Level.INFO,"Current Element :" + nNode3.getNodeName());
                        Element eElement3 = (Element)nNode3;
                        MyLogging.log(Level.INFO,"AdditionalKey1 : " + eElement3.getElementsByTagName("AdditionalKey1").item(0).getTextContent());
                        PlxServiceMeasurePartsMap.put("AdditionalKey1", eElement.getElementsByTagName("AdditionalKey1").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"AdditionalKey2 : " + eElement3.getElementsByTagName("AdditionalKey2").item(0).getTextContent());
                        PlxServiceMeasurePartsMap.put("AdditionalKey2", eElement.getElementsByTagName("AdditionalKey2").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"CoverageListId : " + eElement3.getElementsByTagName("CoverageListId").item(0).getTextContent());
                        PlxServiceMeasurePartsMap.put("CoverageListId", eElement.getElementsByTagName("CoverageListId").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"DMSStatus2 : " + eElement3.getElementsByTagName("DMSStatus2").item(0).getTextContent());
                        PlxServiceMeasurePartsMap.put("DMSStatus2", eElement.getElementsByTagName("DMSStatus2").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"EditType : " + eElement3.getElementsByTagName("EditType").item(0).getTextContent());
                        PlxServiceMeasurePartsMap.put("EditType", eElement.getElementsByTagName("EditType").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"ExternalId : " + eElement3.getElementsByTagName("ExternalId").item(0).getTextContent());
                        PlxServiceMeasurePartsMap.put("ExternalId", eElement.getElementsByTagName("ExternalId").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"InvoiceCode : " + eElement3.getElementsByTagName("InvoiceCode").item(0).getTextContent());
                        PlxServiceMeasurePartsMap.put("InvoiceCode", eElement.getElementsByTagName("InvoiceCode").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"IsCritical : " + eElement3.getElementsByTagName("IsCritical").item(0).getTextContent());
                        PlxServiceMeasurePartsMap.put("IsCritical", eElement.getElementsByTagName("IsCritical").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"IsNegative : " + eElement3.getElementsByTagName("IsNegative").item(0).getTextContent());  
                        PlxServiceMeasurePartsMap.put("IsNegative", eElement.getElementsByTagName("IsNegative").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"Origin : " + eElement3.getElementsByTagName("Origin").item(0).getTextContent());
                        PlxServiceMeasurePartsMap.put("Origin", eElement.getElementsByTagName("Origin").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"PartId : " + eElement3.getElementsByTagName("PartId").item(0).getTextContent());
                        PlxServiceMeasurePartsMap.put("PartId", eElement.getElementsByTagName("PartId").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"PositionNumber : " + eElement3.getElementsByTagName("PositionNumber").item(0).getTextContent());
                        PlxServiceMeasurePartsMap.put("PositionNumber", eElement.getElementsByTagName("PositionNumber").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"Quantity : " + eElement3.getElementsByTagName("Quantity").item(0).getTextContent());
                        PlxServiceMeasurePartsMap.put("Quantity", eElement.getElementsByTagName("Quantity").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"ServiceMeasureId : " + eElement3.getElementsByTagName("ServiceMeasureId").item(0).getTextContent());  
                        PlxServiceMeasurePartsMap.put("ServiceMeasureId", eElement.getElementsByTagName("ServiceMeasureId").item(0).getTextContent());
                    }
                }
                
                
                NodeList ListOfPlxServiceMeasureWorkItemElement = eElement.getElementsByTagName("ListOfPlxServiceMeasureWorkItem");                
                for (int temp2 = 0; temp2 < ListOfPlxServiceMeasureWorkItemElement.getLength(); temp2++) {
                    Node nNode2 = ListOfPlxServiceMeasureWorkItemElement.item(temp2);
                    MyLogging.log(Level.INFO,"\nCurrent Element :" + nNode2.getNodeName());
                    Element eElement2 = (Element)nNode2; 
                    NodeList PlxServiceMeasureWorkItemElement = eElement2.getElementsByTagName("PlxServiceMeasureWorkItem");
                    for (int temp3 = 0; temp3 < PlxServiceMeasureWorkItemElement.getLength(); temp3++) {
                        Node nNode3 = PlxServiceMeasureWorkItemElement.item(temp3);
                        MyLogging.log(Level.INFO,"Current Element :" + nNode3.getNodeName());
                        Element eElement3 = (Element)nNode3;
                        MyLogging.log(Level.INFO,"DMSStatus2 : " + eElement3.getElementsByTagName("DMSStatus2").item(0).getTextContent());
                        PlxServiceMeasureWorkItemMap.put("DMSStatus", eElement.getElementsByTagName("DMSStatus2").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"Description2 : " + eElement3.getElementsByTagName("Description2").item(0).getTextContent());
                        PlxServiceMeasureWorkItemMap.put("Description", eElement.getElementsByTagName("Description2").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"EditType : " + eElement3.getElementsByTagName("EditType").item(0).getTextContent());
                        PlxServiceMeasureWorkItemMap.put("EditType", eElement.getElementsByTagName("EditType").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"ExternalId : " + eElement3.getElementsByTagName("ExternalId").item(0).getTextContent());
                        PlxServiceMeasureWorkItemMap.put("ExternalId", eElement.getElementsByTagName("ExternalId").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"IsNegative : " + eElement3.getElementsByTagName("IsNegative").item(0).getTextContent());
                        PlxServiceMeasureWorkItemMap.put("IsNegative", eElement.getElementsByTagName("IsNegative").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"Number2 : " + eElement3.getElementsByTagName("Number2").item(0).getTextContent());
                        PlxServiceMeasureWorkItemMap.put("Number", eElement.getElementsByTagName("Number2").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"Origin : " + eElement3.getElementsByTagName("Origin").item(0).getTextContent());
                        PlxServiceMeasureWorkItemMap.put("Origin", eElement.getElementsByTagName("Origin").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"PositionNumber : " + eElement3.getElementsByTagName("PositionNumber").item(0).getTextContent());
                        PlxServiceMeasureWorkItemMap.put("PositionNumber", eElement.getElementsByTagName("PositionNumber").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"ServiceMeasureId : " + eElement3.getElementsByTagName("ServiceMeasureId").item(0).getTextContent());
                        PlxServiceMeasureWorkItemMap.put("ServiceMeasureId", eElement.getElementsByTagName("ServiceMeasureId").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"ServicePackageId : " + eElement3.getElementsByTagName("ServicePackageId").item(0).getTextContent());
                        PlxServiceMeasureWorkItemMap.put("ServicePackageId", eElement.getElementsByTagName("ServicePackageId").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"Unit : " + eElement3.getElementsByTagName("Unit").item(0).getTextContent());
                        PlxServiceMeasureWorkItemMap.put("Unit", eElement.getElementsByTagName("Unit").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"Value2 : " + eElement3.getElementsByTagName("Value2").item(0).getTextContent());    
                        PlxServiceMeasureWorkItemMap.put("Value", eElement.getElementsByTagName("Value2").item(0).getTextContent());
                    }
                }
                
                
                NodeList ListOfPlxServiceMeasurePackageElement = eElement.getElementsByTagName("ListOfPlxServiceMeasurePackage");                
                for (int temp2 = 0; temp2 < ListOfPlxServiceMeasurePackageElement.getLength(); temp2++) {
                    Node nNode2 = ListOfPlxServiceMeasurePackageElement.item(temp2);
                    MyLogging.log(Level.INFO,"\nCurrent Element :" + nNode2.getNodeName());
                    Element eElement2 = (Element)nNode2; 
                    NodeList PlxServiceMeasurePackageElement = eElement2.getElementsByTagName("PlxServiceMeasurePackage");
                    for (int temp3 = 0; temp3 < PlxServiceMeasurePackageElement.getLength(); temp3++) {
                        Node nNode3 = PlxServiceMeasurePackageElement.item(temp3);
                        MyLogging.log(Level.INFO,"Current Element :" + nNode3.getNodeName());
                        Element eElement3 = (Element)nNode3;
                        MyLogging.log(Level.INFO,"Description2 : " + eElement3.getElementsByTagName("Description2").item(0).getTextContent());
                        PlxServiceMeasurePackageMap.put("Description", eElement.getElementsByTagName("Description2").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"EditType : " + eElement3.getElementsByTagName("EditType").item(0).getTextContent());
                        PlxServiceMeasurePackageMap.put("EditType", eElement.getElementsByTagName("EditType").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"ExternalId : " + eElement3.getElementsByTagName("ExternalId").item(0).getTextContent());
                        PlxServiceMeasurePackageMap.put("ExternalId", eElement.getElementsByTagName("ExternalId").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"Origin : " + eElement3.getElementsByTagName("Origin").item(0).getTextContent());
                        PlxServiceMeasurePackageMap.put("Origin", eElement.getElementsByTagName("Origin").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"ServiceMeasureId : " + eElement3.getElementsByTagName("ServiceMeasureId").item(0).getTextContent());
                        PlxServiceMeasurePackageMap.put("ServiceMeasureId", eElement.getElementsByTagName("ServiceMeasureId").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"Number : " + eElement3.getElementsByTagName("Number").item(0).getTextContent());
                        PlxServiceMeasurePackageMap.put("Number", eElement.getElementsByTagName("Number").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"PositionNumber : " + eElement3.getElementsByTagName("PositionNumber").item(0).getTextContent());
                        PlxServiceMeasurePackageMap.put("PositionNumber", eElement.getElementsByTagName("PositionNumber").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"Value : " + eElement3.getElementsByTagName("Value").item(0).getTextContent()); 
                        PlxServiceMeasurePackageMap.put("Value", eElement.getElementsByTagName("Value2").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"DmsStatus : " + eElement3.getElementsByTagName("DmsStatus").item(0).getTextContent());
                        PlxServiceMeasurePackageMap.put("DmsStatus", eElement.getElementsByTagName("DmsStatus").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"IsNegative : " + eElement3.getElementsByTagName("IsNegative").item(0).getTextContent()); 
                        PlxServiceMeasurePackageMap.put("IsNegative", eElement.getElementsByTagName("IsNegative").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"Unit : " + eElement3.getElementsByTagName("Unit").item(0).getTextContent()); 
                        PlxServiceMeasurePackageMap.put("Unit", eElement.getElementsByTagName("Unit").item(0).getTextContent());
                        
                        NodeList ListOfPlxServiceMeasurePackagePartsElement = eElement3.getElementsByTagName("ListOfPlxServiceMeasurePackageParts");
                        for (int temp4 = 0; temp4 < ListOfPlxServiceMeasurePackagePartsElement.getLength(); temp4++) {
                            Node nNode4 = ListOfPlxServiceMeasurePackagePartsElement.item(temp4);
                            MyLogging.log(Level.INFO,"Current Element :" + nNode4.getNodeName());
                            Element eElement4 = (Element)nNode4;
                            NodeList PlxServiceMeasurePackagePartsElement = eElement4.getElementsByTagName("PlxServiceMeasurePackageParts");
                            for (int temp5 = 0; temp5 < PlxServiceMeasurePackagePartsElement.getLength(); temp5++) {
                                Node nNode5 = PlxServiceMeasurePackagePartsElement.item(temp5);
                                MyLogging.log(Level.INFO,"Current Element :" + nNode5.getNodeName());
                                Element eElement5 = (Element)nNode5;
                                MyLogging.log(Level.INFO,"CoverageListId : " + eElement5.getElementsByTagName("CoverageListId").item(0).getTextContent());
                                PlxServiceMeasurePackage_PlxServiceMeasurePackagePartsMap.put("CoverageListId", eElement.getElementsByTagName("CoverageListId").item(0).getTextContent());
                                MyLogging.log(Level.INFO,"EditType : " + eElement5.getElementsByTagName("EditType").item(0).getTextContent());
                                PlxServiceMeasurePackage_PlxServiceMeasurePackagePartsMap.put("EditType", eElement.getElementsByTagName("EditType").item(0).getTextContent());
                                MyLogging.log(Level.INFO,"PartId : " + eElement5.getElementsByTagName("PartId").item(0).getTextContent());
                                PlxServiceMeasurePackage_PlxServiceMeasurePackagePartsMap.put("PartId", eElement.getElementsByTagName("PartId").item(0).getTextContent());
                                MyLogging.log(Level.INFO,"Origin : " + eElement5.getElementsByTagName("Origin").item(0).getTextContent());
                                PlxServiceMeasurePackage_PlxServiceMeasurePackagePartsMap.put("Origin", eElement.getElementsByTagName("Origin").item(0).getTextContent());
                                MyLogging.log(Level.INFO,"ServiceMeasurePackageId : " + eElement5.getElementsByTagName("ServiceMeasurePackageId").item(0).getTextContent());
                                PlxServiceMeasurePackage_PlxServiceMeasurePackagePartsMap.put("ServiceMeasurePackageId", eElement.getElementsByTagName("ServiceMeasurePackageId").item(0).getTextContent());
                                MyLogging.log(Level.INFO,"Quantity : " + eElement5.getElementsByTagName("Quantity").item(0).getTextContent());
                                PlxServiceMeasurePackage_PlxServiceMeasurePackagePartsMap.put("Quantity", eElement.getElementsByTagName("Quantity").item(0).getTextContent());
                            }
                        }
                        
                        NodeList ListOfPlxServiceMeasurePackageWorkItemElement = eElement3.getElementsByTagName("ListOfPlxServiceMeasurePackageWorkItem");
                        for (int temp4 = 0; temp4 < ListOfPlxServiceMeasurePackageWorkItemElement.getLength(); temp4++) {
                            Node nNode6 = ListOfPlxServiceMeasurePackageWorkItemElement.item(temp4);
                            MyLogging.log(Level.INFO,"\nCurrent Element :" + nNode6.getNodeName());
                            Element eElement6 = (Element)nNode6;
                            NodeList PlxServiceMeasurePackageWorkItemElement = eElement6.getElementsByTagName("PlxServiceMeasurePackageWorkItem");
                             for (int temp5 = 0; temp5 < PlxServiceMeasurePackageWorkItemElement.getLength(); temp5++) {
                                Node nNode7 = PlxServiceMeasurePackageWorkItemElement.item(temp5);
                                MyLogging.log(Level.INFO,"Current Element :" + nNode7.getNodeName());
                                Element eElement7 = (Element)nNode7;
                                MyLogging.log(Level.INFO,"Description2 : " + eElement7.getElementsByTagName("Description2").item(0).getTextContent());
                                PlxServiceMeasurePackage_PlxServiceMeasurePackageWorkItemMap.put("Description", eElement.getElementsByTagName("Description2").item(0).getTextContent());
                                MyLogging.log(Level.INFO,"EditType : " + eElement7.getElementsByTagName("EditType").item(0).getTextContent());
                                PlxServiceMeasurePackage_PlxServiceMeasurePackageWorkItemMap.put("EditType", eElement.getElementsByTagName("EditType").item(0).getTextContent());
                                MyLogging.log(Level.INFO,"Number2 : " + eElement7.getElementsByTagName("Number2").item(0).getTextContent());
                                PlxServiceMeasurePackage_PlxServiceMeasurePackageWorkItemMap.put("Number", eElement.getElementsByTagName("Number2").item(0).getTextContent());
                                MyLogging.log(Level.INFO,"Origin : " + eElement7.getElementsByTagName("Origin").item(0).getTextContent());
                                PlxServiceMeasurePackage_PlxServiceMeasurePackageWorkItemMap.put("Origin", eElement.getElementsByTagName("Origin").item(0).getTextContent());
                                MyLogging.log(Level.INFO,"ServiceMeasureId : " + eElement7.getElementsByTagName("ServiceMeasureId").item(0).getTextContent());
                                PlxServiceMeasurePackage_PlxServiceMeasurePackageWorkItemMap.put("ServiceMeasureId", eElement.getElementsByTagName("ServiceMeasureId").item(0).getTextContent());
                                MyLogging.log(Level.INFO,"ServiceMeasurePackageId : " + eElement7.getElementsByTagName("ServiceMeasurePackageId").item(0).getTextContent());
                                PlxServiceMeasurePackage_PlxServiceMeasurePackageWorkItemMap.put("ServiceMeasurePackageId", eElement.getElementsByTagName("ServiceMeasurePackageId").item(0).getTextContent());
                                MyLogging.log(Level.INFO,"Unit : " + eElement7.getElementsByTagName("Unit").item(0).getTextContent()); 
                                PlxServiceMeasurePackage_PlxServiceMeasurePackageWorkItemMap.put("Unit", eElement.getElementsByTagName("Unit").item(0).getTextContent());
                                MyLogging.log(Level.INFO,"Value2 : " + eElement7.getElementsByTagName("Value2").item(0).getTextContent());
                                PlxServiceMeasurePackage_PlxServiceMeasurePackageWorkItemMap.put("Value", eElement.getElementsByTagName("Value2").item(0).getTextContent());
                            }
                        }
                    }
                }
                MyLogging.log(Level.INFO,"----------------------------");
                MyLogging.log(Level.INFO,"****************************");
                
                
            }                        
        }
    }
    
    private Map parseXentryInitJobServiceMeasure() throws ParserConfigurationException, SAXException, IOException{
        NodeList nList = service_measure_xml_doc.getElementsByTagName("PlxXentryServiceMeasure");
        MyLogging.log(Level.INFO,"----------------------------");
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Map tmpMap = new HashMap();
            Map tmpMap_2 = new HashMap();
            Map tmpMap_3 = new HashMap();
            Map outputMap = new HashMap();
            Map outMap = new HashMap();
            Node nNode = nList.item(temp);
            MyLogging.log(Level.INFO,"\nCurrent Element :" + nNode.getNodeName());
            MyLogging.log(Level.INFO,"----------------------------");
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                MyLogging.log(Level.INFO,"Description : " + eElement.getElementsByTagName("Description").item(0).getTextContent());
                tmpMap.put("Description", eElement.getElementsByTagName("Description").item(0).getTextContent());                
                MyLogging.log(Level.INFO,"Selected : " + eElement.getElementsByTagName("Selected").item(0).getTextContent());
                tmpMap.put("Selected", eElement.getElementsByTagName("Selected").item(0).getTextContent());
                MyLogging.log(Level.INFO,"Type : " + eElement.getElementsByTagName("ServiceMeasureType").item(0).getTextContent());
                tmpMap.put("Type", eElement.getElementsByTagName("ServiceMeasureType").item(0).getTextContent());
                
                outputMap.put("XentryServiceMeasureMap", tmpMap);
                MyLogging.log(Level.INFO,"----------------------------");
                MyLogging.log(Level.INFO,"****************************");    
                
                NodeList PlxServiceMeasureNotesAndDefectKeyElement = eElement.getElementsByTagName("PlxServiceMeasureNotesAndDefectKey");
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
                }
                outputMap.put("PlxServiceMeasureNotesMap", tmpMap_2);
                outputMap.put("PlxServiceMeasureDefectKeyMap", tmpMap_3);
                
                Map tmpMap_5 = new HashMap();
                NodeList PlxServiceMeasurePartsElement = eElement.getElementsByTagName("PlxServiceMeasureParts");
                for (int temp3 = 0; temp3 < PlxServiceMeasurePartsElement.getLength(); temp3++) {
                    Map tmpMap_4 = new HashMap();
                    Node nNode3 = PlxServiceMeasurePartsElement.item(temp3);
                    MyLogging.log(Level.INFO,"Current Element :" + nNode3.getNodeName());
                    Element eElement3 = (Element)nNode3;
                    MyLogging.log(Level.INFO,"AdditionalKey1 : " + eElement3.getElementsByTagName("AdditionalKey1").item(0).getTextContent());
                    tmpMap_4.put("AdditionalKey1", eElement3.getElementsByTagName("AdditionalKey1").item(0).getTextContent());
                    MyLogging.log(Level.INFO,"AdditionalKey2 : " + eElement3.getElementsByTagName("AdditionalKey2").item(0).getTextContent());
                    tmpMap_4.put("AdditionalKey2", eElement3.getElementsByTagName("AdditionalKey2").item(0).getTextContent());
                    MyLogging.log(Level.INFO,"DMSStatus : " + eElement3.getElementsByTagName("DMSStatus2").item(0).getTextContent());
                    tmpMap_4.put("DMSStatus", eElement3.getElementsByTagName("DMSStatus2").item(0).getTextContent());
                    MyLogging.log(Level.INFO,"EditType : " + eElement3.getElementsByTagName("EditType").item(0).getTextContent());
                    tmpMap_4.put("EditType", eElement3.getElementsByTagName("EditType").item(0).getTextContent());
                    MyLogging.log(Level.INFO,"ExternalId : " + eElement3.getElementsByTagName("ExternalId").item(0).getTextContent());
                    tmpMap_4.put("ExternalId", eElement3.getElementsByTagName("ExternalId").item(0).getTextContent());
                    MyLogging.log(Level.INFO,"InvoiceCode : " + eElement3.getElementsByTagName("InvoiceCode").item(0).getTextContent());
                    tmpMap_4.put("InvoiceCode", eElement3.getElementsByTagName("InvoiceCode").item(0).getTextContent());
                    MyLogging.log(Level.INFO,"IsCritical : " + eElement3.getElementsByTagName("IsCritical").item(0).getTextContent());
                    tmpMap_4.put("IsCritical", eElement3.getElementsByTagName("IsCritical").item(0).getTextContent());
                    MyLogging.log(Level.INFO,"IsNegative : " + eElement3.getElementsByTagName("IsNegative").item(0).getTextContent());  
                    tmpMap_4.put("IsNegative", eElement3.getElementsByTagName("IsNegative").item(0).getTextContent());
                    MyLogging.log(Level.INFO,"Origin : " + eElement3.getElementsByTagName("Origin").item(0).getTextContent());
                    tmpMap_4.put("Origin", eElement3.getElementsByTagName("Origin").item(0).getTextContent());
                    
                    MyLogging.log(Level.INFO,"PosNo : " + eElement3.getElementsByTagName("PositionNumber").item(0).getTextContent());
                    tmpMap_4.put("PosNo", eElement3.getElementsByTagName("PositionNumber").item(0).getTextContent());
                    MyLogging.log(Level.INFO,"Quantity : " + eElement3.getElementsByTagName("Quantity").item(0).getTextContent());
                    tmpMap_4.put("Quantity", eElement3.getElementsByTagName("Quantity").item(0).getTextContent());
                    
                    /*MyLogging.log(Level.INFO,"Name : " + eElement3.getElementsByTagName("PartName").item(0).getTextContent());
                    tmpMap_4.put("Name", eElement3.getElementsByTagName("PartName").item(0).getTextContent());
                    MyLogging.log(Level.INFO,"PartNumber : " + eElement3.getElementsByTagName("PartNumber").item(0).getTextContent());
                    tmpMap_4.put("PartNumber", eElement3.getElementsByTagName("PartNumber").item(0).getTextContent());*/
                    tmpMap_5.put(temp3, tmpMap_4);
                }
                outputMap.put("PlxServiceMeasurePartsMap", tmpMap_5);
                
                Map tmpMap_6 = new HashMap();
                Map tmpMap_7 = new HashMap();
                NodeList PlxServiceMeasureWorkItemElement = eElement.getElementsByTagName("PlxServiceMeasureWorkItem");
                for (int temp3 = 0; temp3 < PlxServiceMeasureWorkItemElement.getLength(); temp3++) {
                    Node nNode3 = PlxServiceMeasureWorkItemElement.item(temp3);
                    MyLogging.log(Level.INFO,"Current Element :" + nNode3.getNodeName());
                    Element eElement4 = (Element)nNode3;
                        
                    MyLogging.log(Level.INFO,"DMSStatus : " + eElement4.getElementsByTagName("DMSStatus2").item(0).getTextContent());
                    tmpMap_6.put("DMSStatus", eElement4.getElementsByTagName("DMSStatus2").item(0).getTextContent());
                    MyLogging.log(Level.INFO,"Description : " + eElement4.getElementsByTagName("Description2").item(0).getTextContent());
                    tmpMap_6.put("Description", eElement4.getElementsByTagName("Description2").item(0).getTextContent());
                    MyLogging.log(Level.INFO,"EditType : " + eElement4.getElementsByTagName("EditType").item(0).getTextContent());
                    tmpMap_6.put("EditType", eElement4.getElementsByTagName("EditType").item(0).getTextContent());
                    MyLogging.log(Level.INFO,"ExternalId : " + eElement4.getElementsByTagName("ExternalId").item(0).getTextContent());
                    tmpMap_6.put("ExternalId", eElement4.getElementsByTagName("ExternalId").item(0).getTextContent());
                    MyLogging.log(Level.INFO,"IsNegative : " + eElement4.getElementsByTagName("IsNegative").item(0).getTextContent());
                    tmpMap_6.put("IsNegative", eElement4.getElementsByTagName("IsNegative").item(0).getTextContent());
                    MyLogging.log(Level.INFO,"Number : " + eElement4.getElementsByTagName("Number2").item(0).getTextContent());
                    tmpMap_6.put("Number", eElement4.getElementsByTagName("Number2").item(0).getTextContent());
                    MyLogging.log(Level.INFO,"Origin : " + eElement4.getElementsByTagName("Origin").item(0).getTextContent());
                    tmpMap_6.put("Origin", eElement4.getElementsByTagName("Origin").item(0).getTextContent());
                    MyLogging.log(Level.INFO,"PositionNumber : " + eElement4.getElementsByTagName("PositionNumber").item(0).getTextContent());
                    tmpMap_6.put("PositionNumber", eElement4.getElementsByTagName("PositionNumber").item(0).getTextContent());                       
                    MyLogging.log(Level.INFO,"Unit : " + eElement4.getElementsByTagName("Unit").item(0).getTextContent());
                    tmpMap_6.put("Unit", eElement4.getElementsByTagName("Unit").item(0).getTextContent());
                    MyLogging.log(Level.INFO,"Value : " + eElement4.getElementsByTagName("Value2").item(0).getTextContent());    
                    tmpMap_6.put("Value", eElement4.getElementsByTagName("Value2").item(0).getTextContent());
                    tmpMap_7.put(temp3, tmpMap_6);
                }                    
                outputMap.put("PlxServiceMeasureWorkItemMap", tmpMap_7);
                
                Map tmpMap_8 = new HashMap();
                NodeList PlxServiceMeasurePackageElement = eElement.getElementsByTagName("PlxServiceMeasurePackage");
                for (int temp3 = 0; temp3 < PlxServiceMeasurePackageElement.getLength(); temp3++) {
                    tmpMap_8 = new HashMap();
                    Map tmpMap_9 = new HashMap();
                    Map tmpMap_10 = new HashMap();
                    Map tmpMap_11 = new HashMap();                    
                    Node nNode3 = PlxServiceMeasurePackageElement.item(temp3);
                    MyLogging.log(Level.INFO,"Current Element :" + nNode3.getNodeName());
                    Element eElement3 = (Element)nNode3;
                    MyLogging.log(Level.INFO,"Description2 : " + eElement3.getElementsByTagName("Description2").item(0).getTextContent());
                    tmpMap_9.put("Description", eElement3.getElementsByTagName("Description2").item(0).getTextContent());
                    MyLogging.log(Level.INFO,"EditType : " + eElement3.getElementsByTagName("EditType").item(0).getTextContent());
                    tmpMap_9.put("EditType", eElement3.getElementsByTagName("EditType").item(0).getTextContent());
                    MyLogging.log(Level.INFO,"ExternalId : " + eElement3.getElementsByTagName("ExternalId").item(0).getTextContent());
                    tmpMap_9.put("ExternalId", eElement3.getElementsByTagName("ExternalId").item(0).getTextContent());
                    MyLogging.log(Level.INFO,"Origin : " + eElement3.getElementsByTagName("Origin").item(0).getTextContent());
                    tmpMap_9.put("Origin", eElement3.getElementsByTagName("Origin").item(0).getTextContent());
                    
                    MyLogging.log(Level.INFO,"Number : " + eElement3.getElementsByTagName("Number").item(0).getTextContent());
                    tmpMap_9.put("Number", eElement3.getElementsByTagName("Number").item(0).getTextContent());
                    MyLogging.log(Level.INFO,"PositionNumber : " + eElement3.getElementsByTagName("PositionNumber").item(0).getTextContent());
                    tmpMap_9.put("PositionNumber", eElement3.getElementsByTagName("PositionNumber").item(0).getTextContent());
                    MyLogging.log(Level.INFO,"Value : " + eElement3.getElementsByTagName("Value").item(0).getTextContent()); 
                    tmpMap_9.put("Value", eElement3.getElementsByTagName("Value2").item(0).getTextContent());
                    MyLogging.log(Level.INFO,"DmsStatus : " + eElement3.getElementsByTagName("DmsStatus").item(0).getTextContent());
                    tmpMap_9.put("DmsStatus", eElement3.getElementsByTagName("DmsStatus").item(0).getTextContent());
                    MyLogging.log(Level.INFO,"IsNegative : " + eElement3.getElementsByTagName("IsNegative").item(0).getTextContent()); 
                    tmpMap_9.put("IsNegative", eElement3.getElementsByTagName("IsNegative").item(0).getTextContent());
                    MyLogging.log(Level.INFO,"Unit : " + eElement3.getElementsByTagName("Unit").item(0).getTextContent()); 
                    tmpMap_9.put("Unit", eElement3.getElementsByTagName("Unit").item(0).getTextContent());
                    
                    outMap.put("ServiceMeasurePackage", tmpMap_9);
                    
                    
                    NodeList PlxServiceMeasurePackagePartsElement = eElement3.getElementsByTagName("PlxServiceMeasurePackageParts");
                    for (int temp5 = 0; temp5 < PlxServiceMeasurePackagePartsElement.getLength(); temp5++) {
                        Node nNode5 = PlxServiceMeasurePackagePartsElement.item(temp5);
                        MyLogging.log(Level.INFO,"Current Element :" + nNode5.getNodeName());
                        Element eElement5 = (Element)nNode5;                       
                        MyLogging.log(Level.INFO,"EditType : " + eElement5.getElementsByTagName("EditType").item(0).getTextContent());
                        tmpMap_10.put("EditType", eElement5.getElementsByTagName("EditType").item(0).getTextContent());
                        //MyLogging.log(Level.INFO,"PartId : " + eElement5.getElementsByTagName("PartId").item(0).getTextContent());
                        //tmpMap_10.put("PartId", eElement5.getElementsByTagName("PartId").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"Origin : " + eElement5.getElementsByTagName("Origin").item(0).getTextContent());
                        tmpMap_10.put("Origin", eElement5.getElementsByTagName("Origin").item(0).getTextContent());
                        //MyLogging.log(Level.INFO,"ServiceMeasurePackageId : " + eElement5.getElementsByTagName("ServiceMeasurePackageId").item(0).getTextContent());
                        //tmpMap_10.put("ServiceMeasurePackageId", eElement5.getElementsByTagName("ServiceMeasurePackageId").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"Quantity : " + eElement5.getElementsByTagName("Quantity").item(0).getTextContent());
                        tmpMap_10.put("Quantity", eElement5.getElementsByTagName("Quantity").item(0).getTextContent());
                        tmpMap_11.put(temp5, tmpMap_10);
                    }
                    
                    outMap.put("ServiceMeasurePackageParts", tmpMap_11);
                        
                    Map tmpMap_12 = new HashMap();
                    Map tmpMap_13 = new HashMap();
                    NodeList PlxServiceMeasurePackageWorkItemElement = eElement3.getElementsByTagName("PlxServiceMeasurePackageWorkItem");
                    for (int temp5 = 0; temp5 < PlxServiceMeasurePackageWorkItemElement.getLength(); temp5++) {
                        Node nNode7 = PlxServiceMeasurePackageWorkItemElement.item(temp5);
                        MyLogging.log(Level.INFO,"Current Element :" + nNode7.getNodeName());
                        Element eElement7 = (Element)nNode7;
                        MyLogging.log(Level.INFO,"Description2 : " + eElement7.getElementsByTagName("Description2").item(0).getTextContent());
                        tmpMap_12.put("Description", eElement7.getElementsByTagName("Description2").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"EditType : " + eElement7.getElementsByTagName("EditType").item(0).getTextContent());
                        tmpMap_12.put("EditType", eElement7.getElementsByTagName("EditType").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"Number2 : " + eElement7.getElementsByTagName("Number2").item(0).getTextContent());
                        tmpMap_12.put("Number", eElement7.getElementsByTagName("Number2").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"Origin : " + eElement7.getElementsByTagName("Origin").item(0).getTextContent());
                        tmpMap_12.put("Origin", eElement7.getElementsByTagName("Origin").item(0).getTextContent());                                                
                        MyLogging.log(Level.INFO,"Unit : " + eElement7.getElementsByTagName("Unit").item(0).getTextContent()); 
                        tmpMap_12.put("Unit", eElement7.getElementsByTagName("Unit").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"Value2 : " + eElement7.getElementsByTagName("Value2").item(0).getTextContent());
                        tmpMap_12.put("Value", eElement7.getElementsByTagName("Value2").item(0).getTextContent());
                        tmpMap_13.put(temp5, tmpMap_12);
                    }
                    outMap.put("ServiceMeasurePackageWorkItem", tmpMap_13); 
                    tmpMap_8.put(temp3, outMap);
                }
                outputMap.put("PlxServiceMeasurePackageMap", tmpMap_8);
                
                
            } 
            XentryInitJobServiceMeasureMap.put(temp, outputMap);
        }
        return XentryInitJobServiceMeasureMap;       
    }
          
    private Map parseXentryServiceMeasure() throws ParserConfigurationException, SAXException, IOException{
        //String xmldoc = customer_concern_xml;
        //XmlFormatter formatter = new XmlFormatter();
        //MyLogging.log(Level.INFO,"xe "+xmldoc);
        //String formatted_xmldoc = formatter.format(xmldoc);
        //MyLogging.log(Level.INFO,"xe "+formatted_xmldoc);
        //XentryServiceMeasure xsm = new XentryServiceMeasure(xmldoc);
        //xsm.handleXMLDoc();
        //String re = xsm.getRootElement();
        //MyLogging.log(Level.INFO,"RootElement is: "+re);
        //MyLogging.log(Level.INFO,"children :"+cust_concern_xml_doc.);
        NodeList nList = service_measure_xml_doc.getElementsByTagName("PlxXentryServiceMeasure");
        MyLogging.log(Level.INFO,"----------------------------");
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            MyLogging.log(Level.INFO,"\nCurrent Element :" + nNode.getNodeName());
            MyLogging.log(Level.INFO,"----------------------------");
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                MyLogging.log(Level.INFO,"Description : " + eElement.getElementsByTagName("Description").item(0).getTextContent());
                PlxXentryServiceMeasureMap.put("Description", eElement.getElementsByTagName("Description").item(0).getTextContent());
                MyLogging.log(Level.INFO,"Name : " + eElement.getElementsByTagName("Name").item(0).getTextContent());
                PlxXentryServiceMeasureMap.put("Name", eElement.getElementsByTagName("Name").item(0).getTextContent());
                MyLogging.log(Level.INFO,"ParentSHId : " + eElement.getElementsByTagName("ParentSHId").item(0).getTextContent());
                PlxXentryServiceMeasureMap.put("ParentSHId", eElement.getElementsByTagName("ParentSHId").item(0).getTextContent());
                MyLogging.log(Level.INFO,"Selected : " + eElement.getElementsByTagName("Selected").item(0).getTextContent());
                PlxXentryServiceMeasureMap.put("Selected", eElement.getElementsByTagName("Selected").item(0).getTextContent());
                MyLogging.log(Level.INFO,"ServiceMeasureType : " + eElement.getElementsByTagName("ServiceMeasureType").item(0).getTextContent());
                PlxXentryServiceMeasureMap.put("ServiceMeasureType", eElement.getElementsByTagName("ServiceMeasureType").item(0).getTextContent());
                
                MyLogging.log(Level.INFO,"----------------------------");
                MyLogging.log(Level.INFO,"****************************");                                
            }                        
        }
        return PlxXentryServiceMeasureMap;
    }
    
    private Map parseServiceMeasureNotesAndDefectKey() throws ParserConfigurationException, SAXException, IOException{
        //String xmldoc = customer_concern_xml;
        //XmlFormatter formatter = new XmlFormatter();
        //MyLogging.log(Level.INFO,"xe "+xmldoc);
        //String formatted_xmldoc = formatter.format(xmldoc);
        //MyLogging.log(Level.INFO,"xe "+formatted_xmldoc);
        //XentryServiceMeasure xsm = new XentryServiceMeasure(xmldoc);
        //xsm.handleXMLDoc();
        //String re = xsm.getRootElement();
        //MyLogging.log(Level.INFO,"RootElement is: "+re);
        //MyLogging.log(Level.INFO,"children :"+cust_concern_xml_doc.);
        NodeList nList = service_measure_xml_doc.getElementsByTagName("PlxXentryServiceMeasure");
        MyLogging.log(Level.INFO,"----------------------------");
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            MyLogging.log(Level.INFO,"\nCurrent Element :" + nNode.getNodeName());
            MyLogging.log(Level.INFO,"----------------------------");
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;                
                NodeList ListOfPlxServiceMeasureNotesAndDefectKeyElement = eElement.getElementsByTagName("ListOfPlxServiceMeasureNotesAndDefectKey");                
                for (int temp2 = 0; temp2 < ListOfPlxServiceMeasureNotesAndDefectKeyElement.getLength(); temp2++) {
                    Node nNode2 = ListOfPlxServiceMeasureNotesAndDefectKeyElement.item(temp2);
                    MyLogging.log(Level.INFO,"\nCurrent Element :" + nNode2.getNodeName());
                    Element eElement2 = (Element)nNode2; 
                    NodeList PlxServiceMeasureNotesAndDefectKeyElement = eElement2.getElementsByTagName("PlxServiceMeasureNotesAndDefectKey");
                    for (int temp3 = 0; temp3 < PlxServiceMeasureNotesAndDefectKeyElement.getLength(); temp3++) {
                        Node nNode3 = PlxServiceMeasureNotesAndDefectKeyElement.item(temp3);
                        MyLogging.log(Level.INFO,"Current Element :" + nNode3.getNodeName());
                        Element eElement3 = (Element)nNode3;
                        MyLogging.log(Level.INFO,"Area : " + eElement3.getElementsByTagName("Area").item(0).getTextContent());
                        PlxServiceMeasureNotesAndDefectKeyMap.put("Area", eElement3.getElementsByTagName("Area").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"Cause : " + eElement3.getElementsByTagName("Cause").item(0).getTextContent());
                        PlxServiceMeasureNotesAndDefectKeyMap.put("Cause", eElement3.getElementsByTagName("Cause").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"Correction : " + eElement3.getElementsByTagName("Correction").item(0).getTextContent());
                        PlxServiceMeasureNotesAndDefectKeyMap.put("Correction", eElement3.getElementsByTagName("Correction").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"DefectKey : " + eElement3.getElementsByTagName("DefectKey").item(0).getTextContent());
                        PlxServiceMeasureNotesAndDefectKeyMap.put("DefectKey", eElement3.getElementsByTagName("DefectKey").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"DefectKeyDescriptioin : " + eElement3.getElementsByTagName("DefectKeyDescriptioin").item(0).getTextContent());
                        PlxServiceMeasureNotesAndDefectKeyMap.put("DefectKeyDescriptioin", eElement3.getElementsByTagName("DefectKeyDescriptioin").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"Note : " + eElement3.getElementsByTagName("Area").item(0).getTextContent());
                        PlxServiceMeasureNotesAndDefectKeyMap.put("Note", eElement3.getElementsByTagName("Note").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"NoteType : " + eElement3.getElementsByTagName("Cause").item(0).getTextContent());
                        PlxServiceMeasureNotesAndDefectKeyMap.put("NoteType", eElement3.getElementsByTagName("NoteType").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"Private : " + eElement3.getElementsByTagName("Correction").item(0).getTextContent());
                        PlxServiceMeasureNotesAndDefectKeyMap.put("Private", eElement3.getElementsByTagName("Private").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"ServiceMeasureId : " + eElement3.getElementsByTagName("DefectKey").item(0).getTextContent());
                        PlxServiceMeasureNotesAndDefectKeyMap.put("ServiceMeasureId", eElement3.getElementsByTagName("ServiceMeasureId").item(0).getTextContent());                        
                    }
                }
                                                                                               
                MyLogging.log(Level.INFO,"----------------------------");
                MyLogging.log(Level.INFO,"****************************");                                
            }                        
        }
        return PlxServiceMeasureNotesAndDefectKeyMap;
    }
    
    private Map parsePlxServiceMeasureParts() throws ParserConfigurationException, SAXException, IOException{        
        NodeList nList = service_measure_xml_doc.getElementsByTagName("PlxXentryServiceMeasure");
        MyLogging.log(Level.INFO,"----------------------------");
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            MyLogging.log(Level.INFO,"\nCurrent Element :" + nNode.getNodeName());
            MyLogging.log(Level.INFO,"----------------------------");
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                                
                NodeList ListOfPlxServiceMeasurePartsKeyElement = eElement.getElementsByTagName("ListOfPlxServiceMeasureParts");                
                for (int temp2 = 0; temp2 < ListOfPlxServiceMeasurePartsKeyElement.getLength(); temp2++) {
                    Node nNode2 = ListOfPlxServiceMeasurePartsKeyElement.item(temp2);
                    MyLogging.log(Level.INFO,"\nCurrent Element :" + nNode2.getNodeName());
                    Element eElement2 = (Element)nNode2; 
                    NodeList PlxServiceMeasurePartsElement = eElement2.getElementsByTagName("PlxServiceMeasureParts");
                    for (int temp3 = 0; temp3 < PlxServiceMeasurePartsElement.getLength(); temp3++) {
                        Node nNode3 = PlxServiceMeasurePartsElement.item(temp3);
                        MyLogging.log(Level.INFO,"Current Element :" + nNode3.getNodeName());
                        Element eElement3 = (Element)nNode3;
                        MyLogging.log(Level.INFO,"AdditionalKey1 : " + eElement3.getElementsByTagName("AdditionalKey1").item(0).getTextContent());
                        PlxServiceMeasurePartsMap.put("AdditionalKey1", eElement3.getElementsByTagName("AdditionalKey1").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"AdditionalKey2 : " + eElement3.getElementsByTagName("AdditionalKey2").item(0).getTextContent());
                        PlxServiceMeasurePartsMap.put("AdditionalKey2", eElement3.getElementsByTagName("AdditionalKey2").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"CoverageListId : " + eElement3.getElementsByTagName("CoverageListId").item(0).getTextContent());
                        PlxServiceMeasurePartsMap.put("CoverageListId", eElement3.getElementsByTagName("CoverageListId").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"DMSStatus2 : " + eElement3.getElementsByTagName("DMSStatus2").item(0).getTextContent());
                        PlxServiceMeasurePartsMap.put("DMSStatus2", eElement3.getElementsByTagName("DMSStatus2").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"EditType : " + eElement3.getElementsByTagName("EditType").item(0).getTextContent());
                        PlxServiceMeasurePartsMap.put("EditType", eElement3.getElementsByTagName("EditType").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"ExternalId : " + eElement3.getElementsByTagName("ExternalId").item(0).getTextContent());
                        PlxServiceMeasurePartsMap.put("ExternalId", eElement3.getElementsByTagName("ExternalId").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"InvoiceCode : " + eElement3.getElementsByTagName("InvoiceCode").item(0).getTextContent());
                        PlxServiceMeasurePartsMap.put("InvoiceCode", eElement3.getElementsByTagName("InvoiceCode").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"IsCritical : " + eElement3.getElementsByTagName("IsCritical").item(0).getTextContent());
                        PlxServiceMeasurePartsMap.put("IsCritical", eElement3.getElementsByTagName("IsCritical").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"IsNegative : " + eElement3.getElementsByTagName("IsNegative").item(0).getTextContent());  
                        PlxServiceMeasurePartsMap.put("IsNegative", eElement3.getElementsByTagName("IsNegative").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"Origin : " + eElement3.getElementsByTagName("Origin").item(0).getTextContent());
                        PlxServiceMeasurePartsMap.put("Origin", eElement3.getElementsByTagName("Origin").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"PartId : " + eElement3.getElementsByTagName("PartId").item(0).getTextContent());
                        PlxServiceMeasurePartsMap.put("PartId", eElement3.getElementsByTagName("PartId").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"PositionNumber : " + eElement3.getElementsByTagName("PositionNumber").item(0).getTextContent());
                        PlxServiceMeasurePartsMap.put("PositionNumber", eElement3.getElementsByTagName("PositionNumber").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"Quantity : " + eElement3.getElementsByTagName("Quantity").item(0).getTextContent());
                        PlxServiceMeasurePartsMap.put("Quantity", eElement3.getElementsByTagName("Quantity").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"ServiceMeasureId : " + eElement3.getElementsByTagName("ServiceMeasureId").item(0).getTextContent());  
                        PlxServiceMeasurePartsMap.put("ServiceMeasureId", eElement3.getElementsByTagName("ServiceMeasureId").item(0).getTextContent());
                    }
                }                                
                MyLogging.log(Level.INFO,"----------------------------");
                MyLogging.log(Level.INFO,"****************************");
                
                
            }                        
        }
        return PlxServiceMeasurePartsMap;
    }
        
    private Map parsePlxServiceMeasureWorkItem() throws ParserConfigurationException, SAXException, IOException{        
        NodeList nList = service_measure_xml_doc.getElementsByTagName("PlxXentryServiceMeasure");
        MyLogging.log(Level.INFO,"----------------------------");
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            MyLogging.log(Level.INFO,"\nCurrent Element :" + nNode.getNodeName());
            MyLogging.log(Level.INFO,"----------------------------");
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;                                
                NodeList ListOfPlxServiceMeasureWorkItemElement = eElement.getElementsByTagName("ListOfPlxServiceMeasureWorkItem");                
                for (int temp2 = 0; temp2 < ListOfPlxServiceMeasureWorkItemElement.getLength(); temp2++) {
                    Node nNode2 = ListOfPlxServiceMeasureWorkItemElement.item(temp2);
                    MyLogging.log(Level.INFO,"\nCurrent Element :" + nNode2.getNodeName());
                    Element eElement2 = (Element)nNode2; 
                    NodeList PlxServiceMeasureWorkItemElement = eElement2.getElementsByTagName("PlxServiceMeasureWorkItem");
                    for (int temp3 = 0; temp3 < PlxServiceMeasureWorkItemElement.getLength(); temp3++) {
                        Node nNode3 = PlxServiceMeasureWorkItemElement.item(temp3);
                        MyLogging.log(Level.INFO,"Current Element :" + nNode3.getNodeName());
                        Element eElement3 = (Element)nNode3;
                        MyLogging.log(Level.INFO,"DMSStatus2 : " + eElement3.getElementsByTagName("DMSStatus2").item(0).getTextContent());
                        PlxServiceMeasureWorkItemMap.put("DMSStatus", eElement3.getElementsByTagName("DMSStatus2").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"Description2 : " + eElement3.getElementsByTagName("Description2").item(0).getTextContent());
                        PlxServiceMeasureWorkItemMap.put("Description", eElement3.getElementsByTagName("Description2").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"EditType : " + eElement3.getElementsByTagName("EditType").item(0).getTextContent());
                        PlxServiceMeasureWorkItemMap.put("EditType", eElement3.getElementsByTagName("EditType").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"ExternalId : " + eElement3.getElementsByTagName("ExternalId").item(0).getTextContent());
                        PlxServiceMeasureWorkItemMap.put("ExternalId", eElement3.getElementsByTagName("ExternalId").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"IsNegative : " + eElement3.getElementsByTagName("IsNegative").item(0).getTextContent());
                        PlxServiceMeasureWorkItemMap.put("IsNegative", eElement3.getElementsByTagName("IsNegative").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"Number2 : " + eElement3.getElementsByTagName("Number2").item(0).getTextContent());
                        PlxServiceMeasureWorkItemMap.put("Number", eElement3.getElementsByTagName("Number2").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"Origin : " + eElement3.getElementsByTagName("Origin").item(0).getTextContent());
                        PlxServiceMeasureWorkItemMap.put("Origin", eElement3.getElementsByTagName("Origin").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"PositionNumber : " + eElement3.getElementsByTagName("PositionNumber").item(0).getTextContent());
                        PlxServiceMeasureWorkItemMap.put("PositionNumber", eElement3.getElementsByTagName("PositionNumber").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"ServiceMeasureId : " + eElement3.getElementsByTagName("ServiceMeasureId").item(0).getTextContent());
                        PlxServiceMeasureWorkItemMap.put("ServiceMeasureId", eElement.getElementsByTagName("ServiceMeasureId").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"ServicePackageId : " + eElement3.getElementsByTagName("ServicePackageId").item(0).getTextContent());
                        PlxServiceMeasureWorkItemMap.put("ServicePackageId", eElement3.getElementsByTagName("ServicePackageId").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"Unit : " + eElement3.getElementsByTagName("Unit").item(0).getTextContent());
                        PlxServiceMeasureWorkItemMap.put("Unit", eElement3.getElementsByTagName("Unit").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"Value2 : " + eElement3.getElementsByTagName("Value2").item(0).getTextContent());    
                        PlxServiceMeasureWorkItemMap.put("Value", eElement3.getElementsByTagName("Value2").item(0).getTextContent());
                    }
                }                
                MyLogging.log(Level.INFO,"----------------------------");
                MyLogging.log(Level.INFO,"****************************");
                                
            }                        
        }
        
        return PlxServiceMeasureWorkItemMap;
    }
    
    private Map parseServiceMeasurePackage() throws ParserConfigurationException, SAXException, IOException{
        Map outputMap = new HashMap();
        Map tmpMap  = new HashMap();        
        NodeList nList = service_measure_xml_doc.getElementsByTagName("PlxXentryServiceMeasure");
        MyLogging.log(Level.INFO,"----------------------------");
        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            MyLogging.log(Level.INFO,"\nCurrent Element :" + nNode.getNodeName());
            MyLogging.log(Level.INFO,"----------------------------");
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                                
                NodeList ListOfPlxServiceMeasurePackageElement = eElement.getElementsByTagName("ListOfPlxServiceMeasurePackage");                
                for (int temp2 = 0; temp2 < ListOfPlxServiceMeasurePackageElement.getLength(); temp2++) {
                    Node nNode2 = ListOfPlxServiceMeasurePackageElement.item(temp2);
                    MyLogging.log(Level.INFO,"\nCurrent Element :" + nNode2.getNodeName());
                    Element eElement2 = (Element)nNode2; 
                    NodeList PlxServiceMeasurePackageElement = eElement2.getElementsByTagName("PlxServiceMeasurePackage");
                    for (int temp3 = 0; temp3 < PlxServiceMeasurePackageElement.getLength(); temp3++) {
                        Node nNode3 = PlxServiceMeasurePackageElement.item(temp3);
                        MyLogging.log(Level.INFO,"Current Element :" + nNode3.getNodeName());
                        Element eElement3 = (Element)nNode3;
                        MyLogging.log(Level.INFO,"Description2 : " + eElement3.getElementsByTagName("Description2").item(0).getTextContent());
                        outputMap.put("Description", eElement3.getElementsByTagName("Description2").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"EditType : " + eElement3.getElementsByTagName("EditType").item(0).getTextContent());
                        outputMap.put("EditType", eElement3.getElementsByTagName("EditType").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"ExternalId : " + eElement3.getElementsByTagName("ExternalId").item(0).getTextContent());
                        outputMap.put("ExternalId", eElement3.getElementsByTagName("ExternalId").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"Origin : " + eElement3.getElementsByTagName("Origin").item(0).getTextContent());
                        outputMap.put("Origin", eElement3.getElementsByTagName("Origin").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"ServiceMeasureId : " + eElement3.getElementsByTagName("ServiceMeasureId").item(0).getTextContent());
                        outputMap.put("ServiceMeasureId", eElement3.getElementsByTagName("ServiceMeasureId").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"Number : " + eElement3.getElementsByTagName("Number").item(0).getTextContent());
                        outputMap.put("Number", eElement3.getElementsByTagName("Number").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"PositionNumber : " + eElement3.getElementsByTagName("PositionNumber").item(0).getTextContent());
                        outputMap.put("PositionNumber", eElement3.getElementsByTagName("PositionNumber").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"Value : " + eElement3.getElementsByTagName("Value").item(0).getTextContent()); 
                        outputMap.put("Value", eElement3.getElementsByTagName("Value2").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"DmsStatus : " + eElement3.getElementsByTagName("DmsStatus").item(0).getTextContent());
                        outputMap.put("DmsStatus", eElement3.getElementsByTagName("DmsStatus").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"IsNegative : " + eElement3.getElementsByTagName("IsNegative").item(0).getTextContent()); 
                        outputMap.put("IsNegative", eElement3.getElementsByTagName("IsNegative").item(0).getTextContent());
                        MyLogging.log(Level.INFO,"Unit : " + eElement3.getElementsByTagName("Unit").item(0).getTextContent()); 
                        outputMap.put("Unit", eElement3.getElementsByTagName("Unit").item(0).getTextContent());
                        
                        NodeList ListOfPlxServiceMeasurePackagePartsElement = eElement3.getElementsByTagName("ListOfPlxServiceMeasurePackageParts");
                        for (int temp4 = 0; temp4 < ListOfPlxServiceMeasurePackagePartsElement.getLength(); temp4++) {
                            Node nNode4 = ListOfPlxServiceMeasurePackagePartsElement.item(temp4);
                            MyLogging.log(Level.INFO,"Current Element :" + nNode4.getNodeName());
                            Element eElement4 = (Element)nNode4;
                            NodeList PlxServiceMeasurePackagePartsElement = eElement4.getElementsByTagName("PlxServiceMeasurePackageParts");
                            for (int temp5 = 0; temp5 < PlxServiceMeasurePackagePartsElement.getLength(); temp5++) {
                                Node nNode5 = PlxServiceMeasurePackagePartsElement.item(temp5);
                                MyLogging.log(Level.INFO,"Current Element :" + nNode5.getNodeName());
                                Element eElement5 = (Element)nNode5;
                                MyLogging.log(Level.INFO,"CoverageListId : " + eElement5.getElementsByTagName("CoverageListId").item(0).getTextContent());
                                PlxServiceMeasurePackage_PlxServiceMeasurePackagePartsMap.put("CoverageListId", eElement5.getElementsByTagName("CoverageListId").item(0).getTextContent());
                                MyLogging.log(Level.INFO,"EditType : " + eElement5.getElementsByTagName("EditType").item(0).getTextContent());
                                PlxServiceMeasurePackage_PlxServiceMeasurePackagePartsMap.put("EditType", eElement5.getElementsByTagName("EditType").item(0).getTextContent());
                                MyLogging.log(Level.INFO,"PartId : " + eElement5.getElementsByTagName("PartId").item(0).getTextContent());
                                PlxServiceMeasurePackage_PlxServiceMeasurePackagePartsMap.put("PartId", eElement5.getElementsByTagName("PartId").item(0).getTextContent());
                                MyLogging.log(Level.INFO,"Origin : " + eElement5.getElementsByTagName("Origin").item(0).getTextContent());
                                PlxServiceMeasurePackage_PlxServiceMeasurePackagePartsMap.put("Origin", eElement5.getElementsByTagName("Origin").item(0).getTextContent());
                                MyLogging.log(Level.INFO,"ServiceMeasurePackageId : " + eElement5.getElementsByTagName("ServiceMeasurePackageId").item(0).getTextContent());
                                PlxServiceMeasurePackage_PlxServiceMeasurePackagePartsMap.put("ServiceMeasurePackageId", eElement5.getElementsByTagName("ServiceMeasurePackageId").item(0).getTextContent());
                                MyLogging.log(Level.INFO,"Quantity : " + eElement5.getElementsByTagName("Quantity").item(0).getTextContent());
                                PlxServiceMeasurePackage_PlxServiceMeasurePackagePartsMap.put("Quantity", eElement5.getElementsByTagName("Quantity").item(0).getTextContent());
                            }
                        }
                        
                        NodeList ListOfPlxServiceMeasurePackageWorkItemElement = eElement3.getElementsByTagName("ListOfPlxServiceMeasurePackageWorkItem");
                        for (int temp4 = 0; temp4 < ListOfPlxServiceMeasurePackageWorkItemElement.getLength(); temp4++) {
                            Node nNode6 = ListOfPlxServiceMeasurePackageWorkItemElement.item(temp4);
                            MyLogging.log(Level.INFO,"\nCurrent Element :" + nNode6.getNodeName());
                            Element eElement6 = (Element)nNode6;
                            NodeList PlxServiceMeasurePackageWorkItemElement = eElement6.getElementsByTagName("PlxServiceMeasurePackageWorkItem");
                             for (int temp5 = 0; temp5 < PlxServiceMeasurePackageWorkItemElement.getLength(); temp5++) {
                                Node nNode7 = PlxServiceMeasurePackageWorkItemElement.item(temp5);
                                MyLogging.log(Level.INFO,"Current Element :" + nNode7.getNodeName());
                                Element eElement7 = (Element)nNode7;
                                MyLogging.log(Level.INFO,"Description2 : " + eElement7.getElementsByTagName("Description2").item(0).getTextContent());
                                PlxServiceMeasurePackage_PlxServiceMeasurePackageWorkItemMap.put("Description", eElement7.getElementsByTagName("Description2").item(0).getTextContent());
                                MyLogging.log(Level.INFO,"EditType : " + eElement7.getElementsByTagName("EditType").item(0).getTextContent());
                                PlxServiceMeasurePackage_PlxServiceMeasurePackageWorkItemMap.put("EditType", eElement7.getElementsByTagName("EditType").item(0).getTextContent());
                                MyLogging.log(Level.INFO,"Number2 : " + eElement7.getElementsByTagName("Number2").item(0).getTextContent());
                                PlxServiceMeasurePackage_PlxServiceMeasurePackageWorkItemMap.put("Number", eElement7.getElementsByTagName("Number2").item(0).getTextContent());
                                MyLogging.log(Level.INFO,"Origin : " + eElement7.getElementsByTagName("Origin").item(0).getTextContent());
                                PlxServiceMeasurePackage_PlxServiceMeasurePackageWorkItemMap.put("Origin", eElement7.getElementsByTagName("Origin").item(0).getTextContent());
                                MyLogging.log(Level.INFO,"ServiceMeasureId : " + eElement7.getElementsByTagName("ServiceMeasureId").item(0).getTextContent());
                                PlxServiceMeasurePackage_PlxServiceMeasurePackageWorkItemMap.put("ServiceMeasureId", eElement7.getElementsByTagName("ServiceMeasureId").item(0).getTextContent());
                                MyLogging.log(Level.INFO,"ServiceMeasurePackageId : " + eElement7.getElementsByTagName("ServiceMeasurePackageId").item(0).getTextContent());
                                PlxServiceMeasurePackage_PlxServiceMeasurePackageWorkItemMap.put("ServiceMeasurePackageId", eElement7.getElementsByTagName("ServiceMeasurePackageId").item(0).getTextContent());
                                MyLogging.log(Level.INFO,"Unit : " + eElement7.getElementsByTagName("Unit").item(0).getTextContent()); 
                                PlxServiceMeasurePackage_PlxServiceMeasurePackageWorkItemMap.put("Unit", eElement7.getElementsByTagName("Unit").item(0).getTextContent());
                                MyLogging.log(Level.INFO,"Value2 : " + eElement7.getElementsByTagName("Value2").item(0).getTextContent());
                                PlxServiceMeasurePackage_PlxServiceMeasurePackageWorkItemMap.put("Value", eElement7.getElementsByTagName("Value2").item(0).getTextContent());
                            }
                        }
                        
                        tmpMap.putAll(PlxServiceMeasurePackage_PlxServiceMeasurePackagePartsMap);
                        tmpMap.putAll(PlxServiceMeasurePackage_PlxServiceMeasurePackageWorkItemMap);
                        tmpMap.putAll(outputMap);
                        PlxServiceMeasurePackageMap.put(temp3,tmpMap);
                    }
                }
                MyLogging.log(Level.INFO,"----------------------------");
                MyLogging.log(Level.INFO,"****************************");
                
                
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
        return PlxServiceMeasureWorkItemMap;
    }
    
    public Map getPlxServiceMeasurePackageMap(){
        return PlxServiceMeasurePackageMap;
    }
    
    public Map getXentryInitJobServiceMeasureMap(){
        return XentryInitJobServiceMeasureMap;
    }
  
    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        String xmldoc = new TestString().xd;
        //XmlFormatter formatter = new XmlFormatter();
        //MyLogging.log(Level.INFO,"xe "+xmldoc);
        //String formatted_xmldoc = formatter.format(xmldoc);
        //MyLogging.log(Level.INFO,"xe "+formatted_xmldoc);
        XentryServiceMeasure xsm = new XentryServiceMeasure(xmldoc);
        /*xsm.handleXMLDoc();
        xsm.parseXentryServiceMeasure();
        xsm.parseServiceMeasureNotesAndDefectKey();
        xsm.parsePlxServiceMeasureParts();
        xsm.parsePlxServiceMeasureWorkItem();
        xsm.parseServiceMeasurePackage();*/
        
        
        MyLogging.log(Level.INFO,"----------------------------");
        MyLogging.log(Level.INFO,"****************************");
                
                
                              
        
    }
    
    
    
}
