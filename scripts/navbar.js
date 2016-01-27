/**
  * Ajoute les EventListeners aux elements de la navbar
  */
function initNavbar(user) {
  if(user == undefined) {
    window.location.href = '../';
  }
  $("#navbar_deconnexion").on("click", function() {
    logout();
  });

  $("#navbar_amis").on("click", function() {
    getFriends(user);
  });

  $("#navbar_discussions").on("click", function() {
    getDiscussions();
  });
}

/**
  * Vérifie le code de retour de la déconnexion, méthode appelée par la requête Ajax
  *
  * @param {Object} data - L'objet renvoyé par le serveur, vide si réussi
  */
function callBackLogout(data) {
  if(data == undefined) {
    window.location.href = '../';
  }
}

/**
  * Mise à jour de l'affichage avec les amis de l'utilisateur, méthode appelée par la requête Ajax
  *
  * @param {Object} data - L'objet renvoyée par la requête Ajax avec le tableau d'amis.
  */
function callBackGetFriends(data) {
  $("#dropdown_navbar_amis").empty();
  var line = '';
  for(var ami in data) {
    line = "<li><a href=\"profile.jsp?id=" + data[ami].id + "\">" + data[ami].firstName + " " + data[ami].lastName + "</a></li>";
    $("#dropdown_navbar_amis").append(line);
  }
}

/**
  * Mise à jour de l'affichage avec les discussions de l'utilisateur, méthode appelée par la requête Ajax
  *
  * @param {Object} data - L'objet renvoyée par la requête Ajax avec entre autres un tableau de discussions.
  */
function callBackGetDiscussions(data) {
  $("#dropdown_navbar_discussions").empty();
  var line = '';
  for(var discussion in data) {
    line = "<li><a href=\"discussion.jsp?id=" + data[discussion].id + "\">" + data[discussion].name + "</a></li>";
    $("#dropdown_navbar_discussions").append(line);
  }
}
