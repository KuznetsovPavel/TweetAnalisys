function group_by_prog_lang(language) {
    if (language === undefined) {
        return db.thematicTweets.aggregate([
            { 
                $group: {        
                    _id: "$forQuery",        
                    count: {$sum: 1}              
                }   
            },    
            { $sort: {count: -1} }, 
            { 
                $project: {
                    _id: 0, 
                    progLang: "$_id", 
                    count: "$count"
                }    
            } 
        ]);
    }
    else {
        return db.thematicTweets.aggregate([
            { $match: {languageCode: language}},
            { 
                $group: {        
                    _id: "$forQuery",        
                    count: {$sum: 1}              
                }   
            },    
            { $sort: {count: -1} }, 
            { 
                $project: {
                    _id: 0, 
                    progLang: "$_id", 
                    count: "$count"
                }    
            } 
        ]);
    }
}; 
