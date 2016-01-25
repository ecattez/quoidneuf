var discussionId;

/**
  * Ajoute les EventListeners aux boutons, charge le nom de la discussion et les messages
  */
function initDiscussionPage(dId) {
  discussionId = dId;
  getMembers(discussionId);
}

/**
  * Charge les membres de la discussion dans la page
  */
function callBackGetMembers(jqXHR) {
  console.log(jqXHR);
  var line = '';
  $("#discussion_name").replaceWith("<h2>"+jqXHR.responseJSON.name+"</h2>");
  var membres = jqXHR.responseJSON.current_subscribers;
  for(var membre in membres) {
    //line = "<li><div class=\"row\"><div class=\"col-lg-8 col-md-8 col-sm-8 col-xs-8 nom_membres\">"+membres[membre].firstName + " " + membres[membre].lastName + "</div><div class=\"col-lg-1 col-lg-offset-1 col-md-1 col-md-offset-1 col-sm-1 col-sm-offset-1 col-xs-1 col-xs-offset-1\"><button type=\"button\" name=\"button_add_friend"++"\">+</button></div></div></li>";
    line = "<li><div class=\"row\"><div class=\"col-lg-8 col-md-8 col-sm-8 col-xs-8 nom_membres\">"
    + membres[membre].firstName.substring(0,1) + "." + membres[membre].lastName
    + "</div><div class=\"col-lg-1 col-lg-offset-1 col-md-1 col-md-offset-1 col-sm-1 col-sm-offset-1 col-xs-1 col-xs-offset-1\">"
    + "<button type=\"button\" name=\"button_add_friend"+  membres[membre].id +"\">+</button></div></div></li>"
    $("#discussion_membres").append(line);
  }
}

/**
  * Charge les messages de la discussion courante
  */
function initMessages() {

}

/**
  * Ajoute les nouveaux messages à la suite de la discussion
  */
function refreshMessages() {

}
