package com.nerdery.umbrella.data;

/**
 * API for getting custom Nerdery icon URLs for weather conditions
 */
public class IconProvider {

    private static final String ICON_URL = "https://codechallenge.nerderylabs.com/mobile-nat/%s%s.png";

    private enum IconType {
        NORMAL,
        HIGHLIGHTED
    }

    /**
     * Get the URL to an icon suitable for use as a replacement for the icons given by Weather Underground
     * @param icon The name of the icon provided by Weather Underground (e.g. "clear").
     * @return A URL to an icon, always returns the non-highlighted version
     */
    public String getUrlForIcon(String icon) {
        return getUrlForIcon(icon, IconType.NORMAL);
    }

    /**
     * Get the URL to an icon suitable for use as a replacement for the icons given by Weather Underground
     * @param icon The name of the icon provided by Weather Underground (e.g. "clear").
     * @param type the type of icon to retrieve.
     * @return A URL to an icon
     */
    public String getUrlForIcon(String icon, IconType type) {
        String highlightParam = type == IconType.HIGHLIGHTED ? "-selected" : "";
        return String.format(ICON_URL, icon, highlightParam);
    }
}
