/**
  * Charge les élements de la page
  *
  * @param {Number} uid - Id de l'utilisateur connecté.
  * @param {Number} user - Id de l'utilisateur de la page.
  */
function initProfilePage(uid, user) {
  if(user != '' && user != undefined) {
    // Si on ne précise pas d'utilisateur, on charge la page de l'utilisateur courant :
    uid = user;
    checkFriend(user);
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
function checkFriend(user) {
  console.log("A implementer");
  //TODO : Vérifier si ami / en demande ou pas ami du tout pour charger les bons boutons
  loadButtonAdd(user);
}

/**
  * Charge le bouton de modification
  */
function loadButtonModify() {
  $("#button").empty();
  $("#button").append("<p><button type=\"button\" class=\"btn btn-primary\" name=\"Bouton modifier\" id=\"button_modify\">Modifier</button></p>");
  $("#button").append("<p><button type=\"button\" class=\"btn btn-danger\" name=\"Bouton changer mot de passe\" id=\"button_modify_password\">Changer de mot de passe</button></p>");
  $("#button_modify").on("click", modifyProfile);
  $("#button_modify_password").on("click", modifyPassword);
}

/**
  * Envoyer la requête de modification du profil
  */
function modifyProfile() {
  modifySubscribersProfile('', $("#description").val(), $("#e_mail").val(), $("#phone").val());
}

/**
  * Ouvre la fenetre modale de modification de mot de passe
  */
function modifyPassword() {
  console.log("A implementer");
}

/**
  * Charge le bouton de demande d'ami
  *
  * @param {Number} user - L'id de l'utilisateur à ajouter
  */
function loadButtonAdd(user) {
  console.log("A implementer");

  $("#button").empty();
  $("#button").append("<p><button type=\"button\" class=\"btn btn-success\" name=\"Bouton ajouter\" id=\"add_friend_"+user+"\">Demande d'ami</button></p>");
  $("#add_friend_"+user).on("click", function() { addFriend(user) });
}

/**
  *
  */
function addFriend(user) {
  console.log("A implementer");
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
  console.log(data);
  $("#nom_utilisateur").text(data.firstName + " " + data.lastName);
  $("#birth_date").val("IL MANQUE LA DATE DE NAISSANCE EDOUARD TU FOUS QUOI !");
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
    line += "<div class=\"col-lg-8 col-md-8 col-sm-8 col-xs-8\">";
    line += data[ami].firstName + " " + data[ami].lastName;
    line += "</div>";
    line += "<div class=\"col-lg-1 col-lg-offset-1 col-md-1 col-md-offset-1 col-sm-1 col-sm-offset-1 col-xs-1 col-xs-offset-1\">";
    line += "<button type=\"button\" name=\"button_add_friend\" id=add_friend_"+data[ami].id+">+</button></div>";
    line += "</div>";
    $("#amis").append(line);
    // TODO : Ajouter le listener
  }
}

/**
  * Méthode de retour de la mise à jour du profil
  *
  * @param {Object} data - Message de retour du serveur
  */
function callBackModifySubscribersProfile(data) {
  console.log(data);
}
