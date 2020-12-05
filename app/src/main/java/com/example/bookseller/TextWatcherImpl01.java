package com.example.bookseller;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

public class TextWatcherImpl01 implements TextWatcher {

    private Button button;
    private List<EditText> list;

    public TextWatcherImpl01(Button button, List<EditText> list) {
        this.list = list;
        this.button = button;
        ButtonUtils.setButtonDisabled(button);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    // 当 List 中的元素都被填充，则使按钮可点击，否则按钮不可点击。
    @Override
    public void afterTextChanged(Editable s) {
        boolean allFilled = true;
        for (EditText editText : list) {
            String content = editText.getText().toString();
            if (TextUtils.isEmpty(content)) {
                allFilled = false;
                ButtonUtils.setButtonDisabled(button);
                break;
            }
        }

        if (allFilled) {
            ButtonUtils.setButtonEnabled(button);
        }
    }
}

class ButtonUtils {
    public static void setButtonEnabled(Button button) {
        button.setAlpha(1.0F);
        button.setEnabled(true);
    }

    public static void setButtonDisabled(Button button) {
        button.setAlpha(0.5F);
        button.setEnabled(false);
    }
}
