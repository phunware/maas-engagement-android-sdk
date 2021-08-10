package com.phunware.engagement.sample.fragments;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.phunware.engagement.messages.model.Message;
import com.phunware.engagement.sample.R;

public class PromotionFragment extends Fragment {

    private static final String EXTRA_MESSAGE = "message";

    private TextView mTitle;
    private WebView mWebview;

    public static PromotionFragment newInstance(Message message) {
        PromotionFragment f = new PromotionFragment();

        if (message != null) {
            Bundle args = new Bundle();
            args.putParcelable(EXTRA_MESSAGE, message);
            f.setArguments(args);
        }

        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_promotion, container, false);
        mTitle = v.findViewById(R.id.title);
        mWebview = v.findViewById(R.id.webview);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            Message message = args.getParcelable(EXTRA_MESSAGE);
            setMessage(message);
        }
    }

    private void setMessage(Message message) {
        mTitle.setText(message.promotionTitle);
        mWebview.loadData(message.promotionMessage, "text/html; charset=utf-8", "UTF-8");
    }
}
