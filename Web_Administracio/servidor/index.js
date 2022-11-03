const express = require("express");
const bp = require('body-parser')
const session = require('express-session');
const app = express();
const cors = require('cors');
const PORT = 3000;
var userTools = require("./percistence/users.js");
var conexion = require("./percistence/bdConnection.js");

app.use(bp.json())
app.use(bp.urlencoded({ extended: true }))

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

/*NO TOCAR AUN*//*NO TOCAR AUN*//*NO TOCAR AUN*//*NO TOCAR AUN*//*NO TOCAR AUN*//*NO TOCAR AUN*//*NO TOCAR AUN*/
app.get("/logInClient/:email/:pwd", (req, res) => {
    var auth = 1;
    console.log("LOGIN CLIENT INICIAT!!!");
    let email = req.params.email;
    let pwd = req.params.pwd;
    con = conexion.getCon();
    con.connect(function(err){
        if (err){
            res.json(false);
        }else{
            con.query(userTools.getAllUsers(), (err, result, fields)=> {
                if(err){
                    res.send(false);
                }
                for (let i = 0; i < result.length && auth == 1; i++) {
                    if(result[i].email == email){
                        if(result[i].pwd == pwd){
                            auth = 0;
                        }
                    }
                }
                res.send(JSON.stringify(auth));
                console.log(auth);
                con.end();
            });            
        }
    });
});
/*NO TOCAR AUN*//*NO TOCAR AUN*//*NO TOCAR AUN*//*NO TOCAR AUN*//*NO TOCAR AUN*//*NO TOCAR AUN*//*NO TOCAR AUN*/

app.get("/signUp/:nom/:cognoms/:email/:pwd/:descripcio/:tel/:solicitar_artista", (req, res) => {
    console.log("Conexió realitzada");
    con = conexion.getCon();
    con.connect(function(err){
        if (err){
            console.log(err)
        }else{
            con.query(userTools.insertClient(req.params.nom, req.params.cognoms, req.params.email, req.params.pwd, req.params.descripcio, req.params.tel), (err) => {
                if(err){
                    console.log(err);
                    res.json(false)
                }
                console.log("Succesfull");
                res.send('0');
                con.end();
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
    con = conexion.getCon();
    con.connect(function(err){
        if (err){
            res.json(false);
        }else{
            con.query(userTools.getAdmins(), (err, result, fields)=> {
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
                con.end();
            });            
        }
    });
    
});


app.post("/delUser", (req, res) => {
    console.log(req.body.id_usuari);
    /*if(req.session.cookie.rol == 'admin'){*/
        con = conexion.getCon();
        con.connect(function(err){
            if (err){
                console.log(err)
            }else{
                con.query(userTools.delUser(req.body.id_usuari), (err) => {
                    if(err){
                        console.log(err);
                        res.json(false)
                    }
                    res.json(true);
                    con.end();
                });   
            }
        });
    /*}else{
        res.json(false);
    }*/
});

app.post("/getUsers", (req, res) => {
        var arrRes = [];
        con = conexion.getCon();
        con.connect(function(err){
            if (err){
                res.json(false);
            }else{
                con.query(userTools.getAllUsers() , (err, result, fields)=> {
                    if(err){
                        res.json(false);
                    }
                    for (let i = 0; i < result.length; i++) {
                        arrRes.push(result[i]);
                    }
                    res.json(arrRes);
                    con.end();
                });           
            }
        });
    
 });
 