/**
 * Created by ilya on 14.12.16.
 */
var profile_img_rendering = function (src) {
    $('#profile_img').append('<img src="'+ src + '">')
}

$(document).ready(function () {
   profile_img_rendering('/img/marcus.jpg');
});