var app = new Vue({
    el: "#app",
    vuetify: new Vuetify(),
    data:{
        credentials: {email:"", pwd:""},
        admin:[],
        rol: '',
        mode: 0,
        users: [],
        headers:[],
        drawer: false
    },
    methods: {
    logIn: function(){
        this.credentials.email = document.getElementById('email').value;
        this.credentials.pwd = document.getElementById('pwd').value;
        console.log(this.credentials.email);
        console.log(this.credentials.pwd);
        fetch("http://localhost:3000/logInAdmin", {
                    method:"POST",
                    headers: {
                        'Content-Type' : 'application/json',
                        'Accept':'application/json'
                    },
                    mode: 'cors',
                    cache: 'default',
                    body: JSON.stringify(this.credentials)
                }).then(
                    (response) =>
                        response.json()
                ).then((data) => {
                        if(data.rol == 'admin'){
                            this.admin = data
                            this.getUsers();
                        }
                    }
                ).catch((error) => {
                    console.log(error)
                });
        },
        getUsers: function () {
            this.mode = 0;
            const myHeaders = new Headers();
            fetch("http://localhost:3000/getUsers", {
              method: "POST",
              headers: myHeaders,
              mode: "cors",
              cache: "default",
            })
              .then((response) => response.json())
              .then((data) => {
                this.users = data;
                this.headers = [
                    {
                        text: 'ID Usuari',
                        align: 'center',
                        sortable: true,
                        value: 'id_usuari',
                    },
                    { text: 'nom', value: 'nom', align: 'center'},
                    { text: 'cognoms', value: 'cognoms', align: 'center' },
                    { text: 'email', value: 'email', align: 'center' },
                    { text: 'tel', value: 'tel', align: 'center' },
                    { text: 'rol', value: 'rol', align: 'center' },
                    { text: "ELIMINA", value: "controls", sortable: false, align: 'center' },
                    { text: "BAN", value: "controls", sortable: false, align: 'center' }
                    ],
                console.log(data);
                console.log(this.mode);
              })
              .catch((error) => {
                console.log(error);
              });
          },
          delUser: function (id_usuari) {
            fetch("http://localhost:3000/delUser", {
              method:"POST",
                          headers: {
                              'Content-Type' : 'application/json',
                              'Accept':'application/json'
                          },
                          mode: 'cors',
                          cache: 'default',
              body: JSON.stringify({ id_usuari: id_usuari }),
            })
              .then((response) => response.json())
              .then((data) => {
                console.log(data);
                this.getUsers();
              })
              .catch((error) => {
                console.log(error);
              });
          },
          banUser: function (id_usuari, reply){
            fetch("http://localhost:3000/banUser", {
              method:"POST",
                headers: {
                    'Content-Type' : 'application/json',
                    'Accept':'application/json'
                },
                mode: 'cors',
                cache: 'default',
                body: JSON.stringify({ id_usuari: id_usuari})
            })
              .then((response) => response.json())
              .then((data) => {
                console.log(data)
                this.getUsers();
              })
              .catch((error) => {
                console.log(error);
              });
          },
          getArtistRequest: function () {
            this.mode = 1;
            fetch("http://localhost:3000/artistRequest", {
              method:"POST",
                          headers: {
                              'Content-Type' : 'application/json',
                              'Accept':'application/json'
                          },
                          mode: 'cors',
                          cache: 'default',
            })
              .then((response) => response.json())
              .then((data) => {
                this.users = data;
                this.headers = [
                    {
                        text: 'ID Usuari',
                        align: 'center',
                        sortable: true,
                        value: 'id_usuari',
                    },
                    { text: 'nom', value: 'nom', align: 'center'},
                    { text: 'cognoms', value: 'cognoms', align: 'center' },
                    { text: 'email', value: 'email', align: 'center' },
                    { text: 'tel', value: 'tel', align: 'center' },
                    { text: 'rol', value: 'rol', align: 'center' },
                    { text: "PETITION", value: "controls", sortable: false, align: 'center' }
                    ],
                console.log(data);
              })
              .catch((error) => {
                console.log(error);
              });
          },
          acceptDeclineArtist: function (id_usuari, reply){
            fetch("http://localhost:3000/manageArtist", {
              method:"POST",
                headers: {
                    'Content-Type' : 'application/json',
                    'Accept':'application/json'
                },
                mode: 'cors',
                cache: 'default',
                body: JSON.stringify({reply: reply, id_usuari: id_usuari})
            })
              .then((response) => response.json())
              .then((data) => {
                console.log(data)
                this.getArtistRequest();
              })
              .catch((error) => {
                console.log(error);
              });
          },
          
          getProducts: function () {
            this.mode = 2;
            const myHeaders = new Headers();
            fetch("http://localhost:3000/getProducts", {
              method: "POST",
              headers: myHeaders,
              mode: "cors",
              cache: "default",
            })
              .then((response) => response.json())
              .then((data) => {
                this.users = data;
                this.headers = [
                    {
                        text: 'ID Producte',
                        align: 'center',
                        sortable: true,
                        value: 'id_producte',
                    },
                    { text: 'nom', value: 'nom', align: 'center'},
                    { text: 'preu', value: 'preu', align: 'center' },
                    { text: 'stock', value: 'stock', align: 'center' },
                    { text: 'path IMG', value: 'path IMG', align: 'center' },
                    { text: 'Venedor ID', value: 'Venedor ID', align: 'center' },
                    { text: "ELIMINA", value: "controls", sortable: false, align: 'center' }
                    ],
                console.log(data);
                console.log(this.mode);
              })
              .catch((error) => {
                console.log(error);
              });
          },
          delProduct: function (id_producte) {
            fetch("http://localhost:3000/delProduct", {
              method:"POST",
                          headers: {
                              'Content-Type' : 'application/json',
                              'Accept':'application/json'
                          },
                          mode: 'cors',
                          cache: 'default',
              body: JSON.stringify({ id_producte: id_producte }),
            })
              .then((response) => response.json())
              .then((data) => {
                console.log(data);
                this.getProducts();
              })
              .catch((error) => {
                console.log(error);
              });
          }
        }}
);