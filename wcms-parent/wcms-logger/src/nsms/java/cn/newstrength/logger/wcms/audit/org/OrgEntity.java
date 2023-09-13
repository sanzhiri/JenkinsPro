package cn.newstrength.logger.wcms.audit.org;

public class OrgEntity {

    private String orgName;

    public OrgEntity() {
    }

    public OrgEntity(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    @Override
    public String toString() {
        return "OrgStep{" +
                "orgName='" + orgName + '\'' +
                '}';
    }
}
