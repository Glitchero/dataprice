package com.dataprice.ui.reports.gridutil.cell;

import com.vaadin.data.Binder;
import com.vaadin.data.HasValue;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;

/**
 * extends CellFilterComponent to allow smallest and biggest filter Component
 *
 * @author Marten Prieß (http://www.rocketbase.io)
 * @version 1.1
 */
public abstract class RangeCellFilterComponentTyped<T, F extends HasValue, C extends Component> extends CellFilterComponent<C> {

    public static final String SMALLEST = "smallest";
    public static final String BIGGEST = "biggest";
    private static final long serialVersionUID = 1L;
    private HorizontalLayout hLayout;
    private Binder<TwoValueObjectTyped<T>> binder;

    public abstract F getSmallestField();

    public abstract F getBiggestField();

    /**
     * creates the layout when not already done
     *
     * @return a HLayout with already set style
     */
    public HorizontalLayout getHLayout() {
        if (this.hLayout == null) {
            this.hLayout = new HorizontalLayout();
            this.hLayout.setMargin(false);
            this.hLayout.addStyleName("filter-header");
        }
        return this.hLayout;
    }

    /**
     * create binder when not already done
     *
     * @return instance of binder
     */
    public Binder<TwoValueObjectTyped<T>> getBinder() {
        if (this.binder == null) {
            this.binder = new Binder(TwoValueObjectTyped.class);
            this.binder.setBean(new TwoValueObjectTyped<>());
        }
        return this.binder;
    }

    @Override
    public void triggerUpdate() {
        // trigger value Changed
        getBinder().setBean(getBinder().getBean());
    }
}
