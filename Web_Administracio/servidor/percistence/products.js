/*SELECTS*/
function getAllProducts () {
    return ("SELECT * FROM PRODUCTE");
  };
/*SELECTS*/

/*INSERTS*/
function insertProduct (nom_producte, preu, stock, descripcio){
  return ("INSERT INTO PRODUCTE VALUES (NULL,'"+ nom_producte + "', '" + preu + "', '" + stock + "', '"
  + descripcio + "', '"+ null +"', 4)");
}
/*INSERTS*/

/*DELETES*/
function delProduct (product_id) {
  return ("DELETE FROM PRODUCTE WHERE id_producte= "+ product_id);
};
/*DELETES*/

/*UPDATE*/
function updatePathImage(path_img){
  return ("UPDATE PRODUCTE SET path_img="+ path_img +"WHERE id_producte = MAX(id_producte)")
}
/*UPDATE*/
  module.exports = {getAllProducts, delProduct, insertProduct, updatePathImage};

