package com.atb.app.activities.chat.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.atb.app.R;


// tag::SEND-1[]
public class MessageComposer extends RelativeLayout {

    public EditText mInput;
    private ImageView mSend;
    private ImageView mAttachment;

    private Listener mListener;

    public MessageComposer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(getContext().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mInput.getWindowToken(), 0);
    }

    private void init() {
        View root = inflate(getContext(), R.layout.view_message_composer, this);
        mInput = root.findViewById(R.id.composer_edittext);
        mSend = root.findViewById(R.id.composer_send);
        mAttachment = root.findViewById(R.id.composer_attachment);

        mSend.setOnClickListener(v -> {
            mListener.onSentClick(mInput.getText().toString().trim());
            mInput.setText("");
        });
        mAttachment.setOnClickListener(v -> {
            mListener.onSentImage(mInput.getText().toString().trim());
            mInput.setText("");
        });
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    public interface Listener {

        void onSentClick(String message);

        void onSentImage(String message);
    }

}
// end::SEND-1[]
