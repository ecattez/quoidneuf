var page;

/**
  * Ajoute les EventListeners aux elements de la navbar
  */
function initNavbar(user, p) {
  page = p;
  if(user == undefined) {
    window.location.href = '../';
  }
  $("#navbar_deconnexion").on("click", function() {
    logout();
  });

  $("#navbar_amis").on("click", function() {
    getFriends(user);
    getRequestedFriends(user);
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
  $("#dropdown_navbar_amis").append("<li><a>Amis</a></li>");
  var line = '';
  for(var ami in data) {
    line = "<li>"
    if(page == 'profile') {
      line += "<a id=\"profile_" + data[ami].id + "\">" + data[ami].firstName + " " + data[ami].lastName + "</a>";
    }
    else {
      line += "<a href=\"profile.jsp?id=" + data[ami].id + "\">" + data[ami].firstName + " " + data[ami].lastName + "</a>";
    }
    line += "</li>"
    $("#dropdown_navbar_amis").append(line);

    addProfileListener(data[ami].id);
  }
}

/**
  * Mise à jour de l'affichage avec les demande d'amis de l'utilisateur
  *
  * @param {Object} data - L'objet renvoyée par la requête Ajax avec le tableau d'amis.
  */
function callBackGetFriendRequests(data) {
  $("#dropdown_navbar_amis").append("<li role=\"separator\" class=\"divider\"></li>");
  $("#dropdown_navbar_amis").append("<li><a>Demandes en attentes</a></li>");
  var line = '<>';
  for(var ami in data) {
    line = "<li>"
    if(page == 'profile') {
      line += "<a id=\"profile_" + data[ami].id + "\">" + data[ami].firstName + " " + data[ami].lastName + "</a>";
    }
    else {
      line += "<a href=\"profile.jsp?id=" + data[ami].id + "\">" + data[ami].firstName + " " + data[ami].lastName + "</a>";
    }
    line += "</li>"
    $("#dropdown_navbar_amis").append(line);
    addProfileListener(data[ami].id);
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
    line = "<li>";
    if(page == 'discussion') {
      line += "<a id=\"discussion_" + data[discussion].id + "\">" + data[discussion].name + "</a>";
    }
    else {
      line += "<a href=\"discussion.jsp?id=" + data[discussion].id + "\">" + data[discussion].name + "</a>";
    }
    line += "</li>";
    $("#dropdown_navbar_discussions").append(line);
    addDiscussionListener(data[discussion].id);
  }
}

/**
  * Ajoute le listener sur les utilisateurs
  */
function addProfileListener(userId) {
  $("#profile_"+userId).on("click", function() { initProfilePage(id, userId) });
}

/**
  * Ajoute le listener sur les discussions
  */
function addDiscussionListener(discussionId) {
  $("#discussion_"+discussionId).on("click", function() { changeDiscussion(discussionId) });
}
