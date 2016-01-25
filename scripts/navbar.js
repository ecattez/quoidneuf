/**
  * Ajoute les EventListeners aux elements de la navbar
  */
function initNavbar(user) {
  document.getElementById("navbar_deconnexion").addEventListener("click", function() {
    logout();
  }, false);

  document.getElementById("navbar_amis").addEventListener("click", function() {
    getFriends(user);
  }, false);

  document.getElementById("navbar_discussions").addEventListener("click", function() {
    getDiscussions();
  }, false);
}

/**
  * Vérifie le code de retour de la déconnexion, méthode appelée par la requête Ajax
  *
  * @param {Object} jqXHR - L'objet renvoyée par la requête Ajax avec entre autres le code de retour de cette dernière.
  */
function callBackLogout(jqXHR) {
  if(jqXHR.status == 204) {
    window.location.href = '../';
  }
}

/**
  * Mise à jour de l'affichage avec les amis de l'utilisateur, méthode appelée par la requête Ajax
  *
  * @param {Object} jqXHR - L'objet renvoyée par la requête Ajax avec entre autres un tableau d'amis.
  */
function callBackGetFriends(jqXHR) {
  console.log(jqXHR);
}

/**
  * Mise à jour de l'affichage avec les discussions de l'utilisateur, méthode appelée par la requête Ajax
  *
  * @param {Object} jqXHR - L'objet renvoyée par la requête Ajax avec entre autres un tableau de discussions.
  */
function callBackGetDiscussions(jqXHR) {
  $("#dropdown_navbar_discussions").empty();
  var line = '';
  for(var discussion in jqXHR.responseJSON) {
    line = "<li><a href=\"discussion.jsp?id=" + jqXHR.responseJSON[discussion].id + "\">" + jqXHR.responseJSON[discussion].name + "</a></li>";
    $("#dropdown_navbar_discussions").append(line);
  }
}
