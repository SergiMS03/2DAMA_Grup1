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

app.use(session({
    secret: "TermoTanqueDeÑoquis",
    resave: true,
    saveUninitialized: true,
    cookie:{
        rol: ''
    }
}));

app.use(cors({
    origin: function(origin, callback){
        return callback(null, true);
    }
}));



app.listen(PORT, () =>{
    console.log("Servidor arrancat pel port "+ PORT);
});


app.get("/logInClient/:email/:pwd", (req, res) => {
    var auth = false;
    var arrRes = {};
    let email = req.params.email;
    let pwd = req.params.pwd;
    con = getCon();
    con.connect(function(err){
        if (err){
            res.json(false);
        }else{
            con.query("SELECT * FROM USUARI", (err, result, fields)=> {
                if(err){
                    res.send(false);
                }
                for (let i = 0; i < result.length && !auth; i++) {
                    if(result[i].email == email){
                        if(result[i].pwd == pwd){
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
                res.send(arrRes);
                con.end;
            });            
        }
    });
});

app.get("/signUp/:nom/:cognoms/:email/:pwd/:descripcio/:tel/:solicitar_artista", (req, res) => {
    var success = false;
    console.log("Conexió realitzada");
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
                console.log("Succesfull");
                con.end();
                res.send(true);
             });   
        }
    });
    if(req.params.solicitar_artista){
        //EN CAS D'HABER SOLICITAT ARTISTA ENTRARIA A UNA FUNCIO PER SOLICITAR-HO
    }
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
                            req.session.cookie.rol = result[i].rol;
                            console.log(req.session.cookie.rol);
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


app.post("/delUser", (req, res) => {
    console.log(req.body.id_usuari);
    /*if(req.session.cookie.rol == 'admin'){*/
        con = getCon();
        con.connect(function(err){
            if (err){
                console.log(err)
            }else{
                con.query("DELETE FROM USUARI WHERE id_usuari = "+ req.body.id_usuari, (err) => {
                    if(err){
                        console.log(err);
                        res.json(false)
                    }
                    con.end();
                    res.json(true);
                });   
            }
        });
    /*}else{
        res.json(false);
    }*/
});

app.post("/getUsers", (req, res) => {
    console.log("Entra");
        var arrRes = [];
        con = getCon();
        con.connect(function(err){
            if (err){
                res.json(false);
            }else{
                con.query("SELECT * FROM USUARI", (err, result, fields)=> {
                    if(err){
                        res.json(false);
                    }
                    for (let i = 0; i < result.length; i++) {
                        arrRes.push(result[i]);
                    }
                    res.json(arrRes);
                    con.end;
                });           
            }
        });
    
 });
 