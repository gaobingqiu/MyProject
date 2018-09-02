package com.gbq.myproject.login;

import android.text.TextUtils;

import com.gbq.myproject.base.usecase.UseCase;

public class LoginTask extends UseCase<LoginTask.RequestValues, LoginTask.ResponseValue> {
    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };

    @Override
    protected void executeUseCase(RequestValues value) {
        boolean cancel = false;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(value.getPassword()) && !isPasswordValid(value.getPassword())) {
            getUseCaseCallback().onError(ResponseValue.ERROR_INVALID_PASSWORD);
            cancel = true;
        }
        // Check for a valid email address.
        if (TextUtils.isEmpty(value.getEmail())) {
            getUseCaseCallback().onError(ResponseValue.ERROR_FIELD_REQUIRED);
            cancel = true;
        } else if (!isEmailValid(value.getEmail())) {
            getUseCaseCallback().onError(ResponseValue.ERROR_INVALID_EMAIL);
            cancel = true;
        }
        if (cancel) {
            return;
        }
        getUseCaseCallback().onError(ResponseValue.SHOW_PROCESS);

        try {
            // Simulate network access.
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            getUseCaseCallback().onError(CODE);
            return;
        }

        for (String credential : DUMMY_CREDENTIALS) {
            String[] pieces = credential.split(":");
            if (pieces[0].equals(value.getEmail())) {
                // Account exists, return true if the password matches.
                if( pieces[1].equals(value.getPassword())){
                    getUseCaseCallback().onSuccess(new ResponseValue(true));
                }else {
                    getUseCaseCallback().onError(CODE);
                }
                return;
            }
        }

        // TODO: register the new account here.
        getUseCaseCallback().onError(CODE);
    }


    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    static class RequestValues implements UseCase.RequestValues {
        private final String mEmail;
        private final String mPassword;

        public RequestValues(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        public String getEmail() {
            return mEmail;
        }

        public String getPassword() {
            return mPassword;
        }
    }

    static class ResponseValue implements UseCase.ResponseValue {
        public final static int ERROR_INVALID_PASSWORD = 999;
        public final static int ERROR_FIELD_REQUIRED = 998;
        public final static int ERROR_INVALID_EMAIL = 997;
        public final static int SHOW_PROCESS = 996;

        private boolean mIsTrue;

        ResponseValue(boolean isTrue) {
            mIsTrue = isTrue;
        }

        public boolean isTrue() {
            return mIsTrue;
        }
    }
}
