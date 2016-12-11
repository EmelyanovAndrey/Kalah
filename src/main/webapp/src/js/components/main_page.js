(function() {
  $(document).ready(function() {
    var comp, human, vs_item;
    vs_item = function(item, speed, opacity) {
      if (speed === null) {
        speed = 500;
      }
      if (opacity === null) {
        opacity = 0.6;
      }
      item.on('mouseenter', function() {
        $(this).find('.inner').fadeTo(500, 0.6);
        $(this).css({
          'cursor': 'pointer'
        });
      });
      item.on('mouseleave', function() {
        return $(this).find('.inner').fadeTo(500, 0);
      });
    };
    human = $("#play .human");
    comp = $("#play .comp");
    if (jQuery.browser.mozilla) {
      human.addClass('simple');
      comp.addClass('simple');
    }
    $('input#input_text, textarea#textarea1').characterCounter();
    $('.button-collapse').sideNav();
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
  });

}).call(this);
