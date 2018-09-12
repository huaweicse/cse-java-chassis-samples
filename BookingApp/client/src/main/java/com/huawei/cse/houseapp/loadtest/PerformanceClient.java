package com.huawei.cse.houseapp.loadtest;

import org.apache.servicecomb.foundation.common.utils.BeanUtils;
import org.apache.servicecomb.foundation.common.utils.Log4jUtils;

public class PerformanceClient {
    public static void main(String[] args) throws Exception {
        init();
    }

    public static void init() throws Exception {
        Log4jUtils.init();
        BeanUtils.init(BeanUtils.DEFAULT_BEAN_RESOURCE, "classpath*:META-INF/spring/*.client.xml");
    }

}
