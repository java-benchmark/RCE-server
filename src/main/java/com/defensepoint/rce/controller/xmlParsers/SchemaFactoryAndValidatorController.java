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

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import java.io.IOException;
import java.io.StringReader;

@RestController
public class SchemaFactoryAndValidatorController {

    private final Logger logger = LoggerFactory.getLogger(SchemaFactoryAndValidatorController.class);

    @PostMapping(path = "/xml/SchemaFactoryAndValidator", consumes = { MediaType.APPLICATION_XML_VALUE })
    public String schemaFactoryAndValidator(@RequestBody String xmlStr) {

        String result = parseWithSchemaFactoryAndValidator( xmlStr );

        if(result != null) {
            return result;
        }

        return "XML parse error";
    }

    private String parseWithSchemaFactoryAndValidator(String xmlString) {
        logger.debug("Convert String To XML Document with XMLInputFactory");

        try {
            String schemaFilename = "foo.xsd";
            Document document = loadXml(xmlString);

            try {
                validate(document, schemaFilename);
                return "The file is valid!";
            } catch (Exception e) {
                return "The file is invalid! Reason: " + e.getMessage();
            }
        } catch (SAXException e) {
            logger.warn("SAXException was thrown: " + e.getMessage());
        } catch (IOException e) {
            logger.error("IOException was thrown: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Exception was thrown: " + e.getMessage());
        }

        return null;
    }

    public void validate(Document document, String schemaFilename) throws SAXException, IOException {
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = factory.newSchema(new StreamSource(schemaFilename));

        Validator validator = schema.newValidator();
        validator.validate(new DOMSource(document));
    }

    private Document loadXml(String xmlToValidate) throws Exception {
        DocumentBuilder builder = createDocumentBuilder();
        return builder.parse(new InputSource(new StringReader(xmlToValidate)));
    }

    private DocumentBuilder createDocumentBuilder() throws ParserConfigurationException {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        builderFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        builderFactory.setNamespaceAware(true);
        return builderFactory.newDocumentBuilder();
    }
}
