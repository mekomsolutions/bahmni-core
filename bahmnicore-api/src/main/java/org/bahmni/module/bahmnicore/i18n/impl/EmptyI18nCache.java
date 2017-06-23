package org.bahmni.module.bahmnicore.i18n.impl;

import java.util.Locale;

import org.openmrs.module.exti18n.api.ReverseI18nCache;

public class EmptyI18nCache implements ReverseI18nCache {

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
}