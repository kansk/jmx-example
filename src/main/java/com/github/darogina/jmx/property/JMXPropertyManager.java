package com.github.darogina.jmx.property;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

import javax.management.*;
import java.util.*;

/**
 * User: davidrogina
 * Date: 12/12/13
 */
@Component("jmxPropertyManager")
@ManagedResource
public class JMXPropertyManager implements ManageableProperties, DynamicMBean {

    @Qualifier("properties")
    @Autowired(required = true)
    private Properties properties;

    private boolean isHidden(String key){
        boolean boo = false;
        Collection<String> hiddenList = this.getHiddenPropertiesList();
        for (String string : hiddenList) {
            if (key.equalsIgnoreCase(string)){
                boo = true;
                break;
            }
        }
        return boo;
    }
    private boolean isReadOnly(String key){
        boolean boo = false;
        Collection<String> roList = this.getReadOnlyPropertiesList();
        for (String string : roList) {
            if (key.equalsIgnoreCase(string)){
                boo = true;
                break;
            }
        }
        return boo;
    }
    @Override
    public MBeanInfo getMBeanInfo() {
        MBeanAttributeInfo[] attributes = new MBeanAttributeInfo[properties.size()];
        MBeanInfo mBeanInfo;
        MBeanOperationInfo[] operations = new MBeanOperationInfo[1];
        int i = 0;
        for (Iterator iterator = properties.keySet().iterator(); iterator.hasNext();) {
            String key = (String) iterator.next();
            if (this.isReadOnly(key) || isHidden(key)){
                attributes[i] = new MBeanAttributeInfo
                        (key, "java.lang.String", key, true, false, false);
            } else {
                attributes[i] = new MBeanAttributeInfo
                        (key, "java.lang.String", key, true, true, false);
            }
            i++;
        }
        operations[0] = new MBeanOperationInfo("refreshCache", "Refresh Caches", null , null, MBeanOperationInfo.ACTION);
        mBeanInfo = new MBeanInfo(this.getClass().getName(),
                "Manage Properties", attributes, null,  operations, null);
        return mBeanInfo;
    }


    @Override
    public Object getAttribute(String attribute) throws AttributeNotFoundException,
            MBeanException, ReflectionException {
        Object o;
        if (isHidden(attribute)) {
            o = "XXX-HIDDEN-VALUE-XXX";
        } else {
            o =  properties.get(attribute);
        }
        return o;
    }


    @Override
    public void setAttribute(Attribute attribute) throws AttributeNotFoundException,
            InvalidAttributeValueException, MBeanException, ReflectionException {
        String key = attribute.getName();
        String value = (String)attribute.getValue();
        properties.put(key, value);
    }

    @Override
    public AttributeList getAttributes(String[] attributes) {
        List attributeList = new AttributeList();
        for (String attribute : attributes) {
            try {
                attributeList.add(getAttribute(attribute));
            } catch (AttributeNotFoundException | MBeanException | ReflectionException e) {
                e.printStackTrace();
            }
        }

        return (AttributeList) attributeList;
    }

    @Override
    public AttributeList setAttributes(AttributeList attributes) {
        List updatedAttributes = new AttributeList();

        for (Attribute attribute : attributes.asList()) {
            try {
                setAttribute(attribute);
            } catch (AttributeNotFoundException | InvalidAttributeValueException | MBeanException | ReflectionException e) {
                e.printStackTrace();
                continue;
            }

            updatedAttributes.add(attribute);
        }

        return (AttributeList) updatedAttributes;
    }

    @Override
    public Object invoke(String actionName, Object[] params, String[] signature) throws MBeanException, ReflectionException {
        Object ret = null;
        if (actionName == null){
            throw new RuntimeOperationsException( new IllegalArgumentException( "Operation name cannot be null"), "Cannot invoke a null operation");
        }
//        if (actionName.equals("refreshCache")){
//            AcmePropertyManager.refreshCache();
//        }
        //Returning null because we would like to avoid passing back a complex object
        //to the JMXClient because we have not implemented a MXBean
        return ret;
    }

    @Override
    public Properties getProperties() {
        return this.properties;
    }

    @Override
    public Collection<String> getHiddenPropertiesList() {
        Collection<String> c = new ArrayList<String>();
        c.add("jmx.property3");
        return c;
    }

    @Override
    public Collection<String> getReadOnlyPropertiesList() {
        Collection<String> c = new ArrayList<String>();
        c.add("acme.allowConcurrentUsers");
        return c;
    }
}
