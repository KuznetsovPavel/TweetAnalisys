function printCsv(cursor){
    if(cursor.hasNext()){
        var item = cursor.next();
        printKeys(item);
        printLine(item);
        while(cursor.hasNext()){
            item = cursor.next();
            printLine(item);
        }
    }
}

function printLine(item){
    var values = []; 
    for(var key in item){
        values.push(item[key])
    }
    print(values.join());
}

function printKeys(item){
    print(Object.keys(item).join());
}