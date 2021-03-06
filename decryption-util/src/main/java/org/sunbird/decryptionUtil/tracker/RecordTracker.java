package org.sunbird.decryptionUtil.tracker;

import org.apache.log4j.Logger;
import org.sunbird.decryptionUtil.LoggerFactory;
import org.sunbird.decryptionUtil.constants.EnvConstants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * this class will be used to read records which are pre processed
 * and list of preprocessed records is present isn preProcessedRecords.txt
 */
public class RecordTracker {

    private static Logger logger = LoggerFactory.getLoggerInstance(RecordTracker.class.getName());
    public static List<String> getPreProcessedRecordsAsList() throws IOException {
        List<String> successfullRecordsList = new ArrayList<>();
        File file = new File(EnvConstants.PRE_PROCESSED_RECORDS_FILE);
        printFilelastModified(file);
        if (file.exists()) {
            successfullRecordsList = readFileGetList(file);
        } else {
            file.createNewFile();
        }
        return successfullRecordsList;
    }


    /**
     * this method will remove the last index value from the list
     * since it may be possible that the last value may be not fully written by writer..
     * @param file
     * @return
     */
    private static List<String> readFileGetList(File file) {
        List<String> preProcessedRecords = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                preProcessedRecords.add(line);
            }
        } catch (IOException e) {
            logger.error(String.format("no file found named %s creating it again....",EnvConstants.PRE_PROCESSED_RECORDS_FILE));
        }
        return preProcessedRecords;
    }

    private static void printFilelastModified(File file) {
        String dateFormat="MM/dd/yyyy HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        logger.info(String.format("%s last modified at %s", EnvConstants.PRE_PROCESSED_RECORDS_FILE,sdf.format(file.lastModified())));
    }
}
