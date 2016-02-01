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
    <jsp:include page="navbar.jsp" />
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
          <div class="col-lg-11 col-md-11 col-sm-11 col-xs-11"><input type="text" class="form-control" placeholder="Message..." id="message_input"></div>
          <button class="btn btn-default col-lg-1 col-md-1 col-sm-1 col-xs-1" type="button" id="send_message"><span class="glyphicon glyphicon-send" aria-hidden="true"></span></button>
        </div>
        <!-- Fin message à envoyer -->

        <!-- Error div-->
        <div class="row alert hidden" role="alert" id="error_div"></div>
        <!-- Fin Error Div-->
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
              <button class="btn btn-default" type="button" id="add_member"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></button>
            </span>
            <input type="text" class="form-control" placeholder="Ajouter utilisateur..." id="member_input">
          </div><!-- /input-group -->
        </div>
      </div>
      <!-- Fin Membres -->
    </div>
    <!-- Fin contenu -->

    <!-- Footer -->
    <jsp:include page="footer.jsp" />
    <!-- Footer -->
  </div>

  <script type="text/javascript" src="../scripts/jquery.min.js"></script>
  <script type="text/javascript" src="../scripts/scripts_ajax.js"></script>
  <script type="text/javascript" src="../scripts/navbar.js"></script>
  <script type="text/javascript" src="../scripts/discussion.js"></script>
  <script type="text/javascript" src="../js/bootstrap.js"></script>
  <script type="text/javascript">initNavbar(${user}, 'discussion');</script>
  <script type="text/javascript">initDiscussionPage("${param.id}");</script>
</body>
</html>
