package com.example.spartdemo;

import org.apache.commons.lang3.RandomUtils;
import org.apache.spark.storage.StorageLevel;
import org.apache.spark.streaming.receiver.Receiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author shenzm
 * @date 2019-5-17
 * @description 作用
 */
public class CustomReceiver extends Receiver<String> {

    private static Logger logger = LoggerFactory.getLogger(CustomReceiver.class);


    /**
     *
     * @author	hz15041240
     * @date	2018年1月18日 下午4:37:22
     * @version
     */
    private static final long serialVersionUID = 5817531198342629801L;

    public CustomReceiver(StorageLevel storageLevel) {
        super(storageLevel);
    }

    @Override
    public void onStart() {
        new Thread(this::doStart).start();
        logger.info("开始启动Receiver...");
        //doStart();
    }

    public void doStart() {
        while(!isStopped()) {
            int value = RandomUtils.nextInt(50,100);
            if(value <20) {
                try {
                    Thread.sleep(1000);
                }catch (Exception e) {
                    logger.error("sleep exception",e);
                    restart("sleep exception", e);
                }
            }
            store(String.valueOf(value));
        }
    }


    @Override
    public void onStop() {
        logger.info("即将停止Receiver...");
    }

}
