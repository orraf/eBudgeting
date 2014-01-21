package biz.thaicom.eBudgeting.models.hrx;

public enum OrganizationType {
	อื่นๆ(0), ฝ่าย(1), ส่วน(2), แผนก(3), จังหวัด(4), อำเภอ(5); 
	
	private Integer value;
    private OrganizationType(int value) {
            this.value = value;
    }
    
    public Integer getValue() {
    	return value;
    }
    
    public static OrganizationType getType(Organization org) {
    	String orgCode = org.getCode();
    	if(orgCode != null) {
    		if(orgCode.matches("01\\d\\d0000")){
    			return OrganizationType.ฝ่าย;
    		} else if(orgCode.matches("01\\d\\d\\d\\d00")) {
    			return OrganizationType.ส่วน;
    		} else if(orgCode.matches("01\\d\\d\\d\\d\\d\\d")) {
    			return OrganizationType.แผนก;
    		} else if(orgCode.matches("\\d\\d\\d\\d0000")) {
    			return OrganizationType.จังหวัด;
    		} else if(orgCode.matches("\\d\\d\\d\\d\\d\\d00")) {
    			return OrganizationType.อำเภอ;
    		} else if(orgCode.matches("\\d\\d\\d\\d\\d\\d\\d\\d")) {
    			return OrganizationType.แผนก;
    		}
    	}
    	
    	return OrganizationType.อื่นๆ;
    }
    
    public static String getChildrenQueryString(Organization org) {
    	String orgCode = org.getCode();
    	if(orgCode != null) {
    		if(orgCode.matches("01\\d\\d0000")){
    			return orgCode.substring(0, 4) + "%";
    		} else if(orgCode.matches("01\\d\\d\\d\\d00")) {
    			return orgCode.substring(0, 6) + "%";
    		} else if(orgCode.matches("01\\d\\d\\d\\d\\d\\d")) {
    			return orgCode.substring(0, 8) + "%";
    		} else if(orgCode.matches("\\d\\d\\d\\d0000")) {
    			return orgCode.substring(0, 4) + "%";
    		} else if(orgCode.matches("\\d\\d\\d\\d\\d\\d00")) {
    			return orgCode.substring(0, 6) + "%";
    		} else if(orgCode.matches("\\d\\d\\d\\d\\d\\d\\d\\d")) {
    			return orgCode.substring(0, 8) + "%";
    		}
    	}
		return orgCode;
    }
}
