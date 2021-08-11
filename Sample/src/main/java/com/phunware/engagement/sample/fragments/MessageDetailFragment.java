package com.phunware.engagement.sample.fragments;

import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import com.phunware.engagement.Callback;
import com.phunware.engagement.Engagement;
import com.phunware.engagement.messages.callback.MessageReadCallback;
import com.phunware.engagement.messages.model.Message;
import com.phunware.engagement.messages.model.MessageMetadata;
import com.phunware.engagement.sample.R;
import com.phunware.engagement.sample.adapters.MessageDetailAdapter;
import com.phunware.engagement.sample.views.DividerItemDecoration;
import java.util.List;

public class MessageDetailFragment extends Fragment implements MessageDetailAdapter.OnItemClickListener {

    private static final String TAG = MessageDetailFragment.class.getSimpleName();

    private static final String EXTRA_MESSAGE = "message";
    private static final String EXTRA_MESSAGE_ID = "message_id";

    private RecyclerView mList;
    private ProgressBar mProgress;
    private MessageDetailAdapter mAdapter;

    public static MessageDetailFragment newInstance(long messageId) {
        MessageDetailFragment messageDetailFragment = new MessageDetailFragment();

        Bundle args = new Bundle();
        args.putLong(EXTRA_MESSAGE_ID, messageId);
        messageDetailFragment.setArguments(args);

        return messageDetailFragment;
    }

    public static MessageDetailFragment newInstance(Message message) {
        MessageDetailFragment messageDetailFragment = new MessageDetailFragment();

        if (message != null) {
            Bundle args = new Bundle();
            args.putParcelable(EXTRA_MESSAGE, message);
            messageDetailFragment.setArguments(args);
        }

        return messageDetailFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_message_detail, container, false);
        mList = v.findViewById(R.id.list);
        mProgress = v.findViewById(R.id.progressBar);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mList.addItemDecoration(new DividerItemDecoration(getActivity()));

        Bundle args = getArguments();
        if (args != null) {
            if (args.containsKey(EXTRA_MESSAGE)) {
                Message message = args.getParcelable(EXTRA_MESSAGE);
                onMessage(message);
            } else {
                final long messageId = args.getLong(EXTRA_MESSAGE_ID);
                mProgress.setVisibility(View.VISIBLE);
                mList.setVisibility(View.INVISIBLE);
                Engagement.fetchMessage(messageId, new Callback<Message>() {
                    @Override
                    public void onSuccess(Message data) {
                        onMessage(data);
                    }

                    @Override
                    public void onFailed(Throwable e) {
                        Log.e(TAG, "Failed to get message for id: " + messageId, e);
                        onError(e);
                    }
                });
            }
        }
    }

    private void onMessage(Message message) {
        mAdapter = new MessageDetailAdapter(message, this);
        mList.setAdapter(mAdapter);
        mList.setVisibility(View.VISIBLE);
        mProgress.setVisibility(View.INVISIBLE);
    }

    private void onError(Throwable e) {
        new AlertDialog.Builder(getActivity())
                .setMessage(getString(R.string.message_load_error, e.getMessage()))
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().getSupportFragmentManager().popBackStack();
                    }
                }).show();
    }

    @Override
    public void onPromotionClicked(Message message) {
        Fragment f = PromotionFragment.newInstance(message);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, f)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onMetadataClicked(Message message) {
        List<MessageMetadata> metadata = message.metadata;
        CharSequence[] items = new CharSequence[metadata.size()];
        StyleSpan bold = new StyleSpan(Typeface.BOLD);
        for (int i = 0, n = metadata.size(); i < n; i++) {
            MessageMetadata md = metadata.get(i);
            SpannableStringBuilder builder = new SpannableStringBuilder(
                getString(R.string.item_metadata, md.key, md.value));
            builder.setSpan(bold, 0, md.key.length() + 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            items[i] = builder;
        }
        new AlertDialog.Builder(getActivity())
            .setTitle(R.string.metadata)
            .setCancelable(true)
            .setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // no op
                }
            })
            .show();
    }

    @Override
    public void onReadClicked(final Message message) {
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.mark_read_confirm_title)
                .setMessage(R.string.mark_read_confirm_message)
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        markMessageRead(message);
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void markMessageRead(Message message) {
        Engagement.markMessageAsRead(String.valueOf(message.campaignId), new MessageReadCallback() {
            @Override
            public void onSuccess() {
                //NO-OP
            }

            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Failed to mark message as read.", e);
            }
        });
    }
}
