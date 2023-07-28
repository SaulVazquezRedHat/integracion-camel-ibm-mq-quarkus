package com.redhat;
import org.apache.camel.builder.RouteBuilder;

public class Route extends RouteBuilder {

    @Override
    public void configure() throws Exception {

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


    }
}