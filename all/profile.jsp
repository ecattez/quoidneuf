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
    <jsp:include page="navbar.jsp" />
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

        <!-- Photo de profil -->
        <div class="row">
          <img src="../img/github.png" alt="Photo de profil" class="photo_profil"/>
        </div>
        <!-- Fin Photo de profil -->

        <!-- Informations -->
        <div class="row">
          <h3>Informations</h3>
          <div class="row">
            <form class="col-lg-12" action="#" method="post">
              <div class="form-group col-lg-4">
                <label for="birth_date">Date de naissance :</label>
                <input class="form-control" type="text" id="birth_date" name="birth_date" value="08/12/1968" readonly/>
              </div>
              <div class="form-group col-lg-4">
                <label for="e_mail">E-Mail :*</label>
                <input class="form-control" type="text" id="e_mail" name="e_mail" value="p.katerine@wanadoo.fr" />
              </div>
              <div class="form-group col-lg-4">
                <label for="phone">Téléphone :</label>
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
        <!-- Fin bouton Modifier / Demande d'ami / Accepter demande -->

        <!-- Message d'erreur -->
        <div id="error_div" class="row alert hidden" role="alert"></div>
        <!-- Fin message d'erreur -->

        <!-- Fenêtre modale modification photo de profil -->
        <div class="row modal fade" id="modale_change_picture" tabindex="-1" role="dialog">
          <div class="modal-dialog">
            <div class="modal-content">
              <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">Modifier photo de profil</h4>
              </div>
              <div class="modal-body">
                <p>TODO</p>
                <input class="row" id="file_loader" type="file" accept="image/*" name="Choix nouvelle photo"/>
                <img class="photo_profil" id="img_preview" src="" alt="Votre image"/>
                <div id="modify_picture_error" class="alert hidden" role="alert"></div>
              </div>
              <div class="modal-footer">
                <button id="button_valid_picture_modification" type="button" class="btn btn-success" name="Valider changement photo" onclick="changePicture()">Valider changement photo</button>
                <button type="button" class="btn btn-danger" data-dismiss="modal">Quitter</button>
              </div>
            </div><!-- /.modal-content -->
          </div><!-- /.modal-dialog -->
        </div><!-- /.modal -->
        <!-- Fin fenêtre modale modification mot de passe -->

        <!-- Fenêtre modale modification mot de passe -->
        <div class="row modal fade" id="modale_change_password" tabindex="-1" role="dialog">
          <div class="modal-dialog">
            <div class="modal-content">
              <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">Modifier mot de passe</h4>
              </div>
              <div class="modal-body">
                <p><label for="modal_password1">Mot de passe :</label><br/><input type="password" placeholder="Mot de passe..." id="modal_password1"></p>
                <p><label for="modal_password2">Vérification :</label><br/><input type="password" placeholder="Vérification mot de passe..." id="modal_password2"></p>
                <div id="modify_password_error" class="alert hidden" role="alert"></div>
              </div>
              <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Annuler</button>
                <button type="button" class="btn btn-primary" id="button_modify_password">Valider changement</button>
              </div>
            </div><!-- /.modal-content -->
          </div><!-- /.modal-dialog -->
        </div><!-- /.modal -->
        <!-- Fin fenêtre modale modification mot de passe -->

      </div>
      <!-- Fin informations -->

    </div>
    <!-- Fin contenu-->


    <!-- Footer -->
    <jsp:include page="footer.jsp" />
    <!-- Footer -->
  </div>

  <script type="text/javascript" src="../scripts/jquery.min.js"></script>
  <script type="text/javascript" src="../scripts/scripts_ajax.js"></script>
  <script type="text/javascript" src="../scripts/profile.js"></script>
  <script type="text/javascript" src="../scripts/navbar.js"></script>
  <script type="text/javascript" src="../js/bootstrap.js"></script>
  <script type="text/javascript" src="../js/jquery.dynatable.js"></script>
  <script type="text/javascript">initNavbar('${user}', 'profile');</script>
  <script type="text/javascript">initProfilePage('${user}', "${param.id}");</script>
</body>
