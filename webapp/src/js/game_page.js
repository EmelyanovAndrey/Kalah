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


  var board = $('#board');
  var enemy_list = board.find('#enemy_list');
  var gamer_list = board.find('#gamer_list');

  enemy_list.append('<td rowspan=2 class="kalah enemy-obj" id="enemy"><span>0</span></td>');

  for (var i = 0; i  < 6; i++) {
    enemy_list.append('<td class="lunka enemy-obj" id="enemy_' + i + '"><span>6</span></td>');
  }

  enemy_list.append('<td rowspan=2 class="kalah" id="gamer"><span>0</span></td>');

  for (var i = 0; i  < 6; i++) {
    gamer_list.append('<td class="lunka" id="gamer_' + i + '"><span>6</span></td>');
  }
});