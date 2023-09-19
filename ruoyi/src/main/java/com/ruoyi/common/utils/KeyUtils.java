package com.ruoyi.common.utils;


import java.util.Random;

public class KeyUtils {


    /**
     * 主键生成，雪花ID
     *
     * @return String
     */
    public static synchronized String genUniqueKey() {
        return IdUtils.snowflakeId().toString();
    }


    public static synchronized Integer genComCode() {
        Random random = new Random();
        return random.nextInt(90000) + 10000;
    }


}
