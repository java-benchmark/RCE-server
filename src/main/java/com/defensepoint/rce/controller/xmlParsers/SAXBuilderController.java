package com.defensepoint.rce.controller.xmlParsers;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.InputSource;

import java.io.StringReader;

@RestController
public class SAXBuilderController {

    private final Logger logger = LoggerFactory.getLogger(SAXBuilderController.class);

    @PostMapping(path = "/xml/SAXBuilder", consumes = { MediaType.APPLICATION_XML_VALUE })
    public String sAXBuilder(@RequestBody String xmlStr) {

        String result = parseWithSAXBuilder(xmlStr);

        if(result != null) {
            return result;
        }

        return "XML parse error";
    }

    private String parseWithSAXBuilder(String xmlString)
    {
        logger.debug("Convert String To XML");

        try {

            SAXBuilder builder = new SAXBuilder();
            builder.setFeature("http://apache.org/xml/features/disallow-doctype-decl",true);

            Document document = builder.build(new InputSource(new StringReader(xmlString)));

            Element root = document.getRootElement();

            return root.getText();

        } catch (Exception e) {
            logger.error("Exception was thrown: " + e.getMessage());
        }

        return null;
    }
}
