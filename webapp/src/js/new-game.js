var range_input = function(item) {
    var label = item.find('label');
    var range = item.find('input');

    var txt = label.text();
    console.log(txt);

    range.on('input', function () {
        label.html(txt + ': <div class="count">' + range.val() + '</div>');
    });
};

$(document).ready(function () {
    range_input($('#lunks_count'));
    range_input($('#stouns_count'));
    range_input($('#level_count'));
});