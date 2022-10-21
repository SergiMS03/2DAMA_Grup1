const express = require("express");
const session = require('express-session');
const app = express();
var mysql = require("mysql2");
const cors = require('cors');
const PORT = 3000;

var con = mysql.createConnection({
    host:"labs.inspedralbes.cat",
    user:"a19sermelseg_user",
    password:"Ausias1234",
    database:"a19sermelseg_quizs_acces"
});

app.use(cors({
    origin: function(origin, callback){
        return callback(null, true);
    }
}));

app.post("/", (req, res) => {
    
});

app.listen(PORT, () =>{
    console.log("Servidor arrancat pel port "+ PORT);
});