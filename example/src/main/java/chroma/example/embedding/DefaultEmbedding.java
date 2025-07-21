package chroma.example.embedding;

import io.github.locxngo.chroma.Client;
import io.github.locxngo.chroma.Collection;
import io.github.locxngo.chroma.Config;
import io.github.locxngo.chroma.params.AddCollectionRecordParam;
import io.github.locxngo.chroma.params.CreateCollectionParam;
import io.github.locxngo.chroma.params.QueryCollectionRecordParam;
import io.github.locxngo.chroma.schemas.QueryResponse;
import io.github.locxngo.chroma.types.Metadata;

import java.util.List;
import java.util.Map;

@SuppressWarnings("java:S106")
public class DefaultEmbedding {

    public static void main(String[] args) {
        try {
            Client client = new Client(Config.defaultConfig());
            client.reset();
            // in case reset is not allowed, force remove existing collection
            client.deleteCollection("test_collection");
            Collection collection = client.createCollection(
                CreateCollectionParam.builder().name("test_collection").getOrCreate(true).build()
            );
            collection.upsert(
                AddCollectionRecordParam.builder().ids(List.of("id1", "id2"))
                    .documents(List.of(
                        "This is a document about pineapple",
                        "This is a document about oranges")
                    )
                    .metadatas(List.of(
                        Metadata.from(Map.of(
                            "color", "yellow", "sku", "SKU001", "price", 1.0f
                        )),
                        Metadata.from(Map.of(
                            "color", "orange", "sku", "SKU002", "price", 1.5f
                        ))
                    ))
                    .build()
            );

            QueryResponse queryResponse = collection.query(
                QueryCollectionRecordParam.builder()
                    .queryTexts(List.of("This is a query document about florida"))
                    .numberOfResults(5)
                    .build()
            );
            System.out.println(queryResponse);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
