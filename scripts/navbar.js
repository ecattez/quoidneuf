var page;

/**
  * Ajoute les EventListeners aux elements de la navbar
  */
function initNavbar(user, p) {
  page = p;
  if(user == undefined) {
    window.location.href = '../';
  }

  loadListeners(user);
  loadNewFriendsDynatable();
}

/**
  * Ajoute les EventListeners aux boutons
  */
function loadListeners(user) {
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

  $("#search_new_friend").on("click", chercherAmi);

  $("#member_search_friend_input").on("enterKey", chercherAmi);

  $("#member_search_friend_input").keyup(function(e) {
    if(e.keyCode == 13)
    {
      $(this).trigger("enterKey");
    }
  });
}

/**
  *
  */
function loadNewFriendsDynatable() {
  $("#search_result_friend_table").dynatable({
    features: {
      paginate: false,
      search: false,
      recordCount: false,
      perPageSelect: false
    },
    dataset: {
      records: ''
    }
  });
}

/**
  * Vérifie le champ et envoie la requête de recherche d'utilisateur
  */
function chercherAmi() {
  var member = $("#member_search_friend_input").val();
  if(member != '') {
    searchForSubscriber(member);
  }
}

/**
  * Mise à jour de l'affichage avec les membres correspondants à la recherche
  */
function callBackSearchForSubscriber(data) {
  if(data.status) {
    updateErrorMessage('error_new_friend_div', false, data.statusText);
  }
  else {
    updateErrorMessage('error_new_friend_div', true, 'Résultat de la recherche...');
    affichageSubscribers(data);
  }
}

/**
  * Met à jour l'affichage de la fenêtre modale et y ajoute les membres corresponants à la recherche
  */
function affichageSubscribers(membres) {
  for(var membre in membres) {
    membres[membre].id = "<a href='profile.jsp?id="+membres[membre].id+"' class=\"btn btn-primary\">Profil</button>";
  }
  $('#search_result_friend_table').data('dynatable').settings.dataset.originalRecords = membres;
  $('#search_result_friend_table').data('dynatable').process();
}

/**
  * Envoie la requête d'ajout aux amis
  */
function ajoutAmi(id) {
  addFriendRequestFromNavbar(id);
}

/**
  * Mise à jour de l'affichage d'après le retour du serveur
  */
function callBackAddFriendRequestFromNavbar(data) {
  if(data.status) {
    updateErrorMessage('error_new_friend_div', false, data.responseJSON.message);
  }
  else {
    updateErrorMessage('error_new_friend_div', true, 'Requête envoyée !');
  }
}

/**
  * Vérifie le code de retour de la déconnexion, méthode appelée par la requête Ajax
  *
  * @param {Object} data - L'objet renvoyé par le serveur, vide si réussi
  */
function callBackLogout(data) {
  window.location.href = '../';
}

/**
  * Mise à jour de l'affichage avec les amis de l'utilisateur, méthode appelée par la requête Ajax
  *
  * @param {Object} data - L'objet renvoyée par la requête Ajax avec le tableau d'amis.
  */
function callBackGetFriends(data) {
  $("#dropdown_navbar_amis").empty();
  $("#dropdown_navbar_amis").append("<li><p><button class=\"btn btn-primary\" data-toggle=\"modal\" data-target=\"#modal_add_friend\">Chercher profil</button></p></li>");
  $("#dropdown_navbar_amis").append("<li><p>Amis</p></li>");
  var line = '';
  for(var ami in data) {
    line = "<li>"
    if(page == 'profile') {
      line += "<a href='#' id=\"profile_" + data[ami].id + "\">" + data[ami].firstName + " " + data[ami].lastName + "</a>";
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
      line += "<a href='#' id=\"profile_" + data[ami].id + "\">" + data[ami].firstName + " " + data[ami].lastName + "</a>";
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
  affichageNouvelleDiscussion();
  $("#dropdown_navbar_discussions").append("<p>Discussion(s)</p>");
  var line = '';
  for(var discussion in data) {
    line = "<li>";
    if(page == 'discussion') {
      line += "<a href='#' id=\"discussion_" + data[discussion].id + "\">" + data[discussion].name + "</a>";
    }
    else {
      line += "<a href=\"discussion.jsp?id=" + data[discussion].id + "\">" + data[discussion].name + "</a>";
    }
    line += "</li>";
    $("#dropdown_navbar_discussions").append(line);
    if(page == 'discussion') {
      addDiscussionListener(data[discussion].id);
    }
  }
}

/**
  * Affiche le formulaire de création de discussion
  */
function affichageNouvelleDiscussion() {
  $("#dropdown_navbar_discussions").append("<p>Nouvelle discussion</p>");
  var line = "<p><li><input id=\"new_discussion_name\" type=\"text\" placeholder=\"Nom de la discussion\"/></li></p>";
  $("#dropdown_navbar_discussions").append(line);
  $("#dropdown_navbar_discussions").append("<li role=\"separator\" class=\"divider\"></li>");

  $("#new_discussion_name").on("enterKey", nouvelleDiscussion);

  $("#new_discussion_name").keyup(function(e) {
    if(e.keyCode == 13)
    {
      $(this).trigger("enterKey");
    }
  });
}

/**
  * Envoie une requete de création de discussion
  */
function nouvelleDiscussion() {
  if($("#new_discussion_name").val() != '') {
    createDiscussion($("#new_discussion_name").val());
    $("#new_discussion_name").val('');
  }
}

/**
  * Vérifie le retour de la création d'une discussion
  */
function callBackCreateDiscussion(data) {
  console.log(data);
  if(data.id) {
    $("#dropdown_navbar_discussions").append("<li class=\"alert-success\"><p>Discussion créée</p></li>");
    writeFirstMessage(data.id);
  }
  else {
    $("#dropdown_navbar_discussions").append("<li class=\"alert-danger\"><p>"+data.message+"</p></li>");
  }
}

/**
  * Vérifie le retour de l'écriture du premier message dans une nouvelle discussion
  */
function callBackWriteFirstMessage(data) {
  console.log(data);
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
