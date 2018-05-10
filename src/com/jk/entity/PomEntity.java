package com.jk.entity;

public class PomEntity {
    private String goupID;
    private String Version;
    private String afID;
    public PomEntity(String goupID,String Version,String afID){
        this.goupID = goupID;
        this.Version = Version;
        this.afID = afID;
    }

    public String getAfID() {
        return afID;
    }

    public String getGoupID() {
        return goupID;
    }

    public String getVersion() {
        return Version;
    }
}
