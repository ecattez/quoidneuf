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
  <title>Login - QuoiDNeuf</title>

  <link href="css/style_classic.css" rel="stylesheet">
  <!-- Bootstrap -->
  <link href="css/bootstrap.min.css" rel="stylesheet">

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
            <a class="navbar-brand" href="#">QuoiDNeuf</a>
          </div>

          <!--<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
            <ul class="nav navbar-nav">
              <li><a href="#">Profil</a></li>
              <li><a href="#">Amis</a></li>
              <li><a href="#">Discussions</a></li>
            </ul>
          </div>-->
        </div>
      </nav>
    </header>
    <!-- Fin Header -->

    <!-- Contenu -->

    <div class="row">

      <!-- Connexion -->
      <div class="col-lg-5 col-md-5 contents img-rounded center_div">
        <h2>Déjà inscrit</h2>

        <div class="row">
          <form class="form-connection col-sm-4 col-sm-offset-4 col-md-5 col-md-offset-3 col-lg-5 col-lg-offset-3" action="#" method="post" onSubmit="JavaScript:login()">
            <div class="form-group">
              <label for="connection_login">Login</label><br/>
              <input class="form-control" type="text" name="connection_login" id="connection_login" placeholder="Login" value="ferrot" />
            </div>
            <div class="form-group">
              <label for="connection_password">Mot de passe</label><br>
              <input class="form-control" type="password" name="connection_password" id="connection_password" placeholder="Mot de passe" value="ferrot"/>
            </div>
            <button class="center-block btn btn-default" type="button" name="button_login" id="button_login">Connexion</button>
          </form>
        </div>

        <p class="row"><a data-toggle="modal" data-target="#modal_mdp_oublie">Mot de passe oublié ?</a></p>

        <div id="login_error" class="row alert alert-danger hidden" role="alert"></div>
      </div>
      <!-- Fin connexion -->


      <!-- Fenetre modale "Mot de passe oublié" -->

      <div class="modal fade" tabindex="-1" role="dialog" id="modal_mdp_oublie">
        <div class="modal-dialog">
          <div class="modal-content">
            <div class="modal-header">
              <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
              <h4 class="modal-title">Formulaire réinitialisation de mot de passe</h4>
            </div>
            <div class="modal-body">
              <p><label for="modal_login">Login :</label><br/><input type="text" placeholder="Login" id="modal_login"></p>
              <p><label for="modal_email">E-mail :</label><br/><input type="text" placeholder="E-mail" id="modal_email"></p>
              <div id="reset_error" class="alert alert-danger hidden" role="alert"></div>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-default" data-dismiss="modal">Annuler</button>
              <button type="button" class="btn btn-primary" id="button_reset_password">Envoyer mail de récupération</button>
            </div>
          </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
      </div><!-- /.modal -->

      <!-- Fin fenetre modale "Mot de passe oublié" -->

      <!-- Inscription -->
      <div class="col-lg-6 col-md-6 col-lg-offset-1 col-md-offset-1 contents img-rounded center_div">
        <h2>Inscription</h2>

        <div class="row">
          <form class="form-connection col-sm-4 col-sm-offset-4 col-md-12 col-md-offset-0 col-lg-12 col-lg-offset-0" action="#" method="post" id="form_inscription">
            <div class="row">
              <div class="form-group col-lg-4 col-lg-offset-0 col-md-4 col-md-offset-3">
                <label for="inscription_login">Login*</label><br/>
                <input class="form-control" type="text" name="inscription_login" id="inscription_login" placeholder="Login" value="catteza" />
              </div>
            </div>

            <div class="row">
              <div class="form-group col-lg-4 col-md-6">
                <label for="inscription_password">Mot de passe*</label><br/>
                <input class="form-control" type="password" name="inscription_password" id="inscription_password" placeholder="Mot de passe" value="cattezacatteza" />
              </div>

              <div class="form-group col-lg-4 col-lg-offset-2 col-md-6 col-md-offset-0">
                <label for="inscription_validation_password">Valider mot de passe*</label><br/>
                <input class="form-control" type="password" name="inscription_validation_password" id="inscription_validation_password" placeholder="Valider mot de passe" value="cattezacatteza"/>
              </div>
            </div>

            <div class="row">
              <div class="form-group col-lg-4 col-md-6">
                <label for="inscription_last_name">Nom*</label><br/>
                <input class="form-control" type="text" name="inscription_last_name" id="inscription_last_name" placeholder="Nom" value="Cattez"/>
              </div>

              <div class="form-group col-lg-4 col-lg-offset-2 col-md-6 col-md-offset-0">
                <label for="inscription_first_name">Prénom*</label><br/>
                <input class="form-control" type="text" name="inscription_first_name" id="inscription_first_name" placeholder="Prénom" value="Aurélie" />
              </div>
            </div>

            <div class="row">
              <div class="form-group col-lg-4 col-lg-offset-0 col-md-4 col-md-offset-3">
                <label for="inscription_birth_date">Date de naissance* <br/>(aaaa-mm-jj)</label><br/>
                <input class="form-control" type="text" name="inscription_birth_date" id="inscription_birth_date" placeholder="aaaa-mm-jj" value="1995-05-26"/>
              </div>
            </div>

            <div class="row">
              <div class="form-group col-lg-4 col-md-6">
                <label for="inscription_email">E-Mail*</label><br/>
                <input class="form-control" type="text" name="inscription_email" id="inscription_email" placeholder="E-Mail" value="a.cattez@sfr.fr"/>
              </div>

              <div class="form-group col-lg-4 col-lg-offset-2 col-md-6 col-md-offset-0">
                <label for="inscription_phone">Téléphone</label><br/>
                <input class="form-control" type="text" name="inscription_phone" id="inscription_phone" placeholder="Téléphone" />
              </div>
            </div>

            <div class="row">
              <div class="form-group col-lg-12">
                <label for="inscription_description">Description</label><br/>
                <textarea class="form-control" name="inscription_description" id="inscription_description" form="form_inscription" rows="4" cols="30" placeholder="Description"></textarea>
              </div>
            </div>
            <button class="center-block btn btn-default" type="button" name="button_inscription" id="button_inscription">Inscription</button>
          </form>
        </div>

        <div id="inscription_error" class="row alert hidden" role="alert"></div>
      </div>
      <!-- Fin inscription -->

    </div>

    <!-- Fin contenu-->


    <!-- Footer -->
    <footer class="row panel panel-default">
      <div class="panel-body">
        <div class="pull-left" id="credits">
          Projet Web/BDD - WhatsApp-Like - Cattez Edouard - Ferro Thomas - Licence Pro DA2I - 2015/2016
        </div>

        <a href="https://www-iut.univ-lille1.fr/" class="pull-right"><img src="img/iut.png" alt="Logo IUT" width="80" height="50"/></a>
        <a href="https://github.com/ecattez/quoidneuf" class="pull-right"><img src="img/github.png" alt="Logo GitHub" class="pull-right" width="50" height="50"/></a>
      </div>
    </footer>
    <!-- Footer -->
  </div>

  <script type="text/javascript" src="scripts/jquery.min.js"></script>
  <script type="text/javascript" src="scripts/scripts_ajax.js"></script>
  <script type="text/javascript" src="js/bootstrap.js"></script>
  <script type="text/javascript" src="scripts/login.js"></script>
  <script type="text/javascript">initLoginPage(${user});</script>
</body>
</html>
