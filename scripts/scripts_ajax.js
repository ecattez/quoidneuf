function login() {
  var username = document.getElementsByName('connection_login')[0].value;
  var password = document.getElementsByName('connection_password')[0].value;
  var json = JSON.stringify({
    'username' : username,
    'password' : password
  });
  console.log("Log : " + username);
  console.log("Pass : " +password);
  console.log("json : " + json)
  $.ajax({
		type : 'POST',
		contentType : 'application/json',
		url : '/quoidneuf/api/authentication',
		dataType : "json",
		data : json,
		success : function(data, textStatus, jqXHR) {
			console.log(data);
		},
		error : function(jqXHR, textStatus, errorThrown) {
			console.log('error: ' + textStatus);
		}
	});
}
/*
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
*/
