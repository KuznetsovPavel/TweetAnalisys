function group_by_retweet_count(query)
    if(query === undefined) {
        return db.thematicTweets.aggregate([
            { $match: {text: {$not: /^RT/}}},
            { 
                $group: {        
                    _id: "$retweetCount",        
                    count: {$sum: 1}              
                }   
            },    
            { $sort: {count: -1} }, 
            { 
                $project: {
                    _id: 0, 
                    retweetCount: "$_id", 
                    count: "$count"
                }    
            } 
        ]);
    }
    else {
        return db.thematicTweets.aggregate([
            { 
                $match: {
                    forQuery: query, 
                    text: {$not: /^RT/}
                }
            },
            { 
                $group: {        
                    _id: "$retweetCount",        
                    count: {$sum: 1}              
                }   
            },    
            { $sort: {count: -1} }, 
            { 
                $project: {
                    _id: 0, 
                    retweetCount: "$_id", 
                    count: "$count"
                }    
            } 
        ]);
    }