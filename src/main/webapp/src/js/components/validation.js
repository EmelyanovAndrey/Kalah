(function() {
  var characterValid, re_passValid;

  characterValid = function(item, min, max) {
    if (item.val().length >= min && item.val().length <= max) {
      return item.addClass('valid');
    } else {
      if (item.val().length < min || item.val().length > item.attr('lenght') || item.val().length > max) {
        return item.addClass('invalid');
      }
    }
  };

  re_passValid = function(pass, re_pass) {
    if (pass.hasClass('valid') && re_pass.val() === pass.val()) {
      return re_pass.addClass('valid');
    } else {
      if (re_pass.val() !== pass.val()) {
        return re_pass.addClass('invalid');
      }
    }
  };

  window.registrationValid = function() {
    var ava, email, name, password, re_pass;
    re_pass = $('#registration').find('#re-password');
    password = $('#registration').find('#password');
    name = $('#registration').find('#name');
    email = $('#registration').find('#email');
    ava = $('#registration').find('#icons');
    characterValid(name, name.attr('minlength'), name.attr('maxlength'));
    characterValid(password, password.attr('minlength'), password.attr('maxlength'));
    characterValid(email, email.attr('minlength'), email.attr('maxlength'));
    re_passValid(password, re_pass);
    if (name.hasClass('valid') && email.hasClass('valid') && password.hasClass('valid') && re_pass.hasClass('valid') && ava.val() !== null) {
      return true;
    } else {
      return false;
    }
  };

  window.loginValid = function() {
    var name, password;
    name = $('#loginin').find('#name');
    password = $('#loginin').find('#password');
    characterValid(name, name.attr('minlength'), name.attr('maxlength'));
    characterValid(password, password.attr('minlength'), password.attr('maxlength'));
    if (name.hasClass('valid') && password.hasClass('valid')) {
      return true;
    } else {
      return false;
    }
  };

  window.profileValid = function() {
    var email, name, password, re_pass;
    name = $('#profile').find('#name');
    password = $('#profile').find('#password');
    re_pass = $('#profile').find('#re-password');
    email = $('#profile').find('#email');
    characterValid(name, name.attr('minlength'), name.attr('maxlength'));
    characterValid(password, password.attr('minlength'), password.attr('maxlength'));
    characterValid(email, email.attr('minlength'), email.attr('maxlength'));
    re_passValid(password, re_pass);
    if (name.hasClass('valid') && email.hasClass('valid') && password.hasClass('valid') && re_pass.hasClass('valid')) {
      return true;
    } else {
      return false;
    }
  };

}).call(this);
