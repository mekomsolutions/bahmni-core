package org.bahmni.module.bahmnicore.i18n.impl;

import org.bahmni.module.bahmnicore.i18n.Internationalizer;
import org.openmrs.api.context.Context;
import org.openmrs.module.ModuleFactory;
import org.openmrs.module.exti18n.ExtI18nConstants;
import org.openmrs.module.exti18n.api.AddressHierarchyI18nCache;
import org.openmrs.module.exti18n.api.ReverseI18nCache;

public class InternationalizerImpl implements Internationalizer {

	private ReverseI18nCache cache;
	private ReverseI18nCache ahCache;
	
	public void init() {
		if (ModuleFactory.isModuleStarted("exti18n")) {
			cache = Context.getRegisteredComponent(ExtI18nConstants.COMPONENT_REVI18N, ReverseI18nCache.class);
			ahCache = Context.getRegisteredComponent(ExtI18nConstants.COMPONENT_AH_REVI18N, AddressHierarchyI18nCache.class);
		}
		else {
			cache = new EmptyI18nCache(); // an inert/disabled cache that does nothing
			ahCache = cache;
		}
	}
	
	@Override
	public String getMessageKey(String msg) {
		return cache.getMessageKey(ahCache.getMessageKey(msg));
	}
}