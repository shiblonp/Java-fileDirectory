package com.pluralsight.filesystem;
import static java.nio.file.StandardCopyOption.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


//Created on 2/4 to demonstrate how Java supports FileSystems
public class Main {

    public static void main(String[] args) {
        String [] data = {
                "Line 1",
                "Line 2 2",
                "Line 3 3 3",
                "Line 4 4 4 4",
                "Line 5 5 5 5 5",
        };

        //try with resource because whenever we open the file system, it needs to be closed
        //the path specifies a zip file in the default sys

        try(FileSystem zipFs = openZip(Paths.get("myData.zip"))){
            copyToZip(zipFs);
            writeToFileInZip1(zipFs,data);
            writeToFileInZip2(zipFs,data);
        }catch(Exception e){
            System.out.println(e.getClass().getSimpleName() + " - " + e.getMessage());
        }
    }
    //this method will create a zip file
    //the default path is passed in through the method parameter
    private static FileSystem openZip(Path zipPath) throws IOException, URISyntaxException{
        //the map will specify provider specific properties
        Map<String, String> providerProps = new HashMap<>();
        //if it doesn't exist, then create it
        providerProps.put("create", "true");

        //added .toUri to make sure that the path given is a correct Uri path
        URI zipUri = new URI("jar:file", zipPath.toUri().getPath(), null);
        //passes in the URI and the properties to create and then return the new zip file system
        FileSystem zipFs = FileSystems.newFileSystem(zipUri, providerProps);

        return zipFs;
    }
    //reads a default file and copies it into a zip file system
    private static void copyToZip(FileSystem zipFs) throws IOException{
        Path sourceFile = Paths.get("file1.txt");
        Path destFile = zipFs.getPath("/file1Copied.txt");
        Files.copy(sourceFile,destFile, REPLACE_EXISTING);
    }
    //Methods to show that I can write to the filesystem directly from the program
    //The first method shows how to do it by creating the buffered writer first
    private static void writeToFileInZip1(FileSystem zipFs, String [] data) throws IOException
    {
        try(BufferedWriter writer = Files.newBufferedWriter(zipFs.getPath("/newFile1.txt")))
        {
            for(String d:data){
                writer.write(d);
                writer.newLine();
            }
        }
    }
    private static void writeToFileInZip2(FileSystem zipFs, String [] data)throws IOException
    {
        //FileSystems way of writing the file, another way to do what is written in the previous method
    Files.write(zipFs.getPath("/newFile2.txt"), Arrays.asList(data), Charset.defaultCharset(), StandardOpenOption.CREATE);
    }
}

