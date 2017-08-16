package org.bahmni.module.bahmnicore.i18n.impl;

import java.util.List;

import org.bahmni.module.bahmnicore.i18n.Internationalizer;
import org.openmrs.api.context.Context;
import org.openmrs.module.ModuleFactory;
import org.openmrs.module.exti18n.ExtI18nConstants;
import org.openmrs.module.exti18n.api.AddressHierarchyI18nCache;
import org.openmrs.module.exti18n.api.ReverseI18nCache;

public class InternationalizerImpl implements Internationalizer {

	private AddressHierarchyI18nCache ahCache = new EmptyI18nCache();
	private ReverseI18nCache cache = ahCache;
	
	@Override
	public boolean isEnabled() {
		return cache.isEnabled() || ahCache.isEnabled();
	}
	
	/*
	 * Spring initializer
	 */
	public void init() {
		if (ModuleFactory.isModuleStarted("exti18n")) {
			cache = Context.getRegisteredComponent(ExtI18nConstants.COMPONENT_REVI18N, ReverseI18nCache.class);
			ahCache = Context.getRegisteredComponent(ExtI18nConstants.COMPONENT_AH_REVI18N, AddressHierarchyI18nCache.class);
		}
	}
	
	@Override
	public String getMessageKey(String msg) {
		return cache.getMessageKey(ahCache.getMessageKey(msg));
	}

	@Override
	public List<String> getAddressMessageKeysByLikeName(String addressSearchString) {
		return ahCache.getMessageKeysByLikeName(addressSearchString);
	}
}