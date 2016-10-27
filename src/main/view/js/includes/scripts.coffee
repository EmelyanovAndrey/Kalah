  #Main-page
  human = "#play .human"
  comp = "#play .comp"

  #Определение Firefox и определение vs-menu
  $ ->
    if jQuery.browser.mozilla is true
      $(human).addClass "simple"
      $(comp).addClass "simple"
    $('select').material_select()
    return

  #Затемнение элемента vs-menu
  vs_item = (item, speed = 500, opacity = 0.6) ->
    $ item
      .mouseenter ->
        $(@)
          .find ".inner"
          .fadeTo speed, opacity, ->
        $(@)
          .css {'cursor': 'pointer'}
        return

    $ item
      .mouseleave ->
        $(@)
          .find ".inner"
          .fadeTo speed, 0, ->
        return

    return

  $(document).ready ->

    $('input#input_text, textarea#textarea1').characterCounter()

    #Плавный скролл
    $ "#slider"
      .on "click", "a", (event) ->
        event.preventDefault()
        id = $(@).attr('href')
        top = $(id).offset().top
        $('body, html').animate {scrollTop: top}, 350

        return

    #Затемнение vs
    vs_item(human)
    vs_item(comp)

    #Game-page
    lunka = $(".board .lunka")
    kalah = $(".board .kalah")

    lunka.mousedown ->
      if (!$(@).hasClass("enemy-obj"))
        $(@)
          .addClass "active"
      return
    lunka.mouseup ->
      $(@)
        .removeClass "active"
      return

#    kalah.mousedown ->
#      $(@)
#        .addClass "active"
#      return
#    kalah.mouseup ->
#      $(@)
#        .removeClass "active"
#      return

    #Registration
      return

#    $(function() {
#      $('select').material_select();
#      $(".dropdown-button").dropdown();
#      Materialize.updateTextFields();
#      $('.collapsible').collapsible({
#        accordion: false // A setting that changes the collapsible behavior to expandable instead of the default accordion style
#      });
#      $("table tbody tr").dblclick(function () {
#        $('#element').openModal();
#      });
#    });
    return


