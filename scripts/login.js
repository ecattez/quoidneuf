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
  $("#button_login").on("click", function() {
    var username = $('#connection_login').val();
    var password = $('#connection_password').val();
    if(!checkLoginParameters(username,password)) {
      $("#login_error").text("Merci de renseigner un login et un mot de passe.");
      $("#login_error").removeClass('hidden');
    }
    else {
      login(username, password);
    }
  });

  $("#button_inscription").on("click", function() {
    var username = $('#inscription_login').val();
    var password = $('#inscription_password').val();
    var passwordCheck = $('#inscription_validation_password').val();
    var firstname = $('#inscription_first_name').val();
    var lastname = $('#inscription_last_name').val();
    var birthdate = $('#inscription_birth_date').val();
    var picture = 'picturePath';
    var mail = $('#inscription_email').val();
    var phone = $('#inscription_phone').val();
    var description = $('#inscription_description').val();

    if(!checkRegistrationParameters(username, password, passwordCheck, firstname, lastname, birthdate, mail)) {
      $("#inscription_error").text("Merci de renseigner toutes les informations suivies d'une étoile.");
      $("#inscription_error").removeClass('hidden');
      $("#inscription_error").removeClass('alert-success');
      $("#inscription_error").addClass('alert-danger');
    }
    else {
      if(password != passwordCheck) {
        $("#inscription_error").text("Mot de passe incorrect");
        $("#inscription_error").removeClass('hidden');
        $("#inscription_error").removeClass('alert-success');
        $("#inscription_error").addClass('alert-danger');
      }
      else {
        $("#inscription_error").text("Vérification des données...");
        $("#inscription_error").removeClass('hidden');
        $("#inscription_error").removeClass('alert-danger');
        $("#inscription_error").addClass('alert-success');
        createUser(username, password, firstname, lastname, birthdate, picture, description, mail, phone);
      }
    }
  });

  $("#button_reset_password").on("click", function() {
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
  });
}

/**
  * Vérifie le message de retour de la requête de création d'user
  */
function callBackCreateUser(data) {
  if(data.code != 201) {
    $("#inscription_error").text(data.responseJSON.message);
    $("#inscription_error").removeClass('hidden');
    $("#inscription_error").removeClass('alert-success');
    $("#inscription_error").addClass('alert-danger');
  }
  else {
    $("#inscription_error").text(data.message);
    $("#inscription_error").removeClass('hidden');
    $("#inscription_error").removeClass('alert-danger');
    $("#inscription_error").addClass('alert-success');
  }
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
function checkRegistrationParameters(username, password, passwordCheck, firstname, lastname, birthdate, mail) {
  return !(
    (username == '') || (username == undefined)
    || (password == '') || (password == undefined)
    || (passwordCheck == '') || (passwordCheck == undefined)
    || (firstname == '') || (firstname == undefined)
    || (lastname == '') || (lastname == undefined)
    || (birthdate == '') || (birthdate == undefined)
    || (mail == '') || (mail == undefined));
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
function callBackLogin(data) {
  $("#login_error").removeClass('hidden');
  if(data.code == 201) {
    $("#login_error").text(data.message);
    $("#login_error").removeClass('alert-danger');
    $("#login_error").addClass('alert-success');
    window.location.href = 'all/profile.jsp';
  }
  else {
    $("#login_error").text(data.responseJSON.message);
  }
}
