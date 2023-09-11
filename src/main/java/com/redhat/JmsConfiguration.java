package com.redhat;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;
import javax.jms.ConnectionFactory;
import javax.transaction.TransactionSynchronizationRegistry;
import javax.transaction.UserTransaction;

import org.apache.camel.component.jms.JmsComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.mq.jms.MQQueueConnectionFactory;
import com.ibm.msg.client.wmq.WMQConstants;

import com.ibm.msg.client.jms.JmsConnectionFactory;
import com.ibm.msg.client.jms.JmsFactoryFactory;
import com.ibm.msg.client.wmq.WMQConstants;

import javax.jms.Destination;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.TextMessage;

@ApplicationScoped
public class JmsConfiguration {

  private static final Logger logger = LoggerFactory.getLogger(JmsConfiguration.class);
  
  private String host = "localhost";
  private int port = 1414;
  private String queueManager = "QM1";
  private String channel = "DEV.APP.SVRCONN";
  private String username = "app";
  private String password = "passw0rd";
  private long receiveTimeout = 2000;

  @Produces
  @Named("xajms")
  public JmsComponent mq() {
    logger.info("\n\n>>>>>>>>>>>>>>>>>>>>>>>\nHello En JmsConfiguration\n\n");
    JmsComponent jmsComponent = new JmsComponent();
    //jmsComponent.setConnectionFactory(mqQueueConnectionFactory());
    jmsComponent.setConnectionFactory(connectionFactory());
    return jmsComponent;

  }

  @Produces
  @Named("connectionFactory")
  public JmsConnectionFactory connectionFactory() {
    logger.info("\n\n>>>>>>>>>>>>>>>>>>>>>>>\nHello En mqQueueConnectionFactory\n\n");

    // Variables
		JMSContext context = null;
		Destination destination = null;
		JMSProducer producer = null;
		JMSConsumer consumer = null;
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


  public MQQueueConnectionFactory mqQueueConnectionFactory() {
    logger.info("\n\n>>>>>>>>>>>>>>>>>>>>>>>\nHello En mqQueueConnectionFactory\n\n");
    MQQueueConnectionFactory mqQueueConnectionFactory = new MQQueueConnectionFactory();
    mqQueueConnectionFactory.setHostName(host);
    try {
      mqQueueConnectionFactory.setTransportType(WMQConstants.WMQ_CM_CLIENT);
      mqQueueConnectionFactory.setChannel(channel);
      mqQueueConnectionFactory.setPort(port);
      mqQueueConnectionFactory.setQueueManager(queueManager);
      mqQueueConnectionFactory.setBooleanProperty(WMQConstants.USER_AUTHENTICATION_MQCSP, true);
      mqQueueConnectionFactory.setStringProperty(WMQConstants.USERID, username);
      mqQueueConnectionFactory.setStringProperty(WMQConstants.PASSWORD, password);
      mqQueueConnectionFactory.setStringProperty(WMQConstants.WMQ_CHANNEL, channel);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }
    return mqQueueConnectionFactory;
  }

}
