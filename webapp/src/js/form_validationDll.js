var characterValid = function(item, min, max) {
    if (item.val().length >= min && item.val().length <= max) {
        return item.addClass('valid');
    } else {
        if (item.val().length < min || item.val().length > item.attr('lenght') || item.val().length > max) {
            return item.addClass('invalid');
        }
    }
};

var re_passValid = function(pass, re_pass) {
    if (pass.hasClass('valid') && re_pass.val() === pass.val()) {
        return re_pass.addClass('valid');
    } else {
        if (re_pass.val() !== pass.val()) {
            return re_pass.addClass('invalid');
        }
    }
};

var loginValid = function () {
    $('#loginin').find('#login').click(function() {
        var name = $('#loginin').find('#name');
        var password = $('#loginin').find('#password');

        characterValid(name, name.attr('minlength'), name.attr('maxlength'));
        characterValid(password, password.attr('minlength'), password.attr('maxlength'));

        if (name.hasClass('valid') && password.hasClass('valid')) {
            return true;
        }
        else return false;
    });
};

var registrValid = function () {
    $('#registration').find('#signup').click(function() {
        var re_pass = $('#registration').find('#re-password');
        var password = $('#registration').find('#password');
        var name = $('#registration').find('#name');
        var email = $('#registration').find('#email');
        var ava = $('#registration').find('#icons');

        characterValid(name, name.attr('minlength'), name.attr('maxlength'));
        characterValid(password, password.attr('minlength'), password.attr('maxlength'));
        characterValid(email, email.attr('minlength'), email.attr('maxlength'));
        re_passValid(password, re_pass);

        if (name.hasClass('valid') && email.hasClass('valid') && password.hasClass('valid') && re_pass.hasClass('valid') && ava.val() !== null) {
            return true;
        } else {
            return false;
        }
    });
};

var changeProfileValid = function () {
    $('#profile').find('#change').click(function() {

        var name = $('#profile').find('#name');
        var password = $('#profile').find('#password');
        var re_pass = $('#profile').find('#re-password');
        var email = $('#profile').find('#email');

        characterValid(name, name.attr('minlength'), name.attr('maxlength'));
        characterValid(password, password.attr('minlength'), password.attr('maxlength'));
        characterValid(email, email.attr('minlength'), email.attr('maxlength'));
        re_passValid(password, re_pass);

        if (name.hasClass('valid') && email.hasClass('valid') && password.hasClass('valid') && re_pass.hasClass('valid')) {
            return true;
        } else {
            return false;
        }
    });
};

$(document).ready(function() {
    if (loginValid()) {
        console.log('login: true');
    }
    else console.log('login: false')


    if (registrValid()) {
        console.log('regist: true');
    } else {
        console.log('regist: false');
    }

    if (changeProfileValid()) {
        console.log('change: true');
    } else {
        console.log('change: false');
    }
});