const url = 'http://localhost:4001/myLogin';
const output = document.getElementById('output');

const userNameInput = document.getElementById('userNameInput');
const passwordInput = document.getElementById('passwordInput');
const submitLogin = document.getElementById('submitLogin');

const emptyFormUserName = document.getElementById("emptyFormUserName");
const emptyFormPassword = document.getElementById("emptyFormPassword");

const signIn = document.getElementById('signIn');


const logIn = async(event) => {

    // checking for empty inputs
    if (!checkEmptyForm()) {
        return;
    }

    const queryParams = '/login';
    const endpoint = url + queryParams;
    const data = {
        userName: userNameInput.value,
        password: passwordInput.value
    }

    try {
        const response = await fetch(endpoint, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });

        const jsonResponse = await response.json();
        
        if (response.ok) {
            output.innerHTML += 'Login succeeded';
        } else {
            output.innerHTML += jsonResponse.message;
        }
    } catch(error) {
        output.innerHTML += 'Error:' + error;
    }

}

const checkEmptyForm = () => {
    if (userNameInput.value == '') {
        return false;
    }
    if (passwordInput.value == '') {
        return false;
    }
    return true;
}

submitLogin.onclick = logIn;