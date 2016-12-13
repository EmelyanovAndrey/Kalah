$(document).ready(function() {
  var board, enemy_list, gamer_list;

  board = $('#board');
  enemy_list = board.find('#enemy_list');
  gamer_list = board.find('#gamer_list');

  enemy_list.append('<td rowspan=2 class="kalah enemy-obj" id="enemy"><span>0</span></td>');

  for (var i = 0; i  < 6; i++) {
    enemy_list.append('<td class="lunka enemy-obj" id="gamer_' + i + '"><span>6</span></td>');
  }

  enemy_list.append('<td rowspan=2 class="kalah" id="gamer"><span>0</span></td>');

  for (var i = 0; i  < 6; i++) {
    gamer_list.append('<td class="lunka" id="gamer_' + i + '"><span>6</span></td>');
  }
});
