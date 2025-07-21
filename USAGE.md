## Usage

### Prerequisite

Ensure you have a running instance of Chroma running. We recommend one of the two following options:

- Official documentation - https://docs.trychroma.com/guides/deploy/client-server-mode

### Initialize Client

Instance of client could be initialized via
```java
Client client = new Client(Config.defaultConfig());
```

By using `defaultConfig`, client will connect to `http://localhost:8000` and `default_tenant`, `default_database`. 
For specifying those values, you can use:
```java
Client client = new Client(
    Config.builder().endpoint("").tenant("").database("").build()
);
```

### Default Embedding Function

```java
public class DefaultEmbedding {

    public static void main(String[] args) {
        try {
            Client client = new Client(Config.defaultConfig());
            client.reset();
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
```

Output should be
```json
{
    "distances": null,
    "documents": [
        [
            "This is a document about oranges",
            "This is a document about pineapple"
        ]
    ],
    "embeddings": null,
    "ids": [
        [
            "id2",
            "id1"
        ]
    ],
    "include": [
        "documents",
        "metadatas"
    ],
    "metadatas": [
        [
            {
                "color": "orange",
                "price": 1.5,
                "sku": "SKU002"
            },
            {
                "color": "yellow",
                "price": 1,
                "sku": "SKU001"
            }
        ]
    ],
    "uris": null
}
```

### Example OpenAI Embedding Function

In this example we rely on `io.github.locxngo.chroma.OpenAIEmbeddingFunction` to generate embeddings for our documents.

```java
public class OpenAIEmbedding {

    public static void main(String[] args) {
        try {
            Client client = new Client(Config.defaultConfig());
            client.reset();
            // in case reset is not allowed, force remove existing collection
            client.deleteCollection("test_collection");
            EmbeddingFunction ef = new OpenAIEmbeddingFunction(
                EmbeddingOption.builder()
                    .apiKey(System.getenv("OPENAI_API_KEY"))
                    .model(System.getenv("OPENAI_MODEL"))
                    .build()
            );
            Collection collection = client.createCollection(
                CreateCollectionParam.builder().name("test_collection").getOrCreate(true)
                    .embeddingFunction(ef).build()
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
```

The above should output:

```json
{
    "distances": null,
    "documents": [
        [
            "This is a document about oranges",
            "This is a document about pineapple"
        ]
    ],
    "embeddings": null,
    "ids": [
        [
            "id2",
            "id1"
        ]
    ],
    "include": [
        "documents",
        "metadatas"
    ],
    "metadatas": [
        [
            {
                "color": "orange",
                "price": 1.5,
                "sku": "SKU002"
            },
            {
                "color": "yellow",
                "price": 1,
                "sku": "SKU001"
            }
        ]
    ],
    "uris": null
}
```
