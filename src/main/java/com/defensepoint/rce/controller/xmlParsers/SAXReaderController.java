package com.defensepoint.rce.controller.xmlParsers;

import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.InputSource;

import java.io.StringReader;

@RestController
public class SAXReaderController {

    private final Logger logger = LoggerFactory.getLogger(SAXReaderController.class);

    @PostMapping(path = "/xml/SAXReader", consumes = { MediaType.APPLICATION_XML_VALUE })
    public String sAXReader(@RequestBody String xmlStr) {

        String result = parseWithSAXReader(xmlStr);

        if(result != null) {
            return result;
        }

        return "XML parse error";
    }

    private String parseWithSAXReader(String xmlString)
    {
        logger.debug("Convert String To XML Document");

        try {
            SAXReader xmlReader = new SAXReader();
            xmlReader.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            org.dom4j.Document document = xmlReader.read(new InputSource(new StringReader(xmlString)));

            Element root = document.getRootElement();

            return root.getText();

        } catch (Exception e) {
            logger.error("Exception was thrown: " + e.getMessage());
        }

        return null;
    }
}
