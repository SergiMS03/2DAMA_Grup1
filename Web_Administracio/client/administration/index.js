var app = new Vue({
    el: "#app",
    vuetify: new Vuetify(),
    data:{
        users: []
    },
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
 