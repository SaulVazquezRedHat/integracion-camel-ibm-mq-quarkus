package com.redhat;
import org.apache.camel.builder.RouteBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import org.apache.camel.component.jms.JmsComponent;


@ApplicationScoped
public class Route extends RouteBuilder {

    private static final Logger logger = LoggerFactory.getLogger(Route.class);

    @Override
    public void configure() throws Exception {

        logger.info("\n\n>>>>>>>>>>>>>>>>>>>>>>>\nHello World 2\n\n");

        /*
        // Consume from rest endpoint
        rest("/api")
            .post("/hello")
            .consumes("plain/text").type(String.class)
            .to("direct:greet");

            from("direct:greet")
                .log("sending ${body} to JMS queue")
                .wireTap("direct:tap");

            from("direct:tap")
                .to("jms:queue:orders");
        */

        /* 
        // ESTA OPCION YA SIRVE
        from("timer:generate?repeatCount=5&period=1000")
            .log("Hello router!")
            .to("jms:queue:DEV.QUEUE.1");
        */

        from("timer:generate?repeatCount=5&period=1000")
            .routeId("generate-route")
            .transform(constant("HELLO from Camel!"))
            .wireTap("direct:tap");

        from("direct:tap")   
            .log("${body}")
            .to("jms:queue:DEV.QUEUE.1");


    }

}