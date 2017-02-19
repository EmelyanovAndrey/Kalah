<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ru">
  <head>
    <title>Kalah | Главная</title>
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
    <link href="https://fonts.googleapis.com/css?family=Orbitron:500,700" rel="stylesheet">
  </head>
  <body>
  <jsp:include page="head.jsp" />
    <main>
      <div class="slider z-depth-3">
        <div class="inner">
          <h1>Добро пожаловать в игру Калах</h1>
          <div id="slider" class="row container"><a href="#play" class="waves-effect waves-light btn-large">Играть</a><a href="#rules" class="waves-effect waves-light btn-large">Правила</a></div>
        </div>
      </div>
      <div id="play" class="vs-menu z-depth-3">
        <div class="comp right"><a href="new-game-ai.jsp">
            <div class="inner">
              <h2>VS AI</h2>
            </div></a></div>
        <div class="human left"><a href="game-list.jsp">
            <div class="inner">
              <h2>VS Human</h2>
            </div></a></div>
      </div>
      <div id="rules" class="row video">
        <div class="video-container z-depth-3">
          <iframe width="auto" height="100vh" src="http://www.youtube.com/embed/_vjbFCVnxBQ" frameborder="0"></iframe>
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