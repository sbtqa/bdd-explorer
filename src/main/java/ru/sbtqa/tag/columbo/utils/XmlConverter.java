package ru.sbtqa.tag.columbo.utils;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.stereotype.Component;

import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Xml -> Object -> Xml converter
 *
 * Created by SBT-Razuvaev-SV on 28.12.2016.
 */
@Component("xmlConverter")
@Lazy(false)
public class XmlConverter {

    @Autowired private Marshaller marshaller;
    @Autowired private Unmarshaller unmarshaller;

    /**
     *     Converts Object to XML file
     * @param fileName - path for save xml file
     * @param graph - serializable object
     * @throws IOException - something wrong
     */
    public void objectToXML(String fileName, Object graph) throws IOException {
        try (FileOutputStream fos = FileUtils.openOutputStream(new File(fileName))) {
            marshaller.marshal(graph, new StreamResult(fos));
        }
    }

    /**
     *     Converts XML to Object
     * @param fileName - source xml file for unmarshalling
     * @throws IOException - something wrong
     */
    public Object xmlToObject(String fileName) throws IOException {
        try (FileInputStream fis = FileUtils.openInputStream(new File(fileName))) {
            return unmarshaller.unmarshal(new StreamSource(fis));
        }
    }

}
