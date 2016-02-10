// TODO : @param / @return de JSon/Object : décrire chaque élément de l'objet.

// Login / Logout :

/**
  * Authentification d'un utilisateur
  *
  * @param {String} username - Le login de l'utilisateur
  * @param {String} password - Le mot de passe de l'utilisateur (crypté ?)
  */
function login(user, pass) {
  ajaxRequest('POST', '../api/authentication', 'callBackLogin', 'application/x-www-form-urlencoded', 'json', { username : user, password : pass });
}

/**
  * Déconnexion de l'utilisateur actuellement authentifié
  */
function logout() {
  ajaxRequest('DELETE', '../api/authentication', 'callBackLogout');
}

/**
  * Création d'un compte
  *
  * @param Les informations du nouvel utilisateur
  */
function createUser(username, password, firstname, lastname, birthday, description, email, phone) {
  ajaxRequest('POST', '../api/profiles', 'callBackCreateUser', undefined, 'json', { username : username, password : password, firstname : firstname, lastname : lastname, birthday : birthday, description : description, email : email, phone : phone });
}

/**
  * Mot de passe perdu
  *
  * @param Le mail et le login de l'utilisateur
  */
function passwordLost(username, email) {
  ajaxRequest('POST', '../api/recover', 'callBackPasswordLost', undefined, 'json', { username : username, email : email });
}

//-----------------------------------------------
// Pour navbar : Récupérer les discussions et les amis

/**
  * Retourne toutes les discussions d'un utilisateur (id en session)
  */
function getDiscussions() {
  ajaxRequest('GET', '../api/discussions', 'callBackGetDiscussions');
}

/**
  * Retourne tous les amis d'un utilisateur
  *
  * @param {Number} userId - L'id de l'utilisateur
  */
function getFriends(userId) {
  ajaxRequest('GET', '../api/friends', 'callBackGetFriends', undefined, 'json', { id : userId, status : true });
}

/**
  * Retourne tous les amis d'un utilisateur
  *
  * @param {Number} userId - L'id de l'utilisateur
  */
function getFriendsProfile(userId) {
  ajaxRequest('GET', '../api/friends', 'callBackGetFriendsProfile', undefined, 'json', { id : userId, status : true });
}

/**
  * Retourne tous les demandes d'amis d'un utilisateur
  *
  * @param {Number} userId - L'id de l'utilisateur
  */
function getRequestedFriends(userId) {
  ajaxRequest('GET', '../api/friends', 'callBackGetFriendRequests', undefined, 'json', { id : userId, status : false });
}

/**
  * Cherche un membre
  *
  * @param {String} search - Le champs de recherche
  */
function searchForSubscriber(search) {
  ajaxRequest('GET', '../api/recover?firstname='+search+'&lastname='+search+'&email='+search, 'callBackSearchForSubscriber');
}

/**
  * Créer une demande d'amitié
  *
  * @param {Number} userId - L'id de l'utilisateur
  */
function addFriendRequestFromNavbar(userId) {
  ajaxRequest('POST', '../api/friends', 'callBackAddFriendRequestFromNavbar', undefined, 'json', { friend : userId });
}

//-----------------------------------------------
// Pour page discussion : Récupérer les membres et les messages, écrire un message

/**
  * Retourne les membres de la discussion
  *
  * @param {Number} discussionId - L'id de la discussion
  */
function getMembers(discussionId) {
  ajaxRequest('GET', '../api/discussions', 'callBackGetMembers', undefined, 'json', { id : discussionId });
}

/**
  * Retourne les messages de la discussion
  *
  * @param {Number} discussionId - L'id de la discussion
  */
function getMessages(discussionId) {
  ajaxRequest('GET', '../api/discussions', 'callBackGetMessages', undefined, 'json', { id : discussionId });
}

/**
  * Ecrit un message dans une discussion (id utilisateur dans la session)
  *
  * @param {Number} discussionId - L'id de la discussion
  * @param {String} content - Le contenu du Message
  */
function writeMessage(discussionId, content) {
  ajaxRequest('POST', '../api/messages', 'callBackWriteMessage', undefined, 'json', { discussion : discussionId, content : content });
}

/**
  * Ecrit le message de bienvenue dans une discussion (id utilisateur dans la session)
  *
  * @param {Number} discussionId - L'id de la discussion
  */
function writeFirstMessage(discussionId) {
  ajaxRequest('POST', '../api/messages', 'callBackWriteFirstMessage', undefined, 'json', { discussion : discussionId, content : "Nouvelle discussion créée" });
}

/**
  * Ajoute un membre à la discussion
  *
  * @param {Number} discussionId - L'id de la discussion
  * @param {Number} userId - L'id de l'utilisateur à ajouter à cette discussion
  */
function addMember(discussionId, userId) {
  ajaxRequest('PUT', '../api/discussions?id='+discussionId+'&user='+userId, 'callBackAddMember');
}

/**
  * Créer une nouvelle discussion
  *
  * @param {String} name - Le nom de la nouvelle discussion
  */
function createDiscussion(name) {
  ajaxRequest('POST', '../api/discussions', 'callBackCreateDiscussion', undefined, 'json', { name : name });
}

/**
  * Quitte une discussion
  *
  * @param {Number} id - L'id de la discussion à quitter
  */
function leaveDiscussion(id) {
  ajaxRequest('DELETE', '../api/discussions?id='+id, 'callBackLeaveDiscussion');
}

/**
  * Cherche un membre
  *
  * @param {String} search - Le champs de recherche
  */
function searchForMembre(search) {
  ajaxRequest('GET', '../api/recover?firstname='+search+'&lastname='+search+'&email='+search, 'callBackSearchForMember');
}

/**
  * Ajoute un fichier à la discussion
  *
  * @param {Object} file - Le fichier à uploader
  */
function uploadFileToDiscussion(file, dId) {
  $.ajax({
    type : 'POST',
    contentType : 'multipart/form-data',
    processData: false, // obligatoire pour de l'upload
    url : '../api/files',
    dataType : 'json',
    data : { dest : 'discussions', folder : dId, file : file },
    success : function(data, textStatus, jqXHR) {
      console.log("success");
    },
    error : function(jqXHR, textStatus, errorThrown) {
      console.log(jqXHR);
    }
  });
}

//-----------------------------------------------
// Pour page de profil : Récupérer amis (dans la partie Navbar) et informations

/**
  * Retourne les informations d'un utilisateur
  *
  * @param {Number} userId - L'id de l'utilisateur
  */
function getSubscribersProfile(userId) {
  ajaxRequest('GET', '../api/profiles', 'callBackGetSubscribersProfile', undefined, 'json', { id : userId });
}

/**
  * Met à jour les informations de l'utilisateur courant
  *
  * @param {String} description - La description de l'utilisateur
  * @param {String} email - Le nouveau mail de l'utilisateur
  * @param {String} phone - Le téléphone de l'utilisateur
  */
function modifySubscribersProfile(description, email, phone) {
  ajaxRequest('PUT', '../api/profiles?description='+description+'&email='+email+'&phone='+phone, 'callBackModifySubscribersProfile');
}

/**
  * Change le mot de passe de l'utilisateur
  *
  * @param {String} password - Le nouveau mot de passe de l'utilisateur
  */
function modifyPassword(password) {
  ajaxRequest('PUT', '../api/authentication?password='+password, 'callBackmodifyPassword');
}

/**
  * Retourne le status de l'amitié
  *
  * @param {Number} userId - L'id de l'utilisateur
  */
function getFriendStatus(userId) {
  ajaxRequest('GET', '../api/friends', 'callBackCheckFriendStatus', undefined, 'json', { id : userId });
}

/**
  * Valide une demande d'amitié
  *
  * @param {Number} userId - L'id de l'utilisateur
  */
function valideFriendRequest(userId) {
  ajaxRequest('PUT', '../api/friends?friend='+userId, 'callBackValideFriendRequest');
}

/**
  * Créer une demande d'amitié
  *
  * @param {Number} userId - L'id de l'utilisateur
  */
function addFriendRequest(userId) {
  ajaxRequest('POST', '../api/friends', 'callBackAddFriendRequest', undefined, 'json', { friend : userId });
}

/**
  * Supprime un lien d'amitié
  *
  * @param {Number} userId - L'id de l'utilisateur
  */
function removeFriend(userId) {
  ajaxRequest('DELETE', '../api/friends?friend='+userId, 'callBackRemoveFriend');
}

/**
  * Change la photo de profil
  *
  * @param {Object} file - Le fichier à uploader
  */
function uploadFileToProfile(data, uId) {
  console.log(data);
  $.ajax({
    type : 'POST',
    contentType : false,
    cache: false,
    processData: false, // obligatoire pour de l'upload
    url : '../api/files',
    dataType : 'json',
    data : { data : data },
    success : function(data, textStatus, jqXHR) {
      console.log(data);
    },
    error : function(jqXHR, textStatus, errorThrown) {
      console.log(jqXHR);
    }
  });
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
