/**
 * Copyright (c), 2015, {COMPANY_NAME}. All rights reserved. Unpublished--rights reserved under the copyright laws
 * of the United States. Use of a copyright notice is precautionary only and does not imply publication or disclosure.
 * This software contains confidential information and trade secrets of {COMPANY_NAME}. Use, disclosure, or reproduction
 * is prohibited without the prior written consent of {COMPANY_NAME}.
 */
package com.bridgestone.bsro.aem.voice.util;

import java.io.File;
import java.io.IOException;

import com.day.io.file.FileUtils;

/*
 *
 * Created by Balaji Podapati on : Feb 1, 2016 for the project : bsro-aem-appointment-funnel
 */
public class DocumentReaderUtil {
	public static String readByName(String fileNameWithPath) {
		String readFileToString = "";
		try {
			readFileToString = FileUtils.readFileToString(new File(fileNameWithPath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return readFileToString;
	}
}
