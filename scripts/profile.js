var id;
var profile;

/**
  * Charge les élements de la page
  *
  * @param {Number} uid - Id de l'utilisateur connecté.
  * @param {Number} user - Id de l'utilisateur de la page.
  */
function initProfilePage(uid, user) {
  id = uid;
  if(uid == undefined || uid == '') {
    window.location.href = '../';
  }
  if(user != '' && user != undefined && user != uid) {
    // Si on ne précise pas d'utilisateur, on charge la page de l'utilisateur courant :
    uid = user;
    checkFriendStatus(user);
  }
  else {
    loadButtonModify();
  }
  profile = user;
  getSubscribersProfile(uid);
  getFriendsProfile(uid);
}

/**
  * Vérifie si les utilisateurs sont amis
  *
  * @param {Number} user - Id de l'utilisateur à vérifier
  */
function checkFriendStatus(user) {
  getFriendStatus(user);
}

/**
  * Charge le bouton d'ajout d'ami
  */
function callBackCheckFriendStatus(data) {
  $("#button").empty();
  if(data.status > 0) {
    //Si pas amis :
    if(data.status != id) {
      $("#button").append("<p><button type=\"button\" class=\"btn btn-success\" name=\"Bouton accepter\" id=\"button_accept\">Accepter demande</button></p>");
      $("#button_accept").on("click", acceptFriendRequest);
    }
    else {
      $("#button").append("<p>Demande d'amitié en attente...</p>");
    }
  }
  else if (data.status == -1) {
    // Pas amis et pas de demande en cours
    loadButtonAdd(id);
  }
}

/**
  * Charge le bouton de demande d'ami
  *
  * @param {Number} user - L'id de l'utilisateur à ajouter
  */
function loadButtonAdd(user) {
  $("#button").append("<p><button type=\"button\" class=\"btn btn-success\" name=\"Bouton ajouter\" id=\"add_friend_"+user+"\">Demande d'ami</button></p>");
  $("#add_friend_"+user).on("click", function() { addFriend(user) });
}

/**
  * Au clique sur le bouton "Accepter demande", on envoie la requête
  */
function acceptFriendRequest() {
  valideFriendRequest(profile);
}

/**
  * Vérification du message de retour de la valdiation de requête d'ami.
  */
function callBackValideFriendRequest(data) {
  if(data) {
    updateErrorMessage("error_div", false, data.message);
  }
  else {
    updateErrorMessage("error_div", true, "Validation de la demande d'ami.");
  }
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
  if(($("#modal_password1").val() != '') && ($("#modal_password1").val() == $("#modal_password2").val())) {
    updateErrorMessage("modify_password_error", true, "Modification en cours...");
    modifyPassword($("#modal_password1").val());
  }
  else {
    updateErrorMessage("modify_password_error", false, "Merci d'entrer deux fois votre mot de passe");
  }
}

/**
  * Met à jour l'affichae en fonction du résultat de la modification de mot de passe
  */
function callBackmodifyPassword(data) {
  if(data == undefined) {
    updateErrorMessage("modify_password_error", true, "Mot de passe modifié.");
  }
  else {
    updateErrorMessage("modify_password_error", false, "Erreur : " + data.code + " : " + data.message);
  }
}

/**
  * Envoie une requête de demande d'ami
  */
function addFriend() {
  addFriendRequest(profile);
}

/**
  * Vérifie le retour de la requête
  */
function callBackAddFriendRequest(data) {
  if(data.code == 201) {
    updateErrorMessage("error_div", true, data.message);
  }
  else {
    updateErrorMessage("error_div", false, data.message);
  }
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
  // console.log(userId);
  $("#profile_"+userId).on("click", function() { initProfilePage(id, userId) });
}


/**
  * Envoyer la requête de modification du profil
  */
function modifyProfile() {
  if(($("#e_mail").val() == undefined) || ($("#e_mail").val() == '')) {
    updateErrorMessage("error_div", false, "Merci de renseigner les champs précédés d'une étoile");
  }
  else {
    updateErrorMessage("error_div", true, "Modification en cours...");
    modifySubscribersProfile($("#description").val(), $("#e_mail").val(), $("#phone").val());
  }
}

/**
  * Méthode de retour de la mise à jour du profil
  *
  * @param {Object} data - Message de retour du serveur
  */
function callBackModifySubscribersProfile(data) {
  if(data == undefined) {
    updateErrorMessage("error_div", true, "Profil modifié.");
  }
  else {
    updateErrorMessage("error_div", false, "Erreur : " + data.responseJSON.code + " : " + data.responseJSON.message);
  }
}

/**
  * Met à jour une division d'erreur
  *
  * @param {String} div - La division à mettre à jour
  * @param {Boolean} bool - <true> si message d'erreur, <false> si message de réussite
  * @param {String} mess - Le message de la division
  */
function updateErrorMessage(div, bool, mess) {
  $("#"+div).empty();
  $("#"+div).removeClass("hidden");
  $("#"+div).removeClass("alert-success");
  $("#"+div).removeClass("alert-danger");

  if(bool == true) {
    $("#"+div).addClass("alert-success");
  }
  else {
    $("#"+div).addClass("alert-danger");
  }
  $("#"+div).append("<p>"+mess+"</p>");
}
