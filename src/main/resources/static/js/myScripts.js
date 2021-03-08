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