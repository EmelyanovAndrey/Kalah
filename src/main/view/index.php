<!DOCTYPE html>
  <html lang="ru">
    <head>
        <title>Главная</title>
        <? include_once('head.php'); ?>
    </head>
    <body>
        <? include_once('header.php'); ?>
        
        
        <div class="hellow z-depth-3">
            <div class="inner">
                <h1>Добро пожаловать в игру Калах</h1>
                <div class="row container">
                    
                        <a id="btn" class="waves-effect waves-light btn-large" href="#menu">Правила</a>
                    
                        <a id="btn" class="waves-effect waves-light btn-large" href="#menu">Играть</a>
                 
                </div>
                
            </div>
        </div>
        
        <div id="menu" class="row main-menu z-depth-3">
            <div class="col s12 m6 l6 comp">
                <div class="inner">
                    <h3>Играть с компьютером</h3>
                </div>
            </div>
            <div class="col s12 m6 l6 human">
                <div class="inner">
                    <h3>Играть с человеком</h3>
                </div>
            </div>
        </div>
        
        <? include_once('rules.php');?>
        <? include_once('board.php');?>
        
        <script>
            $('.carousel.carousel-slider').carousel({full_width: true});
            
        </script>
        
        <? include_once('footer.php'); ?>
    </body>
  </html> 