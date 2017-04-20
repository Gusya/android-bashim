package com.bashim;

/**
 * Created by Gusya on 24/01/2017.
 *
 * Model class for every Bash.im item
 */

public class BashItemData {

    private static final String LINK_SEPARATOR = "%2F";

    private String site;
    private String name;
    private String desc;
    private String link;
    private long elementId;
    private String elementPureHtml;

    public BashItemData(){}

    public BashItemData(String site, String name, String desc, String link, String html_text) {
        this.site = site;
        this.name = name;
        this.desc = desc;
        this.link = link;
        this.elementPureHtml = html_text;
        int lastSeparator = link.lastIndexOf(LINK_SEPARATOR);
        this.elementId = Long.valueOf(link.substring(lastSeparator+LINK_SEPARATOR.length()));

    }

    public String getSite() {
        return site;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public String getLink() {
        return link;
    }

    public long getElementId(){ return elementId; }

    public String getElementPureHtml() {
        return elementPureHtml;
    }

    @Override
    public String toString() {
        return site + ": "+getElementId();
    }

}
