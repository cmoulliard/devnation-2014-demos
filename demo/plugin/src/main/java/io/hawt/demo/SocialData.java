package io.hawt.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

public class SocialData implements SocialDataMBean {

    private static final transient Logger LOG = LoggerFactory.getLogger(SocialData.class);

    private MBeanServer mBeanServer;
    private ObjectName objectName;
    private Integer result;

    public void setPublishData(Integer content) {
        this.result = content;
        System.out.println(">> Metric received");
    }

    public Integer getPublishData() {
        return this.result;
    }

    public void sayHello() {
        System.out.println("hello, world");
    }

    public void init() {
        try {
            if (objectName == null) {
                objectName = new ObjectName("hawtio:type=SocialData");
            }
            if (mBeanServer == null) {
                mBeanServer = ManagementFactory.getPlatformMBeanServer();
            }
            try {
                mBeanServer.registerMBean(this, objectName);
            } catch (InstanceAlreadyExistsException iaee) {
                // Try to remove and re-register
                LOG.info("Re-registering SchemaLookup MBean");
                mBeanServer.unregisterMBean(objectName);
                mBeanServer.registerMBean(this, objectName);
            }
        } catch (Exception e) {
            LOG.warn("Exception during initialization: ", e);
            throw new RuntimeException(e);
        }
    }

    public void destroy() {
        try {
            if (objectName != null && mBeanServer != null) {
                mBeanServer.unregisterMBean(objectName);
            }
        } catch (Exception e) {
            LOG.warn("Exception unregistering mbean: ", e);
            throw new RuntimeException(e);
        }
    }
}
