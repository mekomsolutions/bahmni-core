package org.openmrs.module.bahmnicore.i18n;

import java.util.List;

/**
 * Internationalizer helps leverage and does provide an API to the internationalization caches of Ext I18N.
 * It is placed (as well as its implementations) in an org.openmrs.* package to be scannable according to
 * OpenMRS Core annotation filters.
 * See https://github.com/openmrs/openmrs-core/blob/eeccac7ae121271ddbeda83e6465d9c5da7e65c0/api/src/main/resources/applicationContext-service.xml#L679
 */
public interface Internationalizer {

	/**
	 * Tells whether i18n support is enabled for metadata.
	 * This helps skips the use of the internationalizer altogether when it is marked as disabled.
	 */
	public boolean isEnabled();
	
	/**
	 * Returns the internationalized message key from a localized string.
	 * Eg. "កម្ពុជា" -> "addresshierarchy.cambodia". 
	 * @param A localized string.
	 * @return The i18n message key.
	 */
	public String getMessageKey(String msg);

	/**
	 * Returns the list of possible address hierarchy entries message keys
	 * when given a partial string out of the localized address entry.
	 * @param addressSearchString
	 * @return
	 */
	public List<String> getAddressMessageKeysByLikeName(String addressSearchString);
}