# Secure-Email-Communication-using-RSA-and-PGP

<h3> Sender Side </h3>

On this side, first we scan the plain text from the user. After this, we generate a random session key which is also used to encrypt the plain text. The session key changes every time the program is executed. After generating the session key, we use this to run the block cipher and obtain cipher text. After the encrypted text is obtained, we put the cipher text in the hash function to obtain a 5-bit hash digest. Then, the session key is encrypted with the help of RSA. To encrypt, we use the Receiver’s public key so that the receiver can decrypt the encrypted key with their private key. After the encrypted session key is obtained, we take the first 4 characters of the hash digest and run them through RSA system. But this time, we use the sender’s private key to sign the digest. This signature is later verified by receiver using the sender’s public key. After the signed hash is also obtained, we append all the resultants. For example, our message consists of:
103//187470143//155840672//vwlyb
Where 103 is the public key of the sender, 187470143 is the Signature hash, 155840672 is the encrypted session key and vwlyb is the encrypted text.

<h3> Receiver Side </h3>

On this side, first we scan the sender message mentioned above. The receiver should copy the message sent by the sender (like the one above) and the program will verify the signature and decrypt the cipher text if signature is verified. In the first step, we divide the parts of the message and categorize them accordingly. Then, First we take the encrypted text and perform the hash function on the text. We obtain a 5 bit hash digest which is known as the original hash value. Then, we use sender’s public key to obtain the hash value from the signature value. This resultant is known as the current hash value. If the original hash value and the current hash value is the same, then the signature is said to be authentic. Then, we use the receiver’s private key to decrypt the encrypted session key. After obtaining this session key, we use it to decrypt the encrypted text sent by the sender.
