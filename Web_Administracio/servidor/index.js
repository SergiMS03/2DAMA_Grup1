const express = require("express");
const bp = require('body-parser')
const session = require('express-session');
const app = express();
var mysql = require("mysql2");
const cors = require('cors');
const PORT = 3000;

app.use(bp.json())
app.use(bp.urlencoded({ extended: true }))

function getCon(){
    var con = mysql.createConnection({
        host:"labs.inspedralbes.cat",
        user:"a19sermelseg_user",
        password:"Ausias1234",
        database:"a19sermelseg_plastic_precios"
    });
    return con;
}

app.use(cors({
    origin: function(origin, callback){
        return callback(null, true);
    }
}));

app.post("/logInClient", (req, res) => {
    var auth = false;
    var arrRes = {};
    con = getCon();
    con.connect(function(err){
        if(err){
            res.json(false);
        }else{
            con.query("SELECT * FROM USUARI", (err, result, fields)=> {
                for (let i = 0; i < result.length && !auth; i++) {
                    if(err){
                        res.json(false);
                    }
                    if(result[i].email == req.body.email){
                        if(result[i].pwd == req.body.pwd){
                            auth = true
                            arrRes.id_usuari = (result[i].id_usuari);
                            arrRes.nom = (result[i].nom);
                            arrRes.cognoms = (result[i].cognoms);
                            arrRes.email = (result[i].email);
                            arrRes.rol = (result[i].rol);
                            arrRes.descripcio = (result[i].descripcio);
                            arrRes.tel = (result[i].tel);
                        }
                    }
                }
                res.json(arrRes);
                con.end;
            });            
        }
    });
    
});

app.post("/logInAdmin", (req, res) => {
    var auth = false;
    var arrRes = {};
    con = getCon();
    con.connect(function(err){
        if (err){
            res.json(false);
        }else{
            con.query("SELECT * FROM USUARI WHERE rol = 'admin'", (err, result, fields)=> {
                if(err){
                    res.json(false);
                }
                for (let i = 0; i < result.length && !auth; i++) {
                    if(result[i].email == req.body.email){
                        if(result[i].pwd == req.body.pwd){
                            auth = true
                            arrRes.id_usuari = (result[i].id_usuari);
                            arrRes.nom = (result[i].nom);
                            arrRes.cognoms = (result[i].cognoms);
                            arrRes.email = (result[i].email);
                            arrRes.rol = (result[i].rol);
                            arrRes.descripcio = (result[i].descripcio);
                            arrRes.tel = (result[i].tel);
                        }
                    }
                }
                res.json(arrRes);
                con.end;
            });            
        }
    });
    
});


app.get("/signUp/:nom/:cognoms/:email/:pwd/:descripcio/:tel/:solicitar_artista", (req, res) => {
    var success = false;
    con = getCon();
    con.connect(function(err){
        if (err){
            console.log(err)
        }else{
            con.query("INSERT INTO USUARI VALUES (NULL,'"+ req.params.nom + "', '" + req.params.cognoms + "', '" + req.params.email + "', '"
             + req.params.pwd + "', 'client', '" + req.params.descripcio +"', '"+ req.params.tel +"')", (err) => {
                if(err){
                    console.log(err);
                    res.json(false)
                }
                con.end();
                res.send(true);
             });   
        }
    });
    if(req.params.solicitar_artista){
        
    }
});


app.listen(PORT, () =>{
    console.log("Servidor arrancat pel port "+ PORT);
});