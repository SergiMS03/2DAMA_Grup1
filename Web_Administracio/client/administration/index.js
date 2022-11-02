var app = new Vue({
  el: "#app",
  vuetify: new Vuetify(),
  data: {
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
    getUsers: function () {
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
          this.users = data;
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
        })
        .catch((error) => {
          console.log(error);
        });
    },
  },
});
