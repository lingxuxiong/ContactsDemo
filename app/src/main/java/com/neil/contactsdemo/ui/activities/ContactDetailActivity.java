package com.neil.contactsdemo.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.neil.contactsdemo.R;
import com.neil.contactsdemo.contracts.ContactDetailContract;
import com.neil.contactsdemo.models.Contact;
import com.neil.contactsdemo.presenters.ContactDetailPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ContactDetailActivity extends AppCompatActivity
        implements View.OnClickListener, ContactDetailContract.View {

    private ContactDetailPresenter mPresenter;

    @BindView(R.id.avatar)    CircleImageView avatarImgv;
    @BindView(R.id.name)      TextView nameTxv;
    @BindView(R.id.phone)     TextView phoneTxv;
    @BindView(R.id.gender)    TextView genderTxv;
    @BindView(R.id.email)     TextView emailTxv;
    @BindView(R.id.address)   TextView addrTxv;
    @BindView(R.id.message)   Button messageBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);
        setupViews();

        mPresenter = new ContactDetailPresenter(this);
        mPresenter.subscribePubNubChannels();
    }

    private void setupViews() {
        ButterKnife.bind(this);
        messageBtn.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        Contact contact = (Contact) intent.getSerializableExtra("contact");
        populateContact(contact);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribePubNubChannels();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.message) {
            String message = "Hello " + nameTxv.getText();
            mPresenter.publishPubNubMessage(message);
        }
    }

    @Override
    public void setPresenter(ContactDetailContract.Presenter presenter) {

    }

    @Override
    public void populateContact(Contact contact) {
        nameTxv.setText(contact.getFullName());
        phoneTxv.setText(contact.getPhone());
        genderTxv.setText(contact.getGender());
        emailTxv.setText(contact.getEmail());
        addrTxv.setText(contact.getAddress());
        Glide.with(this).load(contact.getAvatar()).into(avatarImgv);
    }

    @Override
    public void showPubNubMessage(String message) {
        Toast.makeText(ContactDetailActivity.this, message, Toast.LENGTH_LONG).show();
    }
}
