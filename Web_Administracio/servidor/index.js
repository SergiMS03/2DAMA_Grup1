const express = require("express");
const bp = require('body-parser')
const session = require('express-session');
const app = express();
const cors = require('cors');
const PORT = 3000;
const fs = require('fs')
const multer = require('multer');

const IMAGE_UPLOAD_DIR = "./image"

var userTools = require("./percistence/users.js");
var productTools = require("./percistence/products.js");
var missatgeTools = require("./percistence/missatges.js");
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

/////////////////////////////////

var storage = multer.diskStorage({
    destination: function (req, file, cb) {
      cb(null, './image')
    },
    filename: function (req, file, cb) {
      cb(null, file.fieldname + '-' + Date.now()+'.jpg')
    }
})

var upload = multer({ storage: storage })

// Upload Single File
app.post('/uploadfile', upload.single('myFile'), (req, res, next) => {
    console.log("hola")
    const file = req.file
    if (!file) {
        const error = new Error('Please upload a file')
        error.httpStatusCode = 400
        console.log("error", 'Please upload a file');
        
        res.send({code:500, msg:'Please upload a file'})
        return next({code:500, msg:error})

    }
    con = conexion.getCon();
    con.connect(function(err){
        if (err){
            console.log(err)
        }else{
            console.log("Canvian path");
            console.log(file.path);
            con.query(productTools.updatePathImage(file.path), (err) => {
                if(err){
                    console.log(err);
                    res.json(false)
                }
                console.log("Succesfull");
                res.json('0');
                con.end();
             });   
        }
    });
    //res.send({code:200, msg:file})
})
//Uploading multiple files
app.post('/uploadmultiple', upload.array('myFiles', 12), (req, res, next) => {
const files = req.files
if (!files) {
    const error = new Error('Please choose files')
    error.httpStatusCode = 400
    return next(error)
}

res.send(files)

})

////////////////////////////

app.listen(PORT, () =>{
    console.log("Servidor arrancat pel port "+ PORT);
});

app.get("/logInClient/:email/:pwd", (req, res) => {
    var auth = false;
    var arrRes = {};
    con = conexion.getCon();
    con.connect(function(err){
        if (err){
            res.json(false);
        }else{
            con.query(userTools.getAllUsers(), (err, result, fields)=> {
                if(err){
                    res.json(false);
                }
                for (let i = 0; i < result.length && !auth; i++) {
                    console.log(result[i].email == req.params.email);
                    console.log(result[i].pwd == req.params.pwd);
                    if(result[i].email == req.params.email){
                        if(result[i].pwd == req.params.pwd){
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
                res.send(arrRes);
                console.log(arrRes);
                con.end();
            });            
        }
    });
});

app.get("/signUp/:nom/:cognoms/:email/:pwd/:descripcio/:tel/:artist_req/:ubiLat/:ubiLong", (req, res) => {
    console.log("Conexió realitzada");
    con = conexion.getCon();
    con.connect(function(err){
        if (err){
            console.log(err)
        }else{
            console.log(req.params.nom)
            con.query(userTools.insertClient(req.params.nom, req.params.cognoms, req.params.email, req.params.pwd, 
                req.params.descripcio, req.params.tel, req.params.artist_req, req.params.ubiLat, req.params.ubiLong), (err) => {
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
});

app.get("/uploadProduct/:product_name/:price/:stock/:descripcio/:user", (req, res) => {
    console.log("Conexió realitzada");
    con = conexion.getCon();
    con.connect(function(err){
        if (err){
            console.log(err)
        }else{
            con.query(productTools.insertProduct(req.params.product_name, req.params.price, req.params.stock, 
                req.params.descripcio, req.params.user), (err) => {
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

 app.post("/artistRequest", (req, res) => {
    var arrRes = [];
    con = conexion.getCon();
    con.connect(function(err){
        if (err){
            res.json(false);
        }else{
            con.query(userTools.getArtistReq() , (err, result, fields)=> {
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

app.post("/manageArtist", (req, res) => {
    con = conexion.getCon();
    con.connect(function(err){
        if (err){
            console.log(err);
        }else{
            if(req.body.reply == 0){
                con.query(userTools.isArtist(req.body.id_usuari) , (err, fields)=> {
                    if(err){
                        console.log(err);
                        res.json(false);
                    }
                    res.json(true);  
                }); 
            }else if(req.body.reply == 1){
                con.query(userTools.declineArtist(req.body.id_usuari) , (err, fields)=> {
                    if(err){
                        console.log(err);
                        res.json(false);
                    }
                    res.json(true);  
                }); 
            }
            con.end();       
        }
    });

});

app.post("/getProducts", (req, res) => {
    console.log("INICIAT GETPRODUCTS");
    var arrRes = [];
    con = conexion.getCon();
    con.connect(function(err){
        if (err){
            res.json(false);
        }else{
            con.query(productTools.getAllProducts() , (err, result, fields)=> {
                if(err){
                    res.json(false);
                }
                for (let i = 0; i < result.length; i++) {
                    arrRes.push(result[i]);
                }
                res.json(arrRes);
                console.log(arrRes);
                con.end();
            });           
        }
    });

});

app.get("/getProduct/:id_producte", (req, res) => {
    console.log("INICIAT GETPRODUCT");
    var queryResult;
    con = conexion.getCon();
    con.connect(function(err){
        if (err){
            res.json(false);
        }else{
            con.query(productTools.getProduct(req.params.id_producte) , (err, result, fields)=> {
                queryResult = result
                res.json(queryResult);
                console.log(queryResult);
                con.end();
            });           
        }
    });

});


app.post("/delProduct", (req, res) => {
    con = conexion.getCon();
    con.connect(function(err){
        if (err){
            console.log(err)
        }else{
            con.query(productTools.delProduct(req.body.id_producte), (err) => {
                if(err){
                    console.log(err);
                    res.json(false)
                }
                res.json(true);
                con.end();
            });   
        }
    });
});

app.post("/getMissatges", (req, res) => {
    var arrRes = [];
    con = conexion.getCon();
    con.connect(function(err){
        if (err){
            res.json(false);
        }else{
            con.query(missatgeTools.getMissatges() , (err, result, fields)=> {
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