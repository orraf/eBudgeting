package biz.thaicom.eBudgeting.models.hrx;

public enum OrganizationType {
	อื่นๆ(0), ฝ่าย(1), ส่วน(2), แผนก(3), จังหวัด(4), อำเภอ(5), ส่วนในจังหวัด(6), แผนกในจังหวัด(7), แผนกในอำเภอ(8), ศูนย์ปฏิบัติการ(9), เขต(10), กอง(11), กองในเขต(12); 
	
	private Integer value;
    private OrganizationType(int value) {
            this.value = value;
    }
    
    public Integer getValue() {
    	return value;
    }
    
    public static OrganizationType getType(Organization org) {
    	String orgCode = org.getCode();
    	String first2Digit = org.getCode().substring(0, 2);
    	if(orgCode != null && orgCode.length() == 8) {
    		if(orgCode.matches("01\\d\\d0000")){
    			return OrganizationType.ฝ่าย;
    		} else if(orgCode.matches("01\\d\\d\\d\\d00")) {
    			return OrganizationType.ส่วน;
    		} else if(orgCode.matches("01\\d\\d\\d\\d\\d\\d")) {
    			return OrganizationType.แผนก;
    		} else if(orgCode.matches("\\d\\d000000")) {
    			return OrganizationType.จังหวัด;
    		} else if ( (first2Digit.equals("41") || first2Digit.equals("32") ) && 
    				(orgCode.matches("\\d\\d\\d\\d0000")))  {
    			// กรณี นครศรี และ สงขลา
    			return OrganizationType.จังหวัด;		    		
    		} else if (orgCode.matches("\\d\\d001\\d00")) {
    			return OrganizationType.ศูนย์ปฏิบัติการ;
    		} else if (orgCode.matches("\\d\\d000\\d00")) {
    			return OrganizationType.ส่วนในจังหวัด;
    		} else if (orgCode.matches("\\d\\d00\\d\\d\\d\\d")) {
    			return OrganizationType.แผนกในจังหวัด;
    		} else if(orgCode.matches("\\d\\d\\d\\d\\d\\d00")) {
    			return OrganizationType.อำเภอ;
    		} else if(orgCode.matches("\\d\\d\\d\\d\\d\\d\\d\\d")) {
    			return OrganizationType.แผนกในอำเภอ;
    		}
    	} else if (orgCode != null && orgCode.length() == 11) {
    		if(orgCode.matches("100\\d\\d\\d\\d0000")){
    			return OrganizationType.ฝ่าย;
    		} else if(orgCode.matches("100\\d\\d\\d\\d\\d\\d00")){
    			return OrganizationType.กอง; 
    		} else if(orgCode.matches("100\\d\\d\\d\\d\\d\\d\\d\\d")){
    			return OrganizationType.แผนก; 
    		} else if(orgCode.matches("1\\d\\d00000000")){
    			return OrganizationType.เขต;
    		} else if(orgCode.matches("1\\d\\d0000\\d\\d00")){
    			return OrganizationType.กองในเขต;
    		} else if(orgCode.matches("1\\d\\d\\d\\d\\d\\d0000")){
    			return OrganizationType.จังหวัด;
    		} else if(orgCode.matches("1\\d\\d\\d\\d\\d\\d\\d\\d00")){
    			return OrganizationType.อำเภอ;
    		} else if(orgCode.matches("1\\d\\d\\d\\d\\d\\d\\d\\d\\d\\d")){
    			return OrganizationType.แผนกในอำเภอ;
    		}
    	}
    	
    	return OrganizationType.อื่นๆ;
    }
    
  
    
    public static String getChildrenQueryStringOnlyProvince(Organization org) {
    	String query = "";
    	String orgCode = org.getCode();
    	
    	if(orgCode != null && orgCode.length() == 8) {
	    	if(OrganizationType.getType(org) == OrganizationType.จังหวัด) {
	    		query += org.getCode().substring(0, 2);
	    		query += "000%";
	    	}
    	} else if (orgCode != null && orgCode.length() == 11) {
    		if(OrganizationType.getType(org) == OrganizationType.จังหวัด) {
    			query += org.getCode().substring(0, 6);
    			query += "%";
    		} else {
    			query += org.getCode().substring(0, 8);
    			query += "%";
    		}
    	}
    	
    	return query;
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

	public static Long getDistrictId(Organization org) {
		return Long.parseLong(org.getCode().substring(0, 3) + "00000000");
	}
	
	  public static Long getProvinceId(Organization org) {
		  return Long.parseLong(org.getCode().substring(0, 7) + "0000");
		  
//	    	if(org.getCode().startsWith("0")) {
//	    		// จังหวัด code จะขึ้นต้น ด้วย 0
//	    		return null;
//	    	} else {
//	    		String first2Digit = org.getCode().substring(0, 2);
//	    		if(first2Digit.equals("41") || first2Digit.equals("32")) {
//	    			// กรณี นครศรี และ สงขลา
//	    			String first4Digit = org.getCode().substring(0, 4);
//	    			return Long.parseLong("1" + first4Digit + "0000");
//	    			
//	    		} else {
//	    			return Long.parseLong("1" + first2Digit + "000000");	
//	    		}
//	    		
//	    	}
	    	
	    }
	    
	    public static Long get_ส่วนในจังหวัดหรืออำเภอ_Id(Organization org) {
	    	return Long.parseLong(org.getCode().substring(0, 9) + "00");
//	    	if(org.getCode().startsWith("0")) {
//	    		// จังหวัด code จะขึ้นต้น ด้วย 0
//	    		return null;
//	    	} else {
//	    		String first6Digit = org.getCode().substring(0, 6);
//	    		
//	    		return Long.parseLong("1" + first6Digit + "00");	
//	    	}
	    }
}
