package com.vaadin.polymer;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.vaadin.polymer.elemental.*;

public class PolymerWidget extends HTMLPanel {

    public PolymerWidget(String tag, String src, String html) {
        super(tag, html);
        Polymer.ensureCustomElement(getElement(), src);
    }

    public PolymerWidget(String tag, SafeHtml safeHtml) {
        super(tag, safeHtml.asString());
    }

    public void setDisabled(boolean disabled) {
        setBooleanAttribute("disabled", disabled);
    }

    public void setBooleanAttribute(String name, boolean value) {
        if (value) {
            getElement().setAttribute(name, "");
        } else {
            getElement().removeAttribute(name);
        }
    }

    public boolean getBooleanAttribute(String value) {
        return getElement().hasAttribute(value)
                && (getElement().getAttribute(value).isEmpty()
                    || Boolean.parseBoolean(getElement().getAttribute(value)));
    }

    // TODO: Remove this hack for paper-radio-button
    public void setName(String value) {
        getElement().setAttribute("name", value);
    }

    public void setNoink(boolean noink) {
        setBooleanAttribute("noink", noink);
    }
    public void setTabindex(int index) {
        getElement().setAttribute("tabindex", "" + index);
    }

    public void setAriaLabel(String label) {
        getElement().setAttribute("aria-label", label);
    }

    /**
     * @param attributes a list of attributes or attribute:values pairs.
     *        examples:
     *          setAttributes("foo bar")
     *          setAttributes("foo:bar ; hello:bye")
     */
    public void setAttributes(String attributes) {
        for (String attr : attributes.trim().replace(" *([;:]) *", "$1").split("[; ]+")) {
            MatchResult e = RegExp.compile(" *([\\w-]+)( *: *)?(.*)? *").exec(attr);

            if (e.getGroup(3) != null) {
                getElement().setAttribute(e.getGroup(1), e.getGroup(3));
            } else {
                setBooleanAttribute(e.getGroup(1), true);
            }
        }
    }

    public HandlerRegistration addClickHandler(ClickHandler handler) {
        return addDomHandler(handler, ClickEvent.getType());
    }

    public void ready(Function f) {
        Polymer.ready(getElement(), f);
    }

    public void jsinteropError() {
        Window.alert(
            "GWT-Polymer ERROR: Double check that you are compiling your project with the `-XjsInteropMode JS` flag");
    }

    public void setClass(String clazz) {
        getElement().setClassName(clazz);
    }

    public void setStyle(String style) {
        getElement().setAttribute("style", style);
    }

    public void setId(String id) {
        getElement().setId(id);
    }

    /**
     * Polymer’s custom property shim evaluates and applies custom property values once at element creation time.
     * In order to have an element (and its subtree) re- evaluate custom property values due to dynamic changes
     * such as application of CSS classes, etc., call updateStyles().
     */
    public void updateStyles() {
        ((HTMLElement)getElement()).updateStyles();
    }
}
