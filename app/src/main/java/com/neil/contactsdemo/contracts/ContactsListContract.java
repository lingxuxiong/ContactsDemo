package com.neil.contactsdemo.contracts;

import com.neil.contactsdemo.models.Contact;

import java.util.List;

public interface ContactsListContract {

    interface View extends BaseView<Presenter> {

        void showContacts(List<Contact> contacts);

        void showLoadingErrorMessage(String message);

        void showLoadingIndicator();

        void dismissLoadingIndicator();

        void showContactDetail(Contact contact);
    }

    interface Presenter extends BasePresenter {

        void loadContacts(boolean showIndicator);

        void onContactItemClicked(int pos, Contact contact, android.view.View view);
    }

    interface OnContactItemClickedListener {

        void onItemClicked(int pos, Contact contact, android.view.View view);

    }

}
