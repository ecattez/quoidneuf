/**
  * Ajoute les EventListeners aux boutons
  */
function initLoginPage() {
  document.getElementById("button_login").addEventListener("click", function() {
    var username = document.getElementsByName('connection_login')[0].value;
    var password = document.getElementsByName('connection_password')[0].value;
    if(!checkLoginParameters(username,password)) {
      $("#login_error").text("Merci de renseigner un login et un mot de passe.");
      $("#login_error").removeClass('hidden');
    }
    else {
      var user = login(username, password);
      // TODO : Trouver un moyen de le faire fonctionner
      $("#login_error").addClass('hidden');
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
      $("#inscription_error").addClass('hidden');
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


initLoginPage();
