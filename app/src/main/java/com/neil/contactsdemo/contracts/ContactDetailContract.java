package com.neil.contactsdemo.contracts;

import com.neil.contactsdemo.models.Contact;

public interface ContactDetailContract {

    interface View extends BaseView<Presenter> {

        void populateContact(Contact contact);

        void showPubNubMessage(String message);
    }

    interface Presenter extends BasePresenter {

        void subscribePubNubChannels();

        void unsubscribePubNubChannels();

        void publishPubNubMessage(String message);
    }
}
