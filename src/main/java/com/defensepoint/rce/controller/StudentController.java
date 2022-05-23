package com.defensepoint.rce.controller;

import com.defensepoint.rce.entity.Department;
import com.defensepoint.rce.entity.Student;
import com.defensepoint.rce.util.SafeObjectInputStream;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@RestController
public class StudentController {

    private final Logger logger = LoggerFactory.getLogger(PeopleController.class);

    @PostMapping("/student/safe-input/safe-test")
    public String studentSafeInputSafeTest(HttpServletRequest requestEntity) throws IOException {

        byte[] bytes = IOUtils.toByteArray(requestEntity.getInputStream());

        Set whitelist = new HashSet<>(Arrays.asList(Student.class.getName(), Department.class.getName()));
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
}
