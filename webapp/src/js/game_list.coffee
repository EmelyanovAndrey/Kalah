$(document).ready(->
  $('#game_list').find('tr').dblclick ->
    console.log 'blclick'
    $('#join').modal('open')
    return
  return
)