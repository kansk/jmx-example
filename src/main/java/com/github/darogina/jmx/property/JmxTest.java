package com.github.darogina.jmx.property;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.MBeanExporter;
import org.springframework.stereotype.Component;

import javax.management.remote.JMXServiceURL;
import java.net.MalformedURLException;


@Component
public class JmxTest {
    @Autowired(required = true)
    private MBeanExporter mBeanExporter;

    public void someTest() {
        String[] domains;
            domains = mBeanExporter.getServer().getDomains();
//            domains = clientConnector.getObject().getDomains();
            for (String domain : domains) {
                System.out.println("DOMAIN: " + domain);
            }


//        mBeanExporter.getServer().
    }
}
