<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<header>
    <div class="navbar">
        <ul id="slide-out" class="side-nav">
            <li>
                <div class="userView">
                    <h1>Kalah</h1>
                </div>
            </li>
            <li><a href="index.jsp" class="grey-text text-lighten-3">Главная<i class="material-icons">view_stream</i></a></li>
            <li><a href="about.jsp" class="grey-text text-lighten-3">Об игре<i class="material-icons">info</i></a></li>
            <li><a href="game-list.jsp" class="grey-text text-lighten-3">Играть с человеком<i class="material-icons">perm_identity</i></a></li>
            <li><a href="new-game-ai.jsp" class="grey-text text-lighten-3">Играть с компьютером<i class="material-icons">android</i></a></li>
            <shiro:authenticated>
                <li><a href="profile.jsp" class="grey-text text-lighten-3">Профиль</a></li>
                <li><a href="logout" class="grey-text text-lighten-3">Выход</a></li>
            </shiro:authenticated>
            <shiro:notAuthenticated>
                <li><a href="login.jsp" class="grey-text text-lighten-3">Вход</a></li>
            </shiro:notAuthenticated>
        </ul>
        <nav>
            <div class="nav-wrapper container">
                <div class="large-nav-bar"><a href="" target="_blank" class="brand-logo">Kalah</a>
                    <ul class="right">
                        <li><a href="index.jsp" target="_blank" class="grey-text text-lighten-3">Главная</a></li>
                        <li><a href="about.jsp" target="_blank" class="grey-text text-lighten-3">Об игре</a></li>
                        <li><a href="game-list.jsp" target="_blank" class="grey-text text-lighten-3">Играть с человеком</a></li>
                        <li><a href="new-game-ai.jsp" target="_blank" class="grey-text text-lighten-3">Играть с компьютером</a></li>
                        <shiro:authenticated>
                            <li><a href="profile.jsp" class="grey-text text-lighten-3">Профиль</a></li>
                            <li><a href="logout" class="grey-text text-lighten-3">Выход</a></li>
                        </shiro:authenticated>
                        <shiro:notAuthenticated>
                            <li><a href="login.jsp" class="grey-text text-lighten-3">Вход</a></li>
                        </shiro:notAuthenticated>
                    </ul>
                </div>
                <div class="mini-nav-bar"><a href="#!" data-activates="slide-out" class="button-collapse"><i class="material-icons">menu</i></a><a href="" data-activates="slide-out" class="brand-logo">Kalah</a></div>
            </div>
        </nav>
    </div>
</header>