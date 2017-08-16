package org.bahmni.module.bahmnicore.contract.patient.search;


import static org.apache.commons.lang3.StringUtils.isEmpty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.bahmni.module.bahmnicore.i18n.Internationalizer;
import org.bahmni.module.bahmnicore.i18n.impl.InternationalizerImpl;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;

public class PatientAddressFieldQueryHelper {
	private String addressFieldValue;
	private String addressFieldName;
	private String[] addressSearchResultFields;
	protected Internationalizer i18n;
	
	protected static final String unattainableWhereClause = "p.uuid = null";

	public PatientAddressFieldQueryHelper(String addressFieldName,String addressFieldValue, String[] addressResultFields, Internationalizer i18n) {
		this.addressFieldName = addressFieldName;
		this.addressFieldValue = addressFieldValue;
		this.addressSearchResultFields = addressResultFields;
		this.i18n = i18n;
	}

	public PatientAddressFieldQueryHelper(String addressFieldName,String addressFieldValue, String[] addressResultFields) {
		this(addressFieldName, addressFieldValue, addressResultFields, new InternationalizerImpl());
	}

	public String selectClause(String select){
		String selectClause = ", ''  as addressFieldValue";
		List<String> columnValuePairs = new ArrayList<>();

		if (addressSearchResultFields != null) {
			for (String field : addressSearchResultFields)
				if (!"{}".equals(field)) columnValuePairs.add(String.format("\"%s\" : ' , '\"' , IFNULL(pa.%s ,''), '\"'", field, field));

			if(columnValuePairs.size() > 0) {
				selectClause = String.format(",CONCAT ('{ %s , '}') as addressFieldValue",
						StringUtils.join(columnValuePairs.toArray(new String[columnValuePairs.size()]), ", ',"));
			}
		}

		return select + selectClause;
	}

	public String appendToWhereClause(String where){
		if (isEmpty(addressFieldValue)) {
			return where;
		}
		if (!i18n.isEnabled()) {
			return combine(where, "and", enclose(" " + addressFieldName + " like '%" + StringEscapeUtils.escapeSql(addressFieldValue) + "%'"));
		}
		else {
			return appendI18nAddressValuesToWhereClause(where, addressFieldName, addressFieldValue);
		}
	}

	/*
	 * Creates a SQL 'in' statement based on matched i18n address values.
	 * Eg. "city_village in ('address.bilaspur','address.jaipur')"
	 */
	protected String appendI18nAddressValuesToWhereClause(String where, String addressFieldName, String addressFieldValue) {
		List<String> values = i18n.getAddressMessageKeysByLikeName(addressFieldValue);
		if (CollectionUtils.isEmpty(values)) {
			// we know already that no patient should be returned
			return combine(where, "and", unattainableWhereClause);
		}

		String inList = values.stream()
			.map((s) -> "'" + StringEscapeUtils.escapeSql(s) + "'")
			.collect(Collectors.joining(","));

		return combine(where, "and", enclose(" " + addressFieldName + " in (" + inList + ")"));
	}

	private String combine(String query, String operator, String condition) {
		return String.format("%s %s %s", query, operator, condition);
	}

	private String enclose(String value) {
		return String.format("(%s)", value);
	}

	public Map<String,Type> addScalarQueryResult(){
		Map<String,Type> scalarQueryResult = new HashMap<>();
		scalarQueryResult.put("addressFieldValue", StandardBasicTypes.STRING);
		return scalarQueryResult;
	}

	public String appendToGroupByClause(String fieldName) {
		if(isEmpty(fieldName)) return  addressFieldName;
		return addressFieldName + ", " + fieldName;
	}
}
