var discussionId;

/**
  * Ajoute les EventListeners aux boutons, charge le nom de la discussion et les messages
  */
function initDiscussionPage(dId) {
  discussionId = dId;
  getMembers(discussionId);
  getMessages(discussionId);
}

/**
  * Charge les membres de la discussion dans la page
  */
function callBackGetMembers(jqXHR) {
  var line = '';
  $("#discussion_name").replaceWith("<h2>"+jqXHR.responseJSON.name+"</h2>");
  var membres = jqXHR.responseJSON.current_subscribers;
  for(var membre in membres) {
    //line = "<li><div class=\"row\"><div class=\"col-lg-8 col-md-8 col-sm-8 col-xs-8 nom_membres\">"+membres[membre].firstName + " " + membres[membre].lastName + "</div><div class=\"col-lg-1 col-lg-offset-1 col-md-1 col-md-offset-1 col-sm-1 col-sm-offset-1 col-xs-1 col-xs-offset-1\"><button type=\"button\" name=\"button_add_friend"++"\">+</button></div></div></li>";
    line = "<li><div class=\"row\"><div class=\"col-lg-8 col-md-8 col-sm-8 col-xs-8 nom_membres\">"
    + membres[membre].firstName + "." + membres[membre].lastName.substring(0,1)
    + "</div><div class=\"col-lg-1 col-lg-offset-1 col-md-1 col-md-offset-1 col-sm-1 col-sm-offset-1 col-xs-1 col-xs-offset-1\">"
    + "<button type=\"button\" name=\"button_add_friend_"+  membres[membre].id +"\">+</button></div></div></li>"
    $("#discussion_membres").append(line);
  }
}

/**
  * Charge les messages de la discussion courante
  */
function callBackGetMessages(jqXHR) {
  var date = '';
  var user = '';
  var content = '';
  for(var mess in jqXHR.responseJSON.messages) {
    date = formeDate(jqXHR.responseJSON.messages[mess].writtenDate);
    user = jqXHR.responseJSON.messages[mess].subscriber.firstName + "." +jqXHR.responseJSON.messages[mess].subscriber.lastName.substring(0,1);
    content = jqXHR.responseJSON.messages[mess].content;
    $("#messages").prepend(user + " \t[" + date + "] : \t" + content + "\n");
  }
}

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
  * Ajoute les nouveaux messages à la suite de la discussion
  */
function refreshMessages() {

}
