package com.xzy.weather.component;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.EditText;

import com.xzy.weather.R;

/**
 * Author:xzy
 * Date:2020/8/31 10:31
 **/
public class SearchEditText extends androidx.appcompat.widget.AppCompatEditText {

    private static final String TAG = "SearchEditText";

    private Context mContext;
    private Drawable imgSearch;
    private Drawable imgCancel;

    private OnTextChangeListener mListener;

    public SearchEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    void init(){
        imgCancel = mContext.getResources().getDrawable(R.drawable.ic_cancel);
        imgSearch = mContext.getResources().getDrawable(R.drawable.ic_search);
        imgCancel.setBounds(0 ,0, 50, 50);
        imgSearch.setBounds(0, 0 , 80, 80);
        setCompoundDrawables(imgSearch, null, null, null);
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setDrawable();
                mListener.afterChanged();
            }
        });
    }

    private void setDrawable(){
        if(length() < 1){
            setCompoundDrawables(imgSearch, null, null, null);
        } else {
            setCompoundDrawables(imgSearch, null, imgCancel, null);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(imgCancel != null && event.getAction() == MotionEvent.ACTION_DOWN){
            //Log.d(TAG, "onTouchEvent: " + event.getX() + " " + event.getY());
            Rect rect = new Rect();
            getLocalVisibleRect(rect);
            //Log.d(TAG, "onTouchEvent: " + rect);
            rect.left = rect.right - 50;
            if(rect.contains((int)event.getX(), (int)event.getY())){
                setText("");
            }
        }
        return super.onTouchEvent(event);
    }

    public void setOnTextChangedListener(OnTextChangeListener l){
        mListener = l;
    }

    public interface OnTextChangeListener {
        void afterChanged();
    }
}
