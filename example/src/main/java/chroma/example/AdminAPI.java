package chroma.example;

import io.github.locxngo.chroma.AdminClient;
import io.github.locxngo.chroma.Config;
import io.github.locxngo.chroma.schemas.CreateDatabaseResponse;
import io.github.locxngo.chroma.schemas.CreateTenantResponse;
import io.github.locxngo.chroma.schemas.DeleteDatabaseResponse;
import io.github.locxngo.chroma.schemas.GetDatabaseResponse;
import io.github.locxngo.chroma.schemas.GetDatabasesResponse;
import io.github.locxngo.chroma.schemas.GetTenantResponse;

@SuppressWarnings({"java:S106", "java:S1192"})
public class AdminAPI extends BaseAPI {
    public static void main(String[] args) {
        AdminClient adminClient = new AdminClient(
            Config.defaultConfig()
        );
        GetTenantResponse getTenantResponse = adminClient.getTenant("default_tenant");
        throwErrorIfHas(getTenantResponse);

        CreateTenantResponse createTenantResponse = adminClient.createTenant("tenant_example");
        if (createTenantResponse.isError()) {
            System.out.println(
                "create_tenant_error: " + createTenantResponse.getError() + " with message " + createTenantResponse.getMessage());
        } else {
            System.out.println("create tenant: tenant_example successful");
        }
        CreateDatabaseResponse createDatabasePayload = adminClient.createDatabase("tenant_example", "tenant_database");
        if (createDatabasePayload.isError()) {
            System.out.println(
                "create_database_error: " + createDatabasePayload.getError() + " with message " + createDatabasePayload.getMessage());
        } else {
            System.out.println("create database: tenant_database successful");
        }
        GetDatabasesResponse databases = adminClient.getListDatabases("tenant_example", 2, 0);
        throwErrorIfHas(databases);
        databases.getDatabases().forEach(
            getDatabaseResponse ->
                System.out.println(
                    "database: " + getDatabaseResponse.getName() + " with id = " + getDatabaseResponse.getId()
                )
        );

        GetDatabaseResponse getDatabaseResponse = adminClient.getDatabase(
            "tenant_example", "tenant_database"
        );
        throwErrorIfHas(getDatabaseResponse);

        getDatabaseResponse = adminClient.getDatabase("tenant_example", "tenant_database_not_exist");
        System.out.println(
            "database [tenant_database_not_exist] not exist: err_code = " + getDatabaseResponse.getErrorCode() + ", " +
                "err=" + getDatabaseResponse.getError() + ", msg=" + getDatabaseResponse.getMessage()
        );

        DeleteDatabaseResponse deleteDatabaseResponse = adminClient.deleteDatabase(
            "tenant_example", "tenant_database"
        );
        throwErrorIfHas(deleteDatabaseResponse);
    }
}
