/**
  * Authentification d'un utilisateur
  * @param {string} username - Le login de l'utilisateur
  * @param {string} password - Le mot de passe de l'utilisateur (crypté ?)
  */
function login(user, pass) {
  $.ajax({
		type : 'POST',
		contentType : 'application/x-www-form-urlencoded',
		url : '/quoidneuf/api/authentication',
		dataType : 'json',
		data : { username : user, password : pass},
		success : function(data, textStatus, jqXHR) {
			console.log(textStatus);
		},
		error : function(jqXHR, textStatus, errorThrown) {
			console.log('error: ' + textStatus);
		}
	});
}
/**
  * Déconnexion de l'utilisateur actuellement authentifié
  */
function logout() {
	$.ajax({
		type : 'DELETE',
		url : '/quoidneuf/api/authentication',
		success : function(data, textStatus, jqXHR) {
			console.log(textStatus);
		},
		error : function(jqXHR, textStatus, errorThrown) {
			console.log('error: ' + textStatus);
		}
	});
}

/**
  * Retourne tous les messages d'une discussion
  * @param {Number} discussionId - L'id de la discussion à retourner
  * @return {json}
  */
function getDiscuttion(discussionId) {

}
