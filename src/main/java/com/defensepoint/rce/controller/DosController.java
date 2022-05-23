package com.defensepoint.rce.controller;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Base64;

@RestController
public class DosController {

    private final Logger logger = LoggerFactory.getLogger(PeopleController.class);

    @PostMapping("/dos")
    public String dos(HttpServletRequest request) throws IOException {

        byte[] bytes = IOUtils.toByteArray(request.getInputStream());

        System.out.println(new String(bytes));
        // if not base64 serialized obj return
        if(!new String(bytes).startsWith("rO0AB")) {
            logger.error("Error: Invalid object");
            return "Error: Invalid object";
        }

        bytes = Base64.getDecoder().decode(bytes);

        try {
            ObjectInputStream stream = new ObjectInputStream(new ByteArrayInputStream(bytes));

            System.out.println("before");
            Object object = stream.readObject();
            System.out.println("after");

            logger.info(object.toString());
            return object.toString();
        } catch (Exception e) {
            logger.error("Error: " + e.getMessage());
            return "Error: " + e.getMessage();
        }
    }
}