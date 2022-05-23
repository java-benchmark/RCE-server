package com.defensepoint.rce.controller.xmlParsers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.*;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.StringReader;

@RestController
public class XMLReaderController {

    private final Logger logger = LoggerFactory.getLogger(XMLReaderController.class);

    @PostMapping(path = "/xml/XMLReader", consumes = { MediaType.APPLICATION_XML_VALUE })
    public String xMLReader(@RequestBody String xmlStr) {

        String result = parseWithXMLReader(xmlStr);

        if(result != null) {
            return result;
        }

        return "XML parse error";
    }

    private String parseWithXMLReader(String xmlString)
    {
        logger.debug("Convert String To XML Document");

        try {
            FooReaderHandler handler = new FooReaderHandler();
            XMLReader reader = XMLReaderFactory.createXMLReader();
            reader.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            reader.setContentHandler(handler);
            reader.parse(new InputSource(new StringReader(xmlString)));

            return handler.getFoo();

        } catch (Exception e) {
            logger.error("Exception was thrown: " + e.getMessage());
        }

        return null;
    }
}

class FooReaderHandler implements ContentHandler {

    boolean isFoo = false;
    String foo;

    @Override
    public void setDocumentLocator(Locator locator) {

    }

    @Override
    public void startDocument() throws SAXException {

    }

    @Override
    public void endDocument() throws SAXException {

    }

    @Override
    public void startPrefixMapping(String prefix, String uri) throws SAXException {

    }

    @Override
    public void endPrefixMapping(String prefix) throws SAXException {

    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        if (qName.equalsIgnoreCase("foo")) {
            isFoo = true;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {

    }

    public void characters(char ch[], int start, int length) {
        if (isFoo) {
            foo = new String(ch, start, length);
            isFoo = false;
        }
    }

    @Override
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {

    }

    @Override
    public void processingInstruction(String target, String data) throws SAXException {

    }

    @Override
    public void skippedEntity(String name) throws SAXException {

    }

    public String getFoo() {
        return foo;
    }
}