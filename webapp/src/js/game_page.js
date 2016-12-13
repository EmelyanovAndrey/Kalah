var annimation = function(item) {

  $(this).mousedown(function() {
    if (!$(this).hasClass('enemy-obj')) {
      return $(this).addClass('active');
    }
  });

  $(this).mouseup(function() {
    return $(this).removeClass('active');
  });
};

$(document).ready(function() {
  var kalah = $('.board .kalah');
  var lunka = $('.board .lunka');

  annimation(lunka);
});