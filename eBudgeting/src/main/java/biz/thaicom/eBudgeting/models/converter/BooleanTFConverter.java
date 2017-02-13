package biz.thaicom.eBudgeting.models.converter;

import javax.persistence.AttributeConverter;

public class BooleanTFConverter implements AttributeConverter<Boolean, String> {

	@Override
	public String convertToDatabaseColumn(Boolean value) {
		if (Boolean.TRUE.equals(value)) {
			return "T";
		} else {
			return "F";
		}
	}

	@Override
	public Boolean convertToEntityAttribute(String value) {
		return "T".equals(value);
	}

}
