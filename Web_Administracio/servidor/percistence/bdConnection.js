var mysql = require("mysql2");
function getCon(){
    var con = mysql.createConnection({
        host:"labs.inspedralbes.cat",
        user:"a19sermelseg_user",
        password:"Ausias1234",
        database:"a19sermelseg_plastic_precios"
    });
    return con;
};

module.exports = {getCon};