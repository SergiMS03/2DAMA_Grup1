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
var chatTools = require("./percistence/chats.js");
var missatgeTools = require("./percistence/messages.js");
var conexion = require("./percistence/bdConnection.js");

app.use(bp.json())
app.use(bp.urlencoded({ extended: true }))

/* app.use(session({
    secret: "TermoTanqueDeÑoquis",
    resave: true,
    saveUninitialized: true,
    cookie:{
        rol: ''
    }
})); */

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
    console.log("uploadfile")
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
        console.log("Canvian path");
        if (err){
            console.log(err)
        }else{
            linkFile = ""
            if(file.path.includes("\\")){//Si el path es de Windows cambia la contrabarra per barra
            var aaa = file.path.split('\\');
            linkFile = aaa[0] + "/" + aaa[1];
            }else{//Path Linux
                linkFile = file.path;
            }
            console.log(linkFile);
            con.query(productTools.updatePathImage(linkFile), (err) => {
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
                    if(result[i].email == req.params.email){
                        if(result[i].pwd == req.params.pwd){
                            auth = true
                            arrRes.id_usuari = (result[i].id_usuari);
                            arrRes.nom = (result[i].nom);
                            arrRes.cognoms = (result[i].cognoms);
                            arrRes.email = (result[i].email);
                            arrRes.rol = (result[i].rol);
                            req.session.cookie.rol = result[i].rol;
                            arrRes.descripcio = (result[i].descripcio);
                            arrRes.tel = (result[i].tel);
                        }
                    }
                }
                res.send(arrRes);
                con.end();
            });            
        }
    });
});

app.get("/signUp/:nom/:cognoms/:email/:pwd/:descripcio/:tel/:artist_req/:ubiLat/:ubiLong", (req, res) => {
    console.log("signUp");
    con = conexion.getCon();
    con.connect(function(err){
        if (err){
            console.log(err)
        }else{
            con.query(userTools.insertClient(req.params.nom, req.params.cognoms, req.params.email, req.params.pwd, 
                req.params.descripcio, req.params.tel, req.params.artist_req, req.params.ubiLat, req.params.ubiLong), (err) => {
                if(err){
                    console.log(err);
                    res.json(false)
                }
                res.send('0');
                con.end();
             });   
        }
    });
});

app.get("/uploadProduct/:product_name/:price/:stock/:descripcio/:user", (req, res) => {
    console.log("uploadProduct");
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

app.get("/sendMessage/:id_chat/:id_emisor/:missatge", (req, res) => {
    console.log("sendMessage");
    con = conexion.getCon();
    con.connect(function(err){
        if (err){
            console.log(err)
        }else{
            console.log("Enviant Missatge")
            con.query(missatgeTools.insertMessage(req.params.id_chat, req.params.id_emisor, req.params.missatge), 
            (err) => {
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

app.get("/getChat/:id_usuari", (req, res) => {
    var arrRes = [];
    console.log("getChats");
    con = conexion.getCon();
    con.connect(function(err){
        if (err){
            res.send(false);
        }else{
            con.query(chatTools.getChats(req.params.id_usuari) , (err, result, fields)=> {
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

app.get("/getMissatge/:id_chat", (req, res) => {
    var arrRes = [];
    console.log("getMissatge");
    con = conexion.getCon();
    con.connect(function(err){
        if (err){
            res.send(false);
        }else{
            con.query(missatgeTools.getMissatgeChat(req.params.id_chat) , (err, result, fields)=> {
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

app.get("/getProduct/:id_producte", (req, res) => {
    console.log("getProduct");
    var queryResult;
    con = conexion.getCon();
    con.connect(function(err){
        if (err){
            res.json(false);
        }else{
            con.query(productTools.getProduct(req.params.id_producte) , (err, result, fields)=> {
                queryResult = result
                res.json(queryResult);
                con.end();
            });           
        }
    });

});

app.get("/createChat/:id_comprador/:id_venededor/:id_producto", (req, res) => {
    console.log("createChat");
    con = conexion.getCon();
    con.connect(function(err){
        if (err){
            console.log(err);
            res.send('1');
        }else{
            con.query(chatTools.createChat(req.params.id_comprador, req.params.id_venededor, req.params.id_producto) , (err)=> {
                if(err){
                    console.log(err);
                    res.send('1');
                }
                else{
                    res.send('0');
                }
                con.end();
            });           
        }
    });
});

app.get("/artistReqFromProfile/:usuari_id", (req, res) => {
    console.log("artistReqFromProfile");
    con = conexion.getCon();
    con.connect(function(err){
        if (err){
            console.log(err);
            res.send('1');
        }else{
            con.query(userTools.artistReqFromProfile(req.params.usuari_id) , (err)=> {
                if(err){
                    console.log(err);
                    res.send('1');
                }
                else{
                    res.send('0');
                }
                con.end();
            });           
        }
    });
});

/********   POST  **********/



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
    console.log("delUser");
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

app.post("/banUser", (req, res) => {
    console.log("banUser")
    con = conexion.getCon();
    con.connect(function(err){
        if (err){
            console.log(err);
        }else{
                con.query(userTools.banUser(req.body.id_usuari) , (err, fields)=> {
                    if(err){
                        console.log(err);
                        res.json(false);
                    }
                    res.json(true);  
                }); 
            
            con.end();       
        }
    });

});

app.post("/getProducts", (req, res) => {
    console.log("getProducts");
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
                con.end();
            });           
        }
    });

});



app.post("/delProduct", (req, res) => {
    console.log("delProduct")
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




