characterValid = (item, min, max) ->
  if item.val().length >= min and item.val().length <= max
    item.addClass('valid')
  else
    if item.val().length < min or item.val().length > item.attr('lenght') or item.val().length > max
      item.addClass('invalid')

re_passValid = (pass, re_pass) ->
  if pass.hasClass('valid') and re_pass.val() is pass.val()
    re_pass.addClass('valid')
  else
    if re_pass.val() isnt pass.val()
      re_pass.addClass('invalid')

$(document).ready( ->

  $('#loginin').find('#login').click( ->

    name = $('#loginin').find('#name')
    password = $('#loginin').find('#password')

    characterValid(name, name.attr('minlength'), name.attr('maxlength'))
    characterValid(password, password.attr('minlength'), password.attr('maxlength'))

    if name.hasClass('valid') and password.hasClass('valid')
      console.log 'login: true'

    return
  )

  $('#registration').find('#signup').click( ->

    re_pass = $('#registration').find('#re-password')
    password = $('#registration').find('#password')
    name = $('#registration').find('#name')
    email = $('#registration').find('#email')
    ava = $('#registration').find('#icons')

    characterValid(name, name.attr('minlength'), name.attr('maxlength'))
    characterValid(password, password.attr('minlength'), password.attr('maxlength'))
    characterValid(email, email.attr('minlength'), email.attr('maxlength'))

    re_passValid(password, re_pass)

    if name.hasClass('valid') and email.hasClass('valid') and password.hasClass('valid') and re_pass.hasClass('valid') and ava.val() isnt null
      console.log 'regist: true'
    else
      console.log 'regist: false'

#    console.log 'ava: ' + ava.val()
    return
  )

  $('#profile').find('#change').click(->
    name = $('#profile').find('#name')
    password = $('#profile').find('#password')
    re_pass = $('#profile').find('#re-password')
    email = $('#profile').find('#email')

    characterValid(name, name.attr('minlength'), name.attr('maxlength'))
    characterValid(password, password.attr('minlength'), password.attr('maxlength'))
    characterValid(email, email.attr('minlength'), email.attr('maxlength'))

    re_passValid(password, re_pass)

    if name.hasClass('valid') and email.hasClass('valid') and password.hasClass('valid') and re_pass.hasClass('valid')
      console.log 'change: true'
    else
      console.log 'change: false'
  )
)