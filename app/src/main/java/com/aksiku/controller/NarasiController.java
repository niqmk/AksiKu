package com.aksiku.controller;

import com.aksiku.general.Config;
import com.aksiku.general.GlobalVariables;

public class NarasiController {
	public static String get(final int step) {
		switch(step) {
			case 1:
				return GlobalVariables.karakter_session.Desc1();
			case 2:
				return GlobalVariables.karakter_session.Desc2();
			case 3:
				return GlobalVariables.karakter_session.Desc3();
			case 4:
				return GlobalVariables.karakter_session.Desc4();
			case 5:
				return GlobalVariables.karakter_session.Desc5();
			case 6:
				return GlobalVariables.karakter_session.Desc6();
			case 7:
				return GlobalVariables.karakter_session.Desc7();
			case 8:
				return GlobalVariables.karakter_session.Desc8();
			case 9:
				return GlobalVariables.karakter_session.Desc9();
			default:
				return Config.text_blank;
		}
	}
}