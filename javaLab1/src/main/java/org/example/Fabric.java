package org.example;

import java.io.InputStream;
import java.util.Properties;
import java.io.IOException;
public class Fabric {
    public Fabric(){
        Properties properties = new Properties();
        try(InputStream inputStream = Fabric.class.getResourceAsStream("/config.properties")){

        }
        catch(IOException e){
            return;
        }
        InputStream config;
    }
}
