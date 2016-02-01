<%@ page contentType="text/html; charset=UTF-8" %>
<header class="row">
  <nav class="navbar navbar-default">
    <div class="container-fluid">
      <div class="navbar-header">
        <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-navbar-collapse-1" aria-expanded="false">
          <span class="sr-only">Toggle navigation</span>
          <span class="icon-bar"></span>
          <span class="icon-bar"></span>
          <span class="icon-bar"></span>
        </button>
        <a class="navbar-brand" href="profile.jsp">QuoiDNeuf</a>
      </div>

      <div class="collapse navbar-collapse" id="bs-navbar-collapse-1">
        <ul class="nav navbar-nav">
          <li><a href="profile.jsp">Profil</a></li>
          <li class="dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false" id="navbar_amis">Amis <span class="caret"></span></a>
            <ul class="dropdown-menu" id="dropdown_navbar_amis"></ul>
          </li>
          <li class="dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false" id="navbar_discussions">Discussions <span class="caret"></span></a>
            <ul class="dropdown-menu" id="dropdown_navbar_discussions"></ul>
          </li>
          <li class="hidden" id="li_membres"><a data-toggle="collapse" data-target="#membres">Membres</a></li>
          <li><a href="#" id="navbar_deconnexion">Déconnexion</a></li>
        </ul>
      </div>
    </div>
  </nav>
</header>
