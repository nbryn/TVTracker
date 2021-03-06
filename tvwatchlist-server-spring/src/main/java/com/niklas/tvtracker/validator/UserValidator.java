package com.niklas.tvtracker.validator;

import com.niklas.tvtracker.entities.User;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


@Component
public class UserValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object object, Errors errors) {

        User user = (User) object;

        if(user.getPassword().length() < 4) {
            errors.rejectValue("password", "Length", "Password must be min 4 characters");
        }

        if(!user.getPassword().equals(user.getVerifyPassword())) {
            errors.rejectValue("password", "Match", "Passwords must match");
        }

    }
}
