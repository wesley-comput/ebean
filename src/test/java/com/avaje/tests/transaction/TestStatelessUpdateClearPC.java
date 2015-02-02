package com.avaje.tests.transaction;

import com.avaje.ebean.BaseTestCase;
import com.avaje.ebean.Ebean;
import com.avaje.tests.model.basic.EBasic;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TestStatelessUpdateClearPC extends BaseTestCase {

  @Test
  public void test() {


    Ebean.beginTransaction();

    EBasic newUser = new EBasic();
    newUser.setName("any@email.com");
    Ebean.save(newUser);

    // load the bean into the persistence context
    EBasic dummyLoadedUser = Ebean.find(EBasic.class, newUser.getId()); // This row is added
    assertNotNull(dummyLoadedUser);

    // stateless update (should clear the bean from the persistence context)
    EBasic updateUser = new EBasic();
    updateUser.setId(newUser.getId());
    updateUser.setName("anyNew@email.com");
    Ebean.update(updateUser);

    EBasic loadedUser = Ebean.find(EBasic.class, newUser.getId());
    assertEquals("anyNew@email.com", loadedUser.getName());

    Ebean.rollbackTransaction();
  }
}
