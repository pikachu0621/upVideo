package com.pikachu.upvideo.util.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import android.widget.EditText;


import com.pikachu.upvideo.R;

import java.util.regex.Pattern;


/**
 * @author Pikachu
 * @Project upVideo
 * @Package com.pikachu.upvideo.util.view
 * @Date 2021/1/30 ( 下午 8:20 )
 * @description
 */
@SuppressLint("AppCompatCustomView")
public class PkEditText extends EditText {

    private static String regex = "^[\\u4e00-\\u9fa5_a-zA-Z0-9]+$";
    private static boolean isAnti = false;
    private static  boolean isOpen = true;


    public PkEditText(Context context) {
        this(context,null);
    }

    public PkEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public PkEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public PkEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        @SuppressLint("Recycle")
        TypedArray array = context.obtainStyledAttributes(
                attrs, R.styleable.PkEditText, defStyleAttr, 0);
        String regex = array.getString(R.styleable.PkEditText_pkRegex);
        if (regex != null && !regex.equals(""))
            PkEditText.regex = regex;
        isAnti = array.getBoolean(R.styleable.PkEditText_pkAnti, isAnti);
        isOpen = array.getBoolean(R.styleable.PkEditText_pkOpen, isOpen);
    }





    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        return new InnerInputConnection(super.onCreateInputConnection(outAttrs), false);
    }

    static class InnerInputConnection extends InputConnectionWrapper implements InputConnection {

        public InnerInputConnection(InputConnection onCreateInputConnection, boolean b) {
            super(onCreateInputConnection,b);
        }

        /**
         * 对输入的内容进行拦截
         *
         * @param text
         * @param newCursorPosition
         * @return
         */
        @Override
        public boolean commitText(CharSequence text, int newCursorPosition) {

            if (!isOpen)
                return super.commitText(text, newCursorPosition);
            if ( (Pattern.matches(regex, text.toString()) ^ isAnti) )
                return super.commitText(text, newCursorPosition);
            return false;
        }

        @Override
        public boolean sendKeyEvent(KeyEvent event) {
            return super.sendKeyEvent(event);
        }

        @Override
        public boolean setSelection(int start, int end) {
            return super.setSelection(start, end);
        }
    }
}
