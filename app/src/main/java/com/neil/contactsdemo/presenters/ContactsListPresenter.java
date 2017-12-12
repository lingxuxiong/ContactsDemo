package com.neil.contactsdemo.presenters;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.neil.contactsdemo.contracts.ContactsListContract;
import com.neil.contactsdemo.contracts.ContactsLoader;
import com.neil.contactsdemo.models.Contact;

import java.util.Collections;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class ContactsListPresenter implements ContactsListContract.Presenter {

    private static final String TAG = "ContactsListPresenter";

    private static final String API_BASE_URL = "https://s3.eu-central-1.amazonaws.com/";

    private ContactsListContract.View mView;

    private List<Contact> mCachedContacts;

    public ContactsListPresenter(@NonNull ContactsListContract.View view) {
        mView = view;
    }

    @Override
    public void start() {
        loadContacts(true);
    }

    @Override
    public void stop() {

    }

    @Override
    public void loadContacts(final boolean showIndicator) {

        if (mCachedContacts != null && mCachedContacts.size() > 0) {
            mView.dismissLoadingIndicator();
            return;
        }

        if (showIndicator) {
            mView.showLoadingIndicator();
        }

        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .client(new OkHttpClient.Builder().build())
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = retrofitBuilder.build();
        ContactsLoader contactsLoader = retrofit.create(ContactsLoader.class);
        Call<List<Contact>> callToLoad = contactsLoader.loadContacts();
        callToLoad.enqueue(new Callback<List<Contact>>() {

            @Override
            public void onResponse(Call<List<Contact>> call, Response<List<Contact>> response) {
                Log.d(TAG, "onResponse, got " + response.body().size() + " contacts.");
                List<Contact> contacts = response.body();
                Collections.sort(contacts);
                mCachedContacts = contacts;

                if (showIndicator) {
                    mView.dismissLoadingIndicator();
                }
                mView.showContacts(contacts);
            }

            @Override
            public void onFailure(Call<List<Contact>> call, Throwable t) {
                String msg = "Loading error:" + t.getMessage();
                mView.showLoadingErrorMessage(msg);
            }
        });
    }

    @Override
    public void onContactItemClicked(int pos, Contact contact, View view) {
        mView.showContactDetail(contact);
    }
}
