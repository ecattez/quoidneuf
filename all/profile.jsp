<!DOCTYPE html>
<html lang="fr">
<head>
  <meta charset="UTF-8">
  <!-- Commentée car ne passe pas la validation W3C -->
  <!-- <meta http-equiv="X-UA-Compatible" content="IE=edge"> -->
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <meta name="description" content="Tests d'utilisation de Boostrap pour le projet WEB">
  <%@ page contentType="text/html; charset=UTF-8" %>

  <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
  <title>Profil de - QuoiDNeuf</title>

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
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
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
              <li><a href="#" id="navbar_deconnexion">Déconnexion</a></li>
            </ul>
          </div>
        </div>
      </nav>
    </header>
    <!-- Fin Header -->

    <!-- Contenu -->
    <div class="row">

      <!-- Amis -->
      <div class="col-lg-3 col-md-3 contents center_div img-rounded">
        <h2>Amis</h2>
        <div id="amis">
          <div class="row">
            <div class="col-lg-8 col-md-8 col-sm-8 col-xs-8">Nom ami1</div>
            <div class="col-lg-1 col-lg-offset-1 col-md-1 col-md-offset-1 col-sm-1 col-sm-offset-1 col-xs-1 col-xs-offset-1"><button type="button" name="button_add_friend">+</button></div>
          </div>
        </div>
      </div>
      <!-- Fin amis -->

      <!-- Profil -->
      <div class="col-lg-8 col-lg-offset-1 col-md-8 col-md-offset-1 contents center_div img-rounded">
        <h2>Profil de <span id="nom_utilisateur"></span></h2>
        <!-- Informations -->
        <div class="row">
          <h3>Informations</h3>
          <div class="row">
            <form class="col-lg-12" action="#" method="post">
              <div class="form-group col-lg-4">
                <label for="birth_date">Date de naissance :</label>
                <input class="form-control" type="text" id="birth_date" name="birth_date" value="08/12/1968" />
              </div>
              <div class="form-group col-lg-4">
                <label for="e_mail">E-Mail :</label>
                <input class="form-control" type="text" id="e_mail" name="e_mail" value="p.katerine@wanadoo.fr" />
              </div>
              <div class="form-group col-lg-4">
                <label for="phone">Téléphone</label>
                <input class="form-control" type="text" id="phone" name="phone" value="/" />
              </div>
            </form>
          </div>
        </div>
        <!-- Fin informations -->

        <!-- Description -->
        <div class="row">
          <h3>Description</h3>
          <div class="row">
            <form class="col-lg-12" id="form_description" action="#" method="post">
              <div class="form-group col-lg-12">
                <label for="description">Description</label><br/>
                <textarea class="form-control" name="description" id="description" form="form_description" rows="4" cols="40" placeholder="Description"></textarea>
              </div>
            </form>
          </div>
        </div>
        <!-- Fin description -->

        <!-- Bouton Modifier / Demande d'ami / Accepter demande -->
        <div class="row" id="button">
        </div>

      </div>
      <!-- Fin informations -->

    </div>
    <!-- Fin contenu-->


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
  <script type="text/javascript" src="../scripts/profile.js"></script>
  <script type="text/javascript" src="../scripts/navbar.js"></script>
  <script type="text/javascript" src="../js/bootstrap.js"></script>
  <script type="text/javascript">initNavbar(${user});</script>
  <script type="text/javascript">initProfilePage(${user}, "${param.id}");</script>
</body>
