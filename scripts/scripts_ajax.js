// TODO : @param / @return de JSon/Object : décrire chaque élément de l'objet.

// Login / Logout :

/**
  * Authentification d'un utilisateur
  *
  * @param {String} username - Le login de l'utilisateur
  * @param {String} password - Le mot de passe de l'utilisateur (crypté ?)
  */
function login(user, pass) {
  ajaxRequest('POST', '/quoidneuf/api/authentication', 'callBackLogin', 'application/x-www-form-urlencoded', 'json', { username : user, password : pass });
}

/**
  * Déconnexion de l'utilisateur actuellement authentifié
  */
function logout() {
  ajaxRequest('DELETE', '/quoidneuf/api/authentication', 'callBackLogout');
}

//-----------------------------------------------
// Pour navbar : Récupérer les discussions et les amis

/**
  * Retourne toutes les discussions d'un utilisateur (id en session)
  */
function getDiscussions() {
  ajaxRequest('GET', '/quoidneuf/api/discussions', 'callBackGetDiscussions');
}

/**
  * TODO : Implémenter et tester (Doit fonctionner avec l'utilisateur courant et n'importe quel autre avec l'id pour le profil)
  * Retourne tous les amis d'un utilisateur
  *
  * @param {Number} userId - L'id de l'utilisateur
  */
function getFriends(userId) {
  ajaxRequest('GET', '/quoidneuf/api/friends', 'callBackGetFriends', undefined, 'json', { status : false });
}

//-----------------------------------------------
// Pour page discussion : Récupérer les membres et les messages, écrire un message

/**
  * TODO : Tester
  * Retourne les membres de la discussion
  *
  * @param {Number} discussionId - L'id de la discussion
  */
function getMembers(discussionId) {
  ajaxRequest('GET', '/quoidneuf/api/discussions', 'callBackGetMembers', undefined, 'json', { id : discussionId });
}

/**
  * Retourne les messages de la discussion
  *
  * @param {Number} discussionId - L'id de la discussion
  */
function getMessages(discussionId) {
  ajaxRequest('GET', '/quoidneuf/api/discussions', 'callBackGetMessages', undefined, 'json', { id : discussionId });
}

/**
  * TODO : Tester
  * Ecrit un message dans une discussion (id utilisateur dans la session)
  *
  * @param {Number} discussionId - L'id de la discussion
  * @param {String} content - Le contenu du Message
  */
function writeMessage(discussionId, content) {
  ajaxRequest('POST', '/quoidneuf/api/messages', 'callBackWriteMessage', undefined, 'json', { discussion : discussionId, content : content });
}

//-----------------------------------------------
// Pour page de profil : Récupérer amis (dans la partie Navbar) et informations

/**
  * TODO : Tester
  * Retourne les informations d'un utilisateur
  *
  * @param {Number} userId - L'id de l'utilisateur
  */
function getSubscribersProfile(userId) {
  ajaxRequest('GET', '/quoidneuf/api/profils', 'refreshMessages', undefined, 'json', { id : userId });
}


//-----------------------------------------------
// Gabarits pour requêtes Ajax

/**
  * Template de requête Ajax
  *
  * @param {String} type - Le type de requête HTTP ('GET'/'POST'/'PUT'/'PUT'/'DELETE')
  * @param {String} url - L'url de la servlet appelée
  * @param {String} fonction - Le nom de la fonction de retour à appeler
  * @param {String} contentType - Le type de données envoyées. Si vide : requête sans données / juste avec des variables de session
  * @param {String} dataType - Le type de données renvoyées par le serveur
  * @param {String} date - Les données envoyées au serveur
  */
function ajaxRequest(type, url, fonction, contentType, dataType, data) {
  if((!contentType) && (!dataType) && (!data)) {
    // Requête sans envoie de données
    $.ajax({
  		type : type,
  		url : url,
  		success : function(data, textStatus, jqXHR) {
          window[fonction](data);
  		},
  		error : function(jqXHR, textStatus, errorThrown) {
        window[fonction](jqXHR);
  		}
  	});
  }
  else {
    // Requête avec envoie de données
    $.ajax({
  		type : type,
  		contentType : contentType,
  		url : url,
  		dataType : dataType,
  		data : data,
  		success : function(data, textStatus, jqXHR) {
        window[fonction](data);
  		},
  		error : function(jqXHR, textStatus, errorThrown) {
        window[fonction](jqXHR);
  		}
  	});
  }
}
