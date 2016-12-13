$(document).ready(function () {
  $('#game_list').find('tr').dblclick(function () {

    console.log('blclick');

    $('#join').modal('open');

  });
});