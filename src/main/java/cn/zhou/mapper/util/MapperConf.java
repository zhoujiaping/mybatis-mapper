package cn.zhou.mapper.util;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MapperConf {
    private String namespace;
    private ResMap idResMap;
    private List<ResMap> resMaps;
    private String tablename;
    private String idType;
    
    public String getNamespace() {
        return namespace;
    }
    
    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }
    public ResMap getIdResMap() {
        return idResMap;
    }
    
    public void setIdResMap(ResMap idResMap) {
        this.idResMap = idResMap;
    }
    
    public List<ResMap> getResMaps() {
        return resMaps;
    }
    
    public void setResMaps(List<ResMap> resMaps) {
        this.resMaps = resMaps;
    }
    
    public String getTablename() {
        return tablename;
    }
    
    public void setTablename(String tablename) {
        this.tablename = tablename;
    }
    public Set<String> getColumns(){
        return resMaps.stream().map(x->x.getColumn()).collect(Collectors.toSet());
    }
    public Set<String> getProperties(){
        return resMaps.stream().map(x->x.getProperty()).collect(Collectors.toSet());
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }
    
    public String getIdType() {
        return idType;
    }
    
}
