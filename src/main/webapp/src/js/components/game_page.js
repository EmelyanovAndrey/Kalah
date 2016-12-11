(function() {
  var annimation;

  annimation = function(item) {
    $(this).mousedown(function() {
      if (!$(this).hasClass('enemy-obj')) {
        return $(this).addClass('active');
      }
    });
    return $(this).mouseup(function() {
      return $(this).removeClass('active');
    });
  };

  $(document).ready(function() {
    var kalah, lunka;
    kalah = $('.board .kalah');
    lunka = $('.board .lunka');
    return annimation(lunka);
  });

}).call(this);
