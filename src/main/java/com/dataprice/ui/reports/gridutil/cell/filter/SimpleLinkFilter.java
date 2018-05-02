package com.dataprice.ui.reports.gridutil.cell.filter;

import com.vaadin.server.SerializablePredicate;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;

/**
 * Created by marten on 22.02.17.
 */
public class SimpleLinkFilter implements SerializablePredicate<Link> {

    final String filterString;
    final boolean ignoreCase;
    final boolean onlyMatchPrefix;

    public SimpleLinkFilter(String filterString, boolean ignoreCase, boolean onlyMatchPrefix) {
        this.ignoreCase = ignoreCase;
        // ignoreCase has to be applied to filterstring too, otherwise uppercase input won't work
        this.filterString = this.ignoreCase ? filterString.toLowerCase() : filterString;
        this.onlyMatchPrefix = onlyMatchPrefix;
    }

    @Override
    public boolean test(Link value) {
        if (filterString == null || value == null) {
            return false;
        }
        final String v = ignoreCase ? value.getCaption()
                .toLowerCase()
                : value.getCaption();

        if (onlyMatchPrefix) {
            if (!v.startsWith(filterString)) {
                return false;
            }
        } else {
            if (!v.contains(filterString)) {
                return false;
            }
        }
        return true;
    }
}