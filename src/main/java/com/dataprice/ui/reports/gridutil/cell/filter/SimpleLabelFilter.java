package com.dataprice.ui.reports.gridutil.cell.filter;

import com.vaadin.server.SerializablePredicate;
import com.vaadin.ui.Label;

/**
 * Created by marten on 22.02.17.
 */
public class SimpleLabelFilter implements SerializablePredicate<Label> {

    final String filterString;
    final boolean ignoreCase;
    final boolean onlyMatchPrefix;

    public SimpleLabelFilter(String filterString, boolean ignoreCase, boolean onlyMatchPrefix) {
        this.ignoreCase = ignoreCase;
        // ignoreCase has to be applied to filterstring too, otherwise uppercase input won't work
        this.filterString = this.ignoreCase ? filterString.toLowerCase() : filterString;
        this.onlyMatchPrefix = onlyMatchPrefix;
    }

    @Override
    public boolean test(Label value) {
        if (filterString == null || value == null) {
            return false;
        }
        final String v = ignoreCase ? value.getValue()
                .toLowerCase()
                : value.getValue();

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