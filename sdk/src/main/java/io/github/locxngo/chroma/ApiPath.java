package io.github.locxngo.chroma;

public class ApiPath {
    private ApiPath() {
    }

    public static final String IDENTITY = "/api/v2/auth/identity";
    public static final String HEALTH_CHECK = "/api/v2/healthcheck";
    public static final String HEART_BEAT = "/api/v2/heartbeat";
    public static final String RESET = "/api/v2/reset";
    public static final String PRE_FLIGHT_CHECKS = "/api/v2/pre-flight-checks";
    public static final String TENANTS = "/api/v2/tenants";
    public static final String VERSION = "/api/v2/version";

    public static String withPaging(String path, int limit, int offset) {
        return new StringBuilder(path)
            .append(limit > 0 || offset > 0 ? "?" : "")
            .append(limit > 0 ? "limit=" + limit + "&" : "")
            .append(offset > 0 ? "offset=" + offset : "")
            .toString();
    }

    public static String tenant(String tenant) {
        return new StringBuilder(TENANTS).append("/").append(tenant).toString();
    }

    public static String databases(String tenant) {
        return new StringBuilder(TENANTS).append("/")
            .append(tenant).append("/databases").toString();
    }

    public static String database(String tenant, String database) {
        return new StringBuilder(TENANTS).append("/")
            .append(tenant).append("/databases/").append(database).toString();
    }

    public static String collections(String tenant, String database) {
        return new StringBuilder(TENANTS).append("/")
            .append(tenant).append("/databases/").append(database)
            .append("/collections").toString();
    }

    public static String collection(String tenant, String database, String collection) {
        return new StringBuilder(TENANTS).append("/")
            .append(tenant).append("/databases/").append(database)
            .append("/collections/").append(collection).toString();
    }

    public static String collection(String tenant, String database, String collectionId, String action) {
        return new StringBuilder(TENANTS).append("/")
            .append(tenant).append("/databases/").append(database)
            .append("/collections/").append(collectionId).append("/").append(action).toString();
    }

    public static String countCollection(String tenant, String database) {
        return new StringBuilder(TENANTS).append("/")
            .append(tenant).append("/databases/").append(database)
            .append("/collections_count").toString();
    }
}

