var id;
var profile;
var context;

$(document).ready(function() {
	$("#form_change_picture").submit(function() {
    $(this).ajaxSubmit();
    location.reload();
    return false;
  });
});

/**
  * Change la photo de profil de l'utilisateur courant
  */
function submitPicture() {
	$("#form_change_picture").submit();
}

/**
  * Charge les élements de la page
  *
  * @param {Number} uid - Id de l'utilisateur connecté.
  * @param {Number} user - Id de l'utilisateur de la page.
  * @param {String} cont - Le nom du context courant
  */
function initProfilePage(uid, user, cont) {
  id = uid;
  context = cont;
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
    if(data.status != profile) {
      $("#button").append("<p>Demande d'amitié en attente...</p>");
    }
    else {
      loadButtonAcceptFriend(data.status);
    }
  }
  else if (data.status == 0) {
    loadButtonRemove(profile);
  }
  else if (data.status == -1) {
    // Pas amis et pas de demande en cours
    loadButtonAdd(profile);
  }
}

/**
  * Charge le bouton d'acceptation de la demande d'ami
  *
  */
function loadButtonAcceptFriend(user) {
  $("#button").append("<p><button type=\"button\" class=\"btn btn-success\" name=\"Bouton accepter\" onclick=\"acceptFriendRequest()\" id=\"button_accept\">Accepter demande</button></p>");
}

/**
  * Charge le bouton de demande d'ami
  *
  * @param {Number} user - L'id de l'utilisateur à ajouter
  */
function loadButtonAdd(user) {
  $("#button").append("<p><button type=\"button\" class=\"btn btn-success\" name=\"Bouton ajouter\" onclick=\"addFriend("+user+")\" id=\"add_friend_"+user+"\">Demande d'ami</button></p>");
}

/**
  * Charge le bouton de suppression d'ami
  *
  * @param {Number} user - L'id de l'utilisateur à supprimer
  */
function loadButtonRemove(user) {
  $("#button").append("<p><button type=\"button\" class=\"btn btn-danger\" name=\"Bouton retirer\" onclick=\"removeFriendRequest("+user+")\">Retirer l'ami de votre liste</button></p>");
}

/**
  * Envoie une requête de suppression de l'ami
  */
function removeFriendRequest(user) {
  if(user != undefined) {
    removeFriend(user);
  }
  else {
    updateErrorMessage('error_div', false, "Erreur à la suppression de l'ami.");
  }
}

function callBackRemoveFriend(data) {
  if(data) {
    updateErrorMessage('error_div', false, data.statusText);
  }
  else {
    updateErrorMessage('error_div', true, "Suppression effectuée");
  }
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
  $("#button").append("<p><button type=\"button\" class=\"btn btn-primary\" name=\"Bouton modifier profil\" onclick=\"modifyProfile()\" id=\"button_modify\">Modifier</button></p>");
  $("#button").append("<p><button type=\"button\" class=\"btn btn-primary\" name=\"Bouton changer photo de profil\" id=\"button_open_modal_picture\" data-toggle=\"modal\" data-target=\"#modale_change_picture\">Changer de photo de profil</button></p>");
  $("#button").append("<p><button type=\"button\" class=\"btn btn-danger\" name=\"Bouton changer mot de passe\" id=\"button_open_modal_password\" data-toggle=\"modal\" data-target=\"#modale_change_password\">Changer de mot de passe</button></p>");
  $("#button_modify_password").on("click", sendModifyPassword);

  $("#file_loader").change(function() { readURL(this); });
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
  $("#picture")[0].src = context + "/" + data.meta.pictureUri;
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
    line += "<a href='#' onclick=\"initProfilePage("+id+", "+data[ami].id+", '"+context+"')\" id='profile_"+data[ami].id+"'>";
    line += data[ami].firstName + " " + data[ami].lastName;
    line += "</a>";
    line += "</div>";
    $("#amis").append(line);
  }
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
  * Vérifie l'element et affiche l'image.
  */
function readURL(input) {
  if(input.files && input.files[0]) {
    var reader = new FileReader();
    reader.onload = function(e) {
      $("#img_preview").attr('src', e.target.result);
    }
    reader.readAsDataURL(input.files[0]);
  }
}

/**
 *
 */
function changePicture() {
  var file = $("#file_loader").prop('files')[0];
  var data = new FormData($('#form_change_picture'));
  data.append('dest', 'subscribers');
  data.append('folder', $('#folder')[0].value);
  data.append('file', file);
  console.log(data);
  uploadFileToProfile(data);
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
