package org.devnation.camel;

import org.apache.camel.Header;
import org.fusesource.insight.storage.StorageService;
import java.sql.Timestamp;
import java.util.Date;

// tag::snippetService[]
public class StoreService {

    private static String ES_TYPE = "insight-tweet";
    private static StorageService storageService;

    public static void store(@Header("tweet-full") String data) {
        storageService.store(ES_TYPE, generateTimeStamp(), data);
    }

    public void setStorageService(StorageService storageService) {
        StoreService.storageService = storageService;
    }
// end::snippetService[]
    public static Long generateTimeStamp() {
        Date date = new java.util.Date();
        return new Timestamp(date.getTime()).getTime();
    }

}