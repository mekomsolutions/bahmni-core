package org.bahmni.module.bahmnicore.i18n.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.openmrs.PersonAddress;
import org.openmrs.module.exti18n.api.AddressHierarchyI18nCache;

public class EmptyI18nCache implements AddressHierarchyI18nCache {

	@Override
	public void reset() {
	}

	@Override
	public boolean isEnabled() {
		return false;
	}

	@Override
	public void setEnabled(boolean enabled) {
	}

	@Override
	public String getMessage(String key, Locale locale) {
		return key;
	}

	@Override
	public String getMessage(String key) {
		return key;
	}

	@Override
	public String getMessageKey(String message) {
		return message;
	}

	@Override
	public List<String> getOrderedAddressFields() {
		return Collections.<String>emptyList();
	}

	@Override
	public void setOrderedAddressFields(List<String> orderedAddressFields) {
	}

	@Override
	public List<String> getMessageKeysByLikeName(String searchString) {
		return Arrays.asList(searchString);
	}

	@Override
	public PersonAddress getI18nPersonAddress(PersonAddress address) {
		return address;
	}
}