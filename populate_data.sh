#!/usr/bin/env bash

function get_field_from_json {
    temp=`echo $1 | sed 's/\\\\\//\//g' | sed 's/[{}]//g' | awk -v k="text" '{n=split($0,a,","); for (i=1; i<=n; i++) print a[i]}' | sed 's/\"\:\"/\|/g' | sed 's/[\,]/ /g' | sed 's/\"//g' | grep -w $2`
    echo ${temp##*|}
}

FILE=output.txt
if [ -f ${FILE} ];
then
   rm output.txt
fi

echo "Results" >> output.txt
echo "" >> output.txt

echo "ADMIN USERS:" >> output.txt
JSON=$(curl -sX POST "http://localhost:8080/users" -H "Content-Type: application/json" -d '{"user_name":"Admin","user_password":"admin123;"}')
echo "User: Pablo" >> output.txt
echo "Pass: pablo123" >> output.txt
USER_ID_ADMIN=$(get_field_from_json ${JSON} "user_id")
echo "ID: ${USER_ID_ADMIN}" >> output.txt
JSON=$(curl -sX POST "http://localhost:8080/users/authenticate" -H "Content-Type: application/json" -d '{"user_name":"Admin","user_password":"admin123;"}')
USER_TOKEN_ADMIN=$(get_field_from_json ${JSON} "access_token")
echo "TOKEN: "${USER_TOKEN_ADMIN} >> output.txt
echo "" >> output.txt

echo "NORMAL USERS:" >> output.txt
JSON=$(curl -sX POST "http://localhost:8080/users" -H "Content-Type: application/json" -d '{"user_name":"Pablo","user_password":"pablo123;"}')
echo "User: Pablo" >> output.txt
echo "Pass: pablo123" >> output.txt
USER_ID_1=$(get_field_from_json ${JSON} "user_id")
echo "ID: ${USER_ID_1}" >> output.txt
JSON=$(curl -sX POST "http://localhost:8080/users/authenticate" -H "Content-Type: application/json" -d '{"user_name":"Pablo","user_password":"pablo123;"}')
USER_TOKEN_1=$(get_field_from_json ${JSON} "access_token")
echo "TOKEN: "${USER_TOKEN_1} >> output.txt
echo "" >> output.txt

JSON=$(curl -sX POST "http://localhost:8080/users" -H "Content-Type: application/json" -d '{"user_name":"Nico","user_password":"nico123;"}')
echo "User: Nico" >> output.txt
echo "Pass: nico123" >> output.txt
USER_ID_2=$(get_field_from_json ${JSON} "user_id")
echo "ID: ${USER_ID_1}" >> output.txt
JSON=$(curl -sX POST "http://localhost:8080/users/authenticate" -H "Content-Type: application/json" -d '{"user_name":"Nico","user_password":"nico123;"}')
USER_TOKEN_2=$(get_field_from_json ${JSON} "access_token")
echo "TOKEN: "${USER_TOKEN_2} >> output.txt
echo "" >> output.txt

echo "TEAMS:" >> output.txt
JSON=$(curl -sX POST "http://localhost:8080/users/${USER_ID_1}/teams?access_token=${USER_TOKEN_1}" -H "Content-Type: application/json" -d '{"team_name":"PabloTeam"}')
echo "Team: PabloTeam" >> output.txt
TEAM_ID_1=$(get_field_from_json ${JSON} "team_id")
echo "ID: ${TEAM_ID_1}" >> output.txt
echo "" >> output.txt

JSON=$(curl -sX POST "http://localhost:8080/users/${USER_ID_2}/teams?access_token=${USER_TOKEN_2}" -H "Content-Type: application/json" -d '{"team_name":"NicoTeam"}')
echo "Team: NicoTeam" >> output.txt
TEAM_ID_2=$(get_field_from_json ${JSON} "team_id")
echo "ID: ${TEAM_ID_2}" >> output.txt
echo "" >> output.txt

echo "CHARACTERS:" >> output.txt
JSON=$(curl -sX POST "http://localhost:8080/users/${USER_ID_1}/teams/${TEAM_ID_1}/characters?access_token=${USER_TOKEN_1}" -H "Content-Type: application/json" -d '{"id":1011334,"name":"3-D Man","description":"","elected_times":0,"thumbnail":{"path":"http://i.annihil.us/u/prod/marvel/i/mg/c/e0/535fecbbb9784","extension":"jpg"}}')
JSON=$(curl -sX POST "http://localhost:8080/users/${USER_ID_2}/teams/${TEAM_ID_2}/characters?access_token=${USER_TOKEN_2}" -H "Content-Type: application/json" -d '{"id":1011334,"name":"3-D Man","description":"","elected_times":0,"thumbnail":{"path":"http://i.annihil.us/u/prod/marvel/i/mg/c/e0/535fecbbb9784","extension":"jpg"}}')
JSON=$(curl -sX POST "http://localhost:8080/users/${USER_ID_1}/characters/favorites?access_token=${USER_TOKEN_1}" -H "Content-Type: application/json" -d '{"id":1011334,"name":"3-D Man","description":"","elected_times":0,"thumbnail":{"path":"http://i.annihil.us/u/prod/marvel/i/mg/c/e0/535fecbbb9784","extension":"jpg"}}')
JSON=$(curl -sX POST "http://localhost:8080/users/${USER_ID_2}/characters/favorites?access_token=${USER_TOKEN_2}" -H "Content-Type: application/json" -d '{"id":1011334,"name":"3-D Man","description":"","elected_times":0,"thumbnail":{"path":"http://i.annihil.us/u/prod/marvel/i/mg/c/e0/535fecbbb9784","extension":"jpg"}}')
echo "Character: 3-D Man" >> output.txt
echo "ID: 1011334" >> output.txt
echo "Elected times as favorite: 2" >> output.txt
echo "" >> output.txt

JSON=$(curl -sX POST "http://localhost:8080/users/${USER_ID_1}/teams/${TEAM_ID_1}/characters?access_token=${USER_TOKEN_1}" -H "Content-Type: application/json" -d '{"id":1011266,"name":"Adam Destine","description":"","elected_times":0,"thumbnail":{"path":"http://i.annihil.us/u/prod/marvel/i/mg/b/40/image_not_available","extension":"jpg"}}')
JSON=$(curl -sX POST "http://localhost:8080/users/${USER_ID_2}/teams/${TEAM_ID_2}/characters?access_token=${USER_TOKEN_2}" -H "Content-Type: application/json" -d '{"id":1011266,"name":"Adam Destine","description":"","elected_times":0,"thumbnail":{"path":"http://i.annihil.us/u/prod/marvel/i/mg/b/40/image_not_available","extension":"jpg"}}')
JSON=$(curl -sX POST "http://localhost:8080/users/${USER_ID_1}/characters/favorites?access_token=${USER_TOKEN_1}" -H "Content-Type: application/json" -d '{"id":1011266,"name":"Adam Destine","description":"","elected_times":0,"thumbnail":{"path":"http://i.annihil.us/u/prod/marvel/i/mg/b/40/image_not_available","extension":"jpg"}}')
echo "Character: Adam Destine" >> output.txt
echo "ID: 1011266" >> output.txt
echo "Elected times as favorite: 1" >> output.txt
echo "" >> output.txt

JSON=$(curl -sX POST "http://localhost:8080/users/${USER_ID_2}/teams/${TEAM_ID_2}/characters?access_token=${USER_TOKEN_2}" -H "Content-Type: application/json" -d '{"id":1010903,"name":"Abyss (Age of Apocalypse)","description":"","elected_times":0,"thumbnail":{"path":"http://i.annihil.us/u/prod/marvel/i/mg/3/80/4c00358ec7548","extension":"jpg"}}')
echo "Character: Abyss (Age of Apocalypse)" >> output.txt
echo "ID: 1010903" >> output.txt
echo "Elected times as favorite: 0" >> output.txt
echo "" >> output.txt

JSON=$(curl -sX POST "http://localhost:8080/users/${USER_ID_1}/teams/${TEAM_ID_1}/characters?access_token=${USER_TOKEN_1}" -H "Content-Type: application/json" -d '{"id":1009146,"name":"Abomination (Emil Blonsky)","description":"Formerly known as Emil Blonsky, a spy of Soviet Yugoslavian origin working for the KGB, the Abomination gained his powers after receiving a dose of gamma radiation similar to that which transformed Bruce Banner into the incredible Hulk.","elected_times":0,"thumbnail":{"path":"http://i.annihil.us/u/prod/marvel/i/mg/9/50/4ce18691cbf04","extension":"jpg"}}')
echo "Character: Abomination (Emil Blonsky)" >> output.txt
echo "ID: 1009146" >> output.txt
echo "Elected times as favorite: 0" >> output.txt
echo "" >> output.txt