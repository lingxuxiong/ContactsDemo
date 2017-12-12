package com.neil.contactsdemo.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.neil.contactsdemo.R;
import com.neil.contactsdemo.contracts.ContactsListContract;
import com.neil.contactsdemo.models.Contact;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public final class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.VH> {

    private List<Contact> mContacts = new ArrayList<>();

    private ContactsListContract.OnContactItemClickedListener mOnItemClickListener;

    public ContactsAdapter() {
    }

    public ContactsAdapter(Collection<Contact> contacts) {
        mContacts.addAll(contacts);
    }

    public void setContacts(Collection<Contact> contacts) {
        mContacts.clear();
        mContacts.addAll(contacts);
        notifyDataSetChanged();
    }

    public void setItemClickListener(ContactsListContract.OnContactItemClickedListener listener) {
        mOnItemClickListener = listener;
    }


    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item, null);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(final VH holder, final int position) {
        final Context context = holder.name.getContext();
        final Contact contact = mContacts.get(position);
        holder.name.setText(contact.getFullName());
        holder.creationTime.setText(getHumanReadableCreationTime(context, contact.getCreationDate()));

        CircleImageView avatarImgv = holder.avatar;
        String avatarUrl = contact.getAvatar();
        Glide.with(avatarImgv.getContext()).load(avatarUrl).into(avatarImgv);

        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onItemClicked(position, contact, view);
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }

    static final class VH extends RecyclerView.ViewHolder {

        @BindView(R.id.avatar)
        CircleImageView avatar;

        @BindView(R.id.name)
        TextView name;

        @BindView(R.id.creation_time)
        TextView creationTime;

        VH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private String getHumanReadableCreationTime(@NonNull Context context, long time) {
        int quantityResId = 0;
        long count = 0;

        long diffDays = time / (24 * 60 * 60 * 1000);
        long diffHours = time / (60 * 60 * 1000) % 24;
        long diffMinutes = time / (60 * 1000) % 60;

        if (diffDays >= 1) {
            quantityResId = R.plurals.days_ago;
            count = diffDays;
        } else if (diffHours >= 1) {
            quantityResId = R.plurals.hrs_ago;
            count = diffHours;
        } else if (diffMinutes >= 1) {
            quantityResId = R.plurals.mins_ago;
            count = diffMinutes;
        }

        String res = context.getResources().getString(R.string.unknown_creation_time);
        if (quantityResId > 0) {
            res = context.getResources().getQuantityString(quantityResId, (int)count, (int)count);
        }

        return res;
    }

}
