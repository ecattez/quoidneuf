var discussionId;
var nbMessages;
var interval;

/**
  * Ajoute les EventListeners aux boutons, charge le nom de la discussion et les messages
  */
function initDiscussionPage(dId) {
  nbMessages = 0;
  discussionId = dId;
  getMembers(discussionId);
  getMessages(discussionId);

  clearButtonListeners();
  addButtonListeners();

  interval = setInterval(refreshMessages, 1000);
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

  $("#add_member").on("click", ajoutMembre);

  $("#member_input").on("enterKey", ajoutMembre);

  $("#member_input").keyup(function(e) {
    if(e.keyCode == 13)
    {
      $(this).trigger("enterKey");
    }
  });
}

/**
  * Vide la discussion, le nom de cette dernière et ses membres pour charger dynamiquement une nouvelle discussion
  */
function changeDiscussion(dId) {
  clearInterval(interval);
  $("#discussion_name").replaceWith("<h2 id=\"discussion_name\">Discussion</h2>");
  $("#discussion_membres").empty();
  $("#messages").empty();
  initDiscussionPage(dId);
}

/**
  * Charge les membres de la discussion dans la page
  */
function callBackGetMembers(data) {
  var line = '';
  $("#discussion_name").replaceWith("<h2 id=\"discussion_name\">"+data.name+"</h2>");
  $("#discussion_membres").empty();
  var membres = data.current_subscribers;
  for(var membre in membres) {
    //line = "<li><div class=\"row\"><div class=\"col-lg-8 col-md-8 col-sm-8 col-xs-8 nom_membres\">"+membres[membre].firstName + " " + membres[membre].lastName + "</div><div class=\"col-lg-1 col-lg-offset-1 col-md-1 col-md-offset-1 col-sm-1 col-sm-offset-1 col-xs-1 col-xs-offset-1\"><button type=\"button\" name=\"button_add_friend"++"\">+</button></div></div></li>";
    line = "<li><p class=\"row\">";
    line += "<a href=\"profile.jsp?id="+membres[membre].id+"\">";
    line += membres[membre].firstName + " " + membres[membre].lastName;
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
  for(var mess = nbMessages; mess < data.messages.length; mess++) {
    date = formeDate(data.messages[mess].writtenDate);
    user = data.messages[mess].subscriber.firstName + "." +data.messages[mess].subscriber.lastName.substring(0,1);
    content = data.messages[mess].content;
    $("#messages").append(user + " \t[" + date + "] : \t" + content + "\n");
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
function ajoutMembre() {
  var member = $("#member_input").val();
  if(member != '') {
    //TODO : Trouver l'id du membre
    addMember(discussionId, member);
  }
  $("#member_input").val("");
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
  console.log(data);
  if(data == undefined) {
    getMembers(discussionId);
    updateErrorMessage('error_div', true, 'Membre bien ajouté !');
  }
  else {
    updateErrorMessage('error_div', false, data.responseJSON.message);
  }
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

  if(bool == true) {
    $("#"+div).addClass("alert-success");
  }
  else {
    $("#"+div).addClass("alert-danger");
  }
  $("#"+div).append("<p>"+mess+"</p>");
}

/**
  * Ajoute les nouveaux messages à la suite de la discussion
  */
function refreshMessages() {
  getMessages(discussionId);
}
