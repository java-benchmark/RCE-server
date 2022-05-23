package com.defensepoint.rce.controller.xmlParsers;

import com.defensepoint.rce.entity.Foo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.InputSource;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;

@RestController
public class JAXBUnmarshallerController {

    private final Logger logger = LoggerFactory.getLogger(JAXBUnmarshallerController.class);

    @PostMapping(path = "/xml/JAXBUnmarshaller", consumes = { MediaType.APPLICATION_XML_VALUE })
    public String jAXBUnmarshaller(@RequestBody String xmlStr) {

        String result = parseWithJAXBUnmarshaller(xmlStr);

        if(result != null) {
            return result;
        }

        return "XML parse error";
    }

    private String parseWithJAXBUnmarshaller(String xmlString)
    {
        logger.debug("Convert String To XML Document");

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Foo.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            Object foo = jaxbUnmarshaller.unmarshal(new InputSource(new StringReader(xmlString)));

            return foo.toString();

        } catch (UnmarshalException e) {
            logger.error("UnmarshalException was thrown: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Exception was thrown: " + e.getMessage());
        }

        return null;
    }
}
