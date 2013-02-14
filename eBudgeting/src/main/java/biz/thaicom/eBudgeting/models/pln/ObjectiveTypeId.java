package biz.thaicom.eBudgeting.models.pln;

public enum ObjectiveTypeId {
	ROOT(100L), แผนงาน(101L), ยุทธศาสตร์(102L), ผลผลิตโครงการ(103L), กิจกรรมหลัก(104L), 
	แผนปฏิบัติการ(105L), กิจกรรมรอง(106L), กิจกรรมย่อย(107L), กิจกรรมเสริม(108L);
	
	
	 private final long id;
	 private ObjectiveTypeId(long id) {
		this.id = id;
	 }
	 public long getValue() { return id; }
	 
	 public String getName() {
		if(id==100) {
			return "ROOT";
		} else if(id==101){
			return "แผนงาน";
		} else if(id==102){
			return "ยุทศาสตร์";
		} else if(id==103){
			return "ผลผลิต/โครงการ";
		} else if(id==104){
			return "กิจกรรมหลัก";
		} else if(id==105){
			return "แผนปฏิบัติการ";
		} else if(id==106){
			return "กิจกรรมรอง";
		} else if(id==107){
			return "กิจกรรมย่อย";
		} else if(id==108){
			return "กิจกรรมเสริม";
		} 
		
		return "undefined";
	 }
	 
}
