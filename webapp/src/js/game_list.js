$(document).ready(function () {
  $('#game_list').find('tbody tr').dblclick(function () {

    console.log('blclick');

    $('#join').modal('open');

  });
});