package biz.thaicom.eBudgeting.models.pln;

public enum ObjectiveTypeId {
	ROOT(100L), ยุทธศาสตร์(101L), ผลผลิตโครงการ(102L), กิจกรรมหลัก(103L), 
	แผนปฏิบัติการ(104L), กิจกรรมรอง(105L), กิจกรรมย่อย(106L), กิจกรรมเสริม(107L),กิจกรรมสนับสนุน (108L);
	
	
	 private final long id;
	 private ObjectiveTypeId(long id) {
		this.id = id;
	 }
	 public long getValue() { return id; }
	 
	 public String getName() {
		if(id==100) {
			return "ROOT";
		} else if(id==101){
			return "ยุทศาสตร์";
		} else if(id==102){
			return "ผลผลิต/โครงการ";
		} else if(id==103){
			return "กิจกรรมหลัก";
		} else if(id==104){
			return "แผนปฏิบัติการ";
		} else if(id==105){
			return "กิจกรรมรอง";
		} else if(id==106){
			return "กิจกรรมย่อย";
		} else if(id==107){
			return "กิจกรรมเสริม";
		} else if(id==108){
			return "กิจกรรมสนับสนุน";
		} 
		
		return "undefined";
	 }
	 
}
