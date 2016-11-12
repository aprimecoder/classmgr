package com.prime.distributesearch.coreutil;

import java.net.URL;
import java.util.Enumeration;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.util.tracker.BundleTracker;
import org.osgi.util.tracker.BundleTrackerCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.prime.distributesearch.coreutil.classmgr.ClassInstanceMgr;
import com.prime.distributesearch.coreutil.classmgr.FieldTest;
import com.prime.distributesearch.coreutil.propertyutil.PropertiesParser;

public class CoreutilActivator implements BundleActivator {

  private static final Logger LOGGER = LoggerFactory.getLogger(CoreutilActivator.class);

  private Scanner scanner = new Scanner();

  private BundleTracker<Void> tracker;

  @Override
  public void start(BundleContext context) throws Exception {

    LOGGER.info("Coreutil bundle starting...");

    System.out.println("test");

    tracker = new BundleTracker<Void>(context, Bundle.ACTIVE, scanner);

    tracker.open();

  }

  @Override
  public void stop(BundleContext context) throws Exception {

    LOGGER.info("Coreutil bundle stopping...");
    tracker.close();
  }

}


class Scanner implements BundleTrackerCustomizer<Void> {

  private PropertiesParser propertiesParser = PropertiesParser.getInstance();

  private ClassInstanceMgr classInstanceMgr = ClassInstanceMgr.getInstance();

  @Override
  public Void addingBundle(Bundle bundle, BundleEvent event) {

    Enumeration<URL> propertiesEnumer = bundle.findEntries("conf/", "*.properties", true);
    Enumeration<URL> classFileEntries = bundle.findEntries("/com/prime/distributesearch/", "*.class", true);

    findProperties(propertiesEnumer);
    
    findClasses(classFileEntries);
    
    return null;
  }

  private void findClasses(Enumeration<URL> enumer) {

    if (null == enumer) {
      return;
    } else {
      while(enumer.hasMoreElements()){
        
        URL url = enumer.nextElement();
        if(null != url){
          classInstanceMgr.handleClass(url.getPath().substring(1));
        }
      }
    }
  }

  private void findProperties(Enumeration<URL> enumer) {

    if (null == enumer) {
      return;
    } else {
      while (enumer.hasMoreElements()) {

        URL url = enumer.nextElement();

        if (null != url) {
          propertiesParser.parser(url);
        }

      }
    }
  }

  @Override
  public void modifiedBundle(Bundle bundle, BundleEvent event, Void object) {
    // TODO Auto-generated method stub

  }

  @Override
  public void removedBundle(Bundle bundle, BundleEvent event, Void object) {
    // TODO Auto-generated method stub

  }


}
