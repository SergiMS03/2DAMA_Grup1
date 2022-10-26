var app = new Vue({
    el: "#app",
    vuetify: new Vuetify(),
    data:{
<<<<<<< HEAD
        users: []
    },
=======
        credentials: {email:"", pwd:""},
                items: [
                  { title: 'Usuarios', icon: 'mdi-view-dashboard' },
                  { title: 'Solicitudes de Artista', icon: 'mdi-image' },
                  { title: 'Productos', icon: 'mdi-help-box' },
                ],
                right: null,
              
            
          },
    
    
>>>>>>> fbd739fef7769fee5826b05c8aff6446be4ffb5e
    methods: {
    getUsers: function(){
        fetch("http://localhost:3000/getUsers", {
                    method:"POST",
                    headers: {
                        'Content-Type' : 'application/json',
                        'Accept':'application/json'
                    },
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
 