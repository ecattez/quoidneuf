// TODO : @param / @return de JSon : décrire chaque élément de l'objet.

// Login / Logout :

/**
  * Authentification d'un utilisateur
  *
  * @param {String} username - Le login de l'utilisateur
  * @param {String} password - Le mot de passe de l'utilisateur (crypté ?)
  *
  * @return {Json}
  */
function login(user, pass) {
  console.log(ajaxRequest('POST', '/quoidneuf/api/authentication', 'application/x-www-form-urlencoded', 'json', { username : user, password : pass }));
  // return ajaxRequest('POST', '/quoidneuf/api/authentication', 'application/x-www-form-urlencoded', 'json', { username : user, password : pass });
}

/**
  * TODO : Compléter doc et tester
  * Déconnexion de l'utilisateur actuellement authentifié
  *
  * @return {?} ? si la déconnexion à réussi
  * @return {Boolean} <false> sinon
  */
function logout() {
  return ajaxRequest('DELETE', '/quoidneuf/api/authentication');
}

//-----------------------------------------------
// Pour navbar : Récupérer les discussions et les amis

/**
  * TODO : Tester
  * Retourne toutes les discussions d'un utilisateur (id en session)
  *
  * @return {Json} - La liste de toutes les discussions
  */
function getDiscussions() {
  return ajaxRequest('GET', '/quoidneuf/api/discussions');
}

/**
  * TODO : Implémenter et tester (Doit fonctionner avec l'utilisateur courant et n'importe quel autre avec l'id pour le profil)
  * Retourne tous les amis d'un utilisateur
  *
  * @param {Number} userId - L'id de l'utilisateur
  *
  * @return {Json} La liste des amis de l'utilisateur
  */
function getFriends(userId) {
  return 'A implémenter';
}

//-----------------------------------------------
// Pour page discussion : Récupérer les membres et les messages, écrire un message

/**
  * TODO : Implémenter et tester
  * Retourne les membres de la discussion
  *
  * @param {Number} discussionId - L'id de la discussion
  *
  * @return {Json} La liste des utilisateurs participants à la discussion
  */
function getMembers(discussionId) {
  var data = ajaxRequest('GET', '/quoidneuf/api/discussions', undefined, 'json', { id : discussionId });
  // return data.subscribers; ?
  return 'A implémenter';
}

/**
  * TODO : Implémenter et tester
  * Retourne les messages de la discussion
  *
  * @param {Number} discussionId - L'id de la discussion
  *
  * @return {Json} La liste des messages de la discussion
  */
function getMessages(discussionId) {
  var data = ajaxRequest('GET', '/quoidneuf/api/discussions', undefined, 'json', { id : discussionId });
  // return data.discussion; ?
  return 'A implémenter';
}

/**
  * TODO : Implémenter et tester
  * Ecrit un message dans une discussion (id utilisateur dans la session)
  *
  * @param {Number} discussionId - L'id de la discussion
  * @param {String} content - Le contenu du Message
  *
  * @return {?} ?
  */
function writeMessage(discussionId, content) {
  return ajaxRequest('POST', '/quoidneuf/api/messages', undefined, 'json', { discussion : discussionId, content : content });
}

//-----------------------------------------------
// Pour page de profil : Récupérer amis (dans la partie Navbar) et informations

/**
  * TODO : Implémenter et tester
  * Retourne les informations d'un utilisateur
  *
  * @param {Number} userId - L'id de l'utilisateur
  *
  * @return {Json} Les informations de cet utilisateur
  */
function getSubscribersProfile(userId) {
  return ajaxRequest('GET', '/quoidneuf/api/profils', undefined, 'json', { id : userId });
}


//-----------------------------------------------
// Gabarits pour requêtes Ajax

/**
  * TODO : Tester
  * Template de requête Ajax
  *
  * @param {String} type - Le type de requête HTTP ('GET'/'POST'/'PUT'/'PUT'/'DELETE')
  * @param {String} url -
  * @param {String} contentType - Le type de données envoyées. Si vide : requête sans données / juste avec des variables de session
  * @param {String} dataType - Le type de données renvoyées par le serveur
  * @param {String} date - Les données envoyées au serveur
  *
  * @return {String} Le message renvoyé par le serveur : Les données si la requête s'est bien passée, le message d'erreur sinon.
  */
function ajaxRequest(type, url, contentType, dataType, data) {
  if((!contentType) && (!dataType) && (!data)) {
    // Requête sans envoie de données
    return $.ajax({
  		type : type,
  		url : url,
  		success : function(data, textStatus, jqXHR) {
  			// console.log(textStatus);
  		},
  		error : function(jqXHR, textStatus, errorThrown) {
  			// console.log('error: ' + textStatus);
  		}
  	});
  }
  else {
    // Requête avec envoie de données
    return $.ajax({
  		type : type,
  		contentType : contentType,
  		url : url,
  		dataType : dataType,
  		data : data,
  		success : function(data, textStatus, jqXHR) {
        // console.log(textStatus);
  		},
  		error : function(jqXHR, textStatus, errorThrown) {
  			// console.log('error: ' + textStatus);
  		}
  	});
  }
}

//-----------------------------------------------
// Pour les tests :
