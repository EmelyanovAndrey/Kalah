<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ru">
  <head>
    <title>Kalah | Новая игра</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <!--Import Google Icon Font-->
    <link href="http://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <!--Fonts-->
    <link href="https://fonts.googleapis.com/css?family=Roboto:400,500&amp;subset=cyrillic" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css?family=Patua+One" rel="stylesheet">
    <!--Styles-->
    <link type="text/css" rel="stylesheet" href="css/main.css" media="screen,projection">
    <!--Let browser know website is optimized for mobile-->
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <!--Scripts-->
    <script type="text/javascript" src="js/main.js"></script>
    <script type="text/javascript" src="js/new-game-human.js"></script>
  </head>
  <body>
  <jsp:include page="head.jsp" />
    <main>
      <div class="new-game">
        <div class="container">
          <div class="row">
            <h1>Новая игра</h1>
          </div>
          <div class="row">
            <form>
              <div class="input-field col s12">
                <label for="count_lunk">Число лунок</label>
                <p class="range-field">
                  <input id="count_lunk" type="range" min="3" max="8">
                </p>
              </div>
              <div class="input-field col s12">
                <label for="count_stouns">Число камней</label>
                <p class="range-field">
                  <input id="count_stouns" type="range" min="6" max="12">
                </p>
              </div>
              <!--.input-field.col.s12
              label(for="level") Уровень сложности
              p.range-field
                  input#level(type="range", min="1", max="5")
              
              -->
              <div class="input-field col s12"><i class="material-icons prefix">perm_identity</i>
                <input id="friend" type="text" length="10" class="validate">
                <label for="friend">Пригласить друга</label>
              </div>
              <div class="col s12"><a id="new-game-human-btn" type="submit" name="action" class="btn waves-effect waves-light">Создать игру<i class="material-icons right">send</i></a></div>
            </form>
          </div>
        </div>
      </div>
    </main>
    <footer class="page-footer">
      <div id="more-footer" class="container">
        <div class="row">
          <div class="col l6 s12">
            <h5 class="white-text">Kalah</h5>
            <p class="grey-text text-lighten-4">Онлайн игра Калах</p>
          </div>
          <div class="col l4 offset-l2 s12">
            <h5 class="white-text">Ссылки</h5>
            <ul>
              <li><a href="http://ssau.ru/" target="_blank" class="grey-text text-lighten-3">СГАУ</a></li>
            </ul>
          </div>
        </div>
      </div>
      <div class="footer-copyright">
        <div class="container row">
          <div class="col s12 m6 l6">&copy; 2016 Design by Ilya Konstantinov</div>
          <div class="col s12 m6 l6"><a href="mailto:ilyakonst95@gmail.com" class="grey-text text-lighten-4 right">Web-master: admin@kalah.com</a></div>
        </div>
      </div>
    </footer>
  </body>
</html>