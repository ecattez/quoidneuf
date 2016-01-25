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

function callBackLogout(jqXHR) {
  if(jqXHR.status == 204) {
    window.location.href = '../';
  }
}

function callBackGetFriends(jqXHR) {
  console.log(jqXHR);
}

function callBackGetDiscussions(jqXHR) {
  $("#dropdown_navbar_discussions").empty();
  var line = '';
  for(var discussion in jqXHR.responseJSON) {
    line = "<li><a href=\"discussion.jsp?id=" + jqXHR.responseJSON[discussion].id + "\">" + jqXHR.responseJSON[discussion].name + "</a></li>";
    $("#dropdown_navbar_discussions").append(line);
  }
}
