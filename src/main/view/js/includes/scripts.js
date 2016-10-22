//VS

$(function(){
    if (jQuery.browser.mozilla == true) {

        $("#play .comp").addClass("simple");
        $("#play .human").addClass("simple")
    }
});

$(document).ready(function () {

    $("#hellow").on("click", "a", function (event) {
        //отменяем стандартную обработку нажатия по ссылке
        event.preventDefault();

        //забираем идентификатор бока с атрибута href
        var id = $(this).attr('href'),

            //узнаем высоту от начала страницы до блока на который ссылается якорь
            top = $(id).offset().top;

        //анимируем переход на расстояние - top за 350 мс
        $('body,html').animate({scrollTop: top}, 350);
    });

    var comp = $("#play .comp");
    var human = $("#play .human");
    var speed = 1000;

    $(comp).mouseenter(function () {
        //$(comp).find(".inner").addClass("hover");
        //$(human).find(".inner").removeClass("hover");
        $(this).find(".inner").fadeTo(speed , 0.5, function() {});
        $(this).css({'cursor': 'pointer'});
    });

    $(comp).mouseleave(function () {
        $(this).find(".inner").fadeTo(speed , 0, function() {});
    });

   $(human).mouseenter(function () {
      // $(human).find(".inner").addClass("hover");
      // $(comp).find(".inner").removeClass("hover");
       $(this).find(".inner").fadeTo(speed , 0.5, function() {});
       $(this).css({'cursor': 'pointer'});
    });

    $(human).mouseleave(function () {
        $(this).find(".inner").fadeTo(speed , 0, function() {});
    });

});