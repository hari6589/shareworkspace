package com.bfrc.framework.util;

import org.springframework.ui.Model;

import com.bsro.errors.Errors;

public class ControllerUtils {
	public static void setErrors(Model model, Errors errors) {

		if (errors != null) {
			if (errors.getFieldErrors() != null && !errors.getFieldErrors().isEmpty()) {
				for (String element : errors.getFieldErrors().keySet()) {
					model.addAttribute(element + "Error", errors.getFieldErrors().get(element));
				}
			}
			if (errors.getGlobalErrors() != null && !errors.getGlobalErrors().isEmpty()) {
				model.addAttribute("errors", errors.getGlobalErrors().get(0));
			}
		}
	}
}
