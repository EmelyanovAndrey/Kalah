<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Справочная система</title>
    <link rel="stylesheet" href="/InformationSystemWEB/css/main.css">

    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.7/jquery.js"></script>
    <script src="http://ajax.aspnetcdn.com/ajax/jquery.validate/1.15.0/jquery.validate.js"></script>
    <script type="text/javascript">
        $('document').ready(function () {
            $('#registration').validate({
                //Правила
                rules: {
                    password_confirm: {
                        equalTo: "#password"
                    }
                },
                //Тексты предупреждений
                messages: {
                    password_confirm: "Пароли не совпадают"
                },
                //Обработка и отправка данных
                submitHandler: function () {
                    $.ajax({
                        url: '/login',
                        type: 'GET',
                        data: {
                            request: 'register',
                            login: $("#login").attr("value"),
                            password: $("#password").attr("value")
                        },
                        success: function (result) {
                            if (result === "success") {

                            }
                            if (result === "error") {
                                $("#loginError").text("Имя уже существует");
                            }
                        },
                        error: function (jqXHR, textStatus, errorThrown) {
                            alert(errorThrown);
                        }
                    });
                }
            });
            jQuery.validator.addMethod("correctpassword", function (value, element) {
                return /([a-zA-Z0-9]{4,16})/.test(value);
            }, 'Пароль должен состоять только из латинских букв и цифр');
            $("#password").rules("add", {
                required: true,
                minlength: 4,
                correctpasswordword: true,
                maxlength: 16,
                messages: {
                    required: "Вы забыли про пароль",
                    minlength: jQuery.validator.format("Пароль должен содержать не менее {0} символов"),
                    maxlength: jQuery.validator.format("Пароль должен содержать не более {0} символов")
                }
            });
            jQuery.validator.addMethod("correctlogin", function (value, element) {
                return /([_a-zA-Z][_a-zA-Z0-9 ]{3,31})/.test(value);
            }, "Имя должно начинаться с латинской буквы или символа '_' и состоять из них же или цифр и пробелов");
            $("#login").rules("add", {
                required: true,
                minlength: 4,
                correctlogin: true,
                maxlength: 32,
                messages: {
                    required: "Вы забыли про имя",
                    minlength: jQuery.validator.format("Имя должно содержать не менее {0} символов"),
                    maxlength: jQuery.validator.format("Имя должно содержать не более {0} символов")
                }
            });

        });


    </script>
</head>
<body>

    <main>
        <section class="row">
            <div id="form_wrapper" class="form_wrapper">

                <form id="registration">
                    <h3>Данные пользователя</h3>
                    <div>
                        <label>Логин:</label>
                        <input id="login" name="login" type="text" value=""/>
                        <span id="loginError"></span>
                    </div>
                    <div>
                        <label>Пароль:</label>
                        <input id="password" name="password" type="passwordword" value=""/>
                    </div>
                    <div>
                        <label>Подтверждение пароля:</label>
                        <input id="password_confirm" name="password_confirm" type="passwordword"/>
                    </div>
                    <input type="submit" id="finish" value="Закончить" onclick="submitHandler();"/>
                </form>
            </div>
        </section>
    </main>


</div>
</body>
</html>