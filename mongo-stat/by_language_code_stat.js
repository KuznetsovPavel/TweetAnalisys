function group_by_language_code(query) {
    if (query === undefined) {
        return db.thematicTweets.aggregate([
            { 
                $group: {        
                    _id: "$languageCode",        
                    count: {$sum: 1}              
                }   
            },    
            { $sort: {count: -1} }, 
            { 
                $project: {
                    _id: 0, 
                    languageCode: "$_id", 
                    count: "$count"
                }    
            } 
        ]);
    }
    else {
        return db.thematicTweets.aggregate([
            { $match: {forQuery: query}},
            { 
                $group: {        
                    _id: "$languageCode",        
                    count: {$sum: 1}              
                }   
            },    
            { $sort: {count: -1} }, 
            { 
                $project: {
                    _id: 0, 
                    languageCode: "$_id", 
                    count: "$count"
                }    
            } 
        ]);
    }
};