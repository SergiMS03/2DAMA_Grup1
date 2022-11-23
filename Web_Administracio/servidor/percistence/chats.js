/*SELECTS*/
function getChats (id_usuari) {
  return ("SELECT C.id_chat, C.id_venedor, C.id_comprador, C.id_producte, P.nom_producte, P.path_img FROM CHATS C JOIN PRODUCTE P USING(id_producte) WHERE id_venedor = "+ id_usuari +" OR id_comprador = "+ id_usuari);
};
/*SELECTS*/

/*INSERT*/
function createChat (comprador, venedor, producto) {
  return ("INSERT INTO CHATS VALUE (NULL, "+ venedor +", "+ comprador+", "+ producto+")");
};
/*INSERT*/
module.exports = {getChats, createChat};