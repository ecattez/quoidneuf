var discussionId;
var nbMessages;
var interval;
var userId;
var dyna;

/**
  * Ajoute les EventListeners aux boutons, charge le nom de la discussion et les messages
  */
function initDiscussionPage(uId, dId) {
  if(uId == undefined || uId == '') {
    window.location.href = '../';
  }
  $("#li_membres").removeClass("hidden");
  nbMessages = 0;
  userId = uId;
  discussionId = dId;
  getMembers(discussionId);
  getMessages(discussionId);

  clearButtonListeners();
  addButtonListeners();

  interval = setInterval(refreshMessages, 1000);
  loadMembersDynatable();
  $("#file_loader").change(function() { readURL(this); });
}

/**
  *
  */
function loadMembersDynatable() {
  $('#search_result_table').dynatable({
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
  * Détache les EventListeners des boutons
  */
function clearButtonListeners() {
  $("#send_message").off("click");

  $("#message_input").off("enterKey");

  $("#message_input").off("keyup");

  $("#add_member").off("click");

  $("#member_input").off("enterKey");

  $("#member_input").off("keyup");

  $("#leave_discussion_button").off("click");
}

/**
  * Ajoute les EventListeners aux boutons
  */
function addButtonListeners() {
  $("#send_message").on("click", ajoutMessage);

  $("#message_input").on("enterKey", ajoutMessage);

  $("#message_input").keyup(function(e) {
    if(e.keyCode == 13)
    {
      $(this).trigger("enterKey");
    }
  });

  $("#search_member").on("click", chercherMembre);

  $("#member_search_input").on("enterKey", chercherMembre);

  $("#member_search_input").keyup(function(e) {
    if(e.keyCode == 13)
    {
      $(this).trigger("enterKey");
    }
  });

  $("#leave_discussion_button").on("click", quitterDiscussion);
}

/**
  * Vide la discussion, le nom de cette dernière et ses membres pour charger dynamiquement une nouvelle discussion
  */
function changeDiscussion(dId) {
  clearInterval(interval);
  $("#discussion_name").replaceWith("<h2 id=\"discussion_name\">Discussion</h2>");
  $("#discussion_membres").empty();
  $("#messages").empty();
  initDiscussionPage(userId, dId);
}

/**
  * Charge les membres de la discussion dans la page
  */
function callBackGetMembers(data) {
  var line = '';
  $("#discussion_name").replaceWith("<h2 id=\"discussion_name\">"+data.name+"</h2>");
  $("#discussion_membres").empty();
  var memb;
  for(var membre in data.current_subscribers) {
    memb = data.current_subscribers[membre];
    line = "<li><p class=\"row\">";
    line += "<a href=\"profile.jsp?id="+ memb.id +"\">";
    line += memb.firstName + " " + memb.lastName;
    line += "</a></p>";
    $("#discussion_membres").append(line);
  }
}

/**
  * Charge les messages de la discussion courante
  */
function callBackGetMessages(data) {
  var date = '';
  var user = '';
  var content = '';
  var msg;
  var line;
  for(var mess = nbMessages; mess < data.messages.length; mess++) {
    msg = data.messages[mess];
    date = formeDate(msg.writtenDate);
    user = msg.owner.firstName + "." + msg.owner.lastName.substring(0,1);
    content = msg.content;
    if(msg.owner.id == userId) {
      line = "<div class=\"bubbledRight\">";
    }
    else {
      line = "<div class=\"bubbledLeft\">" + user + " ";
    }
    line += "[" + date + "] :<br>" + content + "</div>";
    $("#messages").append(line);
    //TODO : Trouver pourquoi le $("#messages").scrollTop($('#messages').height()) ne fonctionne pas
    $("#messages").scrollTop(100000000000);
  }
  nbMessages = data.messages.length;
}

/**
  * Mise en forme de la date au format YYYY-MM-DD hh:mm:ss
  *
  * @param {String} date - La date au format epoch
  *
  * @return {String} La date au format YYYY-MM-DD hh:mm:ss
  */
function formeDate(date) {
  d = new Date(date);
  year = "" + d.getFullYear();
  month = "" + (d.getMonth() + 1); if (month.length == 1) { month = "0" + month; }
  day = "" + d.getDate(); if (day.length == 1) { day = "0" + day; }
  hour = "" + d.getHours(); if (hour.length == 1) { hour = "0" + hour; }
  minute = "" + d.getMinutes(); if (minute.length == 1) { minute = "0" + minute; }
  second = "" + d.getSeconds(); if (second.length == 1) { second = "0" + second; }
  return year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
}

/**
  * Ajoute une message à la discussion
  */
function ajoutMessage() {
  var message = $("#message_input").val();
  if(message != '') {
    writeMessage(discussionId, message);
  }
  $("#message_input").val("");
}

/**
  * Ajoute une message à la discussion
  */
function ajoutMembre(member) {
  addMember(discussionId, member);
}

/**
  * Vérifie le champ et envoie la requête de recherche d'utilisateur
  */
function chercherMembre() {
  var member = $("#member_search_input").val();
  if(member != '') {
    searchForMembre(member);
  }
}

/**
  * Vérifie l'objet de retour de la méthode d'ajout de message
  *
  * @param {Object} data - L'id du message si ce dernier est bien envoyé, une erreur sinon.
  */
function callBackWriteMessage(data) {
  if(!data.id) {
    updateErrorMessage('error_div', false, 'Erreur à l\'envoie du message');
  }
}

/**
  * Vérifie l'objet de retour de la méthode d'ajout de membre
  *
  * @param {Object} data - Vide si bien ajouté, message d'erreur sinon
  */
function callBackAddMember(data) {
  if(data == undefined) {
    getMembers(discussionId);
    updateErrorMessage('error_search_div', true, 'Membre bien ajouté !');
  }
  else {
    updateErrorMessage('error_search_div', false, data.responseJSON.message);
  }
}

/**
  * Mise à jour de l'affichage avec les membres correspondants à la recherche
  */
function callBackSearchForMember(data) {
  if(data.status) {
    updateErrorMessage('error_search_div', false, data.statusText);
  }
  else {
    updateErrorMessage('error_search_div', true, 'Résultat de la recherche...');
    affichageMembres(data);
  }
}

/**
  * Met à jour l'affichage de la fenêtre modale et y ajoute les membres corresponants à la recherche
  */
function affichageMembres(membres) {
  for(var membre in membres) {
    membres[membre].id = "<button onclick=\"ajoutMembre("+membres[membre].id+")\">Ajouter</button>";
  }
  $('#search_result_table').data('dynatable').settings.dataset.originalRecords = membres;
  $('#search_result_table').data('dynatable').process();
}

/**
  * Ajoute les nouveaux messages à la suite de la discussion
  */
function refreshMessages() {
  getMessages(discussionId);
}

/**
  * Envoie une requête pour quitter le groupe
  */
function quitterDiscussion() {
  leaveDiscussion(discussionId);
}

/**
  * Met à jour l'affichage et redirige vers la page de profil si réussite
  */
function callBackLeaveDiscussion(data) {
  if(data == undefined) {
    updateErrorMessage('error_div', true, "Utilisateur retiré de la conversation.");
    window.location.href = 'profile.jsp';
  }
  else {
    updateErrorMessage('error_div', false, data.responseJSON.message);
  }
}

/**
  * Vérifie l'element et affiche l'image.
  */
function readURL(input) {
  $("#img_preview").removeClass("hidden");
  if(input.files && input.files[0] && input.files[0].type.match("image")) {
    var reader = new FileReader();
    reader.onload = function(e) {
      $("#img_preview").attr('src', e.target.result);
    }
    reader.readAsDataURL(input.files[0]);
  }
  else {
    $("#img_preview").addClass("hidden");
  }
}

/**
  *
  */
function envoieFichier() {
  console.log($("#file_loader").prop('files')[0]);
  updateErrorMessage("send_file_error", false, "Fonction non implémentée.");
}

/**
  * Met à jour une division d'erreur
  *
  * @param {String} div - La division à mettre à jour
  * @param {Boolean} bool - <true> si message d'erreur, <false> si message de réussite
  * @param {String} mess - Le message de la division
  */
function updateErrorMessage(div, bool, mess) {
  $("#"+div).removeClass("hidden");
  $("#"+div).removeClass("alert-success");
  $("#"+div).removeClass("alert-danger");

  $("#"+div).empty();

  if(bool == true) {
    $("#"+div).addClass("alert-success");
  }
  else {
    $("#"+div).addClass("alert-danger");
  }
  $("#"+div).append("<p>"+mess+"</p>");
}
