package io.github.locxngo.chroma;

import io.github.locxngo.chroma.embedding.DefaultEmbeddingFunction;
import io.github.locxngo.chroma.embedding.EmbeddingFunction;
import io.github.locxngo.chroma.embedding.EmbeddingFunctionFactory;
import io.github.locxngo.chroma.embedding.EmbeddingOption;
import io.github.locxngo.chroma.exception.ChromaAPIException;
import io.github.locxngo.chroma.exception.ChromaConnectException;
import io.github.locxngo.chroma.exception.InvalidParameterException;
import io.github.locxngo.chroma.exception.NotFoundException;
import io.github.locxngo.chroma.params.CreateCollectionParam;
import io.github.locxngo.chroma.params.GetCollectionParam;
import io.github.locxngo.chroma.params.UpdateCollectionParam;
import io.github.locxngo.chroma.schemas.CollectionConfiguration;
import io.github.locxngo.chroma.schemas.CountResponse;
import io.github.locxngo.chroma.schemas.CreateCollectionPayload;
import io.github.locxngo.chroma.schemas.DeleteCollectionRecordsResponse;
import io.github.locxngo.chroma.schemas.EmbeddingFunctionConfig;
import io.github.locxngo.chroma.schemas.ErrorResponse;
import io.github.locxngo.chroma.schemas.GetCollectionResponse;
import io.github.locxngo.chroma.schemas.UpdateCollectionPayload;
import io.github.locxngo.chroma.schemas.UpdateCollectionResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

@Setter
@Getter
@Accessors(chain = true)
@SuppressWarnings("java:S1192")
public class Client extends BaseClient {
    /**
     * Set the tenant and database for the client.
     */
    private String tenant;
    /**
     * Set the database for the client.
     */
    private String database;

    public Client(Config config) {
        super(config);
        this.database = config.getDatabase();
        this.tenant = config.getTenant();
    }

    private EmbeddingFunction fromConfig(EmbeddingFunctionConfig efc, EmbeddingFunction ef) {
        if (efc == null) {
            return ef;
        }
        String efType = efc.getType();
        if (StringUtils.isBlank(efType) || "legacy".equals(efType)) {
            return ef;
        } else if ("known".equals(efType)) {
            String efName = efc.getName();
            if (StringUtils.isBlank(efName)) {
                return ef;
            }
            if (!efName.equals(ef.name())) {
                throw new ChromaAPIException(
                    "Embedding function conflict: persisted = " + efName + ", new = " + ef.name()
                );
            }
            return EmbeddingFunctionFactory.get(efName, efc.getConfig());
        } else {
            throw new ChromaAPIException("unknown embedding config type: " + efType);
        }
    }

    /**
     * Count the number of collections.
     */
    public CountResponse countCollections() {
        if (StringUtils.isAnyBlank(tenant, database)) {
            return badParameter(new CountResponse(),
                                "tenant name and database name must not blank");
        }
        try (Response response = getApiClient().get(ApiPath.countCollection(tenant, database))) {
            if (!response.isSuccessful()) {
                if (response.body() == null) {
                    throw new ChromaAPIException("Response data is null");
                }
                return (CountResponse) getApiClient().getObjectMapper()
                    .readValue(response.body().string(), CountResponse.class)
                    .setErrorCode(response.code());
            }
            if (response.body() == null) {
                throw new ChromaAPIException("Response data is null");
            }
            return new CountResponse()
                .setNumberOfRecords(Long.parseLong(response.body().string()));
        } catch (IOException exception) {
            return ioException(new CountResponse(), exception.getMessage());
        }
    }

    /**
     * Get a list of collections of current tenant and database.
     */
    public List<Collection> getListCollections(int limit, int offset) {
        if (StringUtils.isAnyBlank(tenant, database)) {
            throw new InvalidParameterException("Tenant, database name must not blank");
        }
        String path = ApiPath.withPaging(
            ApiPath.collections(tenant, database),
            limit, offset
        );
        try (Response response = getApiClient().get(path)) {
            if (!response.isSuccessful()) {
                if (response.body() == null) {
                    throw new ChromaAPIException("Response data is null");
                }
                throw new ChromaAPIException(response.body().string());
            }
            if (response.body() == null) {
                throw new ChromaAPIException("Response data is null");
            }
            GetCollectionResponse[] array = getApiClient().getObjectMapper().readValue(response.body().string(),
                                                                                       GetCollectionResponse[].class);
            return Stream.of(array)
                .map(item -> new Collection().setApiClient(getApiClient())
                    .setTenant(tenant)
                    .setDatabase(database)
                    .setCollectionName(item.getName())
                    .setCollectionId(item.getId())).toList();
        } catch (IOException exception) {
            throw new ChromaConnectException(exception);
        }
    }

    /**
     * Create a new collection with the given name and metadata.
     */
    public Collection createCollection(CreateCollectionParam param) {
        if (StringUtils.isAnyBlank(tenant, database)) {
            throw new InvalidParameterException("Tenant, database, and collection name must not blank");
        }
        ErrorResponse err = param.validate();
        if (err != null) {
            throw new InvalidParameterException(err.getMessage());
        }
        EmbeddingFunction ef = param.getEmbeddingFunction() == null
                               ? new DefaultEmbeddingFunction(EmbeddingOption.empty())
                               : param.getEmbeddingFunction();
        CollectionConfiguration cf = param.getConfig() == null
                                     ? new CollectionConfiguration() : param.getConfig();
        if (cf.getEmbeddingFunction() == null) {
            cf.setEmbeddingFunction(ef.toConfig());
        }
        CreateCollectionPayload payload = new CreateCollectionPayload()
            .setConfiguration(cf)
            .setGetOrCreate(param.isGetOrCreate())
            .setMetadata(param.getMetadata())
            .setName(param.getName());
        GetCollectionResponse response = getApiClient().post(
            ApiPath.collections(tenant, database),
            GetCollectionResponse.class,
            payload
        );
        if (response.isError()) {
            throw new ChromaAPIException("error = " + response.getError() + ", message = " + response.getMessage());
        }
        EmbeddingFunctionConfig efc = response.getConfigurationJson().getEmbeddingFunction();
        return new Collection().setApiClient(getApiClient())
            .setEmbeddingFunction(fromConfig(efc, ef))
            .setTenant(tenant)
            .setDatabase(database)
            .setCollectionName(param.getName())
            .setCollectionId(response.getId());
    }

    /**
     * Get a collection with the given name.
     */
    public Collection getCollection(GetCollectionParam param) {
        if (StringUtils.isAnyBlank(tenant, database)) {
            throw new InvalidParameterException("Tenant, database must not blank");
        }
        ErrorResponse error = param.validate();
        if (error != null) {
            throw new InvalidParameterException(error.getMessage());
        }
        EmbeddingFunction ef = param.getEmbeddingFunction() == null
                               ? new DefaultEmbeddingFunction(EmbeddingOption.empty())
                               : param.getEmbeddingFunction();
        GetCollectionResponse response = getApiClient().get(
            ApiPath.collection(tenant, database, param.getNameOrId()),
            GetCollectionResponse.class);
        if (response.isError()) {
            if (response.getErrorCode() == HttpStatus.SC_NOT_FOUND) {
                throw new NotFoundException("Not found collection " + param.getNameOrId());
            }
            throw new ChromaAPIException("error = " + response.getError() + ", message = " + response.getMessage());
        }
        EmbeddingFunctionConfig efc = response.getConfigurationJson().getEmbeddingFunction();
        return new Collection().setApiClient(getApiClient())
            .setEmbeddingFunction(fromConfig(efc, ef))
            .setTenant(tenant)
            .setDatabase(database)
            .setCollectionName(param.getNameOrId())
            .setCollectionId(response.getId());
    }

    /**
     * Delete a collection with the given name.
     */
    public DeleteCollectionRecordsResponse deleteCollection(String nameOrId) {
        if (StringUtils.isAnyBlank(tenant, database, nameOrId)) {
            throw new InvalidParameterException("Tenant, database, and collection name must not blank");
        }
        return getApiClient().delete(ApiPath.collection(tenant, database, nameOrId),
                                     DeleteCollectionRecordsResponse.class);
    }

    /**
     * Update the embeddings, metadatas or documents for provided ids.
     */
    public UpdateCollectionResponse updateCollection(UpdateCollectionParam param) {
        ErrorResponse err = param.validate();
        if (err != null) {
            return (UpdateCollectionResponse) new UpdateCollectionResponse()
                .setErrorCode(err.getErrorCode())
                .setMessage(err.getMessage())
                .setErrorCode(err.getErrorCode());
        }
        UpdateCollectionPayload payload = new UpdateCollectionPayload()
            .setName(param.getNewName())
            .setMetadata(param.getMetadata());
        return getApiClient().post(
            ApiPath.collection(tenant, database, param.getCollectionId()),
            UpdateCollectionResponse.class,
            payload
        );
    }


}
