(function() {
  var comp, human, vs_item;

  human = "#play .human";

  comp = "#play .comp";

  $(function() {
    if (jQuery.browser.mozilla === true) {
      $(human).addClass("simple");
      $(comp).addClass("simple");
    }
    $('select').material_select();
  });

  vs_item = function(item, speed, opacity) {
    if (speed == null) {
      speed = 500;
    }
    if (opacity == null) {
      opacity = 0.6;
    }
    $(item).mouseenter(function() {
      $(this).find(".inner").fadeTo(speed, opacity, function() {});
      $(this).css({
        'cursor': 'pointer'
      });
    });
    $(item).mouseleave(function() {
      $(this).find(".inner").fadeTo(speed, 0, function() {});
    });
  };

  $(document).ready(function() {
    var kalah, lunka;
    $('input#input_text, textarea#textarea1').characterCounter();
    $("#slider").on("click", "a", function(event) {
      var id, top;
      event.preventDefault();
      id = $(this).attr('href');
      top = $(id).offset().top;
      $('body, html').animate({
        scrollTop: top
      }, 350);
    });
    vs_item(human);
    vs_item(comp);
    lunka = $(".board .lunka");
    kalah = $(".board .kalah");
    lunka.mousedown(function() {
      if (!$(this).hasClass("enemy-obj")) {
        $(this).addClass("active");
      }
    });
    lunka.mouseup(function() {
      $(this).removeClass("active");
    });
    return;
  });

}).call(this);

