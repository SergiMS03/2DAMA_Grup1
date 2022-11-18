/*SELECTS*/
function getMissatges (producte, venedor, comprador) {
  return ("SELECT emisor, missatge FROM MISSATGES WHERE missatge_producte = "+ producte +"AND usuari_venedor = "+ venedor +"AND usuari_comprador ="+ comprador);
};
/*SELECTS*/

/*DELETES*/
module.exports = {getMissatges};