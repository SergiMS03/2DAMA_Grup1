/*SELECTS*/
function getAllUsers () {
  return ("SELECT * FROM USUARI WHERE ban= 0");
};

function getUser (usuari_id) {
  return ("SELECT * FROM USUARI WHERE id = "+ usuari_id);
};

function getAdmins () {
  return ("SELECT * FROM USUARI WHERE rol = 'admin'");
};

function getArtistReq () {
  return ("SELECT * FROM USUARI WHERE artist_req = 1");
};
/*SELECTS*/

/*DELETES*/
function delUser (usuari_id) {
  return ("DELETE FROM USUARI WHERE id_usuari = "+ usuari_id);
};
/*DELETES*/

/*INSERTS*/
function insertClient (nom, cognoms, email, pwd, descripcio, tel, artistRequest, ubiLat, ubiLong) {
  console.log(artistRequest)
  let artist_req = 0;
  if(artistRequest == 'true'){
    artist_req = 1;
  }
  else{
    artist_req = 0;
  }
  console.log(artist_req)
  return ("INSERT INTO USUARI VALUES (NULL,'"+ nom + "', '" + cognoms + "', '" + email + "', '"
  + pwd + "', 'client', '" + descripcio +"', '"+ tel +"', '"+ artist_req +"', 0 ,"+ ubiLat +", "+ ubiLong +")");
};
/*INSERTS*/

/* UPDATES */

function artistReqFromProfile (usuari_id) {
  return ("UPDATE USUARI SET artist_req = 1 WHERE id_usuari = "+ usuari_id);
};

function isArtist (usuari_id) {
  return ("UPDATE USUARI SET rol = 'artist', artist_req = false WHERE id_usuari = "+ usuari_id);
};

function declineArtist (usuari_id) {
  return ("UPDATE USUARI SET artist_req = false WHERE id_usuari = "+ usuari_id);
};

function banUser (usuari_id) {
  return ("UPDATE USUARI SET ban = true WHERE id_usuari = "+ usuari_id);
};
/* UPDATES */

module.exports = {getAllUsers, getUser, delUser, getAdmins, insertClient, getArtistReq, isArtist, declineArtist, banUser, artistReqFromProfile};