var app = new Vue({
    el: "#app",
    vuetify: new Vuetify(),
    data:{
       // credentials: {email:"", pwd:""}
    },
    methods: {
    logIn: function(){
        const myHeaders = new Headers();
        fetch("http://localhost:3000/logInAdmin", {
                    method:"POST",
                    headers: myHeaders,
                    mode: 'cors',
                    cache: 'default'

                }).then(
                    (response) =>
                        response.json()
                ).then((data) => {
                        console.log(data.rol);
                        if(data.rol == 'admin'){
                            window.location.href = "administration/index.html";
                        }
                    }
                ).catch((error) => {
                    console.log(error)
                });
        }}
});