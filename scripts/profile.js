var id;

/**
  * Charge les élements de la page
  *
  * @param {Number} uid - Id de l'utilisateur connecté.
  * @param {Number} user - Id de l'utilisateur de la page.
  */
function initProfilePage(uid, user) {
  id = uid;
  if(user != '' && user != undefined && user != uid) {
    // Si on ne précise pas d'utilisateur, on charge la page de l'utilisateur courant :
    uid = user;
    checkFriendStatus(user);
  }
  else {
    loadButtonModify();
  }
  getSubscribersProfile(uid);
  getFriendsProfile(uid);
}

/**
  * Vérifie si les utilisateurs sont amis
  *
  * @param {Number} user - Id de l'utilisateur à vérifier
  */
function checkFriendStatus(user) {
  // console.log("A implementer");
  //TODO : Vérifier si ami / en demande ou pas ami du tout pour charger les bons boutons
  loadButtonAdd(user);
}

/**
  * Charge le bouton d'ajout d'ami
  */
function callBackCheckFriendStatus(data) {
  if((data.status == 1) ||(data.status == 2)) {
    // Demande en cours
    //TODO : vérifie quel utilisateur à envoyé la demande, si c'est l'utilisateur courant on bloque le bouton avec "demande en attente...", sinon :
    $("#button").empty();
    $("#button").append("<p><button type=\"button\" class=\"btn btn-success\" name=\"Bouton accepter\" id=\"button_accept\">Accepter demande</button></p>");
    $("#button_accept").on("click", acceptFriendRequest);
  }
  else {
    // Pas amis et pas de demande en cours
    $("#button").empty();
    $("#button").append("<p><button type=\"button\" class=\"btn btn-success\" name=\"Bouton ajouter aux amis\" id=\"button_send_friend_request\">Envoyer demande d'ami</button></p>");
    $("#button_send_friend_request").on("click", acceptFriendRequest);
  }
}

/**
  *
  */
function acceptFriendRequest() {
  //TODO : Envoyer requete validation demande d'ami
  console.log("validation demande d'ami à implementer");
}

/**
  * Charge le bouton de modification
  */
function loadButtonModify() {
  $("#button").empty();
  $("#button").append("<p><button type=\"button\" class=\"btn btn-primary\" name=\"Bouton modifier\" id=\"button_modify\">Modifier</button></p>");
  $("#button").append("<p><button type=\"button\" class=\"btn btn-danger\" name=\"Bouton changer mot de passe\" id=\"button_open_modal\" data-toggle=\"modal\" data-target=\"#modale_change_password\">Changer de mot de passe</button></p>");
  $("#button_modify").on("click", modifyProfile);
  $("#button_modify_password").on("click", sendModifyPassword);
}

/**
  * Envoyer la requête de modification de mot de passe
  */
function sendModifyPassword() {
  $("#modify_password_error").removeClass('hidden');
  $("#modify_password_error").removeClass('alert-success');
  $("#modify_password_error").removeClass('alert-danger');
  if(($("#modal_password1").val() != '') && ($("#modal_password1").val() == $("#modal_password2").val())) {
    $("#modify_password_error").text("Modification en cours...");
    $("#modify_password_error").addClass('alert-success');
    modifyPassword($("#modal_password1").val());
  }
  else {
    $("#modify_password_error").text("Merci d'entrer deux fois votre mot de passe");
    $("#modify_password_error").addClass('alert-danger');
  }
}

/**
  * Met à jour l'affichae en fonction du résultat de la modification de mot de passe
  */
function callBackmodifyPassword(data) {
  if(data == undefined) {
    $("#modify_password_error").text("Mot de passe modifié.");
    $("#modify_password_error").addClass('alert-success');
  }
  else {
    $("#modify_password_error").text("Erreur : " + data.code + " : " + data.message);
    $("#modify_password_error").addClass('alert-danger');
  }
}

/**
  * Charge le bouton de demande d'ami
  *
  * @param {Number} user - L'id de l'utilisateur à ajouter
  */
function loadButtonAdd(user) {

  $("#button").empty();
  $("#button").append("<p><button type=\"button\" class=\"btn btn-success\" name=\"Bouton ajouter\" id=\"add_friend_"+user+"\">Demande d'ami</button></p>");
  $("#add_friend_"+user).on("click", function() { addFriend(user) });
}

/**
  *
  */
function addFriend(user) {
  console.log("addFriend à implementer");
}

/**
  * Charge le bouton d'acceptation de demande d'ami
  *
  * @param {Number} user - L'id de l'utilisateur à accepter
  */
function loadButtonAccept(user) {
  console.log("A implementer");
}

/**
  * Met à jour l'affichage avec les données envoyées par le serveur
  *
  * @param {Object} data - Le profil de l'utilisateur
  */
function callBackGetSubscribersProfile(data) {
  // console.log(data);
  $("#nom_utilisateur").text(data.firstName + " " + data.lastName);
  $("#birth_date").val(data.birthday);
  $("#e_mail").val(data.meta.email);
  $("#phone").val(data.meta.phone);
  $("#description").val(data.meta.description);
}

/**
  * Met à jour l'affichage des amis de l'utilisateur.
  *
  * @param {Array} data - Tableau d'amis
  */
function callBackGetFriendsProfile(data) {
  $("#amis").empty();
  var line = '';
  for(var ami in data) {
    line = "<div class=\"row\">";
    line += "<div class=\"col-lg-12 col-md-12 col-sm-12 col-xs-12\">";
    line += "<a id='profile_"+data[ami].id+"'>";
    line += data[ami].firstName + " " + data[ami].lastName;
    line += "</a>";
    line += "</div>";
    $("#amis").append(line);
    ajoutEventListener(data[ami].id);
  }
}

/**
  * Ajoute le listener au lien
  */
function ajoutEventListener(userId) {
  $("#profile_"+userId).on("click", function() { initProfilePage(id, userId) });
}


/**
  * Envoyer la requête de modification du profil
  */
function modifyProfile() {
  $("#error_div").removeClass("hidden");
  $("#error_div").removeClass('alert-success');
  $("#error_div").removeClass('alert-danger');
  if(($("#e_mail").val() == undefined) || ($("#e_mail").val() == '')) {
    $("#error_div").addClass("alert-danger");
    $("#error_div").text("Merci de renseigner les champs précédés d'une étoile");
  }
  else {
    $("#error_div").addClass("alert-success");
    $("#error_div").text("Modification en cours...");
    modifySubscribersProfile('picturepath', $("#description").val(), $("#e_mail").val(), $("#phone").val());
  }
}

/**
  * Méthode de retour de la mise à jour du profil
  *
  * @param {Object} data - Message de retour du serveur
  */
function callBackModifySubscribersProfile(data) {
  if(data == undefined) {
    $("#error_div").text("Profil modifié.");
    $("#error_div").addClass('alert-success');
  }
  else {
    $("#error_div").text("Erreur : " + data.responseJSON.code + " : " + data.responseJSON.message);
    $("#error_div").addClass('alert-danger');
  }
}
