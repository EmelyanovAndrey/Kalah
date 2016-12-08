$(document).ready( ->

  vs_item = (item, speed, opacity) ->
    if speed is null
      speed = 500
    if opacity is null
      opacity = 0.6

    item.on('mouseenter', ->
      $(@).find('.inner').fadeTo(500,0.6)

      $(@).css({
        'cursor': 'pointer'
      })
      return
    )

    item.on('mouseleave', ->
      $(@).find('.inner').fadeTo(500,0)
    )
    return


  human = $("#play .human")

  comp = $("#play .comp")

  if jQuery.browser.mozilla
    human.addClass('simple')
    comp.addClass('simple')

  $('input#input_text, textarea#textarea1').characterCounter()
  $('.button-collapse').sideNav()

  $("#slider").on("click", "a", (event) ->

    event.preventDefault()

    id = $(this).attr('href')
    top = $(id).offset().top

    $('body, html').animate({scrollTop: top}, 350)

    return
  )

  vs_item(human)
  vs_item(comp)

  return
)