package com.neil.contactsdemo.contracts;

import com.neil.contactsdemo.models.Contact;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ContactsLoader {

    @GET("roboteam/contacts_mock_short.json")
    Call<List<Contact>> loadContacts();

    interface Callbacks {

        void onContactsLoaded(List<Contact> contacts);

        void onContactLoadingException(Exception exception);

    }
}
