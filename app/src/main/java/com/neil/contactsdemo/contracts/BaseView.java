package com.neil.contactsdemo.contracts;

/**
 * Basic definition of presenter based view.
 */

public interface BaseView<T extends BasePresenter> {

    void setPresenter(T presenter);
}
