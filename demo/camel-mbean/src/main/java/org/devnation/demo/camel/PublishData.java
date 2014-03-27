package org.devnation.demo.camel;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

import javax.management.Attribute;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.Random;

public class PublishData extends RouteBuilder {

    private MBeanServer mBeanServer;
    private ObjectName objectName;
    private Attribute attribute;

    public PublishData() throws MalformedObjectNameException {
        objectName = new ObjectName("hawtio:type=SocialData");
    }

    @Override
    public void configure() throws Exception {
        from("timer://publishData?delay=5000&period=10000")
                .setBody().method(PublishData.class, "generateNumber")
                .process(new Processor() {
                    public void process(Exchange exchange) throws Exception {

                        Integer payload = (Integer) exchange.getIn().getBody();

                        // Get Management server
                        if (mBeanServer == null) {
                            mBeanServer = ManagementFactory.getPlatformMBeanServer();
                        }

                        // Set attribute with Content
                        attribute = new Attribute("PublishData", payload);

                        // Publish it
                        mBeanServer.setAttribute(objectName, attribute);
                    }
                });
    }

    public Integer generateNumber() {
        Random randomGenerator = new Random();
        return randomGenerator.nextInt(1000);
    }
}
