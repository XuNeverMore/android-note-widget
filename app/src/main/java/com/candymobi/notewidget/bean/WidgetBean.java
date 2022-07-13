package com.candymobi.notewidget.bean;

import androidx.annotation.Nullable;

import java.util.Objects;

/**
 * <pre>
 *      author: xct
 *      create on: 2022/7/13 14:39
 *      description:
 * </pre>
 */
public class WidgetBean {

    private int widgetId;
    private String widgetBg;
    private String content;

    public int getWidgetId() {
        return widgetId;
    }

    public void setWidgetId(int widgetId) {
        this.widgetId = widgetId;
    }

    public String getWidgetBg() {
        return widgetBg;
    }

    public void setWidgetBg(String widgetBg) {
        this.widgetBg = widgetBg;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int hashCode() {
        return Objects.hash(widgetId, widgetBg, content);
    }

}
