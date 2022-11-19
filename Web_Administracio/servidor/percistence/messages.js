/*SELECT*/
function getMissatgeChat (id_chat) {
    return ("SELECT * FROM MISSATGES WHERE id_chat = "+ id_chat);
  };
/*SELECT*/

/*INSERT*/
function insertMessage (id_chat, id_emisor, missatge){
  return ("INSERT INTO MISSATGES VALUES (NULL,"+ id_chat + " ," + id_emisor + " , '" + missatge + "')");
}

/*INSERT*/

  module.exports = {getMissatgeChat, insertMessage};