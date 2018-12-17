/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.plexada.xentry;

import com.siebel.data.SiebelPropertySet;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author efosa
 */
public class XentryOrder {
    
    private final SiebelPropertySet psOrder;
    private Map XentryOrderMap;
    private Map XentryOrder_ServiceAdvisorMap;

    
    
    public XentryOrder(SiebelPropertySet spsOrder){
        this.psOrder = spsOrder;
        this.XentryOrderMap = new HashMap();
        this.XentryOrder_ServiceAdvisorMap = new HashMap();
        XentryOrderMap = converSiebelPropertySetToMap(this.psOrder);
    }
    
    private Map converSiebelPropertySetToMap(SiebelPropertySet spsOrder){
       Map tmpMap = new HashMap();
       Map tmpMap_2 = new HashMap();
       SiebelPropertySet spsOrderServiceAdvisor = new SiebelPropertySet();
       tmpMap.put("OrderId", spsOrder.getProperty("OrderId"));
       tmpMap.put("PaymentMethod", spsOrder.getProperty("PaymentMethod"));
       tmpMap.put("ReceptionDateTime", spsOrder.getProperty("ReceptionDateTime"));
       tmpMap.put("ReturnDateTime", spsOrder.getProperty("ReturnDateTime"));
       
       spsOrderServiceAdvisor = spsOrder.getChild(0);
       tmpMap_2.put("FirstName", spsOrderServiceAdvisor.getProperty("FirstName"));
       tmpMap_2.put("LastName", spsOrderServiceAdvisor.getProperty("LastName"));
       tmpMap_2.put("Abbreviation", spsOrderServiceAdvisor.getProperty("Abbreviation"));
       XentryOrder_ServiceAdvisorMap = tmpMap_2;
       tmpMap.put("OrderServiceAdvisor", tmpMap_2);
       
       
       return tmpMap;
    }
    
    public Map getXentryOrderMap() {
        return XentryOrderMap;
    }

    public Map getXentryOrder_ServiceAdvisorMap() {
        return XentryOrder_ServiceAdvisorMap;
    }
    
}
