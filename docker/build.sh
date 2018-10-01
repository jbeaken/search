# client
set -x
rm -rf client/svc

mkdir client/svc

unzip -d client/svc /home/git/address-api/demo-ui/target/universal/address-index-demo-ui-c10ab6f508bba46bd6b1050fe3c775193fb72683-SNAPSHOT.zip

mv client/svc/*/* client/svc/

rm client/svc/bin/*.bat

mv client/svc/bin/* client/svc/bin/start
docker build -t client client/.

#server
rm -rf server/svc

unzip -d server/svc /home/git/address-api/server/target/universal/address-index-server-c10ab6f508bba46bd6b1050fe3c775193fb72683-SNAPSHOT.zip

mv server/svc/*/* server/svc/

rm server/svc/bin/*.bat

mkdir -p server/svc/parsers/src/main/resources

cp -r  /home/git/address-api//parsers/src/main/resources/* server/svc/parsers/src/main/resources/

mv server/svc/bin/* server/svc/bin/start

chmod +x server/svc/bin/start

docker build -t server server/.

