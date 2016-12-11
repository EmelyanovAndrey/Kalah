$(document).ready(->
  board = $('#board')
  enemy_list = board.find('#enemy_list')
  gamer_list = board.find('#gamer_list')


  count = [0..6]

  enemy_list.append('<td rowspan=2 class="kalah enemy-obj" id="enemy"><span>0</span></td>')

  for i in count
    enemy_list.append('<td class="lunka enemy-obj" id="enemy"><span>6</span></td>')

  enemy_list.append('<td rowspan=2 class="kalah" id="gamer"><span>0</span></td>')

  for i in count
    gamer_list.append('<td class="lunka" id="gamer"><span>6</span></td>')

  board.find('#gamer').onclick ->

)