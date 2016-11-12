package com.prime.distributesearch.coreutil.classmgr;

import com.prime.distributesearch.coreutil.annotationmgr.ReferenceClass;

public class FieldTest {

  @ReferenceClass
  private static ClassInstanceMgr classInstanceMgr;
  
  public void print(){
    System.out.println("666" + classInstanceMgr);
  }
}
