package io.github.locxngo.chroma;

import io.github.locxngo.chroma.embedding.EmbeddingFunction;
import io.github.locxngo.chroma.exception.ChromaAPIException;
import io.github.locxngo.chroma.exception.NotFoundException;
import io.github.locxngo.chroma.params.AddCollectionRecordParam;
import io.github.locxngo.chroma.params.DeleteCollectionRecordParam;
import io.github.locxngo.chroma.params.GetCollectionRecordParam;
import io.github.locxngo.chroma.params.Parameter;
import io.github.locxngo.chroma.params.QueryCollectionRecordParam;
import io.github.locxngo.chroma.params.UpdateCollectionParam;
import io.github.locxngo.chroma.schemas.AddCollectionRecordsPayload;
import io.github.locxngo.chroma.schemas.AddCollectionRecordsResponse;
import io.github.locxngo.chroma.schemas.CountResponse;
import io.github.locxngo.chroma.schemas.DeleteCollectionRecordsPayload;
import io.github.locxngo.chroma.schemas.DeleteCollectionRecordsResponse;
import io.github.locxngo.chroma.schemas.ErrorResponse;
import io.github.locxngo.chroma.schemas.ForkCollectionPayload;
import io.github.locxngo.chroma.schemas.GetCollectionResponse;
import io.github.locxngo.chroma.schemas.GetRequestPayload;
import io.github.locxngo.chroma.schemas.GetResponse;
import io.github.locxngo.chroma.schemas.PrintableObject;
import io.github.locxngo.chroma.schemas.QueryRequestPayload;
import io.github.locxngo.chroma.schemas.QueryResponse;
import io.github.locxngo.chroma.schemas.UpdateCollectionPayload;
import io.github.locxngo.chroma.schemas.UpdateCollectionResponse;
import io.github.locxngo.chroma.types.Include;
import io.github.locxngo.chroma.types.QueryAction;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

@Setter
@Getter
@Accessors(chain = true)
@SuppressWarnings({"java:S1192", "java:S6539"})
public class Collection extends PrintableObject {
    private ApiClient apiClient;
    private String tenant;
    private String database;
    private String collectionName;
    private String collectionId;
    private EmbeddingFunction embeddingFunction;

    private ErrorResponse validateNecessaryData(Parameter parameter, boolean requireParameter) {
        if (StringUtils.isAnyBlank(tenant, database, collectionId, collectionName)) {
            return new ErrorResponse()
                .setErrorCode(HttpStatus.SC_BAD_REQUEST)
                .setError(Constants.ERROR_PARAMETER)
                .setMessage("Tenant, database, collection name, and collection id must not blank");
        }
        if (requireParameter && parameter == null) {
            return new ErrorResponse().setErrorCode(HttpStatus.SC_BAD_REQUEST)
                .setError(Constants.ERROR_PARAMETER)
                .setMessage("Parameter must not null");
        }
        return parameter == null ? null : parameter.validate();
    }

    /**
     * Retrieves the number of records in a collection.
     */
    public CountResponse count() {
        ErrorResponse err = validateNecessaryData(null, false);
        if (err != null) {
            return (CountResponse) new CountResponse().setErrorCode(err.getErrorCode())
                .setError(err.getError()).setMessage(err.getMessage());
        }
        try (Response response = apiClient.get(ApiPath.collection(
            tenant, database, collectionId, QueryAction.COUNT.getValue()
        ))) {
            if (!response.isSuccessful()) {
                if (response.body() == null) {
                    throw new ChromaAPIException("Response data is null");
                }
                return (CountResponse) apiClient.getObjectMapper()
                    .readValue(response.body().string(), CountResponse.class)
                    .setErrorCode(response.code());
            }
            if (response.body() == null) {
                throw new ChromaAPIException("Response data is null");
            }
            return new CountResponse()
                .setNumberOfRecords(Long.parseLong(response.body().string()));
        } catch (IOException exception) {
            return (CountResponse) new CountResponse()
                .setErrorCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                .setError(Constants.ERROR_IO)
                .setMessage(exception.getMessage());
        }
    }

    /**
     * Adds records to a collection.
     */
    public AddCollectionRecordsResponse add(AddCollectionRecordParam request) {
        ErrorResponse err = validateNecessaryData(request, true);
        if (err != null) {
            return (AddCollectionRecordsResponse) new CountResponse().setErrorCode(err.getErrorCode())
                .setError(err.getError()).setMessage(err.getMessage());
        }
        AddCollectionRecordsPayload payload = new AddCollectionRecordsPayload()
            .setDocuments(request.getDocuments())
            .setMetadatas(request.getMetadatas())
            .setIds(request.getIds())
            .setUris(request.getUris());
        if (request.getEmbeddings() == null) {
            payload.setEmbeddings(embeddingFunction.embedText(request.getDocuments()));
        } else {
            payload.setEmbeddings(request.getEmbeddings());
        }
        return apiClient.post(ApiPath.collection(tenant, database, collectionId, QueryAction.ADD.getValue()),
                              AddCollectionRecordsResponse.class, payload);
    }

    /**
     * Get the first few results in the database up to given limit
     */
    public GetResponse peek(int limit) {
        return get(GetCollectionRecordParam.builder()
                       .limit(limit)
                       .build());
    }

    /**
     * Update the embeddings, metadatas or documents for provided ids.
     */
    public AddCollectionRecordsResponse update(AddCollectionRecordParam request) {
        ErrorResponse err = validateNecessaryData(request, true);
        if (err != null) {
            return (AddCollectionRecordsResponse) new AddCollectionRecordsResponse()
                .setErrorCode(err.getErrorCode())
                .setError(err.getError())
                .setMessage(err.getMessage());
        }
        AddCollectionRecordsPayload payload = new AddCollectionRecordsPayload()
            .setDocuments(request.getDocuments())
            .setMetadatas(request.getMetadatas())
            .setIds(request.getIds())
            .setUris(request.getUris());
        if (request.getEmbeddings() == null) {
            payload.setEmbeddings(embeddingFunction.embedText(request.getDocuments()));
        } else {
            payload.setEmbeddings(request.getEmbeddings());
        }
        return apiClient.post(ApiPath.collection(tenant, database, collectionId, QueryAction.UPDATE.getValue()),
                              AddCollectionRecordsResponse.class, payload);
    }

    /**
     * Modify the collection name or metadata
     */
    public UpdateCollectionResponse modify(UpdateCollectionParam param) {
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
            ApiPath.collection(tenant, database, getCollectionId()),
            UpdateCollectionResponse.class,
            payload
        );
    }

    /**
     * Update the embeddings, metadatas or documents for provided ids, or create them if they don't exist.
     */
    public AddCollectionRecordsResponse upsert(AddCollectionRecordParam request) {
        ErrorResponse err = validateNecessaryData(request, true);
        if (err != null) {
            return (AddCollectionRecordsResponse) new AddCollectionRecordsResponse()
                .setErrorCode(err.getErrorCode())
                .setError(err.getError())
                .setMessage(err.getMessage());
        }
        AddCollectionRecordsPayload payload = new AddCollectionRecordsPayload()
            .setDocuments(request.getDocuments())
            .setMetadatas(request.getMetadatas())
            .setIds(request.getIds())
            .setUris(request.getUris());
        if (request.getEmbeddings() == null) {
            payload.setEmbeddings(embeddingFunction.embedText(request.getDocuments()));
        } else {
            payload.setEmbeddings(request.getEmbeddings());
        }
        return apiClient.post(ApiPath.collection(tenant, database, collectionId, QueryAction.UPSERT.getValue()),
                              AddCollectionRecordsResponse.class, payload);
    }

    /**
     * Forks an existing collection. Support cloud service only
     */
    public Collection fork(String newName) {
        ForkCollectionPayload payload = new ForkCollectionPayload()
            .setNewName(newName);
        GetCollectionResponse response = apiClient.post(
            ApiPath.collection(tenant, database, collectionId, QueryAction.FORK.getValue()),
            GetCollectionResponse.class, payload
        );
        if (response.isError()) {
            if (response.getErrorCode() == HttpStatus.SC_NOT_FOUND) {
                throw new NotFoundException("Not found collection " + collectionName);
            }
            throw new ChromaAPIException("error = " + response.getError() + ", message = " + response.getMessage());
        }
        return new Collection().setApiClient(getApiClient())
            .setEmbeddingFunction(getEmbeddingFunction())
            .setTenant(tenant)
            .setDatabase(database)
            .setCollectionName(newName)
            .setCollectionId(response.getId());
    }

    /**
     * Delete the embeddings based on ids and/or a where filter
     */
    public DeleteCollectionRecordsResponse delete(DeleteCollectionRecordParam request) {
        ErrorResponse err = validateNecessaryData(request, true);
        if (err != null) {
            return (DeleteCollectionRecordsResponse) new DeleteCollectionRecordsResponse()
                .setErrorCode(err.getErrorCode())
                .setError(err.getError())
                .setMessage(err.getMessage());
        }
        DeleteCollectionRecordsPayload payload = new DeleteCollectionRecordsPayload()
            .setIds(request.getIds())
            .setWhere(request.getWhere() != null ? request.getWhere().getValues() : null)
            .setWhereDocument(request.getWhereDocument() != null ? request.getWhereDocument().getValues() : null);
        return apiClient.post(ApiPath.collection(tenant, database, collectionId, QueryAction.DELETE.getValue()),
                              DeleteCollectionRecordsResponse.class, payload);
    }

    /**
     * Get embeddings and their associate data from the data store. If no ids or where filter is provided returns all
     * embeddings up to limit starting at offset.
     */
    public GetResponse get(GetCollectionRecordParam request) {
        ErrorResponse err = validateNecessaryData(request, true);
        if (err != null) {
            return (GetResponse) new GetResponse().setErrorCode(err.getErrorCode())
                .setError(err.getError()).setMessage(err.getMessage());
        }
        GetRequestPayload payload = new GetRequestPayload()
            .setIds(request.getIds())
            .setInclude(request.getInclude() == null ? Include.defaultList() : request.getInclude())
            .setWhere(request.getWhere() != null ? request.getWhere().getValues() : null)
            .setWhereDocument(request.getWhereDocument() != null ? request.getWhereDocument().getValues() : null)
            .setLimit(request.getLimit())
            .setOffset(request.getOffset());
        return apiClient.post(ApiPath.collection(tenant, database, collectionId, QueryAction.GET.getValue()),
                              GetResponse.class, payload);
    }

    /**
     * Get the n_results nearest neighbor embeddings for provided query_embeddings or query_texts.
     */
    public QueryResponse query(QueryCollectionRecordParam request) {
        ErrorResponse err = validateNecessaryData(request, true);
        if (err != null) {
            return (QueryResponse) new QueryResponse()
                .setError(err.getError())
                .setErrorCode(err.getErrorCode())
                .setMessage(err.getMessage());
        }
        QueryRequestPayload payload = new QueryRequestPayload()
            .setIds(request.getIds())
            .setQueryEmbeddings(embeddingFunction.embedText(request.getQueryTexts()))
            .setNumberOfResults(request.getNumberOfResults())
            .setInclude(request.getInclude() == null ? Include.defaultList() : request.getInclude())
            .setWhere(request.getWhere() != null ? request.getWhere().getValues() : null)
            .setWhereDocument(request.getWhereDocument() != null ? request.getWhereDocument().getValues() : null);
        return apiClient.post(ApiPath.collection(tenant, database, collectionId, QueryAction.QUERY.getValue()),
                              QueryResponse.class, payload);
    }

}
