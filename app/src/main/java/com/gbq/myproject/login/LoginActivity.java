package com.gbq.myproject.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gbq.myproject.R;
import com.gbq.myproject.base.BaseVMActivity;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseVMActivity<LoginViewModel> {

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private Button mEmailSignInButton;

    @Override
    protected int getContentId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        mEmailView = findViewById(R.id.email);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        mPasswordView = findViewById(R.id.password);
        mEmailSignInButton = findViewById(R.id.email_sign_in_button);
    }

    @Override
    protected void initData() {
        populateAutoComplete();

        mViewModel.getLoginPre().observe(this, aBoolean -> attemptLogin());
        mViewModel.getPasswordError().observe(this, s -> onViewError(mPasswordView, s));
        mViewModel.getEmailError().observe(this, s -> onViewError(mEmailView, s));
        mViewModel.getShowProcess().observe(this, this::showProgress);
        mViewModel.getOnLoginSuccess().observe(this, aBoolean -> {
            Toast.makeText(LoginActivity.this, "Login success", Toast.LENGTH_LONG).show();
            finish();
        });
        mViewModel.getRequestContacts().observe(this, this::requestContacts);
        mViewModel.getPopulateAutoComplete().observe(this, aBoolean -> initLoader());
        mViewModel.getEmailAdapter().observe(this, stringArrayAdapter -> mEmailView.setAdapter(stringArrayAdapter));

        mEmailSignInButton.setOnClickListener(view -> mViewModel.attemptLogin());
        mPasswordView.setOnEditorActionListener((textView, id, keyEvent) -> mViewModel.onEditorAction(id));
    }

    private void populateAutoComplete() {
        if (!mViewModel.mayRequestContacts()) {
            return;
        }
        initLoader();
    }

    private void initLoader(){
        //noinspection deprecation
        getSupportLoaderManager().initLoader(0, null, mViewModel);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void requestContacts(int requestCode) {
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, v -> requestPermissions(new String[]{READ_CONTACTS}, requestCode));
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, requestCode);
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        mViewModel.onRequestPermissionsResult(requestCode,grantResults);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        mViewModel.toLogin(email, password);
    }

    private void onViewError(EditText editText, String message) {
        editText.setError(message);
        editText.requestFocus();
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }
}

