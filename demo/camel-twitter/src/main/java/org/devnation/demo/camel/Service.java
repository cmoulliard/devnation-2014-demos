package org.devnation.demo.camel;

//import io.fabric8.insight.storage.StorageService;
import org.fusesource.insight.storage.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.Date;

public class Service {

    private static final Logger LOGGER = LoggerFactory.getLogger(Service.class);

    private static StorageService storageService;
    private static String ES_TYPE = "tweet";

    public static void store(String data) {
        storageService.store(ES_TYPE,generateTimeStamp(),data);
    }

    public static String tweetToJSON(String message) {
        StringBuilder builder = new StringBuilder();
        builder.append("{\"tweet\":\"");
        builder.append(message);
        builder.append("\"}");
        return builder.toString();
    }

    public static Long generateTimeStamp() {
        Date date= new java.util.Date();
        return new Timestamp(date.getTime()).getTime();
    }

    public void setStorageService(StorageService storageService) {
        Service.storageService = storageService;
    }

}
