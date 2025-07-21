package io.github.locxngo.chroma;

import io.github.locxngo.chroma.exception.ChromaAPIException;
import io.github.locxngo.chroma.schemas.CreateDatabasePayload;
import io.github.locxngo.chroma.schemas.CreateDatabaseResponse;
import io.github.locxngo.chroma.schemas.CreateTenantPayload;
import io.github.locxngo.chroma.schemas.CreateTenantResponse;
import io.github.locxngo.chroma.schemas.DeleteDatabaseResponse;
import io.github.locxngo.chroma.schemas.GetDatabaseResponse;
import io.github.locxngo.chroma.schemas.GetDatabasesResponse;
import io.github.locxngo.chroma.schemas.GetTenantResponse;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.List;

@SuppressWarnings("java:S1192")
public class AdminClient extends BaseClient {

    public AdminClient(Config config) {
        super(config);
    }


    /**
     * Create a new tenant. Raises an error if the tenant already exists.
     */
    public CreateTenantResponse createTenant(String name) {
        if (StringUtils.isBlank(name)) {
            return badParameter(new CreateTenantResponse(), "tenant name must not blank");
        }
        CreateTenantPayload payload = new CreateTenantPayload().setName(name);
        return getApiClient().post(ApiPath.TENANTS, CreateTenantResponse.class, payload);
    }

    /**
     * Get a tenant. Raises an error if the tenant does not exist.
     */
    public GetTenantResponse getTenant(String name) {
        if (StringUtils.isBlank(name)) {
            return badParameter(new GetTenantResponse(), "tenant name must not blank");
        }
        return getApiClient().get(ApiPath.tenant(name), GetTenantResponse.class);
    }

    /**
     * Create a new database. Raises an error if the database already exists.
     */
    public CreateDatabaseResponse createDatabase(String tenant, String name) {
        if (StringUtils.isAnyBlank(tenant, name)) {
            return badParameter(new CreateDatabaseResponse(),
                                "tenant name and database name must not blank");
        }
        return getApiClient().post(ApiPath.databases(tenant), CreateDatabaseResponse.class,
                                   new CreateDatabasePayload().setName(name));
    }

    /**
     * Get a database. Raises an error if the database does not exist.
     */
    public GetDatabaseResponse getDatabase(String tenant, String name) {
        if (StringUtils.isAnyBlank(tenant, name)) {
            return badParameter(new GetDatabaseResponse(),
                                "tenant name and database name must not blank");
        }
        return getApiClient().get(ApiPath.database(tenant, name), GetDatabaseResponse.class);
    }

    /**
     * List databases for a tenant.
     */
    public GetDatabasesResponse getListDatabases(String tenant, int limit, int offset) {
        if (StringUtils.isBlank(tenant)) {
            return badParameter(new GetDatabasesResponse(), "tenant name must not blank");
        }
        String path = ApiPath.withPaging(ApiPath.databases(tenant), limit, offset);
        try (Response response = getApiClient().get(path)) {
            if (!response.isSuccessful()) {
                if (response.body() == null) {
                    throw new ChromaAPIException("Response data is null");
                }
                return (GetDatabasesResponse) getApiClient().getObjectMapper().readValue(response.body().string(),
                                                                                         GetDatabasesResponse.class)
                    .setErrorCode(response.code());
            }
            if (response.body() == null) {
                throw new ChromaAPIException("Response data is null");
            }
            GetDatabaseResponse[] databases = getApiClient().getObjectMapper().readValue(response.body().string(),
                                                                                         GetDatabaseResponse[].class);
            return new GetDatabasesResponse()
                .setDatabases(List.of(databases));
        } catch (IOException exception) {
            return ioException(new GetDatabasesResponse(), exception.getMessage());
        }
    }

    /**
     * Delete a database and all associated collections. Raises an error if the database does not exist.
     */
    public DeleteDatabaseResponse deleteDatabase(String tenant, String name) {
        if (StringUtils.isAnyBlank(tenant, name)) {
            return badParameter(new DeleteDatabaseResponse(),
                                "tenant name and database name must not blank");
        }
        return getApiClient().delete(ApiPath.database(tenant, name), DeleteDatabaseResponse.class);
    }

}
