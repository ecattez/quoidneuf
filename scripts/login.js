/**
  * Ajoute les EventListeners aux boutons
  */
function initLoginPage(uid) {
  if(uid != null) {
    updateErrorMessage("login_error", true, "Utilisateur déjà authentifié, redirection vers la page de profile...");
    window.location.href = 'all/profile.jsp';
  }
  $("#button_login").on("click", function() {
    var username = $('#connection_login').val();
    var password = $('#connection_password').val();
    if(!checkLoginParameters(username,password)) {
      updateErrorMessage("login_error", false, "Merci de renseigner un login et un mot de passe.");
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
      updateErrorMessage("inscription_error", false, "Merci de renseigner toutes les informations suivies d'une étoile.");
    }
    else {
      if(password != passwordCheck) {
        updateErrorMessage("inscription_error", false, "Mot de passe incorrect");
      }
      else {
        updateErrorMessage("inscription_error", true, "Vérification des données...");
        createUser(username, password, firstname, lastname, birthdate, description, mail, phone);
      }
    }
  });

  $("#button_reset_password").on("click", function() {
    var login = $("#modal_login").val();
    var email = $("#modal_email").val();

    if(!checkResetParameters(login, email)) {
      updateErrorMessage("reset_error", false, "Merci d'indiquer votre login et votre adresse E-Mail");
    }
    else {
      updateErrorMessage("reset_error", true, "Vérification de vos données...");
      passwordLost(login, email);
    }
  });
}

/**
  * Vérifie le message de retour de la requête de création d'user
  */
function callBackCreateUser(data) {
  if(data.code != 201) {
    updateErrorMessage("inscription_error", false, data.responseJSON.message);
  }
  else {
    updateErrorMessage("inscription_error", true, data.message);
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
    || (mail == '') || (mail == undefined) || !(verifierMail(mail)));
}

/**
  * Vérifie la présence de tous les champs de réinitialisation de mot de passe
  *
  * @param {String} Les champs de réinitialisation du mot de passe
  *
  * @return {Boolean} <true> Si tous les champs sont valides, <false> sinon
  */
function checkResetParameters(login, email) {
  return !((login == '') || (login == undefined) || (email == '') || (email == undefined) || !(verifierMail(email)));
}

/**
  * Méthode apppelée par la requête Ajax, redirige vers la page de profil si la connexion s'est bien effectuée
  *
  * @param {Object} data - L'objet renvoyé par la requête Ajax contenant des infos sur l'utilisateur ou un code d'erreur et un message
  */
function callBackLogin(data) {
  console.log(data);
  if(data.code == 201) {
    updateErrorMessage("login_error", true, data.message);
    window.location.href = 'all/profile.jsp';
  }
  else {
    updateErrorMessage("login_error", false, data.responseJSON.message);
  }
}

/**
  * Méthode apppelée par la requête Ajax, vérifie la modification de mot de passe
  *
  * @param {Object} data - L'objet renvoyé par la requête Ajax
  */
function callBackPasswordLost(data) {
  if(data.code == 201) {
    updateErrorMessage("reset_error", true, data.message);
  }
  else {
  console.log(data);
    updateErrorMessage("reset_error", false, data.responseJSON.message);
  }
}

/**
  * Vérifie la validité de l'adresse mail
  *
  * @param {String} mail - L'adresse à vérifier
  *
  * @return {Boolean} <true> si l'adresse est valide, <false> sinon
  */
function verifierMail(mail) {
  return mail.match("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$") != null;
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
