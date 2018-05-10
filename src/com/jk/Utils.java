package com.jk;

import com.jk.entity.PomEntity;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;

public class Utils {

    public static void uploadMVFile(File[] files){
        //递归找出文件
        for(File file : files){
            if(file.isDirectory()){
                recursionFile(file);
            }else{
                String fileName = file.getName();
                String suffix = getSuffix(fileName);
                if(suffix.endsWith("pom")){
                    //读取pom文件的配置
                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    try {
                        DocumentBuilder builder = factory.newDocumentBuilder();
                        Document doc = builder.parse(file);
                        doc.getDocumentElement().normalize();
                        NodeList nl = doc.getElementsByTagName("project");

                        for (int i = 0; i < nl.getLength(); i++) {
                            Node node = nl.item(i);
                            Element  ele = (Element)node;
                            if(node.getNodeType() == Element.ELEMENT_NODE){

                                  String groupID = ele.getElementsByTagName(Config.GROUPID).item(0).getTextContent();
//                                  System.out.println("groupID = " + groupID);
                                  String version = ele.getElementsByTagName(Config.VERIOSN).item(0).getTextContent();
                                  System.out.println("version = " + version);
                                  String atfid = ele.getElementsByTagName(Config.ATFID).item(0).getTextContent();
//                                  System.out.println("atfid = " + atfid);
                                  if(groupID.isEmpty() || version.isEmpty() || atfid.isEmpty()){
                                      continue;
                                  }
                                  PomEntity entity = new PomEntity(groupID,version,atfid);
                                  uploadJar(file,entity);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
//                break;
            }
        }
    }
    public static void recursionFile(File file){
        File [] files = file.listFiles();
        uploadMVFile(files);
    }

    public static void uploadJar(File file,PomEntity entity){
        String filePath = file.getAbsolutePath();
        filePath = filePath.replace("/"+file.getName(),"");
        File fileJarDirectory = new File(filePath);
        File [] files = fileJarDirectory.listFiles();
        for(File fileJar : files){
            String suffix = getSuffix(fileJar.getName());
            if(suffix.equals("jar")){
                //生成批量.sh文件
                File shFile = new File(Config.FILEPATHSH);
                if(shFile.exists()){
                    shFile.delete();
                }
                try {
                    shFile.createNewFile();
                    //写入批量任务 先进入到文件地址--》执行上传命令
                    String mvnString = "mvn deploy:deploy-file" +
                            " -DgroupId=" + entity.getGoupID()+
                            " -DartifactId=" +entity.getAfID()+
                            " -Dversion=" +entity.getVersion()+
                            " -Dpackaging=jar " +
                            " -Dfile=" + fileJar.getAbsolutePath()+
                            " -Durl="+Config.MVPATH +
                            " -DrepositoryId="+Config.MVPATHID+
                            " -Dpackaging=jar";
                    System.out.println(mvnString);
                    StringBuilder builder = new StringBuilder();
                    builder.append("cd "+filePath);
                    builder.append("\n");
                    builder.append(mvnString);
                    whriteFileContent(builder.toString(),shFile);
                    UnixScript.mavenUpload();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static void whriteFileContent(String fileContent, File file) throws IOException{
        FileOutputStream writerStream = new FileOutputStream(file);
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(writerStream, "UTF-8"));
        writer.write(fileContent);
//        writer.flush();
        writer.close();
    }
    public static String getSuffix(String fileName){
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}
