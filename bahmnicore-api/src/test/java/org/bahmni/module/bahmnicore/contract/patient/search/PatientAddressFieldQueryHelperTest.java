package org.bahmni.module.bahmnicore.contract.patient.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import org.bahmni.module.bahmnicore.i18n.Internationalizer;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;
import org.junit.Assert;
import org.junit.Test;

public class PatientAddressFieldQueryHelperTest {

	@Test
	public void shouldReturnWhereClauseWhenAddressFieldValueIsAvailable(){
		PatientAddressFieldQueryHelper patientAddressFieldQueryHelper = new PatientAddressFieldQueryHelper("city_village", "Bilaspur",null);
		String whereClause = patientAddressFieldQueryHelper.appendToWhereClause("where test='1234'");
		assertEquals("where test='1234' and ( city_village like '%Bilaspur%')", whereClause);
	}

	@Test
	public void shouldReturnWhereClauseWhenAddressFieldValueIsNotAvailable(){
		PatientAddressFieldQueryHelper patientAddressFieldQueryHelper = new PatientAddressFieldQueryHelper("city_village", "",null);
		String whereClause = patientAddressFieldQueryHelper.appendToWhereClause("where test='1234'");
		assertEquals("where test='1234'", whereClause);
	}

	@Test
	public void ensureThatScalarQueryResultIsConfigured(){
		PatientAddressFieldQueryHelper patientAddressFieldQueryHelper = new PatientAddressFieldQueryHelper("city_village", "Bilaspur",null);
		Map<String,Type> map = patientAddressFieldQueryHelper.addScalarQueryResult();
		assertTrue(map.containsKey("addressFieldValue"));
		assertEquals(StandardBasicTypes.STRING,map.get("addressFieldValue"));
	}

	@Test
	public void ensureThatGroupByClauseIsConfiguredAndIsNotEmpty(){
		PatientAddressFieldQueryHelper patientAddressFieldQueryHelper = new PatientAddressFieldQueryHelper("city_village", "Bilaspur",null);
		String groupBy = patientAddressFieldQueryHelper.appendToGroupByClause("something");
		assertEquals("city_village, something",groupBy);
	}

	@Test
	public void ensureThatGroupByClauseIsConfiguredAndIsEmpty(){
		PatientAddressFieldQueryHelper patientAddressFieldQueryHelper = new PatientAddressFieldQueryHelper("city_village", "Bilaspur",null);
		String groupBy = patientAddressFieldQueryHelper.appendToGroupByClause("");
		assertEquals("city_village",groupBy);
	}

	@Test
	public void shouldReturnSelectClauseWithAddressFieldValue(){
		String[] addressSearchResultFields = {"address3", "address1", "address2"};
		PatientAddressFieldQueryHelper patientAddressFieldQueryHelper = new PatientAddressFieldQueryHelper("address1","123",addressSearchResultFields );
		String selectClause = patientAddressFieldQueryHelper.selectClause("select someFields");
		assertEquals("select someFields,CONCAT ('{ \"address3\" : ' , '\"' , IFNULL(pa.address3 ,''), '\"', ',\"address1\" : ' , '\"' , IFNULL(pa.address1 ,''), '\"', ',\"address2\" : ' , '\"' , IFNULL(pa.address2 ,''), '\"' , '}') as addressFieldValue", selectClause);

	}
	
	@Test
	public void shouldHaveDisabledInternalionalizerByDefault(){
		PatientAddressFieldQueryHelper helper = new PatientAddressFieldQueryHelper("city_village", "Bilaspur", null);
		Assert.assertNotNull(helper.i18n);
		Assert.assertFalse(helper.i18n.isEnabled());
	}
	
	@Test
	public void shouldReturnWhereClauseWithInStatement_whenI18nSupport() {
		
		Internationalizer i18n = mock(Internationalizer.class);
		when(i18n.isEnabled()).thenReturn(true);
		when(i18n.getAddressMessageKeysByLikeName("pur")).thenReturn(Arrays.asList("address.bilaspur", "address.jaipur"));
		
		PatientAddressFieldQueryHelper helper = new PatientAddressFieldQueryHelper("city_village", "pur", null, i18n);
		String whereClause = helper.appendToWhereClause("where test='1234'");
		
		assertEquals("where test='1234' and ( city_village in ('address.bilaspur','address.jaipur'))", whereClause);
	}
	
	@Test
	public void shouldReturnUnattainableWhereClause_whenI18nSupportAndUnmatchedAddresses() {
		
		Internationalizer i18n = mock(Internationalizer.class);
		when(i18n.isEnabled()).thenReturn(true);
		when(i18n.getAddressMessageKeysByLikeName("pur")).thenReturn(Collections.<String>emptyList());
		
		PatientAddressFieldQueryHelper helper = new PatientAddressFieldQueryHelper("city_village", "pur", null, i18n);
		String whereClause = helper.appendToWhereClause("where test='1234'");
		
		assertEquals("where test='1234' and " + PatientAddressFieldQueryHelper.unattainableWhereClause, whereClause);
	}
}
