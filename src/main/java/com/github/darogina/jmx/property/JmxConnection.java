package com.github.darogina.jmx.property;

import org.springframework.stereotype.Component;

import javax.management.*;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.naming.Context;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Hashtable;

@Component
public class JmxConnection {

    public void connect(String username, String password) {
        String serviceName = "com.bea:Name=DomainRuntimeService,Type=weblogic.management.mbeanservers.domainruntime.DomainRuntimeServiceMBean";
        try {
            ObjectName service = new ObjectName(serviceName);
        } catch (MalformedObjectNameException e) {
            throw new RuntimeException("Not able to create JMX ObjectName: " + serviceName);
        }

        String protocol = "t3";
        String jndiroot = "/jndi/";
        String mserver = "weblogic.management.mbeanservers.runtime";
        JMXServiceURL serviceURL = null;
        try {
            serviceURL = new JMXServiceURL(protocol, "localhost", 7001, jndiroot + mserver);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        System.out.println("Service URL: " + serviceURL.toString());

        Hashtable h = new Hashtable();
        h.put(Context.SECURITY_PRINCIPAL, username);
        h.put(Context.SECURITY_CREDENTIALS, password);
        h.put(JMXConnectorFactory.PROTOCOL_PROVIDER_PACKAGES,
                "weblogic.management.remote");
        h.put("jmx.remote.x.request.waiting.timeout", new Long(10000));
        JMXConnector connector = null;
        try {
            connector = JMXConnectorFactory.connect(serviceURL, h);
        } catch (IOException e) {
            e.printStackTrace();
        }
        MBeanServerConnection remoteMBeanServer = null;
        try {
            remoteMBeanServer = connector.getMBeanServerConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] domains = new String[0];
        try {
            domains = remoteMBeanServer.getDomains();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (String domain : domains) {
            System.out.println("JMXCONNECTION DOMAIN: " + domain);
        }

        ObjectName objectName = null;
        try {
            //INFO: Located managed bean 'person': registering with JMX server as MBean [com.github.darogina.jmx.model:name=person,type=Person]
            objectName = new ObjectName("com.github.darogina.jmx.model:name=person,type=Person");
        } catch (MalformedObjectNameException e) {
            e.printStackTrace();
        }
        MBeanInfo mBeanInfo = null;
        try {
            mBeanInfo = remoteMBeanServer.getMBeanInfo(objectName);
            System.out.println("MBeanInfo: " + mBeanInfo.toString());
        } catch (InstanceNotFoundException | IntrospectionException | ReflectionException | IOException e) {
            e.printStackTrace();
        }


    }
}
