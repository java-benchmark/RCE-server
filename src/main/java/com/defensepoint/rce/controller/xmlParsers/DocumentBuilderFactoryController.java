package com.defensepoint.rce.controller.xmlParsers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import java.io.IOException;
import java.io.StringReader;

@RestController
public class DocumentBuilderFactoryController {

    private final Logger logger = LoggerFactory.getLogger(DocumentBuilderFactoryController.class);

    @PostMapping(path = "/xml/DocumentBuilderFactory", consumes = { MediaType.APPLICATION_XML_VALUE })
    public String documentBuilderFactory(@RequestBody String xmlStr) {

        //Use method to convert XML string content to XML Document object
        Document doc = parseWithDocumentBuilderFactory( xmlStr );

        if (doc != null) {
            return doc.getElementsByTagName("foo").item(0).getTextContent();
        } else {
            return "XML parse error";
        }
    }

    private Document parseWithDocumentBuilderFactory(String xmlString)
    {
        logger.debug("Convert String To XML Document");

        //Parser that produces DOM object trees from XML content
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        //API to obtain DOM Document instance
        DocumentBuilder builder;

        try {

            //Disallow dtd
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            factory.setValidating(true);

            //Create DocumentBuilder with default configuration
            builder = factory.newDocumentBuilder();

            //Parse the content to Document object and return
            return builder.parse(new InputSource(new StringReader(xmlString)));

        } catch (ParserConfigurationException e) {
            logger.info("ParserConfigurationException was thrown: " + e.getMessage());
        } catch (SAXException e) {
            logger.warn("SAXException was thrown: " + e.getMessage());
        } catch (IOException e) {
            logger.error("IOException was thrown. IOException occurred, XXE may still possible: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Exception was thrown. Exception occurred, XXE may still possible: " + e.getMessage());
        }

        return null;
    }
}