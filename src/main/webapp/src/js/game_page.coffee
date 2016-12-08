

annimation = (item) ->

  $(@).mousedown(->
    if (not $(@).hasClass('enemy-obj'))
      $(@).addClass('active')
  )

  $(@).mouseup(->
    $(@).removeClass('active')
  )

$(document).ready(->

  kalah = $('.board .kalah')
  lunka = $('.board .lunka')

  annimation(lunka)
)