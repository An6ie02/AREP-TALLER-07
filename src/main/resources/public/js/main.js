function login() {

    var username = document.getElementById("username").value;
    var password = document.getElementById("password").value;

    var data = {
        username: username,
        password: password
    };

    fetch("/login", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(data)
    })
    .then(response => {
        if (!response.ok) {
            throw new Error("Credenciales incorrectas");
        }
        return response.json();
    })
    .then(data => {
        window.location.href = "calculator.html";
    })
    .catch(error => {
        alert(error.message);
        console.error("Error:", error);
    });
}

function loadGetMsg1() {
    let number = document.getElementById("valueSen").value;
    const xhttp = new XMLHttpRequest();
    xhttp.onload = function () {
        document.getElementById("getrespmsg1").innerHTML = this.responseText;
    }
    xhttp.open("GET", "/sin?value=" + number);
    xhttp.send();
}

function loadGetMsg2() {
    let number = document.getElementById("valueCos").value;
    const xhttp = new XMLHttpRequest();
    xhttp.onload = function () {
        document.getElementById("getrespmsg2").innerHTML = this.responseText;
    }
    xhttp.open("GET", "/cos?value=" + number);
    xhttp.send();
}