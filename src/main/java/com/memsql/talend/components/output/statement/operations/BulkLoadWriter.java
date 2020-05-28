package com.memsql.talend.components.output.statement.operations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Date;

public class BulkLoadWriter {
    private static BulkLoadWriter instance = null;
    private String filePath = null;
    private File dir = new File("." + File.separator + "memsql_bulk_loader");
    private BufferedWriter bulkFile;
    private static final transient Logger LOG = LoggerFactory.getLogger(BulkLoadWriter.class);

    public synchronized static  BulkLoadWriter getInstance(String tableName) {
        //if (instance == null)
            //instance = new BulkLoadWriter(tableName);

        //return instance;
        return new BulkLoadWriter(tableName);
    }

    private BulkLoadWriter(String tableName) {
        String path = ".";

        try {
            if (!dir.exists())
                dir.mkdir();
            path = dir.getCanonicalPath();
            
            filePath = path + File.separator + tableName +
                    "_" + new Date().getTime() + ".csv";
            if (filePath.contains("\\"))
                filePath = filePath.replaceAll("\\\\", "/");
            if (filePath.startsWith(dir.getCanonicalPath()))
            {
                FileWriter fileWriter = new FileWriter(new File(filePath), true);
                bulkFile = new BufferedWriter(fileWriter);
            } else {
                throw new IOException("Default CanonicalPath has been altered.");
            }
        } catch(IOException e) {
            LOG.error(e.getMessage());
        }
    }

    public void write(String data) {
        try {
            this.bulkFile.write(data);
            this.bulkFile.flush();
        } catch (IOException e) {
            LOG.error(e.getMessage());
        }

    }

    public void close() {
        try {
            this.bulkFile.close();
        } catch (IOException e) {
            LOG.error(e.getMessage());
        }
    }

    public String getFilePath() {
        return filePath;
    }

    public void delete() {
        if (new File(filePath).exists())
            new File(filePath).delete();
    }
}
