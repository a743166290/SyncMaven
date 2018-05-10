package com.jk;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class UnixScript {
    public static void useLinuxCommond(String command){
        try {
            Process pro = Runtime.getRuntime().exec(command);
            printLines(command + " stdout:", pro.getInputStream());
            printLines(command + " stderr:", pro.getErrorStream());
            pro.waitFor();
            System.out.println(command + " exitValue() " + pro.exitValue());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void mavenUpload(){
        useLinuxCommond("sh "+Config.FILEPATHSH);
    }
    private static void printLines(String cmd, InputStream ins) throws Exception {
        String line = null;
        BufferedReader in = new BufferedReader(
                new InputStreamReader(ins));
        while ((line = in.readLine()) != null) {
            System.out.println(cmd + " " + line);
        }
    }
}
