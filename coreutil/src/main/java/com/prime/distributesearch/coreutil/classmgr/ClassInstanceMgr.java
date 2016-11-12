package com.prime.distributesearch.coreutil.classmgr;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.prime.distributesearch.coreutil.annotationmgr.ReferenceClass;
import com.prime.distributesearch.coreutil.annotationmgr.Singleton;

@Singleton
public class ClassInstanceMgr {

  private static final Logger LOGGER = LoggerFactory.getLogger(ClassInstanceMgr.class);

  private static final ClassInstanceMgr classInstanceMgr = new ClassInstanceMgr();

  private final Map<String, Object> classMap = new HashMap<String, Object>();

  private ClassInstanceMgr() {

  }

  public static synchronized ClassInstanceMgr getInstance() {

    return classInstanceMgr;
  }

  public Object getClassInstance(String className) {
    return classMap.get(className);
  }

  public void handleClass(String className) {

    String tempName = className.replace("/", ".");

    String name = tempName.substring(0, tempName.length() - 6);

    if ("com.prime.distributesearch.coreutil.annotationmgr.Singleton".equals(name)) {
      return;
    }

    if ("com.prime.distributesearch.coreutil.annotationmgr.ReferenceClass".equals(name)) {
      return;
    }

    try {
      Class<?> c = Class.forName(name);

      getSingletonAnnotation(c, name);

      getAnnotationField(c, name);

    } catch (ClassNotFoundException e) {
      LOGGER.error("ClassNotFoundException:" + name + e.getMessage());
    } catch (SecurityException e) {

      LOGGER.error("SecurityException:" + name + e.getMessage());
    } catch (IllegalArgumentException e) {

      LOGGER.error("IllegalArgumentException:" + name + e.getMessage());
    }
  }

  private void getAnnotationField(Class<?> c, String className){
    
    System.out.println("testclass:" + c.getName());
    
    Field[] fields = c.getDeclaredFields();
    
    if(null != fields && fields.length != 0){
      
      int fieldLen = fields.length;
      
      for(int i=0;i<fieldLen;i++){
        
        Field field = fields[i];
        
        System.out.println("fieldname:" + field.getName());
        
        if(field.isAnnotationPresent(ReferenceClass.class)){
          
          handleAnnotationField(field,field.getType(),c);
        }
      }
    }
  }
  
  private void handleAnnotationField(Field field,Class<?> fieldTypeClass,Class<?> c){
    
    System.out.println("instance:" + c.getName());
    
    System.out.println("fieldTypeClass:" + fieldTypeClass.getName());
    
    Object fieldInstance = classMap.get(fieldTypeClass.getName());
    
    try {
      field.setAccessible(true);
      field.set(null, fieldInstance);
      field.setAccessible(false);
    } catch (IllegalArgumentException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  private void getSingletonAnnotation(Class<?> c, String className) {


    try {

      if (c.isAnnotationPresent(Singleton.class)) {

        Constructor<?> con = c.getDeclaredConstructor();
        con.setAccessible(true);

        Object classObj = con.newInstance();

        classMap.put(className, classObj);
        
        con.setAccessible(false);
      }

    } catch (NoSuchMethodException e) {

      LOGGER.error("NoSuchMethodException:" + className + e.getMessage());
    } catch (SecurityException e) {

      LOGGER.error("SecurityException:" + className + e.getMessage());
    } catch (InstantiationException e) {

      LOGGER.error("InstantiationException:" + className + e.getMessage());
    } catch (IllegalAccessException e) {

      LOGGER.error("IllegalAccessException:" + className + e.getMessage());
    } catch (IllegalArgumentException e) {

      LOGGER.error("IllegalArgumentException:" + className + e.getMessage());
    } catch (InvocationTargetException e) {

      LOGGER.error("InvocationTargetException:" + className + e.getMessage());
    }

  }

  @SuppressWarnings("unused")
  private void print() {

    Set<String> s = classMap.keySet();
    Iterator<String> iter = s.iterator();
    while (iter.hasNext()) {
      String key = iter.next();
      System.out.println(key + ":" + classMap.get(key));
    }
  }
}
