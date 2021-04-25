package com.hll.gmall.manager;

import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class GmallManagerWebApplicationTests {

    @Test
    void contextLoads() throws IOException, MyException {
        ClientGlobal.init("D:/Documents/idea-projects/gmall/gmall-manager-web/target/classes/tracker.conf");

        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getTrackerServer();
        StorageClient storageClient = new StorageClient();
        storageClient.setTrackerServer(trackerServer);

        String[] jpegs = storageClient.upload_file("C:/Users/Jon Han/Pictures/97.jpeg", "jpeg", null);
        for (String jpeg : jpegs) {
            System.out.println(jpeg);
        }
    }

}
