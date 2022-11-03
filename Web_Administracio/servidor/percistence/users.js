/*SELECTS*/
function getAllUsers () {
  return ("SELECT * FROM USUARI");
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
function insertClient (nom, cognoms, email, pwd, descripcio, tel) {
  return ("INSERT INTO USUARI VALUES (NULL,'"+ nom + "', '" + cognoms + "', '" + email + "', '"
  + pwd + "', 'client', '" + descripcio +"', '"+ tel +"', '"+ artist_req +"')");//PROVAR EL ARTIST_REQ
};
/*INSERTS*/

module.exports = {getAllUsers, getUser, delUser, getAdmins, insertClient, getArtistReq};