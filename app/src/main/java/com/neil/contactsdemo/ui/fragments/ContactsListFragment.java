package com.neil.contactsdemo.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.neil.contactsdemo.R;
import com.neil.contactsdemo.adapters.ContactsAdapter;
import com.neil.contactsdemo.contracts.ContactsListContract;
import com.neil.contactsdemo.models.Contact;
import com.neil.contactsdemo.presenters.ContactsListPresenter;
import com.neil.contactsdemo.ui.activities.ContactDetailActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContactsListFragment extends Fragment
        implements ContactsListContract.View,
                   ContactsListContract.OnContactItemClickedListener {

    private static final String TAG = "ContactsListFragment";

    private ContactsListPresenter mPresenter;

    @BindView(R.id.spinner)         ProgressBar mSpinner;
    @BindView(R.id.loading)         TextView mLoadingTxv;
    @BindView(R.id.list)            RecyclerView mContactsList;
    @BindView(R.id.refresh)         SwipeRefreshLayout mSwipeRefresh;

    private ContactsAdapter mContactsAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContactsAdapter = new ContactsAdapter();
        mContactsAdapter.setItemClickListener(this);
        mPresenter = new ContactsListPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.contacts_all, container, false);
        ButterKnife.bind(this, root);
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContactsList.setAdapter(mContactsAdapter);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.loadContacts(false);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.loadContacts(true);
    }

    @Override
    public void showContacts(List<Contact> contacts) {
        Log.d(TAG, "to show " + contacts.size() + " contacts");
        mContactsAdapter.setContacts(contacts);
        mContactsList.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoadingErrorMessage(String message) {
        mSpinner.setVisibility(View.GONE);
        mContactsList.setVisibility(View.GONE);
        mLoadingTxv.setVisibility(View.VISIBLE);
        mLoadingTxv.setText(message);
    }

    @Override
    public void showContactDetail(Contact contact) {
        Intent intent = new Intent(getActivity(), ContactDetailActivity.class);
        intent.putExtra("contact", contact);
        startActivity(intent);
    }

    @Override
    public void showLoadingIndicator() {
        mContactsList.setVisibility(View.GONE);
        mSpinner.setIndeterminate(true);
        mSpinner.setVisibility(View.VISIBLE);
        mLoadingTxv.setText(R.string.loading);
        mLoadingTxv.setVisibility(View.VISIBLE);
    }

    @Override
    public void dismissLoadingIndicator() {
        mSpinner.setIndeterminate(false);
        mSpinner.setVisibility(View.GONE);
        mLoadingTxv.setVisibility(View.GONE);
        mSwipeRefresh.setRefreshing(false);
        mContactsList.setVisibility(View.VISIBLE);
    }

    @Override
    public void setPresenter(ContactsListContract.Presenter presenter) {
    }

    @Override
    public void onItemClicked(int pos, Contact contact, View view) {
        showContactDetail(contact);
    }
}
