package com.defensepoint.rce.controller.xmlParsers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.StringReader;

@RestController
public class SAXParserFactoryController {
    private final Logger logger = LoggerFactory.getLogger(SAXParserFactoryController.class);

    @PostMapping(path = "/xml/SAXParserFactory", consumes = { MediaType.APPLICATION_XML_VALUE })
    public String sAXParserFactory(@RequestBody String xmlStr) {

        String result = parseWithSAXParserFactory(xmlStr);

        if(result != null) {
            return result;
        }

        return "XML parse error";
    }

    private String parseWithSAXParserFactory(String xmlString) {
        logger.debug("Convert String To XML Document with SAXParserFactory");

        try {
            FooHandler handler = new FooHandler();
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();

            parser.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, ""); // Compliant
            parser.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, ""); // compliant

            parser.parse(new InputSource(new StringReader(xmlString)), handler);

            return handler.getFoo();

        } catch (ParserConfigurationException e) {
            // This should catch a failed setFeature feature
            logger.error("ParserConfigurationException was thrown: " + e.getMessage());
        } catch (SAXException e) {
            // On Apache, this should be thrown when disallowing DOCTYPE
            logger.error("SAXException was thrown: " + e.getMessage());
        } catch (IOException e) {
            // XXE that points to a file that doesn't exist
            logger.error("IOException occurred, XXE may still possible: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Exception occurred: " + e.getMessage());
        }

        return null;
    }
}

class FooHandler extends DefaultHandler {

    boolean isFoo = false;
    String foo;

    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        if (qName.equalsIgnoreCase("foo")) {
            isFoo = true;
        }
    }

    public void characters(char ch[], int start, int length) {
        if (isFoo) {
            foo = new String(ch, start, length);
            isFoo = false;
        }
    }

    public String getFoo() {
        return foo;
    }
}
