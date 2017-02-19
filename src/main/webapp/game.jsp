<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ru">
<head>
  <title>Kalah | Игра</title>
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
  <script type="text/javascript" src="js/game.js"></script>
</head>
<body>
<jsp:include page="head.jsp" />
<main>
  <div class="game">
    <div class="container">
      <div class="row">
        <div class="apponent">
        </div>
      </div>
      <div class="row">
        <table class="board no-select">

        </table>
      </div>
      <div class="row">
        <div class="player">
        </div>
      </div>
      <div class="row">
        <div class="save-game">
          <div class="col s12">
            <button disabled="disabled" id="save-game" type="submit" name="action" class="btn waves-effect waves-light large">Сохранить игру<i class="material-icons right">send</i></button>
          </div>
        </div>
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
<div id="messege" class="modal">
  <div class="modal-content">
    <h4>Сообщение</h4>
    <p>К вам хочет присоеденится <span id="join-name"></span></p>
  </div>
  <div class="modal-footer"><a href="#" id="deny" class="modal-action modal-close waves-effect waves-red btn-flat">Отклонить</a><a href="#" id="gran" class="modal-action modal-close waves-effect waves-green btn-flat">Принять</a></div>
</div>
<div id="inform" class="modal">
  <div class="modal-content">
    <h4>Сообщение</h4>
    <p id="inform-message"></p>
  </div>
  <div class="modal-footer"><a href="#" id="ok" class="modal-action modal-close waves-effect waves-red btn-flat">Ок</a></div>
</div>
</body>
</html>