package org.webdriver.patatiumwebui.common;

import org.webdriver.patatiumwebui.exception.RPAException;
import org.webdriver.patatiumwebui.httprequest.vo.CorpTaxQueueVO;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 点击启动时,想立即启动报税,因此用
 * objects.poll(30, TimeUnit.MINUTES);
 * 来周期性地运行,
 */
public class RPAQueue {
    private static LinkedBlockingQueue<CorpTaxQueueVO> objects = new LinkedBlockingQueue<>();

    public static CorpTaxQueueVO take() {
        try {
            return objects.take();
        } catch (InterruptedException e) {
            throw new RPAException(e);
        }
    }

    public static CorpTaxQueueVO poll(long timeout, TimeUnit unit) {
        try {
            return objects.poll(timeout, unit);
        } catch (InterruptedException e) {
            throw new RPAException(e);
        }
    }

    public static void put(CorpTaxQueueVO object) {
        try {
            objects.put(object);
        } catch (InterruptedException e) {
            throw new RPAException(e);
        }
    }
}
