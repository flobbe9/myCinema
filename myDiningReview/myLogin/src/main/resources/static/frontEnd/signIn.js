const url = 'http://localhost:4001/myLogin';
const output = document.getElementById('output');

const firstNameInput = document.getElementById('firstNameInput');
const lastNameInput = document.getElementById('lastNameInput');
const birthdayInput = document.getElementById('birthdayInput');
const mailInput = document.getElementById('mailInput');
const userNameInput = document.getElementById('userNameInput');
const passwordInput = document.getElementById('passwordInput');
const passwordInputRepeat = document.getElementById('passwordInputRepeat');

const submitSignIn = document.getElementById('submitSignIn');


// setting birthdayInput attribute max and min
const today = new Date();

var day = today.getDate().toString();
if (day.length == 1) {
    day = '0' + day;
}

var month = (today.getMonth() + 1).toString();
if (month.length == 1) {
    month = '0' + month;
}

var year = today.getFullYear().toString();

var minYear = (today.getFullYear() - 120).toString();

birthdayInput.setAttribute('min', minYear + '-01-01');
birthdayInput.setAttribute('max', year + '-' + month + '-' + day);

// request function
const registerUser = async(event) => {

    // checking for empty inputs
    if (!checkEmptyForm()) {
        return;
    }

    const queryParams = '/add';
    const endpoint = url + queryParams;
    const data = {
        userName: userNameInput.value,
        password: passwordInput.value,
        firstName: firstNameInput.value,
        lastName: lastNameInput.value,
        birthday: birthdayInput.value,
        mail: mailInput.value
    }

    // checking for strength of password
    // 8 digits, numbers + alpha + 1x other, 

    // checking if password inputs are the same
    if (passwordInput.value != passwordInputRepeat.value) {
        output.innerHTML += 'Passwords dont match.';
        return;
    }

    // fetch
    try {
        const response = await fetch(endpoint, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        })
        const jsonResponse = await response.json();

        if (response.status == 226) {
            output.innerHTML += jsonResponse.message;
        } else if (response.ok) {
            output.innerHTML += 'Successfully registered!';
        } else {
            output.innerHTML += jsonResponse.message;
        }
    } catch(error) {
        output.innerHTML += error;
    }

}

// checks form for mistakes like empty or invalid fields
const checkEmptyForm = () => {
    if (firstNameInput.value == '') return false;
    
    if (lastNameInput.value == '') return false;
    
    if (birthdayInput.value == '') return false;

    if (mailInput.value == '') return false;

    if (userNameInput.value == '') return false;

    if (passwordInput.value == '') return false;
    
    if (passwordInputRepeat.value == '') return false;
    
    if (!mailInput.checkValidity()) return false;

    if (!birthdayInput.checkValidity()) return false;
    return true;
}

submitSignIn.onclick = registerUser;