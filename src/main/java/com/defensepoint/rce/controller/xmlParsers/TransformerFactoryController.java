package com.defensepoint.rce.controller.xmlParsers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.XMLConstants;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.io.StringWriter;

@RestController
public class TransformerFactoryController {
    private final Logger logger = LoggerFactory.getLogger(TransformerFactoryController.class);

    @PostMapping(path = "/xml/TransformerFactory", consumes = { MediaType.APPLICATION_XML_VALUE })
    public String transformerFactory(@RequestBody String xmlStr) {

        String result = parseWithTransformerFactory(xmlStr);

        if(result != null) {
            return result;
        }

        return "XML parse error";
    }

    private String parseWithTransformerFactory(String xmlString) {
        logger.debug("Convert String To XML with TransformerFactory");

        try {
//            SAXTransformerFactory transformerFactory = (SAXTransformerFactory) javax.xml.transform.TransformerFactory.newInstance();
            TransformerFactory transformerFactory = javax.xml.transform.TransformerFactory.newInstance();

            transformerFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
//            transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
//            transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");

            Transformer transformer = transformerFactory.newTransformer();
            StreamSource source = new StreamSource(new StringReader(xmlString));
            StringWriter writer = new StringWriter();
            StreamResult target = new StreamResult(writer);

            transformer.transform(source, target);

            return writer.toString();

        } catch (TransformerConfigurationException e) {
            logger.error("TransformerConfigurationException: " + e.getMessageAndLocation());
        } catch (TransformerException e) {
            logger.error("TransformerException: " + e.getMessageAndLocation());
        } catch (Exception e) {
            logger.error("Exception: " + e.getMessage());
        }

        return null;
    }
}
