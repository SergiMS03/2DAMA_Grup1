var app = new Vue({
    el: "#app",
    vuetify: new Vuetify(),
    data:{
        users: []
    },
    /*credentials:{ {email:"", pwd:""},
                items: [
                  { title: 'Usuarios', icon: 'mdi-view-dashboard' },
                  { title: 'Solicitudes de Artista', icon: 'mdi-image' },
                  { title: 'Productos', icon: 'mdi-help-box' },
                ],
                right: null,
              
            
          },
          */
    methods: {
    getUsers: function(){
        const myHeaders = new Headers();
        fetch("http://localhost:3000/getUsers", {
                    method:"POST",
                    headers: myHeaders,
                    mode: 'cors',
                    cache: 'default'
                }).then(
                    (response) =>
                        response.json()
                ).then((data) => {
                    this.users = data
                }
                ).catch((error) => {
                    console.log(error)
                });
        }}
 });
 