package com.hll.gmall.manager.utils;

import com.hll.gmall.api.constant.Constants;
import org.apache.commons.lang3.StringUtils;
import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class PmsUploadUtil {
    public static String uploadImage(MultipartFile file) {
        String[] jpegs = new String[]{};
        try {
            String path = PmsUploadUtil.class.getResource("/tracker.conf").getPath();
            ClientGlobal.init(path);

            TrackerClient trackerClient = new TrackerClient();
            TrackerServer trackerServer = trackerClient.getTrackerServer();
            StorageClient storageClient = new StorageClient();
            storageClient.setTrackerServer(trackerServer);

            String originalFilename = file.getOriginalFilename();
            String extName = "";
            if (StringUtils.isNotBlank(originalFilename)) {
                extName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            }

            jpegs = storageClient.upload_file(file.getBytes(), extName, null);
        } catch (IOException | MyException e) {
            e.printStackTrace();
        }

        StringBuilder imageUrl = new StringBuilder(Constants.TRACKER_SERVER_ADDRESS);
        for (String jpeg : jpegs) {
            imageUrl.append("/").append(jpeg);
        }

        return imageUrl.toString();
    }
}
