package com.prime.distributesearch.coreutil.propertyutil;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.prime.distributesearch.coreutil.annotationmgr.Singleton;

@Singleton
public class PropertiesParser {

  private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesParser.class);
  
  private Map<String, String> propertyMap = new HashMap<String, String>();
  
  private static final PropertiesParser propertiesParser = new PropertiesParser();
  
  public static synchronized PropertiesParser getInstance(){
    return propertiesParser;
  }
  
  public void parser(URL url){
    
    Properties prop = new Properties();
    InputStream input = null;
    
    try {
      input = url.openStream();
      
      prop.load(input);

      if (null != prop) {
        
        Enumeration<?> e = prop.propertyNames();
        
        while (e.hasMoreElements()) {
          
          String key = (String) e.nextElement();
          propertyMap.put(key, prop.getProperty(key));
        }
      }
      
    } catch (IOException e) {

      LOGGER.error(e.getMessage());
      
    }finally {
      
      try {
        input.close();
      } catch (IOException e) {
        LOGGER.error(e.getMessage());
      }
    }
  }
  
  public String getVauleByKey(String key){
    
    return propertyMap.get(key);
  }
  
}
