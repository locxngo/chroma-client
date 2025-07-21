package chroma.example;

import io.github.locxngo.chroma.Client;
import io.github.locxngo.chroma.Collection;
import io.github.locxngo.chroma.Config;
import io.github.locxngo.chroma.params.AddCollectionRecordParam;
import io.github.locxngo.chroma.params.CreateCollectionParam;
import io.github.locxngo.chroma.params.GetCollectionRecordParam;
import io.github.locxngo.chroma.params.QueryCollectionRecordParam;
import io.github.locxngo.chroma.schemas.AddCollectionRecordsResponse;
import io.github.locxngo.chroma.schemas.CountResponse;
import io.github.locxngo.chroma.schemas.GetResponse;
import io.github.locxngo.chroma.schemas.QueryResponse;
import io.github.locxngo.chroma.types.Include;
import io.github.locxngo.chroma.types.Metadata;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@SuppressWarnings("java:S2094")
public class CollectionAPI extends BaseAPI {

    public static void main(String[] args) {
        Client client = new Client(
            Config.defaultConfig()
        );
        Collection sampleCollection = client.createCollection(CreateCollectionParam.builder()
                                                                  .name("sample_collection_v1")
                                                                  .getOrCreate(true)
                                                                  .metadata(Metadata.from(
                                                                      Map.of("date", new Date().toString())))
                                                                  .build());
        CountResponse countResponse = client.countCollections();
        System.out.println("Number of collection: " + countResponse.getNumberOfRecords());
        List<Collection> collections = client.getListCollections((int) countResponse.getNumberOfRecords(), 0);
        for (Collection collection : collections) {
            System.out.println(
                "Collection: " + collection.getCollectionName() + "[" + collection.getCollectionId() + "]");
        }

        AddCollectionRecordsResponse addResponse = sampleCollection.upsert(
            AddCollectionRecordParam.builder()
                .ids(List.of("id1", "id2"))
                .documents(List.of(
                    "This is a document about pineapple",
                    "This is a document about oranges")
                )
                .metadatas(List.of(
                    Metadata.from(Map.of("color", "yellow", "sku", "SKU001")),
                    Metadata.from(Map.of("color", "orange", "sku", "SKU002"))
                ))
                .build()
        );
        throwErrorIfHas(addResponse);

        CountResponse countCollection = sampleCollection.count();
        System.out.println("Number of records: " + countCollection.getNumberOfRecords());

        GetResponse pineapple = sampleCollection.get(GetCollectionRecordParam.builder()
                                                         .ids(List.of("id1"))
                                                         .include(Stream.of(Include.values())
                                                                      .map(Include::getValue)
                                                                      .toList())
                                                         .build());
        throwErrorIfHas(pineapple);
        System.out.println("==== get record that has id = id1");
        System.out.println("Record Id:" + pineapple.getIds().get(0));
        System.out.println("Document: " + pineapple.getDocuments().get(0));
        System.out.println("Embedding:" + pineapple.getEmbeddings().get(0).subList(0, 10) + "...");
        System.out.println("Metadata: " + pineapple.getMetadatas().get(0));

        var texts = List.of("This is a query document about florida");
        System.out.println("==== find similarity for input text: " + texts);
        var nResults = 2;
        QueryResponse queryResponse = sampleCollection.query(QueryCollectionRecordParam.builder()
                                                                 .queryTexts(texts)
                                                                 .numberOfResults(nResults)
                                                                 .build()
        );

        int totalRecords = texts.size();
        for (int i = 0; i < totalRecords; i++) {
            for (int j = 0; j < nResults; j++) {
                System.out.println("-- Record " + (j + 1));
                System.out.println("Id: " + queryResponse.getIds().get(i).get(j));
                System.out.println("Document: " + queryResponse.getDocuments().get(i).get(j));
                System.out.println("Distance: " + queryResponse.getDistances().get(i).get(j));
                System.out.println("Metadata: " + queryResponse.getMetadatas().get(i).get(j));
            }
        }

        texts = List.of("This is a document about oranges");
        System.out.println("==== find similarity for input text: " + texts);
        queryResponse = sampleCollection.query(QueryCollectionRecordParam.builder()
                                                   .queryTexts(texts)
                                                   .numberOfResults(nResults)
                                                   .build()
        );
        for (int i = 0; i < totalRecords; i++) {
            for (int j = 0; j < nResults; j++) {
                System.out.println("-- Record " + (j + 1));
                System.out.println("Id: " + queryResponse.getIds().get(i).get(j));
                System.out.println("Document: " + queryResponse.getDocuments().get(i).get(j));
                System.out.println("Distance: " + queryResponse.getDistances().get(i).get(j));
                System.out.println("Metadata: " + queryResponse.getMetadatas().get(i).get(j));
            }
        }
    }
}
