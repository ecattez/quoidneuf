/**
  * Ajoute les EventListeners aux boutons
  */
function initLoginPage(uid) {
  if(uid != null) {
    $("#login_error").text("Utilisateur déjà authentifié, redirection vers la page de profile...");
    $("#login_error").removeClass('hidden');
    $("#login_error").removeClass('alert-danger');
    $("#login_error").addClass('alert-success');
    window.location.href = 'all/profile.jsp';
  }
  document.getElementById("button_login").addEventListener("click", function() {
    var username = document.getElementsByName('connection_login')[0].value;
    var password = document.getElementsByName('connection_password')[0].value;
    if(!checkLoginParameters(username,password)) {
      $("#login_error").text("Merci de renseigner un login et un mot de passe.");
      $("#login_error").removeClass('hidden');
    }
    else {
      login(username, password);
    }
  }, false);

  document.getElementById("button_inscription").addEventListener("click", function() {
    var username = document.getElementsByName('inscription_login')[0].value;
    var password = document.getElementsByName('inscription_password')[0].value;
    var passwordCheck = document.getElementsByName('inscription_validation_password')[0].value;
    var firstname = document.getElementsByName('inscription_first_name')[0].value;
    var lastname = document.getElementsByName('inscription_last_name')[0].value;
    var birthdate = document.getElementsByName('inscription_birth_date')[0].value;
    var mail = document.getElementsByName('inscription_email')[0].value;
    var phone = document.getElementsByName('inscription_phone')[0].value;
    var description = document.getElementsByName('inscription_description')[0].value;

    if(!checkRegistrationParameters(username, password, passwordCheck, firstname, lastname, birthdate, mail, phone)) {
      $("#inscription_error").removeClass('hidden');
    }
    else {
      // TODO : Une fois l'ajout d'utilisateur fonctionnel.
      $("#inscription_error").addClass('hidden');
    }
  }, false);

  document.getElementById("button_reset_password").addEventListener("click", function() {
    var login = $("#modal_login").val();
    var email = $("#modal_email").val();

    if(!checkResetParameters(login, email)) {
      $("#reset_error").removeClass('hidden');
    }
    else {
      $("#reset_error").text("Vérification de vos données...");
      $("#reset_error").removeClass('hidden');
      $("#reset_error").removeClass('alert-danger');
      $("#reset_error").addClass('alert-success');
      //TODO : Envoyer req reset mdp
    }
  }, false);
}

/**
  * Vérifie la présence de tous les champs de connexion
  *
  * @param {String} Les champs de connexion
  *
  * @return {Boolean} <true> Si tous les champs sont valides, <false> sinon
  */
function checkLoginParameters(username, password) {
  return !((username == '') || (username == undefined) || (password == '') || (password == undefined));
}

/**
* Vérifie la présence de tous les champs obligatoires d'inscription
*
* @param {String} Les champs d'inscription obligatoires
*
* @return {Boolean} <true> Si tous les champs sont valides, <false> sinon
  */
function checkRegistrationParameters(username, password, passwordCheck, firstname, lastname, birthdate, mail, phone) {
  return !(
    (username == '') || (username == undefined)
    || (password == '') || (password == undefined)
    || (passwordCheck == '') || (passwordCheck == undefined)
    || (password != passwordCheck)
    || (firstname == '') || (firstname == undefined)
    || (lastname == '') || (lastname == undefined)
    || (birthdate == '') || (birthdate == undefined)
    || (mail == '') || (mail == undefined)
    || (phone == '') || (phone == undefined));
}

/**
  * Vérifie la présence de tous les champs de réinitialisation de mot de passe
  *
  * @param {String} Les champs de réinitialisation du mot de passe
  *
  * @return {Boolean} <true> Si tous les champs sont valides, <false> sinon
  */
function checkResetParameters(login, email) {
  return !((login == '') || (login == undefined) || (email == '') || (email == undefined));
}

/**
  * Méthode apppelée par la requête Ajax, redirige vers la page de profil si la connexion s'est bien effectuée
  *
  * @param {Object} data - L'objet renvoyé par la requête Ajax contenant des infos sur l'utilisateur ou un code d'erreur et un message
  */
function callBackLogin(jqXHR) {
  if(jqXHR.status == 201) {
    $("#login_error").text("Bonjour " + jqXHR.responseJSON.firstName + " !");
    $("#login_error").removeClass('hidden');
    $("#login_error").removeClass('alert-danger');
    $("#login_error").addClass('alert-success');
    window.location.href = 'all/profile.jsp';
  }
  else {
    $("#login_error").text("Une erreur est survenue, merci de réesayer ultérieurement.");
    // $("#login_error").text(jqXHR.status);
    $("#login_error").removeClass('hidden');
  }
}

// A supprimer
function callBackLogout(jqXHR) {
  console.log(jqXHR.status == 204);
}
