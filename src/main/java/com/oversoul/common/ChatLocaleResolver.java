package com.oversoul.common;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

public class ChatLocaleResolver extends AcceptHeaderLocaleResolver {

	private List<Locale> LOCALES = Arrays.asList(new Locale("en"), new Locale("es"), new Locale("fr"),
			new Locale("es", "MX"), new Locale("zh"), new Locale("ja"));

	@Override
	public Locale resolveLocale(HttpServletRequest request) {
		if (StringUtils.isBlank(request.getHeader("Accept-Language"))) {
			return Locale.getDefault();
		}
		List<Locale.LanguageRange> list = Locale.LanguageRange.parse(request.getHeader("Accept-Language"));

		Locale locale = Locale.lookup(list, LOCALES);
		System.out.println("locale ===== " + locale);
		return locale == null ? Locale.getDefault() : locale;
	}
}
