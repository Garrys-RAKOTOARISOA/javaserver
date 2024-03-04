package com.example.demo5.models;

public class QrCodeModel {
    private long moduleId;
    private String nomModule;

    public QrCodeModel(ModuleSolar moduleSolar){
        this.moduleId = moduleSolar.getId();
        this.nomModule = moduleSolar.getNommodule();
    }

    public long getModuleId() {
        return moduleId;
    }

    public void setModuleId(long moduleId) {
        this.moduleId = moduleId;
    }

    public String getNomModule() {
        return nomModule;
    }

    public void setNomModule(String nomModule) {
        this.nomModule = nomModule;
    }
}
