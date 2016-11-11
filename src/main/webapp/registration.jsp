<!DOCTYPE html>
<html>
<head>

    <title>Справочная система</title>

    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.7/jquery.js"></script>
    <script src="http://ajax.aspnetcdn.com/ajax/jquery.validate/1.15.0/jquery.validate.js"></script>

</head>
<body>

    <main>
        <section class="row">
            <div id="form_wrapper" class="form_wrapper">

                <form id="registration" action="GET" >
                    <h3>User data</h3>
                    <div>
                        <label>Login:</label>
                        <input id="login" name="login" type="text" value=""/>
                        <span id="loginError"></span>
                    </div>
                    <div>
                        <label>Password:</label>
                        <input id="password" name="password" type="passwordword" value=""/>
                    </div>
                    <div>
                        <label>Password confirm:</label>
                        <input id="password_confirm" name="password_confirm" type="passwordword"/>
                    </div>
                    <input type="submit" id="finish" value="Send" onclick="submitHandler();"/>
                </form>
            </div>
        </section>
    </main>


</div>
</body>
</html>