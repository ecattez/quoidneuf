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

  <!-- <link href="../css/jquery.dynatable.css" rel="stylesheet"> -->

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
        <div class="row col-lg-12 col-md-12 col-sm-12 col-xs-12 commentArea" id="messages">
          <!-- Label ne passe pas le validator -->
          <!-- <label for="messages" hidden="true">Messages</label> -->
        </div>
        <!-- Fin liste des messages-->

        <!-- Message à envoyer -->
        <div class="row">
          <div class="col-lg-10 col-md-10 col-sm-10 col-xs-10"><input type="text" class="form-control" placeholder="Message..." id="message_input"></div>
          <button class="btn btn-default col-lg-1 col-md-1 col-sm-1 col-xs-1" type="button" data-toggle="modal" data-target="#modale_upload_file" id="send_file"><span class="glyphicon glyphicon-file" aria-hidden="true"></span></button>
          <button class="btn btn-default col-lg-1 col-md-1 col-sm-1 col-xs-1" type="button" id="send_message"><span class="glyphicon glyphicon-send" aria-hidden="true"></span></button>
        </div>
        <!-- Fin message à envoyer -->

        <!-- Error div-->
        <div class="row alert hidden" role="alert" id="error_div"></div>
        <!-- Fin Error Div-->
      </div>
      <!-- Fin Discussion -->

      <!-- Fenêtre modale upload fichier -->
      <div class="row modal fade" id="modale_upload_file" tabindex="-1" role="dialog">
        <div class="modal-dialog">
          <div class="modal-content">
            <div class="modal-header">
              <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
              <h4 class="modal-title">Envoyer un document</h4>
            </div>
            <div class="modal-body">
              <p>TODO</p>
              <form class="" action="../api/files" method="post" id="form_change_picture" enctype="multipart/form-data">
                <input class="row" id="file_loader" type="file" accept="image/*" name="file"/>
                <input class="row" type="hidden" name="dest" value="discussions"/>
                <input class="row" id="folder" type="hidden" name="folder" value="${param.id}"/>
                <input type="submit" class="btn btn-success">
              </form>
              <div id="send_file_error" class="alert hidden" role="alert"></div>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-danger" data-dismiss="modal">Quitter</button>
            </div>
          </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
      </div><!-- /.modal -->
      <!-- Fin fenêtre modale upload fichier -->

      <!-- Membres -->
      <div class="col-lg-2 col-md-2 contents img-rounded collapse col-md-offset-1 col-lg-offset-1" id="membres">
        <h2>Membres</h2>
        <ul id="discussion_membres"></ul>

        <p><button type="button" name="Ajout membre" class="btn btn-success" data-toggle="modal" data-target="#modal_add_member">Ajout membre</button></p>

        <p><button type="button" name="Quitter groupe" class="btn btn-danger" data-toggle="modal" data-target="#modal_leave_discussion">Quitter discussion</button></p>
      </div>
      <!-- Fin Membres -->
    </div>
    <!-- Fin contenu -->

    <!-- Fenetre modale "Ajout de membre" -->

    <div class="modal fade" tabindex="-1" role="dialog" id="modal_add_member">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
            <h4 class="modal-title">Ajout de membre</h4>
          </div>
          <div class="modal-body">
            <div class="input-group">
              <span class="input-group-btn">
                <button class="btn btn-default" type="button" id="search_member"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></button>
              </span>
              <input type="text" class="form-control" placeholder="Chercher utilisateur..." id="member_search_input">
            </div><!-- /input-group -->

            <!-- Resultat recherche div-->
            <div class="row" id="search_result">
              <table class="dynatable table table-bordered" id="search_result_table">
                <thead>
                  <tr>
                    <th data-dynatable-column="firstName" class="dynatable-head">Prénom</th>
                    <th data-dynatable-column="lastName" class="dynatable-head">Nom</th>
                    <th data-dynatable-column="birthday" class="dynatable-head">Date de naissance</th>
                    <th data-dynatable-column="id" class="dynatable-head">Ajouter</th>
                  </tr>
                </thead>
                <tbody>
                </tbody>
              </table>
            </div>
            <!-- Fin Resultat Recherche Div-->

            <!-- Error div-->
            <div class="row alert hidden" role="alert" id="error_search_div"></div>
            <!-- Fin Error Div-->
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-default" data-dismiss="modal">Fermer fenêtre</button>
          </div>
        </div><!-- /.modal-content -->
      </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->

    <!-- Fin fenetre modale "Ajout de membre" -->

    <!-- Fenetre modale Confirmation quiter discussion -->

    <div class="modal fade" tabindex="-1" role="dialog" id="modal_leave_discussion">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
            <h4 class="modal-title">Quitter la discussion ?</h4>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-default" data-dismiss="modal">Annuler</button>
            <button type="button" class="btn btn-danger" id="leave_discussion_button">Quitter la discussion</button>
          </div>
        </div><!-- /.modal-content -->
      </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->

    <!-- Fin fenetre modale Confirmation quiter discussion -->

    <!-- Footer -->
    <jsp:include page="footer.jsp" />
    <!-- Footer -->
  </div>

  <script type="text/javascript" src="../scripts/jquery.min.js"></script>
  <script type="text/javascript" src="../scripts/scripts_ajax.js"></script>
  <script type="text/javascript" src="../scripts/navbar.js"></script>
  <script type="text/javascript" src="../scripts/discussion.js"></script>
  <script type="text/javascript" src="../js/bootstrap.js"></script>
  <script type="text/javascript" src="../js/jquery.dynatable.js"></script>
  <script type="text/javascript">initNavbar('${user}', 'discussion');</script>
  <script type="text/javascript">initDiscussionPage('${user}', "${param.id}", "${pageContext.servletContext.contextPath}");</script>
</body>
</html>
