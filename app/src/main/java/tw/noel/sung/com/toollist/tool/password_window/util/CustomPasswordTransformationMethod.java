package tw.noel.sung.com.toollist.tool.password_window.util;

import android.text.method.PasswordTransformationMethod;
import android.view.View;

public class CustomPasswordTransformationMethod extends PasswordTransformationMethod {
    @Override
    public CharSequence getTransformation(CharSequence source, View view) {
        return new PasswordCharSequence(source);
    }

    private class PasswordCharSequence implements CharSequence {
        private CharSequence charSequence;

        public PasswordCharSequence(CharSequence charSequence) {
            this.charSequence = charSequence;
        }

        public char charAt(int index) {
            return '●';
        }

        public int length() {
            return charSequence.length();
        }

        public CharSequence subSequence(int start, int end) {
            return charSequence.subSequence(start, end);
        }
    }


}
