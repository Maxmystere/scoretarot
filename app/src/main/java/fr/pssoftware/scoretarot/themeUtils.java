/*
 * Copyright (c) 2010-2014 Philippe SERAPHIN
 *
 *
 * This file is part of ScoreTarot.
 *
 * ScoreTarot is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package fr.pssoftware.scoretarot;
import android.app.Activity;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by seraphin-local on 19/02/15.
 */
public class themeUtils {



    public static void initTheme(Activity activity)

    {
         SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
        String cTheme=mySharedPreferences.getString("theme_list", "Sombre");
         if (cTheme.equals("Clair")){
            activity.setTheme(R.style.AppThemeLight);
        }else{
            activity.setTheme(R.style.AppTheme);
        }
    }

    public static int getBackground(Activity activity) {
        SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
        String cTheme=mySharedPreferences.getString("theme_list", "Sombre");
        if (cTheme.equals("Clair")){
            return activity.getResources().getColor(R.color.background_material_light);
        } else {
            return activity.getResources().getColor(R.color.background_material_dark);
        }
    }

    public static int getForeground(Activity activity) {
        SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
        String cTheme=mySharedPreferences.getString("theme_list", "Sombre");
        if (cTheme.equals("Clair")){
            return activity.getResources().getColor(R.color.background_material_dark);
        } else {
            return activity.getResources().getColor(R.color.background_material_light);
        }
    }

    public static int getLight(Activity activity) {
        return activity.getResources().getColor(R.color.background_material_light);
     }


}
