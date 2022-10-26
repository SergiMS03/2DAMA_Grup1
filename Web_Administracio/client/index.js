var app = new Vue({
    el: "#app",
    vuetify: new Vuetify(),
    data:{
        credentials: {email:"", pwd:""}
    },
    methods: {
    logIn: function(){
        this.credentials.email = document.getElementById('email').value;
        this.credentials.pwd = document.getElementById('pwd').value;
        console.log(this.credentials.email);
        console.log(this.credentials.pwd);
        fetch("http://192.168.207.154:3000/logInAdmin", {
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