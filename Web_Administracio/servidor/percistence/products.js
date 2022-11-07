/*SELECTS*/
function getAllProducts () {
    return ("SELECT * FROM PRODUCTE");
  };
/*SELECTS*/

/*DELETES*/
function delProduct (product_id) {
  return ("DELETE FROM PRODUCTE WHERE id_producte= "+ product_id);
};
/*DELETES*/
  module.exports = {getAllProducts, delProduct};