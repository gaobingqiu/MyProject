package com.gbq.myproject.login;

import dagger.Component;

@Component
public interface LoginComponent {
    void inject(LoginViewModel loginViewModel);
}
