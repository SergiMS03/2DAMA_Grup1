/*SELECT*/
function getMissatgeChat (id_chat) {
    return ("SELECT * FROM MISSATGES WHERE id_chat = "+ id_chat);
  };
  /*SELECT*/
  module.exports = {getMissatgeChat};