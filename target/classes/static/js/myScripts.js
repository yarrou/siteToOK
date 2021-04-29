function funcDel(){
    var isDel=prompt("введите \"удалить\" для удаления");
    if(isDel!="удалить"){
        alert("операция отменена");
    }
    return isDel=="удалить";
}
function funcConfirmation(){
    var isConfirmation=prompt("введите \"подтверждаю\" для выполнения операции");
    if(isConfirmation!="подтверждаю"){
        alert("операция отменена");
    }
    return isConfirmation=="подтверждаю";
}
function loginCheck(){
     if (document.formLogin.username.value == "" && document.formLogin.password.value == "") {
            alert("Необходимо ввести имя пользователя и пароль");
            document.formLogin.username.focus();
            return false;
     }
     if (document.formLogin.username.value == "") {
            alert("необходимо ввести имя пользователя");
            document.formLogin.username.focus();
            return false;
     }
     if (document.formLogin.password.value == "") {
        alert("необходимо ввести пароль");
        document.formLogin.password.focus();
        return false;
     }
     else{return true;}
}
