/*SELECTS*/
function getAllProducts () {
    return ("SELECT * FROM PRODUCTE");
  };
/*SELECTS*/

/*INSERTS*/
function insertProduct (nom_producte, preu, stock, descripcio, path_img){
  return ("INSERT INTO PRODUCTE VALUES (NULL,'"+ nom_producte + "', '" + preu + "', '" + stock + "', '"
  + descripcio + "', '"+ path_img +"', 4)");
}
/*INSERTS*/

/*DELETES*/
function delProduct (product_id) {
  return ("DELETE FROM PRODUCTE WHERE id_producte= "+ product_id);
};
/*DELETES*/
  module.exports = {getAllProducts, delProduct, insertProduct};