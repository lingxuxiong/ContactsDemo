package com.neil.contactsdemo.presenters;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.JsonObject;
import com.neil.contactsdemo.contracts.ContactDetailContract;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;

import java.util.Arrays;

public final class ContactDetailPresenter implements ContactDetailContract.Presenter {

    private static final String TAG = "ContactDetailPresenter";

    private final ContactDetailContract.View mView;
    private final PubNub mPubNubInstance;

    private static final String PUBNUB_CHANNEL_NAME = "contact_private_channel";
    private static final String PUBNUB_SUBSCRIBE_KEY = "sub-c-a8679dc8-dd3b-11e7-a23d-227f5812a52e";
    private static final String PUBNUB_PUBLISH_KEY= "pub-c-e2d3da1e-cd6f-4878-a4ee-038dcdfdec03";

    private final Handler mUiHandler = new Handler();

    public ContactDetailPresenter(@NonNull ContactDetailContract.View view) {
        mView = view;

        PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setSubscribeKey(PUBNUB_SUBSCRIBE_KEY);
        pnConfiguration.setPublishKey(PUBNUB_PUBLISH_KEY);
        pnConfiguration.setSecure(false);
        mPubNubInstance = new PubNub(pnConfiguration);
        mPubNubInstance.addListener(mSubscribeCallback);
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void subscribePubNubChannels() {
        mPubNubInstance.subscribe()
                .channels(Arrays.asList(PUBNUB_CHANNEL_NAME))
                .execute();
    }

    @Override
    public void unsubscribePubNubChannels() {
        mPubNubInstance.unsubscribe()
                .channels(Arrays.asList(PUBNUB_CHANNEL_NAME))
                .execute();
    }

    @Override
    public void publishPubNubMessage(String message) {
        JsonObject position = new JsonObject();
        position.addProperty("message", message);
        mPubNubInstance.publish()
                .message(message)
                .channel(PUBNUB_CHANNEL_NAME)
                .async(mPublishCallback);
    }

    private final SubscribeCallback mSubscribeCallback = new SubscribeCallback() {

        @Override
        public void status(PubNub pubnub, PNStatus status) {
            Log.d(TAG, "subscribe status:" + status.toString());
        }

        @Override
        public void message(PubNub pubnub, final PNMessageResult message) {
            mUiHandler.post(new Runnable() {
                @Override
                public void run() {
                    mView.showPubNubMessage(message.getMessage().toString());
                }
            });
        }

        @Override
        public void presence(PubNub pubnub, PNPresenceEventResult presence) {
            Log.d(TAG, "got presence result:" + presence);
        }
    };

    private final PNCallback<PNPublishResult> mPublishCallback = new PNCallback<PNPublishResult>() {

        @Override
        public void onResponse(final PNPublishResult result, final PNStatus status) {
            // handle publish result, status always present, result if successful
            // status.isError() to see if error happened
            final String message = status.isError() ? "Pub failed with status:" + status.getStatusCode() :
                    "pub succeeded with time token: " + result.getTimetoken();
            mUiHandler.post(new Runnable() {
                @Override
                public void run() {
                    mView.showPubNubMessage(message);
                }
            });
        }
    };

}
