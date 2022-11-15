/*SELECTS*/
function getAllProducts () {
    return ("SELECT * FROM PRODUCTE");
  };

  function getProduct (product_id) {
    return ("SELECT * FROM PRODUCTE WHERE id_producte = "+ product_id);
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
  return ("UPDATE PRODUCTE SET path_img='"+ path_img +"' WHERE id_producte = (SELECT MAX(id_producte) FROM PRODUCTE)")
}
/*UPDATE*/
  module.exports = {getAllProducts, delProduct, insertProduct, updatePathImage, getProduct};

