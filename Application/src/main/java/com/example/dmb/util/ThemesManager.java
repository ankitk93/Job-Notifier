package com.example.dmb.util;


import android.content.Context;

import com.example.dmb.materialnews.R;
import com.example.dmb.settings.AppConstants;
import com.example.dmb.settings.ThemeEnum;

/**
 * The Class ThemesManager.
 */
public class ThemesManager {
	
	/**
	 * Sets the correct theme.
	 *
	 * @param context the new correct theme
	 */
	public static void setCorrectTheme(Context context) {

		if (AppConstants.APP_THEME == ThemeEnum.BLUE) {
			context.setTheme(R.style.blue);
		}else if(AppConstants.APP_THEME == ThemeEnum.GREEN){
			context.setTheme(R.style.green);
		}else if(AppConstants.APP_THEME == ThemeEnum.PINK){
			context.setTheme(R.style.pink);
		}else if(AppConstants.APP_THEME == ThemeEnum.DARK){
			context.setTheme(R.style.dark);
		}else if(AppConstants.APP_THEME == ThemeEnum.RED){
			context.setTheme(R.style.red);
		}else if(AppConstants.APP_THEME == ThemeEnum.ORANGE){
			context.setTheme(R.style.orange);
		}else if(AppConstants.APP_THEME == ThemeEnum.BLUEGRAY){
            context.setTheme(R.style.bluegray);
        }else if(AppConstants.APP_THEME == ThemeEnum.BROWN){
            context.setTheme(R.style.brown);
        }else if(AppConstants.APP_THEME == ThemeEnum.CYAN){
            context.setTheme(R.style.cyan);
        }else if(AppConstants.APP_THEME == ThemeEnum.LIGHTPINK){
            context.setTheme(R.style.lightpink);
        }else if(AppConstants.APP_THEME == ThemeEnum.LIME){
            context.setTheme(R.style.lime);
        }else if(AppConstants.APP_THEME == ThemeEnum.LIGHTGREEN){
            context.setTheme(R.style.lightgreen);
        }else if(AppConstants.APP_THEME == ThemeEnum.TEAL){
            context.setTheme(R.style.teal);
        }else if(AppConstants.APP_THEME == ThemeEnum.INDIGO){
            context.setTheme(R.style.indigo);
        }else if(AppConstants.APP_THEME == ThemeEnum.LIGHTPURPLE){
            context.setTheme(R.style.lightpurple);
        }
	}
}
