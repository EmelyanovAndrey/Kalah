var annimation = function(item) {
  console.log('anim');

    item.on('click', function() {

        // if (!item.hasClass('enemy')) {
          // setTimeout(function () {
      alert('awdawd');
            console.log('lunka_active');

            item.addClass('active');
          // }, time);
        //}

    });

    /*item.mouseup(function() {
      item.removeClass('active');
    });*/
};

$(document).ready(function() {
  var board = $('#board');

  var enemy_list = board.find('#enemy_list');
  var gamer_list = board.find('#gamer_list');

  var kalah = board.find('.kalah');

  var lunka = gamer_list.find('.lunka');

  var stouns_count = 6;


  enemy_list.append('<td rowspan=2 class="kalah enemy" id="enemy_k"><span>0</span></td>');

  for (var i = 0; i  < 6; i++) {
    enemy_list.append('<td class="lunka enemy" id="enemy_' + i + '"><span>' + stouns_count + '</span></td>');
  }

  enemy_list.append('<td rowspan=2 class="kalah" id="gamer_k"><span>0</span></td>');

  for (var i = 0; i  < 6; i++) {
    gamer_list.append('<td class="lunka" id="gamer_' + i + '"><span>' + stouns_count + '</span></td>');
  }

  // annimation(kalah);
});