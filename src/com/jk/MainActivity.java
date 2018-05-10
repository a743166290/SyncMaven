package com.jk;

import java.io.File;

public class MainActivity {
    public static void main(String[] args) {
        //找到本地repository
        File mvFile = new File(Config.URL);
        File[] files = mvFile.listFiles();
        Utils.uploadMVFile(files);
    }
}
