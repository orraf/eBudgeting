package biz.thaicom.eBudgeting.models.pln;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import biz.thaicom.eBudgeting.models.hrx.Person;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name="PLN_ACTIVITYTARGETRESULT")
@SequenceGenerator(name="PLN_ACTIVITYTARGETSRESULT_SEQ", sequenceName="PLN_ACTIVITYTARGETSRESULT_SEQ", allocationSize=1)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id",scope=ActivityTargetResult.class)
public class ActivityTargetResult implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4820296020355414158L;

	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PLN_ACTIVITYTARGETSRESULT_SEQ")
	private Long id;

	@Temporal(TemporalType.DATE)
	private Date reportedResultDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="RESULTTIMESTAMP")
	private Date timestamp;
	
	@Basic
	private Double result;
	
	@Basic
	private  Double budgetResult;
	
	@Basic
	private Integer budgetFiscalMonth;
	
	@Basic
	private Boolean resultBudgetType;
	
	@ManyToOne
	@JoinColumn(name="REPORTPERSON_HRX_PERSON_ID") 
	private Person person;
	
	@ManyToOne
	@JoinColumn(name="TARGETREPORT_PLN_REPORT_ID")
	private ActivityTargetReport report;
	
	@Lob
	@Column(name = "RESULT_TEXT")
	private String resultText;
	
	@Lob
	@Column(name = "PROBLEM_AND_SUGGESTION")
	private String problemAndSuggestion;
	
	@Basic
	@Column(length=1024)
	private String remark;
	
	
	@Basic
	private Boolean removed;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getReportedResultDate() {
		return reportedResultDate;
	}

	public void setReportedResultDate(Date reportedResultDate) {
		this.reportedResultDate = reportedResultDate;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public Double getResult() {
		return result;
	}

	public void setResult(Double result) {
		this.result = result;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public ActivityTargetReport getReport() {
		return report;
	}

	public void setReport(ActivityTargetReport report) {
		this.report = report;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Boolean getRemoved() {
		return removed;
	}

	public void setRemoved(Boolean removed) {
		this.removed = removed;
	}

	public Double getBudgetResult() {
		return budgetResult;
	}

	public void setBudgetResult(Double budgetResult) {
		this.budgetResult = budgetResult;
	}

	public Integer getBudgetFiscalMonth() {
		return budgetFiscalMonth;
	}

	public void setBudgetFiscalMonth(Integer budgetFiscalMonth) {
		this.budgetFiscalMonth = budgetFiscalMonth;
	}

	public Boolean getResultBudgetType() {
		return resultBudgetType;
	}

	public void setResultBudgetType(Boolean resultBudgetType) {
		this.resultBudgetType = resultBudgetType;
	}

	public String getResultText() {
		return resultText;
	}

	public void setResultText(String resultText) {
		this.resultText = resultText;
	}

	public String getProblemAndSuggestion() {
		return problemAndSuggestion;
	}

	public void setProblemAndSuggestion(String problemAndSuggestion) {
		this.problemAndSuggestion = problemAndSuggestion;
	}
	
	
	
}