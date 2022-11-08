package cn.easylib.domain.visual.service;

public class DomainServiceDescriptor {
    private String name;
    private String domainServiceDescription;

    public DomainServiceDescriptor(String name, String domainServiceDescription) {
        this.name = name;
        this.domainServiceDescription = domainServiceDescription;
    }

    public String getName() {
        return name;
    }

    public String getDomainServiceDescription() {
        return domainServiceDescription;
    }
}
