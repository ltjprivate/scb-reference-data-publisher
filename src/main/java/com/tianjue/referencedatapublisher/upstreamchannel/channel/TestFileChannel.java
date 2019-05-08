package com.tianjue.referencedatapublisher.upstreamchannel.channel;

import java.io.File;
import java.util.Scanner;

public class TestFileChannel extends DataSourceChannel {

    private final String filePath;
    private Thread thread = new Thread(new TestFileChannelThread());

    public TestFileChannel(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void start() {
        thread.start();
    }

    class TestFileChannelThread implements Runnable {
        @Override
        public void run() {
            try (Scanner scanner = new Scanner(new File(filePath))){
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    if(line.startsWith("sleep")) {
                        String[] sleepStr = line.split(" ");
                        int sleep = Integer.valueOf(sleepStr[1]);
                        Thread.sleep(sleep);
                    } else {
                        dataProviderListener.onData(line, null);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
