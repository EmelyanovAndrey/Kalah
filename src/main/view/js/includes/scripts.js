
    $(function(){
        if (jQuery.browser.mozilla == true) {

            $("#play .comp").addClass("simple");
            $("#play .human").addClass("simple")
        }


    });

    $(document).ready(function () {

        $("#play .human .inner").addClass("hover");

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

        $("#play .comp").mouseenter(function () {
            $(this).find(".inner").addClass("hover");
            $("#play .human .inner").removeClass("hover");
        });

        $("#play .human").mouseenter(function () {
            $(this).find(".inner").addClass("hover");
            $("#play .comp .inner").removeClass("hover");
        });

    });