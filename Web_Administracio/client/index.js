var app = new Vue({
    el: "#app",
    vuetify: new Vuetify(),
    data:{
        credentials: {email:"", pwd:""},
        rol: 'admin',//CAMBIAR ESTO, SOLO PARA TRBAJAR MÁS COMODO
        mode: 0,
        users: [],
        headers: [
        {
            text: 'ID Usuari',
            align: 'start',
            sortable: true,
            value: 'id_usuari',
        },
        { text: 'nom', value: 'nom' },
        { text: 'cognoms', value: 'cognoms' },
        { text: 'email', value: 'email' },
        { text: 'tel', value: 'tel' },
        { text: 'rol', value: 'rol' },
        { text: "BAN", value: "controls", sortable: false }
        ],
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
                            this.rol = data.rol
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
                this.users = data;
                console.log(data);
                this.getArtistRequest();
              })
              .catch((error) => {
                console.log(error);
              });
          }
        }}
);