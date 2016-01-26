<!DOCTYPE html>
<html lang="fr">
<head>
  <meta charset="utf-8">
  <!-- Commentée car ne passe pas la validation W3C -->
  <!-- <meta http-equiv="X-UA-Compatible" content="IE=edge"> -->
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <meta name="description" content="Tests d'utilisation de Boostrap pour le projet WEB">
  <%@ page contentType="text/html; charset=UTF-8" %>

  <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
  <title>Discussion - QuoiDNeuf</title>

  <link href="../css/style_classic.css" rel="stylesheet">
  <!-- Bootstrap -->
  <link href="../css/bootstrap.min.css" rel="stylesheet">

  <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
  <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
  <!--[if lt IE 9]>
  <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
  <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
  <![endif]-->
</head>
<body>
  <div class="container" id="corps">

    <!-- Header -->
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

          <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
            <ul class="nav navbar-nav">
              <li><a href="profile.jsp">Profil</a></li>
              <li class="dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false" id="navbar_amis">Amis <span class="caret"></span></a>
                <ul class="dropdown-menu" id="dropdown_navbar_amis"></ul>
              </li>
              <li class="dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false" id="navbar_discussions">Discussions <span class="caret"></span></a>
                <ul class="dropdown-menu" id="dropdown_navbar_discussions"></ul>
              </li>
              <li><a data-toggle="collapse" data-target="#membres">Membres</a></li>
              <li><a href="#" id="navbar_deconnexion">Déconnexion</a></li>
            </ul>
          </div>
        </div>
      </nav>
    </header>
    <!-- Fin Header -->

    <!-- Contenu -->
    <div class="row">
      <!-- Discussion -->
      <div class="col-lg-9 col-md-9 contents img-rounded">
        <h2 id="discussion_name">Discussion</h2>

        <!-- Liste des messages -->
        <div class="row">
          <!-- Label ne passe pas le validator -->
          <!--<label for="messages" hidden="true">Messages</label> -->
          <textarea name="name" rows="8" cols="40" class="col-lg-12 col-md-12 col-sm-12 col-xs-12" id="messages" readonly></textarea>
        </div>
        <!-- Fin liste des messages-->

        <!-- Message à envoyer -->
        <div class="row">
          <div class="col-lg-11 col-md-11 col-sm-11 col-xs-11"><input type="text" class="form-control" placeholder="Message..." id="message_input"></input></div>
          <button class="btn btn-default col-lg-1 col-md-1 col-sm-1 col-xs-1" type="button" id="send_message"><span class="glyphicon glyphicon-send" aria-hidden="true"></span></button>
        </div>
        <!-- Fin message à envoyer -->
      </div>
      <!-- Fin Discussion -->

      <!-- Membres -->
      <div class="col-lg-2 col-md-2 contents img-rounded collapse col-md-offset-1 col-lg-offset-1" id="membres">
        <h2>Membres</h2>
        <ul id="discussion_membres"></ul>
        <!-- Barre de recherche pour ajout à la discussion : -->
        <div class="row">
          <div class="input-group">
            <span class="input-group-btn">
              <button class="btn btn-default" type="button"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></button>
            </span>
            <input type="text" class="form-control" placeholder="Ajouter utilisateur...">
          </div><!-- /input-group -->
        </div>
      </div>
      <!-- Fin Membres -->
    </div>
    <!-- Fin contenu -->

    <!-- Footer -->
    <footer class="row panel panel-default">
      <div class="panel-body">
        <div class="pull-left" id="credits">
          Projet Web/BDD - WhatsApp-Like - Cattez Edouard - Ferro Thomas - Licence Pro DA2I - 2015/2016
        </div>

        <a href="https://www-iut.univ-lille1.fr/" class="pull-right"><img src="../img/iut.png" alt="Logo IUT" width="80" height="50"/></a>
        <a href="https://github.com/ecattez/quoidneuf" class="pull-right"><img src="../img/github.png" alt="Logo GitHub" class="pull-right" width="50" height="50"/></a>
      </div>
    </footer>
    <!-- Footer -->
  </div>

  <script type="text/javascript" src="../scripts/jquery.min.js"></script>
  <script type="text/javascript" src="../scripts/scripts_ajax.js"></script>
  <script type="text/javascript" src="../scripts/navbar.js"></script>
  <script type="text/javascript" src="../scripts/discussion.js"></script>
  <script type="text/javascript" src="../js/bootstrap.js"></script>
  <script type="text/javascript">initNavbar(${user});</script>
  <script type="text/javascript">initDiscussionPage(${param.id});</script>
</body>
</html>
