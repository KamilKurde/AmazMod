package com.amazmod.service.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.emoji.widget.EmojiButton;

import com.amazmod.service.Constants;
import com.amazmod.service.R;
import com.amazmod.service.settings.SettingsManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.tinylog.Logger;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import amazmod.com.models.Reply;

public class FragmentUtil {
    private static final float FONT_SIZE_NORMAL = 14.0f;
    private static final float FONT_SIZE_MEDIUM = 16.0f;
    private static final float FONT_SIZE_LARGE = 18.0f;
    private static final float FONT_SIZE_MORE_LARGE = 20.0f;
    private static final float FONT_SIZE_HUGE = 22.0f;
    private static final float FONT_SIZE_MORE_HUGE = 24.0f;

    private Context mContext;
    private SettingsManager settingsManager;
    private LinearLayout.LayoutParams params;
    private float fontTitleSizeSP, fontSizeSP;
    private String defaultLocale;

    public FragmentUtil(Context context) {
        this.mContext = context;
        settingsManager = new SettingsManager(this.mContext);

        params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        fontTitleSizeSP = getFontTitleSize();

        fontSizeSP = getFontSize();

        defaultLocale = getDefaultLocaleSettings();
    }

    private float getFontTitleSize() {
        String fontSize = settingsManager.getString(Constants.PREF_NOTIFICATIONS_FONT_TITLE_SIZE,
                Constants.PREF_DEFAULT_NOTIFICATIONS_FONT_TITLE_SIZE);
        switch (fontSize) {
            case "m":
                return FONT_SIZE_MEDIUM;
            case "l":
                return FONT_SIZE_LARGE;
            case "ml":
                return FONT_SIZE_MORE_LARGE;
            case "h":
                return FONT_SIZE_HUGE;
            case "mh":
                return FONT_SIZE_MORE_HUGE;
            default:
                return FONT_SIZE_NORMAL;
        }
    }

    private float getFontSize() {
        String fontSize = settingsManager.getString(Constants.PREF_NOTIFICATIONS_FONT_SIZE,
                Constants.PREF_DEFAULT_NOTIFICATIONS_FONT_SIZE);
        switch (fontSize) {
            case "m":
                return FONT_SIZE_MEDIUM;
            case "l":
                return FONT_SIZE_LARGE;
            case "ml":
                return FONT_SIZE_MORE_LARGE;
            case "h":
                return FONT_SIZE_HUGE;
            case "mh":
                return FONT_SIZE_MORE_HUGE;
            default:
                return FONT_SIZE_NORMAL;
        }
    }

    public float getFontTitleSizeSP() {
        return fontTitleSizeSP;
    }

    public float getFontSizeSP() {
        return fontSizeSP;
    }

    public boolean getInvertedTheme() {
        return settingsManager.getBoolean(Constants.PREF_NOTIFICATIONS_INVERTED_THEME,
                Constants.PREF_DEFAULT_NOTIFICATIONS_INVERTED_THEME);
    }

    public boolean getDisableDelay() {
        return settingsManager.getBoolean(Constants.PREF_DISABLE_DELAY,
                Constants.PREF_DEFAULT_DISABLE_DELAY);
    }


    public boolean getDisableNotificationText() {
        return settingsManager.getBoolean(Constants.PREF_DISABLE_NOTIFICATIONS_REPLIES,
                Constants.PREF_DEFAULT_DISABLE_NOTIFICATIONS_REPLIES);
    }

    /*
    public boolean getEnableDeleteButton() {
        return settingsManager.getBoolean(Constants.PREF_NOTIFICATION_DELETE_BUTTON, Constants.PREF_DEFAULT_NOTIFICATION_DELETE_BUTTON);
    }
    */

    private String getDefaultLocaleSettings() {
        String locale = settingsManager.getString(Constants.PREF_DEFAULT_LOCALE, "");
        Logger.info("FragmentUtil defaultLocale: " + locale);
        return locale;
    }

    public String getDefaultLocale() {
        return defaultLocale;
    }

    public void setFontLocale(TextView b) {
        // Old code to change font based on locale:
        if (defaultLocale.contains("iw") || defaultLocale.contains("ar")) {
            Logger.debug("[Notification Fragment] Element font changed to Hebrew/Arabic.");
            Typeface face = Typeface.createFromAsset(mContext.getAssets(), "fonts/DroidSansFallback.ttf");
            b.setTypeface(face);
        }
    }

    public void setFontLocale(TextView b, String locale) {
        // Logger.debug("[Notification Fragment] Testing element characters.");
        if (locale == null) {
            return;
        }
        // Identify Hebrew language
        //String unicode_iw_pattern = ".*[\u0590-\u05FF\uFB2A-\uFB4E].*"; // Hebrew character pattern (With precomposed characters)
        //String unicode_ar_pattern = ".*[\u0600-\u06FF].*"; // Arabic character pattern
        String unicode_iwar_pattern = ".*[\u0590-\u05FF\uFB2A-\uFB4E\u0600-\u06FF].*"; // Hebrew & Arabic characters pattern
        Pattern p = Pattern.compile(unicode_iwar_pattern, Pattern.MULTILINE); // Multiline search

        // Identify Hebrew or Arabic characters
        //if(locale.matches(unicode_iwar_pattern)){
        if (p.matcher(locale).find()) {
            Logger.debug("[Notification Fragment] Element font changed to Hebrew/Arabic.");
            Typeface face = Typeface.createFromAsset(mContext.getAssets(), "fonts/DroidSansFallback.ttf");
            b.setTypeface(face);
        }
    }

    public void setButtonParams(EmojiButton button, String text) {
        params.setMargins(20, 12, 20, 12);
        button.setLayoutParams(params);
        button.setPadding(0, 15, 0, 15);
        button.setIncludeFontPadding(false);
        button.setMinHeight(36);
        setFontLocale(button, defaultLocale);
        button.setText(text);
        button.setAllCaps(false);
        button.setTextSize(TypedValue.COMPLEX_UNIT_DIP, fontSizeSP);
    }

    public void setButtonParams(Button button, String text, Boolean mode) {
        if (mode) {
            params.setMargins(20, 12, 20, 12);
            button.setLayoutParams(params);
        }
        button.setPadding(0, 15, 0, 15);
        button.setIncludeFontPadding(false);
        button.setMinHeight(36);
        button.setText(text);
        button.setAllCaps(true);
        button.setTextSize(TypedValue.COMPLEX_UNIT_DIP, fontSizeSP);
    }

    public void setButtonTheme(Button button, String color) {
        switch (color) {
            case ("red"): {
                button.setTextColor(Color.parseColor("#ffffff"));
                button.setBackground(mContext.getDrawable(R.drawable.close_red));
                break;
            }
            case ("blue"): {
                button.setTextColor(Color.parseColor("#ffffff"));
                button.setBackground(mContext.getDrawable(R.drawable.reply_blue));
                break;
            }
            case ("grey"): {
                button.setTextColor(Color.parseColor("#000000"));
                button.setBackground(mContext.getDrawable(R.drawable.reply_grey));
                break;
            }
            default: {
                button.setTextColor(Color.parseColor("#000000"));
                button.setBackground(mContext.getDrawable(R.drawable.reply_grey));
            }
        }
    }


    public void setParamMargins(int left, int top, int right, int bottom) {
        params.setMargins(left, top, right, bottom);
    }

    public LinearLayout.LayoutParams getParams() {
        return this.params;
    }

    public List<Reply> listReplies() {
        final String replies = settingsManager.getString(Constants.PREF_NOTIFICATION_CUSTOM_REPLIES, "[]");

        try {
            Type listType = new TypeToken<List<Reply>>() {
            }.getType();
            return new Gson().fromJson(replies, listType);
        } catch (Exception ex) {
            return new ArrayList<>();
        }

    }

}
