package com.redhat;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.msg.client.wmq.WMQConstants;

import com.ibm.msg.client.jms.JmsConnectionFactory;
import com.ibm.msg.client.jms.JmsFactoryFactory;

import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class JmsConfiguration {

  private static final Logger logger = LoggerFactory.getLogger(JmsConfiguration.class);

  @ConfigProperty(name = "mq.host") 
  String host;

  @ConfigProperty(name = "mq.port") 
  int port;

  @ConfigProperty(name = "mq.queueManager") 
  String queueManager;

  @ConfigProperty(name = "mq.channel") 
  String channel;

  @ConfigProperty(name = "mq.username") 
  String username;

  @ConfigProperty(name = "mq.password") 
  String password;

  @Produces
  @Named("connectionFactory")
  public JmsConnectionFactory connectionFactory() {
    logger.info("Creating connectionFactory");

    JmsConnectionFactory cf = null;

    try {
			JmsFactoryFactory ff = JmsFactoryFactory.getInstance(WMQConstants.WMQ_PROVIDER);
			cf = ff.createConnectionFactory();

			// Set the properties
			cf.setStringProperty(WMQConstants.WMQ_HOST_NAME, host);
			cf.setIntProperty(WMQConstants.WMQ_PORT, port);
			cf.setStringProperty(WMQConstants.WMQ_CHANNEL, channel);
			cf.setIntProperty(WMQConstants.WMQ_CONNECTION_MODE, WMQConstants.WMQ_CM_CLIENT);
			cf.setStringProperty(WMQConstants.WMQ_QUEUE_MANAGER, queueManager);
			cf.setStringProperty(WMQConstants.WMQ_APPLICATIONNAME, "JmsPutGet (JMS)");
			cf.setBooleanProperty(WMQConstants.USER_AUTHENTICATION_MQCSP, true);
			cf.setStringProperty(WMQConstants.USERID, username);
			cf.setStringProperty(WMQConstants.PASSWORD, password);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }
    return cf;
  }

}
