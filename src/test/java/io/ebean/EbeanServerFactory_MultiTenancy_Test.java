package io.ebean;

import io.ebean.EbeanServerFactory;
import io.ebean.config.CurrentTenantProvider;
import io.ebean.config.ServerConfig;
import io.ebean.config.TenantDataSourceProvider;
import io.ebean.config.TenantMode;
import io.ebean.config.TenantSchemaProvider;
import io.ebean.config.dbplatform.mysql.MySqlPlatform;
import io.ebean.config.dbplatform.postgres.PostgresPlatform;
import org.junit.Test;
import org.mockito.Mockito;

import javax.sql.DataSource;

public class EbeanServerFactory_MultiTenancy_Test {

  /**
   *  Tests using multi tenancy per database
   */
  @Test
  public void create_new_server_with_multi_tenancy_db() {

    String tenant = "customer";
    CurrentTenantProvider tenantProvider = Mockito.mock(CurrentTenantProvider.class);
    Mockito.doReturn(tenant).when(tenantProvider).currentId();

    DataSource mockedDataSource = Mockito.mock(DataSource.class);
    TenantDataSourceProvider dataSourceProvider = Mockito.mock(TenantDataSourceProvider.class);
    Mockito.doReturn(mockedDataSource).when(dataSourceProvider).dataSource(tenant);

    ServerConfig config = new ServerConfig();
    config.setName("multiTenantDb");
    config.loadFromProperties();
    config.loadTestProperties();
    config.setRegister(false);
    config.setDefaultServer(false);

    config.setTenantMode(TenantMode.DB);
    config.setCurrentTenantProvider(tenantProvider);
    config.setTenantDataSourceProvider(dataSourceProvider);

    // When TenantMode.DB we don't really want to run DDL
    // and we want to explicitly specify the Database platform
    //config.setDdlGenerate(false);
    //config.setDdlRun(false);
    config.setDatabasePlatform(new PostgresPlatform());

    EbeanServerFactory.create(config);
  }



  /**
   *  Tests using multi tenancy per schema
   */
  @Test
  public void create_new_server_with_multi_tenancy_schema() {

    String tenant = "customer";
    CurrentTenantProvider tenantProvider = Mockito.mock(CurrentTenantProvider.class);
    Mockito.doReturn(tenant).when(tenantProvider).currentId();

    TenantSchemaProvider schemaProvider = Mockito.mock(TenantSchemaProvider.class);
    Mockito.doReturn("tenant_schema").when(schemaProvider).schema(tenant);

    ServerConfig config = new ServerConfig();
    config.setName("h2");
    config.loadFromProperties();
    config.loadTestProperties();
    config.setRegister(false);
    config.setDefaultServer(false);

    config.setTenantMode(TenantMode.SCHEMA);
    config.setCurrentTenantProvider(tenantProvider);
    config.setTenantSchemaProvider(schemaProvider);

    config.setDdlRun(false);
    config.setDatabasePlatform(new MySqlPlatform());

    EbeanServerFactory.create(config);
  }
}
