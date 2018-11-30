package com.huawei.cse.porter.gateway;

import java.io.File;

import org.apache.servicecomb.foundation.ssl.SSLCustom;

public class EdgeSSLCustom extends SSLCustom {

    @Override
    public char[] decode(char[] plain) {
        return plain;
    }

    @Override
    public String getFullPath(String name) {
        String fullName = System.getProperty("user.dir") + "/src/main/resources/" + name;
        System.out.println(fullName);
        return (new File(fullName)).getAbsolutePath();
    }

}
