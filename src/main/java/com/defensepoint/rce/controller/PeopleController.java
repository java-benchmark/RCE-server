package com.defensepoint.rce.controller;

import com.defensepoint.rce.entity.People;
import com.defensepoint.rce.entity.Student;
import com.defensepoint.rce.util.LoggerUtil;
import com.defensepoint.rce.util.SafeObjectInputStream;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.*;
import java.util.logging.Level;

@RestController
public class PeopleController {

    private final Logger logger = LoggerFactory.getLogger(PeopleController.class);

    @PostMapping("/people/safe-input/unsafe-test")
    public String peopleSafeInputUnsafeTest(HttpServletRequest requestEntity) throws IOException {

        byte[] bytes = IOUtils.toByteArray(requestEntity.getInputStream());

        ObjectInputStream stream = new ObjectInputStream(new ByteArrayInputStream(bytes));

        try {
            Object object = stream.readObject();

            logger.info(object.toString());
            return object.toString();
        } catch(Exception e) {
            logger.error("Error: " + e.getMessage());
            return "Error: " + e.getMessage();
        }
    }

    @PostMapping("/people/safe-input/safe-test")
    public String peopleSafeInputSafeTest(HttpServletRequest requestEntity) throws IOException {

        byte[] bytes = IOUtils.toByteArray(requestEntity.getInputStream());

        Set whitelist = new HashSet<>(Arrays.asList(People.class.getName(), ArrayList.class.getName()));
        ObjectInputStream stream = new SafeObjectInputStream(new ByteArrayInputStream(bytes), whitelist);

        try {
            Object object = stream.readObject();

            logger.info(object.toString());
            return object.toString();
        } catch(Exception e) {
            logger.error("Error: " + e.getMessage());
            return "Error: " + e.getMessage();
        }
    }

    @PostMapping("/people/unsafe-input/safe-test")
    public String peopleUnsafeInputSafeTest(HttpServletRequest request) throws IOException {

        byte[] bytes = IOUtils.toByteArray(request.getInputStream());

        Set whitelist = new HashSet<>(Arrays.asList(People.class.getName()));
        ObjectInputStream stream = new SafeObjectInputStream(new ByteArrayInputStream(bytes), whitelist);

        try {
            Object object = stream.readObject();

            logger.info(object.toString());
            return object.toString();
        } catch(Exception e) {
            logger.error("Error: " + e.getMessage());
            return "Error: " + e.getMessage();
        }
    }

    @PostMapping("/people/unsafe-input/unsafe-test")
    public String peopleUnsafeInputUnsafeTest(HttpServletRequest requestEntity) throws IOException {

        byte[] bytes = IOUtils.toByteArray(requestEntity.getInputStream());

        ObjectInputStream stream = new ObjectInputStream(new ByteArrayInputStream(bytes));

        try {
            Object object = stream.readObject();

            logger.info(object.toString());
            return object.toString();
        } catch(Exception e) {
            logger.error("Error: " + e.getMessage());
            return "Error: " + e.getMessage();
        }
    }

    @PostMapping("/people/safe-input/nested-unsafe-test")
    public String peopleUnsafeInputNestedUnsafeTest(HttpServletRequest requestEntity) throws IOException {

        byte[] bytes = IOUtils.toByteArray(requestEntity.getInputStream());

        ObjectInputStream stream = new ObjectInputStream(new ByteArrayInputStream(bytes));

        try {
            Object object = stream.readObject();

            if(object instanceof People) {
                System.out.println("People class");
            } else if(object instanceof Student) {
                System.out.println("Student class");
            } else {
                System.out.println("Teacher class? or Director class?");
            }

            logger.info(object.toString());
            return object.toString();
        } catch(Exception e) {
            logger.error("Error: " + e.getMessage());
            return "Error: " + e.getMessage();
        }
    }

    @PostMapping("/people/unsafe-input/safe-test/contrast")
    public String peopleUnsafeInputSafeTestContratSuggestion(HttpServletRequest request) throws IOException {

        InputStream untrustedStream = request.getInputStream();
        List safeClasses = Arrays.asList(new Class[] { People.class, ArrayList.class });

        ObjectInputStream in = new ObjectInputStream(untrustedStream) {
            protected Class resolveClass(ObjectStreamClass desc) throws InvalidClassException {
                Class clazz = null;
                try {
                    clazz = super.resolveClass(desc);
                    if (clazz.isArray() || clazz.isPrimitive() || safeClasses.contains(clazz) ) {
                        return clazz;
                    }
                } catch (ClassNotFoundException | IOException e) {
                    throw new InvalidClassException("Unauthorized deserialization attempt => ", clazz.getName());
                }
                return null;
            }
        };

        try {
            Object object = in.readObject();

            logger.info(object.toString());
            return object.toString();
        } catch(Exception e) {
            logger.error("Error: " + e.getMessage());
            return "Error: " + e.getMessage();
        }
    }

    @PostMapping("/people/safe-input/safe-test/contrast")
    public String peopleSafeInputSafeTestContratSuggestion(HttpServletRequest request) throws IOException {

        byte[] bytes = IOUtils.toByteArray(request.getInputStream());

        List safeClasses = Arrays.asList(new Class[] { People.class, ArrayList.class });

        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes)) {
            protected Class resolveClass(ObjectStreamClass desc) throws InvalidClassException {
                Class clazz = null;
                try {
                    clazz = super.resolveClass(desc);
                    if (clazz.isArray() || clazz.isPrimitive() || safeClasses.contains(clazz) ) {
                        return clazz;
                    }
                } catch (ClassNotFoundException | IOException e) {
                    throw new InvalidClassException("Unauthorized deserialization attempt => ", clazz.getName());
                }
                return null;
            }
        };

        try {
            Object object = in.readObject();

            logger.info(object.toString());
            return object.toString();
        } catch(Exception e) {
            logger.error("Error: " + e.getMessage());
            return "Error: " + e.getMessage();
        }
    }

    @PostMapping("/people/test/log1")
    public String peopleTestLog1(@RequestParam String message) {

        Logger logger1 = LoggerFactory.getLogger(PeopleController.class);

        logger1.info("CWE117: " + LoggerUtil.logFileSanitizer(message));

        return "logInjection1";
    }

    @PostMapping("/people/test/log2")
    public String peopleTestLog2(@RequestParam String message) {

        java.util.logging.Logger logger2 = java.util.logging.Logger.getLogger(PeopleController.class.getName());

        message = message.replaceAll("[\n|\r|\t]", "_")
                .replace("<", "&lt")
                .replace(">", "&gt");

        logger2.log(Level.INFO, message);

        return "logInjection2";
    }

    @PostMapping("/people/test/log3")
    public String peopleTestLog3(@RequestParam String message) {

        org.apache.logging.log4j.Logger logger3 = org.apache.logging.log4j.LogManager.getLogger(PeopleController.class);

        logger3.info("CWE117: " + LoggerUtil.logFileSanitizer(message));

        return "logInjection3";
    }

    @PostMapping("/people/test/log4")
    public String peopleTestLog4(@RequestParam String message) {

        org.apache.logging.log4j.Logger logger4 = org.apache.logging.log4j.LogManager.getLogger(PeopleController.class);

        logger4.info("CWE117: " + message);

        return "logInjection4";
    }
}
