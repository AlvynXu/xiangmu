package com.guangxuan.util;

import com.guangxuan.constant.ItemStatus;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author deofly
 * @since 2019-05-22
 */
public class ItemUtils {

    private static final Logger logger = LoggerFactory.getLogger(ItemUtils.class);

    public static boolean isStatusValid(int status) {
        return ArrayUtils.contains(new int[] {
                ItemStatus.NOT_AUDIT,
                ItemStatus.OFF_SHELVE,
                ItemStatus.ON_SALE
        }, status);
    }
}
