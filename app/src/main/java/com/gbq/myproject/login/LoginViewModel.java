package com.gbq.myproject.login;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;

import com.gbq.myproject.R;
import com.gbq.myproject.base.BaseVm;
import com.gbq.myproject.base.usecase.UseCase;
import com.gbq.myproject.base.usecase.UseCaseHandler;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

public class LoginViewModel extends BaseVm implements LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    private MutableLiveData<Boolean> mLoginPre;

    private MutableLiveData<String> mPasswordError;
    private MutableLiveData<String> mEmailError;
    private MutableLiveData<Boolean> mShowProcess;
    private MutableLiveData<Boolean> mOnLoginSuccess;
    private MutableLiveData<Integer> mRequestContacts;
    private MutableLiveData<Boolean> mPopulateAutoComplete;
    private MutableLiveData<ArrayAdapter<String>> mEmaiAdapter;

    public LoginViewModel(@NonNull Application application) {
        super(application);
    }

    public boolean mayRequestContacts() {
        boolean needRequest = Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                getApplication().checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;
        if (needRequest) {
            getRequestContacts().setValue(REQUEST_READ_CONTACTS);
        }
        return needRequest;
    }


    public void onRequestPermissionsResult(int requestCode, int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getPopulateAutoComplete().setValue(true);
            }
        }
    }

    public boolean onEditorAction(int id) {
        if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
            attemptLogin();
            return true;
        }
        return false;
    }

    public void attemptLogin() {
        getLoginPre().setValue(true);
    }

    public void toLogin(String email, String password) {
        LoginTask.RequestValues values = new LoginTask.RequestValues(email, password);
        UseCaseHandler.getInstance().execute(new LoginTask(), values, new UseCase.UseCaseCallback<LoginTask.ResponseValue>() {
            @Override
            public void onError(Integer code) {
                switch (code) {
                    case LoginTask.ResponseValue.ERROR_INVALID_PASSWORD:
                        getPasswordError().setValue(getApplication().getString(R.string.error_invalid_password));
                        break;
                    case LoginTask.ResponseValue.ERROR_FIELD_REQUIRED:
                        getEmailError().setValue(getApplication().getString(R.string.error_field_required));
                        break;
                    case LoginTask.ResponseValue.ERROR_INVALID_EMAIL:
                        getEmailError().setValue(getApplication().getString(R.string.error_invalid_email));
                        break;
                    case LoginTask.ResponseValue.SHOW_PROCESS:
                        getShowProcess().setValue(true);
                        break;
                    case UseCase.CODE:
                        getShowProcess().setValue(false);
                        getPasswordError().setValue(getApplication().getString(R.string.error_incorrect_password));
                        break;
                    default:
                        getShowProcess().setValue(false);
                        getPasswordError().setValue(getApplication().getString(R.string.error_incorrect_password));
                        break;
                }
            }

            @Override
            public void onSuccess(LoginTask.ResponseValue result) {
                getShowProcess().setValue(false);
                getOnLoginSuccess().setValue(true);
            }
        });
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        return new CursorLoader(getApplication(), // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplication(),
                        android.R.layout.simple_dropdown_item_1line, emails);
        getEmaiAdapter().setValue(adapter);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    public MutableLiveData<Boolean> getLoginPre() {
        if (mLoginPre == null) {
            mLoginPre = new MutableLiveData<>();
        }
        return mLoginPre;
    }

    public MutableLiveData<String> getPasswordError() {
        if (mPasswordError == null) {
            mPasswordError = new MutableLiveData<>();
        }
        return mPasswordError;
    }

    public MutableLiveData<String> getEmailError() {
        if (mEmailError == null) {
            mEmailError = new MutableLiveData<>();
        }
        return mEmailError;
    }

    public MutableLiveData<Boolean> getShowProcess() {
        if (mShowProcess == null) {
            mShowProcess = new MutableLiveData<>();
        }
        return mShowProcess;
    }

    public MutableLiveData<Boolean> getOnLoginSuccess() {
        if (mOnLoginSuccess == null) {
            mOnLoginSuccess = new MutableLiveData<>();
        }
        return mOnLoginSuccess;
    }

    public MutableLiveData<Integer> getRequestContacts() {
        if (mRequestContacts == null) {
            mRequestContacts = new MutableLiveData<>();
        }
        return mRequestContacts;
    }

    public MutableLiveData<Boolean> getPopulateAutoComplete() {
        if (mPopulateAutoComplete == null) {
            mPopulateAutoComplete = new MutableLiveData<>();
        }
        return mPopulateAutoComplete;
    }

    public MutableLiveData<ArrayAdapter<String>> getEmaiAdapter() {
        if (mEmaiAdapter == null) {
            mEmaiAdapter = new MutableLiveData<>();
        }
        return mEmaiAdapter;
    }
}

