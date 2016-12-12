$(document).ready(->
  $('#game_list').find('tr').dblclick ->
    console.log 'blclick'
    $('#modal').modal('open')
    return
  return
)