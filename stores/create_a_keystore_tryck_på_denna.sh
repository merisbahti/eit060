echo '============================'
echo 'Skapar client side nyckelpar'
echo '============================'

#skapa ett nyckelpar
keytool -genkeypair -keyalg RSA -keystore clientkeystore -alias keystore  

echo '============================'
echo '!SKAPAR EN CSR FÖR KEYSTORE!'
echo '============================'

#skapa en CSR för keystore
keytool -certreq -keystore clientkeystore -file keystore.csr -alias keystore 

echo '==================================='
echo '!SKAPAR EN TRUSTSTORE FÖR KEYSTORE!'
echo '==================================='

#skapa truststore för klient
keytool -import -file CA.crt -alias firstCA -keystore clienttruststore 

echo '=============================='
echo 'Signerar client side nyckelpar'
echo '  Vad vänlig ange lösenord:   '
echo '=============================='

#signera keystore med CA
openssl x509 -req -in keystore.csr -CA CA.crt -CAkey CA.key -out keystore.crt -CAcreateserial

echo '==================================='
echo '!IMPORTERA CLAES-ANDERS I KEYSTORE!'
echo '==================================='

#importera CA
keytool -importcert -file CA.crt -alias rootca -keystore clientkeystore 

echo '==================================='
echo '!!!!IMPORT SIG SJÄLV I KEYSTORE!!!!'
echo '==================================='

#importera sitt eget cert
keytool -importcert -file keystore.crt -alias keystore -keystore clientkeystore 

echo '=================================='
echo '!!!!CHECK THAT SHIT I KEYSTORE!!!!'
echo '=================================='

#check
keytool -list -v -keystore clientkeystore 

rm *.csr
rm keystore.crt
