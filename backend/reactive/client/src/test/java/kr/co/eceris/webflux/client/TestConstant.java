package kr.co.eceris.webflux.client;

public final class TestConstant {

    public static String API_FILE_URI = "/file";
    public static String API_DB_FIND_URI = "/db/find";
    public static String API_DB_SAVE_URI = "/db/save";
    public static String API_REST_URI = "/rest/call";
    public static String API_SERVICE_URI = "/service";

    private TestConstant() {
        throw new AssertionError("No kr.co.eceris.webflux.client.TestConstant instances for you!");
    }
}
