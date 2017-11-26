if [ ! -d out ]; then
  mkdir out
fi

cd out
if [ ! -d by_language_code_stat ]; then
  mkdir by_language_code_stat
fi
cd ..
mongo tweetdata --eval "load('print.js'); load('by_language_code_stat.js'); var cursor = group_by_language_code(); printCsv(cursor);" \
> out/by_language_code_stat/all.csv --quiet
queryList=$(mongo tweetdata --eval "load('by_prog_lang_stat.js'); var cursor = group_by_prog_lang(); var list = []; cursor.forEach(function(item){list.push(item.progLang)}); print(list.join(' '))" --quiet)
for query in $queryList
do
    mongo tweetdata --eval "load('print.js'); load('by_language_code_stat.js'); var cursor = group_by_language_code('$query'); printCsv(cursor);" \
    > out/by_language_code_stat/$query.csv --quiet
done

cd out
if [ ! -d by_prog_lang_stat ]; then
  mkdir by_prog_lang_stat
fi
cd ..
mongo tweetdata --eval "load('print.js'); load('by_prog_lang_stat.js'); var cursor = group_by_prog_lang(); printCsv(cursor);" \
> out/by_prog_lang_stat/all.csv --quiet
queryList=$(mongo tweetdata --eval "load('by_language_code_stat.js'); var cursor = group_by_language_code(); var list = []; cursor.forEach(function(item){list.push(item.languageCode)}); print(list.join(' '))" --quiet)
count=0
for query in $queryList
do
    mongo tweetdata --eval "load('print.js'); load('by_prog_lang_stat.js'); var cursor = group_by_prog_lang('$query'); printCsv(cursor);" \
    > out/by_prog_lang_stat/$query.csv --quiet
    count=$(( $count + 1 ))
    if [ $count -gt 20 ]; then
        break
    fi
done

cd out
if [ ! -d by_retweet_count_stat ]; then
  mkdir by_retweet_count_stat
fi
cd ..
mongo tweetdata --eval "load('print.js'); load('by_retweet_count_stat.js'); var cursor = group_by_retweet_count(); printCsv(cursor);" \
> out/by_retweet_count_stat/all.csv --quiet
for query in $queryList
do
    mongo tweetdata --eval "load('print.js'); load('by_retweet_count_stat.js'); var cursor = group_by_retweet_count('$query'); printCsv(cursor);" \
    > out/by_retweet_count_stat/$query.csv --quiet
done

cd out
if [ ! -d by_favorite_count_stat ]; then
  mkdir by_favorite_count_stat
fi
cd ..
mongo tweetdata --eval "load('print.js'); load('by_favorite_count_stat.js'); var cursor = group_by_favorite_count(); printCsv(cursor);" \
> out/by_favorite_count_stat/all.csv --quiet
for query in $queryList
do
    mongo tweetdata --eval "load('print.js'); load('by_favorite_count_stat.js'); var cursor = group_by_favorite_count('$query'); printCsv(cursor);" \
    > out/by_favorite_count_stat/$query.csv --quiet
done