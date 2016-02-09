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
          <li class="hidden" id="li_membres"><a href="#" data-toggle="collapse" data-target="#membres">Membres</a></li>
          <li><a href="#" data-toggle="modal" data-target="#modal_add_friend">Recherche profil</a></li>
        </ul>
        <ul class="nav navbar-nav navbar-right">
          <li><a href="#" class="btn" id="navbar_deconnexion">Déconnexion</a></li>
        </ul>
      </div>
    </div>
  </nav>
</header>

<!-- Fenêtre modale ajout ami -->

<div class="modal fade" tabindex="-1" role="dialog" id="modal_add_friend">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title">Ajout d'ami</h4>
      </div>
      <div class="modal-body">
        <div class="input-group">
          <span class="input-group-btn">
            <button class="btn btn-default" type="button" id="search_new_friend"><span class="glyphicon glyphicon-search" aria-hidden="true"></span></button>
          </span>
          <input type="text" class="form-control" placeholder="Chercher utilisateur..." id="member_search_friend_input">
        </div><!-- /input-group -->

        <!-- Resultat recherche div-->
        <div class="row" id="search_friend_result">
          <table class="dynatable table table-bordered" id="search_result_friend_table">
            <thead>
              <tr>
                <th data-dynatable-column="firstName" class="dynatable-head">Prénom</th>
                <th data-dynatable-column="lastName" class="dynatable-head">Nom</th>
                <th data-dynatable-column="birthday" class="dynatable-head">Date de naissance</th>
                <th data-dynatable-column="id" class="dynatable-head">Profil</th>
              </tr>
            </thead>
            <tbody>
            </tbody>
          </table>
        </div>
        <!-- Fin Resultat Recherche Div-->

        <!-- Error div-->
        <div class="row alert hidden" role="alert" id="error_new_friend_div"></div>
        <!-- Fin Error Div-->
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">Fermer fenêtre</button>
      </div>
    </div><!-- /.modal-content -->
  </div><!-- /.modal-dialog -->
</div><!-- /.modal -->

<!-- Fenêtre modale ajout ami -->
