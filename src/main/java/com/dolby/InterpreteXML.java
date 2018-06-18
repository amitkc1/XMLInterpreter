package com.dolby;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.xml.stream.*;

public class InterpreteXML {
   static List<String> commandList = new ArrayList<>();
   static File file;
   static BufferedWriter out;

    public static void main(String[] args) throws Exception{
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        InputStream in = new FileInputStream("/Users/achaudhari/Desktop/XMLInterpreter/Sample.xml");

        XMLStreamReader streamReader = inputFactory.createXMLStreamReader(in);
        streamReader.nextTag();
        parseXML(streamReader);

    }

    public static void openFile(File fileToOpen) throws IOException {
        file = fileToOpen;
        out = new BufferedWriter(new FileWriter(file));
        System.out.println("Called open file");
        System.out.println("Can read the file: "+file.canRead());
        System.out.println("Can read the file: "+file.canWrite());
    }

    public static void writeToFile (String text) throws IOException {
        out.write(text);
        System.out.println("Called write file");
    }

    public static void closeFile(File fileToClose) throws IOException {
        if(file.equals(fileToClose)){
            out.close();
        }

        else{
            System.err.println("File has to be opened to read and write before closing");
        }
        System.out.println("Called close file");
    }

    public static void parseXML(XMLStreamReader streamReader) throws XMLStreamException, IOException {
        while (streamReader.hasNext()) {
            if (streamReader.isStartElement()){

                String text = streamReader.getElementText();

                commandList = Stream.of(text.split("\n")).filter(item -> !"".equals(item)).collect(Collectors.toList());

                for (String element : commandList) {
                    String[] commandAndArgument = element.trim().split(" ");
                    switch (commandAndArgument[0]){
                        case "OPEN": {
                            openFile(new File(commandAndArgument[1]));
                            break;
                        }

                        case "WRITE": {
                            writeToFile(commandAndArgument[1]);
                            break;
                        }

                        case "CLOSE": {
                            closeFile(new File(commandAndArgument[1]));
                            break;
                        }
                    }

                }
                streamReader.next();

            }

        }
    }
}
